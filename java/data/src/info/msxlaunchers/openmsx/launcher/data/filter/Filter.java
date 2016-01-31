/*
 * Copyright 2013 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.data.filter;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

/**
 * Game filter interface
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface Filter
{
	/**
	 * Returns whether the given game needs to be filtered according to some condition
	 * 
	 * @param game Game object - cannot be null
	 * @param repositoryGame Repository Game object - can be null
	 * @return true if the given game is to be filtered
	 */
	boolean isFiltered( Game game, RepositoryGame repositoryGame );
}
