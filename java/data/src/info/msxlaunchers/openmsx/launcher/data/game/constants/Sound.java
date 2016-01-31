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
package info.msxlaunchers.openmsx.launcher.data.game.constants;

import info.msxlaunchers.openmsx.common.EnumWithDisplayName;

/**
 * Enum class to list all sound chips supported by openMSX
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public enum Sound implements EnumWithDisplayName
{
	PSG( "PSG" ),
	SCC( "SCC" ),
	SCC_I( "SCC-I" ),
	PCM( "PCM" ),
	MSX_MUSIC( "MSX-MUSIC" ),
	MSX_AUDIO( "MSX-AUDIO" ),
	MOONSOUND( "Moonsound" ),
	MIDI( "MIDI" );

	private final String displayName;

	private Sound( String displayName )
	{
		this.displayName = displayName;
	}

	@Override
	public String getDisplayName()
	{
		return displayName;
	}
}
