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

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for Adding Dragged And Dropped Games UI Model and Presenter
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
public interface DraggedAndDroppedGamesPresenter
{
	/**
	 * Called when user requests the add dragged and dropped games screen
	 * 
	 * @param currentLanguage Current language
	 * @param currentRightToLeft Orientation of the current language (e.g. true for Arabic)
	 * @throws LauncherException
	 */
	void onRequestAddDraggedAndDroppedGamesScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException;

	/**
	 * Called when user requests the add dragged and dropped games to the current database
	 * 
	 * @param getNameFromOpenMSXDatabase True to get name from openMSX database for profile name, otherwise use filename as profile name
	 * @param backupDatabase True to back up database
	 * @Param machine Selected machine
	 * @return Total number of found games
	 * @throws LauncherException
	 */
	int onRequestAddDraggedAndDroppedGamesAddAction( boolean getNameFromOpenMSXDatabase, boolean backupDatabase, String machine ) throws LauncherException;

	/**
	 * Called when user requests to interrupt the running fill database process
	 */
	void onRequestInterruptFillDatabaseProcess();

	/**
	 * @param database
	 * @throws LauncherException
	 */
	void onUpdateViewedDatabase( String database ) throws LauncherException;
}
