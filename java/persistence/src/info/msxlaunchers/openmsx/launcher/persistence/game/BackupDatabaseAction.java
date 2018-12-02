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

import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Class to back up games database
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class BackupDatabaseAction extends TransactionalDatabaseOperation<DatabaseBackup>
{
	private static final String GET_CURRENT_BACKUPS_TOTAL_STATEMENT="SELECT COUNT(time) AS rowCount FROM database_backup where IDDB=?";
	private static final String CREATE_BACKUP_DATABASE_NUMBER_STATEMENT = "INSERT INTO database_backup (time, IDDB) VALUES(?, ?)";
	private static final String BACKUP_GAMES_STATEMENT = "INSERT INTO game_backup (name, info, machine, romA, extension_rom, romB, " +
			"diskA, diskB, tape, harddisk, laserdisc, tcl_script, msx, msx2, msx2plus, turbo_r, " +
			"psg, scc, scc_i, pcm, msx_music, msx_audio, moonsound, midi, genre1, genre2, msx_genid, screenshot_suffix, sha1, size, IDDB, fdd_mode, tcl_script_override) " +
			"SELECT game.name, game.info, game.machine, game.romA, game.extension_rom, game.romB, " +
			"game.diskA, game.diskB, game.tape, game.harddisk, game.laserdisc, game.tcl_script, " +
			"game.msx, game.msx2, game.msx2plus, game.turbo_r, " +
			"game.psg, game.scc, game.scc_i, game.pcm, game.msx_music, game.msx_audio, game.moonsound, game.midi," +
			"game.genre1, game.genre2, game.msx_genid, game.screenshot_suffix, game.sha1, game.size, ?, game.fdd_mode, game.tcl_script_override " +
			"FROM game where game.IDDB=?";

	private final int MAX_DATABASE_BACKUP_NUMBER = 10;

	private final String database;

	BackupDatabaseAction( String database )
	{
		this.database = database;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public BackupDatabaseResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		//first get the current backup
		long databaseId = getDatabaseId( connection, database );

		try( PreparedStatement statement = connection.prepareStatement( GET_CURRENT_BACKUPS_TOTAL_STATEMENT ) )
		{
			statement.setLong( 1, databaseId );

			try( ResultSet result = statement.executeQuery() )
			{
				if( result.next() )
				{
					if( result.getInt( "rowCount" ) >= MAX_DATABASE_BACKUP_NUMBER )
					{
						throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_MAX_BACKUPS_REACHED, database ) );
					}
				}
			}
		}
		catch( SQLException se )
		{
			LauncherLogger.logException( this, se );

			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		Timestamp currentTimestamp = new Timestamp( Calendar.getInstance().getTime().getTime() );

		try( PreparedStatement backupNumberStatement = connection.prepareStatement( CREATE_BACKUP_DATABASE_NUMBER_STATEMENT, Statement.RETURN_GENERATED_KEYS  );
				PreparedStatement backupGamesStatement = connection.prepareStatement( BACKUP_GAMES_STATEMENT ) )
		{
			backupNumberStatement.setTimestamp( 1, currentTimestamp );
			backupNumberStatement.setLong( 2, databaseId );

			backupNumberStatement.executeUpdate();

			try( ResultSet generatedKeys = backupNumberStatement.getGeneratedKeys() )
			{
				generatedKeys.next();

				backupGamesStatement.setLong( 1, generatedKeys.getLong( 1 ) );
				backupGamesStatement.setLong( 2, databaseId );
			}

			backupGamesStatement.executeUpdate();
		}
		catch( SQLException se )
		{
			LauncherLogger.logException( this, se );

			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		return new BackupDatabaseResponse( new DatabaseBackup( database, currentTimestamp ) );
	}
}
