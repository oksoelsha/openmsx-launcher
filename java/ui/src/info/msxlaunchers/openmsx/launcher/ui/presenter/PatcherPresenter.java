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
	 * Called to validate user input and to present user with dialog to confirm overwriting an existing target file
	 * 
	 * @param fileToPatch Filename to patch
	 * @param patchFile Patch filename
	 * @param useTargetFile If true, use targetFile argument
	 * @param targetFile Target filename to save the patched file. Cannot be null if useTargetFile is true
	 * @param isIPSPatchMethod True if user selected IPS patch method
	 * @param skipChecksumValidation If true, skip all checksum validation against source file
	 * @param checksum Checksum value (SHA1, MD5 or CRC32)
	 * @return True is input is valid (and user confirmed overwriting existing target file if applicable), false otherwise
	 * @throws LauncherException
	 */
	boolean onValidate( String fileToPatch, String patchFile, boolean useTargetFile, String targetFile, boolean isIPSPatchMethod,
			boolean skipChecksumValidation, String checksum ) throws LauncherException;

	/**
	 * Called when user requests to patch a file with an IPS patch. This should be called after onValidate
	 *  
	 * @param fileToPatch Filename to patch
	 * @param patchFile Patch filename
	 * @param useTargetFile If true, use targetFile argument
	 * @param targetFile Target filename to save the patched file. Cannot be null if useTargetFile is true
	 * @param skipChecksumValidation If true, skip all checksum validation against source file
	 * @param checksum Checksum value (SHA1, MD5 or CRC32)
	 * @throws LauncherException
	 */
	void onRequestPatchFileActionForIPS( String fileToPatch, String patchFile, boolean useTargetFile, String targetFile,
			boolean skipChecksumValidation, String checksum ) throws LauncherException;

	/**
	 * Called when user requests to patch a file with a UPS patch
	 *  
	 * @param fileToPatch Filename to patch
	 * @param patchFile Patch filename
	 * @param useTargetFile If true, use targetFile argument
	 * @param targetFile Target filename to save the patched file. Cannot be null if useTargetFile is true
	 * @param skipChecksumValidation If true, skip all checksum validations
	 * @throws LauncherException
	 */
	void onRequestPatchFileActionForUPS( String fileToPatch, String patchFile, boolean useTargetFile, String targetFile,
			boolean skipChecksumValidation ) throws LauncherException;
}
