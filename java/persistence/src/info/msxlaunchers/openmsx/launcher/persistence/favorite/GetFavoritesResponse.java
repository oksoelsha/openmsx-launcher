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
import info.msxlaunchers.openmsx.launcher.persistence.DatabaseResponse;

import java.util.Set;

/**
 * Class to contain result of <code>GetFavoritesAction</code> operation
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class GetFavoritesResponse implements DatabaseResponse<Set<Favorite>>
{
	private final Set<Favorite> favorites;

	GetFavoritesResponse( Set<Favorite> favorites )
	{
		this.favorites = favorites;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.DatabaseResponse#getResult()
	 */
	@Override
	public Set<Favorite> getResult()
	{
		return favorites;
	}
}
