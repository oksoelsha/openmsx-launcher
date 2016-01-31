package info.msxlaunchers.openmsx.launcher.ui.view.platform;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class OtherPlatformViewPropertiesTest
{
	@Test
	public void testReturnValues()
	{
		OtherPlatformViewProperties viewProperties = new OtherPlatformViewProperties();

		assertNull( viewProperties.getSuggestedOpenMSXPath() );
		assertFalse( viewProperties.isMachinesFolderInsideOpenMSX() );
		assertNull( viewProperties.getOpenMSXMachinesPath() );
	}
}
