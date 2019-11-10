package info.msxlaunchers.openmsx.common;

import info.msxlaunchers.openmsx.common.HashUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;

public class HashUtilsTest
{
	@Test
	public void test_whenInstantiateClass_thenGetAnInstance()
	{
		//this is not a useful test. It's just meant to get code coverage
		new HashUtils();
	}

	@Test
	public void testGetSHA1CodeForValidFile()
	{
		String validFile = getClass().getResource( "files/valid.rom" ).getFile();
		assertEquals( "f236345f43828597739f4a326318b6a3876ff73f", HashUtils.getSHA1Code( new File( validFile ) ) );

		String emptyFile = getClass().getResource( "files/empty.rom" ).getFile();
		assertEquals( "da39a3ee5e6b4b0d3255bfef95601890afd80709", HashUtils.getSHA1Code( new File( emptyFile ) ) );
	}

	@Test( expected = NullPointerException.class )
	public void testGetSHA1CodeForNullFile()
	{
		HashUtils.getSHA1Code( (File)null );
	}

	@Test( expected = NullPointerException.class )
	public void testGetSHA1CodeForNullStream()
	{
		HashUtils.getSHA1Code( (FileInputStream)null );
	}

	@Test
	public void testGetSHA1CodeForNonExistentFile()
	{
		assertNull( HashUtils.getSHA1Code( new File( "/no_file" ) ) );
	}

	@Test
	public void testGetSHA1CodeForIOException() throws IOException
	{
		InputStream inputStream = mock( InputStream.class );
		Mockito.doThrow( new IOException() ).when( inputStream ).read( (byte[])notNull() );

		assertNull( HashUtils.getSHA1Code(inputStream ) );
	}

	@Test
	public void testGetMD5SumForValidFile()
	{
		String validFile = getClass().getResource( "files/valid.rom" ).getFile();
		assertEquals( "cf623b0847e0dc101cb183d7ade3cb27", HashUtils.getMD5Sum( new File( validFile ) ) );

		String emptyFile = getClass().getResource( "files/empty.rom" ).getFile();
		assertEquals( "d41d8cd98f00b204e9800998ecf8427e", HashUtils.getMD5Sum( new File( emptyFile ) ) );
	}

	@Test( expected = NullPointerException.class )
	public void testGetMD5SumForNullFile()
	{
		HashUtils.getMD5Sum( (File)null );
	}

	@Test
	public void testGetMD5SumForNonExistentFile()
	{
		assertNull( HashUtils.getMD5Sum( new File( "/no_file" ) ) );
	}

	@Test
	public void testGetCRC32CodeForValidFile()
	{
		String validFile = getClass().getResource( "files/valid.rom" ).getFile();
		assertEquals( "b1ace0a0", HashUtils.getCRC32Code( new File( validFile ) ) );

		String emptyFile = getClass().getResource( "files/empty.rom" ).getFile();
		assertEquals( "0", HashUtils.getCRC32Code( new File( emptyFile ) ) );
	}

	@Test( expected = NullPointerException.class )
	public void testGetCRC32CodeForNullFile()
	{
		HashUtils.getCRC32Code( (File)null );
	}

	@Test
	public void testGetCRC32CodeForNonExistentFile()
	{
		assertNull( HashUtils.getCRC32Code( new File( "/no_file" ) ) );
	}
}
