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

import java.util.Set;

/**
 * Interface for Import from blueMSX Launcher databases UI View
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public interface BlueMSXLauncherDatabaseImporterView
{
	/**
	 * Displays screen to import blueMSX Launcher databases
	 * 
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @param machines Set containing list of openMSX machines
	 */
	void displayScreen( Language language, boolean rightToLeft, Set<String> machines );

	/**
	 * Displays Action Decider screen to ask the user to decide how to proceed when blueMSXLauncher database name exists in openMSX Launcher
	 * 
	 * @param databaseName Name of game to show in the message
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @return A 0-based integer that represents the desired action, or -1 to mean that the decision operation was cancelled.
	 */
	int displayAndGetActionDecider( String databaseName, Language language, boolean rightToLeft );
}
