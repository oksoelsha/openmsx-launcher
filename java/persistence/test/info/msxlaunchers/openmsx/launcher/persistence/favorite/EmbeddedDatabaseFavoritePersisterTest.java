package info.msxlaunchers.openmsx.launcher.persistence.favorite;

import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.persistence.DatabaseTest;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith( MockitoJUnitRunner.class )
public class EmbeddedDatabaseFavoritePersisterTest extends DatabaseTest
{
	@Mock GameBuilder gameBuilder;

	private static final String database1 = "database1";
	private static final String database2 = "database2";

	@Test
	public void test_whenAddFavorite_thenGetFavoritesContainsAddedOnes() throws GamePersistenceException, FavoritePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine2" ).diskA( "diskA2" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );
		launcherPersistence.getGamePersister().saveGame( game2, database1 );

		Game game3 = Game.name( "name3" ).machine( "machine3" ).romA( "romA3" ).build();

		launcherPersistence.getGamePersister().createDatabase( database2 );
		launcherPersistence.getGamePersister().saveGame( game3, database2 );

		launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( game1.getName(), database1 ) );
		launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( game2.getName(), database1 ) );

		Set<DatabaseItem> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 2, favorites.size() );
		assertTrue( favorites.contains( new DatabaseItem( game1.getName(), database1 ) ) );
		assertTrue( favorites.contains( new DatabaseItem( game2.getName(), database1 ) ) );

		//now add more favorites to a game in the other database
		launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( game3.getName(), database2 ) );

		favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 3, favorites.size() );
		assertTrue( favorites.contains( new DatabaseItem( game1.getName(), database1 ) ) );
		assertTrue( favorites.contains( new DatabaseItem( game2.getName(), database1 ) ) );
		assertTrue( favorites.contains( new DatabaseItem( game3.getName(), database2 ) ) );
	}

	@Test
	public void test_whenDeleteFavorite_thenGetFavoritesDoesNotContainDeletedOnes() throws GamePersistenceException, FavoritePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine2" ).diskA( "diskA2" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );
		launcherPersistence.getGamePersister().saveGame( game2, database1 );

		launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( game1.getName(), database1 ) );
		launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( game2.getName(), database1 ) );

		Set<DatabaseItem> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 2, favorites.size() );

		//now delete one of the favorites
		launcherPersistence.getFavoritePersister().deleteFavorite( new DatabaseItem( game1.getName(), database1 ) );

		favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 1, favorites.size() );
		assertTrue( favorites.contains( new DatabaseItem( game2.getName(), database1 ) ) );
	}

	@Test( expected = FavoritePersistenceException.class )
	public void test_whenAddFavoriteWithNonExistingGame_thenThrowException() throws GamePersistenceException, FavoritePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );

		try
		{
			launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( "nonExistingName", database1 ) );
		}
		catch( FavoritePersistenceException fpe )
		{
			assertEquals( FavoritePersistenceExceptionIssue.GAME_OR_DATABASE_NOT_FOUND, fpe.getIssue() );
			throw fpe;
		}
	}

	@Test( expected = FavoritePersistenceException.class )
	public void test_whenAddFavoriteWithNonExistingDatabase_thenThrowException() throws GamePersistenceException, FavoritePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );

		try
		{
			launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( "name1", "nonExistingDatabase" ) );
		}
		catch( FavoritePersistenceException fpe )
		{
			assertEquals( FavoritePersistenceExceptionIssue.GAME_OR_DATABASE_NOT_FOUND, fpe.getIssue() );
			throw fpe;
		}
	}

	@Test( expected = FavoritePersistenceException.class )
	public void test_whenAddSameFavoriteTwice_thenThrowException() throws GamePersistenceException, FavoritePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );

		launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( "name1", database1 ) );

		try
		{
			launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( "name1", database1 ) );
		}
		catch( FavoritePersistenceException fpe )
		{
			assertEquals( FavoritePersistenceExceptionIssue.FAVORITE_ALREADY_EXISTS, fpe.getIssue() );
			throw fpe;
		}
	}

	@Test
	public void test_whenDeleteGame_thenFavoriteIsDeletedByCascade() throws GamePersistenceException, FavoritePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine2" ).diskA( "diskA2" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );
		launcherPersistence.getGamePersister().saveGame( game2, database1 );

		launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( game1.getName(), database1 ) );
		launcherPersistence.getFavoritePersister().addFavorite( new DatabaseItem( game2.getName(), database1 ) );

		Set<DatabaseItem> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 2, favorites.size() );

		//now delete one of the games
		launcherPersistence.getGamePersister().deleteGame( game1, database1 );

		favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 1, favorites.size() );
		assertTrue( favorites.contains( new DatabaseItem( game2.getName(), database1 ) ) );
	}

	@Test
	public void test_givenNoFavoritesSaved_whenGetFavorites_thenReturnEmptySet() throws FavoritePersistenceException
	{
		Set<DatabaseItem> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 0, favorites.size() );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void test_whenAddToFavoritesSetFromGetFavorites_thenReturnThrowException() throws GamePersistenceException, FavoritePersistenceException
	{
		Set<DatabaseItem> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		favorites.add( new DatabaseItem( "gameName", database1 ) );
	}
}
