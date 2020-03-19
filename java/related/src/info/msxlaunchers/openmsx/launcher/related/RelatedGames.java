/*
 * Copyright 2020 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.related;

import java.io.IOException;
import java.util.List;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;

/**
 * Interface for finding games related to the given one
 * 
 * @author Sam Elsharif
 * @since v1.13
 */
public interface RelatedGames
{
	/**
	 * Find related games to the given game
	 * 
	 * @param game Game
	 * @throws IOException
	 * @return List of related games to given one sorted by "proximity". List size is to be determined by implementation
	 */
	List<RelatedGame> findRelated( Game game ) throws IOException;
}
