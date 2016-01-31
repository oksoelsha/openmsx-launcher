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

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

import java.util.Set;

/**
 * Interface for Settings UI Model and Presenter
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface SettingsPresenter
{
	/**
	 * Called when user requests settings screen (e.g. by clicking on settings menu item)
	 * 
	 * @param oldSettings Currently persisted Settings object
	 * @param databases Set containing current list of databases
	 * @param currentLanguage Language enum currently used
	 * @param currentLanguageCode Language enum name currently used
	 * @param currentRightToLeft Orientation of the current language (e.g. true for Arabic)
	 */
	void onRequestSettingsScreen( Settings oldSettings, Set<String> databases, Language currentLanguage,
			String currentLanguageCode, boolean currentRightToLeft );

	/**
	 * Called when user decides to persist the new settings (e.g. by clicking on OK button in settings screen)
	 * 
	 * @param openMSXFullPath Full path to directory of openMSX binary
	 * @param screenshotsFullPath Full path to screenshots directory
	 * @param defaultDatabase Default database
	 * @param languageChoice String that represents language name in Language enum
	 * @throws LauncherException
	 */
	void onRequestSettingsAction( String openMSXFullPath,
			String screenshotsFullPath,
			String defaultDatabase,
			String languageChoice ) throws LauncherException;
}
