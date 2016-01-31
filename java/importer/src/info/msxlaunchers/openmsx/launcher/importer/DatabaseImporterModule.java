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

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public class DatabaseImporterModule extends AbstractModule
{
	@Override 
	protected void configure()
	{
		install( new FactoryModuleBuilder().implement( DatabaseImporter.class, BlueMSXLauncherDatabaseImporter.class ).build( DatabaseImporterFactory.class ) );
	}
}