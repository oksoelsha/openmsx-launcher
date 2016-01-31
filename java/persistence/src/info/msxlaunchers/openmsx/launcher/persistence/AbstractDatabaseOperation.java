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
package info.msxlaunchers.openmsx.launcher.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract implementation of <code>DatabaseAction<code> that contains default implementation of the interface
 * and common methods
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
abstract class AbstractDatabaseOperation<E> implements DatabaseAction<E>
{
	private static final String GET_DATABASE_ID_BY_NAME_STATEMENT = "SELECT ID FROM database WHERE name=?";

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.DatabaseAction#execute(java.lang.String)
	 */
	@Override
	public DatabaseResponse<E> execute( String databaseFullPath ) throws LauncherPersistenceException
	{
		DatabaseResponse<E> response = null;

		String dbURL = "jdbc:derby:" + databaseFullPath;

		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
			try
			{
				response = executeOperation( connection );
			}
			catch( LauncherPersistenceException lpe )
			{
				connection.rollback();
				throw lpe;
			}
		}
    	catch( SQLException se )
    	{
    		//TODO What to do?
    	}

		return response;
	}

	/**
	 * Executes specific database operation. This method must be overridden in the subclass
	 * 
	 * @param connection Connection to the embedded database - cannot be null
	 * @return Instance of DatabaseResponse that contains the result of the operation
	 * @throws LauncherPersistenceException
	 */
	abstract protected DatabaseResponse<E> executeOperation( Connection connection ) throws LauncherPersistenceException;

	protected long getDatabaseId( Connection connection, String database )
	{
		long id = 0;

		try( PreparedStatement statement = connection.prepareStatement( GET_DATABASE_ID_BY_NAME_STATEMENT ) )
		{
			statement.setString( 1, database );
	
			try( ResultSet result = statement.executeQuery() )
			{
				result.next();
	
				id = result.getLong( "ID" );
			}
		}
		catch( SQLException se )
		{
			//just return an id of 0
		}

		return id;
	}

	protected void throwEncapsulatingException( Exception ex ) throws LauncherPersistenceException
	{
		throw new LauncherPersistenceException( ex );
	}
}
