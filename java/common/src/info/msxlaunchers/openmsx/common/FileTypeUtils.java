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

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class that contains static methods that deal with <code>Game</code> file fields 
 * 
 * @since v1.0
 * @author Sam Elsharif
 */
public final class FileTypeUtils
{
	private static final Set<String> romExtensions;
	private static final Set<String> diskExtensions;
	private static final Set<String> tapeExtensions;
	private static final Set<String> laserdiscExtensions;
	private static final Set<String> zipExtensions;
	private static final Set<String> xmlExtension;
	private static final Set<String> ipsExtension;

	static
	{
		final Set<String> romExtensionsTemp = new HashSet<String>();
		romExtensionsTemp.add( "ri" );
		romExtensionsTemp.add( "rom" );

		romExtensions = Collections.unmodifiableSet( romExtensionsTemp );

		final Set<String> diskExtensionsTemp = new HashSet<String>();
		diskExtensionsTemp.add( "di1" );
		diskExtensionsTemp.add( "di2" );
		diskExtensionsTemp.add( "dmk" );
		diskExtensionsTemp.add( "dsk" );
		diskExtensionsTemp.add( "xsa" );

		diskExtensions = Collections.unmodifiableSet( diskExtensionsTemp );

		final Set<String> tapeExtensionsTemp = new HashSet<String>();
		tapeExtensionsTemp.add( "cas" );
		tapeExtensionsTemp.add( "wav" );

		tapeExtensions = Collections.unmodifiableSet( tapeExtensionsTemp );

		final Set<String> laserdiscExtensionsTemp = new HashSet<String>();
		laserdiscExtensionsTemp.add( "ogv" );

		laserdiscExtensions = Collections.unmodifiableSet( laserdiscExtensionsTemp );

		final Set<String> zipExtensionsTemp = new HashSet<String>();
		zipExtensionsTemp.add( "zip" );
		zipExtensionsTemp.add( "gz" );

		zipExtensions = Collections.unmodifiableSet( zipExtensionsTemp );

		xmlExtension = Collections.unmodifiableSet( Collections.singleton( "xml" ) );

		ipsExtension = Collections.unmodifiableSet( Collections.singleton( "ips" ) );
	}

	public static final long MAX_DISK_FILE_SIZE = 737280;

	/**
	 * Returns if the given file is a ROM based on its extension
	 * 
	 * @param file File
	 * @return true if file is a ROM, false otherwise
	 */
	public static boolean isROM( File file )
	{
		return isType( file, romExtensions );
	}

	/**
	 * Returns if the given file is a disk based on its extension
	 * 
	 * @param file File
	 * @return true if file is a disk, false otherwise
	 */
	public static boolean isDisk( File file )
	{
		return isType( file, diskExtensions );
	}
	
	/**
	 * Returns if the given file is a tape based on its extension
	 * 
	 * @param file File
	 * @return true if file is a tape, false otherwise
	 */
	public static boolean isTape( File file )
	{
		return isType( file, tapeExtensions );
	}
	
	/**
	 * Returns if the given file is a laserdisc based on its extension
	 * 
	 * @param file File
	 * @return true if file is a laserdisc, false otherwise
	 */
	public static boolean isLaserdisc( File file )
	{
		return isType( file, laserdiscExtensions );
	}
	
	/**
	 * Returns if the given file is a ZIP file based on its extension
	 * 
	 * @param file File
	 * @return true if file is a ZIP file, false otherwise
	 */
	public static boolean isZIP( File file )
	{
		return isType( file, zipExtensions );
	}

	/**
	 * Returns if the given file is an XML file based on its extension
	 * 
	 * @param file File
	 * @return true if file is an XML file, false otherwise
	 */
	public static boolean isXML( File file )
	{
		return isType( file, xmlExtension );
	}

	/**
	 * Returns if the given file is an IPS file based on its extension
	 * 
	 * @param file File
	 * @return true if file is an IPS file, false otherwise
	 */
	public static boolean isIPS( File file )
	{
		return isType( file, ipsExtension );
	}

	/**
	 * Returns which of the given game fields is the main file for that game. The check is to find the first 
	 * non-empty game field in the following order:
	 * ROM A then ROM B then disk A then disk B then tape then hard disk then laserdisc then script.
	 * 
	 * @param romA ROM A file name
	 * @param romB ROM B file name
	 * @param diskA Disk A file name
	 * @param diskB Disk B file name
	 * @param tape Tape file name
	 * @param harddisk Hard disk file name
	 * @param laserdisc Laserdisc file name
	 * @param script Script file name
	 * @return First non-empty field in the above mentioned order
	 */
	public static String getMainFile( String romA, String romB, String diskA, String diskB,
			String tape, String harddisk, String laserdisc, String script )
	{
		String mainFile = null;

		if( !Utils.isEmpty( romA ) )
		{
			mainFile = romA;
		}
		else if( !Utils.isEmpty( romB ) )
		{
			mainFile = romB;
		}
		else if( !Utils.isEmpty( diskA ) )
		{
			mainFile = diskA;
		}
		else if( !Utils.isEmpty( diskB ) )
		{
			mainFile = diskB;
		}
		else if( !Utils.isEmpty( tape ) )
		{
			mainFile = tape;
		}
		else if( !Utils.isEmpty( harddisk ) )
		{
			mainFile = harddisk;
		}
		else if( !Utils.isEmpty( laserdisc ) )
		{
			mainFile = laserdisc;
		}
		else if( !Utils.isEmpty( script ) )
		{
			mainFile = script;
		}

		return mainFile;
	}

	/**
	 * Returns set containing ROM file extensions supported by openMSX
	 * 
	 * @return Non-modifiable set containing ROM file extensions
	 */
	public static Set<String> getROMExtensions()
	{
		return romExtensions;
	}

	/**
	 * Returns set containing disk file extensions supported by openMSX
	 * 
	 * @return Non-modifiable set containing disk file extensions
	 */
	public static Set<String> getDiskExtensions()
	{
		return diskExtensions;
	}

	/**
	 * Returns set containing tape file extensions supported by openMSX
	 * 
	 * @return Non-modifiable set containing tape file extensions
	 */
	public static Set<String> getTapeExtensions()
	{
		return tapeExtensions;
	}

	/**
	 * Returns set containing laserdisc file extensions supported by openMSX
	 * 
	 * @return Non-modifiable set containing laserdisc file extensions
	 */
	public static Set<String> getLaserdiscExtensions()
	{
		return laserdiscExtensions;
	}

	/**
	 * Returns set containing ZIP file extensions supported by openMSX
	 * 
	 * @return Non-modifiable set containing ZIP file extensions
	 */
	public static Set<String> getZIPExtensions()
	{
		return zipExtensions;
	}

	/**
	 * Returns set containing IPS file extensions
	 * 
	 * @return Non-modifiable set containing IPS file extensions
	 */
	public static Set<String> getIPSExtensions()
	{
		return ipsExtension;
	}

	private static boolean isType( File file, Set<String> validExtensions )
	{
		boolean isType = false;
		
		if( file == null || file.isDirectory() )
		{
			isType = false;
		}
		else
		{
			String filename = file.getName();
			String extension = filename.substring( filename.lastIndexOf( '.' ) + 1 ).toLowerCase();

			isType = validExtensions.contains( extension );
		}

		return isType;
	}
}
