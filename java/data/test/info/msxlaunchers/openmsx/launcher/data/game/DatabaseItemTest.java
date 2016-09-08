package info.msxlaunchers.openmsx.launcher.data.game;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.Game;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseItemTest
{
	private Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).build();
	private String database = "database";

	@Test
	public void testFavorite()
	{
		DatabaseItem favorite = new DatabaseItem( game.getName(), database );

		assertEquals( game.getName(), favorite.getGameName() );
		assertEquals( database, favorite.getDatabase() );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testConstructorArg1Null()
	{
		new DatabaseItem( null, database );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testConstructorArg2Null()
	{
		new DatabaseItem( game.getName(), null );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		DatabaseItem databaseItem1a = new DatabaseItem( "name1", "database1" );
		DatabaseItem databaseItem1b = new DatabaseItem( "name1", "database1" );
		DatabaseItem databaseItem2 = new DatabaseItem( "name1", "database2" );
		DatabaseItem databaseItem3 = new DatabaseItem( "name2", "database1" );

		assertEquals( databaseItem1a, databaseItem1a );
		assertEquals( databaseItem1a, databaseItem1b );
		assertNotEquals( databaseItem1a, databaseItem2 );
		assertNotEquals( databaseItem1b, databaseItem2 );
		assertNotEquals( databaseItem2, databaseItem3 );
		assertNotEquals( databaseItem1a, null );
		assertNotEquals( databaseItem1a, "string" );

		assertEquals( databaseItem1a.hashCode(), databaseItem1b.hashCode() );
		assertNotEquals( databaseItem1a.hashCode(), databaseItem2.hashCode() );
		assertNotEquals( databaseItem1b.hashCode(), databaseItem2.hashCode() );
		assertNotEquals( databaseItem2.hashCode(), databaseItem3.hashCode() );
	}

	@Test
	public void testGetDatabaseItem()
	{
		DatabaseItem databaseItem;

		databaseItem = DatabaseItem.getDatabaseItem( null );
		assertTrue( databaseItem.getGameName().isEmpty() );
		assertTrue( databaseItem.getDatabase().isEmpty() );

		databaseItem = DatabaseItem.getDatabaseItem( "some-string" );
		assertTrue( databaseItem.getGameName().isEmpty() );
		assertTrue( databaseItem.getDatabase().isEmpty() );

		databaseItem = DatabaseItem.getDatabaseItem( "game[" );
		assertTrue( databaseItem.getGameName().isEmpty() );
		assertTrue( databaseItem.getDatabase().isEmpty() );

		databaseItem = DatabaseItem.getDatabaseItem( "database]" );
		assertTrue( databaseItem.getGameName().isEmpty() );
		assertTrue( databaseItem.getDatabase().isEmpty() );

		databaseItem = DatabaseItem.getDatabaseItem( "][" );
		assertTrue( databaseItem.getGameName().isEmpty() );
		assertTrue( databaseItem.getDatabase().isEmpty() );

		databaseItem = DatabaseItem.getDatabaseItem( "[game" );
		assertTrue( databaseItem.getGameName().isEmpty() );
		assertTrue( databaseItem.getDatabase().isEmpty() );

		databaseItem = DatabaseItem.getDatabaseItem( "]database" );
		assertTrue( databaseItem.getGameName().isEmpty() );
		assertTrue( databaseItem.getDatabase().isEmpty() );

		databaseItem = DatabaseItem.getDatabaseItem( "gamename[]" );
		assertEquals( "gamename", databaseItem.getGameName() );
		assertTrue( databaseItem.getDatabase().isEmpty() );

		databaseItem = DatabaseItem.getDatabaseItem( "[database]" );
		assertTrue( databaseItem.getGameName().isEmpty() );
		assertEquals( "database", databaseItem.getDatabase() );

		databaseItem = DatabaseItem.getDatabaseItem( "gamename[database]" );
		assertEquals( "gamename", databaseItem.getGameName() );
		assertEquals( "database", databaseItem.getDatabase() );
	}

	@Test
	public void testGetDatabaseItemDisplay()
	{
		DatabaseItem databaseItem = new DatabaseItem( "gamename", "database" );

		assertEquals( "gamename [database]", databaseItem.toString() );
	}
}