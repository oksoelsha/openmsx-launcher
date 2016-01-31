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

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

/**
 * Filter for year. The year string is obtained from openMSX's software database
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@FilterDescriptor( type = FilterType.YEAR )
final class YearFilter implements Filter
{
	@Value1Field
	private final int year1;

	@Value2Field
	private final int year2;

	@ParameterField
	private final FilterParameter filterParameter;

	YearFilter( int year1, int year2, FilterParameter filterParameter )
	{
		this.year1 = year1;
		this.year2 = year2;
		this.filterParameter = Objects.requireNonNull( filterParameter );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( year1, year2, filterParameter );
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
		YearFilter other = (YearFilter) obj;
		if( filterParameter != other.filterParameter || year1 != other.year1 || year2 != other.year2 )
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

		if( repositoryGame == null )
		{
			filtered = true;
		}
		else
		{
			int year = Utils.getNumber( repositoryGame.getYear() );
			switch( filterParameter )
			{
				case EQUAL:
					filtered = year != year1;
					break;
				case EQUAL_OR_LESS:
					filtered = year > year1;
					break;
				case EQUAL_OR_GREATER:
					filtered = year < year1;
					break;
				case LESS:
					filtered = year >= year1;
					break;
				case GREATER:
					filtered = year <= year1;
					break;
				case BETWEEN_INCLUSIVE:
					filtered = year < year1 || year > year2;
					break;
			}
		}

		return filtered;
	}
}
