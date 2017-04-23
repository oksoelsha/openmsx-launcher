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

import static org.mockito.Matchers.notNull;
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
		assertEquals( HashUtils.getSHA1Code( new File( validFile ) ), "f236345f43828597739f4a326318b6a3876ff73f" );

		String emptyFile = getClass().getResource( "files/empty.rom" ).getFile();
		assertEquals( HashUtils.getSHA1Code( new File( emptyFile ) ), "da39a3ee5e6b4b0d3255bfef95601890afd80709" );
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
		assertEquals( HashUtils.getMD5Sum( new File( validFile ) ), "cf623b0847e0dc101cb183d7ade3cb27" );

		String emptyFile = getClass().getResource( "files/empty.rom" ).getFile();
		assertEquals( HashUtils.getMD5Sum( new File( emptyFile ) ), "d41d8cd98f00b204e9800998ecf8427e" );
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
}
