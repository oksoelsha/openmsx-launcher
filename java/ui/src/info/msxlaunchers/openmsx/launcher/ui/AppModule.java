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
package info.msxlaunchers.openmsx.launcher.ui;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import info.msxlaunchers.openmsx.common.OSUtils;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;

/**
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public class AppModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind( String.class ).annotatedWith( Names.named( "UserDataDirectory" ) ).toInstance( getUserDataDirectory() );
		bind( String.class ).annotatedWith( Names.named( "LauncherDataDirectory" ) ).toInstance( getLauncherDataDirectory() );
		bind( String.class ).annotatedWith( Names.named( "JarFilesDirectory" ) ).toInstance( getJarFilesDirectory() );
		bind( String.class ).annotatedWith( Names.named( "BaseDirectory" ) ).toProvider( Providers.<String>of( null ) );
	}

	private String getUserDataDirectory()
	{
		String userDataDirectory = OSUtils.getUserDataDirectory();

		//before anything, make sure user data directories exist
		createUserDataDirectoryIfNecessary( userDataDirectory );

		return userDataDirectory;
	}

	private String getLauncherDataDirectory()
	{
		String launcherDataDirectory = null;

		if( OSUtils.isWindows() )
		{
			launcherDataDirectory = System.getProperty( "user.dir" );
		}
		else if( OSUtils.isMac() )
		{
	        try
			{
				String appDir = (String)Class.forName( "com.apple.eio.FileManager" )
						.getMethod( "getPathToApplicationBundle", (Class[])null ).invoke( null, (Object[]) null );
				launcherDataDirectory = new File( appDir, "share" ).getAbsolutePath();
			}
			catch( IllegalAccessException | IllegalArgumentException| InvocationTargetException | NoSuchMethodException
					| SecurityException | ClassNotFoundException e )
			{
				//if this fails on MacOS then the application will not function well
			}
		}
		else if( OSUtils.isLinux() || OSUtils.isBSD() )
		{
			launcherDataDirectory = System.getProperty( "user.dir" );
		}

		return launcherDataDirectory;
	}

	private String getJarFilesDirectory()
	{
		String jarFilesDirectory = null;

		if( OSUtils.isWindows() )
		{
			jarFilesDirectory = new File( System.getProperty( "user.dir" ), "lib" ).getAbsolutePath();
		}
		else if( OSUtils.isMac() )
		{
	        try
			{
				String appDir = (String)Class.forName( "com.apple.eio.FileManager" )
						.getMethod( "getPathToApplicationBundle", (Class[])null ).invoke( null, (Object[]) null );
				jarFilesDirectory = new File( appDir, "Contents/Java" ).getAbsolutePath();
			}
			catch( IllegalAccessException | IllegalArgumentException| InvocationTargetException | NoSuchMethodException
					| SecurityException | ClassNotFoundException e )
			{
				//if this fails on MacOS then the application will not function well
			}
		}
		else if( OSUtils.isLinux() || OSUtils.isBSD() )
		{
			jarFilesDirectory = new File( System.getProperty( "user.dir" ), "lib" ).getAbsolutePath();
		}

		return jarFilesDirectory;
	}

	private void createUserDataDirectoryIfNecessary( String userDataDirectory )
	{
		File userDataDirectoryPath = new File( userDataDirectory );

		if( !userDataDirectoryPath.exists() )
		{
			userDataDirectoryPath.mkdir();
		}
	}
}