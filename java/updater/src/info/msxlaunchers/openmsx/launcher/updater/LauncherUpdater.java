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
 * Interface for applying platform specific openMSX Launcher update procedure and operations
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
public interface LauncherUpdater
{
	/**
	 * Install the new version of openMSX Launcher by unzipping the update file and copying files to their corresponding locations
	 * 
	 * @param jarFilesDirectory Diretory where JAR files are
	 * @param helpFileDirectory Directory where REAEME html help file is
	 * @param zipFile Downloaded zip file after being written to disk
	 * @throws FileUpdateFailedException
	 * @throws IOException
	 */
	void installNewOpenMSXLauncher( String jarFilesDirectory, String helpFileDirectory, File zipFile ) throws FileUpdateFailedException, IOException;
}
