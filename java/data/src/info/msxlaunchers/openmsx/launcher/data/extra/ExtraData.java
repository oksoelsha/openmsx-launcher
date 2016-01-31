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
package info.msxlaunchers.openmsx.launcher.data.extra;

/**
 * Class to represent extra data fields
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public class ExtraData
{
	private final int msxGenerationID;
	private final int generations;
	private final int soundChips;
	private final int genre1;
	private final int genre2;
	private final String suffix;

	private final static int MASK_BIT_1 = 0b00000001;
	private final static int MASK_BIT_2 = 0b00000010;
	private final static int MASK_BIT_3 = 0b00000100;
	private final static int MASK_BIT_4 = 0b00001000;
	private final static int MASK_BIT_5 = 0b00010000;
	private final static int MASK_BIT_6 = 0b00100000;
	private final static int MASK_BIT_7 = 0b01000000;
	private final static int MASK_BIT_8 = 0b10000000;

	public ExtraData( int msxGenerationID, int generations, int soundChips, int genre1, int genre2, String suffix )
	{
		this.msxGenerationID = msxGenerationID;
		this.generations = generations;
		this.soundChips = soundChips;
		this.genre1 = genre1;
		this.genre2 = genre2;
		this.suffix = suffix;
	}

	public int getMSXGenerationsID()
	{
		return msxGenerationID;
	}

	public boolean isMSX()
	{
		return (generations & MASK_BIT_1) == MASK_BIT_1;
	}

	public boolean isMSX2()
	{
		return (generations & MASK_BIT_2) == MASK_BIT_2;
	}

	public boolean isMSX2Plus()
	{
		return (generations & MASK_BIT_3) == MASK_BIT_3;
	}

	public boolean isTurboR()
	{
		return (generations & MASK_BIT_4) == MASK_BIT_4;
	}

	public boolean isPSG()
	{
		return (soundChips & MASK_BIT_1) == MASK_BIT_1;
	}

	public boolean isSCC()
	{
		return (soundChips & MASK_BIT_2) == MASK_BIT_2;
	}

	public boolean isSCCI()
	{
		return (soundChips & MASK_BIT_3) == MASK_BIT_3;
	}

	public boolean isPCM()
	{
		return (soundChips & MASK_BIT_4) == MASK_BIT_4;
	}

	public boolean isMSXMUSIC()
	{
		return (soundChips & MASK_BIT_5) == MASK_BIT_5;
	}

	public boolean isMSXAUDIO()
	{
		return (soundChips & MASK_BIT_6) == MASK_BIT_6;
	}

	public boolean isMoonsound()
	{
		return (soundChips & MASK_BIT_7) == MASK_BIT_7;
	}

	public boolean isMIDI()
	{
		return (soundChips & MASK_BIT_8) == MASK_BIT_8;
	}

	public int getGenre1()
	{
		return genre1;
	}

	public int getGenre2()
	{
		return genre2;
	}

	public String getSuffix()
	{
		return suffix;
	}
}
