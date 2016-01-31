package info.msxlaunchers.openmsx.launcher.data.game.constants;

import org.junit.Test;

public class MediumTest
{
	@Test
	public void testMediumValues()
	{
		//the following is to get complete code coverage even if there's no value in this test
		Medium.valueOf( Medium.ROM.toString() );
		Medium.valueOf( Medium.DISK.toString() );
		Medium.valueOf( Medium.TAPE.toString() );
		Medium.valueOf( Medium.HARDDISK.toString() );
		Medium.valueOf( Medium.LASERDISC.toString() );
		Medium.valueOf( Medium.SCRIPT.toString() );
	}
}
