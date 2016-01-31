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

import info.msxlaunchers.openmsx.common.NumericalEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Enum class for the game genres
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public enum Genre implements NumericalEnum
{
	UNKNOWN( "", 0 ),
	ACTION( "Action", 1 ),
	ADULT( "Adult", 2 ),
	ADVENTURE_ALL( "Adventure (All)", 3 ),
	ADVENTURE_POINT_AND_CLICK( "Adventure | Point and Click", 4 ),
	ADVENTURE_TEXT_AND_GFX( "Adventure | Text and Gfx", 5 ),
	ADVENTURE_TEXT_ONLY( "Adventure | Text only", 6 ),
	ARCADE( "Arcade", 7 ),
	BOARD_GAMES( "Board Games", 8 ),
	BREAK_OUT( "Break-out", 9 ),
	CARD_GAMES( "Card Games", 10 ),
	COMMUNICATION( "Communication", 11 ),
	COMPILER( "Compiler", 12 ),
	DATABASE( "Database", 13 ),
	DTP( "DTP", 14 ),
	EDUCATIONAL( "Educational",  15 ),
	FIGHTING( "Fighting", 16 ),
	FINANCIAL( "Financial", 17 ),
	GAMBLING_FRUIT_MACHINE( "Gambling / Fruit Machine", 18 ),
	GRAPHICS( "Graphics", 19 ),
	MISCELLANEOUS( "Miscellaneous", 20 ),
	OFFICE( "Office", 21 ),
	OPERATING_SYSTEM( "Operating System", 22 ),
	PARODY( "Parody", 23 ),
	PINBALL( "Pinball", 24 ),
	PLATFORM( "Platform", 25 ),
	PUZZLE( "Puzzle", 26 ),
	QUIZ( "Quiz", 27 ),
	RACING( "Racing", 28 ),
	REMAKE( "Remake", 29 ),
	RPG( "RPG", 30 ),
	SHOOT_EM_UP_ALL( "Shoot-'em-up (All)", 31 ),
	SHOOT_EM_UP_FIRST_PERSON_SHOOTER( "Shoot-'em-up | First-person shooter", 32 ),
	SHOOT_EM_UP_HORIZONTAL( "Shoot-'em-up | Horizontal", 33 ),
	SHOOT_EM_UP_ISOMETRIC( "Shoot-'em-up | Isometric", 34 ),
	SHOOT_EM_UP_MULTI_DIRECTIONAL( "Shoot-'em-up | Multi-directional", 35 ),
	SHOOT_EM_UP_MULTI_VERTICAL( "Shoot-'em-up | Vertical", 36 ),
	SIMULATION( "Simulation", 37 ),
	SOUND( "Sound", 38 ),
	SPORT_GAMES( "Sport Games", 39 ),
	SPORT_MANAGEMENT( "Sport Management", 40 ),
	SPREADSHEET( "Spreadsheet", 41 ),
	STRATEGY( "Strategy", 42 ),
	TOOL( "Tool", 43 ),
	VARIETY( "Variety", 44 ),
	WAR_GAMES( "War Games", 45 ),
	WORD_PROCESSOR( "Word Processor", 46 ),
	MAZE( "Maze", 47 ),
	PONG( "Pong", 48 ),
	BEAT_EM_UP( "Beat-'em-up", 49 ),
	DEXTERITY( "Dexterity", 50 ),
	;

	private final String displayName;
	private final int value;

	private static List<Genre> valuesList = new ArrayList<Genre>( Genre.values().length );
	static
	{
		for( Genre genre: Genre.values() )
		{
			valuesList.add( genre );
		}
		valuesList = Collections.unmodifiableList( valuesList );
	}

	private Genre( String displayName, int value )
	{
		this.displayName = displayName;
		this.value = value;
	}

	/**
	 * Returns the display name of the genre enum. This name is not localized and is only in English.
	 * 
	 * @return Display name
	 */
	public String getDisplayName()
	{
		return displayName;
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
	 * @return Genre enum for the given numerical value
	 */
	public static Genre fromValue( final int value )
	{
		int valueOrDefault = value;

		if( valueOrDefault < 0 || valueOrDefault > (Genre.values().length - 1) ) //subtracting 1 because max index is less than length
		{
			valueOrDefault = 0;
		}

		return valuesList.get( valueOrDefault );
	}
}
