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
package info.msxlaunchers.openmsx.launcher.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Abstract class that implements <code>UpdateChecker</code> that contains platform common methods
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
abstract class AbstractLauncherUpdater implements LauncherUpdater
{
	private final int WRITE_BUFFER = 2048;
	private final String JAR_EXT = ".jar";
	private final String UPDATE_JAR_ENDING = JAR_EXT + "_update";
	private final String HELP_FILENAME = "README.html";

	protected void unzipUpdateFile( String jarFilesDirectory, String helpFileDirectory, File zipFile ) throws FileUpdateFailedException, IOException
	{
        byte[] buffer = new byte[WRITE_BUFFER];
        try( ZipInputStream zis = new ZipInputStream( new FileInputStream( zipFile ) ) )
        {
	    	ZipEntry ze = zis.getNextEntry();

	    	while( ze != null )
	    	{
	    		String fileName = ze.getName();
	    		File newFile = new File( jarFilesDirectory, fileName );

	    		try( FileOutputStream fos = new FileOutputStream( newFile ) )
	    		{
		    		int len;
		    		while( (len = zis.read( buffer )) > 0 )
		    		{
		    			fos.write( buffer, 0, len );
		    		}
	    		}
	    		catch( IOException ioe )
	    		{
	    			throw new FileUpdateFailedException();
	    		}

	    		ze = zis.getNextEntry();
	    	}

	    	zis.closeEntry();
        }

        //delete the ZIP file
        if( !zipFile.delete() )
        {
        	throw new FileUpdateFailedException();
        }
	}

	protected void installNewJarFiles( String jarFilesDirectory ) throws FileUpdateFailedException
	{
		File files[] = new File( jarFilesDirectory ).listFiles();

		if( files != null )
		{
			List<File> oldJarFiles = new ArrayList<>( files.length );
			List<File> newUpdateJarFiles = new ArrayList<>( files.length );
	
			for( File file: files )
			{
				String filename = file.getName();
	
				if( filename.endsWith( UPDATE_JAR_ENDING ) )
				{
					newUpdateJarFiles.add( file );
				}
				else if( filename.endsWith( JAR_EXT ) )
				{
					oldJarFiles.add( file );
				}
			}
	
			if( newUpdateJarFiles.size() > 0 )
			{
				for( File file: oldJarFiles )
				{
					if( !file.delete() )
					{
						throw new FileUpdateFailedException();
					}
				}
	
				int jarFileValidLength = UPDATE_JAR_ENDING.length() - JAR_EXT.length();
				for( File file: newUpdateJarFiles )
				{
					if( !file.renameTo( new File( jarFilesDirectory, file.getName().substring( 0, file.getName().length() - jarFileValidLength ) ) ) )
					{
						throw new FileUpdateFailedException();
					}
				}
			}
		}
	}

	protected void installNewHelpFile( String jarFilesDirectory, String helpFileDirectory ) throws FileUpdateFailedException
	{
		File files[] = new File( jarFilesDirectory ).listFiles();

		if( files != null )
		{
			for( File file: files )
			{
				if( file.getName().equals( HELP_FILENAME ) )
				{
					File destination = new File( helpFileDirectory, file.getName() );
					if( !destination.delete() )
					{
						throw new FileUpdateFailedException();
					}
					if( !file.renameTo( destination ) )
					{
						throw new FileUpdateFailedException();
					}
					return;
				}
			}
		}
	}
}
