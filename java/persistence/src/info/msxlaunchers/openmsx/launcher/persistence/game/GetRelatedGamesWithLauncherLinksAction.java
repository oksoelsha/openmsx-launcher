/*
 * Copyright 2020 Sam Elsharif
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

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.DatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.NonTransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to get an updated list of related games that contain the launcher game's name and database if it exists in the launcher
 * 
 * @since v1.14
 * @author Sam Elsharif
 *
 */
final class GetRelatedGamesWithLauncherLinksAction extends NonTransactionalDatabaseOperation<List<RelatedGame>>
{
	private static final String GET_RELATED_GAMES_STATEMENT = "SELECT g.name, d.name FROM game g join database d on g.IDDB=d.ID WHERE g.msx_genid=?";

	private final List<RelatedGame> relatedGames;

	GetRelatedGamesWithLauncherLinksAction( List<RelatedGame> relatedGames )
	{
		this.relatedGames = relatedGames;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.NonTransactionalDatabaseOperation#executeNonTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DatabaseResponse<List<RelatedGame>> executeNonTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		List<RelatedGame> updatedRelatedGames = new ArrayList<>( relatedGames.size() );

		for( RelatedGame relatedGame: relatedGames )
		{
			try( PreparedStatement statement = connection.prepareStatement( GET_RELATED_GAMES_STATEMENT ) )
			{
				statement.setInt( 1, relatedGame.getMSXGenId() );

				try( ResultSet result = statement.executeQuery() )
				{
					if( result.next() )
					{
						DatabaseItem databaseItem = new DatabaseItem( result.getString( 1 ), result.getString( 2 ) );
						RelatedGame updatedRelatedGame = new RelatedGame( relatedGame.getGameName(), relatedGame.getCompany(),
								relatedGame.getYear(), relatedGame.getMSXGenId(), databaseItem );
						updatedRelatedGames.add( updatedRelatedGame );
					}
					else
					{
						updatedRelatedGames.add( relatedGame );
					}
				}
			}
			catch( SQLException se )
			{
				//No reason for this to happen but if it does return the same relatedObject
				updatedRelatedGames.add( relatedGame );
				LauncherLogger.logException( this, se );
			}
		}

		return new GetRelatedGamesWithLauncherLinksResponse( Collections.unmodifiableList( updatedRelatedGames ) );
	}
}
