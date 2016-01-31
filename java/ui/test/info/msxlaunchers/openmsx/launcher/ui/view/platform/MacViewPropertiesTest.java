package info.msxlaunchers.openmsx.launcher.ui.view.platform;

import info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;

public class MacViewPropertiesTest
{
	@Test
	public void testConstructor()
	{
		new MacViewProperties( mock( MainPresenter.class ) );
	}

	@Test( expected = NullPointerException.class )
	public void test_WhenCreatingMacViewPropertiesWithNullArg1_ThenThrowNullPointerException()
	{
		new MacViewProperties( null );
	}

	@Test
	public void testReturnValues()
	{
		MacViewProperties viewProperties = new MacViewProperties( mock( MainPresenter.class ) );

		assertEquals( "/Applications", viewProperties.getSuggestedOpenMSXPath() );
		assertTrue( viewProperties.isMachinesFolderInsideOpenMSX() );
		assertEquals( "openmsx.app/share/machines", viewProperties.getOpenMSXMachinesPath() );
	}
}
