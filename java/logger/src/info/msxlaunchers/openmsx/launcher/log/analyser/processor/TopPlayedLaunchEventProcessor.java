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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.log.analyser.LogProcessItem;

/**
 * Implementation of the interface <code>LaunchEventProcessor</code> that returned the top 10 played games.
 * as recorded in the log file. For entries with equal value, their message part (game and database) will be
 * ordered alphabetically
 * 
 * @since v1.8
 * @author Sam Elsharif
 */
final class TopPlayedLaunchEventProcessor implements LaunchEventProcessor
{
	private final Map<DatabaseItem,Integer> topPlayedGames = new HashMap<>();

	private final int MAX_TOP_GAMES = 10;

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.log.analyser.processor.LaunchEventProcessor#processMessage(java.lang.String, info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem)
	 */
	@Override
	public void processMessage( String date, DatabaseItem databaseItem )
	{
		if( topPlayedGames.containsKey( databaseItem ) )
		{
			int count = topPlayedGames.get( databaseItem ) + 1;
			topPlayedGames.put( databaseItem, count );
		}
		else
		{
			topPlayedGames.put( databaseItem, 1 );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.log.analyser.processor.LaunchEventProcessor#getProcessedData()
	 */
	@Override
	public LogProcessItem getProcessedData()
	{
		Comparator<Map.Entry<DatabaseItem,Integer>> gameNameComparator = (g1, g2) -> g1.getKey().getGameName().compareToIgnoreCase( g2.getKey().getGameName() );
		Comparator<Map.Entry<DatabaseItem,Integer>> databaseComparator = (d1, d2) -> d1.getKey().getDatabase().compareToIgnoreCase( d2.getKey().getDatabase() );

		List<String[]> list = topPlayedGames.entrySet().stream()
				.sorted( Map.Entry.<DatabaseItem,Integer>comparingByValue().reversed().thenComparing( gameNameComparator ).thenComparing( databaseComparator ) )
				.limit( MAX_TOP_GAMES )
				.map( e -> new String[] {e.getKey().getGameName(), e.getKey().getDatabase(), e.getValue().toString()} )
				.collect( Collectors.toList() );

		return new LogProcessItem( "MOST_PLAYED", list );
	}
}
