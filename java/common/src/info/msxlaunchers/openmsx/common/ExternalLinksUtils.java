/*
 * Copyright 2020 Sam Elsharif
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

/**
 * Utility class that contains methods for external links (Generation MSX, YouTube, etc)
 * 
 * @since v1.13
 * @author Sam Elsharif
 *
 */
public final class ExternalLinksUtils
{
	private static final String GENERATION_MSX_URL = "http://www.generation-msx.nl/msxdb/softwareinfo/";
	private static final int MSX_GEN_NON_EXISTING_CODES_START = 10000;
	private static final String YOUTUBE_URL = "https://www.youtube.com/results?search_query=MSX";

	/**
	 * Returns whether the given game id is a valid MSX Generation id
	 * 
	 * @param id Game id to check
	 * @return True if given game id is a valid MSX Generation id, false otherwise
	 */
	public static boolean isMSXGenerationIdValid( int id )
	{
		return id > 0 && id < MSX_GEN_NON_EXISTING_CODES_START;
	}

	/**
	 * Returns MSX Generation URL for the given game id
	 * 
	 * @param id Game MSX Generation id
	 * @return MSX Generation URL for the given game id
	 */
	public static String getMSXGenerationURL( int id )
	{
		return GENERATION_MSX_URL + id;
	}

	/**
	 * Returns YouTube search URL for the given game name
	 * 
	 * @param gameName Game name to search for on YouTube
	 * @return YouTube search URL for the given game name
	 */
	public static String getYouTubeSearchURL( String gameName )
	{
		return YOUTUBE_URL + "+" + gameName.replace( " ", "+" ).replaceAll( "\\&", "%26" );
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
