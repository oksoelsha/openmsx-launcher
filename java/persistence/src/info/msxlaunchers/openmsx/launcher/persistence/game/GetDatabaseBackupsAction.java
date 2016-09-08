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
import info.msxlaunchers.openmsx.launcher.persistence.DatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.NonTransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to get backups for a given database
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
final class GetDatabaseBackupsAction extends NonTransactionalDatabaseOperation<Set<DatabaseBackup>>
{
	private static final String GET_DATABASE_BACKUPS_STATEMENT = "SELECT * FROM database_backup WHERE IDDB=?";

	private final String database;

	GetDatabaseBackupsAction( String database )
	{
		this.database = database;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.NonTransactionalDatabaseOperation#executeNonTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DatabaseResponse<Set<DatabaseBackup>> executeNonTransactionalOperation( Connection connection )
	{
		Set<DatabaseBackup> backups = new HashSet<>();

		long databaseId = getDatabaseId( connection, database );

		try( PreparedStatement statement = connection.prepareStatement( GET_DATABASE_BACKUPS_STATEMENT ) )
		{
			statement.setLong( 1, databaseId );

			try( ResultSet result = statement.executeQuery() )
			{
				while( result.next() )
				{
					backups.add( getDatabaseBackupFromResultSet( result ) );
				}
			}
		}
		catch( SQLException se )
		{
			//there's no valid reason for this so ignore - method will return an empty Set
			LauncherLogger.logException( this, se );
		}

		return new GetDatabaseBackupsResponse( backups );
	}

	private DatabaseBackup getDatabaseBackupFromResultSet( ResultSet result ) throws SQLException
	{
		return new DatabaseBackup( database, result.getTimestamp( "time" ) );
	}
}
