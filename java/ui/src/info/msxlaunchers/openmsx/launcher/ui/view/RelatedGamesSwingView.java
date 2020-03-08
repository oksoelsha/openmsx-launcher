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
package info.msxlaunchers.openmsx.launcher.ui.view;

import java.util.List;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.RelatedGamesPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.RelatedGamesWindow;

/**
 * Swing-based implementation of <code>RelatedGamesView</code>
 * 
 * @since v1.13
 * @author Sam Elsharif
 *
 */
final class RelatedGamesSwingView implements RelatedGamesView
{
	private final RelatedGamesPresenter presenter;

	@Inject
	public RelatedGamesSwingView( RelatedGamesPresenter presenter )
	{
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.RelatedGamesView#displayRelatedGamesScreen(java.lang.String, java.util.List, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void displayRelatedGamesScreen( String gameName, List<RelatedGame> relatedGames, Language language, boolean rightToLeft )
	{
		RelatedGamesWindow window = new RelatedGamesWindow( presenter, gameName, relatedGames, language, rightToLeft );

		window.displayScreen();
	}
}
