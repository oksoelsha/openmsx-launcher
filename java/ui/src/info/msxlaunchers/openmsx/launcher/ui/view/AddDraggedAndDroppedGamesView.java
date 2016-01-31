/*
 * Copyright 2015 Sam Elsharif
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
import info.msxlaunchers.openmsx.launcher.ui.presenter.DraggedAndDroppedGamesPresenter;

import java.io.File;
import java.util.Set;

/**
 * Interface for Add Dragged And Dropped Games UI View
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
public interface AddDraggedAndDroppedGamesView
{
	/**
	 * Displays screen to show dragged and dropped games and allow to add them to current database
	 * 
	 * @param presenter Instance of DraggedAndDroppedGamesPresenter
	 * @param currentDatabase Currently selected database to games to
	 * @param language Language
	 * @param rightToLeft Flag to determine screen orientation based on language
	 * @param files Dragged and dropped files
	 */
	void displayScreen( DraggedAndDroppedGamesPresenter presenter, String currentDatabase, Language language, boolean rightToLeft, File[] files, Set<String> machines );
}
