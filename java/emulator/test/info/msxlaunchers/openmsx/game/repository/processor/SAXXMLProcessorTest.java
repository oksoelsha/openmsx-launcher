package info.msxlaunchers.openmsx.game.repository.processor;

import info.msxlaunchers.openmsx.game.repository.processor.SAXXMLProcessor;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SAXXMLProcessorTest
{
	private final File softwareXML = new File( getClass().getResource( "softwaredb.xml" ).getFile() );

	@Test
	public void testGetValidRepositoryInfo() throws IOException
	{
		SAXXMLProcessor saxXMLProcessor = new SAXXMLProcessor();

		Map<String,RepositoryGame> repositoryInfo = saxXMLProcessor.getRepositoryInfo( softwareXML );

		//check that something is in the map
		assertTrue( repositoryInfo.size() > 0 );
	}

	@Test( expected = IOException.class)
	public void testGetRepositoryInfoException() throws IOException
	{
		SAXXMLProcessor saxXMLProcessor = new SAXXMLProcessor();

		saxXMLProcessor.getRepositoryInfo( new File( "/non existent/softwaredb.xml" ) );
	}

	@Test
	public void testGetAllDumpCodes() throws IOException
	{
		SAXXMLProcessor saxXMLProcessor = new SAXXMLProcessor();
		Set<String> allDumpCodes;

		//this software, Final Justice, has a total of 4 dumps
		allDumpCodes = saxXMLProcessor.getDumpCodes( softwareXML, "9bca89c71c033bb9a85ee30cf75960ec839c0462" );

		//check that there are three other dumps
		assertTrue( allDumpCodes.size() == 4 );

		//this software, Flappy Limited 85, has 1 dump
		allDumpCodes = saxXMLProcessor.getDumpCodes( softwareXML, "46f3954d7f92f5d00f45b82fdde543c28a4b53c9" );

		//check that there are no other dumps
		assertTrue( allDumpCodes.size() == 1 );

		//check that a non-existent-code returns no other dumps
		allDumpCodes = saxXMLProcessor.getDumpCodes( softwareXML, "non-existent-code" );

		//check that there are no other dumps
		assertTrue( allDumpCodes.size() == 0 );
	}

	@Test( expected = IOException.class)
	public void testGetAllDumpCodesException() throws IOException
	{
		SAXXMLProcessor saxXMLProcessor = new SAXXMLProcessor();

		saxXMLProcessor.getDumpCodes( new File( "/non existent/softwaredb.xml" ), "code" );
	}

	@Test
	public void testGetGameInfo() throws IOException
	{
		SAXXMLProcessor saxXMLProcessor = new SAXXMLProcessor();
		RepositoryGame repositoryGame;

		//Game: F15 Strike Eagle
		repositoryGame = saxXMLProcessor.getGameInfo( softwareXML, "39bc111c953bdf25683db7ff4c0bad79b5c49958" );

		assertEquals( repositoryGame.getTitle(), "F15 Strike Eagle" );

		//Game: Final Justice
		repositoryGame = saxXMLProcessor.getGameInfo( softwareXML, "9bca89c71c033bb9a85ee30cf75960ec839c0462" );

		assertEquals( repositoryGame.getTitle(), "Final Justice" );
		assertEquals( repositoryGame.getCompany(), "Compile" );
		assertEquals( repositoryGame.getYear(), "1985" );
		assertEquals( repositoryGame.getCountry(), "JP" );
		assertEquals( repositoryGame.isOriginal(), true );
		assertEquals( repositoryGame.getOriginalText(), "GoodMSX" );
		assertEquals( repositoryGame.getMapper(), RepositoryGame.MIRRORED_ROM );
		assertEquals( repositoryGame.getStart(), null );
		assertEquals( repositoryGame.getRemark(), null );

		//Game: Final Race
		repositoryGame = saxXMLProcessor.getGameInfo( softwareXML, "6ca349d1ba2fc90c17a57fa01a337fdea7acfd9d" );

		assertEquals( repositoryGame.getTitle(), "Final Race" );
		assertEquals( repositoryGame.getCompany(), "Karoshi Corporation" );
		assertEquals( repositoryGame.getYear(), "2005" );
		assertEquals( repositoryGame.getCountry(), "ES" );
		assertEquals( repositoryGame.isOriginal(), true );
		assertEquals( repositoryGame.getOriginalText(), "Author" );
		assertEquals( repositoryGame.getMapper(), RepositoryGame.MIRRORED_ROM );
		assertEquals( repositoryGame.getStart(), null );
		assertEquals( repositoryGame.getRemark(), "unfinished game" );

		//Game: Hardball
		repositoryGame = saxXMLProcessor.getGameInfo( softwareXML, "4ee3e785e6949bd535804a32c6a1a31eeef30031" );

		assertEquals( repositoryGame.getTitle(), "Hardball" );
		assertEquals( repositoryGame.getCompany(), "Sony" );
		assertEquals( repositoryGame.getYear(), "1987" );
		assertEquals( repositoryGame.getCountry(), "JP" );
		assertEquals( repositoryGame.isOriginal(), false );
		assertEquals( repositoryGame.getOriginalText(), null );
		assertEquals( repositoryGame.getMapper(), "Konami" );
		assertEquals( repositoryGame.getStart(), null );
		assertEquals( repositoryGame.getRemark(), null );

		//Game: Hose Diogo Martinez: The Bussas Quest
		repositoryGame = saxXMLProcessor.getGameInfo( softwareXML, "21e44cc5957b65b24c34d2c42b39879cacd82ff9" );

		assertEquals( repositoryGame.getTitle(), "Hose Diogo Martinez: The Bussas Quest" );
		assertEquals( repositoryGame.getCompany(), "Muffie" );
		assertEquals( repositoryGame.getYear(), "2009" );
		assertEquals( repositoryGame.getCountry(), "BR" );
		assertEquals( repositoryGame.isOriginal(), true );
		assertEquals( repositoryGame.getOriginalText(), "Author" );
		assertEquals( repositoryGame.getMapper(), "Normal" );
		assertEquals( repositoryGame.getStart(), "0x8000" );
		assertEquals( repositoryGame.getRemark(), "MSX-DEV08" );

		//Game: Rice 2
		repositoryGame = saxXMLProcessor.getGameInfo( softwareXML, "04a564958ea71653f87a8aa51b84aa567c73a8bd" );

		assertEquals( repositoryGame.getTitle(), "Rice 2" );
		assertEquals( repositoryGame.getCompany(), "Konami" );
		assertEquals( repositoryGame.getYear(), "1989" );
		assertEquals( repositoryGame.getCountry(), "JP" );
		assertEquals( repositoryGame.isOriginal(), false );
		assertEquals( repositoryGame.getOriginalText(), null );
		assertEquals( repositoryGame.getMapper(), "scc+" );
		assertEquals( repositoryGame.getStart(), null );
		assertEquals( repositoryGame.getRemark(), "[The Snatcher RAM SCC+]" );

		//Game: Rick & Mick's Adventure
		repositoryGame = saxXMLProcessor.getGameInfo( softwareXML, "a57f8ec4f89ab84936f46821969de6bca9f46d8e" );

		assertEquals( repositoryGame.getTitle(), "Rick & Mick's Adventure" );
		assertEquals( repositoryGame.getCompany(), "Humming Bird Soft" );
		assertEquals( repositoryGame.getYear(), "1987" );
		assertEquals( repositoryGame.getCountry(), "JP" );
		assertEquals( repositoryGame.isOriginal(), true );
		assertEquals( repositoryGame.getOriginalText(), "GoodMSX" );
		assertEquals( repositoryGame.getMapper(), "ASCII8" );
		assertEquals( repositoryGame.getStart(), null );
		assertEquals( repositoryGame.getRemark(), null );

		//Game: Robots
		repositoryGame = saxXMLProcessor.getGameInfo( softwareXML, "c1577863c6ac7aab87cc0c2d64cc0577e2e5e360" );

		assertEquals( repositoryGame.getTitle(), "Robots" );
		assertEquals( repositoryGame.getCompany(), "Michel d'Alger" );
		assertEquals( repositoryGame.getYear(), "2011" );
		assertEquals( repositoryGame.getCountry(), "EU" );
		assertEquals( repositoryGame.isOriginal(), true );
		assertEquals( repositoryGame.getOriginalText(), "Author" );
		assertEquals( repositoryGame.getMapper(), RepositoryGame.MIRRORED_ROM );
		assertEquals( repositoryGame.getStart(), null );
		assertEquals( repositoryGame.getRemark(), "MSXDev11 Entry" );
	}
}
