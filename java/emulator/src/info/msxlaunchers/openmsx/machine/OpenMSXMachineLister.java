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
package info.msxlaunchers.openmsx.machine;

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.common.FileUtils;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


import com.google.inject.Inject;

/**
 * Implementation class for <code>MachineLister</code> that reads machine names from openMSX's machines directory
 * 
 * @since v1.0
 * @author Sam
 *
 */
final class OpenMSXMachineLister implements MachineLister
{
	private final SettingsPersister settingsPersister;

	private final String HARDWARE_CONFIG_FILENAME = "hardwareconfig.xml";

	@Inject
	OpenMSXMachineLister( SettingsPersister settingsPersister )
	{
		this.settingsPersister = Objects.requireNonNull( settingsPersister );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.machine.MachineLister#get()
	 */
	@Override
	public Set<String> get() throws InvalidMachinesDirectoryException, IOException
	{
		Settings settings = settingsPersister.getSettings();

		Set<String> machines = new HashSet<String>();

		String openMSXMachinesFullPath = settings.getOpenMSXMachinesFullPath();
		if( openMSXMachinesFullPath != null )
		{
			File openMSXMachinesDirectory = new File( openMSXMachinesFullPath );

			if( openMSXMachinesDirectory.isDirectory() )
			{
				File[] fileList = openMSXMachinesDirectory.listFiles();
				
				if( fileList != null )
				{
					for( File file: fileList )
					{
						if ( isValidOpenMSXMachineFile( file ) )
						{
							machines.add( FileUtils.getFileNameWithoutExtension( file ) );
						}
						else if( isValidOpenMSXMachineDirectory( file ) )
						{
							machines.add( file.getName() );
						}
					}
				}
			}
			else
			{
				throw new InvalidMachinesDirectoryException();
			}
		}
		else
		{
			throw new InvalidMachinesDirectoryException();
		}

		return Collections.unmodifiableSet( machines );
	}

	private boolean isValidOpenMSXMachineFile( File file )
	{
		return FileTypeUtils.isXML( file );
	}

	private boolean isValidOpenMSXMachineDirectory( File directory )
	{
		boolean valid = directory.isDirectory();

		if( valid )
		{
			File hardwareConfigFile = new File( directory, HARDWARE_CONFIG_FILENAME );
	
			valid = hardwareConfigFile.exists();
		}

		return valid;
	}
}
