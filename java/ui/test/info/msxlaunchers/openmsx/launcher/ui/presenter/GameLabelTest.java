package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;
import info.msxlaunchers.openmsx.launcher.ui.presenter.GameLabel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class GameLabelTest
{
	@Test( expected = NullPointerException.class )
	public void testNullArg1CannotBeNull()
	{
		new GameLabel( null, "company", "year", 1, Medium.ROM );
	}

	@Test
	public void testNullArg2CanBeNull()
	{
		new GameLabel( "name", null, "year", 1, Medium.ROM );
	}

	@Test
	public void testNullArg3CanBeNull()
	{
		new GameLabel( "name", "company", null, 1, Medium.ROM );
	}

	@Test
	public void testNullArg4CanBeZero()
	{
		new GameLabel( "name", "company", "year", 0, Medium.ROM );
	}

	@Test( expected = NullPointerException.class )
	public void testNullArg5CannotBeNull()
	{
		new GameLabel( "name", "company", "year", 1, null );
	}

	@Test
	public void testGetters()
	{
		GameLabel gameLabel = new GameLabel( "name", "company", "year", 1, Medium.ROM );

		assertEquals( "name", gameLabel.getName() );
		assertEquals( "company", gameLabel.getCommany() );
		assertEquals( "year", gameLabel.getYear() );
		assertEquals( 1, gameLabel.getSize() );
		assertEquals( Medium.ROM, gameLabel.getMedium() );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		GameLabel gameLabel1 = new GameLabel( "name", "company1", "year1", 1, Medium.ROM );
		GameLabel gameLabel2 = new GameLabel( "Name", "company2", "year2", 2,  Medium.DISK );
		GameLabel gameLabel3 = new GameLabel( "Name", "company3", "year3", 3,  Medium.DISK );

		//equals
		assertEquals( gameLabel1, gameLabel1 );
		assertEquals( gameLabel2, gameLabel3 );

		assertNotEquals( gameLabel1, gameLabel2 );
		assertNotEquals( gameLabel1, null );
		assertNotEquals( gameLabel1, this );

		//hashcode
		assertEquals( gameLabel1.hashCode(), gameLabel1.hashCode() );
		assertEquals( gameLabel2.hashCode(), gameLabel3.hashCode() );

		assertNotEquals( gameLabel1.hashCode(), gameLabel2.hashCode() );
		assertNotEquals( gameLabel1.hashCode(), hashCode() );
	}

	@Test
	public void testComparison()
	{
		GameLabel gameLabel1 = new GameLabel( "name", "company1", "year1", 1, Medium.ROM );
		GameLabel gameLabel2 = new GameLabel( "Name", "company2", "year2", 2, Medium.DISK );
		GameLabel gameLabel3 = new GameLabel( "obc", "company3", "year3", 3, Medium.HARDDISK );		
		GameLabel gameLabel4 = new GameLabel( "XYZ", "company4", "year4", 4, Medium.LASERDISC );		
		GameLabel gameLabel5 = new GameLabel( "name", "company5", "year5", 5, Medium.TAPE );

		assertEquals( 0, gameLabel1.compareTo( gameLabel5 ) );
		assertTrue( gameLabel1.compareTo( gameLabel2 ) > 0 );
		assertTrue( gameLabel1.compareTo( gameLabel3 ) < 0 );
		assertTrue( gameLabel1.compareTo( gameLabel4 ) > 0 );
	}
}
