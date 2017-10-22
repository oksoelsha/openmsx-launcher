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
package info.msxlaunchers.openmsx.launcher.persistence.settings;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Properties;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation of the <code>SettingsPersister</code> interface that persists the settings on and retrieves them from
 * the local hard disk. This implementation caches the retrieved settings to avoid unnecessarily getting them from disk
 * every time they're needed. That's why this class was designated with Guice's Singleton annotation to keep only one
 * instance of it in the lifetime of the Guice injector (which is the lifetime of the entire application)
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@Singleton
final class FileSettingsPersister implements SettingsPersister
{
	private final String SETTINGS_FILENAME = "settings-oml.ini";

	private final static String OPENMSX_FULL_PATH = "openmsx-path";
	private final static String OPENMSX_MACHINES_FULL_PATH = "openmsx-machines-path";
	private final static String SCREENSHOTS_FULL_PATH = "screenshots-path";
	private final static String DEFAULT_DATABASE = "default-database";
	private final static String LANGUAGE = "language";
	private final static String SHOW_UPDATE_ALL_DATABASES = "showUpdateAllDatabases";
	private final static String ENABLE_FEED_SERVICE = "enableFeedService";

	private final File settingsFile;

	private Settings cachedSettings = null;

	@Inject
	FileSettingsPersister( @Named("UserDataDirectory") String settingsPath )
	{
		Objects.requireNonNull( settingsPath );

		this.settingsFile = new File( settingsPath, SETTINGS_FILENAME );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister#saveSettings(info.msxlaunchers.openmsx.launcher.data.global.Settings)
	 */
	@Override
	public void saveSettings( Settings settings ) throws IOException
	{
		Objects.requireNonNull( settings );

		Properties properties = new Properties();

		String openMSXFullPath = getPropertyValue( settings.getOpenMSXFullPath() );
		String openMSXMachinesFullPath = getPropertyValue( settings.getOpenMSXMachinesFullPath() );
		String screenshotsFullPath = getPropertyValue( settings.getScreenshotsFullPath() );
		String defaultDatabase = getPropertyValue( settings.getDefaultDatabase() );
		String language = Utils.getEnumValue( settings.getLanguage() );
		boolean showUpdateAllDatabases = settings.isShowUpdateAllDatabases();
		boolean enableFeedService = settings.isEnableFeedService();

		properties.put( OPENMSX_FULL_PATH, openMSXFullPath );
		properties.put( OPENMSX_MACHINES_FULL_PATH, openMSXMachinesFullPath );
		properties.put( SCREENSHOTS_FULL_PATH, screenshotsFullPath );
		properties.put( DEFAULT_DATABASE, defaultDatabase );
		properties.put( LANGUAGE, language );
		properties.put( SHOW_UPDATE_ALL_DATABASES, new Boolean( showUpdateAllDatabases ).toString() );
		properties.put( ENABLE_FEED_SERVICE, new Boolean( enableFeedService ).toString() );

		try( OutputStream stream = new FileOutputStream( settingsFile ) )
		{
			properties.store( stream, null );
			cachedSettings = new Settings( openMSXFullPath,
											openMSXMachinesFullPath,
											screenshotsFullPath,
											defaultDatabase,
											Language.fromValue( Utils.getNumber( language ) ),
											showUpdateAllDatabases,
											enableFeedService );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister#getSettings()
	 */
	@Override
	public Settings getSettings() throws IOException
	{
		Settings settings = cachedSettings;
		
		if( settings == null )
		{
			Properties properties = new Properties();
	
			try( InputStream stream = new FileInputStream( settingsFile ) )
			{
				properties.load( stream );
			}
			catch( FileNotFoundException e )
			{
				//this means that there's no file. Most likely this is first time run. Ignore.
			}

			settings = new Settings( properties.getProperty( OPENMSX_FULL_PATH ),
					properties.getProperty( OPENMSX_MACHINES_FULL_PATH ),
					properties.getProperty( SCREENSHOTS_FULL_PATH ),
					properties.getProperty( DEFAULT_DATABASE ) ,
					Language.fromValue( Utils.getNumber( properties.getProperty( LANGUAGE ) ) ),
					Boolean.parseBoolean( properties.getProperty( SHOW_UPDATE_ALL_DATABASES, "false" ) ),
					Boolean.parseBoolean( properties.getProperty( ENABLE_FEED_SERVICE, "false" ) ) );

			cachedSettings = settings;
		}
		
		return settings;
	}

	private static String getPropertyValue( String propertyValue )
	{
		String value;
		
		if( propertyValue == null )
		{
			value = "";
		}
		else
		{
			value = propertyValue;
		}
		
		return value;
	}
}
