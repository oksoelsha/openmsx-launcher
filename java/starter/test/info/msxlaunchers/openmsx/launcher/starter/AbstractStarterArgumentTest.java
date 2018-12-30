package info.msxlaunchers.openmsx.launcher.starter;

import static org.mockito.Mockito.mock;

import org.junit.Before;

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.platform.ArgumentsBuilder;

public abstract class AbstractStarterArgumentTest
{
	protected Settings settings;
	protected ArgumentsBuilder argsBuilder;

	@Before
	public void setup()
	{
		settings = new Settings( "/openMSX dir/",
				"/openMSX/share/machines",
				null,
				null,
				null,
				false,
				false );

		argsBuilder = mock( ArgumentsBuilder.class );
	}
}
