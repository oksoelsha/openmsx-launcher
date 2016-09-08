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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.log.LogEvent;

/**
 * Implementation of the interface <code>LogAnalyser</code> that reads a log file and calls corresponding processor
 * on each line based on the log event in the line
 * 
 * @since v1.8
 * @author Sam Elsharif
 */
final class LogAnalyserImpl implements LogAnalyser
{
	private final LogReader logReader;
	private final Map<LogEvent,LogProcessor> logProcessors;

	@Inject
	LogAnalyserImpl( LogReader logReader, Map<LogEvent,LogProcessor> logProcessors )
	{
		this.logReader = logReader;
		this.logProcessors = logProcessors;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.log.analyser.LogAnalyser#getProcessedData()
	 */
	@Override
	public Map<String,List<String[]>> getProcessedData()
	{
		List<String> logLines = logReader.read();

		Map<String,List<String[]>> processedData = new HashMap<>();

		for( String line: logLines )
		{
			LogEvent logEvent = getLogEvent( line );

			LogProcessor logProcessor = logProcessors.get( logEvent );

			if( logProcessor != null )
			{
				String[] lineDateAndLogMessage = getLineDateAndMessage( line, logEvent );

				logProcessor.processMessage( lineDateAndLogMessage[0], lineDateAndLogMessage[1] );
			}
		}

		//at this point collect all processed data
		for( LogProcessor logProcessor: logProcessors.values() )
		{
			processedData.putAll( logProcessor.getProcessedData() );
		}

		return processedData;
	}

	private LogEvent getLogEvent( String line )
	{
		LogEvent event = null;

		if( !Utils.isEmpty( line ) )
		{
			int firstIndex = line.indexOf( ": " ) + 2;
			int secondIndex = line.indexOf( " ", firstIndex );

			if( secondIndex > firstIndex )
			{
				String eventString = line.substring( firstIndex, secondIndex );
		
				try
				{
					event = LogEvent.valueOf( eventString );
				}
				catch( IllegalArgumentException | NullPointerException ex )
				{
					//ignore - just return null
				}
			}
		}

		return event;
	}

	/**
	 * This method will only be called if the line contains a valid event (i.e. only after getLogEvent() returns an existing event
	 */
	private String[] getLineDateAndMessage( String line, LogEvent logEvent )
	{
		String splitString = ": " + logEvent + " ";
		int firstIndex = line.indexOf( splitString );
		int secondIndex = firstIndex + splitString.length();

		return new String[] { line.substring( 0, firstIndex ), line.substring( secondIndex, line.length() ) };
	}
}