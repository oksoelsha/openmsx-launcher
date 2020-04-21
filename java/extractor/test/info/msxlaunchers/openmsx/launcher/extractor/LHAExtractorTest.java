package info.msxlaunchers.openmsx.launcher.extractor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.common.ActionDecider;

@RunWith( MockitoJUnitRunner.class )
public class LHAExtractorTest
{
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	@Mock
	ActionDecider actionDecider;

	Path compressedFile;

	@Before
	public void setup() throws IOException, URISyntaxException
	{
		Path testDirectory = tmpFolder.newFolder().toPath();
		compressedFile = Paths.get( testDirectory.toString(), "test.lha" );

		//copy the test compressed file to the temp folder
		URI resourceCompressedFile = getClass().getResource( "resource/test.lha" ).toURI();
		Files.copy( Paths.get( resourceCompressedFile ), compressedFile );
	}

	@Test
	public void givenExtractMSXImagesOnlyOption_whenExtract_thenExtractMSXImagesOnly() throws ExtractionException
	{
		LHAExtractor extractor = new LHAExtractor();
		ExtractorData extractorData = extractor.extract( compressedFile.toString(), null, true, actionDecider );

		Assert.assertEquals( 8, extractorData.getTotalFiles() );
		Assert.assertEquals( 6, extractorData.getTotalMSXImages() );
		Assert.assertEquals( 6, extractorData.getTotalExtractedFiles() );

		assertFileExists( "rom1.rom" );
		assertFileExists( "rom2.rom" );
		assertFileExists( "disk1.dsk" );
		assertFileExists( "disk2.dsk" );
		assertFileExists( "tape1.cas" );
		assertFileExists( "tape2.wav" );
		assertFileDoesNotExist( "file1.bas" );
		assertFileDoesNotExist( "file2.ldr" );
	}

	@Test
	public void givenExtractAllFilesOption_whenExtract_thenExtractAll() throws ExtractionException
	{
		LHAExtractor extractor = new LHAExtractor();
		ExtractorData extractorData = extractor.extract( compressedFile.toString(), null, false, actionDecider );

		Assert.assertEquals( 8, extractorData.getTotalFiles() );
		Assert.assertEquals( 6, extractorData.getTotalMSXImages() );
		Assert.assertEquals( 8, extractorData.getTotalExtractedFiles() );

		assertFileExists( "rom1.rom" );
		assertFileExists( "rom2.rom" );
		assertFileExists( "disk1.dsk" );
		assertFileExists( "disk2.dsk" );
		assertFileExists( "tape1.cas" );
		assertFileExists( "tape2.wav" );
		assertFileExists( "file1.bas" );
		assertFileExists( "file2.ldr" );
	}

	@Test
	public void givenAllExtractedFilesExistAlready_whenExtractAndNoAll_thenDoNotExtract() throws ExtractionException
	{
		LHAExtractor extractor = new LHAExtractor();
		//add files to force duplicate detection
		ExtractorData extractorData = extractor.extract( compressedFile.toString(), null, false, actionDecider );

		Mockito.when( actionDecider.isNoAll() ).thenReturn( true );
		extractorData = extractor.extract( compressedFile.toString(), null, false, actionDecider );

		Assert.assertEquals( 8, extractorData.getTotalFiles() );
		Assert.assertEquals( 6, extractorData.getTotalMSXImages() );
		Assert.assertEquals( 0, extractorData.getTotalExtractedFiles() );
	}

	@Test
	public void givenAllExtractedFilesExistAlreadyAndMSXImagesOnlyOption_whenExtractAndYesAll_thenExtractAll() throws ExtractionException
	{
		LHAExtractor extractor = new LHAExtractor();
		//add files to force duplicate detection
		ExtractorData extractorData = extractor.extract( compressedFile.toString(), null, false, actionDecider );

		Mockito.when( actionDecider.isYesAll() ).thenReturn( true );
		extractorData = extractor.extract( compressedFile.toString(), null, true, actionDecider );

		Assert.assertEquals( 8, extractorData.getTotalFiles() );
		Assert.assertEquals( 6, extractorData.getTotalMSXImages() );
		Assert.assertEquals( 6, extractorData.getTotalExtractedFiles() );
	}

	@Test
	public void givenAllExtractedFilesExistAlready_whenExtractAndCancel_thenSkipExtract() throws ExtractionException
	{
		LHAExtractor extractor = new LHAExtractor();
		//add files to force duplicate detection
		ExtractorData extractorData = extractor.extract( compressedFile.toString(), null, false, actionDecider );


		Mockito.when( actionDecider.isCancel() ).thenReturn( true );
		extractorData = extractor.extract( compressedFile.toString(), null, false, actionDecider );

		Assert.assertEquals( 8, extractorData.getTotalFiles() );
		Assert.assertEquals( 6, extractorData.getTotalMSXImages() );
		Assert.assertEquals( 0, extractorData.getTotalExtractedFiles() );
	}

	@Test
	public void givenSomeExtractedFilesExistAlreadyAndMSXImagesOnlyOption_whenExtractAndNo_thenExtractNonExistent() throws ExtractionException, IOException
	{
		LHAExtractor extractor = new LHAExtractor();
		//add files to force duplicate detection
		ExtractorData extractorData = extractor.extract( compressedFile.toString(), null, false, actionDecider );
		//delete a couple MSX images
		Files.delete( Paths.get( compressedFile.getParent().toString(), "rom1.rom" ) );
		Files.delete( Paths.get( compressedFile.getParent().toString(), "disk2.dsk" ) );

		Mockito.when( actionDecider.isNo() ).thenReturn( true );
		extractorData = extractor.extract( compressedFile.toString(), null, true, actionDecider );

		Assert.assertEquals( 8, extractorData.getTotalFiles() );
		Assert.assertEquals( 6, extractorData.getTotalMSXImages() );
		Assert.assertEquals( 2, extractorData.getTotalExtractedFiles() );
	}

	@Test
	public void givenSomeExtractedFilesExistAlreadyAndMSXImagesOnlyOption_whenExtractAndYes_thenExtractAllMSXImages() throws ExtractionException, IOException
	{
		LHAExtractor extractor = new LHAExtractor();
		//add files to force duplicate detection
		ExtractorData extractorData = extractor.extract( compressedFile.toString(), null, false, actionDecider );
		//delete a couple MSX images
		Files.delete( Paths.get( compressedFile.getParent().toString(), "rom2.rom" ) );
		Files.delete( Paths.get( compressedFile.getParent().toString(), "tape2.wav" ) );

		Mockito.when( actionDecider.isYes() ).thenReturn( true );
		extractorData = extractor.extract( compressedFile.toString(), null, true, actionDecider );

		Assert.assertEquals( 8, extractorData.getTotalFiles() );
		Assert.assertEquals( 6, extractorData.getTotalMSXImages() );
		Assert.assertEquals( 6, extractorData.getTotalExtractedFiles() );
	}

	@Test( expected = ExtractionException.class )
	public void givenNonExsitentCompressedFile_whenExtract_thenThrowException() throws ExtractionException
	{
		LHAExtractor extractor = new LHAExtractor();

		try
		{
			extractor.extract( "non-existent-file", null, true, actionDecider );
		}
		catch( ExtractionException ee )
		{
			Assert.assertEquals( ExtractorExceptionIssue.COMPRESSED_FILE_NOT_FOUND, ee.getIssue() );
			throw ee;
		}
	}

	@Test( expected = ExtractionException.class )
	public void givenNonExsitentTargetDirectory_whenExtract_thenThrowException() throws ExtractionException
	{
		LHAExtractor extractor = new LHAExtractor();

		try
		{
			extractor.extract( compressedFile.toString(), "non-existent-directory", true, actionDecider );
		}
		catch( ExtractionException ee )
		{
			Assert.assertEquals( ExtractorExceptionIssue.IO, ee.getIssue() );
			throw ee;
		}
	}

	@Test
	public void givenDifferentDirectory_whenExtract_thenExtractIntoDifferentDirectory() throws ExtractionException, IOException
	{
		Path differentFolder = tmpFolder.newFolder().toPath();
		LHAExtractor extractor = new LHAExtractor();
		ExtractorData extractorData = extractor.extract( compressedFile.toString(), differentFolder.toString(), false, actionDecider );

		Assert.assertEquals( 8, extractorData.getTotalFiles() );
		Assert.assertEquals( 6, extractorData.getTotalMSXImages() );
		Assert.assertEquals( 8, extractorData.getTotalExtractedFiles() );

		assertFileExists( "rom1.rom", differentFolder );
		assertFileExists( "rom2.rom", differentFolder );
		assertFileExists( "disk1.dsk", differentFolder );
		assertFileExists( "disk2.dsk", differentFolder );
		assertFileExists( "tape1.cas", differentFolder );
		assertFileExists( "tape2.wav", differentFolder );
		assertFileExists( "file1.bas", differentFolder );
		assertFileExists( "file2.ldr", differentFolder );
	}

	private void assertFileExists( String filename )
	{
		Assert.assertTrue( Paths.get( compressedFile.getParent().toString(), filename ).toFile().exists() );
	}

	private void assertFileExists( String filename, Path directory )
	{
		Assert.assertTrue( Paths.get( directory.toString(), filename ).toFile().exists() );
	}

	private void assertFileDoesNotExist( String filename )
	{
		Assert.assertFalse( Paths.get( compressedFile.getParent().toString(), filename ).toFile().exists() );
	}
}
