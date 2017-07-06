/*
 * Copyright 2017 Sam Elsharif
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

import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

/**
 * Class to update machines
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
final class UpdateMachineAction extends TransactionalDatabaseOperation<Integer>
{
	private static final String UPDATE_MACHINE_STATEMENT = "UPDATE game SET machine=?";

	private final String to;
	private final String from;
	private final String database;
	private final boolean backupAffectedDatabases;

	UpdateMachineAction( String to, String from, String database, boolean backupAffectedDatabases )
	{
		this.to = to;
		this.from = from;
		this.database = database;
		this.backupAffectedDatabases = backupAffectedDatabases;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public UpdateMachineResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		//backup databases(s) if requested
		if( backupAffectedDatabases )
		{
			Set<String> databasesToBackups = getDatabasesToBackup( connection );
	
			for( String databaseToBackup: databasesToBackups )
			{
				new BackupDatabaseAction( databaseToBackup ).executeTransactionalOperation( connection );
			}
		}

		String statementString = getStatementString();
		int totalUpdated = 0;

		try( PreparedStatement statement = connection.prepareStatement( statementString ) )
		{
			setParameters( connection, statement, statementString );

			totalUpdated = statement.executeUpdate();
		}
		catch( SQLException se )
		{
			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		return new UpdateMachineResponse( totalUpdated );
	}

	private Set<String> getDatabasesToBackup( Connection connection )
	{
		if( database == null )
		{
			return new GetDatabasesAction().executeNonTransactionalOperation( connection ).getResult();
		}
		else
		{
			return Collections.singleton( database );
		}
	}

	private String getStatementString()
	{
		StringBuilder builder = new StringBuilder( UPDATE_MACHINE_STATEMENT );

		if( from != null || database != null )
		{
			builder.append( " WHERE " );

			if( from != null )
			{
				builder.append( "machine=?" );
			}
			if( database != null )
			{
				if( from != null )
				{
					builder.append( " AND " );
				}
				builder.append( "IDDB=?" );
			}
		}

		return builder.toString();
	}

	private void setParameters( Connection connection, PreparedStatement statement, String statementString ) throws SQLException
	{
		int index = 1;
		statement.setString( index++, to );

		if( from != null )
		{
			statement.setString( index++, from );
		}
		if( database != null )
		{
			statement.setLong( index, getDatabaseId( connection, database ) );
		}
	}
}
