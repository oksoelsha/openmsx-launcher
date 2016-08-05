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
import info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter;

import java.util.Set;

/**
 * Interface for Profile Editing UI View
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface ProfileEditingView
{
	/**
	 * Displays screen to add a new game
	 * 
	 * @param presenter ProfileEditingPresenter implementation
	 * @param language Language
	 * @param machines Set containing list of openMSX machines
	 * @param extensions Set containing list of openMSX extensions
	 * @param rightToLeft Flag to determine screen orientation based on language
	 */
	void displayAddGameScreen( ProfileEditingPresenter presenter, Language language, Set<String> machines, Set<String> extensions, boolean rightToLeft );

	/**
	 * Displays screen to edit an existing game
	 * 
	 * @param presenter ProfileEditingPresenter implementation
	 * @param language Language
	 * @param machines Set containing list of openMSX machines
	 * @param extensions Set containing list of openMSX extensions
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @param name Name of game
	 * @param info Info file or URL for a game
	 * @param machine Machine name
	 * @param romA Full path to ROM A file
	 * @param romB Full path to ROM B file
	 * @param extension Name of openMSX extension
	 * @param diskA Full path to Disk A file
	 * @param diskB Full path to Disk B file
	 * @param tape Full path to Tape file
	 * @param harddisk Full path to Hard Disk file
	 * @param laserdisc Full path to Laserdisc file
	 * @param script Full path to Script file
	 * @param fddModeCode FDD Mode Enum code
	 */
	void displayEditGameScreen( ProfileEditingPresenter presenter, Language language, Set<String> machines, Set<String> extensions, boolean rightToLeft, 
			String name, String info, String machine, String romA, String romB, String extension,
			String diskA, String diskB, String tape, String harddisk, String laserdisc, String script, int fddModeCode );
}
