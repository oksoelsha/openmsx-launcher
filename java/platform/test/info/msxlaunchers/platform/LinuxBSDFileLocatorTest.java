package info.msxlaunchers.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class LinuxBSDFileLocatorTest
{
	@Mock ArgumentsBuilder argumentsBuilder;

	@Test
	public void testConstructor()
	{
		new LinuxBSDFileLocator( argumentsBuilder );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg()
	{
		new LinuxBSDFileLocator( null );
	}

	@Test
	public void testLocateFile() throws IOException
	{
		LinuxBSDFileLocator fileLocator = new LinuxBSDFileLocator( argumentsBuilder );

		List<String> args = new ArrayList<String>();

		//this test works on Windows so just bring up File Explorer
		args.add( "explorer.exe" );

		when( argumentsBuilder.getArgumentList() ).thenReturn( args );

		Process fileManager = fileLocator.locateFile( "c:\\a\\b" );
		fileManager.destroy();

		verify( argumentsBuilder, times( 1 ) ).append( eq( "nautilus" ) );
		verify( argumentsBuilder, times( 1 ) ).appendIfValueDefined( eq( "--browser" ), eq( "c:\\a" ) );
	}
}
