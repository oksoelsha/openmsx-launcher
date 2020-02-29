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

import java.util.Map;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for Related Games UI Model and Presenter
 * 
 * @since v1.13
 * @author Sam Elsharif
 *
 */
public interface RelatedGamesPresenter
{
	/**
	 * Called when user requests 'Related Games' screen
	 * 
	 * @param Game game
	 * @param repositoryInfoMap Repository Info Map
	 * @param currentLanguage Language enum currently used
	 * @param currentRightToLeft Orientation of the current language
	 * @throws LauncherException
	 */
	void onRequestRelatedGamesScreen( Game game, Map<String,RepositoryGame> repositoryInfoMap, Language currentLanguage, boolean currentRightToLeft )
			throws LauncherException;

	/**
	 * Returns whether given related game has a valid (or existing) MSX Generation id
	 * 
	 * @param relatedGame Related game to get MSX Generation id from
	 * @return True if MSX Generation id is valid, false otherwise
	 */
	boolean isMSXGenerationIdValid( RelatedGame relatedGame );

	/**
	 * Returns full MSX Generation id URL for the given game
	 * 
	 * @param relatedGame Related game to get MSX Generation URL for
	 * @return Full MSX Generation URL
	 */
	String getMSXGenerationURL( RelatedGame relatedGame );

	/**
	 * Return full YouTune URL with search parameter for the given game name
	 * 
	 * @param gameName Game name to search for
	 * @return Full YouTube URL that includes search parameter
	 */
	String getYouTubeURL( String gameName );
}
