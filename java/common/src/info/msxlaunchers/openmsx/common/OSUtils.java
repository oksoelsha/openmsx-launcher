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
package info.msxlaunchers.openmsx.common;

/**
 * Utility class that contains methods to determine the OS where the JRE is running
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public final class OSUtils
{
	private final static String OS = System.getProperty( "os.name" );

	/**
	 * Returns if the current OS is Windows
	 * 
	 * @return true if the OS is Windows
	 */
	public static boolean isWindows()
	{
		return OS.startsWith( "Windows" );
	}

	/**
	 * Returns if the current OS is Linux
	 * 
	 * @return true if the OS is Linux
	 */
	public static boolean isLinux()
	{
		return OS.startsWith( "Linux" );
	}

	/**
	 * Returns if the current OS is MacOS
	 * 
	 * @return true if the OS is MacOS
	 */
	public static boolean isMac()
	{
		return OS.startsWith( "Mac" );
	}

	/**
	 * Returns if the current OS is BSD
	 * 
	 * @return true if the OS is BSD
	 */
	public static boolean isBSD()
	{
		return OS.startsWith( "FreeBSD" ) || OS.startsWith( "NetBSD" ) || OS.startsWith( "OpenBSD" );
	}

	/**
	 * Returns if the current OS is derived from/based on Unix
	 * 
	 * @return true if the OS is derived from/based on Unix
	 */
	public static boolean isUnixFamily()
	{
		return isLinux() || isMac()|| isBSD();
	}
}
