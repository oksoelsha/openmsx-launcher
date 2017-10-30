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

import info.msxlaunchers.openmsx.launcher.data.feed.FeedMessage;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.GameLabel;

import java.util.List;
import java.util.Set;

/**
 * Interface for UI View
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface MainView
{
	/**
	 * Displays main screen of UI
	 * 
	 * @param language Language
	 * @param games Set containing GameLabel objects
	 * @param databases Set containing database names
	 * @param defaultDatabase Name of default database to display - could be null
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @param showUpdateAllDatabases Flag to show the update all databases menu item
	 * @param enableFeedAccess Flag to enable the feed button
	 */
	void displayMain( Language language,
			Set<GameLabel> games,
			Set<String> databases,
			String defaultDatabase,
			boolean rightToLeft,
			boolean showUpdateAllDatabases,
			boolean enableFeedAccess );

	/**
	 * Refreshes language display in the application based on the given language
	 * 
	 * @param language Language
	 */
	void refreshLanguage( Language language );

	/**
	 * Flips screen orientation to be left to right (for languages such as English)
	 */
	void flipOrientationLeftToRight();

	/**
	 * Flips screen orientation to be right to left (for languages such as Arabic)
	 */
	void flipOrientationRightToLeft();

	/**
	 * Fills the game list with given games
	 * 
	 * @param currentDatabase Current database name
	 * @param games Set containing GameLabel objects
	 * @param selectedGame
	 */
	void fillGameList( String currentDatabase, Set<GameLabel> games, String selectedGame );

	/**
	 * Updates the total game count on the main screen
	 * 
	 * @param total Total game count of the current database
	 */
	void updateGameCount( int total );

	/**
	 * Removes a database from the list of database names in the screen
	 * 
	 * @param database Database name
	 */
	void removeDatabase( String database );

	/**
	 * Renames a database from the list of database names in the screen to a new name
	 * 
	 * @param oldDatabase Old database name
	 * @param newDatabase New database name
	 */
	void renameDatabase( String oldDatabase, String newDatabase );

	/**
	 * Displays About screen
	 * 
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @param extraDataVersion Version of the extra-data.dat file - can be null or empty
	 * @param screenshotsVersion Version of the screenshots folder (from the version.txt file) - can be null or empty
	 */
	void displayAbout( Language language, boolean rightToLeft, String extraDataVersion, String screenshotsVersion );

	/**
	 * Shows the given two image files
	 * 
	 * @param screenshot1 Full path to screenshot1 image file - could be null
	 * @param screenshot2 Full path to screenshot2 image file - could be null
	 */
	void showGameScreenshots( String screenshot1, String screenshot2 );

	/**
	 * Enables or disables the four buttons: Launch, Remove, Edit and Info based on the given flags
	 * 
	 * @param launchFlag True if to enable, false to disable
	 * @param removeFlag True if to enable, false to disable
	 * @param addFlag True if to enable, false to disable
	 * @param editFlag True if to enable, false to disable
	 * @param infoFlag True if to enable, false to disable
	 */
	void enableButtons( boolean launchFlag, boolean removeFlag, boolean addFlag, boolean editFlag, boolean infoFlag );

	/**
	 * Enables or disables the sound indicators based on the data of the selected game
	 *
	 * @param psgFlag PSG flag
	 * @param sccFlag SCC flag
	 * @param scciFlag SCC+ flag
	 * @param pcmFlag PCM flag
	 * @param msxMusicFlag MSX-Music flag
	 * @param msxAudioFlag MSX-Audio flag
	 * @param moonsoundFlag Moonsound flag
	 * @param midiFlag MIDI flag
	 */
	void enableSoundIndicators( boolean psgFlag, boolean sccFlag, boolean scciFlag, boolean pcmFlag,
			boolean msxMusicFlag, boolean msxAudioFlag, boolean moonsoundFlag, boolean midiFlag );

	/**
	 * Enables or disables the generation indicators based on the data of the selected game
	 *
	 * @param msxFlag MSX flag
	 * @param msx2Flag MSX2 flag
	 * @param msx2pFlag MSX2+ flag
	 * @param turboRFlag Turbo-R flag
	 */
	void enableGenerationIndicators( boolean msxFlag, boolean msx2Flag, boolean msx2pFlag, boolean turboRFlag );

	/**
	 * Displays screen to ask user for the target database and return a set of the moved game names
	 * 
	 * @param language Language
	 * @param gameNames Set containing game names to move
	 * @param oldDatabase Name of current database to move from
	 * @param targetDatabase  Name of database to move to
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @return Set of moved game names - null if operation was cancelled
	 */
	Set<String> displayAndGetMoveGames( Language language,
			Set<String> gameNames,
			String oldDatabase,
			Set<String> targetDatabase,
			boolean rightToLeft );

	/**
	 * Displays Action Decider screen to ask the user to decide how to proceed when game name exists in target database
	 * 
	 * @param gameName Name of game to show in the message
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @return A 0-based integer that represents the desired action, or -1 to mean that the decision operation was cancelled.
	 */
	int displayAndGetActionDecider( String gameName, Language language, boolean rightToLeft );

	/**
	 * Displays screen to ask the user for the name of the empty database to create
	 * 
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 */
	void displayCreateEmptyDatabase( Language language, boolean rightToLeft );

	/**
	 * Shows the favorites menu
	 * 
	 * @param favorites Set of favorites represented as moniker strings
	 */
	void showFavoritesMenu( Set<String> favoritesAsStrings );

	/**
	 * Shows the databases list
	 * 
	 * @param databases Set of database names
	 */
	void showDatabasesList( Set<String> databases );

	/**
	 * Highlights the given game by name
	 * 
	 * @param gameName Name of game
	 */
	void highlightGame( String gameName );

	/**
	 * Shows the filters menu
	 * 
	 * @param filterNames Filter set name
	 * @param isFilterSelected Flag that indicates that a saved filter is currently selected
	 * @param isEditCurrentUntitledFilter Flag to show an option to edit current unsaved filters
	 */
	void showFiltersMenu( Set<String> filterNames, boolean isFilterSelected, boolean isEditCurrentUntitledFilter );

	/**
	 * Updates the filter name on the main screen with the given name.
	 * 
	 * @param filterName Filter name. If null, then reset to show nothing
	 */
	void updateFilterNameLabel( String filterName );

	/**
	 * Updates the filter name as 'Untitled' on the main screen
	 */
	void setFilterNameLabelUntitled();

	/**
	 * Enables or disables access to feed menu (by enabling or disabling button for example)
	 * 
	 * @param flag If true enable access, otherwise disable
	 */
	void enableFeedAccess( boolean flag );

	/**
	 * Indicates that there are new messages in the news feed
	 * 
	 * @param flag If true indicate new news (like change the new icon), otherwise set to normal state
	 */
	void indicateNewFeedMessages( boolean flag );

	/**
	 * Shows the feed news list
	 */
	void showFeedMessagesList( List<FeedMessage> feedMessages );

	/**
	 * Shows message that launcher is still processing feed
	 */
	void showFeedProcessingMessage();

}
