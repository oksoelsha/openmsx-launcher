/*
 * Copyright 2016 Sam Elsharif
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
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;

import static java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of <code>LauncherUpdater</code> for Linux and BSD
 * 
 * @since v1.6
 * @author Sam Elsharif
 *
 */
final class LinuxBSDLauncherUpdater extends AbstractLauncherUpdater
{
	private final String EXE_FILENAME = "openmsx-launcher.run";
	private final String EXE_FILENAME_UPDATE = EXE_FILENAME + STARTER_UPDATE_EXT;

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.updater.AbstractLauncherUpdater#startInstallation(java.lang.String, java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public void startInstallation( String jarFilesDirectory, String executableDirectory, String helpFileDirectory, File zipFile )
			throws FileUpdateFailedException, IOException
	{
		//Linux and BSD OSes do not lock the JAR files that are used by the JVM process. This means that we can replace them while the launcher is running.
		//The update will take effect (i.e. new start script and JARs will be used) when the launcher is restarted
		unzipUpdateFile( jarFilesDirectory, zipFile );

		installNewJarFiles( jarFilesDirectory );

		installNewStartScript( jarFilesDirectory, executableDirectory );

		installNewHelpFile( jarFilesDirectory, helpFileDirectory );
	}

	private void installNewStartScript( String jarFilesDirectory, String executableDirectory ) throws FileUpdateFailedException
	{
		File files[] = new File( jarFilesDirectory ).listFiles();

		if( files != null )
		{
			for( File file: files )
			{
				if( file.getName().equals( EXE_FILENAME_UPDATE ) )
				{
					Path currentExecutable = Paths.get( executableDirectory, EXE_FILENAME );
					Path newExecutable = Paths.get( jarFilesDirectory, EXE_FILENAME_UPDATE );

					try
					{
						Set<PosixFilePermission> permissions = Arrays.asList( OWNER_READ, OWNER_WRITE, OWNER_EXECUTE ).stream().collect( Collectors.toSet() );
						Files.setPosixFilePermissions( newExecutable, permissions );
						Files.move( newExecutable, currentExecutable, StandardCopyOption.REPLACE_EXISTING );
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
