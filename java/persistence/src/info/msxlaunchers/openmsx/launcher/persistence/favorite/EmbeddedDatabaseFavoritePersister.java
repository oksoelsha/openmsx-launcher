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
package info.msxlaunchers.openmsx.launcher.persistence.favorite;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;

import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation of the <code>FavoritePersister</code> interface that persists favorites and retrieves them from an embedded
 * database.
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
@Singleton
final class EmbeddedDatabaseFavoritePersister implements FavoritePersister
{
	private final String databaseFullPath;

	@Inject
	EmbeddedDatabaseFavoritePersister( @Named("EmbeddedDatabaseFullPath") String databaseFullPath )
	{
		this.databaseFullPath = databaseFullPath;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersister#addFavorite(info.msxlaunchers.openmsx.launcher.data.favorite.Favorite)
	 */
	@Override
	public void addFavorite( DatabaseItem favorite ) throws FavoritePersistenceException
	{
		Objects.requireNonNull( favorite );

		try
		{
			new SaveFavoriteAction( favorite ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (FavoritePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersister#deleteFavorite(info.msxlaunchers.openmsx.launcher.data.favorite.Favorite)
	 */
	@Override
	public void deleteFavorite( DatabaseItem favorite ) throws FavoritePersistenceException
	{
		Objects.requireNonNull( favorite );

		try
		{
			new DeleteFavoriteAction( favorite ).execute( databaseFullPath );
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (FavoritePersistenceException)lpe.getException();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersister#getFavorites()
	 */
	@Override
	public Set<DatabaseItem> getFavorites()
	{
		try
		{
			return new GetFavoritesAction().execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException lpe )
		{
			//ignore - just return an empty set
		}
		return null;
	}
}
