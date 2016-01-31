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
import info.msxlaunchers.openmsx.launcher.data.game.constants.MSXGeneration;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

/**
 * Filter for MSX generation
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@FilterDescriptor( type = FilterType.GENERATION )
final class GenerationFilter implements Filter
{
	@Value1Field
	private final MSXGeneration generation;

	GenerationFilter( MSXGeneration generation )
	{
		this.generation = Objects.requireNonNull( generation );
	}

	@Override
	public int hashCode()
	{
		return generation.hashCode();
	}

	@Override
	public boolean equals( Object obj )
	{
		boolean isSame;
		
		if( obj == null || !(obj instanceof GenerationFilter) )
		{
			isSame = false;
		}
		else
		{
			isSame = generation.equals( ((GenerationFilter)obj).generation );
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

		switch( generation )
		{
			case MSX:
				filtered = !game.isMSX();
				break;
			case MSX2:
				filtered = !game.isMSX2();
				break;
			case MSX2Plus:
				filtered = !game.isMSX2Plus();
				break;
			case TURBO_R:
				filtered = !game.isTurboR();
				break;
			default:
				throw new RuntimeException( "Update filter if MSXGeneration contains an extra generation" );
		}

		return filtered;
	}
}
