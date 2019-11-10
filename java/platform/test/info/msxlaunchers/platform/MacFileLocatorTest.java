package info.msxlaunchers.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class MacFileLocatorTest
{
	@Mock ArgumentsBuilder argumentsBuilder;

	@Test
	public void testConstructor()
	{
		new MacFileLocator( argumentsBuilder );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg()
	{
		new MacFileLocator( null );
	}

	@Test
	public void testLocateFile() throws IOException
	{
		MacFileLocator fileLocator = new MacFileLocator( argumentsBuilder );

		List<String> args = new ArrayList<String>();

		//this test works on Windows so just bring up File Explorer
		args.add( "explorer.exe" );

		when( argumentsBuilder.getArgumentList() ).thenReturn( args );

		Process fileManager = fileLocator.locateFile( "c:\\a\\b" );
		fileManager.destroy();

		verify( argumentsBuilder, times( 1 ) ).append( eq( "open" ) );
		verify( argumentsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-R" ), eq( "c:\\a\\b" ) );
	}
}
