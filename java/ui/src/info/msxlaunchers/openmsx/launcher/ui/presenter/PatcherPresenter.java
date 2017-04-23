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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for Patcher UI Model and Presenter
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
public interface PatcherPresenter
{
	/**
	 * Called when user requests IPS Patcher screen
	 * 
	 * @param currentLanguage Current language
	 * @param currentRightToLeft Orientation of the current language (e.g. true for Arabic)
	 */
	void onRequestIPSPatcherScreen( Language currentLanguage, boolean currentRightToLeft );

	/**
	 * Called when user decides to patch a file
	 *  
	 * @param fileToPatch Filename to patch
	 * @param patchFile Patch filename
	 * @param useTargetFile If true, use targetFile argument
	 * @param targetFile Target filename to save the patched file. Cannot be null if useTargetFile is true
	 * @param verifyChecksum If true, verify the given checksum against the source file
	 * @param checksum Checksum value (SHA1 or MD5)
	 * @return True if patching was successful, false if the user interrupted it (because they didn't want to replace an existing target file) 
	 * @throws LauncherException
	 */
	boolean onRequestPatchFileAction( String fileToPatch, String patchFile, boolean useTargetFile, String targetFile,
			boolean verifyChecksum, String checksum ) throws LauncherException;
}
