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
package info.msxlaunchers.openmsx.launcher.starter;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;

import java.util.List;

/**
 * Interface to get the openMSX command line arguments on any OS
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public interface StarterPlatformArguments
{
	/**
	 * Returns openMSX command line arguments that work on the current OS
	 * 
	 * @param settings Settings of the launcher, needed to get the path to openMSX
	 * @param game Game object
	 * @return Unmodifiable List of openMSX command line arguments
	 */
	List<String> getArguments( Settings settings, Game game );
}
