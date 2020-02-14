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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.launcher.related.RelatedGamesFactory;
import info.msxlaunchers.openmsx.launcher.ui.view.RelatedGamesView;

/**
 * Implementation of <code>RelatedGamesPresenter</code>
 * 
 * @since v1.13
 * @author Sam Elsharif
 *
 */
final class RelatedGamesPresenterImpl implements RelatedGamesPresenter
{
	private final RelatedGamesFactory relatedGamesFactory;
	private final RelatedGamesView view;
	private final MainPresenter mainPresenter;
	private final String scrrenshotsPath;

	@Inject
	RelatedGamesPresenterImpl( RelatedGamesFactory relatedGamesFactory, RelatedGamesView view, MainPresenter mainPresenter, SettingsPersister settingsPersister ) throws IOException
	{
		this.relatedGamesFactory = relatedGamesFactory;
		this.view = view;
		this.mainPresenter = Objects.requireNonNull( mainPresenter );
		this.scrrenshotsPath = settingsPersister.getSettings().getScreenshotsFullPath();
	}

	@Override
	public void onRequestRelatedGamesScreen( Game game, Map<String,RepositoryGame> repositoryInfoMap, Language currentLanguage, boolean currentRightToLeft )
			throws LauncherException
	{
		List<RelatedGame> relatedGames = relatedGamesFactory.create( repositoryInfoMap ).findRelated( game );

		view.displayRelatedGamesScreen( relatedGames, scrrenshotsPath, currentLanguage, currentRightToLeft );
	}
}
