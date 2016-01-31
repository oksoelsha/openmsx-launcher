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

import java.io.File;

/**
 * Utility class that contains static file utility methods 
 * 
 * @since v1.3
 * @author Sam Elsharif
 */
public final class FileUtils
{
	/**
	 * Returns file name without extension
	 * 
	 * @param filename File or directory name
	 * @return File or directory name without extension, or null if given null
	 */
	public static String getFileNameWithoutExtension( String filename )
	{
		String filenameWithoutExt = null;

		if( filename != null )
		{
			String filePortion = new File( filename ).getName();
			int pos = filePortion.lastIndexOf( "." );

	        if( pos == -1 )
	        {
	        	filenameWithoutExt = filePortion;
	        }
	        else
	        {
	        	filenameWithoutExt = filePortion.substring( 0, pos );
	        }
		}

		return filenameWithoutExt;
	}

	/**
	 * Returns file or directory name without extension
	 * 
	 * @param file File that represents a file or directory name
	 * @return File or directory name without extension, or null if given null
	 */
	public static String getFileNameWithoutExtension( File file )
	{
		String filename = null;

		if( file != null )
		{
			return getFileNameWithoutExtension( file.getName() );
		}

		return filename;
	}
}
