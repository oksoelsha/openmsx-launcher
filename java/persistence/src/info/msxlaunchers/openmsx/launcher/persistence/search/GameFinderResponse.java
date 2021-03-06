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

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.persistence.DatabaseResponse;

import java.util.Set;

/**
 * Class to contain result of <code>GameFinderAction</code> operation
 * 
 * @since v1.6
 * @author Sam Elsharif
 *
 */
final class GameFinderResponse implements DatabaseResponse<Set<DatabaseItem>>
{
	private final Set<DatabaseItem> foundItems;

	GameFinderResponse( Set<DatabaseItem> foundItems )
	{
		this.foundItems = foundItems;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.DatabaseResponse#getResult()
	 */
	@Override
	public Set<DatabaseItem> getResult()
	{
		return foundItems;
	}
}
