package info.msxlaunchers.openmsx.launcher.ui.view.platform;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BSDViewPropertiesTest
{
	@Test
	public void testReturnValues()
	{
		BSDViewProperties viewProperties = new BSDViewProperties();

		assertEquals( "/usr/local/bin", viewProperties.getSuggestedOpenMSXPath() );
		assertFalse( viewProperties.isMachinesFolderInsideOpenMSX() );
		assertEquals( "/usr/local/share/openmsx/machines", viewProperties.getOpenMSXMachinesPath() );
	}
}
