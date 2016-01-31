package info.msxlaunchers.openmsx.launcher.persistence.game;

import info.msxlaunchers.openmsx.launcher.extra.ExtraDataModule;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import static org.junit.Assert.assertTrue;

public class GamePersisterModuleTest
{
	@Test
	public void test_WhenRequestingInstances_ThenAllInstancesAreTheSame()
	{
		Injector injector = Guice.createInjector( new ConstantsModule(), new ExtraDataModule(), new GamePersisterModule() );

		GamePersister instance1 = injector.getInstance( GamePersister.class );
		GamePersister instance2 = injector.getInstance( GamePersister.class );

		assertTrue( instance1 == instance2 );
	}

	private class ConstantsModule extends AbstractModule
	{
		@Override 
		protected void configure()
		{
			bind( String.class ).annotatedWith( Names.named( "EmbeddedDatabaseFullPath" ) ).toInstance( "embeddedDatabaseFullPath" );
			bind( String.class ).annotatedWith( Names.named( "LauncherDataDirectory" ) ).toInstance( "launcherDataDirectory" );
			bind( String.class ).annotatedWith( Names.named( "GenerationMSXURL" ) ).toInstance( "generationMSXURL" );
		}
	}
}