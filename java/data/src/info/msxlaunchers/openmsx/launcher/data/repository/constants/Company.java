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
package info.msxlaunchers.openmsx.launcher.data.repository.constants;

import info.msxlaunchers.openmsx.common.EnumWithDisplayName;

/**
 * Enum class to list companies present in openMSX's game database with a high number of games
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public enum Company implements EnumWithDisplayName
{
	ACTIVISION( "Activision" ),
	AL_ALAMIAH( "Al Alamiah" ),
	ASCII( "ASCII" ),
	BOTHTEC( "Bothtec" ),
	BYTE_BUSTERS( "The Bytebusters" ),
	CASIO( "Casio" ),
	COMPILE( "Compile" ),
	DB_SOFT( "dB-SOFT" ),
	ENIX( "ENIX" ),
	GERMAN_GOMEZ_HERRERA( "German Gomez Herrera" ),
	HUDSON_SOFT( "Hudson Soft" ),
	HUSDON_SOFT_SOFTBANK( "Hudson Soft / Japanese Softbank" ),
	INFINITE( "Infinite" ),
	KAROSHI( "Karoshi Corporation" ),
	KONAMI( "Konami" ),
	MASS_TAEL( "Mass Tael" ),
	MICROCABIN( "Microcabin" ),
	NIPPON_COLPAX_UNIV( "Nippon Columbia / Colpax / Universal" ),
	PACK_IN_VIDEO( "Pack-In-Video" ),
	PAXANGA_SOFT( "Paxanga Soft" ),
	PONY_CANYON( "Pony Canyon" ),
	SEGA( "Sega" ),
	SONY( "Sony" ),
	TAITO( "TAITO" ),
	TE_SOFT( "T&ESOFT" ),
	TELNET_JAPAN( "Telenet Japan" ),
	ZAP( "ZAP" ),
	ZEMINA( "Zemina" );

	private final String displayName;

	private Company( String displayName )
	{
		this.displayName = displayName;
	}

	@Override
	public String getDisplayName()
	{
		return displayName;
	}
}
