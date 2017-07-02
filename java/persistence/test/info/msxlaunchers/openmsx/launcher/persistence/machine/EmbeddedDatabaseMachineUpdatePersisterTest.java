package info.msxlaunchers.openmsx.launcher.persistence.machine;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.persistence.DatabaseTest;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith( MockitoJUnitRunner.class )
public class EmbeddedDatabaseMachineUpdatePersisterTest extends DatabaseTest
{
	private static final String database1 = "database1";
	private static final String database2 = "database2";

	@Before
	public void initializeDatabases() throws GamePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine2" ).diskA( "diskA2" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );
		launcherPersistence.getGamePersister().saveGame( game2, database1 );

		Game game3 = Game.name( "name3" ).machine( "machine3" ).romA( "romA3" ).build();
		Game game4 = Game.name( "name4" ).machine( "machine2" ).romA( "romA4" ).build();

		launcherPersistence.getGamePersister().createDatabase( database2 );
		launcherPersistence.getGamePersister().saveGame( game3, database2 );
		launcherPersistence.getGamePersister().saveGame( game4, database2 );
	}

	@Test
	public void whenChangeAllMachines_thenAllMachinesAreChanged() throws GamePersistenceException, MachineUpdatePersistenceException
	{
		int totalUpdated = launcherPersistence.getMachineUpdatePersister().update( "newMachine", null, null );
		assertEquals( 4, totalUpdated );

		Set<Game> games = launcherPersistence.getGamePersister().getGames( database1 );

		assertEquals( 2, games.size() );
		assertTrue( games.stream().map( Game::getMachine ).allMatch( m -> "newMachine".equals( m ) ) );

		games = launcherPersistence.getGamePersister().getGames( database2 );

		assertEquals( 2, games.size() );
		assertTrue( games.stream().map( Game::getMachine ).allMatch( m -> "newMachine".equals( m ) ) );
	}

	@Test
	public void whenChangeSpecificMachineInAllDatabases_thenOnlySpecificMachineIsChanged() throws GamePersistenceException, MachineUpdatePersistenceException
	{
		int totalUpdated = launcherPersistence.getMachineUpdatePersister().update( "newMachine", "machine2", null );
		assertEquals( 2, totalUpdated );

		Set<Game> games = launcherPersistence.getGamePersister().getGames( database1 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name1" ) )
			{
				assertEquals( "machine", game.getMachine() );
			}
			else
			{
				assertEquals( "newMachine", game.getMachine() );
			}
		}

		games = launcherPersistence.getGamePersister().getGames( database2 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name3" ) )
			{
				assertEquals( "machine3", game.getMachine() );
			}
			else
			{
				assertEquals( "newMachine", game.getMachine() );
			}
		}
	}

	@Test
	public void whenChangeSpecificMachineInSpecificDatabase_thenOnlySpecificMachineInSpecificDatabaseIsChanged() throws GamePersistenceException, MachineUpdatePersistenceException
	{
		int totalUpdated = launcherPersistence.getMachineUpdatePersister().update( "newMachine", "machine2", database2 );
		assertEquals( 1, totalUpdated );

		Set<Game> games = launcherPersistence.getGamePersister().getGames( database1 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name1" ) )
			{
				assertEquals( "machine", game.getMachine() );
			}
			else
			{
				assertEquals( "machine2", game.getMachine() );
			}
		}

		games = launcherPersistence.getGamePersister().getGames( database2 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name3" ) )
			{
				assertEquals( "machine3", game.getMachine() );
			}
			else
			{
				assertEquals( "newMachine", game.getMachine() );
			}
		}
	}

	@Test
	public void whenChangeAllMachinesInSpecificDatabase_thenAllMachinesInSpecificDatabaseAreChanged() throws GamePersistenceException, MachineUpdatePersistenceException
	{
		int totalUpdated = launcherPersistence.getMachineUpdatePersister().update( "newMachine", null, database1 );
		assertEquals( 2, totalUpdated );

		Set<Game> games = launcherPersistence.getGamePersister().getGames( database1 );

		assertEquals( 2, games.size() );
		assertTrue( games.stream().map( Game::getMachine ).allMatch( m -> "newMachine".equals( m ) ) );

		games = launcherPersistence.getGamePersister().getGames( database2 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name3" ) )
			{
				assertEquals( "machine3", game.getMachine() );
			}
			else
			{
				assertEquals( "machine2", game.getMachine() );
			}
		}
	}
}
