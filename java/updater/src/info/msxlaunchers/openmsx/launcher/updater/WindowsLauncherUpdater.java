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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implementation of <code>LauncherUpdater</code> for Windows
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class WindowsLauncherUpdater extends AbstractLauncherUpdater
{
	private final String EXE_FILENAME = "openMSX Launcher.exe";
	private final String EXE_FILENAME_UPDATE = EXE_FILENAME + STARTER_UPDATE_EXT;

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.updater.AbstractLauncherUpdater#startInstallation(java.lang.String, java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public void startInstallation( String jarFilesDirectory, String executableDirectory, String helpFileDirectory, File zipFile )
			throws FileUpdateFailedException, IOException
	{
		//Windows version will unzip the update file and replace the README file (since it doesn't get locked). It will not attempt to delete the old JAR files
		//as they're locked by the OS while the launcher is running. The Windows exe launcher will detect the presence of the update files and will copy them
		//over the old ones before starting the JVM process. The updater will also rename the current exe (allowed in Windows) to install the new one,
		unzipUpdateFile( jarFilesDirectory, zipFile );

		installNewExecutable( jarFilesDirectory, executableDirectory );

		installNewHelpFile( jarFilesDirectory, helpFileDirectory );
	}

	private void installNewExecutable( String jarFilesDirectory, String executableDirectory ) throws FileUpdateFailedException
	{
		File files[] = new File( jarFilesDirectory ).listFiles();

		if( files != null )
		{
			for( File file: files )
			{
				if( file.getName().equals( EXE_FILENAME_UPDATE ) )
				{
					Path currentExecutable = Paths.get( executableDirectory, EXE_FILENAME );
					Path backupExecutable = currentExecutable.resolveSibling( currentExecutable.getFileName() + OLD_EXE_EXT );
					Path newExecutable = Paths.get( jarFilesDirectory, EXE_FILENAME_UPDATE );

					try
					{
						Files.move( currentExecutable, backupExecutable );
						Files.move( newExecutable, currentExecutable );
					}
					catch( IOException ioe )
					{
						throw new FileUpdateFailedException();
					}

					return;
				}
			}
		}
	}
}
