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
package info.msxlaunchers.openmsx.launcher.data.game;

import java.util.Objects;

/**
 * Container for a game and database names
 * 
 * @since v1.6
 * @author Sam Elsharif
 *
 */
public final class DatabaseItem
{
	private final String gameName;
	private final String database;

	public DatabaseItem( String gameName, String database )
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
		DatabaseItem other = (DatabaseItem) obj;
		if( !gameName.equals( other.gameName ) || !database.equals( other.database ) )
		{
			return false;
		}

		return true;
	}

	/**
	 * Return an instance of DatabaseItem for the given string of the format gamename[database]
	 * 
	 * @param databaseItemString String of the format gamename[database]
	 * @return Instance of DatabaseItem from the given string. If input is null or incorrect, return an instance with empty fields
	 */
	public static DatabaseItem getDatabaseItem( String databaseItemString )
	{
		DatabaseItem databaseItem;

		if( databaseItemString == null )
		{
			databaseItem = new DatabaseItem( "", "" );
		}
		else
		{
			int lastClosingBracketIndex = databaseItemString.lastIndexOf( ']' );
			if( lastClosingBracketIndex > -1 )
			{
				int lastOpeningBracketIndex = databaseItemString.lastIndexOf( '[', lastClosingBracketIndex );

				if( lastOpeningBracketIndex > -1 &&  lastOpeningBracketIndex < lastClosingBracketIndex )
				{
					String gameName = databaseItemString.substring( 0, lastOpeningBracketIndex ).trim();
					String database = databaseItemString.substring( lastOpeningBracketIndex + 1, lastClosingBracketIndex );

					databaseItem = new DatabaseItem( gameName, database );
				}
				else
				{
					databaseItem = new DatabaseItem( "", "" );
				}
			}
			else
			{
				databaseItem = new DatabaseItem( "", "" );
			}
		}

		return databaseItem;
	}

	@Override
	public String toString()
	{
		return gameName + " [" + database + "]";
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
