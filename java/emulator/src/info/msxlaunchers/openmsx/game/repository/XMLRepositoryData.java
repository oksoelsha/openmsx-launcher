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
package info.msxlaunchers.openmsx.game.repository;

import info.msxlaunchers.openmsx.common.Nullable;
import info.msxlaunchers.openmsx.game.repository.processor.XMLProcessor;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * XML implementation of <code>RepositoryData</code>
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class XMLRepositoryData implements RepositoryData
{
	private final SettingsPersister settingsPersister;
	private final XMLProcessor xmlProcessor;
	private final String baseDirectory;

	@Inject
	XMLRepositoryData( SettingsPersister settingsPersister, XMLProcessor xmlProcessor, @Nullable @Named("BaseDirectory") String baseDirectory )
	{
		this.settingsPersister = Objects.requireNonNull( settingsPersister );
		this.xmlProcessor = Objects.requireNonNull( xmlProcessor );
		this.baseDirectory = baseDirectory;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.RepositoryData#getRepositoryInfo()
	 */
	@Override
	public Map<String, RepositoryGame> getRepositoryInfo() throws IOException
	{
		String xmlFile = getXMLFile();

		if( xmlFile == null )
		{
			return null;
		}
		else
		{
			return xmlProcessor.getRepositoryInfo( xmlFile );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.RepositoryData#getDumpCodes(java.lang.String)
	 */
	@Override
	public Set<String> getDumpCodes( String code ) throws IOException
	{
		Objects.requireNonNull( code );

		return xmlProcessor.getDumpCodes( getXMLFile(), code );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.RepositoryData#getGameInfo(java.lang.String)
	 */
	@Override
	public RepositoryGame getGameInfo( String code ) throws IOException
	{
		Objects.requireNonNull( code );

		return xmlProcessor.getGameInfo( getXMLFile(), code );
	}

	private String getXMLFile() throws IOException
	{
		String openMSXMachinesFullPath = null;

		openMSXMachinesFullPath = settingsPersister.getSettings().getOpenMSXMachinesFullPath();

		String xmlFile;

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

			xmlFile = new File( openMSXMachinesFullPathWithBase.getParentFile(), "softwaredb.xml" ).getAbsolutePath();
		}
		else
		{
			xmlFile = null;
		}

		return xmlFile;
	}
}
