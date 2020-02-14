package info.msxlaunchers.openmsx.launcher.data.game;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RelatedGameTest
{
	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null()
	{
		new RelatedGame( null, "company", "1990", 0 );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		RelatedGame relatedGame1a = new RelatedGame( "gameName1", "company1", "1982", 1 );
		RelatedGame relatedGame1b = new RelatedGame( "gameName1", "company2", "1983", 1 );
		RelatedGame relatedGame2 = new RelatedGame( "gameName1", "company2", "1987", 2 );
		RelatedGame relatedGame3 = new RelatedGame( "gameName2", "company1", "1989", 3 );

		assertEquals( relatedGame1a, relatedGame1a );
		assertEquals( relatedGame1a, relatedGame1b );
		assertNotEquals( relatedGame1a, relatedGame2 );
		assertNotEquals( relatedGame1b, relatedGame3 );
		assertNotEquals( relatedGame1a, null );
		assertNotEquals( relatedGame1a, "string" );

		assertEquals( relatedGame1a.hashCode(), relatedGame1b.hashCode() );
		assertNotEquals( relatedGame1a.hashCode(), relatedGame2.hashCode() );
		assertNotEquals( relatedGame1a.hashCode(), relatedGame3.hashCode() );
	}

	@Test
	public void testGetters()
	{
		RelatedGame relatedGame = new RelatedGame( "gameName", "company", "1993", 1 );

		assertEquals( "gameName", relatedGame.getGameName() );
		assertEquals( "company", relatedGame.getCompany() );
		assertEquals( "1993", relatedGame.getYear() );
		assertEquals( 1, relatedGame.getMSXGenId() );
	}
}