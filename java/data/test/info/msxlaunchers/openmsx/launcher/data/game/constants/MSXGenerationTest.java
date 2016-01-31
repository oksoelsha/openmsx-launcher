package info.msxlaunchers.openmsx.launcher.data.game.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MSXGenerationTest
{
	@Test
	public void testGenerationValues()
	{
		//the following is to get complete code coverage even if there's no value in this test
		MSXGeneration.valueOf( MSXGeneration.MSX.toString() );
		MSXGeneration.valueOf( MSXGeneration.MSX2.toString() );
		MSXGeneration.valueOf( MSXGeneration.MSX2Plus.toString() );
		MSXGeneration.valueOf( MSXGeneration.TURBO_R.toString() );
	}

	@Test
	public void testTotalGenerationsCount()
	{
		assertEquals( MSXGeneration.values().length, 4 );
	}

	@Test
	public void testGenerationsDisplayName()
	{
		assertEquals( MSXGeneration.MSX.getDisplayName(), "MSX" );
		assertEquals( MSXGeneration.MSX2.getDisplayName(), "MSX2" );
		assertEquals( MSXGeneration.MSX2Plus.getDisplayName(), "MSX2+" );
		assertEquals( MSXGeneration.TURBO_R.getDisplayName(), "Turbo-R" );
	}
}
