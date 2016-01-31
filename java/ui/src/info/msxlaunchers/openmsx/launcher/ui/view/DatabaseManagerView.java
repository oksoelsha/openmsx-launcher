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
package info.msxlaunchers.openmsx.launcher.ui.view;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseAndBackups;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseInfo;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter;

import java.util.Set;

/**
 * Interface for Database Manager UI View
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public interface DatabaseManagerView
{
	/**
	 * Displays screen to manage databases
	 * 
	 * @param presenter Reference to DatabaseManagerPresenter
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @param databaseAndBackups Set containing objects of database and its backups total
	 * @param databases Set of database names
	 */
	void displayScreen( DatabaseManagerPresenter presenter, Language language, boolean rightToLeft, Set<DatabaseAndBackups> databaseAndBackups );

	/**
	 * Displays screen to prompt user to rename database
	 * 
	 * @param presenter Reference to DatabaseManagerPresenter
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @param databases Set containing databases
	 * @return New name of database
	 */
	String displayRenameDatabaseScreen( DatabaseManagerPresenter presenter, String database, Language language, boolean rightToLeft );

	/**
	 * Update database and backups section after a change
	 * 
	 * @param database Name of database
	 * @param gamesTotal Total number of games for the given database
	 * @param backupsTotal Total number of backups for the given database
	 */
	void updateDatabaseAndBackups( String database, int gamesTotel, int backupsNumber );

	/**
	 * Update database info section after a change
	 * 
	 * @param databaseInfo DatabaseInfo object
	 */
	void updateDatabaseInfo( DatabaseInfo databaseInfo );
}
