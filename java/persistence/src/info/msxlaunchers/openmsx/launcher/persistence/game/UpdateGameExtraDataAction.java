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

import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class to update extra data in all games in all databases
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class UpdateGameExtraDataAction extends TransactionalDatabaseOperation<Integer>
{
	private static final String UPDATE_GAME_EXTRA_FIELDS_STATEMENT = "UPDATE game SET msx=?, msx2=?, msx2plus=?, turbo_r=?, psg=?, scc=?, scc_i=?, pcm=?," +
			"msx_music=?, msx_audio=?, moonsound=?, midi=?, genre1=?, genre2=?, msx_genid=?, screenshot_suffix=?" +
			" WHERE name=? and IDDB=?";

	private final GameBuilder gameBuilder;
	private final Map<String,ExtraData> extraDataMap;

	UpdateGameExtraDataAction( GameBuilder gameBuilder, Map<String,ExtraData> extraDataMap )
	{
		this.gameBuilder = gameBuilder;
		this.extraDataMap = extraDataMap;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public UpdateGameExtraDataResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		Set<String> databases = new GetDatabasesAction().executeNonTransactionalOperation( connection ).getResult();

		int numberUpdatedProfiles = 0;

		Map<String, Set<Game>> updatedGamesMap = new HashMap<>();

		for( String database: databases )
		{
			boolean databaseFound = false;
			Set<Game> updatedGames = null;

			try
			{
				Set<Game> games = new GetGamesAction( database ).executeNonTransactionalOperation( connection ).getResult();
				databaseFound = true;
				updatedGames = new HashSet<Game>();

				for( Game game: games )
				{
					Game newGame = gameBuilder.createGameObjectFromGameAndUpdateExtraData( game, extraDataMap );

					//compare new and old to see if the extra data were updated
					if( !newGame.isExtraDataEqual( game ) )
					{
						updatedGames.add( newGame );
						numberUpdatedProfiles++;
					}
				}
			}
			catch( LauncherPersistenceException lpe )
			{
				//shouldn't happen in normal circumstances - simply skip - try the next one
				databaseFound = false;
			}

			if( databaseFound )
			{
				updatedGamesMap.put( database, updatedGames );
			}
		}

		try ( PreparedStatement statement = connection.prepareStatement( UPDATE_GAME_EXTRA_FIELDS_STATEMENT ) )
		{
			for( Map.Entry<String,Set<Game>> entry: updatedGamesMap.entrySet() )
			{
				long databaseId = getDatabaseId( connection, entry.getKey() );

				for( Game game: entry.getValue() )
				{
					statement.setBoolean( 1, game.isMSX() );
					statement.setBoolean( 2, game.isMSX2() );
					statement.setBoolean( 3, game.isMSX2Plus() );
					statement.setBoolean( 4, game.isTurboR() );
					statement.setBoolean( 5, game.isPSG() );
					statement.setBoolean( 6, game.isSCC() );
					statement.setBoolean( 7, game.isSCCI() );
					statement.setBoolean( 8, game.isPCM() );
					statement.setBoolean( 9, game.isMSXMUSIC() );
					statement.setBoolean( 10, game.isMSXAUDIO() );
					statement.setBoolean( 11, game.isMoonsound() );
					statement.setBoolean( 12, game.isMIDI() );
					statement.setInt( 13, getGenreEnumValue( game.getGenre1() ) );
					statement.setInt( 14, getGenreEnumValue( game.getGenre2() ) );
					statement.setInt( 15,  game.getMsxGenID() );
					statement.setString( 16 , game.getScreenshotSuffix() );
					statement.setString( 17, game.getName() );
					statement.setLong( 18, databaseId );

					statement.addBatch();
				}
			}

			statement.executeBatch();
		}
		catch( SQLException se )
		{
			LauncherLogger.logException( this, se );

			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		return new UpdateGameExtraDataResponse( numberUpdatedProfiles );
	}
}
