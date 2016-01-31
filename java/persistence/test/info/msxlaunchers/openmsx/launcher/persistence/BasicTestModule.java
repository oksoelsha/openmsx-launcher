package info.msxlaunchers.openmsx.launcher.persistence;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class BasicTestModule  extends AbstractModule
{
	private final String userDataDirectory;

	public BasicTestModule( String userDataDirectory )
	{
		this.userDataDirectory = userDataDirectory;
	}

	@Override
	protected void configure()
	{
		bind( String.class ).annotatedWith( Names.named( "UserDataDirectory" ) ).toInstance( userDataDirectory );
		bind( String.class ).annotatedWith( Names.named( "GenerationMSXURL" ) ).toInstance( "GENERATION_MSX_URL" );
	}
}
