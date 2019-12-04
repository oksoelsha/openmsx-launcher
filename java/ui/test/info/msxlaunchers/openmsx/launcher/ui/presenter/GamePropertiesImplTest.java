package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.game.repository.RepositoryData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.GamePropertiesView;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class GamePropertiesImplTest
{
	@Mock GamePropertiesView gamePropertiesView;
	@Mock RepositoryData repositoryData;
	@Mock Set<String> otherCodes;

	@Test
	public void testConstructor()
	{
		new GamePropertiesPresenterImpl( gamePropertiesView, repositoryData );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null()
	{
		new GamePropertiesPresenterImpl( null, repositoryData );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null()
	{
		new GamePropertiesPresenterImpl( gamePropertiesView, null );
	}

	@Test
	public void testOnRequestGamePropertiesScreen() throws IOException
	{
		GamePropertiesPresenterImpl presenter = new GamePropertiesPresenterImpl( gamePropertiesView, repositoryData );

		Game game = Game.name( "name" ).diskA( "diskA.dsk" ).sha1Code( "1234" ).build();
		RepositoryGame repositoryGame = RepositoryGame.title( "title" ).system( "system" ).company( "company" ).year( "year" ).country( "country" ).build();

		when( repositoryData.getGameInfo( game.getSha1Code() ) ).thenReturn( repositoryGame );
		when( otherCodes.size() ).thenReturn( 3 );
		when( repositoryData.getDumpCodes( game.getSha1Code() ) ).thenReturn( otherCodes );

		presenter.onRequestGamePropertiesScreen( game, Language.ENGLISH, false );

		verify( gamePropertiesView, times( 1 ) ).displayGamePropertiesScreen( game, repositoryGame, 3, Collections.emptyList(), Language.ENGLISH, false );
	}

	@Test
	public void testOnRequestGamePropertiesScreenIOException() throws IOException
	{
		GamePropertiesPresenterImpl presenter = new GamePropertiesPresenterImpl( gamePropertiesView, repositoryData );

		Game game = Game.name( "name" ).romA( "romA.rom" ).sha1Code( "1234" ).build();

		when( repositoryData.getGameInfo( game.getSha1Code() ) ).thenThrow( new IOException() );

		presenter.onRequestGamePropertiesScreen( game, Language.ENGLISH, false );

		verify( gamePropertiesView, times( 1 ) ).displayGamePropertiesScreen( game, null, 0, Collections.emptyList(), Language.ENGLISH, false );
	}
}