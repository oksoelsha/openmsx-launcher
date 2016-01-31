package info.msxlaunchers.openmsx.launcher.ui.view.platform;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LinuxViewPropertiesTest
{
	@Test
	public void testReturnValues()
	{
		LinuxViewProperties viewProperties = new LinuxViewProperties();

		assertEquals( "/usr/bin", viewProperties.getSuggestedOpenMSXPath() );
		assertFalse( viewProperties.isMachinesFolderInsideOpenMSX() );
		assertEquals( "/usr/share/openmsx/machines", viewProperties.getOpenMSXMachinesPath() );
	}
}
