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

/**
 * Implementation of <code>LauncherUpdater</code> for Unix family of OSes (Linux, BSD and Mac)
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class UnixFamilyLauncherUpdater extends AbstractLauncherUpdater
{
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.webclient.LauncherUpdater#installNewOpenMSXLauncher(java.lang.String, java.lang.String, java.io.File)
	 */
	@Override
	public void installNewOpenMSXLauncher( String jarFilesDirectory, String helpFileDirectory, File zipFile ) throws FileUpdateFailedException, IOException
	{
		//The Unix family of OSes (Linux, BSD and Mac) do not lock the JAR files that are used by the JVM process. This means that we can replace them while
		//the launcher is running. The update will take effect (i.e. new JARs will be used) when the launcher is restarted
		unzipUpdateFile( jarFilesDirectory, helpFileDirectory, zipFile );

		installNewJarFiles( jarFilesDirectory );

		installNewHelpFile( jarFilesDirectory, helpFileDirectory );
	}
}
