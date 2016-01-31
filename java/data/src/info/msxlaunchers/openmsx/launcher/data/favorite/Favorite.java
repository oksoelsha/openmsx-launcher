/*
 * Copyright 2015 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.data.favorite;

import java.util.Objects;

/**
 * Favorite object - basically a container for a game and database names
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
public final class Favorite
{
	private final String gameName;
	private final String database;

	public Favorite( String gameName, String database )
	{
		this.gameName = validateNonNull( gameName );
		this.database = validateNonNull( database );
	}

	public String getGameName()
	{
		return gameName;
	}

	public String getDatabase()
	{
		return database;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( gameName, database );
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
		Favorite other = (Favorite) obj;
		if( !gameName.equals( other.gameName ) || !database.equals( other.database ) )
		{
			return false;
		}

		return true;
	}

	private <T extends Object> T validateNonNull( T object )
	{
		if( object == null )
		{
			throw new IllegalArgumentException( "Object is null" );
		}

		return object;
	}
}
