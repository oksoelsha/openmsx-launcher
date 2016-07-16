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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import java.util.Set;

/**
 * Interface for Database Manager UI Model and Presenter
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public interface DatabaseManagerPresenter
{
	/**
	 * Called when user requests Database Manager screen (e.g. by clicking on Database Manager menu item)
	 * 
	 */
	void onRequestDatabaseManagerScreen();

	/**
	 * Called when user requests to delete a database
	 * 
	 * @param database Name of database
	 * @throws LauncherException
	 */
	void onRequestDeleteDatabase( String database ) throws LauncherException;

	/**
	 * Called when user requests to rename a database
	 * 
	 * @param database Name of database
	 * @return New name of database
	 */
	String onRequestRenameDatabaseScreen( String database );

	/**
	 * Called when user requests to rename a database
	 * 
	 * @param oldDatabase Old name of database
	 * @param newDatabase New name of database
	 * @throws LauncherException
	 */
	void onRequestRenameDatabaseAction( String oldDatabase, String newDatabase ) throws LauncherException;

	/**
	 * Called when user requests to delete all backups
	 * 
	 * @throws LauncherException
	 */
	void onRequestDeleteAllBackups() throws LauncherException;

	/**
	 * Update database and backups section on view (when database or backups data change)
	 * 
	 * @param database Name of database
	 */
	void updateDatabaseAndBackupsView( String database );

	/**
	 * Update info section on view (when database or backups data change)
	 * 
	 */
	void updateDatabaseInfoView();

	/**
	 * Return total database info
	 * 
	 * @param databases Set of database names
	 * @return DatabaseInfo object
	 */
	DatabaseInfo getDatabaseInfo( Set<String> databases );

	/**
	 * Called when user requests the database backups screen
	 * 
	 * @param database Name of database
	 * @throws LauncherException
	 */
	void onRequestDatabaseBackupsScreen( String database );

	/**
	 * View restored database on the main screen
	 * 
	 * @param database Name of database
	 * @throws LauncherException
	 */
	void viewRestoredDatabase( String database ) throws LauncherException;
}
