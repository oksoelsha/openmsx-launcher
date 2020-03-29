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
import java.util.Optional;

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
	private final String year;
	private final int msxGenId;
	private final DatabaseItem databaseItem;

	public RelatedGame( String gameName, String company, String year, int msxGenId )
	{
		this( gameName, company, year, msxGenId, null );
	}

	public RelatedGame( String gameName, String company, String year, int msxGenId, DatabaseItem databaseItem )
	{
		this.gameName = Objects.requireNonNull( gameName );
		this.company = company;
		this.year = year;
		this.msxGenId = msxGenId;
		this.databaseItem = databaseItem;
	}

	public String getGameName()
	{
		return gameName;
	}

	public String getCompany()
	{
		return company;
	}

	public String getYear()
	{
		return year;
	}

	public int getMSXGenId()
	{
		return msxGenId;
	}

	public Optional<DatabaseItem> getDatabaseItem()
	{
		return Optional.ofNullable( databaseItem );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( msxGenId );
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

		return msxGenId == ((RelatedGame)obj).msxGenId;
	}
}
