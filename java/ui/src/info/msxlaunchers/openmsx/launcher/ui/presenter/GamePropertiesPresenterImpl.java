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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import info.msxlaunchers.openmsx.common.ExternalLinksUtils;
import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.common.FileUtils;
import info.msxlaunchers.openmsx.game.repository.RepositoryData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.GamePropertiesView;

import com.google.inject.Inject;

/**
 * Implementation of <code>GamePropertiesPresenter</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
final class GamePropertiesPresenterImpl implements GamePropertiesPresenter
{
	private final GamePropertiesView view;
	private final RepositoryData repositoryData;

	@Inject
	GamePropertiesPresenterImpl( GamePropertiesView view, RepositoryData repositoryData )
	{
		this.view = Objects.requireNonNull( view );
		this.repositoryData = Objects.requireNonNull( repositoryData );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.GamePropertiesPresenter#onRequestGamePropertiesScreen(info.msxlaunchers.openmsx.launcher.data.game.Game, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestGamePropertiesScreen( Game game, Language currentLanguage, boolean currentRightToLeft )
	{
		RepositoryGame repositoryGame = null;
		int knownDumps = 0;

		//when sha1 code is missing then this is a script -=> ignore
		if( game.getSha1Code() != null )
		{
			try
			{
				repositoryGame = repositoryData.getGameInfo( game.getSha1Code() );
				knownDumps = repositoryData.getDumpCodes( game.getSha1Code() ).size();
			}
			catch( IOException e )
			{
				//in this case ignore - the properties screen will show nothing
			}
		}

		Path mainFile = Paths.get( FileTypeUtils.getMainFile(game.getRomA(), game.getRomB(), game.getDiskA(), game.getDiskB(),
        		game.getTape(), game.getHarddisk(), game.getLaserdisc(), game.getTclScript()));

		List<String> fileGroup;
		
		try
		{
			fileGroup = FileUtils.getFileGroup( mainFile );
		}
		catch( IOException ioe )
		{
			fileGroup = Collections.emptyList();
		}

		view.displayGamePropertiesScreen( game, repositoryGame, knownDumps, fileGroup, currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.GamePropertiesPresenter#isGenerationMSXIdValid(info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public boolean isGenerationMSXIdValid( Game game )
	{
		return ExternalLinksUtils.isGenerationMSXIdValid( game.getMsxGenID() );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.GamePropertiesPresenter#getGenerationMSXURL(info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public String getGenerationMSXURL( Game game )
	{
		return ExternalLinksUtils.getGenerationMSXURL( game.getMsxGenID() );
	}
}
