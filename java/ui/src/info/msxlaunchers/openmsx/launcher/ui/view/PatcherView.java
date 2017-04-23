/*
 * Copyright 2017 Sam Elsharif
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
 * Interface for Patcher UI View
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
public interface PatcherView
{
	/**
	 * Displays screen to patch files
	 * 
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 */
	void displayScreen( Language language, boolean rightToLeft );

	/**
	 * Show the replacement confirmation dialogue when the target file already exists
	 * 
	 * @return True if user clicked Yes to replace, false otherwise
	 */
	boolean confirmTargetFileReplacement();
}
