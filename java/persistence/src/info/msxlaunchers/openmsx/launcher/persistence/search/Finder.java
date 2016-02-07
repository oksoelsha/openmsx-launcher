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

import java.util.Set;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;

/**
 * Search interface
 * 
 * @since v1.6
 * @author Sam Elsharif
 *
 */
public interface Finder
{
	/**
	 * Returns a Set of matches for the entered string, up to a maximum, or an empty Set if no matches
	 * 
	 * @param string String to search (in any field in the database)
	 * @param maximumMatches Maximum number of matches to return
	 */
	Set<DatabaseItem> find( String string, int maximumMatches );
}
