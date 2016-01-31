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

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for File Scanner UI Model and Presenter
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface ScannerPresenter
{
	/**
	 * Called when user requests fill database screen (e.g. by clicking on fill database menu item)
	 * 
	 * @throws LauncherException
	 */
	void onRequestFillDatabaseScreen( Set<String> databases, String currentDatabase, Language currentLanguage, boolean currentRightToLeft )
			throws LauncherException;

	/**
	 * Called when user requests to interrupt the running fill database process
	 */
	void onRequestInterruptFillDatabaseProcess();

	/**
	 * Called when user requests to fill a database with the given parameters
	 * 
	 * @param paths
	 * @param traverseSubDirectories
	 * @param database
	 * @param newDatabase
	 * @param append
	 * @param machine
	 * @param searchROM
	 * @param searchDisk
	 * @param searchTape
	 * @param searchLaserdisc
	 * @param getNameFromOpenMSXDatabase True to get name from openMSX database for profile name, otherwise use filename as profile name
	 * @param backupDatabase True to back up database
	 * @return Total number of found games
	 * @throws LauncherException
	 */
	int onRequestFillDatabaseAction( String[] paths,
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
			boolean backupDatabase ) throws LauncherException;

	/**
	 * @param database
	 * @throws LauncherException
	 */
	void onUpdateViewedDatabase( String database ) throws LauncherException;
}
