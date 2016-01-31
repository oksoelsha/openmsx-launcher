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
	public void testNullArg1()
	{
		new GameLabel( null, Medium.ROM );
	}

	@Test( expected = NullPointerException.class )
	public void testNullArg2()
	{
		new GameLabel( "name", null );
	}

	@Test
	public void testGetters()
	{
		GameLabel gameLabel = new GameLabel( "name", Medium.ROM );

		assertEquals( "name", gameLabel.getName() );
		assertEquals( Medium.ROM, gameLabel.getMedium() );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		GameLabel gameLabel1 = new GameLabel( "name", Medium.ROM );
		GameLabel gameLabel2 = new GameLabel( "Name", Medium.DISK );
		GameLabel gameLabel3 = new GameLabel( "Name", Medium.DISK );

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
		GameLabel gameLabel1 = new GameLabel( "name", Medium.ROM );
		GameLabel gameLabel2 = new GameLabel( "Name", Medium.DISK );
		GameLabel gameLabel3 = new GameLabel( "obc", Medium.HARDDISK );		
		GameLabel gameLabel4 = new GameLabel( "XYZ", Medium.LASERDISC );		
		GameLabel gameLabel5 = new GameLabel( "name", Medium.TAPE );

		assertEquals( 0, gameLabel1.compareTo( gameLabel5 ) );
		assertTrue( gameLabel1.compareTo( gameLabel2 ) > 0 );
		assertTrue( gameLabel1.compareTo( gameLabel3 ) < 0 );
		assertTrue( gameLabel1.compareTo( gameLabel4 ) > 0 );
	}
}
