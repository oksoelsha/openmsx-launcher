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

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

import java.util.Set;

/**
 * Interface for Settings UI View
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface SettingsView
{
	/**
	 * Displays settings screen
	 * 
	 * @param settings
	 * @param databases
	 * @param language Language
	 * @param languageCode
	 * @param rightToLeft Flag to determine screen orientation based on language
	 */
	void displaySettings( Settings settings,
			Set<String> databases,
			Language language,
			String languageCode,
			boolean rightToLeft );
}
