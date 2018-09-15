/*
 * Copyright 2016 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.log.analyser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;

/**
 * Implementation of the interface <code>LogFileReader</code> that reads log files including the backups
 * 
 * @since v1.8
 * @author Sam Elsharif
 */
final class LogFileReader implements LogReader
{
	private final String logDirectory;

	@Inject
	LogFileReader( @Named("UserDataDirectory") String logDirectory )
	{
		this.logDirectory = Objects.requireNonNull( logDirectory );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.log.analyser.LogReader#read()
	 */
	@Override
	public List<String> read()
	{
		List<String> lines = null;

		//first read the rotated log file - the launcher logging is configured to use a single backup
		try
		{
			lines = Files.readAllLines( Paths.get( logDirectory, LauncherLogger.MESSAGE_LOG_FILENAME + ".1" ) );
		}
		catch( IOException ioe )
		{
			//a 50kb log file contains approximately 500-550 lines. Set the initial list size to a little bit more
			lines = new ArrayList<>( 600 );
		}

		//second read the current log file
		try
		{
			List<String> moreLines = Files.readAllLines( Paths.get( logDirectory, LauncherLogger.MESSAGE_LOG_FILENAME + ".0" ) );
			lines.addAll( moreLines );
		}
		catch( IOException ioe )
		{
			//this means that no log files were found. the variable lines is already set to an empty list, so proceed
		}

		return Collections.unmodifiableList( lines );
	}
}