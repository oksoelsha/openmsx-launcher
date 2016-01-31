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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import java.util.Objects;

/**
 * Class to hold data about the database
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
public final class DatabaseInfo
{
	private final int totalDatabases;
	private final int totalGames;
	private final int totalBackups;

	public DatabaseInfo( int totalDatabases, int totalGames, int totalBackups )
	{
		this.totalDatabases = totalDatabases;
		this.totalGames = totalGames;
		this.totalBackups = totalBackups;
	}

	public int getTotalDatabases()
	{
		return totalDatabases;
	}

	public int getTotalGames()
	{
		return totalGames;
	}

	public int getTotalBackups()
	{
		return totalBackups;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( totalDatabases, totalGames, totalBackups );
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
		DatabaseInfo other = (DatabaseInfo) obj;
		if( totalDatabases != other.getTotalDatabases() || totalGames != other.getTotalGames() || totalBackups != other.getTotalBackups() )
		{
			return false;
		}

		return true;
	}
}
