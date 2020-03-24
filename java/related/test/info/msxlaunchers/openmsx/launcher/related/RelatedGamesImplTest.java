package info.msxlaunchers.openmsx.launcher.related;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;

@RunWith( MockitoJUnitRunner.class )
public class RelatedGamesImplTest
{
	@Mock ExtraDataGetter extraDataGetter;

	@Test
	public void test() throws FileNotFoundException, IOException
	{
		Map<String,ExtraData> extraDataMap = new HashMap<>();
		extraDataMap.put( "hash1", new ExtraData( 11, 2, 3, Genre.SHOOT_EM_UP_ALL.getValue(), 0, "a" ) );
		extraDataMap.put( "hash2", new ExtraData( 22, 2, 3, Genre.SHOOT_EM_UP_ALL.getValue(), 0, null ) );
		extraDataMap.put( "hash3", new ExtraData( 33, 2, 3, Genre.MAZE.getValue(), 0, "e" ) );
		extraDataMap.put( "hash4", new ExtraData( 44, 2, 3, Genre.SHOOT_EM_UP_ALL.getValue(), 0, "" ) );
		extraDataMap.put( "hash5", new ExtraData( 55, 2, 3, Genre.ACTION.getValue(), 0, "" ) );
		extraDataMap.put( "hash6", new ExtraData( 66, 2, 3, Genre.ADULT.getValue(), 0, "" ) );
		extraDataMap.put( "hash7", new ExtraData( 77, 2, 3, Genre.ADULT.getValue(), 0, "" ) );

		Mockito.when( extraDataGetter.getExtraData() ).thenReturn( extraDataMap );

		Map<String,RepositoryGame> repositoryInfoMap = new HashMap<>();
		repositoryInfoMap.put( "hash1", RepositoryGame.title( "Gradius - Nemesis" ).company( "kona" ).system( "MSX" ).year( "1990" ).country( "AA" ).build() );
		repositoryInfoMap.put( "hash2", RepositoryGame.title( "Manbow" ).company( "kona" ).system( "MSX" ).year( "1980" ).country( "BB" ).build() );
		repositoryInfoMap.put( "hash3", RepositoryGame.title( "Cat and Mouse" ).company( "sony" ).system( "MSX" ).year( "1990" ).country( "AA" ).build() );
		repositoryInfoMap.put( "hash4", RepositoryGame.title( "Gradius 21 - Nemesis 7" ).company( "kona" ).system( "MSX" ).year( "19xx" ).country( "AA" ).build() );
		repositoryInfoMap.put( "hash5", RepositoryGame.title( "Vampire Killer" ).company( "kona" ).system( "MSX" ).year( "1988" ).country( "CC" ).build() );
		repositoryInfoMap.put( "hash6", RepositoryGame.title( "Great Vampire" ).company( "some company" ).system( "MSX" ).year( "1988" ).country( "CC" ).build() );
		repositoryInfoMap.put( "hash7", RepositoryGame.title( "Night Fun" ).company( "company name" ).system( "MSX" ).year( "2002" ).country( "VV" ).build() );

		RelatedGamesImpl relatedGamesImpl = new RelatedGamesImpl( extraDataGetter, repositoryInfoMap );

		List<RelatedGame> relatedGames;

		relatedGames = relatedGamesImpl.findRelated( Game.name( "Name is irrelevant" ).sha1Code( "hash1" ).genre1( Genre.SHOOT_EM_UP_ALL ).genre2( Genre.UNKNOWN ).msxGenID( 11 ).build() );
		Assert.assertEquals( Arrays.asList( "Gradius 21 - Nemesis 7", "Manbow", "Vampire Killer" ), getMatchedGameNames( relatedGames ) );

		relatedGames = relatedGamesImpl.findRelated( Game.name( "Name is irrelevant" ).sha1Code( "hash6" ).genre1( Genre.ADULT ).genre2( Genre.UNKNOWN ).msxGenID( 66 ).build() );
		Assert.assertEquals( Arrays.asList( "Night Fun", "Vampire Killer" ), getMatchedGameNames( relatedGames ) );

		relatedGames = relatedGamesImpl.findRelated( Game.name( "Name is irrelevant" ).sha1Code( "hash7" ).genre1( Genre.ADULT ).genre2( Genre.UNKNOWN ).msxGenID( 77 ).build() );
		Assert.assertEquals( Arrays.asList( "Great Vampire" ), getMatchedGameNames( relatedGames ) );
	}

	private List<String> getMatchedGameNames( List<RelatedGame> relatedGames )
	{
		return relatedGames.stream().map( RelatedGame::getGameName ).collect( Collectors.toList() );
	}
}
