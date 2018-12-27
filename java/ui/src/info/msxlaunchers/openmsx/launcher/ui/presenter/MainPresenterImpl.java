/*
 * Copyright 2014 Sam Elsharif
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.common.version.VersionUtils;
import info.msxlaunchers.openmsx.game.repository.RepositoryData;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.filter.Filter;
import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.log.LogEvent;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterSetNotFoundException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.starter.EmulatorStarter;
import info.msxlaunchers.openmsx.launcher.ui.view.MainView;
import info.msxlaunchers.platform.FileLocator;

/**
 * Implementation of <code>MainPresenter</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@Singleton
final class MainPresenterImpl implements MainPresenter
{
	private final MainView view;
	private final Provider<SettingsPresenter> settingsPresenterFactory;
	private final ProfileEditingPresenterFactory profileEditingPresenterFactory;
	private final Provider<ScannerPresenter> scannerPresenterFactory;
	private final Provider<FilterEditingPresenter> filterEditingPresenterFactory;
	private final Provider<GamePropertiesPresenter> gamePropertiesPresenterFactory;
	private final Provider<BlueMSXLauncherDatabasesImporterPresenter> blueMSXLauncherImporterPresenterFactory;
	private final DatabaseManagerPresenterFactory databaseManagerPresenterFactory;
	private final Provider<UpdateCheckerPresenter> updateCheckerPresenterFactory;
	private final LauncherPersistence launcherPersistence;
	private final EmulatorStarter emulatorStarter;
	private final ExtraDataGetter extraDataGetter;
	private final String helpFileDirectory;
	private final RepositoryData repositoryData;
	private final FileLocator fileLocator;
	private final DraggedAndDroppedGamesPresenterFactory draggedAndDroppedGamesPresenterFactory;
	private final Provider<ActivityViewerPresenter> activityViewerPresenterFactory;
	private final Provider<PatcherPresenter> patcherPresenterFactory;
	private final MachineUpdatePresenterFactory machineUpdatePresenterFactory;
	private final FeedServicePresenter feedServicePresenter;

	private static final String SCREENSHOT_EXT = ".png";
	private static final String SCREENSHOT1_SUFFIX = "a";
	private static final String SCREENSHOT2_SUFFIX = "b";

	private static final String DEFAULT_SYSTEM_LANGUAGE = "SYSTEM_DEFAULT";
	private static final int MAX_SEARCH_MATCHES = 10;

	//the following fields represent the model
	private Settings settings;

	private Language currentLanguage;
	private String currentLanguageCode;
	private boolean currentRightToLeft;
	private boolean showUpdateAllDatabases;

	private Set<String> databases = null;
	private String currentDatabase = null;;
	private Map<String,Game> gamesMap = null;
	private String openMSXMachinesFullPath = null;
	private Map<String,RepositoryGame> repositoryInfoMap;

	private Set<Filter> currentFilter = null;
	private String currentFilterName = null;
	private boolean untitledFilter = false;
	private boolean filterEditMode = false;

	@Inject
	MainPresenterImpl( MainView view,
			Provider<SettingsPresenter> settingsPresenterFactory,
			ProfileEditingPresenterFactory profileEditingFactory,
			Provider<ScannerPresenter> scannerPresenterFactory,
			Provider<FilterEditingPresenter> filterEditingPresenterFactory,
			Provider<GamePropertiesPresenter> gamePropertiesPresenterFactory,
			Provider<BlueMSXLauncherDatabasesImporterPresenter> blueMSXLauncherImporterPresenterFactory,
			DatabaseManagerPresenterFactory databaseManagerPresenterFactory,
			Provider<ActivityViewerPresenter> activityViewerPresenterFactory,
			Provider<UpdateCheckerPresenter> updateCheckerPresenterFactory,
			LauncherPersistence launcherPersistence,
			EmulatorStarter emulatorStarter,
			ExtraDataGetter extraDataGetter,
			@Named("LauncherDataDirectory") String extraDataDirectory,
			RepositoryData repositoryData,
			FileLocator fileLocator,
			DraggedAndDroppedGamesPresenterFactory draggedAndDroppedGamesPresenterFactory,
			Provider<PatcherPresenter> patcherPresenterFactory,
			MachineUpdatePresenterFactory machineUpdatePresenterFactory,
			FeedServicePresenter feedServicePresenter ) throws IOException
	{
		this.view = Objects.requireNonNull( view );
		this.settingsPresenterFactory = Objects.requireNonNull( settingsPresenterFactory );
		this.profileEditingPresenterFactory = Objects.requireNonNull( profileEditingFactory );
		this.scannerPresenterFactory = Objects.requireNonNull( scannerPresenterFactory );
		this.filterEditingPresenterFactory = Objects.requireNonNull( filterEditingPresenterFactory );
		this.gamePropertiesPresenterFactory = Objects.requireNonNull( gamePropertiesPresenterFactory );
		this.blueMSXLauncherImporterPresenterFactory = Objects.requireNonNull( blueMSXLauncherImporterPresenterFactory );
		this.databaseManagerPresenterFactory = Objects.requireNonNull( databaseManagerPresenterFactory );
		this.updateCheckerPresenterFactory = Objects.requireNonNull( updateCheckerPresenterFactory );
		this.launcherPersistence = Objects.requireNonNull( launcherPersistence );
		this.emulatorStarter = Objects.requireNonNull( emulatorStarter );
		this.extraDataGetter = Objects.requireNonNull( extraDataGetter );
		this.helpFileDirectory = extraDataDirectory;
		this.repositoryData = Objects.requireNonNull( repositoryData );
		this.fileLocator = Objects.requireNonNull( fileLocator );
		this.draggedAndDroppedGamesPresenterFactory = Objects.requireNonNull( draggedAndDroppedGamesPresenterFactory );
		this.activityViewerPresenterFactory = Objects.requireNonNull( activityViewerPresenterFactory );
		this.patcherPresenterFactory = Objects.requireNonNull( patcherPresenterFactory );
		this.machineUpdatePresenterFactory = Objects.requireNonNull( machineUpdatePresenterFactory );
		this.feedServicePresenter = Objects.requireNonNull( feedServicePresenter );

		try
		{
			launcherPersistence.initialize();
		}
		catch( LauncherPersistenceException lpe )
		{
			//This gets called at application initialization time, so if it fails then log it and rethrow
			LauncherLogger.logException( this, lpe );

			throw new IOException();
		}

		try
		{
			this.settings = launcherPersistence.getSettingsPersister().getSettings();
		}
		catch ( IOException ioe )
		{
			//This gets called at application initialization time, so if it fails then log it and rethrow
			LauncherLogger.logException( this, ioe );

			throw ioe;
		}

		setLanguageParameters( settings.getLanguage() );

		databases = launcherPersistence.getGamePersister().getDatabases().stream()
				.collect( Collectors.toCollection( () -> new TreeSet<String>( (db1, db2) -> db1.compareToIgnoreCase( db2 ) ) ) );

		String defaultDatabase = settings.getDefaultDatabase();
		if( defaultDatabase == null )
		{
			//in this case there is no default database in the settings => just select the first database in the list.
			//if list is empty then nothing will be selected anyway
			if( !databases.isEmpty() )
			{
				currentDatabase = databases.iterator().next();
			}
		}
		else if( databases.contains( defaultDatabase ) )
		{
			currentDatabase = defaultDatabase;
		}
		else
		{
			//if the default database from the settings is not in database list (maybe because it was renamed but not updated in the settings)
			//then the current database will remain null
		}

		if( currentDatabase != null )
		{
			try
			{
				retrieveDatabaseGames( currentDatabase );
			}
			catch ( GamePersistenceException gpe )
			{
				//This gets called at application initialization time, so if it fails then just log it
				LauncherLogger.logException( this, gpe );
			}
		}

		this.showUpdateAllDatabases = settings.isShowUpdateAllDatabases();

		if( settings.isEnableFeedService() )
		{
			this.feedServicePresenter.startService();
		}

		openMSXMachinesFullPath = settings.getOpenMSXMachinesFullPath();
		initializeRepositoryInfoMap();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#start()
	 */
	@Override
	public void start()
	{
		view.displayMain( currentLanguage, getSortedGameList(), databases, currentDatabase, currentRightToLeft, showUpdateAllDatabases,
				settings.isEnableFeedService() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestSettingsScreen()
	 */
	@Override
	public void onRequestSettingsScreen()
	{
		settingsPresenterFactory.get().onRequestSettingsScreen( settings, launcherPersistence.getGamePersister().getDatabases(),
				currentLanguage, currentLanguageCode, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onAcceptSettingsAction(info.msxlaunchers.openmsx.launcher.data.settings.Settings)
	 */
	@Override
	public void onAcceptSettingsAction( Settings newSettings ) throws LauncherException
	{
		Settings oldSettings = this.settings;
		this.settings = newSettings;

		//check if the openMSX path changed so we reload the Repository Info Map
		if( !Utils.equalStrings( this.openMSXMachinesFullPath, settings.getOpenMSXMachinesFullPath() ) )
		{
			this.openMSXMachinesFullPath = settings.getOpenMSXMachinesFullPath();
			initializeRepositoryInfoMap();
		}

		//only do the following if the a new language was selected
		Language oldLanguage = oldSettings.getLanguage();
		Language newLanguage = settings.getLanguage();

		if( (oldLanguage == null && newLanguage != null) ||
				(oldLanguage != null && newLanguage == null) ||
				(oldLanguage != null && newLanguage != null && !oldLanguage.equals( newLanguage )) )
		{
			boolean oldRightToLeft = this.currentRightToLeft;
			setLanguageParameters( newLanguage );

			if( oldRightToLeft && Language.isLeftToRight( currentLanguage ) )
			{
				view.flipOrientationLeftToRight();
			}
			else if( Language.isRightToLeft( currentLanguage ) && !oldRightToLeft )
			{
				view.flipOrientationRightToLeft();
			}

			view.refreshLanguage( currentLanguage );

			if( untitledFilter )
			{
				view.setFilterNameLabelUntitled();
			}
		}

		if( newSettings.isEnableFeedService() )
		{
			feedServicePresenter.startService();
		}
		else
		{
			feedServicePresenter.stopService();
		}
		view.enableFeedAccess( newSettings.isEnableFeedService() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onSelectDatabase(java.lang.String)
	 */
	@Override
	public void onSelectDatabase( String database ) throws LauncherException
	{
		if( database != null && !database.equals( currentDatabase ) )
		{
			try
			{
				retrieveDatabaseGames( database );
			}
			catch( GamePersistenceException gpe )
			{
				if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) )
				{
					throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, database );
				}
				else
				{
					throw new LauncherException( LauncherExceptionCode.ERR_IO );
				}
			}

			currentDatabase = database;
			view.fillGameList( database, getSortedGameList(), null );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onLaunchGame(java.lang.String)
	 */
	@Override
	public void onLaunchGame( String gameName ) throws LauncherException
	{
		try
		{
			emulatorStarter.start( settings, gamesMap.get( gameName ) );

			LauncherLogger.logMessage( LogEvent.LAUNCH, gameName + "[" + currentDatabase + "]" );
		}
		catch ( IOException e )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_START_OPENMSX );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestAddGameScreen()
	 */
	@Override
	public void onRequestAddGameScreen() throws LauncherException
	{
		if( currentDatabase == null )
		{
			//this means that no database is currently selected - ignore
		}
		else
		{
			profileEditingPresenterFactory.create( settings, currentDatabase, null ).onRequestAddGameScreen( currentLanguage, Language.isRightToLeft( currentLanguage ) );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestEditGameScreen(java.lang.String)
	 */
	@Override
	public void onRequestEditGameScreen( String gameName ) throws LauncherException
	{
		Game game = gamesMap.get( gameName );

		profileEditingPresenterFactory.create( settings, currentDatabase, game ).onRequestEditGameScreen( currentLanguage, Language.isRightToLeft( currentLanguage ) );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onAcceptAddGameSaveAction(info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public void onAcceptAddGameSaveAction( Game game ) throws LauncherException
	{
		gamesMap.put( game.getName(), game );
		view.fillGameList( currentDatabase, getSortedGameList(), game.getName() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onAcceptEditGameSaveAction(java.lang.String, info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public void onAcceptEditGameSaveAction( String oldName, Game game ) throws LauncherException
	{
		gamesMap.remove( oldName );
		gamesMap.put( game.getName(),  game );
		view.fillGameList( currentDatabase, getSortedGameList(), game.getName() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestRemoveGamesAction(java.util.Set)
	 */
	@Override
	public void onRequestRemoveGamesAction( Set<String> gameNames ) throws LauncherException
	{
		//create a Game set from the game names
		Set<Game> games = new HashSet<Game>();

		gameNames.forEach( gameName -> {
			games.add( gamesMap.get( gameName ) );
			gamesMap.remove( gameName );
			} );

		try
		{
			launcherPersistence.getGamePersister().deleteGames( games, currentDatabase );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.GAME_NOT_FOUND ) )
			{
				//maybe the game didn't exist - don't really need to process that
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, currentDatabase );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}

		view.updateGameCount( gamesMap.size() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onSelectGames(java.util.Set)
	 */
	@Override
	public void onSelectGames( Set<String> gameNames )
	{
		if( currentDatabase == null )
		{
			//this happens when the last database is deleted
			resetAll();
		}
		else if( gameNames == null || gameNames.size() != 1 )
		{
			//this is the multi selection case
			view.showGameScreenshots( null, null );
			view.enableButtons( false, true, true, false, false );
			view.enableSoundIndicators( false, false, false, false, false, false, false, false );
			view.enableGenerationIndicators( false,  false,  false,  false );
		}
		else
		{
			//this is the single selection case
			Game game = gamesMap.get( gameNames.iterator().next() );

			String screenshot1 = null;
			String screenshot2 = null;

			if( game != null )
			{
				String screenshotsPath = settings.getScreenshotsFullPath();
				if( screenshotsPath != null )
				{
					int msxGenID = game.getMsxGenID();
					if( msxGenID > 0 )
					{
						StringBuilder screenshot1Filename = new StringBuilder( Utils.getString( msxGenID ) )
															.append( SCREENSHOT1_SUFFIX );
						String screenshotSuffix = game.getScreenshotSuffix();
						if( screenshotSuffix != null )
						{
							screenshot1Filename.append( screenshotSuffix );
						}
						screenshot1Filename.append( SCREENSHOT_EXT );

						File screenshot1File = new File( screenshotsPath, screenshot1Filename.toString() );
						if( screenshot1File.isFile() )
						{
							screenshot1 = screenshot1File.toString();
	
							StringBuilder screenshot2Filename = new StringBuilder( Utils.getString( msxGenID ) )
																.append( SCREENSHOT2_SUFFIX );
							if( screenshotSuffix != null )
							{
								screenshot2Filename.append( screenshotSuffix );
							}
							screenshot2Filename.append( SCREENSHOT_EXT );
	
							File screenshot2File = new File( screenshotsPath, screenshot2Filename.toString() );
							if( screenshot2File.isFile() )
							{
								screenshot2 = screenshot2File.toString();
							}
						}
					}
	
					view.showGameScreenshots( screenshot1, screenshot2  );
				}
				else
				{
					view.showGameScreenshots( null, null  );
				}
				
				//enable buttons and indicators according to the selection
				view.enableButtons( true,  true, true, true, game.getInfo() != null );
				view.enableSoundIndicators( game.isPSG(), game.isSCC(), game.isSCCI(), game.isPCM(), game.isMSXMUSIC(), game.isMSXAUDIO(),
						game.isMoonsound(), game.isMIDI() );
				view.enableGenerationIndicators( game.isMSX(), game.isMSX2(), game.isMSX2Plus(), game.isTurboR() );
			}
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#resetAll()
	 */
	@Override
	public void resetAll()
	{
		view.showGameScreenshots( null, null );
		view.enableButtons( false, false, currentDatabase != null, false, false );
		view.enableSoundIndicators( false, false, false, false, false, false, false, false );
		view.enableGenerationIndicators( false,  false,  false,  false );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onViewUpdatedDatabase(java.lang.String)
	 */
	@Override
	public void onViewUpdatedDatabase( String database ) throws LauncherException
	{
		if( database != null )
		{
			currentDatabase = database;
		}

		if( !databases.contains( currentDatabase ) )
		{
			//then this is a new database
			databases.add( currentDatabase );
		}

		try
		{
			retrieveDatabaseGames( currentDatabase );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, currentDatabase );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}

		view.fillGameList( currentDatabase, getSortedGameList(), null );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestFillDatabaseScreen()
	 */
	@Override
	public void onRequestFillDatabaseScreen() throws LauncherException
	{
		scannerPresenterFactory.get().onRequestFillDatabaseScreen( databases, currentDatabase, currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestGameInfo(java.lang.String)
	 */
	@Override
	public void onRequestGameInfo( String gameName ) throws LauncherException
	{
		Game game = gamesMap.get( gameName );

		if( game != null )
		{
			String gameInfo = game.getInfo();
			if( gameInfo != null )
			{
				startBrowser( gameInfo, LauncherExceptionCode.ERR_CANNOT_VIEW_GAMEINFO );
			}
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestAboutScreen()
	 */
	@Override
	public void onRequestAboutScreen()
	{
		String extraDataVersion = null;

		try
		{
			extraDataVersion = extraDataGetter.getExtraDataFileVersion();
		}
		catch( IOException ioe )
		{
			//do nothing - just pass an empty string
		}

		String screenshotsPath = null;
		try
		{
			screenshotsPath = launcherPersistence.getSettingsPersister().getSettings().getScreenshotsFullPath();
		}
		catch( IOException ioe )
		{
			//do nothing - just pass null
		}

		String screenshotsVersion = VersionUtils.getScreenshotsVersion( screenshotsPath );

		view.displayAbout( currentLanguage, Language.isRightToLeft( currentLanguage ), extraDataVersion, screenshotsVersion );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestMoveGamesScreen(java.util.Set, java.lang.String)
	 */
	@Override
	public Set<String> onRequestMoveGamesScreen( Set<String> gameNames, String oldDatabase )
	{
		//create a list of databases that excludes the current one
		Set<String> targetDatabases = new HashSet<String>( databases );
		targetDatabases.remove( currentDatabase );

		return view.displayAndGetMoveGames( currentLanguage, gameNames, oldDatabase, targetDatabases,
				Language.isRightToLeft( currentLanguage ) );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestMoveGamesAction(java.util.Set, java.lang.String, java.lang.String)
	 */
	@Override
	public Set<String> onRequestMoveGamesAction( Set<String> gameNames, String oldDatabase, String newDatabase )
			 throws LauncherException
	{
		//create a Game set from the game names
		Set<Game> games = new HashSet<Game>();

		gameNames.forEach( gameName -> games.add( gamesMap.get( gameName ) ) );

		Set<String> movedGameNames = new HashSet<String>();

		try
		{
			Set<Game> movedGames = launcherPersistence.getGamePersister().moveGames( games, oldDatabase, newDatabase,
					new MoveGameActionDecider( view, currentLanguage, Language.isRightToLeft( currentLanguage ) ) );

			movedGames.forEach( movedGame -> {
				movedGameNames.add( movedGame.getName() );
				gamesMap.remove( movedGame.getName() );
			});
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.GAME_NOT_FOUND ) )
			{
				//don't have the game name
				throw new LauncherException( LauncherExceptionCode.ERR_GAME_NOT_FOUND );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) )
			{
				//don't know which one was not found
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME) )
			{
				//don't have the game with null name
				throw new LauncherException( LauncherExceptionCode.ERR_GAME_WITH_NULL_NAME );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}

		view.updateGameCount( gamesMap.size() );

		return movedGameNames;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestCreateEmptyDatabaseScreen()
	 */
	@Override
	public void onRequestCreateEmptyDatabaseScreen()
	{
		view.displayCreateEmptyDatabase( currentLanguage, Language.isRightToLeft( currentLanguage ) );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestCreateEmptyDatabaseAction(java.lang.String)
	 */
	@Override
	public void onRequestCreateEmptyDatabaseAction( String newDatabase ) throws LauncherException
	{
		try
		{
			launcherPersistence.getGamePersister().createDatabase( newDatabase );

			databases.add( newDatabase );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NULL_NAME ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NULL_NAME );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_ALREADY_EXISTS, newDatabase );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestHelpFile()
	 */
	@Override
	public void onRequestHelpFile() throws LauncherException
	{
		String helpFileFullPath = new File( helpFileDirectory, "README.html" ).getAbsolutePath();
		startBrowser( helpFileFullPath, LauncherExceptionCode.ERR_CANNOT_VIEW_HELP_FILE );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestLocateFile(java.lang.String)
	 */
	@Override
	public void onRequestLocateFile( String gameName ) throws LauncherException
	{
		Game game = gamesMap.get( gameName );

		File file = new File(FileTypeUtils.getMainFile( game.getRomA(),
				game.getRomB(),
				game.getDiskA(),
				game.getDiskB(),
				game.getTape(),
				game.getHarddisk(),
				game.getLaserdisc(),
				game.getTclScript() ) );

		try
		{
			fileLocator.locateFile( file.getAbsolutePath() );
		}
		catch( IOException e )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestAddFavorite(java.lang.String, java.lang.String)
	 */
	public void onRequestAddFavorite( String gameName, String database ) throws LauncherException
	{
		try
		{
			launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( gameName, database ) );
		}
		catch( FavoritePersistenceException lpe )
		{
			if( lpe.getIssue().equals( FavoritePersistenceExceptionIssue.FAVORITE_ALREADY_EXISTS ) )
			{
				//ignore
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestListOfFavorites()
	 */
	@Override
	public void onRequestListOfFavorites()
	{
		Set<DatabaseItem> favorites =  new TreeSet<>( new DatabaseItemComparator() );

		favorites.addAll( launcherPersistence.getFavoritePersister().getFavorites() );

		view.showFavoritesMenu( favorites );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestDatabasesList()
	 */
	@Override
	public void onRequestDatabasesList()
	{
		view.showDatabasesList( databases );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onSelectDatabaseItem(info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem)
	 */
	@Override
	public void onSelectDatabaseItem( DatabaseItem databaseItem ) throws LauncherException
	{
		onSelectDatabase( databaseItem.getDatabase() );

		view.highlightGame( databaseItem.getGameName() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestDeleteFavoriteAction(info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem)
	 */
	@Override
	public void onRequestDeleteFavoriteAction( DatabaseItem favorite ) throws LauncherException
	{
		try
		{
			launcherPersistence.getFavoritePersister().deleteFavorite( favorite );
		}
		catch( FavoritePersistenceException fpe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestListOfSavedFilters()
	 */
	@Override
	public void onRequestListOfSavedFilters()
	{
		Set<String> filterNames =  launcherPersistence.getFiltersPersister().getFilterNames();

		view.showFiltersMenu( filterNames, currentFilterName != null, untitledFilter );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onSelectFilter(java.lang.String)
	 */
	@Override
	public void onSelectFilter( String filterName ) throws LauncherException
	{
		selectFilter( filterName );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestDeleteFilterAction(java.lang.String)
	 */
	@Override
	public void onRequestDeleteFilterAction( String filterName ) throws LauncherException
	{
		try
		{
			launcherPersistence.getFiltersPersister().deleteFilter( filterName );

			if( filterName.equals( currentFilterName ) )
			{
				selectFilter( null );
			}
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestAddFilterScreen()
	 */
	@Override
	public void onRequestAddFilterScreen() throws LauncherException
	{
		filterEditMode = false;

		filterEditingPresenterFactory.get().onRequestAddFilterScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestEditFilterScreen(java.lang.String)
	 */
	@Override
	public void onRequestEditFilterScreen( String filterName ) throws LauncherException
	{
		Set<Filter> filter = null;

		if( filterName == null )
		{
			filter = currentFilter;
		}
		else
		{
			try
			{
				filter = launcherPersistence.getFiltersPersister().getFilter( filterName );
			}
			catch ( FilterSetNotFoundException fsnfe )
			{
				//should not really happen - just throw a generic IO exception
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}

		filterEditMode = true;

		filterEditingPresenterFactory.get().onRequestEditFilterScreen( currentLanguage, currentRightToLeft, filterName, filter );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onApplyFilter(java.util.Set)
	 */
	@Override
	public void onApplyFilter( Set<Filter> filter ) throws LauncherException
	{
		currentFilter = filter;

		try
		{
			retrieveDatabaseGames( currentDatabase );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}

		view.fillGameList( currentDatabase, getSortedGameList(), null );

		if( !filterEditMode )
		{
			if( filter.size() > 0 )
			{
				untitledFilter = true;
				view.setFilterNameLabelUntitled();
			}
			else
			{
				//this could be the case where in an untitled filter the last filter item was deleted from the Add/Edit table
				untitledFilter = false;
				view.updateFilterNameLabel( null );
			}
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestUpdateFilterName(java.lang.String)
	 */
	@Override
	public void onRequestUpdateFilterName( String filterName )
	{
		updateFilterName( filterName );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestSetFilterNameUntitled()
	 */
	@Override
	public void onRequestSetFilterNameUntitled()
	{
		untitledFilter = true;
		view.setFilterNameLabelUntitled();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestShowProperties(java.lang.String)
	 */
	@Override
	public void onRequestShowProperties( String gameName )
	{
		gamePropertiesPresenterFactory.get().onRequestGamePropertiesScreen( gamesMap.get( gameName ), currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestUpdateAllDatabases()
	 */
	@Override
	public int onRequestUpdateAllDatabases() throws LauncherException
	{
		Map<String, ExtraData> extraDataMap = null;
		int numberUpdatedProfiles = 0;

		try
		{
			extraDataMap = extraDataGetter.getExtraData();
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		try
		{
			numberUpdatedProfiles = launcherPersistence.getGamePersister().updateGameExtraDataInDatabases( extraDataMap );
		}
		catch( GamePersistenceException gpe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		return numberUpdatedProfiles;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestImportBlueMSXLauncherDatabasesScreen()
	 */
	@Override
	public void onRequestImportBlueMSXLauncherDatabasesScreen() throws LauncherException
	{
		blueMSXLauncherImporterPresenterFactory.get().onRequestImportBlueMSXLauncherDatabasesScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onAcceptImportBlueMSXLauncherDatabasesAction(java.util.Set)
	 */
	@Override
	public void onAcceptImportBlueMSXLauncherDatabasesAction( Set<String> importedDatabases ) throws LauncherException
	{
		for( String importedDatabase: importedDatabases )
		{
			if( !databases.contains( importedDatabase ) )
			{
				//the reason we're checking is that some databases may have been replaced so we don't need to re-add them since they have the same name
				databases.add( importedDatabase );
			}
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestDatabaseManagerScreen()
	 */
	@Override
	public void onRequestDatabaseManagerScreen() throws LauncherException
	{
		databaseManagerPresenterFactory.create( databases, currentLanguage, currentRightToLeft ).onRequestDatabaseManagerScreen();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestDeleteDatabaseAction(java.lang.String)
	 */
	@Override
	public void onRequestDeleteDatabaseAction( String database )
	{
		databases.remove( database );
		if( currentDatabase != null && currentDatabase.equals( database ) )
		{
			gamesMap.clear();
			currentDatabase = null;
		}
		view.removeDatabase( database );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestRenameDatabaseAction(java.lang.String, java.lang.String)
	 */
	@Override
	public void onRequestRenameDatabaseAction( String oldDatabase, String newDatabase )
	{
		if( currentDatabase != null && currentDatabase.equals( oldDatabase ) )
		{
			currentDatabase = newDatabase;
		}

		view.renameDatabase( oldDatabase, newDatabase );

		databases.add( newDatabase );
		databases.remove( oldDatabase );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestUpdatesChecker()
	 */
	@Override
	public void onRequestUpdatesChecker() throws LauncherException
	{
		updateCheckerPresenterFactory.get().onRequestCheckForUpdatesScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestActivityViewerScreen()
	 */
	@Override
	public void onRequestActivityViewerScreen()
	{
		activityViewerPresenterFactory.get().onRequestActivityViewerScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestPatcherScreen()
	 */
	@Override
	public void onRequestPatcherScreen()
	{
		patcherPresenterFactory.get().onRequestIPSPatcherScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestMachineUpdateScreen()
	 */
	@Override
	public void onRequestMachineUpdateScreen() throws LauncherException
	{
		machineUpdatePresenterFactory.create( currentDatabase ).onRequestMachineUpdateScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestExit()
	 */
	@Override
	public void onRequestExit()
	{
		try
		{
			launcherPersistence.shutdown();
		}
		catch( LauncherPersistenceException lpe )
		{
			//ignore
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestAddDraggedAndDroppedGamesScreen(java.io.File[])
	 */
	@Override
	public void onRequestAddDraggedAndDroppedGamesScreen( File[] files ) throws LauncherException
	{
		if( currentDatabase == null )
		{
			//this means that no database is currently selected - ignore
		}
		else
		{
			draggedAndDroppedGamesPresenterFactory.create( currentDatabase, files ).onRequestAddDraggedAndDroppedGamesScreen( currentLanguage, Language.isRightToLeft( currentLanguage ) );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestSearchMatches(java.lang.String)
	 */
	@Override
	public Set<DatabaseItem> onRequestSearchMatches( String searchString )
	{
		Set<DatabaseItem> matches =  new TreeSet<>( new DatabaseItemComparator() );

		matches.addAll( launcherPersistence.getGameFinder().find( searchString, MAX_SEARCH_MATCHES ) );

		return matches;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onUpdateExtraData()
	 */
	@Override
	public void onUpdateExtraData() throws LauncherException
	{
		initializeRepositoryInfoMap();

		onViewUpdatedDatabase( null );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter#onRequestNewsList()
	 */
	@Override
	public void onRequestNewsList()
	{
		try
		{
			feedServicePresenter.onRequestNewsList();
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );
		}
	}

	private static void startBrowser( String uriString, LauncherExceptionCode errCodeIfNotFound ) throws LauncherException
	{
		URI uri;

		//first inspect the uriString string to see if it is a file or URL
		File file = new File( uriString );
		if( file.exists() )
		{
			uri = file.toURI();
		}
		else
		{
			//then this is not a file - assume it is a URL
			try
			{
				uri = new URI( uriString );
			}
			catch( URISyntaxException use )
			{
				throw new LauncherException( errCodeIfNotFound );
			}
		}

		try
		{
			Utils.startBrowser( uri );
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}

	private void setLanguageParameters( Language language )
	{
		currentLanguage = language;
		if( currentLanguage == null )
		{
			//in this case try to get system default
			Locale locale = Locale.getDefault();
			currentLanguage = Language.fromLocale( locale.getLanguage() + "_" + locale.getCountry() );

			//if still cannot find this locale, then force English
			if( currentLanguage == null )
			{
				currentLanguage = Language.ENGLISH;
			}

			currentLanguageCode = DEFAULT_SYSTEM_LANGUAGE;
		}
		else
		{
			currentLanguageCode = currentLanguage.toString();
		}

		currentRightToLeft = Language.isRightToLeft( currentLanguage );		
	}

	private Set<GameLabel> getSortedGameList()
	{
		Set<GameLabel> sortedList = new LinkedHashSet<GameLabel>();
		if( gamesMap != null )
		{
			gamesMap.entrySet().stream()
					.map( Entry::getValue )
					.sorted( (game1, game2) -> game1.getName().compareToIgnoreCase( game2.getName() ) )
					.forEach( game -> sortedList.add( getGameLabel( game ) ) );
		}
		
		return sortedList;
	}

	private GameLabel getGameLabel( Game game )
	{
		String company = null;
		String year = null;
		if( repositoryInfoMap != null )
		{
			RepositoryGame repositoryGame = repositoryInfoMap.get( game.getSha1Code() );
			if( repositoryGame != null )
			{
				company = repositoryGame.getCompany();
				year = repositoryGame.getYear();
			}
		}

		return new GameLabel( game.getName(), company, year, game.getSize(), getGameMedium( game ) );
	}

	private static Medium getGameMedium( Game game )
	{
		Medium medium = null;
		
		if( game.isROM() )
		{
			medium = Medium.ROM;
		}
		else if( game.isDisk() )
		{
			medium = Medium.DISK;
		}
		else if( game.isHarddisk() )
		{
			medium = Medium.HARDDISK;
		}
		else if( game.isTape() )
		{
			medium = Medium.TAPE;
		}
		else if( game.isLaserdisc() )
		{
			medium = Medium.LASERDISC;
		}
		else if( game.isScript() )
		{
			medium = Medium.SCRIPT;
		}

		return medium;
	}

	private void retrieveDatabaseGames( String database ) throws GamePersistenceException
	{
		Set<Game> games =  launcherPersistence.getGamePersister().getGames( database );

		if( currentFilter == null )
		{
			setGameMap( games );
		}
		else
		{
			Set<Game> filteredGames = new HashSet<Game>();

			games.stream().filter( game -> !isFiltered( game ) ).forEach( game -> filteredGames.add( game ) );

			setGameMap( filteredGames );
		}
	}

	private boolean isFiltered( Game game )
	{
		Map<String,Boolean> filtersByType = new HashMap<String,Boolean>();

		for( Filter filter: currentFilter )
		{
			String type = filter.getClass().getSimpleName();
			Boolean existingFilterValue = filtersByType.get( type );
			if( existingFilterValue == null )
			{
				filtersByType.put( type, true );
				existingFilterValue = true;
			}

			filtersByType.put( type, existingFilterValue && filter.isFiltered( game, repositoryInfoMap == null ? null:repositoryInfoMap.get( game.getSha1Code() ) ) );
		}

		//check which filter types have true
		return filtersByType.entrySet().stream().map( Entry::getValue ).anyMatch( v -> v );
	}

	private void setGameMap( Set<Game> games )
	{
		gamesMap = games.stream().collect( Collectors.toMap( Game::getName, game -> game ) );
	}

	private void initializeRepositoryInfoMap()
	{
		try
		{
			repositoryInfoMap = repositoryData.getRepositoryInfo();
		}
		catch( IOException ioe )
		{
			//in this case reset it
			repositoryInfoMap = null;
		}
	}

	private void selectFilter( String filterName ) throws LauncherException
	{
		if( filterName == null )
		{
			currentFilter = null;
		}
		else
		{
			try
			{
				currentFilter = launcherPersistence.getFiltersPersister().getFilter( filterName );
			}
			catch ( FilterSetNotFoundException fsnfe )
			{
				//should not really happen
			}
		}

		if( !Utils.isEmpty( currentDatabase ) )
		{
			try
			{
				retrieveDatabaseGames( currentDatabase );
			}
			catch( GamePersistenceException gpe )
			{
				if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) )
				{
					throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, currentDatabase );
				}
				else
				{
					throw new LauncherException( LauncherExceptionCode.ERR_IO );
				}
			}
	
			view.fillGameList( currentDatabase, getSortedGameList(), null );

			updateFilterName( filterName );
		}
	}

	private void updateFilterName( String filterName )
	{
		untitledFilter = false;
		currentFilterName = filterName;
		view.updateFilterNameLabel( filterName );
	}

	private class DatabaseItemComparator implements Comparator<DatabaseItem>
	{
		@Override
		public int compare( DatabaseItem fav1, DatabaseItem fav2 )
		{
			String gameName1 = fav1.getGameName();
			String gameName2 = fav2.getGameName();
			int sComp = gameName1.compareToIgnoreCase( gameName2 );

			if (sComp != 0)
			{
				return sComp;
			}
			else
			{
				String database1 = fav1.getDatabase();
				String database2 = fav2.getDatabase();
				return database1.compareToIgnoreCase( database2 );
			}
		}
	}
}
