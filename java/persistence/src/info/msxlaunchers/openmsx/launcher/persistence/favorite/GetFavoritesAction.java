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
package info.msxlaunchers.openmsx.launcher.persistence.favorite;

import info.msxlaunchers.openmsx.launcher.data.favorite.Favorite;
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
 * Class to get all favorites
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class GetFavoritesAction extends NonTransactionalDatabaseOperation<Set<Favorite>>
{
	private static final String GET_ALL_FAVORITES_STATEMENT = "SELECT game.name AS gameName, database.name AS database FROM database JOIN game" +
			" ON database.id=game.IDDB join favorite ON favorite.IDGAME=game.ID";

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.NonTransactionalDatabaseOperation#executeNonTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DatabaseResponse<Set<Favorite>> executeNonTransactionalOperation( Connection connection) throws LauncherPersistenceException
	{
		Set<Favorite> favorites = new HashSet<Favorite>();

		try( PreparedStatement statement = connection.prepareStatement( GET_ALL_FAVORITES_STATEMENT ) )
		{
			try( ResultSet result = statement.executeQuery() )
			{
				while( result.next() )
				{
					favorites.add( new Favorite( result.getString( "gameName" ), result.getString( "database" ) ) );
				}
			}
		}
		catch( SQLException se )
		{
			//ignore - method will return an empty Set
		}

		return new GetFavoritesResponse( Collections.unmodifiableSet( favorites ) );
	}
}
