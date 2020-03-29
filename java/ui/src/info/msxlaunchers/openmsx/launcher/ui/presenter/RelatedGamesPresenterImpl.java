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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.common.ExternalLinksUtils;
import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
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
	private final String scrrenshotsPath;
	private final GamePersister gamePersister;
	private final MainPresenter mainPresenter;

	@Inject
	RelatedGamesPresenterImpl( RelatedGamesFactory relatedGamesFactory, RelatedGamesView view, SettingsPersister settingsPersister,
			GamePersister gamePersister, MainPresenter mainPresenter )
			throws IOException
	{
		this.relatedGamesFactory = relatedGamesFactory;
		this.view = view;
		this.scrrenshotsPath = settingsPersister.getSettings().getScreenshotsFullPath();
		this.gamePersister = gamePersister;
		this.mainPresenter = mainPresenter;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.RelatedGamesPresenter#onRequestRelatedGamesScreen(info.msxlaunchers.openmsx.launcher.data.game.Game, java.util.Map, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestRelatedGamesScreen( Game game, Map<String,RepositoryGame> repositoryInfoMap, Language currentLanguage, boolean currentRightToLeft )
			throws LauncherException
	{
		List<RelatedGame> relatedGames;
		try
		{
			relatedGames = relatedGamesFactory.create( repositoryInfoMap ).findRelated( game );
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		try
		{
			relatedGames = gamePersister.getRelatedGamesWithLauncherLinks( relatedGames );
		}
		catch ( GamePersistenceException gpe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		view.displayRelatedGamesScreen( game.getName(), relatedGames, currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.RelatedGamesPresenter#onRequestHiglightGameInLauncher(info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem)
	 */
	public void onRequestHighlightGameInLauncher( DatabaseItem databaseItem ) throws LauncherException
	{
		mainPresenter.onSelectDatabaseItem( databaseItem );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.RelatedGamesPresenter#getScreenshotPath(int)
	 */
	public Path getScreenshotPath( int msxGenId )
	{
		String[] variations = {"b", "b-en", "b-a"};
		for( int index = 0; index < variations.length; index++ )
		{
			Path path = Paths.get( scrrenshotsPath, msxGenId + variations[index] + ".png" );
			if( path.toFile().exists())
			{
				return path;
			}
		}

		//here just return any path that doesn't exist so that No Screenshot image is shown
		return Paths.get( scrrenshotsPath, msxGenId + "b.png" );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.RelatedGamesPresenter#isGenerationMSXIdValid(info.msxlaunchers.openmsx.launcher.data.game.RelatedGame)
	 */
	@Override
	public boolean isGenerationMSXIdValid( RelatedGame relatedGame )
	{
		return ExternalLinksUtils.isGenerationMSXIdValid( relatedGame.getMSXGenId() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.RelatedGamesPresenter#getGenerationMSXURL(info.msxlaunchers.openmsx.launcher.data.game.RelatedGame)
	 */
	@Override
	public String getGenerationMSXURL( RelatedGame relatedGame )
	{
		return ExternalLinksUtils.getGenerationMSXURL( relatedGame.getMSXGenId() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.RelatedGamesPresenter#getYouTubeURL(java.lang.String)
	 */
	@Override
	public String getYouTubeURL( String gameName )
	{
		return ExternalLinksUtils.getYouTubeSearchURL( gameName );
	}
}
