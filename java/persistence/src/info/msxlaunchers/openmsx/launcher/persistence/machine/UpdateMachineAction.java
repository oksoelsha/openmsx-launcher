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
package info.msxlaunchers.openmsx.launcher.persistence.machine;

import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

	UpdateMachineAction( String to, String from, String database )
	{
		this.to = to;
		this.from = from;
		this.database = database;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public UpdateMachineResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		String statementString = getStatementString();
		int totalUpdated = 0;

		try( PreparedStatement statement = connection.prepareStatement( statementString ) )
		{
			setParameters( connection, statement, statementString );

			totalUpdated = statement.executeUpdate();
		}
		catch( SQLException se )
		{
			throwEncapsulatingException( new MachineUpdatePersistenceException( MachineUpdatePersistenceExceptionIssue.IO ) );
		}

		return new UpdateMachineResponse( totalUpdated );
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
