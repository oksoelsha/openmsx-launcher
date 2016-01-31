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
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to move given games from one database to another
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class MoveGamesAction extends TransactionalDatabaseOperation<Set<Game>>
{
	private static final String MOVE_GAME_STATEMENT = "UPDATE game SET IDDB=? WHERE name=? and IDDB=?";
	private static final String DELETE_OVERRIDDEN_GAMES_STATEMENT = "DELETE FROM game WHERE name=? and IDDB=?";

	private final Set<Game> games;
	private final String oldDatabase;
	private final String newDatabase;
	private final ActionDecider actionDecider;

	MoveGamesAction( Set<Game> games, String oldDatabase, String newDatabase, ActionDecider actionDecider )
	{
		this.games = games;
		this.oldDatabase = oldDatabase;
		this.newDatabase = newDatabase;
		this.actionDecider = actionDecider;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public MoveGamesResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		Set<Game> movedGames = new HashSet<>();
		Set<String> overriddenGameNames = new HashSet<>();

		Set<Game> destinationDatabaseCache = new GetGamesAction( newDatabase ).executeNonTransactionalOperation( connection ).getResult();

		for( Game game: games )
		{
			if( destinationDatabaseCache.contains( game ) )
			{
				if( !actionDecider.isYesAll() && !actionDecider.isNoAll() )
				{
					actionDecider.promptForAction( game.getName() );
				}

				if( actionDecider.isYes() || actionDecider.isYesAll() )
				{
					movedGames.add( game );
					overriddenGameNames.add( game.getName() );
				}
				else if( actionDecider.isNo() || actionDecider.isNoAll() )
				{
					//do nothing - skip
				}
				else if( actionDecider.isCancel() )
				{
					break;
				}
				else
				{
					//this should not happen
					throw new RuntimeException( "At least one action must be set" );
				}
			}
			else
			{
				movedGames.add( game );
			}
		}

		//add the moved games to the new database and remove them from the old database
		long oldDatabaseId = getDatabaseId( connection, oldDatabase );
		long newDatabaseId = getDatabaseId( connection, newDatabase );

		try( PreparedStatement statement = connection.prepareStatement( DELETE_OVERRIDDEN_GAMES_STATEMENT ) )
		{
			for( String gameName: overriddenGameNames )
			{
				statement.setString( 1, gameName );
				statement.setLong( 2, newDatabaseId );

				statement.addBatch();
			}

			statement.executeBatch();
		}
		catch( SQLException se )
		{
			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		try( PreparedStatement statement = connection.prepareStatement( MOVE_GAME_STATEMENT ) )
		{
			for( Game game: movedGames )
			{
				statement.setLong( 1, newDatabaseId );
				statement.setString( 2, game.getName() );
				statement.setLong( 3, oldDatabaseId );

				statement.addBatch();
			}

			statement.executeBatch();
		}
		catch( SQLException se )
		{
			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		return new MoveGamesResponse( movedGames );
	}
}
