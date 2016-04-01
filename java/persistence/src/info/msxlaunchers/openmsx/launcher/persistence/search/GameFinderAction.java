/*
 * Copyright 2016 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.persistence.search;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.persistence.DatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.NonTransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to find games in the database given a string. The search is for partial match and in all
 * applicable fields
 * 
 * @since v1.6
 * @author Sam Elsharif
 *
 */
final class GameFinderAction extends NonTransactionalDatabaseOperation<Set<DatabaseItem>>
{
	private static final String GET_MATCHES_STATEMENT = "SELECT game.name AS gameName,"
			+ " database.name AS database FROM database JOIN game" +
			" ON database.id=game.IDDB AND (UPPER(game.name) like UPPER(?) OR game.sha1 like ?)";
	private final String string;
	private final int maximumMatches;
	GameFinderAction( String string, int maximumMatches )
	{
		this.string = string;
		this.maximumMatches = maximumMatches;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.NonTransactionalDatabaseOperation#executeNonTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DatabaseResponse<Set<DatabaseItem>> executeNonTransactionalOperation( Connection connection) throws LauncherPersistenceException
	{
		Set<DatabaseItem> matches = new HashSet<DatabaseItem>();

		try( PreparedStatement statement = connection.prepareStatement( GET_MATCHES_STATEMENT ) )
		{
			statement.setString( 1, "%" + string + "%" );
			statement.setString( 2, "%" + string.toLowerCase() + "%" );
			statement.setMaxRows( maximumMatches );

			try( ResultSet result = statement.executeQuery() )
			{
				while( result.next() )
				{
					matches.add( new DatabaseItem( result.getString( "gameName" ), result.getString( "database" ) ) );
				}
			}
		}
		catch( SQLException se )
		{
			//ignore - method will return an empty Set
		}

		return new GameFinderResponse( Collections.unmodifiableSet( matches ) );
	}
}
