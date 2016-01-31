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

import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.ui.view.DatabaseBackupsView;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Implementation of <code>DatabaseBackupsPresenter</code>
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
final class DatabaseBackupsPresenterImpl implements DatabaseBackupsPresenter
{
	private final LauncherPersistence launcherPersistence;
	private final DatabaseBackupsView view;
	private final DatabaseManagerPresenter databaseManagerPresenter;
	private final String database;
	private final Set<DatabaseBackup> backups;
	private final Map<Timestamp,DatabaseBackup> timestampToBackup;

	@Inject
	DatabaseBackupsPresenterImpl( DatabaseBackupsView view, LauncherPersistence launcherPersistence, @Assisted DatabaseManagerPresenter databaseManagerPresenter,
			@Assisted String database, @Assisted Set<DatabaseBackup> backups )
	{
		this.databaseManagerPresenter = Objects.requireNonNull( databaseManagerPresenter );
		this.launcherPersistence = Objects.requireNonNull( launcherPersistence );
		this.view = Objects.requireNonNull( view );
		this.database = Objects.requireNonNull( database );
		this.backups = Objects.requireNonNull( backups );
		this.timestampToBackup = backups.stream().collect( Collectors.toMap( DatabaseBackup::getTimestamp, p -> p ) );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter#onRequestDatabaseBackupsScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestDatabaseBackupsScreen( Language currentLanguage, boolean currentRightToLeft )
	{
		view.displayScreen( this, currentLanguage, currentRightToLeft, backups );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter#onRequestDeleteBackup(java.sql.Timestamp)
	 */
	@Override
	public void onRequestDeleteBackup( Timestamp timestamp ) throws LauncherException
	{
		try
		{
			launcherPersistence.getGamePersister().deleteBackup( timestampToBackup.get( timestamp ) );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.BACKUP_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_BACKUP_NOT_FOUND, timestamp.toString() );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter#onRequestRestoreBackup(java.sql.Timestamp)
	 */
	@Override
	public void onRequestRestoreBackup( Timestamp timestamp ) throws LauncherException
	{
		try
		{
			launcherPersistence.getGamePersister().restoreBackup( timestampToBackup.get( timestamp ) );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.BACKUP_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_BACKUP_NOT_FOUND, timestamp.toString() );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter#updateDatabaseAndBackupsView(java.sql.Timestamp)
	 */
	@Override
	public void updateDatabaseAndBackupsView( Timestamp timestamp )
	{
		databaseManagerPresenter.updateDatabaseAndBackupsView( timestampToBackup.get( timestamp ).getDatabase() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter#updateDatabaseAndBackupsView(info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup)
	 */
	@Override
	public void updateDatabaseAndBackupsView( DatabaseBackup databaseBackup )
	{
		timestampToBackup.put( databaseBackup.getTimestamp(), databaseBackup );

		databaseManagerPresenter.updateDatabaseAndBackupsView( database );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter#updateDatabaseInfoView()
	 */
	@Override
	public void updateDatabaseInfoView()
	{
		databaseManagerPresenter.updateDatabaseInfoView();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter#updateViewedDatabase(java.sql.Timestamp)
	 */
	@Override
	public void updateViewedDatabase( Timestamp timestamp ) throws LauncherException
	{
		databaseManagerPresenter.updateViewedDatabase( timestampToBackup.get( timestamp ).getDatabase() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter#onRequestBackupDatabase()
	 */
	@Override
	public DatabaseBackup onRequestBackupDatabase() throws LauncherException
	{
		try
		{
			return launcherPersistence.getGamePersister().backupDatabase( database );
		}
		catch( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_MAX_BACKUPS_REACHED ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_MAX_BACKUPS_REACHED );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}
	}
}
