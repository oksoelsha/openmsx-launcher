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
package info.msxlaunchers.openmsx.launcher.data.settings.constants;

import info.msxlaunchers.openmsx.common.NumericalEnum;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum class for supported languages and locales
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public enum Language implements NumericalEnum
{
	ENGLISH(1, "en_US"),				//English - USA
	ARABIC(2, "ar_KW"),					//Arabic - Kuwait
	CATALAN(3, "ca_ES"),				//Catalan - Spain
	CHINESE_SIMPLIFIED(4, "zh_CN"),		//Chinese - China
	CHINESE_TRADITIONAL(5, "zh_TW"),	//Chinese - Taiwan
	DUTCH(6, "nl_NL"),					//Dutch - Netherlands
	FINNISH(7, "fi_FI"),				//Finish - Finland
	FRENCH(8, "fr_FR"),					//French - France
	GERMAN(9, "de_DE"),					//German - Germany
	ITALIAN(10, "it_IT"),				//Italian - Italy
	JAPANESE(11, "ja_JP"),				//Japanese - Japan
	KOREAN(12, "ko_KR"),				//Korean - Korea
	PERSIAN(13, "fa_IR"),				//Persian - Iran
	PORTUGUESE(14, "pt_BR"),			//Portuguese - Brazil
	RUSSIAN(15, "ru_RU"),				//Russian - Russia
	SPANISH(16, "es_ES"),				//Spanish - Spain
	SWEDISH(17, "sv_SE"),				//Swedish - Sweden
	;

	private final int value;
	private final String localeName;
	private static Map<Integer,Language> valuesMap = new HashMap<Integer,Language>();
	private static Map<String,Language> localesMap = new HashMap<String,Language>();
	static
	{
		for( Language language: Language.values() )
		{
			valuesMap.put( language.getValue(), language );
			localesMap.put( language.getLocaleName(), language );
		}
		valuesMap = Collections.unmodifiableMap( valuesMap );
		localesMap = Collections.unmodifiableMap( localesMap );
	}

	private Language( int value, String localeName )
	{
		this.value = value;
		this.localeName = localeName;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.common.NumericalEnum#getValue()
	 */
	@Override
	public int getValue()
	{
		return value;
	}

	/**
	 * Returns locale name for the language enum
	 * 
	 * @return Locale name (e.g. en_US)
	 */
	public String getLocaleName()
	{
		return localeName;
	}
	
	/**
	 * Returns <code>Language</code> enum for the given numerical value, or null if no match was found
	 * 
	 * @param value Numerical value
	 * @return Language enum, or null if no match was found
	 */
	public static Language fromValue( int value )
	{
		return valuesMap.get( value );
	}

	/**
	 * Returns <code>Language</code> enum for the given locale, or null if no match was found
	 * 
	 * @param localeName Locale name (e.g. en_US)
	 * @return Language enum, or null if no match was found
	 */
	public static Language fromLocale( String localeName )
	{
		return localesMap.get( localeName );
	}

	/**
	 * Returns if the given language is displayed from left to right, or false if argument is null  
	 * 
	 * @param language Language enum
	 * @return true if the given language is displayed from left to right, or false if argument is null
	 */
	public static boolean isLeftToRight( Language language )
	{
		return !isRightToLeft( language );
	}

	/**
	 * Returns if the given language is displayed from right to left, or false if argument is null  
	 * 
	 * @param language Language enum
	 * @return true if the given language is displayed from right to left, or false if argument is null
	 */
	public static boolean isRightToLeft( Language language )
	{
		if( language == null )
		{
			return false;
		}
		else
		{
			return language.equals( ARABIC ) || language.equals( PERSIAN );
		}
	}
}
