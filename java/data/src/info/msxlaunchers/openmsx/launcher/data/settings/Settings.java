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
package info.msxlaunchers.openmsx.launcher.data.settings;

import java.util.Objects;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Class to hold global settings for the application such as path to openMSX, path to openMSX's machines directory,
 * path to screenshots, default database to show when application first starts up and display language
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public final class Settings
{
	private final String openMSXFullPath;
	private final String openMSXMachinesFullPath;
	private final String screenshotsFullPath;
	private final String defaultDatabase;
	private final Language language;

	public Settings( String openMSXFullPath,
			String openMSXMachinesFullPath,
			String screenshotsFullPath,
			String defaultDatabase,
			Language language )
	{
		this.openMSXFullPath = openMSXFullPath;
		this.openMSXMachinesFullPath = openMSXMachinesFullPath;
		this.screenshotsFullPath = screenshotsFullPath;
		this.defaultDatabase = defaultDatabase;
		this.language = language;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( defaultDatabase, language, openMSXFullPath, openMSXMachinesFullPath, screenshotsFullPath );
	}

	@Override
	public boolean equals( Object obj )
	{
        if( obj == this )
        {
            return true;
        } 

        if( obj instanceof Settings )
        {
        	Settings other = (Settings)obj; 

            return Objects.equals( defaultDatabase, other.defaultDatabase ) &&
            		Objects.equals( language, other.language ) &&
            		Objects.equals( openMSXFullPath, other.openMSXFullPath ) &&
            		Objects.equals( openMSXMachinesFullPath, other.openMSXMachinesFullPath ) &&
            		Objects.equals( screenshotsFullPath, other.screenshotsFullPath );
        }

        return false;
	}

	//--------
	// Getters
	//--------
	public String getOpenMSXFullPath()
	{
		if( Utils.isEmpty( openMSXFullPath ) )
		{
			return null;
		}
		else
		{
			return openMSXFullPath;
		}
	}

	public String getOpenMSXMachinesFullPath()
	{
		if( Utils.isEmpty( openMSXMachinesFullPath ) )
		{
			return null;
		}
		else
		{
			return openMSXMachinesFullPath;
		}
	}

	public String getScreenshotsFullPath()
	{
		if( Utils.isEmpty( screenshotsFullPath ) )
		{
			return null;
		}
		else
		{
			return screenshotsFullPath;
		}
	}

	public String getDefaultDatabase()
	{
		if( Utils.isEmpty( defaultDatabase ) )
		{
			return null;
		}
		else
		{
			return defaultDatabase;
		}
	}

	public Language getLanguage()
	{
		return language;
	}
}
