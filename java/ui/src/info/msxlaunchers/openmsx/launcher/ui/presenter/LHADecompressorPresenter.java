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

/**
 * Interface for Patcher UI Model and Presenter
 * 
 * @since v1.14
 * @author Sam Elsharif
 *
 */
public interface LHADecompressorPresenter
{
	/**
	 * Called when user requests LHA Decompressor screen
	 * 
	 * @param currentLanguage Current language
	 * @param currentRightToLeft Orientation of the current language (e.g. true for Arabic)
	 */
	void onRequestLHADecompressorScreen( Language currentLanguage, boolean currentRightToLeft );

	/**
	 * Called when user requests to patch a file with an IPS patch. This should be called after onValidate
	 *  
	 * @param fileToDecompress Filename to decompress
	 * @param targetDirectory Target directory to decompress into. Can be null
	 * @param onlyMSXFiles If true, decompress only MSX files
	 * @throws LauncherException
	 */
	void onRequestLHADecompressAction( String fileToDecompress, String targetDirectory, boolean onlyMSXFiles ) throws LauncherException;
}
