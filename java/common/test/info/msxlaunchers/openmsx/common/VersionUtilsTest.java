package info.msxlaunchers.openmsx.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;

public class VersionUtilsTest
{
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void test_whenInstantiateClass_thenGetAnInstance()
	{
		//this is not a useful test. It's just meant to get code coverage
		new VersionUtils();
	}

	@Test
	public void test_givenNullOrEmptyFolderName_whenGetScreenshotsVersion_thenReturnEmptyString()
	{
		assertEquals( VersionUtils.getScreenshotsVersion( null ), "" );
		assertEquals( VersionUtils.getScreenshotsVersion( "" ), "" );
	}

	@Test
	public void test_givenValidFolderNameWithoutVersionFile_whenGetScreenshotsVersion_thenReturnEmptyString()
	{
		assertEquals( VersionUtils.getScreenshotsVersion( tmpFolder.getRoot().toString() ), "" );
	}

	@Test
	public void test_givenValidFolderNameWithValidVersionFile_whenGetScreenshotsVersion_thenReturnVersion() throws IOException
	{
		createTempVersionFile( "1.2" );

		assertEquals( VersionUtils.getScreenshotsVersion( tmpFolder.getRoot().toString() ), "1.2" );
	}

	private void createTempVersionFile( String version ) throws IOException
	{
		File tmpFile = tmpFolder.newFile( "version.txt" );
		PrintWriter writer = new PrintWriter( tmpFile );
		writer.println( version );
		writer.close();
	}
}