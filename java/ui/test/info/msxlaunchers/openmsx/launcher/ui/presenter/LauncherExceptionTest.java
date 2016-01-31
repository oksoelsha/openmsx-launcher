package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherExceptionCode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LauncherExceptionTest
{
	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null()
	{
		new LauncherException( null, "something" );
	}

	@Test
	public void testNullAdditionalString()
	{
		LauncherException le = new LauncherException( LauncherExceptionCode.ERR_CANNOT_SAVE_SETTINGS );

		assertNull( le.getAdditionalString() );
	}

	@Test
	public void testExceptionWithAdditionalString()
	{
		LauncherException le = new LauncherException( LauncherExceptionCode.ERR_CANNOT_SAVE_SETTINGS, "something" );

		assertEquals( LauncherExceptionCode.ERR_CANNOT_SAVE_SETTINGS, le.getCode() );
		assertEquals( LauncherExceptionCode.ERR_CANNOT_SAVE_SETTINGS.toString(), le.getCodeAsString() );
		assertEquals( "something", le.getAdditionalString() );
	}
}
