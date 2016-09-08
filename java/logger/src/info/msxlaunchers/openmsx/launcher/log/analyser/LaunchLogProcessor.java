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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.log.analyser.processor.LaunchEventProcessor;

/**
 * Implementation that extends <code>AbstractLogProcessor</code> that passes all registered
 * launch log events to the parent
 * 
 * @since v1.8
 * @author Sam Elsharif
 */
final class LaunchLogProcessor implements LogProcessor
{
	private final Set<LaunchEventProcessor> eventProcessors;

	@Inject
	LaunchLogProcessor( Set<LaunchEventProcessor> eventProcessors )
	{
		this.eventProcessors = Objects.requireNonNull( eventProcessors );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.log.analyser.LogProcessor#processMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public final void processMessage( String date, String message )
	{
		DatabaseItem databaseItem = DatabaseItem.getDatabaseItem( message );

		for( LaunchEventProcessor eventProcessor: eventProcessors )
		{
			eventProcessor.processMessage( date, databaseItem );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.log.analyser.LogProcessor#getProcessedData()
	 */
	@Override
	public final Map<String,List<String[]>> getProcessedData()
	{
		Map<String,List<String[]>> processedData = new HashMap<>();

		for( LaunchEventProcessor eventProcessor: eventProcessors )
		{
			LogProcessItem logProcessItem = eventProcessor.getProcessedData();

			processedData.put( logProcessItem.getKey(), logProcessItem.getItems() );
		}

		return Collections.unmodifiableMap( processedData );
	}
}
