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
package info.msxlaunchers.openmsx.launcher.data.backup;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Database Backup object - a container for a database backup
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
public final class DatabaseBackup
{
	private final String database;
	private final Timestamp timestamp;

	public DatabaseBackup( String database, Timestamp timestamp )
	{
		this.database = Objects.requireNonNull( database );
		this.timestamp = Objects.requireNonNull( timestamp );
	}

	public String getDatabase()
	{
		return database;
	}

	public Timestamp getTimestamp()
	{
		return timestamp;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( database, timestamp );
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
		DatabaseBackup other = (DatabaseBackup) obj;
		if( !timestamp.equals( other.timestamp ) || !database.equals( other.database ) )
		{
			return false;
		}

		return true;
	}
}
