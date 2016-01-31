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

import info.msxlaunchers.openmsx.launcher.persistence.DatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.NonTransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to get all game databases
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class GetDatabasesAction extends NonTransactionalDatabaseOperation<Set<String>>
{
	private static final String GET_ALL_DATABASES_STATEMENT = "SELECT * FROM database";

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.NonTransactionalDatabaseOperation#executeNonTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DatabaseResponse<Set<String>> executeNonTransactionalOperation( Connection connection )
	{
		Set<String> databases = new HashSet<String>();

		try( Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery( GET_ALL_DATABASES_STATEMENT ) )
		{
			while( result.next() )
			{
				databases.add( result.getString( "name" ) );
			}
		}
		catch( SQLException se )
		{
			//ignore - method will return an empty Set
		}

		return new GetDatabasesResponse( databases );
	}
}
