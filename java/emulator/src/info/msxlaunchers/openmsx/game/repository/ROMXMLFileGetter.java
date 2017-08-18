/*
 * Copyright 2017 Sam Elsharif
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
package info.msxlaunchers.openmsx.game.repository;

import java.io.File;
import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import info.msxlaunchers.openmsx.common.Nullable;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;

/**
 * Implementation of <code>XMLFileGetter</code> to get the ROMs XML file (from openMSX)
 * 
 * @since v1.10
 * @author Sam Elsharif
 *
 */
final class ROMXMLFileGetter implements XMLFileGetter
{
	private final SettingsPersister settingsPersister;
	private final String baseDirectory;

	@Inject
	ROMXMLFileGetter( SettingsPersister settingsPersister, @Nullable @Named("BaseDirectory") String baseDirectory )
	{
		this.settingsPersister = settingsPersister;
		this.baseDirectory = baseDirectory;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.XMLFileGetter#get()
	 */
	@Override
	public File get() throws IOException
	{
		String openMSXMachinesFullPath = null;

		openMSXMachinesFullPath = settingsPersister.getSettings().getOpenMSXMachinesFullPath();

		File xmlFile;

		if( openMSXMachinesFullPath != null )
		{
			File openMSXMachinesFullPathWithBase;
			if( baseDirectory == null )
			{
				openMSXMachinesFullPathWithBase = new File( openMSXMachinesFullPath );
			}
			else
			{
				openMSXMachinesFullPathWithBase = new File( baseDirectory, openMSXMachinesFullPath );
			}

			xmlFile = new File( openMSXMachinesFullPathWithBase.getParentFile(), "softwaredb.xml" );
		}
		else
		{
			xmlFile = null;
		}

		return xmlFile;
	}
}
