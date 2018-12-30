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
package info.msxlaunchers.openmsx.launcher.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersister;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterPersister;
import info.msxlaunchers.openmsx.launcher.persistence.game.DerbyLogSuppressor;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.persistence.search.GameFinder;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;

/**
 * Implementation of the <code>LauncherPersistence</code> interface that persists data and retrieves them from an embedded
 * database. This class is designated with Guice's Singleton annotation to keep only one instance of it in the lifetime
 * of the Guice injector (which is the lifetime of the entire application)
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
@Singleton
final class EmbeddedDatabaseLauncherPersistence implements LauncherPersistence
{
	private static final String CREATE_DATABASE_TABLE_STATEMENT = "CREATE TABLE database (ID BIGINT not null generated always as identity," +
			" name VARCHAR(64) not null unique, primary key (ID))";
	private static final String CREATE_DATABASE_BACKUP_TABLE_STATEMENT = "CREATE TABLE database_backup (ID BIGINT not null generated always as identity," +
			" time TIMESTAMP not null, IDDB BIGINT not null, primary key (ID))";
	private static final String ADD_FOREIGN_KEY_TO_DATABASE_BACKUP_TABLE = "ALTER TABLE database_backup ADD CONSTRAINT DATABASE_FK Foreign Key (IDDB) REFERENCES database (ID) ON DELETE CASCADE";
	private static final String GAME_TABLE_DEF = " (ID BIGINT not null generated always as identity," +
			"name VARCHAR(128) not null, info VARCHAR(512), machine VARCHAR(64) not null," +
			"romA VARCHAR(512), extension_rom VARCHAR(20), romB VARCHAR(512)," +
			"diskA VARCHAR(512), diskB VARCHAR(512), tape VARCHAR(512), harddisk VARCHAR(512), laserdisc VARCHAR(512), tcl_script VARCHAR(512)," + 
			"msx BOOLEAN default false, msx2 BOOLEAN default false, msx2plus BOOLEAN default false, turbo_r BOOLEAN default false," +
			"psg BOOLEAN default false, scc BOOLEAN default false, scc_i BOOLEAN default false, pcm BOOLEAN default false," +
			"msx_music BOOLEAN default false, msx_audio BOOLEAN default false, moonsound BOOLEAN default false, midi BOOLEAN default false," +
			"genre1 INTEGER, genre2 INTEGER, msx_genid INTEGER, screenshot_suffix VARCHAR(10), sha1 VARCHAR(40), size BIGINT," +
			"IDDB BIGINT not null, primary key (ID), fdd_mode SMALLINT, tcl_script_override BOOLEAN default true," +
			"input_device SMALLINT, connect_gfx9000 BOOLEAN default false)";
	private static final String CREATE_GAME_TABLE_STATEMENT = "CREATE TABLE game" + GAME_TABLE_DEF;
	private static final String ADD_FOREIGN_KEY_TO_GAME_TABLE = "ALTER TABLE game ADD CONSTRAINT DATABASE_GAME_FK Foreign Key (IDDB) REFERENCES database (ID) ON DELETE CASCADE";
	private static final String ADD_UNIQUE_CONSTRAINT_TO_GAME_TABLE = "ALTER TABLE game ADD CONSTRAINT UNIQUE_GAMENAME UNIQUE(name,IDDB)";
	private static final String CREATE_GAME_BACKUP_TABLE_STATEMENT = "CREATE TABLE game_backup" + GAME_TABLE_DEF;
	private static final String ADD_FOREIGN_KEY_TO_GAME_BACKUP_TABLE = "ALTER TABLE game_backup ADD CONSTRAINT DATABASE_BAK_FK Foreign Key (IDDB) REFERENCES database_backup (ID) ON DELETE CASCADE";
	private static final String CREATE_FAVORITE_TABLE_STATEMENT = "CREATE TABLE favorite (ID BIGINT not null generated always as identity, IDGAME BIGINT not null unique, primary key (ID))";
	private static final String ADD_FOREIGN_KEY_TO_FAVORITE_TABLE = "ALTER TABLE favorite ADD CONSTRAINT GAME_FK Foreign Key (IDGAME) REFERENCES game (ID) ON DELETE CASCADE";

	//upgrade statements
	private static final String ADD_INPUT_DEVICE_COLUMN_TO_GAME = "ALTER TABLE game ADD COLUMN input_device SMALLINT";
	private static final String ADD_INPUT_DEVICE_COLUMN_TO_GAME_BACKUP = "ALTER TABLE game_backup ADD COLUMN input_device SMALLINT";
	private static final String ADD_CONNECT_GFX9000_COLUMN_TO_GAME = "ALTER TABLE game ADD COLUMN connect_gfx9000 BOOLEAN default false";
	private static final String ADD_CONNECT_GFX9000_COLUMN_TO_GAME_BACKUP = "ALTER TABLE game_backup ADD COLUMN connect_gfx9000 BOOLEAN default false";

	private static final String COLUMN_ALREADY_EXISTS_ERROR_CODE = "X0Y32";

	private final GamePersister gamePersister;
	private final FavoritePersister favoritePersister;
	private final FilterPersister filterPersister;
	private final SettingsPersister settingsPersister;
	private final GameFinder gameFinder;
	private final File databasesDirectory;
	private final String databaseFullPath;

	@Inject
	EmbeddedDatabaseLauncherPersistence( GamePersister gamePersister,
			FavoritePersister favoritePersister,
			FilterPersister filterPersister,
			SettingsPersister settingsPersister,
			GameFinder gameFinder,
			@Named("UserDataDirectory") String userDataDirectory,
			@Named("DatabasesDirectoryName") String databasesDirectoryName,
			@Named("EmbeddedDatabaseFullPath") String databaseFullPath )
	{
		this.gamePersister = gamePersister;
		this.favoritePersister = favoritePersister;
		this.filterPersister = filterPersister;
		this.settingsPersister = settingsPersister;
		this.gameFinder = gameFinder;
		this.databasesDirectory = new File( userDataDirectory, databasesDirectoryName );
		this.databaseFullPath = databaseFullPath;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence#initialize()
	 */
	@Override
	public void initialize() throws LauncherPersistenceException
	{
		//disable Derby logging
		System.setProperty( "derby.stream.error.method", DerbyLogSuppressor.class.getName() + ".getDevNull" );

		if( !databasesDirectory.exists() )
		{
			databasesDirectory.mkdir();
		}

		String dbURL = "jdbc:derby:" + databaseFullPath + ";create=true";

		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
			try
			{
				if( connection.getWarnings() == null )
				{
					//then this database did not exist before => create all tables
					createTables( connection );
				}
				else
				{
					//then might be the upgrade case
					//first case to deal with is FDDMode column in Games table (new in v1.11)
					addInputDeviceColumnIfNecessary( connection );

					//second case to deal with is TCLScriptOverride column in Games table (new in v1.11)
					addConnectGFX9000ColumnIfNecessary( connection );
				}
			}
			catch( SQLException se )
			{
				connection.rollback();
				throw new LauncherPersistenceException();
			}
		}
    	catch( SQLException se )
    	{
    		//TODO What to do?
    	}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence#shutdown()
	 */
	@Override
	public void shutdown() throws LauncherPersistenceException
	{
		String dbURL = "jdbc:derby:;shutdown=true";

		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
		}
    	catch( SQLException se )
    	{
    		//Ignore
    	}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence#getGamePersister()
	 */
	@Override
	public GamePersister getGamePersister()
	{
		return gamePersister;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence#getFavoritePersister()
	 */
	@Override
	public FavoritePersister getFavoritePersister()
	{
		return favoritePersister;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence#getFiltersPersister()
	 */
	@Override
	public FilterPersister getFiltersPersister()
	{
		return filterPersister;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence#getSettingsPersister()
	 */
	@Override
	public SettingsPersister getSettingsPersister()
	{
		return settingsPersister;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence#getGameFinder()
	 */
	@Override
	public GameFinder getGameFinder()
	{
		return gameFinder;
	}

	private void createTables( Connection connection ) throws SQLException
	{
		createDatabaseTable( connection );
		createGameTable( connection );
		createDatabaseBackupTable( connection );
		createGameBackupTable( connection );
		createFavoriteTable( connection );
	}

	private void createDatabaseTable( Connection connection ) throws SQLException
	{
		try( Statement statement = connection.createStatement() )
		{
			statement.execute( CREATE_DATABASE_TABLE_STATEMENT );
		}
	}

	private void createGameTable( Connection connection ) throws SQLException
	{
		try( Statement statement = connection.createStatement() )
		{
			statement.execute( CREATE_GAME_TABLE_STATEMENT );
			statement.execute( ADD_FOREIGN_KEY_TO_GAME_TABLE );
			statement.execute( ADD_UNIQUE_CONSTRAINT_TO_GAME_TABLE );
		}
	}

	private void createDatabaseBackupTable( Connection connection ) throws SQLException
	{
		try( Statement statement = connection.createStatement() )
		{
			statement.execute( CREATE_DATABASE_BACKUP_TABLE_STATEMENT );
			statement.execute( ADD_FOREIGN_KEY_TO_DATABASE_BACKUP_TABLE );
		}
	}

	private void createGameBackupTable( Connection connection ) throws SQLException
	{
		try( Statement statement = connection.createStatement() )
		{
			statement.execute( CREATE_GAME_BACKUP_TABLE_STATEMENT );
			statement.execute( ADD_FOREIGN_KEY_TO_GAME_BACKUP_TABLE );
		}
	}

	private void createFavoriteTable( Connection connection ) throws SQLException
	{
		try( Statement statement = connection.createStatement() )
		{
			statement.execute( CREATE_FAVORITE_TABLE_STATEMENT );
			statement.execute( ADD_FOREIGN_KEY_TO_FAVORITE_TABLE );
		}
	}

	private void addInputDeviceColumnIfNecessary( Connection connection ) throws SQLException
	{
		try( Statement statement = connection.createStatement() )
		{
			statement.execute( ADD_INPUT_DEVICE_COLUMN_TO_GAME );
			statement.execute( ADD_INPUT_DEVICE_COLUMN_TO_GAME_BACKUP );
		}
		catch( SQLException se )
		{
			if( !se.getSQLState().equals( COLUMN_ALREADY_EXISTS_ERROR_CODE ) )
			{
				//if we get an exception other than 'column already exists' then rethrow it
				LauncherLogger.logException( this, se );

				throw se;
			}
		}
	}

	private void addConnectGFX9000ColumnIfNecessary( Connection connection ) throws SQLException
	{
		try( Statement statement = connection.createStatement() )
		{
			statement.execute( ADD_CONNECT_GFX9000_COLUMN_TO_GAME );
			statement.execute( ADD_CONNECT_GFX9000_COLUMN_TO_GAME_BACKUP );
		}
		catch( SQLException se )
		{
			if( !se.getSQLState().equals( COLUMN_ALREADY_EXISTS_ERROR_CODE ) )
			{
				//if we get an exception other than 'column already exists' then rethrow it
				LauncherLogger.logException( this, se );

				throw se;
			}
		}
	}
}
