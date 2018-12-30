/*
 * Copyright 2019 Sam Elsharif
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
 * Enum class for the input devices
 * 
 * @since v1.11
 * @author Sam Elsharif
 *
 */
public enum InputDevice implements NumericalEnum
{
	NONE( 0 ),
	JOYSTICK( 1 ),
	JOYSTICK_KEYBOARD( 2 ),
	MOUSE( 3 ),
	ARKANOID_PAD( 4 ),
	TRACKBALL( 5 ),
	TOUCHPAD( 6 );

	private final int value;

	private static List<InputDevice> valuesList = new ArrayList<>( InputDevice.values().length );
	static
	{
		for( InputDevice genre: InputDevice.values() )
		{
			valuesList.add( genre );
		}
		valuesList = Collections.unmodifiableList( valuesList );
	}

	private InputDevice( int value )
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
	 * Returns <code>InputDevice</code> enum for the given numerical value, or NONE if no match was found
	 * 
	 * @param value Numerical value of a genre
	 * @return JoyPort enum for the given numerical value
	 */
	public static InputDevice fromValue( final int value )
	{
		int valueOrDefault = value;

		if( valueOrDefault < 0 || valueOrDefault >= InputDevice.values().length )
		{
			valueOrDefault = 0;
		}

		return valuesList.get( valueOrDefault );
	}
}
