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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import info.msxlaunchers.openmsx.common.version.Application;

/**
 * Utility class that contains general purpose methods
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public final class Utils
{
	/**
	 * Returns a signed decimal value of the given String, or 0 if the given String is not a number
	 * 
	 * @param string Number as a string
	 * @return Signed decimal integer value represented by the argument
	 */
	public static int getNumber( String string )
	{
		int number = 0;
		
		try
		{
			number = Integer.parseInt( string );
		}
		catch( NumberFormatException nfe )
		{
			//nothing - 0 will be returned
		}

		return number;
	}

	/**
	 * Returns whether the given string is a number
	 * 
	 * @param string Potential number as a string
	 * @return True if given string is a number
	 */
	public static boolean isNumber( String string )
	{
		try
		{
			Integer.parseInt( string );
			return true;
		}
		catch( NumberFormatException nfe )
		{
			return false;
		}
	}
	
	/**
	 * Returns a String object representing the given integer value
	 * 
	 * @param value Integer value
	 * @return String object of the argument in base 10
	 */
	public static String getString( int value )
	{
		return Integer.toString( value );
	}

	/**
	 * Returns a String object representing the given long value
	 * 
	 * @param value Long value
	 * @return String object of the argument in base 10
	 */
	public static String getString( long value )
	{
		return Long.toString( value );
	}

	/**
	 * Returns String object representing the given <code>NumericalEnum</code> value
	 * 
	 * @param numericalEnum NumericalEnum enum
	 * @return String object representing the given argument
	 */
	public static String getEnumValue( NumericalEnum numericalEnum )
	{
		int value = 0;

		if( numericalEnum != null )
		{
			value = numericalEnum.getValue();
		}

		return getString( value );
	}	

    /**
     * Returns whether the given String is null or empty after trimming white spaces
     * 
     * @param string String object
     * @return true if the given String is null or is still empty after trimming white spaces
     */
    public static boolean isEmpty( String string )
    {
    	return string == null || string.trim().isEmpty();
    }

    /**
     * Returns null for an empty string after trimming or the string itself if it is not empty
     * 
     * @param string String object
     * @return true if the given String is null or is still empty after trimming white spaces
     */
	public static String resetIfEmpty( String string )
	{
		return isEmpty( string ) ? null : string;
	}

	/**
	 * Returns whether two strings are equal - two null strings are considered equal
	 * 
	 * @param str1 First string
	 * @param str2 Second string
	 * @return true if the two strings are equal
	 */
	public static boolean equalStrings( String str1, String str2 )
	{
		return (str1 == null ? str2 == null : str1.equals( str2 ));
	}

	/**
	 * Converts Set of Strings to a case-insensitive ordered array of Strings
	 * 
	 * @param set Set containing strings
	 * @return Case-insensitive sorted array of Strings
     * @throws NullPointerException if set is null
	 */
	public static String[] getSortedCaseInsensitiveArray( Set<String> set )
	{
		Objects.requireNonNull( set );

		return set.stream().sorted( String.CASE_INSENSITIVE_ORDER ).collect( Collectors.toList() ).toArray( new String[set.size()] );
	}

	/**
	 * Returns a sorted copy Set of the provided set
	 * 
	 * @param set Set containing strings
	 * @return Case-insensitive sorted Set of Strings
     * @throws NullPointerException if set is null
	 */
	public static Set<String> getSortedCaseInsensitiveSet( Set<String> set )
	{
		Objects.requireNonNull( set );

		SortedSet<String> sortedSet = new TreeSet<String>( String.CASE_INSENSITIVE_ORDER );

		sortedSet.addAll( set );

		return sortedSet;
	}

	/**
	 * Returns the OS-specific directory full name where the launcher will save some of its data
	 * 
	 */
	public static String getUserDataDirectory()
	{
		String userDataDirectory = null;

		if( OSUtils.isWindows() )
		{
			userDataDirectory = System.getProperty( "user.dir" );
		}
		else if( OSUtils.isMac() )
		{
			userDataDirectory = System.getProperty( "user.home" ) + "/Library/Application Support/" + Application.APP_NAME;
		}
		else if( OSUtils.isLinux() || OSUtils.isBSD() )
		{
			userDataDirectory = System.getProperty( "user.home" ) + "/" + Application.APP_NAME;
		}

		return userDataDirectory;
	}

	/**
	 * Starts default browser pointing to given address or host name
	 * 
	 * @param address Address or host name as String
	 * @throws IOException if the user default browser is not found, or it fails to be launched, or the default handler application failed to be launched. The given address must be valid
	 */
	public static void startBrowser( URI uri ) throws IOException
	{
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if( desktop != null && desktop.isSupported( Desktop.Action.OPEN ) )
	    {
			desktop.browse( uri );
	    }
	    else
	    {
	    	//the following link:
	    	// http://stackoverflow.com/questions/8258153/how-to-get-desktop-class-supported-under-linux
	    	//explains why code might get here on Linux
	    }		
	}

	/**
	 * Starts default browser pointing to given address or host name
	 * 
	 * @param address Address or host name as String
	 * @throws IOException if the user default browser is not found, or it fails to be launched, or the default handler application failed to be launched. The given address must be valid
	 */
	public static void startBrowser( String address ) throws IOException
	{
		try
		{
			URI uri = new URI( address );
			startBrowser( uri );
		}
		catch( URISyntaxException e )
		{
			//this is not supposed to happen but throw an IOException anyway
		}
	}
}