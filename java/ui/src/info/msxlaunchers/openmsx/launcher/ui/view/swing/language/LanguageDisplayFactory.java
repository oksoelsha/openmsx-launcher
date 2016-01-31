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
package info.msxlaunchers.openmsx.launcher.ui.view.swing.language;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public final class LanguageDisplayFactory
{
	private static Language currentSelectedLanguage;
	private static Map<String,String> commonDisplayMessages = null;

	/**
	 * Returns Map of keys to their translations as they appear in the corresponding properties file
	 * 
	 * @param clazz Class of the display window
	 * @param language Language enum
	 * @return Unmodifiable Map of keys to their translations
	 */
	public static Map<String,String> getDisplayMessages( Class<?> clazz, Language language )
	{
		if( !language.equals( currentSelectedLanguage ) )
		{
			commonDisplayMessages = getDisplayMessages( "Common_" + language.getLocaleName().toString()
					+ ".properties" );
			currentSelectedLanguage = language;
		}

		Map<String,String> specificClassPropertiesMap = getDisplayMessages( clazz.getSimpleName() +
				"_" + language.getLocaleName().toString() + ".properties" );

		Map<String,String> completeMessagesMap = new HashMap<String, String>( commonDisplayMessages );
		completeMessagesMap.putAll( specificClassPropertiesMap );
		
		return Collections.unmodifiableMap( completeMessagesMap );
	}

	private static Map<String,String> getDisplayMessages( String propertiesString )
	{
		Properties properties = new Properties();
		
		try( InputStream stream = LanguageDisplayFactory.class.getResourceAsStream( propertiesString ) )
		{
			properties.load( stream );
		}
		catch( IOException e )
		{
			//this shouldn't happen
		}

		Map<String,String> displayMessages = new HashMap<String, String>();

		for( String name: properties.stringPropertyNames() )
		{
			displayMessages.put( name, properties.getProperty( name ) );
		}

		return displayMessages;
	}
}
