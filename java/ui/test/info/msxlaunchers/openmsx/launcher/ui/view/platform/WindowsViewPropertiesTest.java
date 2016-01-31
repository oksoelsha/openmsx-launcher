package info.msxlaunchers.openmsx.launcher.ui.view.platform;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WindowsViewPropertiesTest
{
	@Test
	public void testReturnValues()
	{
		WindowsViewProperties viewProperties = new WindowsViewProperties();

		String openMSXPath = "C:\\Program Files\\openMSX";

		File path = new File(openMSXPath);

		if(path.exists())
		{
			assertEquals( openMSXPath, viewProperties.getSuggestedOpenMSXPath() );
		}
		else
		{
			assertNull( viewProperties.getSuggestedOpenMSXPath() );
		}

		assertTrue( viewProperties.isMachinesFolderInsideOpenMSX() );
		assertEquals( "share/machines", viewProperties.getOpenMSXMachinesPath() );
	}
}
