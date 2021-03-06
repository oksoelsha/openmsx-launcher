/*
 * Copyright 2020 Sam Elsharif
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
import info.msxlaunchers.openmsx.launcher.extractor.ExtractorData;

/**
 * Interface for LHA Extractor UI Model and Presenter
 * 
 * @since v1.14
 * @author Sam Elsharif
 *
 */
public interface LHAExtractorPresenter
{
	/**
	 * Called when user requests LHA Extractor screen
	 * 
	 * @param currentLanguage Current language
	 * @param currentRightToLeft Orientation of the current language (e.g. true for Arabic)
	 */
	void onRequestLHAExtractorScreen( Language currentLanguage, boolean currentRightToLeft );

	/**
	 * Called when user requests to uncompress an LHA/LZH file
	 *  
	 * @param fileToUncompress Filename to extract from
	 * @param targetDirectory Target directory to extract into. Can be null
	 * @param extractOnlyMSXImages If true, extract only MSX images
	 * @return data about the extraction operation 
	 * @throws LauncherException
	 */
	ExtractorData onRequestLHAExtractAction( String fileToUncompress, String targetDirectory, boolean extractOnlyMSXImages ) throws LauncherException;
}
