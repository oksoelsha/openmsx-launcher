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

import info.msxlaunchers.openmsx.launcher.data.favorite.Favorite;

import java.util.Set;

/**
 * Favorite persister interface
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
public interface FavoritePersister
{
	/**
	 * Saves the given favorite in the database
	 * 
	 * @param favorite Favorite object
	 * @throws
	 */
	void addFavorite( Favorite favorite ) throws FavoritePersistenceException;

	/**
	 * Deletes the given favorite from the database
	 * 
	 * @param favorite Favorite object
	 * @throws
	 */
	void deleteFavorite( Favorite favorite ) throws FavoritePersistenceException;

	/**
	 * Returns a Set containing all favorites in the database
	 * 
	 * @return Unmodifiable Set containing favorites
	 */
	Set<Favorite> getFavorites();
}
