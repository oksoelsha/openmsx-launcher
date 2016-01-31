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

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

import java.io.File;
import java.util.Set;

/**
 * Interface for import blueMSX Launcher databases UI Model and Presenter
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public interface BlueMSXLauncherDatabasesImporterPresenter
{
	/**
	 * Called when user requests importer blueMSX Launcher databases screen
	 *
	 * @throws LauncherException
	 */
	void onRequestImportBlueMSXLauncherDatabasesScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException;

	/**
	 * Called when user decides to import the selected blueMSX Launcher databases
	 *  
	 *  @param path Path to blueMSX Launcher where the databases are
	 * @param databaseNames Names of blueMSX Launcher databases
	 * @param machine Selected Machine
	 * @return Number of imported databases
	 * @throws LauncherException
	 */
	int onRequestImportBlueMSXLauncherDatabasesAction( String path, String[] databasesName, String machine ) throws LauncherException;

	/**
	 * Called when user enters or selects blueMSX Launcher directory to get list of databases
	 *  
	 * @param databases blueMSX Launcher databases
	 * @return Unmodifiable set containing blueMSX Launcher database names
	 */
	Set<String> onGetDatabasesInDirectory( File directory );
}
