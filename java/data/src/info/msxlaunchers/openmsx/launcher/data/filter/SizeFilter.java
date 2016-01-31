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
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

/**
 * Filter for Size
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@FilterDescriptor( type = FilterType.SIZE )
final class SizeFilter implements Filter
{
	@Value1Field
	private final long size1;

	@Value2Field
	private final long size2;

	@ParameterField
	private final FilterParameter filterParameter;

	SizeFilter( long size1, long size2, FilterParameter filterParameter )
	{
		this.size1 = size1;
		this.size2 = size2;
		this.filterParameter = Objects.requireNonNull( filterParameter );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( size1, size2, filterParameter );
	}

	@Override
	public boolean equals( Object obj )
	{
		if( this == obj )
		{
			return true;
		}
		if( obj == null )
		{
			return false;
		}
		if( getClass() != obj.getClass() )
		{
			return false;
		}
		SizeFilter other = (SizeFilter) obj;
		if( filterParameter != other.filterParameter || size1 != other.size1 || size2 != other.size2 )
		{
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.filter.Filter#isFiltered(info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public boolean isFiltered( Game game, RepositoryGame repositoryGame )
	{
		Objects.requireNonNull( game );

		boolean filtered = false;

		switch( filterParameter )
		{
			case EQUAL:
				filtered = game.getSize() != size1;
				break;
			case EQUAL_OR_LESS:
				filtered = game.getSize() > size1;
				break;
			case EQUAL_OR_GREATER:
				filtered = game.getSize() < size1;
				break;
			case LESS:
				filtered = game.getSize() >= size1;
				break;
			case GREATER:
				filtered = game.getSize() <= size1;
				break;
			case BETWEEN_INCLUSIVE:
				filtered = game.getSize() < size1 || game.getSize() > size2;
				break;
		}

		return filtered;
	}
}
