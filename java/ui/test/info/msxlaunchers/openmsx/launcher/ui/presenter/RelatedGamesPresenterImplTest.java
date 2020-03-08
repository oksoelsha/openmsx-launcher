package info.msxlaunchers.openmsx.launcher.ui.presenter;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.launcher.related.RelatedGames;
import info.msxlaunchers.openmsx.launcher.related.RelatedGamesFactory;
import info.msxlaunchers.openmsx.launcher.ui.view.RelatedGamesView;

@RunWith( MockitoJUnitRunner.class )
public class RelatedGamesPresenterImplTest
{
	@Mock RelatedGamesFactory relatedGamesFactory;
	@Mock RelatedGamesView view;
	@Mock SettingsPersister settingsPersister;
	@Mock RelatedGames relatedGames;

	private Settings settings;
	private RelatedGamesPresenterImpl relatedGamesPresenterImpl;

	@Before
	public void setup() throws IOException
	{
		settings = new Settings( "openMSXPath", "machinesPath", "screenshotsPath", null, Language.CATALAN, false, false );
		when( settingsPersister.getSettings() ).thenReturn( settings );

		relatedGamesPresenterImpl = new RelatedGamesPresenterImpl( relatedGamesFactory, view, settingsPersister );
	}

	@Test
	public void givenRelatedGame_whenOnRequestRelatedGamesScreen_thenReturnCorrectAnswer() throws LauncherException
	{
		Map<String,RepositoryGame> repositoryInfoMap = Collections.emptyMap();
		List<RelatedGame> relatedGamesList = Collections.emptyList();
		Game game = Game.name( "gameName" ).build();
		Language language = Language.FINNISH;
		boolean rightToLeft = false;

		when( relatedGamesFactory.create( repositoryInfoMap ) ).thenReturn( relatedGames );
		when( relatedGames.findRelated( game ) ).thenReturn( relatedGamesList );

		relatedGamesPresenterImpl.onRequestRelatedGamesScreen( game, repositoryInfoMap, language, rightToLeft );

		Mockito.verify( view, Mockito.times( 1) ).displayRelatedGamesScreen( game.getName(), relatedGamesList, language, rightToLeft );
	}

	@Test
	public void givenRelatedGame_whenIsMSXGenerationIdValid_thenReturnCorrectAnswer()
	{
		Assert.assertFalse( relatedGamesPresenterImpl.isGenerationMSXIdValid( new RelatedGame("name1", "company1", "1990", 0 ) ) );
		Assert.assertTrue( relatedGamesPresenterImpl.isGenerationMSXIdValid( new RelatedGame("name2", "company2", "1990", 1 ) ) );
		Assert.assertTrue( relatedGamesPresenterImpl.isGenerationMSXIdValid( new RelatedGame("name3", "company3", "1990", 9999 ) ) );
		Assert.assertFalse( relatedGamesPresenterImpl.isGenerationMSXIdValid( new RelatedGame("name4", "company4", "1990", 10000 ) ) );
	}

	@Test
	public void givenRelatedGame_whenGetMSXGenerationURL_thenReturnGenerationMSXURL()
	{
		Assert.assertEquals( "http://www.generation-msx.nl/msxdb/softwareinfo/900", relatedGamesPresenterImpl.getGenerationMSXURL( new RelatedGame( "name1", "company1", "1990", 900 ) ) );
	}

	@Test
	public void givenRelatedGame_whenGetYouTubeURL_thenReturnYouTubeURL()
	{
		Assert.assertEquals( "https://www.youtube.com/results?search_query=MSX+part1+part2", relatedGamesPresenterImpl.getYouTubeURL( "part1 part2" ) );
	}
}
