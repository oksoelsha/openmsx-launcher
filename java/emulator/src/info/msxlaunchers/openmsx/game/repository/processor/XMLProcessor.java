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
package info.msxlaunchers.openmsx.game.repository.processor;

import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Interface for openMSX softwaredb XML processing 
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public interface XMLProcessor
{
	/**
	 * Returns an unmodifiable Map with hash code as the key and a <code>RepositoryGame</code> object as the value for
	 * all games in a given XML file
	 * 
	 * @param xmlFile Full path to the XML file
	 * @return Unmodifiable Map of hash codes to RepositoryGame objects
	 * @throws IOException if given XML file cannot be accessed
	 */
	Map<String,RepositoryGame> getRepositoryInfo( File xmlFile ) throws IOException;

	/**
	 * Returns unmodifiable Set containing hash codes for all dumps for a given hash code in a given softwaredb XML file
	 * 
	 * @param xmlFile Full path to the XML file
	 * @param code Hash code
	 * @return Unmodifiable Set containing hash codes for all dumps
	 * @throws IOException if given XML file cannot be accessed
	 */
	Set<String> getDumpCodes( File xmlFile, String code ) throws IOException;

	/**
	 * Returns a <code>RepositoryGame</code> object
	 * 
	 * @param xmlFile Full path to the XML file
	 * @param code Hash code
	 * @return RepositoryGame object or null if given hash code was not found in the given XML file
	 * @throws IOException if given XML file cannot be accessed
	 */
	RepositoryGame getGameInfo( File xmlFile, String code ) throws IOException;
}
