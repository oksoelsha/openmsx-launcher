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

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for Game Properties UI Model and Presenter
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface GamePropertiesPresenter
{
	/**
	 * Called when user requests game properties screen
	 * 
	 * @param game Game object
	 * @param currentLanguage Current language
	 * @param currentRightToLeft Orientation of the current language
	 */
	void onRequestGamePropertiesScreen( Game game, Language currentLanguage, boolean currentRightToLeft );

	/**
	 * Returns whether given game has a valid (or existing) MSX Generation id
	 * 
	 * @param game Game to get MSX Generation id from
	 * @return True if MSX Generation id is valid, false otherwise
	 */
	boolean isMSXGenerationIdValid( Game game );

	/**
	 * Returns full MSX Generation id URL for the given game
	 * 
	 * @param game Game to get MSX Generation URL for
	 * @return Full MSX Generation URL
	 */
	String getMSXGenerationURL( Game game );
}
