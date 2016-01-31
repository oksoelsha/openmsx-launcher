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

import java.io.IOException;
import java.util.Map;

/**
 * Interface for checking available updates on the MSX Launchers server
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
public interface UpdateChecker
{
	/*
	 * The following are the keys to use with the map returned in the getVersions() method
	 */
	public static final String KEY_OPENMSX_LAUNCHER = "openmsx-launcher";
	public static final String KEY_EXTRA_DATA = "extra-data";
	public static final String KEY_SCREENSHOTS = "screenshots";

	/**
	 * Returns versions of openMSX Launcher, extra-data.dat and screenshots
	 * 
	 * @return Map containing versions of the components referenced by the keys defined in this interface. If there's partial data, still return a partial map or an empty map if there's nothing defined
	 * @throws IOException
	 */
	Map<String,String> getVersions() throws IOException;

	/**
	 * Gets the new openMSX Launcher update file from MSX Launchers server as a ZIP file, writes it to disk and installs it
	 * 
	 * @throws IOException
	 * @throws FileUpdateFailedException
	 */
	void getNewOpenMSXLauncher() throws FileUpdateFailedException, IOException;

	/**
	 * Returns whether a new version of the launcher was downloaded but launcher wan't restarted for new version to take effect
	 * 
	 * @return True if the new version of the launcher was downloaded but not installed (because the application wasn't restarted)
	 */
	boolean isNewOpenMSXLauncherDownloaded();

	/**
	 * Gets the new extra-data.dat file from MSX Launchers server and writes it to disk on top of the existing one
	 * 
	 * @throws IOException
	 * @throws FileUpdateFailedException
	 */
	void getNewExtraDataFile() throws FileUpdateFailedException, IOException;
}
