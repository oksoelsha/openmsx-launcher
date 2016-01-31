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
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

/**
 * Filter for Genre
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@FilterDescriptor( type = FilterType.GENRE )
final class GenreFilter implements Filter
{
	@Value1Field
	private final Genre genre;

	GenreFilter( Genre genre )
	{
		this.genre = Objects.requireNonNull( genre );
	}

	@Override
	public int hashCode()
	{
		return genre.hashCode();
	}

	@Override
	public boolean equals( Object obj )
	{
		boolean isSame;
		
		if( obj == null || !(obj instanceof GenreFilter) )
		{
			isSame = false;
		}
		else
		{
			isSame = genre.equals( ((GenreFilter)obj).genre );
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

		return !genre.equals( game.getGenre1() ) && !genre.equals( game.getGenre2() );
	}
}
