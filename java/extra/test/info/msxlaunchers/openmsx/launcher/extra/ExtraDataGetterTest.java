package info.msxlaunchers.openmsx.launcher.extra;

import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExtraDataGetterTest
{
	private String extraDataWorkingDirectory = new File( System.getProperty( "user.dir" ), "sampleWorkingExtraDataFile" ).getAbsolutePath();

	@Test
	public void testValidExtraDataFile() throws FileNotFoundException, IOException
	{
		ExtraDataGetterImpl extraDataGetter = new ExtraDataGetterImpl( extraDataWorkingDirectory );

		Map<String,ExtraData> extraDataMap = extraDataGetter.getExtraData();

		ExtraData extraData = extraDataMap.get( "c81e6b8949b5d57ad6b527b418cd2cd8d2c37bcf" );
		assertEquals( 2, extraData.getMSXGenerationsID() );

		assertTrue( extraData.isMSX() );
		assertFalse( extraData.isMSX2() );
		assertFalse( extraData.isMSX2Plus() );
		assertFalse( extraData.isTurboR() );

		assertTrue( extraData.isPSG() );
		assertFalse( extraData.isSCC() );

		assertEquals( 1, extraData.getGenre1() );
		assertEquals( 0, extraData.getGenre2() );

		assertNull( extraData.getSuffix() );

		//another item
		extraData = extraDataMap.get( "bfbd5cddc38b5f65543f41562a6be749beae9516" );
		assertEquals( 961, extraData.getMSXGenerationsID() );

		assertFalse( extraData.isMSX() );
		assertTrue( extraData.isMSX2() );
		assertFalse( extraData.isMSX2Plus() );
		assertTrue( extraData.isTurboR() );

		assertTrue( extraData.isPSG() );
		assertFalse( extraData.isSCC() );
		assertTrue( extraData.isMSXMUSIC() );
		assertFalse( extraData.isMSXAUDIO() );

		assertEquals( 24, extraData.getGenre1() );
		assertEquals( 25, extraData.getGenre2() );

		assertEquals( "-en", extraData.getSuffix() );

		//another item
		extraData = extraDataMap.get( "e5f090574f8043f13e86b61486f23f92138ced01" );
		assertEquals( 1797, extraData.getMSXGenerationsID() );

		assertTrue( extraData.isMSX2() );
		assertFalse( extraData.isMSX2Plus() );

		assertFalse( extraData.isSCCI() );
		assertFalse( extraData.isPCM() );
		assertTrue( extraData.isMIDI() );

		assertEquals( 0, extraData.getGenre1() );
		assertEquals( 0, extraData.getGenre2() );

		assertNull( extraData.getSuffix() );

		//another item
		extraData = extraDataMap.get( "65160f0d59068dbbf76b11bc79e17a9369c8dcee" );
		assertEquals( 10003, extraData.getMSXGenerationsID() );

		assertTrue( extraData.isMSX() );
		assertTrue( extraData.isMSX2() );
		assertFalse( extraData.isTurboR() );

		assertTrue( extraData.isPSG() );
		assertTrue( extraData.isSCC() );
		assertFalse( extraData.isMoonsound() );

		assertEquals( 1, extraData.getGenre1() );
		assertEquals( 0, extraData.getGenre2() );

		assertNull( extraData.getSuffix() );

		//non-existing
		extraData = extraDataMap.get( "wrong" );

		assertNull( extraData );
	}

	@Test( expected = FileNotFoundException.class )
	public void testNonExistentFileWhenGettingFile() throws FileNotFoundException, IOException
	{
		ExtraDataGetterImpl extraDataGetter = new ExtraDataGetterImpl( "/wrong directory" );		

		extraDataGetter.getExtraData();
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testExtraDataCannotBeModified() throws FileNotFoundException, IOException
	{
		ExtraDataGetterImpl extraDataGetter = new ExtraDataGetterImpl( extraDataWorkingDirectory );

		Map<String,ExtraData> extraDataMap = extraDataGetter.getExtraData();

		ExtraData extraData = new ExtraData( 1, 1, 1, 1, 2, null );

		extraDataMap.put( "c81e6b8949b5d57ad6b527b418cd2cd8d2c37bcf", extraData );
	}

	@Test
	public void testGetExtraDataFileVersion() throws FileNotFoundException, IOException
	{
		ExtraDataGetterImpl extraDataGetter = new ExtraDataGetterImpl( extraDataWorkingDirectory );

		assertEquals( "1.14", extraDataGetter.getExtraDataFileVersion() );
	}

	@Test
	public void testGetExtraDataFileVersionForFileWithoutVersions() throws FileNotFoundException, IOException
	{
		ExtraDataGetterImpl extraDataGetter =
				new ExtraDataGetterImpl( new File( System.getProperty( "user.dir" ), "sampleWorkingExtraDataFileWithoutVersion" ).getAbsolutePath() );

		assertEquals( "0.0", extraDataGetter.getExtraDataFileVersion() );
	}

	@Test
	public void testGetExtraDataFileVersionForFileWithWrongVersionFormat() throws FileNotFoundException, IOException
	{
		ExtraDataGetterImpl extraDataGetter =
				new ExtraDataGetterImpl( new File( System.getProperty( "user.dir" ), "sampleWorkingExtraDataFileWithWrongVersionFormat" ).getAbsolutePath() );

		assertEquals( "0.0", extraDataGetter.getExtraDataFileVersion() );
	}

	@Test( expected = FileNotFoundException.class )
	public void testNonExistentFileWhenGettingFileVersion() throws FileNotFoundException, IOException
	{
		ExtraDataGetterImpl extraDataGetter = new ExtraDataGetterImpl( "/wrong directory" );		

		extraDataGetter.getExtraDataFileVersion();
	}
}
