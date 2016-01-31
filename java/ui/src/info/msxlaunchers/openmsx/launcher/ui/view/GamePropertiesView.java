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

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for Game Properties UI View
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface GamePropertiesView
{
	/**
	 * Displays screen to add a new filter
	 * 
	 * @param game Game object
	 * @param repositoryGame RepositoryGame object. Null if information is unavailable
	 * @param knownDumps Number of known dumps of game ROM, including the ROM itself
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 */
	void displayGamePropertiesScreen( Game game, RepositoryGame repositoryGame, int knownDumps, Language language, boolean rightToLeft );
}
