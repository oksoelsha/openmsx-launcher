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
import info.msxlaunchers.platform.ArgumentsBuilder;

import java.util.List;

import com.google.inject.Inject;

/**
 * Implementation of <code>StarterPlatformArguments</code> for Windows
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class WindowsStarterArguments extends AbstractStarterPlatformArguments
{
	private final ArgumentsBuilder argumentsBuilder;

	@Inject
	WindowsStarterArguments( ArgumentsBuilder argumentsBuilder )
	{
		this.argumentsBuilder = argumentsBuilder;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.starter.StarterPlatformArguments#getArguments(info.msxlaunchers.openmsx.launcher.data.global.Settings, info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public List<String> getArguments( Settings settings, Game game )
	{
		buildArguments( settings.getOpenMSXFullPath(), "openmsx.exe", argumentsBuilder, game );

		return argumentsBuilder.getArgumentList();
	}
}
