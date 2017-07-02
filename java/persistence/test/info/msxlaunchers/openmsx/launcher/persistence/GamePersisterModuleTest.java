package info.msxlaunchers.openmsx.launcher.persistence;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import static org.junit.Assert.assertSame;

public class GamePersisterModuleTest
{
	@Test
	public void test_WhenRequestingInstances_ThenAllInstancesAreTheSame()
	{
		Injector injector = Guice.createInjector(
				new BasicTestModule( "folder" ),
	    		new LauncherPersistenceModule()
				);

		LauncherPersistence instance1 = injector.getInstance( LauncherPersistence.class );
		LauncherPersistence instance2 = injector.getInstance( LauncherPersistence.class );

		assertSame( instance1, instance2 );
	}
}