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

import com.google.inject.Inject;
import com.google.inject.name.Named;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.GamePropertiesWindow;

/**
 * Swing-based implementation of <code>GamePropertiesView</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
class GamePropertiesSwingView implements GamePropertiesView
{
	private final String generationMSXURL;

	@Inject
	public GamePropertiesSwingView( @Named("GenerationMSXURL") String generationMSXURL )
	{
		this.generationMSXURL = generationMSXURL;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.GamePropertiesView#displayGamePropertiesScreen(info.msxlaunchers.openmsx.launcher.data.game.Game, info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame, int, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void displayGamePropertiesScreen( Game game, RepositoryGame repositoryGame, int knownDumps, Language language, boolean rightToLeft )
	{
		GamePropertiesWindow gamePropertiesWindow = new GamePropertiesWindow( game, repositoryGame, knownDumps, language, rightToLeft, generationMSXURL );

		gamePropertiesWindow.display();
	}
}
