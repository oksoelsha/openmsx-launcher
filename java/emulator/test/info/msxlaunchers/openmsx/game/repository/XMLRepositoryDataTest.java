package info.msxlaunchers.openmsx.game.repository;

import info.msxlaunchers.openmsx.game.repository.XMLRepositoryData;
import info.msxlaunchers.openmsx.game.repository.processor.XMLProcessor;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith( MockitoJUnitRunner.class )
public class XMLRepositoryDataTest
{
	@Mock XMLProcessor xmlProcessor;
	@Mock XMLFileGetter xmlFileGetter1;
	@Mock XMLFileGetter xmlFileGetter2;

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg1()
	{
		new XMLRepositoryData( null, Collections.emptySet() );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg2()
	{
		new XMLRepositoryData( xmlProcessor, null );
	}

	@Test
	public void givenNoXMLFiles_whenGetRepositoryInfo_thenReturnNull() throws IOException
	{
		Set<XMLFileGetter> xmlFileGetters = Collections.singleton( xmlFileGetter1 );

		XMLRepositoryData repositoryData = new XMLRepositoryData( xmlProcessor, xmlFileGetters );

		Assert.assertNull( repositoryData.getRepositoryInfo() );
	}

	@Test
	public void givenTwoExistingXMLFilesAndOneNonExistingXMLFile_whenGetRepositoryInfo_thenMapOfAllXMLDataInTwoFiles() throws IOException
	{
		XMLFileGetter xmlFileGetter3 = Mockito.mock( XMLFileGetter.class );
		Set<XMLFileGetter> xmlFileGetters = Stream.of( xmlFileGetter1, xmlFileGetter2, xmlFileGetter3 ).collect( Collectors.toSet() );

		File xmlFile1 = File.createTempFile( "temp1", null );
		xmlFile1.deleteOnExit();
		File xmlFile2 = File.createTempFile( "temp2", null );
		xmlFile2.deleteOnExit();
		File xmlFile3 = new File( "non-existing-file" );

		Mockito.when( xmlFileGetter1.get() ).thenReturn( xmlFile1 );
		Mockito.when( xmlFileGetter2.get() ).thenReturn( xmlFile2 );
		Mockito.when( xmlFileGetter3.get() ).thenReturn( xmlFile3 );

		XMLRepositoryData repositoryData = new XMLRepositoryData( xmlProcessor, xmlFileGetters );
		Assert.assertNotNull( repositoryData.getRepositoryInfo() );

		Mockito.verify( xmlProcessor, Mockito.times( 1 ) ).getRepositoryInfo( xmlFile1 );
		Mockito.verify( xmlProcessor, Mockito.times( 1 ) ).getRepositoryInfo( xmlFile2 );
		Mockito.verify( xmlProcessor, Mockito.never() ).getRepositoryInfo( xmlFile3 );
	}

	@Test
	public void givenTwoExistingXMLFilesWithNoMatchingCode_whenGetDumps_thenReturnEmptySet() throws IOException
	{
		Set<XMLFileGetter> xmlFileGetters = Stream.of( xmlFileGetter1, xmlFileGetter2 ).collect( Collectors.toSet() );

		File xmlFile1 = new File( "file1" );
		File xmlFile2 = new File( "file2" );

		Mockito.when( xmlFileGetter1.get() ).thenReturn( xmlFile1 );
		Mockito.when( xmlFileGetter2.get() ).thenReturn( xmlFile2 );

		Mockito.when( xmlProcessor.getDumpCodes( xmlFile1, "code" ) ).thenReturn( Collections.emptySet() );

		XMLRepositoryData repositoryData = new XMLRepositoryData( xmlProcessor, xmlFileGetters );

		Assert.assertTrue( repositoryData.getDumpCodes( "code" ).size() == 0 );
	}

	
	@Test
	public void givenTwoExistingXMLFilesWithMatchingCode_whenGetDumps_thenReturnDumpCodesFromFirstXML() throws IOException
	{
		Set<XMLFileGetter> xmlFileGetters = Stream.of( xmlFileGetter1, xmlFileGetter2 ).collect( Collectors.toSet() );

		File xmlFile1 = new File( "file1" );

		Mockito.when( xmlFileGetter1.get() ).thenReturn( xmlFile1 );

		Set<String> dumpCodes = Stream.of( "code1", "code2" ).collect( Collectors.toSet() );
		Mockito.when( xmlProcessor.getDumpCodes( xmlFile1, "code" ) ).thenReturn( dumpCodes );

		XMLRepositoryData repositoryData = new XMLRepositoryData( xmlProcessor, xmlFileGetters );

		Assert.assertTrue( repositoryData.getDumpCodes( "code" ).size() == 2 );
	}

	@Test
	public void givenTwoExistingXMLFilesWithNoMatchingCode_whenGetGameInfo_thenReturnNull() throws IOException
	{
		Set<XMLFileGetter> xmlFileGetters = Stream.of( xmlFileGetter1, xmlFileGetter2 ).collect( Collectors.toSet() );

		File xmlFile1 = new File( "file1" );

		Mockito.when( xmlFileGetter1.get() ).thenReturn( xmlFile1 );

		XMLRepositoryData repositoryData = new XMLRepositoryData( xmlProcessor, xmlFileGetters );

		Assert.assertNull( repositoryData.getGameInfo( "code" ) );
	}

	@Test
	public void givenExistingXMLFilesWithMatchingCode_whenGetGameInfo_thenReturnGameRepository() throws IOException
	{
		Set<XMLFileGetter> xmlFileGetters = Stream.of( xmlFileGetter1, xmlFileGetter2 ).collect( Collectors.toSet() );

		File xmlFile = new File( "file" );

		Mockito.when( xmlFileGetter1.get() ).thenReturn( xmlFile );

		RepositoryGame repositoryGame = RepositoryGame.title( "title" ).system( "MSX" ).company( "company" ).year( "year" ).country( "country" ).build();
		Mockito.when( xmlProcessor.getGameInfo( xmlFile, "code" ) ).thenReturn( repositoryGame );

		XMLRepositoryData repositoryData = new XMLRepositoryData( xmlProcessor, xmlFileGetters );

		Assert.assertSame( repositoryGame, repositoryData.getGameInfo( "code" ) );
	}
}
