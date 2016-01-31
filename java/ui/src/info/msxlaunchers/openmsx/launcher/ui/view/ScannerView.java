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
 * Interface for Scanner UI View
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface ScannerView
{
	/**
	 * Displays screen to fill a database with games by scanning folders on disks
	 * 
	 * @param language Language
	 * @param databases Set containing current list of existing databases
	 * @param currentDatabase Name of current database
	 * @param machines Set containing list of openMSX machines
	 * @param rightToLeft Flag to determine screen orientation based on language
	 */
	void displayFillDatabase( Language language,
			Set<String> databases,
			String currentDatabase,
			Set<String> machines,
			boolean rightToLeft );
}
