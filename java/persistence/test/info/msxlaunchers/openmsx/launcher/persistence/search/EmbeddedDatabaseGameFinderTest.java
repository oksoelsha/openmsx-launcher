package info.msxlaunchers.openmsx.launcher.persistence.search;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.persistence.BasicTestModule;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceModule;
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
import org.mockito.junit.MockitoJUnitRunner;

import com.google.inject.Guice;
import com.google.inject.Injector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith( MockitoJUnitRunner.class )
public class EmbeddedDatabaseGameFinderTest
{
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
	public void givenStringToSearch_whenSearchDatabase_thenReturnMatches() throws GamePersistenceException
	{
		Game game1 = Game.name( "racing" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "fighting" ).machine( "machine2" ).diskA( "diskA2" ).build();
		Game game3 = Game.name( "fighting 2" ).machine( "machine3" ).diskA( "diskA3" ).build();
		Game game4 = Game.name( "Arcade" ).machine( "machine3" ).romA( "diskA7" ).sha1Code( "a1e4fb56433309ed" ).build();
		Game game5 = Game.name( "sports" ).machine( "machine" ).romA( "romD" ).build();
		Game game6 = Game.name( "more sports" ).machine( "machine2" ).diskA( "diskA4" ).build();
		Game game7 = Game.name( "Additional Sports" ).machine( "machine3" ).diskA( "diskA6" ).build();
		Game game8 = Game.name( "aports extended" ).machine( "machine3" ).romA( "diskA4" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );
		launcherPersistence.getGamePersister().saveGame( game2, database1 );
		launcherPersistence.getGamePersister().saveGame( game5, database1 );
		launcherPersistence.getGamePersister().saveGame( game6, database1 );

		launcherPersistence.getGamePersister().createDatabase( database2 );
		launcherPersistence.getGamePersister().saveGame( game3, database2 );
		launcherPersistence.getGamePersister().saveGame( game4, database2 );
		launcherPersistence.getGamePersister().saveGame( game7, database2 );
		launcherPersistence.getGamePersister().saveGame( game8, database2 );

		//one match
		Set<DatabaseItem> matches = launcherPersistence.getGameFinder().find( "arca", 5 );
		assertEquals( 1, matches.size() );
		DatabaseItem item = matches.iterator().next();
		assertEquals( "Arcade", item.getGameName() );
		assertEquals( database2, item.getDatabase() );

		//two matches
		matches = launcherPersistence.getGameFinder().find( "ting", 5 );
		assertEquals( 2, matches.size() );
		assertTrue( matches.contains(  new DatabaseItem( "fighting", database1 ) ) );
		assertTrue( matches.contains(  new DatabaseItem( "fighting 2", database2 ) ) );

		//sha1 match
		matches = launcherPersistence.getGameFinder().find( "a1e4fb", 5 );
		assertEquals( 1, matches.size() );
		assertTrue( matches.contains(  new DatabaseItem( "Arcade", database2 ) ) );

		//maximum return - even there are more items in the database
		matches = launcherPersistence.getGameFinder().find( "sport", 3 );
		assertEquals( 3, matches.size() );
		//since I don't know which three will be return - this will not be tested

		//no matches - get an empty set
		matches = launcherPersistence.getGameFinder().find( "nothing", 3 );
		assertEquals( 0, matches.size() );
	}

	@Test
	public void givenEmptyString_whenSearchDatabase_thenReturnEmptySet()
	{
		Set<DatabaseItem> matches = launcherPersistence.getGameFinder().find( "", 1 );

		assertEquals( 0, matches.size() );
	}

	@Test
	public void givenNullString_whenSearchDatabase_thenReturnEmptySet()
	{
		Set<DatabaseItem> matches = launcherPersistence.getGameFinder().find( null, 1 );

		assertEquals( 0, matches.size() );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void whenAddToSetReturnedFromSearchDatabase_thenThrowException()
	{
		Set<DatabaseItem> matches = launcherPersistence.getGameFinder().find( "something", 1 );

		matches.add( new DatabaseItem( "gameName", database1 ) );
	}
}
