/*
 * Copyright 2020 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.data.game;

import java.util.Objects;

/**
 * Container for a related game
 * 
 * @since v1.13
 * @author Sam Elsharif
 *
 */
public final class RelatedGame
{
	private final String gameName;
	private final String company;
	private final int msxGenId;

	public RelatedGame( String gameName, String company, int msxGenId )
	{
		this.gameName = Objects.requireNonNull( gameName );
		this.company = company;
		this.msxGenId = msxGenId;
	}

	public String getGameName()
	{
		return gameName;
	}

	public String getCompany()
	{
		return company;
	}

	public int getMSXGenId()
	{
		return msxGenId;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( gameName, msxGenId );
	}

	@Override
	public boolean equals( Object obj )
	{
		if( this == obj )
		{
			return true;
		}
		if( obj == null || getClass() != obj.getClass() )
		{
			return false;
		}

		RelatedGame other = (RelatedGame) obj;
		return gameName.equals( other.gameName ) && msxGenId == other.msxGenId;
	}
}
