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
package info.msxlaunchers.openmsx.game.scan;

import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;

import java.io.IOException;

/**
 * Interface for scanning for games supported by openMSX 
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public interface Scanner
{
	/**
	 * Scans for games supported by openMSX and returns number of found games
	 * 
	 * @param paths Array of paths to scan
	 * @param traverseSubDirectories If true, traverse sub-directories
	 * @param database Name of existing database to either overwrite or append to
	 * @param newDatabase If true, given database is new
	 * @param append If true, append to existing database
	 * @param machine Machine name as it appears in openMSX machines folder
	 * @param searchROM If true, search for files with ROM extensions
	 * @param searchDisk If true, search for files with disk extensions
	 * @param searchTape If true, search for files with tape extensions
	 * @param searchLaserdisc If true, search for files with laserdisc extensions
	 * @param getNameFromOpenMSXDatabase If true, game name will be obtained from openMSX software database, otherwise use filename as game name
	 * @param backupDatabase If true, backup existing database
	 * @return Number of found games
	 * @throws GameWithNullNameException
	 * @throws DatabaseMaxBackupReachedException
	 * @throws DatabaseAlreadyExistsException
	 * @throws DatabaseNullNameException
	 * @throws DatabaseNotFoundException
	 * @throws IOException
	 */
	int scan( String[] paths,
			boolean traverseSubDirectories,
			String database,
			boolean newDatabase,
			boolean append,
			String machine,
			boolean searchROM,
			boolean searchDisk,
			boolean searchTape,
			boolean searchLaserdisc,
			boolean getNameFromOpenMSXDatabase,
			boolean backupDatabase )
		throws GamePersistenceException, IOException;

	/**
	 * Interrupts current scan process
	 */
	void interrupt();
}
