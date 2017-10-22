/*
 * Copyright 2015 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.persistence;

import java.io.File;

import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersisterModule;
import info.msxlaunchers.openmsx.launcher.persistence.feed.FeedMessagePersisterModule;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterPersisterModule;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersisterModule;
import info.msxlaunchers.openmsx.launcher.persistence.search.GameFinderModule;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersisterModule;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * @since v1.4
 * @author Sam Elsharif
 *
 */
public class LauncherPersistenceModule extends AbstractModule
{
	private static final String DATABASES_DIRECTORY_NAME = "databases";
	private static final String EMBEDDED_DATABASE_NAME = "launcherdb";

	@Override
	protected void configure()
	{
		bind( String.class ).annotatedWith( Names.named( "DatabasesDirectoryName" ) ).toInstance( DATABASES_DIRECTORY_NAME );
		bind( String.class ).annotatedWith( Names.named( "EmbeddedDatabaseFullPath" ) ).toProvider( EmbeddedDatabaseFullPathProvider.class );

		bind( LauncherPersistence.class ).to( EmbeddedDatabaseLauncherPersistence.class ).in( Singleton.class );

		install( new GamePersisterModule() );
		install( new FilterPersisterModule() );
		install( new FavoritePersisterModule() );
		install( new SettingsPersisterModule() );
		install( new GameFinderModule() );
		install( new FeedMessagePersisterModule() );
	}

	private static class EmbeddedDatabaseFullPathProvider implements Provider<String>
	{
		private final String userDataDirectory;

		@Inject
		EmbeddedDatabaseFullPathProvider( @Named("UserDataDirectory") String userDataDirectory )
		{
			this.userDataDirectory = userDataDirectory;
		}

		@Override
		public String get()
		{
			return new File( new File( userDataDirectory, DATABASES_DIRECTORY_NAME ), EMBEDDED_DATABASE_NAME ).toString();
		}
	}
}