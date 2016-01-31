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
package info.msxlaunchers.openmsx.common;

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
}