package info.msxlaunchers.openmsx.launcher.data.extra;

import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExtraDataTest
{
	@Test
	public void testAll()
	{
		//create extra data where all fields are set
		ExtraData extraData1 = new ExtraData( 20, 15, 255, 25, 35, "suffix" );

		assertEquals( extraData1.getMSXGenerationsID(), 20 );

		//test that all generations are set
		assertTrue( extraData1.isMSX() );
		assertTrue( extraData1.isMSX2() );
		assertTrue( extraData1.isMSX2Plus() );
		assertTrue( extraData1.isTurboR() );

		//test that all sound chips are set
		assertTrue( extraData1.isPSG() );
		assertTrue( extraData1.isSCC() );
		assertTrue( extraData1.isSCCI() );
		assertTrue( extraData1.isPCM() );
		assertTrue( extraData1.isMSXMUSIC() );
		assertTrue( extraData1.isMSXAUDIO() );
		assertTrue( extraData1.isMoonsound() );
		assertTrue( extraData1.isMIDI() );

		assertEquals( extraData1.getGenre1(), 25 );
		assertEquals( extraData1.getGenre2(), 35 );

		assertEquals( extraData1.getSuffix(), "suffix" );

		//create extra data where no generations or sound chips are set
		ExtraData extraData2 = new ExtraData( 20, 0, 0, 25, 35, null );

		//test that all generations are not set
		assertFalse( extraData2.isMSX() );
		assertFalse( extraData2.isMSX2() );
		assertFalse( extraData2.isMSX2Plus() );
		assertFalse( extraData2.isTurboR() );

		//test that all sound chips are not set
		assertFalse( extraData2.isPSG() );
		assertFalse( extraData2.isSCC() );
		assertFalse( extraData2.isSCCI() );
		assertFalse( extraData2.isPCM() );
		assertFalse( extraData2.isMSXMUSIC() );
		assertFalse( extraData2.isMSXAUDIO() );
		assertFalse( extraData2.isMoonsound() );
		assertFalse( extraData2.isMIDI() );
	}

}
