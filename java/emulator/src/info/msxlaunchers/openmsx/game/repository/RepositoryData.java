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
package info.msxlaunchers.openmsx.game.repository;

import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Interface to get data from openMSX game database
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public interface RepositoryData
{
	/**
	 * Returns unmodifiable Map of hash codes to <code>RepositoryGame</code> objects
	 * 
	 * @return Unmodifiable Map of hash codes to RepositoryGame objects, or null if there's no data
	 * @throws IOException
	 */
	Map<String,RepositoryGame> getRepositoryInfo() throws IOException;

	/**
	 * Returns unmodifiable Set containing hash codes for all dumps for a given hash code
	 * 
	 * @param code Hash code
	 * @return Unmodifiable Set containing hash codes for all dumps for a given hash code
	 * @throws IOException
	 */
	Set<String> getDumpCodes( String code ) throws IOException;

	/**
	 * Returns a <code>RepositoryGame</code> object
	 * 
	 * @param code Hash code
	 * @return RepositoryGame object or null if given hash code was not found
	 * @throws IOException
	 */
	RepositoryGame getGameInfo( String code ) throws IOException;
}
