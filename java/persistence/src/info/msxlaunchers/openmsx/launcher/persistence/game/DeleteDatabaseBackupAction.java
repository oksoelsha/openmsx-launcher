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
package info.msxlaunchers.openmsx.launcher.persistence.game;

import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.DefaultDatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class to delete a database backup
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
final class DeleteDatabaseBackupAction extends TransactionalDatabaseOperation<Boolean>
{
	private static final String DELETE_DATABASE_BACKUP_STATEMENT = "DELETE FROM database_backup where time=? and IDDB=?";

	private final DatabaseBackup backup;

	DeleteDatabaseBackupAction( DatabaseBackup backup )
	{
		this.backup = backup;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DefaultDatabaseResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		//first get the current backup
		long databaseId = getDatabaseId( connection, backup.getDatabase() );

		try( PreparedStatement deleteDatabaseBackupStatement = connection.prepareStatement( DELETE_DATABASE_BACKUP_STATEMENT ) )
		{
			deleteDatabaseBackupStatement.setTimestamp( 1, backup.getTimestamp() );
			deleteDatabaseBackupStatement.setLong( 2, databaseId );

			deleteDatabaseBackupStatement.executeUpdate();
		}
		catch( SQLException se )
		{
			LauncherLogger.logException( this, se );

			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		return new DefaultDatabaseResponse();
	}
}
