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
package info.msxlaunchers.platform;

import info.msxlaunchers.openmsx.common.OSUtils;

import com.google.inject.Provider;

/**
 * @since v1.1
 * @author Sam Elsharif
 *
 */
public final class ArgumentsBuilderProvider implements Provider<ArgumentsBuilder>
{
	@Override
	public ArgumentsBuilder get()
	{
		if( OSUtils.isWindows() )
		{
			return new WindowsArgumentsBuilder();
		}
		else if( OSUtils.isUnixFamily() )
		{
			return new UnixFamilyArgumentsBuilder();
		}
		else
		{
			throw new IllegalArgumentException( "Unsupported Operating System" );
		}
	}
}
