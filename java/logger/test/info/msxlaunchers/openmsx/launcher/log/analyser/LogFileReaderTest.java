package info.msxlaunchers.openmsx.launcher.log.analyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LogFileReaderTest
{
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test( expected = NullPointerException.class )
	public void givenNullDirectory_whenCreateLogReader_thenThrowException()
	{
		new LogFileReader( null );
	}

	@Test
	public void givenNonExistentLogFileDirectory_whenRead_thenReturnEmptyList()
	{
		LogFileReader logFileReader = new LogFileReader( "directory" );

		List<String> lines = logFileReader.read();

		assertTrue( lines.isEmpty() );
	}

	@Test
	public void givenOneCurrentLogFile_whenRead_thenReturnAllLines() throws IOException
	{
		populateLogFile( "message.log.0", "log line");

		LogFileReader logFileReader = new LogFileReader( tmpFolder.getRoot().toString() );

		List<String> lines = logFileReader.read();

		assertEquals( 50, lines.size() );
		for( int ix = 0; ix < 50; ix++)
		{
			assertEquals( "log line" + ix, lines.get( ix ) );
		}
	}

	@Test
	public void givenCurrentAndRotatedLogFiles_whenRead_thenReturnAllLines() throws IOException
	{
		populateLogFile( "message.log.0", "current log line");
		populateLogFile( "message.log", "rotated log line");

		LogFileReader logFileReader = new LogFileReader( tmpFolder.getRoot().toString() );

		List<String> lines = logFileReader.read();

		assertEquals( 100, lines.size() );
		for( int ix = 0; ix < 50; ix++)
		{
			assertEquals( "rotated log line" + ix, lines.get( ix ) );
		}
		for( int ix = 0; ix < 50; ix++)
		{
			assertEquals( "current log line" + ix, lines.get( ix + 50 ) );
		}
	}

	@Test( expected = UnsupportedOperationException.class )
	public void whenAddToListFromRead_thenThrowException()
	{
		LogFileReader logFileReader = new LogFileReader( "directory" );

		List<String> lines = logFileReader.read();

		lines.add( "should not be allowed" );
	}

	private void populateLogFile( String filename, String linePrefix ) throws IOException
	{
		PrintWriter writer = new PrintWriter( tmpFolder.newFile( filename ) );

		for( int ix = 0; ix < 50; ix++)
		{
			writer.println( linePrefix + ix );
		}

		writer.close();
	}
}
