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

import java.nio.file.Path;
import java.util.Map;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
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
	 * Called when user clicks on icon to find related game in the launcher
	 * 
	 * @param databaseItem Selected related game database item
	 * @throws LauncherException
	 */
	void onRequestHighlightGameInLauncher( DatabaseItem databaseItem ) throws LauncherException;

	/**
	 * Returns path to the screenshot of the given game Generation MSX id
	 * 
	 * @param msxGenId Generation MSX id of a game
	 * @return Path of the screenshot corresponding to the given Generation MSX id
	 */
	Path getScreenshotPath( int msxGenId );

	/**
	 * Returns whether given related game has a valid (or existing) Generation MSX id
	 * 
	 * @param relatedGame Related game to get Generation MSX id from
	 * @return True if Generation MSX id is valid, false otherwise
	 */
	boolean isGenerationMSXIdValid( RelatedGame relatedGame );

	/**
	 * Returns full Generation MSX id URL for the given game
	 * 
	 * @param relatedGame Related game to get Generation MSX URL for
	 * @return Full Generation MSX URL
	 */
	String getGenerationMSXURL( RelatedGame relatedGame );

	/**
	 * Return full YouTune URL with search parameter for the given game name
	 * 
	 * @param gameName Game name to search for
	 * @return Full YouTube URL that includes search parameter
	 */
	String getYouTubeURL( String gameName );
}
