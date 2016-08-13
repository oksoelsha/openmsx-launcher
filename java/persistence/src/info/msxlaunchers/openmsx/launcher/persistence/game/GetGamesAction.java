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
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
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
 * Class to get all games in a given database
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class GetGamesAction extends NonTransactionalDatabaseOperation<Set<Game>>
{
	private static final String GET_ALL_GAMES_STATEMENT = "SELECT * FROM game WHERE IDDB=?";

	private final String database;

	GetGamesAction( String database )
	{
		this.database = database;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.NonTransactionalDatabaseOperation#executeNonTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DatabaseResponse<Set<Game>> executeNonTransactionalOperation( Connection connection) throws LauncherPersistenceException
	{
		Set<Game> games = new HashSet<Game>();

		long databaseId = getDatabaseId( connection, database );

		try( PreparedStatement statement = connection.prepareStatement( GET_ALL_GAMES_STATEMENT ) )
		{
			statement.setLong( 1, databaseId );

			try( ResultSet result = statement.executeQuery() )
			{
				while( result.next() )
				{
					games.add( getGameFromResultSet( result ) );
				}
			}
		}
		catch( SQLException se )
		{
			//there's no valid reason for this so ignore - method will return an empty Set
			LauncherLogger.logException( this, se );
		}

		return new GetGamesResponse( Collections.unmodifiableSet( games ) );
	}

	private Game getGameFromResultSet( ResultSet result ) throws SQLException
	{
		return Game.name( result.getString( "name" ) )
				.info( result.getString( "info" ) )
				.machine( result.getString( "machine" ) )
				.romA( result.getString( "romA" ) )
				.extensionRom( result.getString( "extension_rom" ) )
				.romB( result.getString( "romB" ) )
				.diskA( result.getString( "diskA" ) )
				.diskB( result.getString( "diskB" ) )
				.tape( result.getString( "tape" ) )
				.harddisk( result.getString( "harddisk" ) )
				.laserdisc( result.getString( "laserdisc" ) )
				.tclScript( result.getString( "tcl_script" ) )
				.isMSX( result.getBoolean( "msx" ) )
				.isMSX2( result.getBoolean( "msx2" ) )
				.isMSX2Plus( result.getBoolean( "msx2plus" ) )
				.isTurboR( result.getBoolean( "turbo_r" ) )
				.isPSG( result.getBoolean( "psg" ) )
				.isSCC( result.getBoolean( "scc" ) )
				.isSCCI( result.getBoolean( "scc_i" ) )
				.isPCM( result.getBoolean( "pcm" ) )
				.isMSXMUSIC( result.getBoolean( "msx_music" ) )
				.isMSXAUDIO( result.getBoolean( "msx_audio" ) )
				.isMoonsound( result.getBoolean( "moonsound" ) )
				.isMIDI( result.getBoolean( "midi" ) )
				.genre1( Genre.fromValue( result.getInt( "genre1" ) ) )
				.genre2( Genre.fromValue( result.getInt( "genre2" ) ) )
				.msxGenID( result.getInt( "msx_genid" ) )
				.screenshotSuffix( result.getString( "screenshot_suffix" ) )
				.sha1Code( result.getString( "sha1" ) )
				.size( result.getLong( "size" ) )
				.fddMode( FDDMode.fromValue( result.getShort( "fdd_mode" )) )
				.build();
	}
}
