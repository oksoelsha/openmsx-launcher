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
import info.msxlaunchers.openmsx.launcher.persistence.DefaultDatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to restore a database backup
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
final class RestoreDatabaseBackupAction extends TransactionalDatabaseOperation<Boolean>
{
	private static final String DELETE_CURRENT_GAMES_STATEMENT = "DELETE FROM game where IDDB=?";
	private static final String GET_BACKUP_ID_STATEMENT = "SELECT ID FROM database_backup where IDDB=? and time=?";
	private static final String RESTORE_GAMES_STATEMENT = "INSERT INTO game (name, info, machine, romA, extension_rom, romB, " +
			"diskA, diskB, tape, harddisk, laserdisc, tcl_script, msx, msx2, msx2plus, turbo_r, " +
			"psg, scc, scc_i, pcm, msx_music, msx_audio, moonsound, midi, genre1, genre2, msx_genid, screenshot_suffix, sha1, size, " +
			"IDDB, fdd_mode, tcl_script_override, input_device, connect_gfx9000) " +
			"SELECT name, info, machine, romA, extension_rom, romB, " +
			"diskA, diskB, tape, harddisk, laserdisc, tcl_script, " +
			"msx, msx2, msx2plus, turbo_r, " +
			"psg, scc, scc_i, pcm, msx_music, msx_audio, moonsound, midi," +
			"genre1, genre2, msx_genid, screenshot_suffix, sha1, size, ?, fdd_mode, tcl_script_override, input_device, connect_gfx9000 " + 
			"FROM game_backup where IDDB=?";
	private static final String DELETE_BACKUP_STATEMENT = "DELETE FROM database_backup where ID=?";

	private final DatabaseBackup backup;

	RestoreDatabaseBackupAction( DatabaseBackup backup )
	{
		this.backup = backup;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DefaultDatabaseResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		//first get the current backup
		long databaseId = getDatabaseId( connection, backup.getDatabase() );

		try( PreparedStatement deleteCurrentGamesStatement = connection.prepareStatement( DELETE_CURRENT_GAMES_STATEMENT );				
				PreparedStatement getBackupIDStatement = connection.prepareStatement( GET_BACKUP_ID_STATEMENT );
				PreparedStatement restoreGamesStatement = connection.prepareStatement( RESTORE_GAMES_STATEMENT );
				PreparedStatement deleteBackupStatement = connection.prepareStatement( DELETE_BACKUP_STATEMENT ) )
		{
			//first delete the current games
			deleteCurrentGamesStatement.setLong( 1, databaseId );
			deleteCurrentGamesStatement.executeUpdate();

			//get the backup ID
			getBackupIDStatement.setLong( 1, databaseId );
			getBackupIDStatement.setTimestamp( 2, backup.getTimestamp() );
			long databaseBackupId = 0;
			try( ResultSet result = getBackupIDStatement.executeQuery() )
			{
				result.next();
				databaseBackupId = result.getLong( "ID" );
			}
			catch( SQLException se )
			{
				//just return an id of 0
			}

			if( databaseBackupId == 0 )
			{
				throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.BACKUP_NOT_FOUND, backup.getTimestamp().toString() ) );
			}

			//restore the games by coping them from the games backup to the games table
			restoreGamesStatement.setLong( 1, databaseId );
			restoreGamesStatement.setLong( 2, databaseBackupId );
			restoreGamesStatement.executeUpdate();

			//finally delete the backup itself since it was restored
			deleteBackupStatement.setLong( 1, databaseBackupId );
			deleteBackupStatement.executeUpdate();
		}
		catch( SQLException se )
		{
			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
		}

		return new DefaultDatabaseResponse();
	}
}
