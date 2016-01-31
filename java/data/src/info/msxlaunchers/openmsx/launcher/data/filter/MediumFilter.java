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
import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

/**
 * Filter for Medium
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@FilterDescriptor( type = FilterType.MEDIUM )
final class MediumFilter implements Filter
{
	@Value1Field
	private final Medium medium;

	MediumFilter( Medium medium )
	{
		this.medium = Objects.requireNonNull( medium );
	}

	@Override
	public int hashCode()
	{
		return medium.hashCode();
	}

	@Override
	public boolean equals( Object obj )
	{
		boolean isSame;
		
		if( obj == null || !(obj instanceof MediumFilter) )
		{
			isSame = false;
		}
		else
		{
			isSame = medium.equals( ((MediumFilter)obj).medium );
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

		switch( medium )
		{
			case ROM:
				filtered = !game.isROM();
				break;
			case DISK:
				filtered = !game.isDisk();
				break;
			case TAPE:
				filtered = !game.isTape();
				break;
			case HARDDISK:
				filtered = !game.isHarddisk();
				break;
			case LASERDISC:
				filtered = !game.isLaserdisc();
				break;
			case SCRIPT:
				filtered = !game.isScript();
				break;
			default:
				throw new RuntimeException( "Update filter if Medium contains an extra medium" );
		}

		return filtered;
	}
}
