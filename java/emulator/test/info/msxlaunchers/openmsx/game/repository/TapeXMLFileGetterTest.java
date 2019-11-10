package info.msxlaunchers.openmsx.game.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

@RunWith( MockitoJUnitRunner.class )
public class TapeXMLFileGetterTest
{
	@Test
	public void givenTapeXMLFileDirectory_whenGet_returnValidFileInstance() throws IOException
	{
		TapeXMLFileGetter tapeXmlFileGetter = new TapeXMLFileGetter( "/directory" );

		Assert.assertEquals( new File( "/directory", "msxcaswavdb.xml" ), tapeXmlFileGetter.get() );
	}
}
