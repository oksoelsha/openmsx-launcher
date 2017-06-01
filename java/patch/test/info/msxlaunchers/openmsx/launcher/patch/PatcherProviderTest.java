package info.msxlaunchers.openmsx.launcher.patch;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class PatcherProviderTest
{
	@Test
	public void givenPatchMethod_whenGet_thenReturnCorrectPatcherInstance()
	{
		Patcher ipsPatcher = Mockito.mock( Patcher.class );
		Patcher upsPatcher = Mockito.mock( Patcher.class );

		PatcherProvider provider = new PatcherProvider( ipsPatcher, upsPatcher );

		Assert.assertSame( ipsPatcher, provider.get( PatchMethod.IPS ) );
		Assert.assertSame( upsPatcher, provider.get( PatchMethod.UPS ) );
	}
}
