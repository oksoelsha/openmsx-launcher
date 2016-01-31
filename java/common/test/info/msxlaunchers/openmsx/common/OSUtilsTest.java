package info.msxlaunchers.openmsx.common;

import info.msxlaunchers.openmsx.common.OSUtils;

import java.io.IOException;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OSUtilsTest
{
	@Test
	public void test_whenInstantiateClass_thenGetAnInstance()
	{
		//this is not a useful test. It's just meant to get code coverage
		new OSUtils();
	}

	@Test
	public void testIsPlatformWindows() throws IOException
	{
		//this will only work on Windows

		assertTrue( OSUtils.isWindows() );
		assertFalse( OSUtils.isUnixFamily() );
		assertFalse( OSUtils.isLinux() );
		assertFalse( OSUtils.isMac() );
		assertFalse( OSUtils.isBSD() );
	}
}
