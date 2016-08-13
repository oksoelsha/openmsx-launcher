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

import info.msxlaunchers.openmsx.common.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.inject.Inject;

/**
 * Implementation of the <code>EmulatorStarter</code> interface
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class OpenMSXStarter implements EmulatorStarter
{
	private final StarterPlatformArguments platformArguments;
	
	@Inject
	OpenMSXStarter( StarterPlatformArguments platformArguments )
	{
		this.platformArguments = Objects.requireNonNull( platformArguments );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.starter.EmulatorStarter#start(info.msxlaunchers.openmsx.launcher.data.global.Settings, info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public Process start( Settings settings, Game game ) throws IOException
	{
		Objects.requireNonNull( settings );
		Objects.requireNonNull( game );

		List<String> arguments = platformArguments.getArguments( settings, game );

		LauncherLogger.logMessage( arguments.stream().collect( Collectors.joining( " " ) ) );

		ProcessBuilder pb = new ProcessBuilder( arguments );

		return pb.start();
	}
}
