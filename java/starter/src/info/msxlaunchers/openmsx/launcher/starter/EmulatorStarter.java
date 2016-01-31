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

import java.io.IOException;

/**
 * Interface to start openMSX
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public interface EmulatorStarter
{
	/**
	 * Starts openMSX with the given <code>Game</code>
	 * 
	 * @param settings Settings of the launcher, needed to get the path to openMSX
	 * @param game Game object
	 * @return Instance of Process that represents the openMSX process
	 * @throws IOException
	 */
	Process start( Settings settings, Game game ) throws IOException;
}
