/*
 * Copyright 2016 Sam Elsharif
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

import java.util.List;
import java.util.Map;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for Activity Viewer UI View
 * 
 * @since v1.8
 * @author Sam Elsharif
 *
 */
public interface ActivityViewerView
{
	/**
	 * Displays Activity Viewer screen
	 * 
	 * @param logData Activity data obtained from the logs
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 */
	void displayActivityViewerScreen( Map<String,List<String[]>> logData, Language language, boolean rightToLeft );
}
