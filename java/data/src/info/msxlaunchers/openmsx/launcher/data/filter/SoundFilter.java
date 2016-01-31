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
package info.msxlaunchers.openmsx.launcher.data.filter;

import java.util.Objects;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Sound;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

/**
 * Filter for Sound
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@FilterDescriptor( type = FilterType.SOUND )
final class SoundFilter implements Filter
{
	@Value1Field
	private final Sound sound;

	SoundFilter( Sound sound )
	{
		this.sound = Objects.requireNonNull( sound );
	}

	@Override
	public int hashCode()
	{
		return sound.hashCode();
	}

	@Override
	public boolean equals( Object obj )
	{
		boolean isSame;
		
		if( obj == null || !(obj instanceof SoundFilter) )
		{
			isSame = false;
		}
		else
		{
			isSame = sound.equals( ((SoundFilter)obj).sound );
		}

		return isSame;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.filter.Filter#isFiltered(info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public boolean isFiltered( Game game, RepositoryGame repositoryGame )
	{
		Objects.requireNonNull( game );

		boolean filtered = false;

		switch( sound )
		{
			case PSG:
				filtered = !game.isPSG();
				break;
			case SCC:
				filtered = !game.isSCC();
				break;
			case SCC_I:
				filtered = !game.isSCCI();
				break;
			case PCM:
				filtered = !game.isPCM();
				break;
			case MSX_MUSIC:
				filtered = !game.isMSXMUSIC();
				break;
			case MSX_AUDIO:
				filtered = !game.isMSXAUDIO();
				break;
			case MOONSOUND:
				filtered = !game.isMoonsound();
				break;
			case MIDI:
				filtered = !game.isMIDI();
				break;
			default:
				throw new RuntimeException( "Update filter if Sound contains an extra sound chip" );
		}

		return filtered;
	}
}
