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
 * Filter for company. The company string is obtained from openMSX's software database
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@FilterDescriptor( type = FilterType.COMPANY )
final class CompanyFilter implements Filter
{
	@Value1Field
	private final String company;

	CompanyFilter( String company )
	{
		this.company = Objects.requireNonNull( company );
	}

	@Override
	public int hashCode()
	{
		return company.hashCode();
	}

	@Override
	public boolean equals( Object obj )
	{
		boolean isSame;
		
		if( obj == null || !(obj instanceof CompanyFilter) )
		{
			isSame = false;
		}
		else
		{
			isSame = company.equals( ((CompanyFilter)obj).company );
		}

		return isSame;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.filter.Filter#isFiltered(info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public boolean isFiltered( Game game, RepositoryGame repositoryGame )
	{
		return repositoryGame == null || !company.equals( repositoryGame.getCompany() );
	}
}
