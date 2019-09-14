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

import info.msxlaunchers.openmsx.launcher.data.filter.Filter;
import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;

import java.io.File;
import java.util.Set;

/**
 * Interface for UI Model and Presenter
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface MainPresenter
{
	/**
	 * Starts the UI application
	 */
	void start();

	/**
	 * Called when user requests settings screen (e.g. by clicking on settings menu item)
	 */
	void onRequestSettingsScreen();

	/**
	 * Called when new settings are successfully persisted. Model will be updated accordingly
	 * 
	 * @param Settings new persisted settings
	 * @throws LauncherException
	 */
	void onAcceptSettingsAction( Settings settings ) throws LauncherException;

	/**
	 * Called when user selects a database to view
	 * 
	 * @param database Selected database name
	 * @throws LauncherException
	 */
	void onSelectDatabase( String database ) throws LauncherException;

	/**
	 * Called when user selects one or more games
	 * 
	 * @param gameNames Set containing selected game names
	 */
	void onSelectGames( Set<String> gameNames );

	/**
	 * Resets all buttons to initial state
	 */
	void resetAll();

	/**
	 * Called when user launches openMSX with selected game
	 * 
	 * @param gameName Name of game to launch
	 * @throws LauncherException
	 */
	void onLaunchGame( String gameName ) throws LauncherException;

	/**
	 * Called when user requests the add game screen (e.g. by clicking on the Add Game button)
	 */
	void onRequestAddGameScreen() throws LauncherException;

	/**
	 * Called when user requests the edit game screen (e.g. by clicking on the Edit Game button)
	 * 
	 * @param gameName Name of game to edit
	 * @throws LauncherException
	 */
	void onRequestEditGameScreen( String gameName ) throws LauncherException;

	/**
	 * Called when game is successfully saved from within the Add screen. Model will be updated accordingly
	 * 
	 * game Game object
	 * @throws LauncherException
	 */
	void onAcceptAddGameSaveAction( Game game ) throws LauncherException;

	/**
	 * Called when game is successfully saved from within the Edit screen
	 * 
	 * oldName Name of game before it was saved
	 * game Game object (that may contain a new name or the old name)
	 * @throws LauncherException
	 */
	void onAcceptEditGameSaveAction( String oldName, Game game ) throws LauncherException;

	/**
	 * Called when user requests to remove selected games
	 * 
	 * @param gameNames Names of games
	 * @throws LauncherException
	 */
	void onRequestRemoveGamesAction( Set<String> gameNames ) throws LauncherException;

	/**
	 * Called when user requests fill database screen (e.g. by clicking on fill database menu item)
	 * 
	 * @throws LauncherException
	 */
	void onRequestFillDatabaseScreen() throws LauncherException;

	/**
	 * Called to view modified database (e.g. after running fill database)
	 * 
	 * @param database Name of the database to update. If null, then use the currently selected database in the model
	 * @throws LauncherException
	 */
	void onViewUpdatedDatabase( String database ) throws LauncherException;

	/**
	 * Called when user requests view the game's info file, or web URL in a browser
	 * 
	 * @param gameName Name of game
	 * @throws LauncherException
	 */
	void onRequestGameInfo( String gameName ) throws LauncherException;

	/**
	 * Called when user requests the About screen
	 */
	void onRequestAboutScreen();

	/**
	 * Called when user requests the move games screen
	 * 
	 * @param gameNames Names of games to move
	 * @param oldDatabase Name of the database to move the selected games from
	 * @return Set containing games that were moved successfully
	 */
	Set<String> onRequestMoveGamesScreen( Set<String> gameNames, String oldDatabase );

	/**
	 * Called when user requests the move games action (e.g. by clicking OK in the move games screen after selecting target database
	 * 
	 * @param gameNames Names of games to move
	 * @param oldDatabase Name of the database to move the selected games from
	 * @param newDatabase Name of the target database to move the selected games to
	 * @return Set containing games that were moved successfully
	 * @throws LauncherException
	 */
	Set<String> onRequestMoveGamesAction( Set<String> gameNames, String oldDatabase, String newDatabase ) throws LauncherException;

	/**
	 * Called when user requests the create empty database screen
	 */
	void onRequestCreateEmptyDatabaseScreen();

	/**
	 * Called when user requests to create an empty database with the given name
	 * 
	 * @param newDatabase Name of the new empty database
	 * @throws LauncherException
	 */
	void onRequestCreateEmptyDatabaseAction( String newDatabase ) throws LauncherException;

	/**
	 * Called when user requests the help file
	 * 
	 * @throws LauncherException
	 */
	void onRequestHelpFile() throws LauncherException;

	/**
	 * Called when user requests to locate the main file of the selected game
	 * 
	 * @param gameName Name of game to locate its main file
	 * @throws LauncherException
	 */
	void onRequestLocateFile( String gameName ) throws LauncherException;

	/**
	 * Called when user requests to add the selected game to the list of favorites
	 * 
	 * @param gameName Name of game to locate its main file
	 * @param database Database name
	 * @throws LauncherException
	 */
	void onRequestAddFavorite( String gameName, String database ) throws LauncherException;

	/**
	 * Called when user requests the current list of favorites
	 */
	void onRequestListOfFavorites();

	/**
	 * Called when user selects a database item (e.g. favorite or search item)
	 * 
	 * @param databaseItem Selected favorite or search item
	 * @throws LauncherException
	 */
	void onSelectDatabaseItem( DatabaseItem databaseItem ) throws LauncherException;

	/**
	 * Called when user requests to delete selected a favorite
	 * 
	 * @param favorite Favorite to delete
	 * @throws LauncherException
	 */
	void onRequestDeleteFavoriteAction( DatabaseItem favorite ) throws LauncherException;

	/**
	 * Called when user requests the current list of saved filters
	 */
	void onRequestListOfSavedFilters();

	/**
	 * Called when user requests the current list of databases
	 */
	void onRequestDatabasesList();

	/**
	 * Called when user selects a saved filter
	 * 
	 * @param filterName Selected filter name
	 * @throws LauncherException
	 */
	void onSelectFilter( String filterName ) throws LauncherException;

	/**
	 * Called when user selects a quick filter
	 * 
	 * @param filterMoniker Selected filter
	 * @throws LauncherException
	 */
	void onSelectQuickFilter( Filter filter ) throws LauncherException;

	/**
	 * Called when user requests to delete selected a filter by name
	 * 
	 * @param filterName Filter name to delete
	 * @throws LauncherException
	 */
	void onRequestDeleteFilterAction( String filterName ) throws LauncherException;

	/**
	 * Called when user requests the add new filter screen (e.g. by clicking on the New filter menu item)
	 */
	void onRequestAddFilterScreen() throws LauncherException;

	/**
	 * Called when user requests to edit a filter given its name (e.g. by clicking on the edit filter button)
	 * 
	 * @param filterName Filter name to edit
	 * @throws LauncherException
	 */
	void onRequestEditFilterScreen( String filterName ) throws LauncherException;

	/**
	 * Called when user requests to apply a filter as a set of filter items
	 * 
	 * @param filter Set containing filter items
	 * @throws LauncherException
	 */
	void onApplyFilter( Set<Filter> filter ) throws LauncherException;

	/**
	 * Called when when an event requires to show a filter name on the display
	 * 
	 * @param filterName Filter name to display
	 */
	void onRequestUpdateFilterName( String filterName );

	/**
	 * Called when an events requires to reset the filter name on the display
	 */
	void onRequestSetFilterNameUntitled();

	/**
	 * Called when user requests to show properties of selected game
	 * 
	 * @param gameName Name of game to show properties for
	 */
	void onRequestShowProperties( String gameName );

	/**
	 * Called when user requests to update all databases with extra data
	 * 
	 * @return Number of updated profiles
	 * @throws LauncherException
	 */
	int onRequestUpdateAllDatabases() throws LauncherException;

	/**
	 * Called when user requests the import from blueMSX Launcher databases screen
	 * 
	 * @throws LauncherException
	 */
	void onRequestImportBlueMSXLauncherDatabasesScreen() throws LauncherException;

	/**
	 * Called when the import blueMSX Launcher databases process finished
	 * 
	 * @param importedDatabases blueMSX Launcher databases that were imported successfully. Cannot be null
	 * @throws LauncherException
	 */
	void onAcceptImportBlueMSXLauncherDatabasesAction( Set<String> importedDatabases ) throws LauncherException;

	/**
	 * Called when user requests the database manager screen
	 * 
	 * @throws LauncherException
	 */
	void onRequestDatabaseManagerScreen() throws LauncherException;

	/**
	 * Called when user is done  deleting a database by name
	 * 
	 * @param database Database name
	 */
	void onRequestDeleteDatabaseAction( String database );

	/**
	 * Called when user is done renaming a database
	 * 
	 * @param oldDatabase Old name of database
	 * @param newDatabase New name of database
	 */
	void onRequestRenameDatabaseAction( String oldDatabase, String newDatabase );

	/**
	 * Called when user requests to check for updates on the msxlaunchers.info server
	 * 
	 * @throws LauncherException
	 */
	void onRequestUpdatesChecker() throws LauncherException;

	/**
	 * Called when user requests the activity viewer screen
	 */
	void onRequestActivityViewerScreen();

	/**
	 * Called when user requests the patcher screen
	 */
	void onRequestPatcherScreen();

	/**
	 * Called when user requests the machine update screen
	 */
	void onRequestMachineUpdateScreen() throws LauncherException;

	/**
	 * Called when application closes down
	 */
	void onRequestExit();

	/**
	 * Called when user drags and drops game files on the main window
	 */
	void onRequestAddDraggedAndDroppedGamesScreen( File[] files ) throws LauncherException;

	/**
	 * Called when user requests game matches for the string entered in the search field
	 * 
	 * @param searchString String to search partially for
	 * @return Set of ordered search matches
	 */
	Set<DatabaseItem> onRequestSearchMatches( String searchString );

	/**
	 * Called when update extra data operation is finished successfully
	 * 
	 * @throws LauncherException
	 */
	void onUpdateExtraData() throws LauncherException;

	/**
	 * Called when user requests to view new feed
	 */
	void onRequestNewsList();
}
