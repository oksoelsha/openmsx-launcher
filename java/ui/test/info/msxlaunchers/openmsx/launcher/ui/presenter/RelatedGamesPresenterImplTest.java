package info.msxlaunchers.openmsx.launcher.ui.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
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
	@Mock GamePersister gamePersister;
	@Mock MainPresenter mainPresenter;

	@Rule public TemporaryFolder tmpFolder = new TemporaryFolder();

	private Settings settings;
	private RelatedGamesPresenterImpl relatedGamesPresenterImpl;

	@Before
	public void setup() throws IOException
	{
		settings = new Settings( "openMSXPath", "machinesPath", tmpFolder.getRoot().toString(), null, Language.CATALAN, false, false );
		when( settingsPersister.getSettings() ).thenReturn( settings );

		relatedGamesPresenterImpl = new RelatedGamesPresenterImpl( relatedGamesFactory, view, settingsPersister, gamePersister, mainPresenter );
	}

	@Test
	public void givenGame_whenOnRequestRelatedGamesScreen_thenGetRelatedGamesAndDisplayScreen()
			throws LauncherException, IOException, GamePersistenceException
	{
		Map<String,RepositoryGame> repositoryInfoMap = Collections.emptyMap();
		List<RelatedGame> relatedGamesList = Collections.emptyList();
		Game game = Game.name( "gameName" ).build();
		Language language = Language.FINNISH;
		boolean rightToLeft = false;

		when( relatedGamesFactory.create( repositoryInfoMap ) ).thenReturn( relatedGames );
		when( relatedGames.findRelated( game ) ).thenReturn( relatedGamesList );

		relatedGamesPresenterImpl.onRequestRelatedGamesScreen( game, repositoryInfoMap, language, rightToLeft );

		Mockito.verify( gamePersister, Mockito.times( 1) ).getRelatedGamesWithLauncherLinks( relatedGamesList );
		Mockito.verify( view, Mockito.times( 1) ).displayRelatedGamesScreen( game.getName(), relatedGamesList, language, rightToLeft );
	}

	@Test(expected = LauncherException.class)
	public void givenFindingRelatedGamesInOpenMSXAndExtraDataThrowsException_whenOnRequestRelatedGamesScreen_thenThrowException()
			throws LauncherException, IOException, GamePersistenceException
	{
		Map<String,RepositoryGame> repositoryInfoMap = Collections.emptyMap();
		Game game = Game.name( "gameName" ).build();
		Language language = Language.FINNISH;
		boolean rightToLeft = false;

		when( relatedGamesFactory.create( repositoryInfoMap ) ).thenReturn( relatedGames );
		when( relatedGames.findRelated( game ) ).thenThrow( IOException.class );

		try
		{
			relatedGamesPresenterImpl.onRequestRelatedGamesScreen( game, repositoryInfoMap, language, rightToLeft );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test(expected = LauncherException.class)
	public void givenPersistenceGetThrowsException_whenOnRequestRelatedGamesScreen_thenThrowException()
			throws LauncherException, IOException, GamePersistenceException
	{
		Map<String,RepositoryGame> repositoryInfoMap = Collections.emptyMap();
		List<RelatedGame> relatedGamesList = Collections.emptyList();
		Game game = Game.name( "gameName" ).build();
		Language language = Language.FINNISH;
		boolean rightToLeft = false;

		when( relatedGamesFactory.create( repositoryInfoMap ) ).thenReturn( relatedGames );
		when( relatedGames.findRelated( game ) ).thenReturn( relatedGamesList );
		when( gamePersister.getRelatedGamesWithLauncherLinks( relatedGamesList ) ).thenThrow( GamePersistenceException.class );

		try
		{
			relatedGamesPresenterImpl.onRequestRelatedGamesScreen( game, repositoryInfoMap, language, rightToLeft );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void givenDatabaseItem_whenOnRequestHiglightGameInLauncher_thenRelayToMainPresenter() throws LauncherException
	{
		DatabaseItem databaseItem = new DatabaseItem( "gameName", "database" );

		relatedGamesPresenterImpl.onRequestHighlightGameInLauncher( databaseItem );

		Mockito.verify( mainPresenter, Mockito.times( 1) ).onSelectDatabaseItem( databaseItem );
	}

	@Test
	public void givenScreenshot_b_whenGetScreenshotPath_returnScreenshot() throws IOException
	{
		File tmpFile = tmpFolder.newFile( "123b.png" );

		Path path = relatedGamesPresenterImpl.getScreenshotPath( 123 );

		assertEquals( tmpFile, path.toFile() );
	}

	@Test
	public void givenScreenshot_b_en_whenGetScreenshotPath_returnScreenshot() throws IOException
	{
		File tmpFile = tmpFolder.newFile( "123b-en.png" );

		Path path = relatedGamesPresenterImpl.getScreenshotPath( 123 );

		assertEquals( tmpFile, path.toFile() );
	}

	@Test
	public void givenScreenshot_b_awhenGetScreenshotPath_returnScreenshot() throws IOException
	{
		File tmpFile = tmpFolder.newFile( "123b-a.png" );

		Path path = relatedGamesPresenterImpl.getScreenshotPath( 123 );

		assertEquals( tmpFile, path.toFile() );
	}

	@Test
	public void givenNonExistentScreenshot_awhenGetScreenshotPath_returnNonExistentScreenshot() throws IOException
	{
		Path path = relatedGamesPresenterImpl.getScreenshotPath( 123 );

		assertFalse( path.toFile().exists() );
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
