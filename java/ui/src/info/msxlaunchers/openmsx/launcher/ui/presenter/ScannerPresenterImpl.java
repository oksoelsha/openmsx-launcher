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

import info.msxlaunchers.openmsx.game.scan.Scanner;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.ui.view.ScannerView;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.MachineLister;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;

/**
 * Implementation of <code>ScannerPresenter</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
final class ScannerPresenterImpl implements ScannerPresenter
{
	private final ScannerView view;
	private final MainPresenter mainPresenter;
	private final Scanner scanner;
	private final MachineLister machineLister;

	@Inject
	ScannerPresenterImpl( ScannerView view,
			MainPresenter mainPresenter,
			Scanner scanner,
			MachineLister machineLister ) throws IOException
	{
		this.view = Objects.requireNonNull( view );
		this.mainPresenter = Objects.requireNonNull( mainPresenter );
		this.scanner = Objects.requireNonNull( scanner );
		this.machineLister = Objects.requireNonNull( machineLister );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.ScannerPresenter#onRequestFillDatabaseScreen(java.util.Set, java.lang.String, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestFillDatabaseScreen( Set<String> databases, String currentDatabase, Language currentLanguage, boolean currentRightToLeft )
			throws LauncherException
	{
		try
		{
			view.displayFillDatabase( currentLanguage,
					databases,
					currentDatabase,
					machineLister.get(),
					Language.isRightToLeft( currentLanguage ) );
		}
		catch( InvalidMachinesDirectoryException imde )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_INVALID_MACHINES_DIRECTORY );
		}
		catch ( IOException e )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.ScannerPresenter#onRequestFillDatabaseAction(java.lang.String[], boolean, java.lang.String, boolean, boolean, java.lang.String, boolean, boolean, boolean, boolean, boolean, boolean)
	 */
	@Override
	public int onRequestFillDatabaseAction( String[] paths,
			boolean traverseSubDirectories,
			String database,
			boolean newDatabase,
			boolean append,
			String machine,
			boolean searchROM,
			boolean searchDisk,
			boolean searchTape,
			boolean searchLaserdisc,
			boolean getNameFromOpenMSXDatabase,
			boolean backupDatabase )
					throws LauncherException
	{
		int totalFound = 0;
		try
		{
			//scan and update the database
			totalFound = scanner.scan( paths,
					traverseSubDirectories,
					database,
					newDatabase,
					append,
					machine,
					searchROM,
					searchDisk,
					searchTape,
					searchLaserdisc,
					getNameFromOpenMSXDatabase,
					backupDatabase );
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
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_MAX_BACKUPS_REACHED, database );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_ALREADY_EXISTS, database );
			}
			else if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, database );
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
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.ScannerPresenter#onRequestInterruptFillDatabaseProcess()
	 */
	@Override
	public void onRequestInterruptFillDatabaseProcess()
	{
		scanner.interrupt();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.ScannerPresenter#onUpdateViewedDatabase(java.lang.String)
	 */
	@Override
	public void onUpdateViewedDatabase( String database ) throws LauncherException
	{
		mainPresenter.onUpdateViewedDatabase( database );
	}	
}
