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
public class WindowsFileLocatorTest
{
	@Mock ArgumentsBuilder argumentsBuilder;

	@Test
	public void testConstructor()
	{
		new WindowsFileLocator( argumentsBuilder );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg()
	{
		new WindowsFileLocator( null );
	}

	@Test
	public void testLocateFile() throws IOException
	{
		WindowsFileLocator fileLocator = new WindowsFileLocator( argumentsBuilder );

		List<String> args = new ArrayList<String>();

		//this test works on Windows so just bring up File Explorer
		args.add( "explorer.exe" );

		when( argumentsBuilder.getArgumentList() ).thenReturn( args );

		Process fileManager = fileLocator.locateFile( "c:\\a\\b" );
		fileManager.destroy();

		verify( argumentsBuilder, times( 1 ) ).append( eq( "explorer.exe" ) );
		verify( argumentsBuilder, times( 1 ) ).appendIfValueDefined( eq( "/select," ), eq( "c:\\a\\b" ) );
	}
}
