package info.msxlaunchers.openmsx.launcher.persistence.filter;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import static org.junit.Assert.assertTrue;

public class FilterPersisterModuleTest
{
	@Test
	public void test_WhenRequestingInstances_ThenAllInstancesAreTheSame()
	{
		String currentDirectory = System.getProperty( "user.dir" );
		Injector injector = Guice.createInjector( new ConstantsModule( currentDirectory ), new FilterPersisterModule() );

		FilterPersister instance1 = injector.getInstance( FilterPersister.class );
		FilterPersister instance2 = injector.getInstance( FilterPersister.class );

		assertTrue( instance1 == instance2 );
	}

	private class ConstantsModule extends AbstractModule
	{
		private final String userDataDirectory;

		ConstantsModule( String userDataDirectory )
		{
			this.userDataDirectory = userDataDirectory;
		}

		@Override 
		protected void configure()
		{
			bind( String.class ).annotatedWith( Names.named( "UserDataDirectory" ) ).toInstance( userDataDirectory );
		}
	}
}