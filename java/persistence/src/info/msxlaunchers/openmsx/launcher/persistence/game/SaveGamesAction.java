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

import info.msxlaunchers.openmsx.common.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.persistence.DefaultDatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

/**
 * Class to save given games in a given database
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class SaveGamesAction extends AbstractPersistGameAction
{
	private static final String INSERT_GAME_STATEMENT = "INSERT INTO game (name, info, machine, romA, extension_rom, romB, diskA, diskB," +
			"tape, harddisk, laserdisc, tcl_script, msx, msx2, msx2plus, turbo_r, psg, scc, scc_i, pcm, msx_music, msx_audio, moonsound, midi," +
			"genre1, genre2, msx_genid, screenshot_suffix, sha1, size, IDDB, fdd_mode) " +
			"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private final Set<Game> games;
	private final String database;

	SaveGamesAction( Set<Game> games, String database )
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
		String duplicateGame = null;

		try( PreparedStatement statement = connection.prepareStatement( INSERT_GAME_STATEMENT ) )
		{
			long databaseId = getDatabaseId( connection, database );

			for( Game game: games )
			{
				validateGame( game );

				//potential duplicate name
				duplicateGame = game.getName();

				setGameStatementFields( statement, game, databaseId );

				statement.addBatch();
			}

			statement.executeBatch();
		}
		catch( SQLException se )
		{
			if( isDuplicateError( se ) )
			{
				//abort the whole operation
				throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_ALREADY_EXISTS, duplicateGame ) );
			}
			else
			{
				LauncherLogger.logException( this, se );

				throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
			}
		}

		return new DefaultDatabaseResponse();
	}
}
