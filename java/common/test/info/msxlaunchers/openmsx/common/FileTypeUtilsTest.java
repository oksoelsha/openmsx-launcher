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
	private String rom = "rom.rom";
	private String romUpper = "romUpper.ROM";
	private String ri = "ri.ri";
	private String riUpper = "riUpper.RI";
	private String col = "col.col";
	private String colUpper = "colUpper.COL";
	private String mx1 = "mx1.mx1";
	private String mx1Upper = "mx1Upper.MX1";
	private String mx2 = "mx2.mx2";
	private String mx2Upper = "mx2Upper.MX2";

	private String di1 = "di1.di1";
	private String di1Upper = "di1Upper.DI1";
	private String di2 = "di2.di2";
	private String di2Upper = "di2Upper.DI2";
	private String dmk = "dmk.dmk";
	private String dmkUpper = "dmkUpper.DMK";
	private String dsk = "dsk.dsk";
	private String dskUpper = "dskUpper.DSK";
	private String xsa = "files/xsa.xsa";
	private String xsaUpper = "xsaUpper.XSA";
	private String fd1 = "fd1.fd1";
	private String fd1Upper = "fd1Upper.FD1";
	private String fd2 = "fd2.fd2";
	private String fd2Upper = "fd2Upper.FD2";

	private String tapeCas = "tapeCas.cas";
	private String tapeCasUpper = "tapeCasUpper.CAS";
	private String tapeWav = "tapeWav.wav";
	private String tapeWavUpper = "tapeWavUpper.WAV";

	private String harddisk = "hdd.hdd";
	private String harddiskUpper = "hdd.HDD";

	private String laserdisc = "laserdisc.ogv";
	private String laserdiscUpper = "laserdiscUpper.OGV";

	private String zip = "zip.zip";
	private String zipUpper = "zipUpper.ZIP";
	private String gz = "gz.gz";
	private String gzUpper = "gzUpper.GZ";

	private String xml = "xml.Xml";

	private String ips = "ips.IPS";
	private String ups = "ups.ups";

	private String lha = "lha.LHA";
	private String lzh = "lzh.lzh";

	private String non = "non.non";

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

		file = new File( col );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( colUpper );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( mx1 );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( mx1Upper );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( mx2 );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( mx2Upper );
		assertTrue( FileTypeUtils.isROM( file ) );

		file = new File( dsk );
		assertFalse( FileTypeUtils.isROM( file ) );

		file = new File( harddisk );
		assertFalse( FileTypeUtils.isROM( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isROM( file ) );

		file = new File( ips );
		assertFalse( FileTypeUtils.isROM( file ) );

		file = new File( lha );
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

		file = new File( fd1 );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( fd1Upper );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( fd2 );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( fd2Upper );
		assertTrue( FileTypeUtils.isDisk( file ) );

		file = new File( rom );
		assertFalse( FileTypeUtils.isDisk( file ) );

		file = new File( harddiskUpper );
		assertFalse( FileTypeUtils.isDisk( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isDisk( file ) );

		file = new File( ips );
		assertFalse( FileTypeUtils.isDisk( file ) );

		file = new File( lzh );
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

		file = new File( ips );
		assertFalse( FileTypeUtils.isTape( file ) );

		file = new File( lha );
		assertFalse( FileTypeUtils.isTape( file ) );
	}

	@Test
	public void testIsHarddisk()
	{
		File file;

		file = null;
		assertFalse( FileTypeUtils.isHarddisk( file ) );

		file = new File( "/" );
		assertFalse( FileTypeUtils.isHarddisk( file ) );

		file = new File( harddisk );
		assertTrue( FileTypeUtils.isHarddisk( file ) );

		file = new File( harddiskUpper );
		assertTrue( FileTypeUtils.isHarddisk( file ) );

		file = new File( dsk );
		assertTrue( FileTypeUtils.isHarddisk( file ) );

		file = new File( dskUpper );
		assertTrue( FileTypeUtils.isHarddisk( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isHarddisk( file ) );

		file = new File( ips );
		assertFalse( FileTypeUtils.isHarddisk( file ) );

		file = new File( lzh );
		assertFalse( FileTypeUtils.isHarddisk( file ) );
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

		file = new File( harddisk );
		assertFalse( FileTypeUtils.isLaserdisc( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isLaserdisc( file ) );

		file = new File( ips );
		assertFalse( FileTypeUtils.isLaserdisc( file ) );

		file = new File( lzh );
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

		file = new File( ips );
		assertFalse( FileTypeUtils.isZIP( file ) );

		file = new File( lha );
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

		file = new File( ips );
		assertFalse( FileTypeUtils.isXML( file ) );

		file = new File( lzh );
		assertFalse( FileTypeUtils.isXML( file ) );
	}

	@Test
	public void testIsPatch()
	{
		File file;

		file = null;
		assertFalse( FileTypeUtils.isPatch( file ) );

		file = new File( "/" );
		assertFalse( FileTypeUtils.isPatch( file ) );

		file = new File( ips );
		assertTrue( FileTypeUtils.isPatch( file ) );

		file = new File( ups );
		assertTrue( FileTypeUtils.isPatch( file ) );

		file = new File( tapeCasUpper );
		assertFalse( FileTypeUtils.isPatch( file ) );

		file = new File( harddiskUpper );
		assertFalse( FileTypeUtils.isPatch( file ) );

		file = new File( zip );
		assertFalse( FileTypeUtils.isPatch( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isPatch( file ) );

		file = new File( lha );
		assertFalse( FileTypeUtils.isPatch( file ) );
	}
	
	@Test
	public void testIsLHA()
	{
		File file;

		file = null;
		assertFalse( FileTypeUtils.isLHA( file ) );

		file = new File( "/" );
		assertFalse( FileTypeUtils.isLHA( file ) );

		file = new File( ips );
		assertFalse( FileTypeUtils.isLHA( file ) );

		file = new File( ups );
		assertFalse( FileTypeUtils.isLHA( file ) );

		file = new File( tapeCasUpper );
		assertFalse( FileTypeUtils.isLHA( file ) );

		file = new File( harddiskUpper );
		assertFalse( FileTypeUtils.isLHA( file ) );

		file = new File( zip );
		assertFalse( FileTypeUtils.isLHA( file ) );

		file = new File( xml );
		assertFalse( FileTypeUtils.isLHA( file ) );

		file = new File( lha );
		assertTrue( FileTypeUtils.isLHA( file ) );

		file = new File( lzh );
		assertTrue( FileTypeUtils.isLHA( file ) );
	}

	@Test
	public void testOtherType()
	{
		File file = new File( non );

		assertFalse( FileTypeUtils.isROM( file ) );
		assertFalse( FileTypeUtils.isDisk( file ) );
		assertFalse( FileTypeUtils.isTape( file ) );
		assertFalse( FileTypeUtils.isHarddisk( file ) );
		assertFalse( FileTypeUtils.isLaserdisc( file ) );
		assertFalse( FileTypeUtils.isZIP( file ) );
		assertFalse( FileTypeUtils.isXML( file ) );
		assertFalse( FileTypeUtils.isPatch( file ) );
		assertFalse( FileTypeUtils.isLHA( file ) );
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
		assertEquals( 5, romExtensions.size() );

		//check the ROM extensions
		assertTrue( romExtensions.contains( "rom" ) );
		assertTrue( romExtensions.contains( "ri" ) );
		assertTrue( romExtensions.contains( "col" ) );
		assertTrue( romExtensions.contains( "mx1" ) );
		assertTrue( romExtensions.contains( "mx2" ) );
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
		assertEquals( 7, diskExtensions.size() );

		//check the disk extensions
		assertTrue( diskExtensions.contains( "di1" ) );
		assertTrue( diskExtensions.contains( "di2" ) );
		assertTrue( diskExtensions.contains( "dsk" ) );
		assertTrue( diskExtensions.contains( "dmk" ) );
		assertTrue( diskExtensions.contains( "xsa" ) );
		assertTrue( diskExtensions.contains( "fd1" ) );
		assertTrue( diskExtensions.contains( "fd2" ) );
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
		assertEquals( 3, tapeExtensions.size() );

		//check the tape extensions
		assertTrue( tapeExtensions.contains( "wav" ) );
		assertTrue( tapeExtensions.contains( "cas" ) );
		assertTrue( tapeExtensions.contains( "tsx" ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnmodifiableGetTapeExtensions()
	{
		Set<String> tapeExtensions = FileTypeUtils.getTapeExtensions();

		tapeExtensions.add( "cannot-add" );
	}

	@Test
	public void testGetHarddiskExtensions()
	{
		Set<String> harddiskExtensions = FileTypeUtils.getHarddiskExtensions();

		//there are currently two harddisk extensions supported
		assertEquals( 2, harddiskExtensions.size() );

		//check the harddisk extensions
		assertTrue( harddiskExtensions.contains( "dsk" ) );
		assertTrue( harddiskExtensions.contains( "hdd" ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnmodifiableGetHarddiskExtensions()
	{
		Set<String> harddiskExtensions = FileTypeUtils.getHarddiskExtensions();

		harddiskExtensions.add( "cannot-add" );
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

	@Test
	public void testGetLHAExtensions()
	{
		Set<String> lhaExtensions = FileTypeUtils.getLHAExtensions();

		//there are currently two LHA extensions supported
		assertEquals( 2, lhaExtensions.size() );

		//check the LHA extensions
		assertTrue( lhaExtensions.contains( "lha" ) );
		assertTrue( lhaExtensions.contains( "lzh" ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnmodifiableGetLHAExtensions()
	{
		Set<String> lhaExtensions = FileTypeUtils.getLHAExtensions();

		lhaExtensions.add( "cannot-add" );
	}
}
