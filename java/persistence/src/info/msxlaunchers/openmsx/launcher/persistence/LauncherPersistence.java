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

import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersister;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterPersister;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.persistence.search.GameFinder;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;

/**
 * Interface to persistence setup operations and individual persisters. The individual persisters can also be
 * injected directly though. The purpose of the interface is to allow to inject all persisters in one "container"
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
public interface LauncherPersistence
{
	/**
	 * Initialises the persistence system if needed. This method must be called once at the start of the application
	 * before any access to the persistence system
	 * 
	 * @throws LauncherPersistenceException
	 */
	void initialize() throws LauncherPersistenceException;

	/**
	 * Shuts down the persistence system
	 * 
	 * @throws LauncherPersistenceException
	 */
	void shutdown() throws LauncherPersistenceException;

	/**
	 * Returns the games persister
	 * 
	 * @return Reference to the games persister
	 */
	GamePersister getGamePersister();

	/**
	 * Returns the favorites persister
	 * 
	 * @return Reference to the favorites persister
	 */
	FavoritePersister getFavoritePersister();

	/**
	 * Returns the filters persister
	 * 
	 * @return Reference to the filters persister
	 */
	FilterPersister getFiltersPersister();

	/**
	 * Returns the settings persister
	 * 
	 * @return Reference to the settings persister
	 */
	SettingsPersister getSettingsPersister();

	/**
	 * Returns the database game finder
	 * 
	 * @return Reference to the GameFinder
	 */
	GameFinder getGameFinder();
}
