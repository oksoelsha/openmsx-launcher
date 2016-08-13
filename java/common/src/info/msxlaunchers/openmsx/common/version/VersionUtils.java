/*
 * Copyright 2014 Sam Elsharif
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
package info.msxlaunchers.openmsx.common.version;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class that contains version related utility methods
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
public final class VersionUtils
{
	/**
	 * Returns the screenshots version
	 * 
	 * @param screenshotsPath Path to the screenshots - can be null
	 * @return Version extracted from the version.txt file in the given screenshots folder, or empty string if version cannot be identified
	 */
	public static String getScreenshotsVersion( String screenshotsPath )
	{
		String screenshotsVersion = "";

		try
		{
			if( screenshotsPath != null )
			{
				Path path = FileSystems.getDefault().getPath( screenshotsPath, "version.txt" );
	            byte data[] =  Files.readAllBytes( path );
	            screenshotsVersion = new String( data ).replace( "\n", "" ).replace( "\r", "" );
			}
		}
		catch( IOException ioe )
		{
			//just ignore - the answer will be empty string
		}

		return screenshotsVersion;
	}

	/**
	 * Returns true if the new version is newer than the current version. This method works on versions that contain digits and any number of decimal points
	 * 
	 * @param currentVersion Current version
	 * @param newVersion New version to compare against the current version
	 * @return True if newVersion is newer than currentVersion, false otherwise or if one of the versions is null or invalid format
	 */
	public static boolean isVersionNewer( String currentVersion, String newVersion )
	{
		boolean answer = false;

		if( currentVersion != null && newVersion != null )
		{
			String[] currentVersionDigits = currentVersion.split( "\\." );
			String[] newVersionDigits = newVersion.split( "\\." );

			boolean done = false;
			int counter = 0;

			try
			{
				while( !done )
				{
					int currentVersionInt;
					int newVersionInt;

					if( currentVersionDigits.length > counter && newVersionDigits.length > counter )
					{
						currentVersionInt = Integer.parseInt( currentVersionDigits[counter] );
						newVersionInt = Integer.parseInt( newVersionDigits[counter] );

						if( newVersionInt > currentVersionInt )
						{
							answer = true;
							done = true;
						}
						else
						{
							counter++;
						}
					}
					else
					{
						//pad the one with shorter length with a 0
						if( currentVersionDigits.length > counter && newVersionDigits.length == counter )
						{
							currentVersionInt = Integer.parseInt( currentVersionDigits[counter] );
							newVersionInt = 0;
						}
						else if( currentVersionDigits.length == counter && newVersionDigits.length > counter )
						{
							currentVersionInt = 0;
							newVersionInt = Integer.parseInt( newVersionDigits[counter] );
						}
						else
						{
							currentVersionInt = 0;
							newVersionInt = 0;
						}

						if( newVersionInt > currentVersionInt )
						{
							answer = true;
						}

						done = true;
					}
				}
			}
			catch( NumberFormatException nfe )
			{
				//any invalid version format (e.g. non-digits) just return false
				answer = false;
			}
		}

		return answer;
	}
}