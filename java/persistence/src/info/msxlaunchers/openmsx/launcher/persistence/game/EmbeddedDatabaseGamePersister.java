/*
 * Copyright 2014 Sam Elsharif
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

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation of the <code>GamePersister</code> interface that persists games and retrieves them from an embedded
 * database. This class is designated with Guice's Singleton annotation to keep only one instance of it in the lifetime
 * of the Guice injector (which is the lifetime of the entire application)
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
@Singleton
final class EmbeddedDatabaseGamePersister implements GamePersister
{
	private final GameBuilder gameBuilder;
	private final String databaseFullPath;

	@Inject
	EmbeddedDatabaseGamePersister( @Named("EmbeddedDatabaseFullPath") String databaseFullPath, GameBuilder gameBuilder )
	{
		this.databaseFullPath = databaseFullPath;
		this.gameBuilder = gameBuilder;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#createDatabase(java.lang.String)
	 */
	@Override
	public void createDatabase( String database ) throws GamePersistenceException
	{
		if( Utils.isEmpty( database ) )
		{
			throw new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NULL_NAME );
		}

		try
		{
			new CreateDatabaseAction( database ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#deleteDatabase(java.lang.String)
	 */
	@Override
	public void deleteDatabase( String database ) throws GamePersistenceException
	{
		Objects.requireNonNull( database );

		try
		{
			new DeleteDatabaseAction( database ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#renameDatabase(java.lang.String, java.lang.String)
	 */
	@Override
	public void renameDatabase( String oldDatabase, String newDatabase ) throws GamePersistenceException
	{
		Objects.requireNonNull( newDatabase );
		Objects.requireNonNull( oldDatabase );

		try
		{
			new RenameDatabaseAction( oldDatabase, newDatabase ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#backupDatabase(java.lang.String)
	 */
	@Override
	public DatabaseBackup backupDatabase( String database ) throws GamePersistenceException
	{
		Objects.requireNonNull( database );

		try
		{
			return new BackupDatabaseAction( database ).execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#restoreBackup(info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup)
	 */
	@Override
	public void restoreBackup( DatabaseBackup backup ) throws GamePersistenceException
	{
		Objects.requireNonNull( backup );

		try
		{
			new RestoreDatabaseBackupAction( backup ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#deleteBackup(info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup)
	 */
	@Override
	public void deleteBackup( DatabaseBackup backup ) throws GamePersistenceException
	{
		Objects.requireNonNull( backup );

		try
		{
			new DeleteDatabaseBackupAction( backup ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#recreateDatabase(java.lang.String)
	 */
	@Override
	public void recreateDatabase( String database ) throws GamePersistenceException
	{
		Objects.requireNonNull( database );

		//recreating a database is equivalent to deleting all games in it
		Set<Game> games = getGames( database );
		deleteGames( games, database );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#getDatabases()
	 */
	@Override
	public Set<String> getDatabases()
	{
		Set<String> databases;
		try
		{
			databases = new GetDatabasesAction().execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException gpe )
		{
			databases = new HashSet<>();
		}

		return databases;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#getBackups(java.lang.String)
	 */
	@Override
	public Set<DatabaseBackup> getBackups( String database )
	{
		Set<DatabaseBackup> backups;
		try
		{
			backups = new GetDatabaseBackupsAction( database ).execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException gpe )
		{
			backups = new HashSet<>();
		}

		return Collections.unmodifiableSet( backups );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#updateGameExtraDataInDatabases(java.util.Map)
	 */
	@Override
	public int updateGameExtraDataInDatabases( Map<String,ExtraData> extraDataMap ) throws GamePersistenceException
	{
		Objects.requireNonNull( extraDataMap );

		try
		{
			return new UpdateGameExtraDataAction( gameBuilder, extraDataMap ).execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#getGames(java.lang.String)
	 */
	@Override
	public Set<Game> getGames( String database ) throws GamePersistenceException
	{
		try
		{
			return new GetGamesAction( database ).execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#saveGame(info.msxlaunchers.openmsx.launcher.data.game.Game, java.lang.String)
	 */
	@Override
	public void saveGame( Game game, String database ) throws GamePersistenceException
	{
		Objects.requireNonNull( database );
		Objects.requireNonNull( game );

		saveGames( Collections.singleton( game ), database );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#saveGames(java.util.Set, java.lang.String)
	 */
	@Override
	public void saveGames( Set<Game> games, String database ) throws GamePersistenceException
	{
		Objects.requireNonNull( games );
		Objects.requireNonNull( database );

		try
		{
			new SaveGamesAction( games, database ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#updateGame(info.msxlaunchers.openmsx.launcher.data.game.Game, info.msxlaunchers.openmsx.launcher.data.game.Game, java.lang.String)
	 */
	@Override
	public void updateGame( Game oldGame, Game newGame, String database ) throws GamePersistenceException
	{
		Objects.requireNonNull( oldGame );
		Objects.requireNonNull( newGame );
		Objects.requireNonNull( database );

		try
		{
			new UpdateGameAction( oldGame, newGame, database ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#deleteGame(info.msxlaunchers.openmsx.launcher.data.game.Game, java.lang.String)
	 */
	@Override
	public void deleteGame( Game game, String database ) throws GamePersistenceException
	{
		Objects.requireNonNull( game );

		deleteGames( Collections.singleton( game ), database );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#deleteGames(java.util.Set, java.lang.String)
	 */
	@Override
	public void deleteGames( Set<Game> games, String database ) throws GamePersistenceException
	{
		Objects.requireNonNull( games );
		Objects.requireNonNull( database );

		try
		{
			new DeleteGamesAction( games, database ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#moveGames(java.util.Set, java.lang.String, java.lang.String, info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider)
	 */
	@Override
	public Set<Game> moveGames( Set<Game> games, String oldDatabase, String newDatabase, ActionDecider actionDecider ) throws GamePersistenceException
	{
		Objects.requireNonNull( games );
		Objects.requireNonNull( oldDatabase );
		Objects.requireNonNull( newDatabase );
		Objects.requireNonNull( actionDecider );

		try
		{
			return new MoveGamesAction( games, oldDatabase, newDatabase, actionDecider ).execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#updateMachine(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public int updateMachine( String to, String from, String database, boolean backupDatabases ) throws GamePersistenceException
	{
		Objects.requireNonNull( to );

		try
		{
			return new UpdateMachineAction( to, from, database, backupDatabases ).execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister#getRelatedGamesWithLauncherLinks(java.util.List)
	 */
	public List<RelatedGame> getRelatedGamesWithLauncherLinks( List<RelatedGame> relatedGames ) throws GamePersistenceException
	{
		Objects.requireNonNull( relatedGames );

		try
		{
			return new GetRelatedGamesWithLauncherLinksAction( relatedGames ).execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (GamePersistenceException)lpe.getException();
		}
	}

}
