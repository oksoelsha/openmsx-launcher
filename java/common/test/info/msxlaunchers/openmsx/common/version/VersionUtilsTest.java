package info.msxlaunchers.openmsx.common.version;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

	@Test
	public void testIsVersionNewer()
	{
		assertTrue( VersionUtils.isVersionNewer( "1", "2" ) );
		assertTrue( VersionUtils.isVersionNewer( "1.2", "1.3" ) );
		assertFalse( VersionUtils.isVersionNewer( "1.2", "1.2" ) );
		assertFalse( VersionUtils.isVersionNewer( "1.3", "1.2" ) );
		assertTrue( VersionUtils.isVersionNewer( "1.3", "3.4.9" ) );
		assertFalse( VersionUtils.isVersionNewer( "2.1", "2.1.0" ) );
		assertTrue( VersionUtils.isVersionNewer( "2.1", "2.1.1" ) );
		assertFalse( VersionUtils.isVersionNewer( "2.1.1", "2.1" ) );
		assertTrue( VersionUtils.isVersionNewer( "1.12.13.3", "1.12.13.4" ) );
		assertFalse( VersionUtils.isVersionNewer( "1.3", null ) );
		assertFalse( VersionUtils.isVersionNewer( null, "3.4" ) );
		assertFalse( VersionUtils.isVersionNewer( "1.3", "as" ) );
		assertFalse( VersionUtils.isVersionNewer( "h", "3.2" ) );
		assertFalse( VersionUtils.isVersionNewer( "", "2.4" ) );
		assertFalse( VersionUtils.isVersionNewer( "1.1", "" ) );
	}
}