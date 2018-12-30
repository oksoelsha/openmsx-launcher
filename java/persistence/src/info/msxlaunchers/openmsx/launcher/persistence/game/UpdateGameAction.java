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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to update game
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class UpdateGameAction extends AbstractPersistGameAction
{
	private static final String GET_GAME_ID_BY_NAME_STATEMENT = "SELECT ID FROM game WHERE name=? AND IDDB=?";
	private static final String UPDATE_GAME_STATEMENT = "UPDATE game SET name=?, info=?, machine=?, romA=?, extension_rom=?, romB=?," +
			"diskA=?, diskB=?, tape=?, harddisk=?, laserdisc=?, tcl_script=?," +
			"msx=?, msx2=?, msx2plus=?, turbo_r=?, psg=?, scc=?, scc_i=?, pcm=?," +
			"msx_music=?, msx_audio=?, moonsound=?, midi=?, genre1=?, genre2=?," +
			"msx_genid=?, screenshot_suffix=?, sha1=?, size=?, IDDB=?, fdd_mode=?, tcl_script_override=?," +
			"input_device=?, connect_gfx9000=? WHERE id=?";

	private final Game oldGame;
	private final Game newGame;
	private final String database;

	UpdateGameAction( Game oldGame, Game newGame, String database )
	{
		this.oldGame = oldGame;
		this.newGame = newGame;
		this.database = database;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DefaultDatabaseResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		validateGame( newGame );

		long databaseId = getDatabaseId( connection, database );

		try( PreparedStatement statementCheck = connection.prepareStatement( GET_GAME_ID_BY_NAME_STATEMENT ) )
		{
			statementCheck.setString( 1, oldGame.getName() );
			statementCheck.setLong( 2, databaseId );

			try( ResultSet result = statementCheck.executeQuery() )
			{
				if( !result.next() )
				{
					throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_NOT_FOUND, oldGame.getName() ) );
				}
				else
				{
					long gameId = result.getInt( 1 );

					try ( PreparedStatement statementUpdate = connection.prepareStatement( UPDATE_GAME_STATEMENT ) )
					{
						setGameStatementFields( statementUpdate, newGame, databaseId );
						statementUpdate.setLong( 36, gameId );

						if( statementUpdate.executeUpdate() == 0 )
						{
							//this shouldn't happen
						}
					}
					catch( SQLException se )
					{
						if( isDuplicateError( se ) )
						{
							throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_ALREADY_EXISTS, newGame.getName() ) );
						}
						else
						{
							LauncherLogger.logException( this, se );

							throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
						}
					}
				}
			}
		}
		catch( SQLException se )
		{
			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		return new DefaultDatabaseResponse();
	}
}
