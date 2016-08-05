/*
 * Copyright 2016 Sam Elsharif
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

import info.msxlaunchers.openmsx.common.NumericalEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Enum class for the FDD (Floppy Disk Drive) mode
 * 
 * @since v1.8
 * @author Sam Elsharif
 *
 */
public enum FDDMode implements NumericalEnum
{
	ENABLE_BOTH( 0 ),
	DISABLE_SECOND( 1 ),
	DISABLE_BOTH( 2 );

	private final int value;

	private static List<FDDMode> valuesList = new ArrayList<FDDMode>( FDDMode.values().length );
	static
	{
		for( FDDMode genre: FDDMode.values() )
		{
			valuesList.add( genre );
		}
		valuesList = Collections.unmodifiableList( valuesList );
	}

	private FDDMode( int value )
	{
		this.value = value;
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
	 * Returns <code>Genre</code> enum for the given numerical value, or UNKNOWN if no match was found
	 * 
	 * @param value Numerical value of a genre
	 * @return FFMode enum for the given numerical value
	 */
	public static FDDMode fromValue( final int value )
	{
		int valueOrDefault = value;

		if( valueOrDefault < 0 || valueOrDefault >= FDDMode.values().length )
		{
			valueOrDefault = 0;
		}

		return valuesList.get( valueOrDefault );
	}
}
