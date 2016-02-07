/*
 * Copyright 2016 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.persistence.search;

import java.util.Collections;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;

/**
 * Implementation of the <code>Finder</code> interface that searches an embedded database.
 * 
 * @since v1.6
 * @author Sam Elsharif
 *
 */
final class EmbeddedDatabaseFinder implements Finder
{
	private final String databaseFullPath;

	@Inject
	EmbeddedDatabaseFinder( @Named("EmbeddedDatabaseFullPath") String databaseFullPath )
	{
		this.databaseFullPath = databaseFullPath;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.search.Finder#find(java.lang.String, int)
	 */
	@Override
	public Set<DatabaseItem> find( String string, int maximumMatches )
	{
		try
		{
			return new FindAction( string, maximumMatches ).execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException lpe )
		{
			//ignore - just return an empty set
		}

		return Collections.emptySet();
	}
}
