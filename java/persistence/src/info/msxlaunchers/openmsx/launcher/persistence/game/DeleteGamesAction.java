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

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.DefaultDatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

/**
 * Class to delete given games from given database
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class DeleteGamesAction extends TransactionalDatabaseOperation<Boolean>
{
	private static final String DELETE_GAME_STATEMENT = "DELETE FROM game WHERE name=? AND IDDB=?";

	private final Set<Game> games;
	private final String database;

	DeleteGamesAction( Set<Game> games, String database )
	{
		this.games = games;
		this.database = database;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DefaultDatabaseResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		long databaseId = getDatabaseId( connection, database );

		try( PreparedStatement statement = connection.prepareStatement( DELETE_GAME_STATEMENT ) )
		{
			for( Game game: games )
			{
				statement.setString( 1, game.getName() );
				statement.setLong( 2, databaseId );

				statement.addBatch();
			}

			statement.executeBatch();
		}
		catch( SQLException se )
		{
			LauncherLogger.logException( this, se );

			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		return new DefaultDatabaseResponse();
	}
}
