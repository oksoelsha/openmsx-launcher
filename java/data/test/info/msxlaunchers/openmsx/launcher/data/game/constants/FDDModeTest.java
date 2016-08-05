package info.msxlaunchers.openmsx.launcher.data.game.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FDDModeTest
{
	@Test
	public void testFDDModeValues()
	{
		FDDMode[] fddModes = FDDMode.values();

		for( FDDMode fddMode: fddModes )
		{
			assertEquals( FDDMode.fromValue( fddMode.getValue() ), fddMode );
		}
	}

	@Test
	public void testFromValueBoundaries()
	{
		assertEquals( FDDMode.fromValue( -1 ), FDDMode.ENABLE_BOTH );
		assertEquals( FDDMode.fromValue( 0 ), FDDMode.ENABLE_BOTH );
		assertEquals( FDDMode.fromValue( 1 ), FDDMode.DISABLE_SECOND );
		assertEquals( FDDMode.fromValue( 2 ), FDDMode.DISABLE_BOTH );
		assertEquals( FDDMode.fromValue( 3 ), FDDMode.ENABLE_BOTH );
	}
}
