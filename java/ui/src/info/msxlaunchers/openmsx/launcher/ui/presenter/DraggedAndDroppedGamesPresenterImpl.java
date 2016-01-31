/*
 * Copyright 2015 Sam Elsharif
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

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.game.scan.Scanner;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.ui.view.AddDraggedAndDroppedGamesView;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.MachineLister;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Implementation of <code>DraggedAndDroppedGamesPresenter</code>
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
final class DraggedAndDroppedGamesPresenterImpl implements DraggedAndDroppedGamesPresenter
{
	private final MainPresenter mainPresenter;
	private final AddDraggedAndDroppedGamesView view;
	private final String currentDatabase;
	private final File[] files;
	private final MachineLister machineLister;
	private final Scanner scanner;

	//Model
	private File[] filteredFiles;
	
	@Inject
	DraggedAndDroppedGamesPresenterImpl( MainPresenter mainPresenter, AddDraggedAndDroppedGamesView view, @Assisted String currentDatabase,
			@Assisted File[] files, MachineLister machineLister, Scanner scanner )
	{
		this.mainPresenter = Objects.requireNonNull( mainPresenter );
		this.view = Objects.requireNonNull( view );
		this.currentDatabase = Objects.requireNonNull( currentDatabase );
		this.files = Objects.requireNonNull( files );
		this.machineLister = Objects.requireNonNull( machineLister );
		this.scanner = Objects.requireNonNull( scanner );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DraggedAndDroppedGamesPresenter#onRequestAddDraggedAndDroppedGamesScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestAddDraggedAndDroppedGamesScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException
	{
		try
		{
			//filter non-openMSX files out
			filteredFiles = Arrays.asList( files ).stream().filter( file -> isDirectoryOrPotentialMSXFile( file ) ).toArray( File[]::new );

			if( filteredFiles.length > 0 )
			{
				view.displayScreen( this, currentDatabase, currentLanguage, currentRightToLeft, filteredFiles, machineLister.get() );
			}
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
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DraggedAndDroppedGamesPresenter#onRequestAddDraggedAndDroppedGamesAddAction(boolean, boolean, java.lang.String)
	 */
	@Override
	public int onRequestAddDraggedAndDroppedGamesAddAction( boolean getNameFromOpenMSXDatabase, boolean backupDatabase, String machine ) throws LauncherException
	{
		int totalFound = 0;
		String[] filePaths = Arrays.asList( filteredFiles ).stream().map( file -> file.getAbsolutePath() ).toArray( String[]::new );

		try
		{
			totalFound = scanner.scan( filePaths, true, currentDatabase, false, true, machine, true, true, true, true, getNameFromOpenMSXDatabase, backupDatabase );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME ) )
			{
				//this should not happen because all games we find will have names
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NULL_NAME ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NULL_NAME );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_MAX_BACKUPS_REACHED ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_MAX_BACKUPS_REACHED, currentDatabase );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_ALREADY_EXISTS, currentDatabase );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, currentDatabase );
			}
		}
		catch( IOException e )
		{
			//maybe the backup, overwrite, or game saving failed
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		return totalFound;

	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DraggedAndDroppedGamesPresenter#onRequestInterruptFillDatabaseProcess()
	 */
	@Override
	public void onRequestInterruptFillDatabaseProcess()
	{
		scanner.interrupt();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DraggedAndDroppedGamesPresenter#onUpdateViewedDatabase(java.lang.String)
	 */
	@Override
	public void onUpdateViewedDatabase( String database ) throws LauncherException
	{
		mainPresenter.onUpdateViewedDatabase( database );
	}	

	private boolean isDirectoryOrPotentialMSXFile( File file )
	{
		return file.isDirectory() || FileTypeUtils.isROM( file ) || FileTypeUtils.isDisk( file ) ||
				FileTypeUtils.isTape( file ) || FileTypeUtils.isLaserdisc( file ) || FileTypeUtils.isZIP( file );
	}
}
