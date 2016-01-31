package info.msxlaunchers.platform;

import info.msxlaunchers.openmsx.common.OSUtils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ArgumentsBuilderProviderTest
{
	@Test
	public void test_WhenCallGet_ThenPerPlatformArgumentsBuilder()
	{
		ArgumentsBuilderProvider provider = new ArgumentsBuilderProvider();

		if( OSUtils.isWindows() )
		{
			assertTrue( provider.get() instanceof WindowsArgumentsBuilder );
		}
		else if( OSUtils.isLinux() )
		{
			assertTrue( provider.get() instanceof UnixFamilyArgumentsBuilder );
		}
		else if( OSUtils.isMac() )
		{
			assertTrue( provider.get() instanceof UnixFamilyArgumentsBuilder );
		}
		else if( OSUtils.isBSD() )
		{
			assertTrue( provider.get() instanceof UnixFamilyArgumentsBuilder );
		}
	}
}
