package info.msxlaunchers.openmsx.launcher.persistence.favorite;

import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.favorite.Favorite;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.persistence.BasicTestModule;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceModule;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Guice;
import com.google.inject.Injector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith( MockitoJUnitRunner.class )
public class EmbeddedDatabaseFavoritePersisterTest
{
	@Mock GameBuilder gameBuilder;

	@ClassRule
	public static TemporaryFolder tmpFolder = new TemporaryFolder();

	static private String dbLocation;
	static private String dbURL;

	static LauncherPersistence launcherPersistence;

	private static final String database1 = "database1";
	private static final String database2 = "database2";

	@BeforeClass
	public static void createDatabaseOnce() throws SQLException, LauncherPersistenceException
	{
		dbLocation = new File( new File( tmpFolder.getRoot().toString(), "databases" ), "launcherdb" ).toString();
		dbURL = "jdbc:derby:" + dbLocation;

		Injector injector = Guice.createInjector(
				new BasicTestModule( tmpFolder.getRoot().toString() ),
	    		new LauncherPersistenceModule()
				);

		launcherPersistence = injector.getInstance( LauncherPersistence.class );
		launcherPersistence.initialize();
	}

	@Before
	public void cleanupDatabase() throws SQLException
	{
		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
			try( PreparedStatement statement = connection.prepareStatement( "DELETE FROM database" ) )
			{
				statement.executeUpdate();
			}
		}
	}

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

		launcherPersistence.getFavoritePersister().addFavorite( new Favorite( game1.getName(), database1 ) );
		launcherPersistence.getFavoritePersister().addFavorite( new Favorite( game2.getName(), database1 ) );

		Set<Favorite> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 2, favorites.size() );
		assertTrue( favorites.contains( new Favorite( game1.getName(), database1 ) ) );
		assertTrue( favorites.contains( new Favorite( game2.getName(), database1 ) ) );

		//now add more favorites to a game in the other database
		launcherPersistence.getFavoritePersister().addFavorite( new Favorite( game3.getName(), database2 ) );

		favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 3, favorites.size() );
		assertTrue( favorites.contains( new Favorite( game1.getName(), database1 ) ) );
		assertTrue( favorites.contains( new Favorite( game2.getName(), database1 ) ) );
		assertTrue( favorites.contains( new Favorite( game3.getName(), database2 ) ) );
	}

	@Test
	public void test_whenDeleteFavorite_thenGetFavoritesDoesNotContainDeletedOnes() throws GamePersistenceException, FavoritePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine2" ).diskA( "diskA2" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );
		launcherPersistence.getGamePersister().saveGame( game2, database1 );

		launcherPersistence.getFavoritePersister().addFavorite( new Favorite( game1.getName(), database1 ) );
		launcherPersistence.getFavoritePersister().addFavorite( new Favorite( game2.getName(), database1 ) );

		Set<Favorite> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 2, favorites.size() );

		//now delete one of the favorites
		launcherPersistence.getFavoritePersister().deleteFavorite( new Favorite( game1.getName(), database1 ) );

		favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 1, favorites.size() );
		assertTrue( favorites.contains( new Favorite( game2.getName(), database1 ) ) );
	}

	@Test( expected = FavoritePersistenceException.class )
	public void test_whenAddFavoriteWithNonExistingGame_thenThrowException() throws GamePersistenceException, FavoritePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );

		try
		{
			launcherPersistence.getFavoritePersister().addFavorite( new Favorite( "nonExistingName", database1 ) );
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
			launcherPersistence.getFavoritePersister().addFavorite( new Favorite( "name1", "nonExistingDatabase" ) );
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

		launcherPersistence.getFavoritePersister().addFavorite( new Favorite( "name1", database1 ) );

		try
		{
			launcherPersistence.getFavoritePersister().addFavorite( new Favorite( "name1", database1 ) );
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

		launcherPersistence.getFavoritePersister().addFavorite( new Favorite( game1.getName(), database1 ) );
		launcherPersistence.getFavoritePersister().addFavorite( new Favorite( game2.getName(), database1 ) );

		Set<Favorite> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 2, favorites.size() );

		//now delete one of the games
		launcherPersistence.getGamePersister().deleteGame( game1, database1 );

		favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 1, favorites.size() );
		assertTrue( favorites.contains( new Favorite( game2.getName(), database1 ) ) );
	}

	@Test
	public void test_givenNoFavoritesSaved_whenGetFavorites_thenReturnEmptySet() throws FavoritePersistenceException
	{
		Set<Favorite> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		assertEquals( 0, favorites.size() );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void test_whenAddToFavoritesSetFromGetFavorites_thenReturnThrowException() throws GamePersistenceException, FavoritePersistenceException
	{
		Set<Favorite> favorites = launcherPersistence.getFavoritePersister().getFavorites();

		favorites.add( new Favorite( "gameName", database1 ) );
	}
}
