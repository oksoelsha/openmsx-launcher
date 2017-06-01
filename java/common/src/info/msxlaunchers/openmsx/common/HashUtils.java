/*
 * Copyright 2013 Sam Elsharif
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.msxlaunchers.openmsx.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.zip.CRC32;

/**
 * Utility class that contains static methods to calculate SHA1 code 
 * 
 * @since v1.0
 * @author Sam Elsharif
 */
public final class HashUtils
{
	private final static String SHA1 = "SHA1";
	private final static String MD5 = "MD5";

	/**
	 * Returns SHA1 code of data coming from given stream
	 * 
	 * @param inputStream Input stream
	 * @return SHA1 code of the file, or null if an IOException is thrown when reading from the stream
	 * @throws NullPointerException if inputStream is null
	 */
	public static String getSHA1Code( InputStream inputStream )
	{
		Objects.requireNonNull( inputStream );

		return getHash( inputStream, SHA1 );
	}

	/**
	 * Returns SHA1 code of the given file
	 * 
	 * @param file File
	 * @return SHA1 code of the file, or null if a FileNotFoundException is thrown when converting the given file to a file input stream
	 * @throws NullPointerException if file is null
	 */
	public static String getSHA1Code( File file )
	{
		Objects.requireNonNull( file );

		try( FileInputStream fis = new FileInputStream( file ) )
		{
			return getSHA1Code( fis );
		}
		catch( IOException ioe )
		{
			return null;
		}
	}

	/**
	 * Returns MD5 sum of the given file
	 * 
	 * @param file File
	 * @return MD5 sum of the file, or null if the file is not found
	 * @throws NullPointerException if file is null
	 */
	public static String getMD5Sum( File file )
	{
		Objects.requireNonNull( file );

		try( FileInputStream fis = new FileInputStream( file ) )
		{
			return getHash( fis, MD5 );
		}
		catch( IOException ioe )
		{
			return null;
		}
	}

	/**
	 * Returns CRC32 code of the given file
	 * 
	 * @param file File
	 * @return CRC32 code of the file, or null if the file is not found
	 * @throws NullPointerException if file is null
	 */
	public static String getCRC32Code( File file )
	{
		try( InputStream inputStream = new BufferedInputStream( new FileInputStream( file ) ) )
		{
			CRC32 crc = new CRC32();

			int count;
			while( (count = inputStream.read()) != -1 )
			{
				crc.update( count );
			}

			return Long.toHexString( crc.getValue() );
		}
		catch( IOException ioe )
		{
			return null;
		}
	}

	private static String getHash( InputStream inputStream, String algorithm )
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance( algorithm );
		}
		catch( NoSuchAlgorithmException e )
		{
			//This should not happen
			throw new RuntimeException( e );
		}

		byte[] dataBytes = new byte[1024];
		 
		int nread = 0; 
		 
		try
		{
			while( (nread = inputStream.read( dataBytes )) != -1 )
			{
				md.update( dataBytes, 0, nread );
			}
		}
		catch ( IOException e )
		{
			return null;
		}

		byte[] mdbytes = md.digest();

		//convert the byte to hex format
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < mdbytes.length; i++ )
		{
			sb.append( Integer.toString( (mdbytes[i] & 0xff) + 0x100, 16).substring( 1 ) );
		}
		
		return sb.toString();
	}
}