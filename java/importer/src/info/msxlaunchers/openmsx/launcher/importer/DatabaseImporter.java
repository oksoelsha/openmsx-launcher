/*
 * Copyright 2014 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.importer;

import info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Interface to import databases
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public interface DatabaseImporter
{
	/**
	 * Imports data from given database files into openMSX Launcher format
	 * 
	 * @param databases Databases to import
	 * @param actionDecider Reference to an ActionDecider implementation that prompts the caller for actions when imported database name exists already
	 * @return Unmodified Set containing names of databases imported successfully 
	 * @throws IOException
	 */
	Set<String> importDatabases( File[] databases, ActionDecider actionDecider ) throws IOException;
}
