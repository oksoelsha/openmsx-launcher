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
 * Enums for MSX generations
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public enum MSXGeneration implements EnumWithDisplayName
{
	MSX( "MSX" ),
	MSX2( "MSX2" ),
	MSX2Plus( "MSX2+" ),
	TURBO_R( "Turbo-R" );

	private final String displayName;

	private MSXGeneration( String displayName )
	{
		this.displayName = displayName;
	}

	@Override
	public String getDisplayName()
	{
		return displayName;
	}
}
