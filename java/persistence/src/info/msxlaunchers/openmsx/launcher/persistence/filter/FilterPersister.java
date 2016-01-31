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
package info.msxlaunchers.openmsx.launcher.persistence.filter;

import info.msxlaunchers.openmsx.launcher.data.filter.Filter;

import java.io.IOException;
import java.util.Set;

/**
 * Filter persister interface
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface FilterPersister
{
	/**
	 * Saves the given filter under the given name
	 * 
	 * @param name Filter name
	 * @param filters Set containing filter items
	 * @throws FilterSetAlreadyExistsException
	 * @throws IOException
	 */
	void saveFilter( String name, Set<Filter> filters ) throws FilterSetAlreadyExistsException, IOException;

	/**
	 * Deletes the filter saved under the given name
	 * 
	 * @param name Filter name
	 * @throws IOException
	 */
	void deleteFilter( String name ) throws IOException;

	/**
	 * Returns the filters set saved under the given name
	 * 
	 * @param name Name of filters set
	 * @throws FilterSetNotFoundException
	 * @return Unmodifiable Set containing filter items
	 */
	Set<Filter> getFilter( String name ) throws FilterSetNotFoundException;

	/**
	 * Returns a Set containing names of saved filter sets
	 * 
	 * @return Unmodifiable Set containing filter set names
	 */
	Set<String> getFilterNames();
}
