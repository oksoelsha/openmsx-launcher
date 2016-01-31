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

import info.msxlaunchers.openmsx.common.OSUtils;
import info.msxlaunchers.platform.ArgumentsBuilderProvider;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class PlatformArgumentsProvider implements Provider<StarterPlatformArguments>
{
	private final ArgumentsBuilderProvider argumentsBuilderProvider;

	@Inject
	PlatformArgumentsProvider( ArgumentsBuilderProvider argumentsBuilderProvider )
	{
		this.argumentsBuilderProvider = argumentsBuilderProvider;
	}

	@Override
	public StarterPlatformArguments get()
	{
		if( OSUtils.isWindows() )
		{
			return new WindowsStarterArguments( argumentsBuilderProvider.get() );
		}
		else if( OSUtils.isMac() )
		{
			return new MacStarterArguments( argumentsBuilderProvider.get() );
		}
		else if( OSUtils.isLinux() || OSUtils.isBSD() )
		{
			return new LinuxBSDStarterArguments( argumentsBuilderProvider.get() );
		}
		else
		{
			throw new IllegalArgumentException( "Unsupported Operating System" );
		}
	}
}
