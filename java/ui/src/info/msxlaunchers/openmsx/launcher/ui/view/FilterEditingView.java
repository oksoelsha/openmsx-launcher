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

/**
 * Interface for Filter Editing UI View
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface FilterEditingView
{
	/**
	 * Displays screen to add a new filter
	 * 
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 */
	void displayAddFilterScreen( Language language, boolean rightToLeft );

	/**
	 * Displays screen to edit an existing filter
	 * 
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @param filterName Filter name
	 * @param filter Filter represented as an array of filter item monikers
	 */
	void displayEditFilterScreen( Language language, boolean rightToLeft, String filterName, String[] filter );
}
