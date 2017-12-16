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

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for Profile Editing UI Model and Presenter
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface ProfileEditingPresenter
{
	/**
	 * Called when user requests the add game screen (e.g. by clicking on the Add Game button)
	 * 
	 * @param currentLanguage Current language
	 * @param currentRightToLeft Orientation of the current language (e.g. true for Arabic)
	 * @throws LauncherException
	 */
	void onRequestAddGameScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException;

	/**
	 * Called when user requests the edit game screen (e.g. by clicking on the Edit Game button)
	 * 
	 * @param currentLanguage Current language
	 * @param currentRightToLeft Orientation of the current language
	 * @throws LauncherException
	 */
	void onRequestEditGameScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException;

	/**
	 * Called when user requests to save the game from within the Add screen
	 * 
	 * @param name Name of game
	 * @param info Full path to info file or URL for a game
	 * @param machine Machine name
	 * @param romA Full path to ROM A file
	 * @param romB Full path to ROM B file
	 * @param extensionRom Name of openMSX extension
	 * @param diskA Full path to Disk A file
	 * @param diskB Full path to Disk B file
	 * @param tape Full path to Tape file
	 * @param harddisk Full path to Hard Disk file
	 * @param laserdisc Full path to Laserdisc file
	 * @param script Full path to Script file
	 * @param fddModeCode FDD Mode Enum Code
	 * @param scriptOverride Flag to override other non-Script arguments
	 * @throws LauncherException
	 */
	void onRequestAddGameSaveAction( String name,
			String info,
			String machine,
			String romA,
			String romB,
			String extensionRom,
			String diskA,
			String diskB,
			String tape,
			String harddisk,
			String laserdisc,
			String script,
			int fddModeCode,
			boolean scriptOverride ) throws LauncherException;

	/**
	 * Called when user decides to launch the game from within the Add or Edit screens
	 * 
	 * @param machine Machine name
	 * @param romA Full path to ROM A file
	 * @param romB Full path to ROM B file
	 * @param extensionRom Name of openMSX extension
	 * @param diskA Full path to Disk A file
	 * @param diskB Full path to Disk B file
	 * @param tape Full path to Tape file
	 * @param harddisk Full path to Hard Disk file
	 * @param laserdisc Full path to Laserdisc file
	 * @param script Full path to Script file
	 * @param fddModeCode FDD Mode Enum Code
	 * @param scriptOverride Flag to override other non-Script arguments
	 * @throws LauncherException
	 */
	void onRequestLaunchAction( String machine,
			String romA,
			String romB,
			String extensionRom,
			String diskA,
			String diskB,
			String tape,
			String harddisk,
			String laserdisc,
			String script,
			int fddModeCode,
			boolean scriptOverride ) throws LauncherException;

	/**
	 * Called when user requests to save the game from within the Edit screen
	 * 
	 * @param oldName Current name of game to be edited
	 * @param newName New name for the game to be edited - can be the same as the old name
	 * @param info Full path to info file or URL for a game
	 * @param machine Machine name
	 * @param romA Full path to ROM A file
	 * @param romB Full path to ROM B file
	 * @param extensionRom Name of openMSX extension
	 * @param diskA Full path to Disk A file
	 * @param diskB Full path to Disk B file
	 * @param tape Full path to Tape file
	 * @param harddisk Full path to Hard Disk file
	 * @param laserdisc Full path to Laserdisc file
	 * @param script Full path to Script file
	 * @param fddModeCode FDD Mode Enum Code
	 * @param scriptOverride Flag to override other non-Script arguments
	 * @throws LauncherException
	 */
	void onRequestEditGameSaveAction( String oldName,
			String newName,
			String info,
			String machine,
			String romA,
			String romB,
			String extensionRom,
			String diskA,
			String diskB,
			String tape,
			String harddisk,
			String laserdisc,
			String script,
			int fddModeCode,
			boolean scriptOverride ) throws LauncherException;
}
