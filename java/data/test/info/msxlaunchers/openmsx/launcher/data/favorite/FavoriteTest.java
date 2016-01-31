package info.msxlaunchers.openmsx.launcher.data.favorite;

import info.msxlaunchers.openmsx.launcher.data.game.Game;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class FavoriteTest
{
	private Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).build();
	private String database = "database";

	@Test
	public void testFavorite()
	{
		Favorite favorite = new Favorite( game.getName(), database );

		assertEquals( game.getName(), favorite.getGameName() );
		assertEquals( database, favorite.getDatabase() );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testConstructorArg1Null()
	{
		new Favorite( null, database );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testConstructorArg2Null()
	{
		new Favorite( game.getName(), null );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		Favorite favorite1a = new Favorite( "name1", "database1" );
		Favorite favorite1b = new Favorite( "name1", "database1" );
		Favorite favorite2 = new Favorite( "name1", "database2" );
		Favorite favorite3 = new Favorite( "name2", "database1" );

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