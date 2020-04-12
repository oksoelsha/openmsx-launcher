package info.msxlaunchers.openmsx.launcher.ui.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.inject.Provider;

import info.msxlaunchers.openmsx.game.repository.RepositoryData;
import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersister;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterPersister;
import info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.persistence.search.GameFinder;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.launcher.starter.EmulatorStarter;
import info.msxlaunchers.openmsx.launcher.ui.view.MainView;
import info.msxlaunchers.platform.FileLocator;

@RunWith( MockitoJUnitRunner.class )
public class MainPresenterImplTest
{
	@Mock MainView view;
	@Mock Provider<SettingsPresenter> settingsPresenterFactory;
	@Mock ProfileEditingPresenterFactory profileEditingPresenterFactory;
	@Mock Provider<ScannerPresenter> scannerPresenterFactor;
	@Mock Provider<FilterEditingPresenter> filterEditingPresenterFactory;
	@Mock Provider<GamePropertiesPresenter> gamePropertiesPresenterFactory;
	@Mock Provider<BlueMSXLauncherDatabasesImporterPresenter> blueMSXLauncherImporterPresenterFactory;
	@Mock DatabaseManagerPresenterFactory databaseManagerPresenterFactory;
	@Mock Provider<ActivityViewerPresenter> activityViewerPresenterFactory;
	@Mock Provider<PatcherPresenter> patcherPresenterFactory;
	@Mock MachineUpdatePresenterFactory machineUpdatePresenterFactory;
	@Mock FeedServicePresenter feedServicePresenter;
	@Mock Provider<RelatedGamesPresenter> relatedGamesPresenterFactory;
	@Mock Provider<LHADecompressorPresenter> lhaDecompressorPresenter;
	@Mock Provider<UpdateCheckerPresenter> updateCheckerPresenterFactory;
	@Mock SettingsPersister settingsPersister;
	@Mock LauncherPersistence launcherPersistence;
	@Mock GamePersister gamePersister;
	@Mock EmulatorStarter emulatorStarter;
	@Mock ExtraDataGetter extraDataGetter;
	@Mock RepositoryData repositoryData;
	@Mock FileLocator fileLocator;
	@Mock FavoritePersister favoritePersister;
	@Mock FilterPersister filterPersister;
	@Mock DraggedAndDroppedGamesPresenterFactory draggedAndDroppedGamesPresenterFactory;
	@Mock GameFinder gameFinder;


	private final String extraDataDirectory = "extraDataDirectory";
	private final String defaultDatabase = "defaultDatabase";

	MainPresenterImpl presenter;

	@Before
	public void setup() throws IOException
	{
		when( launcherPersistence.getGamePersister() ).thenReturn( gamePersister );
		when( launcherPersistence.getFavoritePersister() ).thenReturn( favoritePersister );
		when( launcherPersistence.getFiltersPersister() ).thenReturn( filterPersister );
		when( launcherPersistence.getSettingsPersister() ).thenReturn( settingsPersister );
		when( launcherPersistence.getGameFinder() ).thenReturn( gameFinder );
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, defaultDatabase, null, false, false ) );
		Set<String> databases = new HashSet<>();
		databases.add( defaultDatabase );
		when( gamePersister.getDatabases() ).thenReturn( databases );

		presenter = new MainPresenterImpl( view, settingsPresenterFactory, profileEditingPresenterFactory, scannerPresenterFactor, filterEditingPresenterFactory, gamePropertiesPresenterFactory,
				blueMSXLauncherImporterPresenterFactory, databaseManagerPresenterFactory, activityViewerPresenterFactory, updateCheckerPresenterFactory, launcherPersistence, emulatorStarter,
				extraDataGetter, extraDataDirectory, repositoryData, fileLocator, draggedAndDroppedGamesPresenterFactory, patcherPresenterFactory, machineUpdatePresenterFactory,
				feedServicePresenter, relatedGamesPresenterFactory, lhaDecompressorPresenter );
	}

	@Test( expected = IOException.class )
	public void testWhenSettingsPersisterThrowsException() throws IOException
	{
		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		new MainPresenterImpl( view, settingsPresenterFactory, profileEditingPresenterFactory, scannerPresenterFactor, filterEditingPresenterFactory, gamePropertiesPresenterFactory,
				blueMSXLauncherImporterPresenterFactory, databaseManagerPresenterFactory, activityViewerPresenterFactory, updateCheckerPresenterFactory, launcherPersistence, emulatorStarter,
				extraDataGetter, extraDataDirectory, repositoryData, fileLocator, draggedAndDroppedGamesPresenterFactory, patcherPresenterFactory, machineUpdatePresenterFactory,
				feedServicePresenter, relatedGamesPresenterFactory, lhaDecompressorPresenter );
	}

	@Test
	public void testDefaultDatabaseNullInConstructor() throws IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, null, null, false, false ) );

		//no exception is thrown
		new MainPresenterImpl( view, settingsPresenterFactory, profileEditingPresenterFactory, scannerPresenterFactor, filterEditingPresenterFactory, gamePropertiesPresenterFactory,
				blueMSXLauncherImporterPresenterFactory, databaseManagerPresenterFactory, activityViewerPresenterFactory, updateCheckerPresenterFactory, launcherPersistence, emulatorStarter,
				extraDataGetter, extraDataDirectory, repositoryData, fileLocator, draggedAndDroppedGamesPresenterFactory, patcherPresenterFactory, machineUpdatePresenterFactory,
				feedServicePresenter, relatedGamesPresenterFactory, lhaDecompressorPresenter );
	}

	@Test
	public void testDatabaseNotFoundWhenRetrieveGamesInConstructor() throws IOException, GamePersistenceException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, defaultDatabase, null, false, false ) );
		when( gamePersister.getGames( defaultDatabase ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, defaultDatabase ) );

		//no exception is thrown
		new MainPresenterImpl( view, settingsPresenterFactory, profileEditingPresenterFactory, scannerPresenterFactor, filterEditingPresenterFactory, gamePropertiesPresenterFactory,
				blueMSXLauncherImporterPresenterFactory, databaseManagerPresenterFactory, activityViewerPresenterFactory, updateCheckerPresenterFactory, launcherPersistence, emulatorStarter,
				extraDataGetter, extraDataDirectory, repositoryData, fileLocator, draggedAndDroppedGamesPresenterFactory, patcherPresenterFactory, machineUpdatePresenterFactory,
				feedServicePresenter, relatedGamesPresenterFactory, lhaDecompressorPresenter );
	}

	@Test
	public void testIOExceptionWhenRetrieveGamesInConstructor() throws IOException, GamePersistenceException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, defaultDatabase, null, false, false ) );
		when( gamePersister.getGames( defaultDatabase ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );

		//no exception is thrown
		new MainPresenterImpl( view, settingsPresenterFactory, profileEditingPresenterFactory, scannerPresenterFactor, filterEditingPresenterFactory, gamePropertiesPresenterFactory,
				blueMSXLauncherImporterPresenterFactory, databaseManagerPresenterFactory, activityViewerPresenterFactory, updateCheckerPresenterFactory, launcherPersistence, emulatorStarter,
				extraDataGetter, extraDataDirectory, repositoryData, fileLocator, draggedAndDroppedGamesPresenterFactory, patcherPresenterFactory, machineUpdatePresenterFactory,
				feedServicePresenter, relatedGamesPresenterFactory, lhaDecompressorPresenter );
	}

	@Test
	public void testOnRequestSettingsScreen() throws IOException
	{
		SettingsPresenter settingsPresenter = mock( SettingsPresenter.class );
		when( settingsPresenterFactory.get() ).thenReturn( settingsPresenter );

		presenter.onRequestSettingsScreen();
	}

	@Test( expected = LauncherException.class )
	public void testOnSelectDatabaseIOExceptionWhenRetrieveGames() throws IOException, GamePersistenceException, LauncherException
	{
		String newDatabase = "newDatabase";

		when( gamePersister.getGames( newDatabase ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );

		presenter.onSelectDatabase( newDatabase );
	}

	@Test( expected = LauncherException.class )
	public void testOnSelectDatabaseDatabaseNotFoundExceptionWhenRetrieveGames() throws IOException, GamePersistenceException, LauncherException
	{
		String newDatabase = "newDatabase";

		when( gamePersister.getGames( newDatabase ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, newDatabase ) );

		presenter.onSelectDatabase( newDatabase );
	}

	@Test
	public void testOnSelectDatabase() throws IOException, LauncherException
	{
		String newDatabase = "newDatabase";

		presenter.onSelectDatabase( newDatabase );

		verify( view, times( 1 ) ).fillGameList( newDatabase, new LinkedHashSet<GameLabel>(), null );
	}

	@Test
	public void testOnSelectDatabaseDatabaseNullOrSameDatabase() throws IOException, LauncherException
	{
		//case 1: null database
		presenter.onSelectDatabase( null );

		verify( view, never() ).fillGameList( null, new LinkedHashSet<GameLabel>(), null );
		verify( view, never() ).updateGameCount( 0 );

		//case 2: same database
		presenter.onSelectDatabase( defaultDatabase );

		verify( view, never() ).fillGameList( defaultDatabase, new LinkedHashSet<GameLabel>(), null );
		verify( view, never() ).updateGameCount( 0 );
	}

	@Test
	public void testOnLaunchGame()
			throws IOException, LauncherException
	{
		presenter.onLaunchGame( "gameName" );
	}

	@Test( expected = LauncherException.class )
	public void testOnLaunchGameExceptionWhenStartEmulator()
			throws IOException, LauncherException
	{
		when( emulatorStarter.start( (Settings)any(), (Game)any() ) ).thenThrow( new IOException() );

		presenter.onLaunchGame( "gameName" );
	}

	@Test( expected = LauncherException.class )
	public void testOnRemoveGamesIOException() throws IOException, GamePersistenceException, LauncherException
	{
		doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) ).when( gamePersister ).deleteGames( anySet(), anyString() );

		Set<String> gameNames = new HashSet<String>();
		presenter.onRequestRemoveGamesAction( gameNames );
	}

	@Test( expected = LauncherException.class )
	public void testOnRemoveGamesDatabaseDatabaseNotFoundException() throws IOException, GamePersistenceException, LauncherException
	{
		doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, defaultDatabase ) ).when( gamePersister ).deleteGames( anySet(), anyString() );

		Set<String> gameNames = new HashSet<String>();
		presenter.onRequestRemoveGamesAction( gameNames );
	}

	@Test
	public void testOnRemoveGameFromNonExistentDatabase() throws IOException, GamePersistenceException, LauncherException
	{
		Set<String> gameNames = new HashSet<String>();
		gameNames.add( "gameName" );

		doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_NOT_FOUND, "gameName" ) ).when( gamePersister ).deleteGames( anySet(), anyString() );

		presenter.onRequestRemoveGamesAction( gameNames );

		verify( view, times( 1 ) ).updateGameCount( 0 );
	}

	@Test
	public void testOnRemoveGamesDatabase() throws IOException, LauncherException
	{
		Set<String> gameNames = new HashSet<String>();
		gameNames.add( "gameName" );

		presenter.onRequestRemoveGamesAction( gameNames );

		verify( view, times( 1 ) ).updateGameCount( 0 );
	}

	@Test
	public void testResetAllWhenDatabaseIsSelected()
	{
		presenter.resetAll();

		verify( view, times( 1 ) ).showGameScreenshots( null, null );
		verify( view, times( 1 ) ).enableButtons( false, false, true, false, false );
		verify( view, times( 1 ) ).enableSoundIndicators( false, false, false, false, false, false, false, false );
		verify( view, times( 1 ) ).enableGenerationIndicators( false,  false,  false,  false );
	}

	@Test
	public void testResetAllWhenNoDatabaseIsSelected() throws LauncherException
	{
		presenter.onRequestDeleteDatabaseAction( defaultDatabase );
		presenter.resetAll();

		verify( view, times( 1 ) ).showGameScreenshots( null, null );
		verify( view, times( 1 ) ).enableButtons( false, false, false, false, false );
		verify( view, times( 1 ) ).enableSoundIndicators( false, false, false, false, false, false, false, false );
		verify( view, times( 1 ) ).enableGenerationIndicators( false,  false,  false,  false );
	}

	@Test
	public void testOnRequestMoveGamesScreen()
	{
		presenter.onRequestMoveGamesScreen( new HashSet<String>(), defaultDatabase );

		verify( view, times( 1 ) ).displayAndGetMoveGames( any( Language.class ), anySet(), anyString(), anySet(), anyBoolean() );
	}

	@Test
	public void testOnRequestMoveGamesAction() throws LauncherException, GamePersistenceException
	{
		presenter.onRequestMoveGamesAction( new HashSet<String>(), defaultDatabase, "newDatabase" );

		verify( gamePersister, times( 1 ) ).moveGames( anySet(), anyString(), anyString(), any( ActionDecider.class ) );
		verify( view, times( 1 ) ).updateGameCount( anyInt() );
	}

	@Test
	public void testOnRequestMoveGamesActionReturnValue() throws LauncherException, GamePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine" ).romA( "romA" ).build();

		Set<Game> movedGames = new HashSet<>();
		movedGames.add( game1 );
		movedGames.add( game2 );

		when( gamePersister.moveGames( anySet(), anyString(), anyString(), any( ActionDecider.class ) ) ).thenReturn( movedGames );

		Set<String> movedGameNames = presenter.onRequestMoveGamesAction( new HashSet<String>(), defaultDatabase, "newDatabase" );

		assertEquals( 2, movedGameNames.size() );
		assertTrue( movedGameNames.contains( "name1" ) );
		assertTrue( movedGameNames.contains( "name2" ) );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestMoveGamesActionGameNotFound() throws LauncherException, GamePersistenceException
	{
		when( gamePersister.moveGames( anySet(), anyString(), anyString(), any( ActionDecider.class ) ) )
			.thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_NOT_FOUND, "name" ) );

		try
		{
			presenter.onRequestMoveGamesAction( new HashSet<String>(), defaultDatabase, "newDatabase" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_GAME_NOT_FOUND, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestMoveGamesActionDatabaseNotFound() throws LauncherException, GamePersistenceException
	{
		when( gamePersister.moveGames( anySet(), anyString(), anyString(), any( ActionDecider.class ) ) )
			.thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, "name" ) );

		try
		{
			presenter.onRequestMoveGamesAction( new HashSet<String>(), defaultDatabase, "newDatabase" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestMoveGamesActionGameWithNullName() throws LauncherException, GamePersistenceException
	{
		when( gamePersister.moveGames( anySet(), anyString(), anyString(), any( ActionDecider.class ) ) )
			.thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME ) );

		try
		{
			presenter.onRequestMoveGamesAction( new HashSet<String>(), defaultDatabase, "newDatabase" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_GAME_WITH_NULL_NAME, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestMoveGamesActionIOException() throws LauncherException, GamePersistenceException
	{
		when( gamePersister.moveGames( anySet(), anyString(), anyString(), any( ActionDecider.class ) ) )
			.thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );

		try
		{
			presenter.onRequestMoveGamesAction( new HashSet<String>(), defaultDatabase, "newDatabase" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void testOnRequestCreateEmptyDatabaseScreen() throws LauncherException
	{
		presenter.onRequestCreateEmptyDatabaseScreen();

		verify( view, times( 1 ) ).displayCreateEmptyDatabase( any( Language.class ), anyBoolean() );
	}

	@Test
	public void testOnRequestCreateEmptyDatabaseAction() throws LauncherException, GamePersistenceException
	{
		presenter.onRequestCreateEmptyDatabaseAction( defaultDatabase );

		verify( gamePersister, times( 1 ) ).createDatabase( defaultDatabase );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestCreateEmptyDatabaseActionDatabaseNullName() throws LauncherException, GamePersistenceException
	{
		doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NULL_NAME ) ).when( gamePersister ).createDatabase( defaultDatabase);

		try
		{
			presenter.onRequestCreateEmptyDatabaseAction( defaultDatabase );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_NULL_NAME, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestCreateEmptyDatabaseActionDatabaseAlreadyExists() throws LauncherException, GamePersistenceException
	{
		doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS ) ).when( gamePersister ).createDatabase( defaultDatabase);

		try
		{
			presenter.onRequestCreateEmptyDatabaseAction( defaultDatabase );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_ALREADY_EXISTS, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestCreateEmptyDatabaseActionIOException() throws LauncherException, GamePersistenceException
	{
		doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) ).when( gamePersister ).createDatabase( defaultDatabase);

		try
		{
			presenter.onRequestCreateEmptyDatabaseAction( defaultDatabase );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void testOnRequestLocateFile() throws LauncherException, IOException
	{
		Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).build();

		presenter.onAcceptAddGameSaveAction( game );	//this is to populate gameMap

		presenter.onRequestLocateFile( "name" );

		verify( fileLocator, times( 1 ) ).locateFile( anyString() );
	}

	@Test
	public void testOnRequestAddFavorite() throws LauncherException, FavoritePersistenceException
	{
		presenter.onRequestAddFavorite( "gameName", "database" );

		verify( favoritePersister, times( 1 ) ).addFavorite( new DatabaseItem( "gameName", "database") );
	}

	@Test
	public void testOnRequestAddFavoriteAlreadyExists_thenIgnore() throws LauncherException, FavoritePersistenceException
	{
		doThrow( new FavoritePersistenceException( FavoritePersistenceExceptionIssue.FAVORITE_ALREADY_EXISTS ) ).when( favoritePersister) .addFavorite( any( DatabaseItem.class ) );

		presenter.onRequestAddFavorite( "gameName", "database" );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestAddFavoriteIOException_thenThrowException() throws LauncherException, FavoritePersistenceException
	{
		doThrow( new FavoritePersistenceException( FavoritePersistenceExceptionIssue.IO ) ).when( favoritePersister ) .addFavorite( any( DatabaseItem.class ) );

		try
		{
			presenter.onRequestAddFavorite( "gameName", "database" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void testOnRequestListOfFavorites() throws LauncherException
	{
		Set<DatabaseItem> favorites = getTestDatabaseItems();

		when( favoritePersister.getFavorites() ).thenReturn( favorites );

		presenter.onRequestListOfFavorites();

		verify( view, times( 1 ) ).showFavoritesMenu( favorites );
	}

	@Test
	public void testOnSelectDatabaseItem() throws LauncherException, FavoritePersistenceException
	{
		presenter.onSelectDatabaseItem( new DatabaseItem( "game", "name") );

		verify( view, times( 1 ) ).highlightGame( "game" );
	}

	@Test
	public void testOnRequestDeleteFavoriteAction() throws LauncherException, FavoritePersistenceException
	{
		presenter.onRequestDeleteFavoriteAction( new DatabaseItem("game name", "database name" ) );

		verify( favoritePersister, times( 1 ) ).deleteFavorite( new DatabaseItem( "game name", "database name" ) );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestDeleteFavoriteAction_thenThrowException() throws LauncherException, FavoritePersistenceException
	{
		doThrow( new FavoritePersistenceException( FavoritePersistenceExceptionIssue.IO ) ).when( favoritePersister ) .deleteFavorite( any( DatabaseItem.class ) );

		presenter.onRequestDeleteFavoriteAction( new DatabaseItem( "game name", "database name" ) );
	}

	@Test
	public void testOnRequestListOfSavedFilters()
	{
		Set<String> filterNames = new HashSet<String>( Arrays.asList( "f1", "f2", "f3", "f4" ) );

		when( filterPersister.getFilterNames() ).thenReturn( filterNames );

		presenter.onRequestListOfSavedFilters();

		verify( view, times(1 ) ).showFiltersMenu( filterNames, false, false );
	}

	@Test
	public void testOnRequestDeleteFilterAction() throws LauncherException, IOException
	{
		presenter.onRequestDeleteFilterAction( "filterName" );

		verify( filterPersister, times( 1 ) ).deleteFilter( "filterName" );
	}

	@Test
	public void testOnRequestAddFilterScreen() throws LauncherException
	{
		FilterEditingPresenter filterEditingPresenter = Mockito.mock( FilterEditingPresenter.class );
		when( filterEditingPresenterFactory.get() ).thenReturn( filterEditingPresenter );

		presenter.onRequestAddFilterScreen();

		verify( filterEditingPresenter, times( 1 ) ).onRequestAddFilterScreen( Language.ENGLISH, false );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestDeleteFilterAction_thenThrowException() throws LauncherException, IOException
	{
		doThrow( new IOException() ).when( filterPersister ) .deleteFilter( "filterName" );
		
		presenter.onRequestDeleteFilterAction( "filterName" );
	}

	@Test
	public void testOnRequestSetFilterNameUntitled()
	{
		presenter.onRequestSetFilterNameUntitled();

		verify( view, times( 1 ) ).setFilterNameLabelUntitled();
		verify( view, times( 1 ) ).displayFilterDetails( anyList() );
	}

	@Test
	public void testOnRequestShowProperties() throws LauncherException
	{
		Game game = Game.name( "game name" ).machine( "machine" ).romA( "romA" ).build();

		presenter.onAcceptAddGameSaveAction( game );	//this is to populate gameMap

		GamePropertiesPresenter gamePropertiesPresenter = Mockito.mock( GamePropertiesPresenter.class );
		when( gamePropertiesPresenterFactory.get() ).thenReturn( gamePropertiesPresenter );

		presenter.onRequestShowProperties( "game name" );

		verify( gamePropertiesPresenter, times( 1 ) ).onRequestGamePropertiesScreen( game, Language.ENGLISH, false );
	}

	@Test
	public void testOnRequestImportBlueMSXLauncherDatabasesScreen() throws LauncherException
	{
		BlueMSXLauncherDatabasesImporterPresenter blueMSXLauncherDatabasesImporterPresenter = Mockito.mock( BlueMSXLauncherDatabasesImporterPresenter.class );
		when( blueMSXLauncherImporterPresenterFactory.get() ).thenReturn( blueMSXLauncherDatabasesImporterPresenter );

		presenter.onRequestImportBlueMSXLauncherDatabasesScreen();

		verify( blueMSXLauncherDatabasesImporterPresenter, times( 1 ) ).onRequestImportBlueMSXLauncherDatabasesScreen( Language.ENGLISH, false );
	}

	@Test
	public void testOnRequestRenameDatabaseAction()
	{
		presenter.onRequestRenameDatabaseAction( "old name", "new name" );

		verify( view, times( 1 ) ).renameDatabase( "old name", "new name" );
	}

	@Test
	public void testOnRequestUpdatesChecker() throws LauncherException
	{
		UpdateCheckerPresenter updateCheckerPresenter = Mockito.mock( UpdateCheckerPresenter.class );
		when( updateCheckerPresenterFactory.get() ).thenReturn( updateCheckerPresenter );

		presenter.onRequestUpdatesChecker();

		verify( updateCheckerPresenter, times( 1 ) ).onRequestCheckForUpdatesScreen( Language.ENGLISH, false );
	}

	@Test
	public void testOnRequestDatabaseManagerScreen() throws LauncherException
	{
		DatabaseManagerPresenter databaseManagerPresenter = Mockito.mock( DatabaseManagerPresenter.class );
		when( databaseManagerPresenterFactory.create( anySet(), any( Language.class ), anyBoolean() ) ).thenReturn( databaseManagerPresenter );

		presenter.onRequestDatabaseManagerScreen();

		verify( databaseManagerPresenter, times( 1 ) ).onRequestDatabaseManagerScreen();
	}

	@Test
	public void testOnRequestDeleteDatabaseAction()
	{
		presenter.onRequestDeleteDatabaseAction( defaultDatabase );

		verify( view, times( 1 ) ).removeDatabase( defaultDatabase );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestLocateFileException() throws LauncherException, IOException
	{
		Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).build();

		presenter.onAcceptAddGameSaveAction( game );	//this is to populate gameMap

		doThrow( new IOException() ).when( fileLocator ).locateFile( anyString() );

		try
		{
			presenter.onRequestLocateFile( "name" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE, le.getCode() );
			throw le;
		}
	}

	@Test
	public void testOnRequestAddGameScreen() throws IOException, LauncherException
	{
		Settings settings = new Settings( null, null, null, defaultDatabase, null, false, false );

		ProfileEditingPresenter profileEditingPresenter = Mockito.mock( ProfileEditingPresenter.class );
		when( profileEditingPresenterFactory.create( settings, defaultDatabase, null) ).thenReturn( profileEditingPresenter );
		presenter.onRequestAddGameScreen();

		verify( profileEditingPresenter ).onRequestAddGameScreen( any( Language.class ), anyBoolean() );
	}

	@Test
	public void testOnRequestEditGameScreen() throws IOException, LauncherException
	{
		Settings settings = new Settings( null, null, null, defaultDatabase, null, false, false );

		String gameName = "gameName";

		ProfileEditingPresenter profileEditingPresenter = Mockito.mock( ProfileEditingPresenter.class );
		when( profileEditingPresenterFactory.create( settings, defaultDatabase, null) ).thenReturn( profileEditingPresenter );
		presenter.onRequestEditGameScreen( gameName );

		verify( profileEditingPresenter ).onRequestEditGameScreen( any( Language.class ), anyBoolean() );
	}

	@Test
	public void testOnAcceptAddGameSaveAction() throws IOException, LauncherException
	{
		Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).build();

		presenter.onAcceptAddGameSaveAction( game );

		verify( view, times(1) ).fillGameList( anyString(), anySet(), anyString() );
	}

	@Test
	public void testOnAcceptEditGameSaveAction() throws IOException, LauncherException
	{
		Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).build();

		presenter.onAcceptEditGameSaveAction( "oldName", game );

		verify( view, times(1) ).fillGameList( anyString(), anySet(), anyString() );
	}

	@Test
	public void testOnRequestFilterName() throws IOException, LauncherException
	{
		String filterName = "filterName";

		presenter.onRequestUpdateFilterName( filterName );

		verify( view, times( 1 ) ).updateFilterNameLabel( filterName );
	}

	@Test
	public void testOnRequestExit() throws LauncherPersistenceException
	{
		presenter.onRequestExit();

		verify( launcherPersistence, times( 1 ) ).shutdown();
	}

	@Test
	public void testOnRequestExitWithException() throws LauncherPersistenceException
	{
		doThrow( new LauncherPersistenceException() ).when( launcherPersistence ).shutdown();

		presenter.onRequestExit();

		//exception will be ignored
		verify( launcherPersistence, times( 1 ) ).shutdown();
	}

	@Test
	public void testOnRequestSearchMatches()
	{
		Set<DatabaseItem> matches = getTestDatabaseItems();

		when( gameFinder.find( anyString(), anyInt() ) ).thenReturn( matches );

		Set<DatabaseItem> searchMatches = presenter.onRequestSearchMatches( "searchString" );

		assertEquals( matches, searchMatches );
	}

	private Set<DatabaseItem> getTestDatabaseItems()
	{
		DatabaseItem databaseItem1 = new DatabaseItem( "gameName", "database" );
		DatabaseItem databaseItem2 = new DatabaseItem( "Abc", "def" );
		DatabaseItem databaseItem3 = new DatabaseItem( "abc", "dee" );
		DatabaseItem databaseItem4 = new DatabaseItem( "srf", "mnb" );
		DatabaseItem databaseItem5 = new DatabaseItem( "t y r", "l wer tr" );
		DatabaseItem databaseItem6 = new DatabaseItem( "kkbb", "xx vv" );
		DatabaseItem databaseItem7 = new DatabaseItem( "Kk", "xx vv" );

		Set<DatabaseItem> set = new HashSet<>();
		set.add( databaseItem1 );
		set.add( databaseItem2 );
		set.add( databaseItem3 );
		set.add( databaseItem4 );
		set.add( databaseItem5 );
		set.add( databaseItem6 );
		set.add( databaseItem7 );

		return set;
	}
}