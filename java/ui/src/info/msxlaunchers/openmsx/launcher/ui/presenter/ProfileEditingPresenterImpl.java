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

import info.msxlaunchers.openmsx.common.Nullable;
import info.msxlaunchers.openmsx.extension.ExtensionLister;
import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.starter.EmulatorStarter;
import info.msxlaunchers.openmsx.launcher.ui.view.ProfileEditingView;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.MachineLister;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Implementation of <code>ProfileEditingPresenter</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
final class ProfileEditingPresenterImpl implements ProfileEditingPresenter
{
	private final ProfileEditingView view;
	private final MainPresenter mainPresenter;
	private final GamePersister gamePersister;
	private final EmulatorStarter emulatorStarter;
	private final MachineLister machineLister;
	private final ExtensionLister extensionLister;
	private final GameBuilder gameBuilder;
	private final ExtraDataGetter extraDataGetter;
	private final Settings settings;
	private final String currentDatabase;
	private final Game currentSelectedGame;

	@Inject
	ProfileEditingPresenterImpl( ProfileEditingView view,
			MainPresenter mainPresenter,
			GamePersister gamePersister,
			EmulatorStarter emulatorStarter,
			MachineLister machineLister,
			ExtensionLister extensionLister,
			GameBuilder gameBuilder,
			ExtraDataGetter extraDataGetter,
			@Assisted Settings settings,
			@Assisted String currentDatabase,
			@Nullable @Assisted Game currentSelectedGame ) throws IOException
	{
		this.view = Objects.requireNonNull( view );
		this.mainPresenter = Objects.requireNonNull( mainPresenter );
		this.gamePersister = Objects.requireNonNull( gamePersister );
		this.emulatorStarter = Objects.requireNonNull( emulatorStarter );
		this.machineLister = Objects.requireNonNull( machineLister );
		this.extensionLister = Objects.requireNonNull( extensionLister );
		this.gameBuilder = Objects.requireNonNull( gameBuilder );
		this.extraDataGetter = Objects.requireNonNull( extraDataGetter );
		this.settings = Objects.requireNonNull( settings );
		this.currentDatabase = Objects.requireNonNull( currentDatabase );
		this.currentSelectedGame = currentSelectedGame;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter#onRequestAddGameScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestAddGameScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException
	{
		try
		{
			view.displayAddGameScreen( this, currentLanguage, machineLister.get(), extensionLister.get(), currentRightToLeft );
		}
		catch( InvalidMachinesDirectoryException imde )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_INVALID_MACHINES_DIRECTORY );
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter#onRequestEditGameScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestEditGameScreen( Language currentLanguage, boolean currentRightToLeft )
			throws LauncherException
	{
		try
		{
			view.displayEditGameScreen( this,
				currentLanguage,
				machineLister.get(),
				extensionLister.get(),
				Language.isRightToLeft( currentLanguage ),
				currentSelectedGame.getName(),
				currentSelectedGame.getInfo(),
				currentSelectedGame.getMachine(),
				currentSelectedGame.getRomA(),
				currentSelectedGame.getRomB(),
				currentSelectedGame.getExtensionRom(),
				currentSelectedGame.getDiskA(),
				currentSelectedGame.getDiskB(),
				currentSelectedGame.getTape(),
				currentSelectedGame.getHarddisk(),
				currentSelectedGame.getLaserdisc(),
				currentSelectedGame.getTclScript() );
		}
		catch( InvalidMachinesDirectoryException imde )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_INVALID_MACHINES_DIRECTORY );
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter#onRequestAddGameSaveAction(java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onRequestAddGameSaveAction( String name,
			String info,
			String machine,
			boolean romsUsed,
			String romA,
			String romB,
			String extensionRom,
			String diskA,
			String diskB,
			String tape,
			String harddisk,
			String laserdisc,
			String script) throws LauncherException
	{
		String romAEffectiveValue;
		String romBEffectiveValue;
		String extensionRomEffectiveValue;

		if( romsUsed )
		{
			romAEffectiveValue = romA;
			romBEffectiveValue = romB;
			extensionRomEffectiveValue = null;
		}
		else
		{
			romAEffectiveValue = null;
			romBEffectiveValue = null;
			extensionRomEffectiveValue = extensionRom;
		}

		Map<String,ExtraData> extraDataMap = null;
		try
		{
			extraDataMap = extraDataGetter.getExtraData();
		}
		catch ( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		Game game = null;
		
		try
		{
			game = getGameObject( name, info, machine, romAEffectiveValue, romBEffectiveValue, extensionRomEffectiveValue,
					diskA, diskB, tape, harddisk, laserdisc, script, extraDataMap );
		}
		catch( IllegalArgumentException iae )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_EMPTY_GAME_FIELDS );
		}

		if( game == null )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_EMPTY_GAME_FIELDS );
		}

		try
		{
			gamePersister.saveGame( game, currentDatabase );

			mainPresenter.onAcceptAddGameSaveAction( game );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_GAME_WITH_NULL_NAME );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.GAME_ALREADY_EXISTS ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_GAME_ALREADY_EXISTS, name );
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
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter#onRequestLaunchAction(java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onRequestLaunchAction( String machine,
			boolean romsUsed,
			String romA,
			String romB,
			String extensionRom,
			String diskA,
			String diskB,
			String tape,
			String harddisk,
			String laserdisc,
			String script) throws LauncherException
	{
		String extensionRomEffectiveValue;
		if( romsUsed )
		{
			extensionRomEffectiveValue = null;
		}
		else
		{
			extensionRomEffectiveValue = extensionRom;
		}

		Game game = null;
		try
		{
			game = Game.machine( machine )
				.romA( romA ).romB( romB ).extensionRom( extensionRomEffectiveValue )
				.diskA( diskA ).diskB( diskB )
				.tape( tape )
				.harddisk( harddisk )
				.laserdisc( laserdisc )
				.tclScript( script )
				.build();
		}
		catch ( IllegalArgumentException iae )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_EMPTY_GAME_FIELDS );
		}

		try
		{
			emulatorStarter.start( settings, game );
		}
		catch ( IOException e )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_START_OPENMSX );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter#onRequestEditGameSaveAction(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onRequestEditGameSaveAction( String oldName,
			String newName,
			String info,
			String machine,
			boolean romsUsed,
			String romA,
			String romB,
			String extensionRom,
			String diskA,
			String diskB,
			String tape,
			String harddisk,
			String laserdisc,
			String script) throws LauncherException
	{
		String romAEffectiveValue;
		String romBEffectiveValue;
		String extensionRomEffectiveValue;

		if( romsUsed )
		{
			romAEffectiveValue = romA;
			romBEffectiveValue = romB;
			extensionRomEffectiveValue = null;
		}
		else
		{
			romAEffectiveValue = null;
			romBEffectiveValue = null;
			extensionRomEffectiveValue = extensionRom;
		}

		Map<String,ExtraData> extraDataMap = null;
		try
		{
			extraDataMap = extraDataGetter.getExtraData();
		}
		catch ( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		Game newGame = null;
		try
		{
			newGame = getGameObject( newName, info, machine, romAEffectiveValue, romBEffectiveValue,
					extensionRomEffectiveValue, diskA, diskB, tape, harddisk, laserdisc, script, extraDataMap );
		}
		catch ( IllegalArgumentException iae )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_EMPTY_GAME_FIELDS );
		}

		try
		{
			gamePersister.updateGame( currentSelectedGame, newGame, currentDatabase );

			mainPresenter.onAcceptEditGameSaveAction( oldName, newGame );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_GAME_WITH_NULL_NAME );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.GAME_ALREADY_EXISTS ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_GAME_ALREADY_EXISTS, newName );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.GAME_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_GAME_NOT_FOUND, oldName );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_GAME_ALREADY_EXISTS, currentDatabase );
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
	}

	private Game getGameObject( String name, String info, String machine,
			String romA, String romB, String extensionRom,
			String diskA, String diskB,
			String tape, String harddisk,
			String laserdisc, String script,
			Map<String,ExtraData> extraDataMap )
	{
		return gameBuilder.createGameObjectForDataEnteredByUser( name, info, machine, romA, romB, extensionRom, diskA, diskB, tape, harddisk, laserdisc, script, extraDataMap );
	}
}
