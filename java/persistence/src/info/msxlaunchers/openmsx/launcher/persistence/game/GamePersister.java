/*
 * Copyright 2013 Sam Elsharif
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
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;

import java.util.Map;
import java.util.Set;

/**
 * Interface to provide operation on databases and game persistence
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public interface GamePersister
{
	/**
	 * Creates a new database
	 * 
	 * @param database Name of database
	 * @throws GamePersistenceException
	 */
	void createDatabase( String database ) throws GamePersistenceException;
	
	/**
	 * Deletes an existing database
	 * 
	 * @param database Name of database
	 * @throws GamePersistenceException
	 */
	void deleteDatabase( String database ) throws GamePersistenceException;
	
	/**
	 * Renames an existing database
	 * 
	 * @param oldDatabase old name of database
	 * @param newDatabase New name of database
	 * @throws GamePersistenceException
	 */
	void renameDatabase( String oldDatabase, String newDatabase ) throws GamePersistenceException;
	
	/**
	 * Backs up an existing database by copying and appending a number to it. This operation should allow a finite number
	 * of backups
	 * 
	 * @param database Name of database
	 * @return Instance of DatabaseBackup that contains name and the timestamp
	 * @throws GamePersistenceException
	 */
	DatabaseBackup backupDatabase( String database ) throws GamePersistenceException;
	
	/**
	 * Restores an existing database backup
	 * 
	 * @param backup DatabaseBackup object
	 * @throws GamePersistenceException
	 */
	void restoreBackup( DatabaseBackup backup ) throws GamePersistenceException;
	
	/**
	 * Deletes an existing database backup
	 * 
	 * @param backup DatabaseBackup object
	 * @throws GamePersistenceException
	 */
	void deleteBackup( DatabaseBackup backup ) throws GamePersistenceException;
	
	/**
	 * Recreates an existing database. This operation will wipe clean an existing database and create a new one with
	 * the same name
	 * 
	 * @param database Name of database
	 * @throws GamePersistenceException
	 */
	void recreateDatabase( String database ) throws GamePersistenceException;
	
	/**
	 * Returns a Set containing all existing database names
	 * 
	 * @return Set containing database names. If no databases were found, this method will return an empty Set
	 */
	Set<String> getDatabases();

	/**
	 * Returns a Set containing backups for given database
	 * 
	 * @return Unmodifiable Set containing database backups for given database name. If no databases were found, return an empty Set
	 */
	Set<DatabaseBackup> getBackups( String database );

	/*
	 * Updates extra data of all games in all databases
	 * 
	 * @param extraDataMap Map of sha1 codes to ExtraData objects
	 * @return Number of updated profiles
	 * @throws GamePersistenceException
	 */
	int updateGameExtraDataInDatabases( Map<String,ExtraData> extraDataMap ) throws GamePersistenceException;

	/**
	 * Returns all games in a given database
	 * 
	 * @param database Name of database
	 * @return Unmodifiable Set containing Game objects found in the given database. If no games were found, then an empty Set is returned
	 * @throws GamePersistenceException
	 */
	Set<Game> getGames( String database ) throws GamePersistenceException;

	/**
	 * Saves a game in a given database
	 * 
	 * @param game Game object
	 * @param database Name of database
	 * @throws GamePersistenceException
	 */
	void saveGame( Game game, String database ) throws GamePersistenceException;

	/**
	 * Saves a Set of games in a given database
	 * 
	 * @param games Set containing Game objects
	 * @param database Name of database
	 * @throws GamePersistenceException
	 */
	void saveGames( Set<Game> games, String database ) throws GamePersistenceException;

	/**
	 * Updates a game by replacing the old one with a new one
	 * 
	 * @param oldGame Old Game object
	 * @param newGame New Game object
	 * @param database Name of database
	 * @throws GamePersistenceException
	 */
	void updateGame( Game oldGame, Game newGame, String database ) throws GamePersistenceException;

	/**
	 * Deletes a game from a given database
	 * 
	 * @param game Game object
	 * @param database Name of database
	 * @throws GamePersistenceException
	 */
	void deleteGame( Game game, String database ) throws GamePersistenceException;

	/**
	 * Deletes a Set of games from a given database
	 * 
	 * @param games Set containing games to delete
	 * @param database Name of database
	 * @throws GamePersistenceException
	 */
	void deleteGames( Set<Game> games, String database ) throws GamePersistenceException;

	/**
	 * Moves a Set of games from a database to another and returns a Set containing the games that were moved
	 * 
	 * @param games Set containing Game objects to move
	 * @param oldDatabase Name of old database
	 * @param newDatabase Name of new database
	 * @param actionDecider Reference to an ActionDecider implementation that prompts the caller for actions when target database contains the game being moved
	 * @return Set containing moved games. IF no games were moved, then an empty Set is returned
	 * @throws GamePersistenceException
	 */
	Set<Game> moveGames( Set<Game> games, String oldDatabase, String newDatabase, ActionDecider actionDecider ) throws GamePersistenceException;
}
