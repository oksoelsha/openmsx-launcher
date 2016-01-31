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
package info.msxlaunchers.openmsx.extension;

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.common.FileUtils;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


import com.google.inject.Inject;

/**
 * Implementation class for <code>ExtensionLister</code> that reads extensions from openMSX's extensions directory
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class OpenMSXExtensionLister implements ExtensionLister
{
	private final SettingsPersister settingsPersister;

	private final static String HARDWARE_CONFIG_FILENAME = "hardwareconfig.xml";

	@Inject
	OpenMSXExtensionLister( SettingsPersister settingsPersister )
	{
		this.settingsPersister = Objects.requireNonNull( settingsPersister );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.extension.ExtensionLister#get()
	 */
	@Override
	public Set<String> get() throws InvalidMachinesDirectoryException, IOException
	{
		Settings settings = settingsPersister.getSettings();

		Set<String> extensions = new HashSet<String>();

		//Extension directory is at the same level as machines
		String openMSXMachinesFullPath = settings.getOpenMSXMachinesFullPath();
		if( openMSXMachinesFullPath == null || !new File( openMSXMachinesFullPath ).isDirectory() )
		{
			throw new InvalidMachinesDirectoryException();
		}

		File parentDirectory = new File( settings.getOpenMSXMachinesFullPath() ).getParentFile();
		File[] fileList = parentDirectory.listFiles();

		for( File directory: fileList )
		{
			if( directory.isDirectory() && directory.getName().equals( "extensions" ) )
			{
				fileList = directory.listFiles();

				for( File file: fileList )
				{
					if( isValidOpenMSXExtensionFile( file ) )
					{
						extensions.add( FileUtils.getFileNameWithoutExtension( file ) );
					}
					else if( isValidOpenMSXExtensionDirectory( file ) )
					{
						extensions.add( file.getName() );
					}
				}
			}
		}

		return Collections.unmodifiableSet( extensions );
	}

	private boolean isValidOpenMSXExtensionFile( File file )
	{
		return FileTypeUtils.isXML( file );
	}

	private static boolean isValidOpenMSXExtensionDirectory( File directory )
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
