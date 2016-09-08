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
package info.msxlaunchers.openmsx.launcher.log.analyser.processor;

import java.util.ArrayList;
import java.util.List;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.log.analyser.LogProcessItem;

/**
 * Implementation of the interface <code>LaunchEventProcessor</code> that returned a list of all launched
 * games ordered by date (i.e. the same order they appear in the log file)
 * 
 * @since v1.8
 * @author Sam Elsharif
 */
final class AllPlayHistoryLaunchEventProcessor implements LaunchEventProcessor
{
	private final List<String[]> allPlayHistory = new ArrayList<>( 1200 );

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.log.analyser.processor.LaunchEventProcessor#processMessage(java.lang.String, info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem)
	 */
	@Override
	public void processMessage( String date, DatabaseItem databaseItem )
	{
		allPlayHistory.add( new String[] { date, databaseItem.getGameName(), databaseItem.getDatabase() } );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.log.analyser.processor.LaunchEventProcessor#getProcessedData()
	 */
	@Override
	public LogProcessItem getProcessedData()
	{
		return new LogProcessItem( "ALL_PLAY_HISTORY", allPlayHistory );
	}
}
