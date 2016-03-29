package info.msxlaunchers.openmsx.launcher.data.game;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.Game;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
		DatabaseItem favorite1a = new DatabaseItem( "name1", "database1" );
		DatabaseItem favorite1b = new DatabaseItem( "name1", "database1" );
		DatabaseItem favorite2 = new DatabaseItem( "name1", "database2" );
		DatabaseItem favorite3 = new DatabaseItem( "name2", "database1" );

		assertEquals( favorite1a, favorite1a );
		assertEquals( favorite1a, favorite1b );
		assertNotEquals( favorite1a, favorite2 );
		assertNotEquals( favorite1b, favorite2 );
		assertNotEquals( favorite2, favorite3 );
		assertNotEquals( favorite1a, null );
		assertNotEquals( favorite1a, "string" );

		assertEquals( favorite1a.hashCode(), favorite1b.hashCode() );
		assertNotEquals( favorite1a.hashCode(), favorite2.hashCode() );
		assertNotEquals( favorite1b.hashCode(), favorite2.hashCode() );
		assertNotEquals( favorite2.hashCode(), favorite3.hashCode() );
	}
}