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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

import java.sql.Timestamp;

/**
 * Interface for Database Backups UI Model and Presenter
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
public interface DatabaseBackupsPresenter
{
	/**
	 * Called when user requests a given Database Backups screen
	 * 
	 * @param currentLanguage Language enum currently used
	 * @param currentRightToLeft Orientation of the current language (e.g. true for Arabic)
	 */
	void onRequestDatabaseBackupsScreen( Language currentLanguage, boolean currentRightToLeft );

	/**
	 * Called when user requests to delete a backup
	 * 
	 * @param timestamp Timestamp of the backup
	 * @throws LauncherException
	 */
	void onRequestDeleteBackup( Timestamp timestamp ) throws LauncherException;

	/**
	 * Called when user requests to restore a backup
	 * 
	 * @param timestamp Timestamp of the backup
	 * @throws LauncherException
	 */
	void onRequestRestoreBackup( Timestamp timestamp ) throws LauncherException;

	/**
	 * Update databases and their backups section on view in case of delete or restore (when database or backups data change)
	 * 
	 * @param timestamp Timestamp of the backup (to get the database from model)
	 */
	void updateDatabaseAndBackupsView( Timestamp timestamp );

	/**
	 * Update databases and their backups section on view in case of addition of new backup (when database or backups data change)
	 * 
	 * @param databaseBackup DatabaseBackup object
	 */
	void updateDatabaseAndBackupsView( DatabaseBackup databaseBackup );

	/**
	 * Update info section on view (when database or backups data change)
	 * 
	 */
	void updateDatabaseInfoView();

	/**
	 * View restored database
	 * 
	 * @param timestamp Timestamp of the backup (to get the database from model)
	 * @throws LauncherException
	 */
	void viewRestoredDatabase( Timestamp timestamp ) throws LauncherException;

	/**
	 * Called when user requests to backup current database
	 * 
	 * @return Instance of DatabaseBackup
	 * @throws LauncherException
	 */
	DatabaseBackup onRequestBackupDatabase() throws LauncherException;
}
