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

/**
 * Interface for Update Checker UI Model and Presenter
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
public interface UpdateCheckerPresenter
{
	/**
	 * Called when user requests 'Check for Updates' screen (e.g. by clicking on Check for Updates menu item)
	 * 
	 * @param currentLanguage Language enum currently used
	 * @param currentRightToLeft Orientation of the current language (e.g. true for Arabic)
	 */
	void onRequestCheckForUpdatesScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException;

	/**
	 * Called to determine what message to present to the user about openMSX Launcher version upgrade
	 * 
	 * @return True if version on server is higher than current version
	 */
	boolean isNewOpenMSXLauncherVersionAvailable();

	/**
	 * Called to determine if a new version of the launcher was downloaded but not installed
	 * 
	 * @return True if the new version was downloaded but not installed
	 */
	boolean isNewOpenMSXLauncherVersionDownloaded();

	/**
	 * Called to determine what message to present to the user about Extra Data version upgrade
	 * 
	 * @return True if version on server is higher than current version
	 */
	boolean isNewExtraDataVersionAvailable();

	/**
	 * Called to determine what message to present to the user about Screenshots version upgrade
	 * 
	 * @return True if version on server is higher than current version
	 */
	boolean isNewScreenshotsVersionAvailable();

	/**
	 * Called to determine if the Screenshots directory is set in the launcher settings
	 * 
	 * @return True if the Screenshots directory is set in the launcher settings
	 */
	boolean isScreenshotsSetInSettings();

	/**
	 * Called when user requests to update extra data
	 * 
	 * @throws LauncherException
	 */
	void onRequestUpdateExtraData() throws LauncherException;

	/**
	 * Called when user requests to update openMSX Launcher
	 * 
	 * @throws LauncherException
	 */
	void onRequestUpdateOpenMSXLauncher() throws LauncherException;

	/**
	 * Called when user requests to open the download page (e.g. to get the latest screenshots)
	 * 
	 * @throws LauncherException
	 */
	void onRequestOpenDownloadPage() throws LauncherException;
}
