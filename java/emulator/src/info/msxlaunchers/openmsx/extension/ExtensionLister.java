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
package info.msxlaunchers.openmsx.extension;

import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;

import java.io.IOException;
import java.util.Set;

/**
 * Interface for listing openMSX's extensions
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public interface ExtensionLister
{
	/**
	 * Returns Set containing openMSX extensions
	 * 
	 * @return Unmodifiable Set containing openMSX extensions. If no extensions are found, then an empty Set is returned 
	 * @throws InvalidMachinesDirectoryException if directory is invalid
	 */
	Set<String> get() throws InvalidMachinesDirectoryException, IOException;
}
