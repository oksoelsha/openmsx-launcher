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

import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.ui.view.DatabaseManagerView;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Implementation of <code>DatabaseManagerPresenter</code>
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
final class DatabaseManagerPresenterImpl implements DatabaseManagerPresenter
{
	private final DatabaseManagerView view;
	private final MainPresenter mainPresenter;
	private final LauncherPersistence launcherPersistence;
	private final DatabaseBackupsPresenterFactory databaseBackupsPresenterFactory;
	private final Set<String> databases;
	private final Language currentLanguage;
	private final boolean currentRightToLeft;

	@Inject
	DatabaseManagerPresenterImpl( DatabaseManagerView view,	MainPresenter launcherPresenter, LauncherPersistence launcherPersistence,
			DatabaseBackupsPresenterFactory databaseBackupsPresenterFactory, @Assisted Set<String> databases, @Assisted Language currentLanguage, @Assisted boolean currentRightToLeft )
	{
		this.view = Objects.requireNonNull( view );
		this.mainPresenter = Objects.requireNonNull( launcherPresenter );
		this.launcherPersistence = Objects.requireNonNull( launcherPersistence );
		this.databaseBackupsPresenterFactory = Objects.requireNonNull( databaseBackupsPresenterFactory );
		this.databases = Objects.requireNonNull( databases );
		this.currentLanguage = Objects.requireNonNull( currentLanguage );
		this.currentRightToLeft = Objects.requireNonNull( currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#onRequestDatabaseManagerScreen()
	 */
	@Override
	public void onRequestDatabaseManagerScreen()
	{
		//Sort the set
		Comparator<DatabaseAndBackups> byName = Comparator.comparing( DatabaseAndBackups::getName, String.CASE_INSENSITIVE_ORDER );
		Set<DatabaseAndBackups> databaseAndBackups = databases.stream()
				.map( d -> new DatabaseAndBackups( d, getTotalGamesInDatabase( d ), launcherPersistence.getGamePersister().getBackups( d ).size() ) )
				.collect( Collectors.toCollection( () -> new TreeSet<DatabaseAndBackups>( byName ) ) );

		view.displayScreen( this, currentLanguage, currentRightToLeft, databaseAndBackups );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#onRequestDeleteDatabase(java.lang.String)
	 */
	@Override
	public void onRequestDeleteDatabase( String database ) throws LauncherException
	{
		try
		{
			launcherPersistence.getGamePersister().deleteDatabase( database );
			mainPresenter.onRequestDeleteDatabaseAction( database );
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
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#onRequestRenameDatabaseScreen(java.lang.String)
	 */
	@Override
	public String onRequestRenameDatabaseScreen( String database )
	{
		return view.displayRenameDatabaseScreen( this, database, currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#onRequestRenameDatabaseAction(java.lang.String, java.lang.String)
	 */
	@Override
	public void onRequestRenameDatabaseAction( String oldDatabase, String newDatabase ) throws LauncherException
	{
		try
		{
			launcherPersistence.getGamePersister().renameDatabase( oldDatabase, newDatabase );
			mainPresenter.onRequestRenameDatabaseAction( oldDatabase, newDatabase );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, oldDatabase );
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
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#onRequestDeleteAllBackups()
	 */
	@Override
	public void onRequestDeleteAllBackups() throws LauncherException
	{
		for( String database:databases )
		{
			Set<DatabaseBackup> backups = launcherPersistence.getGamePersister().getBackups( database );

			for( DatabaseBackup backup:backups )
			{
				try
				{
					launcherPersistence.getGamePersister().deleteBackup( backup );
				}
				catch( GamePersistenceException gpe )
				{
					throw new LauncherException( LauncherExceptionCode.ERR_IO );
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#updateDatabaseAndBackupsView(java.lang.String)
	 */
	@Override
	public void updateDatabaseAndBackupsView( String database )
	{
		int gamesTotal = 0;
		try
		{
			gamesTotal = launcherPersistence.getGamePersister().getGames( database ).size();
		}
		catch( GamePersistenceException gpe )
		{
			//just ignore
		}
		int backupsTotal = launcherPersistence.getGamePersister().getBackups( database ).size();

		view.updateDatabaseAndBackups( database, gamesTotal, backupsTotal );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#updateDatabaseInfoView()
	 */
	@Override
	public void updateDatabaseInfoView()
	{
		DatabaseInfo databaseInfo = getDatabaseInfo( databases );

		view.updateDatabaseInfo( databaseInfo );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#getDatabaseInfo(java.util.Set)
	 */
	@Override
	public DatabaseInfo getDatabaseInfo( Set<String> databases )
	{
		int totalDatabases = databases.size();
		int totalGames = 0;
		int totalBackups = 0;

		GamePersister persister = launcherPersistence.getGamePersister();

		for( String database: databases )
		{
			try
			{
				totalGames += persister.getGames( database ).size();
				totalBackups += persister.getBackups( database ).size();
			}
			catch( GamePersistenceException gpe )
			{
				//ignore
			}
		}
		
		return new DatabaseInfo( totalDatabases, totalGames, totalBackups );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#onRequestDatabaseBackupsScreen(java.lang.String)
	 */
	@Override
	public void onRequestDatabaseBackupsScreen( String database )
	{
		//create a sorted copy of the backups set
		Set<DatabaseBackup> backups = launcherPersistence.getGamePersister().getBackups( database );

		SortedSet<DatabaseBackup> sortedBackups = new TreeSet<DatabaseBackup>( (b1, b2) -> b1.getTimestamp().compareTo( b2.getTimestamp() ) );

		sortedBackups.addAll( backups );

		databaseBackupsPresenterFactory.create( this, database, sortedBackups).onRequestDatabaseBackupsScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter#updateViewedDatabase(java.lang.String)
	 */
	@Override
	public void updateViewedDatabase( String database ) throws LauncherException
	{
		mainPresenter.onUpdateViewedDatabase( database );
	}

	private int getTotalGamesInDatabase( String database )
	{
		int total = 0;
		try
		{
			total = launcherPersistence.getGamePersister().getGames( database ).size();
		}
		catch( GamePersistenceException e )
		{
			//Ignore - just return 0
		}
		return total;
	}
}
