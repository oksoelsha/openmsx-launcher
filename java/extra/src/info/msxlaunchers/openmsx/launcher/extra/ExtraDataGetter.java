/*
 * Copyright 2013 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.extra;

import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Interface for getting extra data
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public interface ExtraDataGetter
{
	/**
	 * Returns Map of hash codes to ExtraData objects
	 * 
	 * @return Unmodifiable Map of hash codes to ExtraData objects. if no extra data are found, then an empty Map is returned 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	Map<String,ExtraData> getExtraData() throws FileNotFoundException, IOException;

	/**
	 * Returns the version of the current extra-data.dat file or "0.0" if it is not there
	 * 
	 * @return Version of the current extra-data.dat file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	String getExtraDataFileVersion() throws FileNotFoundException, IOException;
}
