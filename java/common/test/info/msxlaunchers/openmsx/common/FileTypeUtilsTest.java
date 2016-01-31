package info.msxlaunchers.openmsx.common;

import info.msxlaunchers.openmsx.common.FileTypeUtils;

import java.io.File;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FileTypeUtilsTest
{
	private String rom = getClass().getResource("files/rom.rom").getFile();
	private String romUpper = getClass().getResource("files/romUpper.ROM").getFile();
	private String ri = getClass().getResource("files/ri.ri").getFile();
	private String riUpper = getClass().getResource("files/riUpper.RI").getFile();

	private String di1 = getClass().getResource("files/di1.di1").getFile();
	private String di1Upper = getClass().getResource("files/di1Upper.DI1").getFile();
	private String di2 = getClass().getResource("files/di2.di2").getFile();
	private String di2Upper = getClass().getResource("files/di2Upper.DI2").getFile();
	private String dmk = getClass().getResource("files/dmk.dmk").getFile();
	private String dmkUpper = getClass().getResource("files/dmkUpper.DMK").getFile();
	private String dsk = getClass().getResource("files/dsk.dsk").getFile();
	private String dskUpper = getClass().getResource("files/dskUpper.DSK").getFile();
	private String xsa = getClass().getResource("files/xsa.xsa").getFile();
	private String xsaUpper = getClass().getResource("files/xsaUpper.XSA").getFile();

	private String tapeCas = getClass().getResource("files/tapeCas.cas").getFile();
	private String tapeCasUpper = getClass().getResource("files/tapeCasUpper.CAS").getFile();
	private String tapeWav = getClass().getResource("files/tapeWav.wav").getFile();
	private String tapeWavUpper = getClass().getResource("files/tapeWavUpper.WAV").getFile();

	private String laserdisc = getClass().getResource("files/laserdisc.ogv").getFile();
	private String laserdiscUpper = getClass().getResource("files/laserdiscUpper.OGV").getFile();

	private String zip = getClass().getResource("files/zip.zip").getFile();
	private String zipUpper = getClass().getResource("files/zipUpper.ZIP").getFile();
	private String gz = getClass().getResource("files/gz.gz").getFile();
	private String gzUpper = getClass().getResource("files/gzUpper.GZ").getFile();

	private String xml = getClass().getResource("files/xml.Xml").getFile();

	private String non = getClass().getResource("files/non.non").getFile();

	@Test
	public void test_whenInstantiateClass_thenGetAnInstance()
	{
		//this is not a useful test. It's just meant to get code coverage
		new FileTypeUtils();
	}

	@Test
	public void testIsROM()
	{
		File file;
		
		file = null;
		assertFalse( FileTypeUtils.isROM( file ) );

		file = new File( "/" );
		assertFalse( FileTypeUtils.isROM( file ) );

		file = new File( rom );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( romUpper );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( ri );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( riUpper );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( dsk );
		assertFalse( FileTypeUtils.isROM( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isROM( file ) );
	}

	@Test
	public void testIsDisk()
	{
		File file;
		
		file = null;
		assertFalse( FileTypeUtils.isDisk( file ) );

		file = new File( "/" );
		assertFalse( FileTypeUtils.isDisk( file ) );

		file = new File( di1 );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( di1Upper );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( di2 );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( di2Upper );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( dmk );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( dmkUpper );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( dsk );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( dskUpper );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( xsa );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( xsaUpper );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( rom );
		assertFalse( FileTypeUtils.isDisk( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isDisk( file ) );
	}

	@Test
	public void testIsTape()
	{
		File file;
		
		file = null;
		assertFalse( FileTypeUtils.isTape( file ) );

		file = new File( "/" );
		assertFalse( FileTypeUtils.isTape( file ) );

		file = new File( tapeCas );
		assertTrue( FileTypeUtils.isTape( file ) );

		file = new File( tapeCasUpper );
		assertTrue( FileTypeUtils.isTape( file ) );

		file = new File( tapeWav );
		assertTrue( FileTypeUtils.isTape( file ) );

		file = new File( tapeWavUpper );
		assertTrue( FileTypeUtils.isTape( file ) );

		file = new File( dsk );
		assertFalse( FileTypeUtils.isTape( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isTape( file ) );
	}

	@Test
	public void testIsLaserdisc()
	{
		File file;
		
		file = null;
		assertFalse( FileTypeUtils.isLaserdisc( file ) );

		file = new File( "/" );
		assertFalse( FileTypeUtils.isLaserdisc( file ) );

		file = new File( laserdisc );
		assertTrue( FileTypeUtils.isLaserdisc( file ) );

		file = new File( laserdiscUpper );
		assertTrue( FileTypeUtils.isLaserdisc( file ) );

		file = new File( tapeWavUpper );
		assertFalse( FileTypeUtils.isLaserdisc( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isLaserdisc( file ) );
	}

	@Test
	public void testIsZIP()
	{
		File file;
		
		file = null;
		assertFalse( FileTypeUtils.isZIP( file ) );

		file = new File( "/" );
		assertFalse( FileTypeUtils.isZIP( file ) );

		file = new File( zip );
		assertTrue( FileTypeUtils.isZIP( file ) );

		file = new File( zipUpper );
		assertTrue( FileTypeUtils.isZIP( file ) );

		file = new File( gz );
		assertTrue( FileTypeUtils.isZIP( file ) );

		file = new File( gzUpper );
		assertTrue( FileTypeUtils.isZIP( file ) );

		file = new File( tapeCasUpper );
		assertFalse( FileTypeUtils.isZIP( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isZIP( file ) );
	}

	@Test
	public void testIsXML()
	{
		File file;
		
		file = null;
		assertFalse( FileTypeUtils.isXML( file ) );

		file = new File( "/" );
		assertFalse( FileTypeUtils.isXML( file ) );

		file = new File( xml );
		assertTrue( FileTypeUtils.isXML( file ) );

		file = new File( tapeCasUpper );
		assertFalse( FileTypeUtils.isXML( file ) );

		file = new File( zip );
		assertFalse( FileTypeUtils.isXML( file ) );
	}
	
	@Test
	public void testOtherType()
	{
		File file = new File( non );

		assertFalse( FileTypeUtils.isROM( file ) );
		assertFalse( FileTypeUtils.isDisk( file ) );
		assertFalse( FileTypeUtils.isTape( file ) );
		assertFalse( FileTypeUtils.isLaserdisc( file ) );
		assertFalse( FileTypeUtils.isZIP( file ) );
		assertFalse( FileTypeUtils.isXML( file ) );
	}	

	@Test
	public void testGetMainFile()
	{
		String mainFile = null;
		String romA = "romA";
		String romB = "romB";
		String diskA = "diskA";
		String diskB = "diskB";
		String tape = "tape";
		String harddisk = "harddisk";
		String laserdisc = "laserdisc";
		String script = "script";

		mainFile = FileTypeUtils.getMainFile( romA, romB, diskA, diskB, tape, harddisk, laserdisc, script );
		assertEquals( romA, mainFile );

		mainFile = FileTypeUtils.getMainFile( null, romB, diskA, diskB, tape, harddisk, laserdisc, script );
		assertEquals( romB, mainFile );

		mainFile = FileTypeUtils.getMainFile( null, null, diskA, diskB, tape, harddisk, laserdisc, script );
		assertEquals( diskA, mainFile );

		mainFile = FileTypeUtils.getMainFile( null, null, null, diskB, tape, harddisk, laserdisc, script );
		assertEquals( diskB, mainFile );

		mainFile = FileTypeUtils.getMainFile( null, null, null, null, tape, harddisk, laserdisc, script );
		assertEquals( tape, mainFile );

		mainFile = FileTypeUtils.getMainFile( null, null, null, null, null, harddisk, laserdisc, script );
		assertEquals( harddisk, mainFile );

		mainFile = FileTypeUtils.getMainFile( null, null, null, null, null, null, laserdisc, script );
		assertEquals( laserdisc, mainFile );

		mainFile = FileTypeUtils.getMainFile( null, null, null, null, null, null, null, script );
		assertEquals( script, mainFile );

		mainFile = FileTypeUtils.getMainFile( null, null, null, null, null, null, null, null );
		assertNull( mainFile );
	}

	@Test
	public void testGetROMExtensions()
	{
		Set<String> romExtensions = FileTypeUtils.getROMExtensions();

		//there are currently two ROM extensions supported
		assertEquals( 2, romExtensions.size() );

		//check the ROM extensions
		assertTrue( romExtensions.contains( "rom" ) );
		assertTrue( romExtensions.contains( "ri" ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnmodifiableGetROMExtensions()
	{
		Set<String> romExtensions = FileTypeUtils.getROMExtensions();

		romExtensions.add( "cannot-add" );
	}

	@Test
	public void testGetDiskExtensions()
	{
		Set<String> diskExtensions = FileTypeUtils.getDiskExtensions();

		//there are currently five disk extensions supported
		assertEquals( 5, diskExtensions.size() );

		//check the disk extensions
		assertTrue( diskExtensions.contains( "di1" ) );
		assertTrue( diskExtensions.contains( "di2" ) );
		assertTrue( diskExtensions.contains( "dsk" ) );
		assertTrue( diskExtensions.contains( "dmk" ) );
		assertTrue( diskExtensions.contains( "xsa" ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnmodifiableGetDiskExtensions()
	{
		Set<String> diskExtensions = FileTypeUtils.getDiskExtensions();

		diskExtensions.add( "cannot-add" );
	}

	@Test
	public void testGetTapeExtensions()
	{
		Set<String> tapeExtensions = FileTypeUtils.getTapeExtensions();

		//there are currently two tape extensions supported
		assertEquals( 2, tapeExtensions.size() );

		//check the tape extensions
		assertTrue( tapeExtensions.contains( "wav" ) );
		assertTrue( tapeExtensions.contains( "cas" ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnmodifiableGetTapeExtensions()
	{
		Set<String> tapeExtensions = FileTypeUtils.getTapeExtensions();

		tapeExtensions.add( "cannot-add" );
	}

	@Test
	public void testGetLaserdiscExtensions()
	{
		Set<String> laserdiscExtensions = FileTypeUtils.getLaserdiscExtensions();

		//there is currently one laserdisc extension supported
		assertEquals( 1, laserdiscExtensions.size() );

		//check the laserdisc extensions
		assertTrue( laserdiscExtensions.contains( "ogv" ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnmodifiableGetLaserdiscExtensions()
	{
		Set<String> laserdiscExtensions = FileTypeUtils.getLaserdiscExtensions();

		laserdiscExtensions.add( "cannot-add" );
	}

	@Test
	public void testGetZIPExtensions()
	{
		Set<String> zipExtensions = FileTypeUtils.getZIPExtensions();

		//there are currently two ZIP extensions supported
		assertEquals( 2, zipExtensions.size() );

		//check the ZIP extensions
		assertTrue( zipExtensions.contains( "zip" ) );
		assertTrue( zipExtensions.contains( "gz" ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnmodifiableGetZIPExtensions()
	{
		Set<String> zipExtensions = FileTypeUtils.getZIPExtensions();

		zipExtensions.add( "cannot-add" );
	}
}
