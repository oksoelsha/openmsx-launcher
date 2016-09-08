package info.msxlaunchers.openmsx.launcher.log.analyser.processor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.log.analyser.LogProcessItem;

public class TopPlayedLaunchEventProcessorTest
{
	@Test
	public void whenGetItems_thenTop10LaunchListIsReturned()
	{
		TopPlayedLaunchEventProcessor topPlayedLaunchEventProcessor = new TopPlayedLaunchEventProcessor();

		for( int iy = 0; iy < 11; iy++ )
		{
			for( int ix = iy; ix < 11; ix++ )
			{
				DatabaseItem databaseItem = new DatabaseItem( "game" + ix, "database" + ix );
				topPlayedLaunchEventProcessor.processMessage( "date" + ix, databaseItem );
			}
		}

		LogProcessItem logProcessItem = topPlayedLaunchEventProcessor.getProcessedData();
		assertEquals( "MOST_PLAYED", logProcessItem.getKey() );

		List<String[]> items = logProcessItem.getItems();

		assertEquals( 10, items.size() );
		for( int count = 0; count < 10; count++ )
		{
			assertEquals( "game" + Integer.toString( 10 - count ), items.get( count )[0] );
			assertEquals( "database" + Integer.toString( 10 - count ), items.get( count )[1] );
			assertEquals( Integer.toString( 11 - count ), items.get( count )[2] );
		}
	}

	@Test
	public void givenManyGamesLaunchedMultipleTimes_whenGetItems_thenTop10LaunchListWithOrderedKeysForEqualValuesIsReturned()
	{
		TopPlayedLaunchEventProcessor topPlayedLaunchEventProcessor = new TopPlayedLaunchEventProcessor();

		callProcessMessage( topPlayedLaunchEventProcessor, "c", "dd", 3 );
		callProcessMessage( topPlayedLaunchEventProcessor, "f", "ff", 1 );
		callProcessMessage( topPlayedLaunchEventProcessor, "i", "ii", 1 );
		callProcessMessage( topPlayedLaunchEventProcessor, "g", "gg", 2 );
		callProcessMessage( topPlayedLaunchEventProcessor, "k", "kk", 3 );
		callProcessMessage( topPlayedLaunchEventProcessor, "b", "bb", 3 );
		callProcessMessage( topPlayedLaunchEventProcessor, "a", "aa", 2 );
		callProcessMessage( topPlayedLaunchEventProcessor, "c", "hh", 3 );
		callProcessMessage( topPlayedLaunchEventProcessor, "h", "hh", 3 );
		callProcessMessage( topPlayedLaunchEventProcessor, "e", "ee", 2 );
		callProcessMessage( topPlayedLaunchEventProcessor, "j", "jj", 1 );
		callProcessMessage( topPlayedLaunchEventProcessor, "d", "dd", 2 );
		callProcessMessage( topPlayedLaunchEventProcessor, "c", "aa", 3 );

		List<String[]> items = topPlayedLaunchEventProcessor.getProcessedData().getItems();

		assertEquals( 10, items.size() );

		assertEquals( "b", items.get( 0 )[0] );
		assertEquals( "bb", items.get( 0 )[1] );
		assertEquals( "3", items.get( 0 )[2] );

		assertEquals( "c", items.get( 1 )[0] );
		assertEquals( "aa", items.get( 1 )[1] );
		assertEquals( "3", items.get( 1 )[2] );

		assertEquals( "c", items.get( 2 )[0] );
		assertEquals( "dd", items.get( 2 )[1] );
		assertEquals( "3", items.get( 2 )[2] );

		assertEquals( "c", items.get( 3 )[0] );
		assertEquals( "hh", items.get( 3 )[1] );
		assertEquals( "3", items.get( 3 )[2] );

		assertEquals( "h", items.get( 4 )[0] );
		assertEquals( "hh", items.get( 4 )[1] );
		assertEquals( "3", items.get( 4 )[2] );

		assertEquals( "k", items.get( 5 )[0] );
		assertEquals( "kk", items.get( 5 )[1] );
		assertEquals( "3", items.get( 5 )[2] );

		assertEquals( "a", items.get( 6 )[0] );
		assertEquals( "aa", items.get( 6 )[1] );
		assertEquals( "2", items.get( 6 )[2] );

		assertEquals( "d", items.get( 7 )[0] );
		assertEquals( "dd", items.get( 7 )[1] );
		assertEquals( "2", items.get( 7 )[2] );

		assertEquals( "e", items.get( 8 )[0] );
		assertEquals( "ee", items.get( 8 )[1] );
		assertEquals( "2", items.get( 8 )[2] );

		assertEquals( "g", items.get( 9 )[0] );
		assertEquals( "gg", items.get( 9 )[1] );
		assertEquals( "2", items.get( 9 )[2] );
	}

	private void callProcessMessage( TopPlayedLaunchEventProcessor topPlayedLaunchEventProcessor, String game, String database, int numberOfTimes )
	{
		for( int count = 0; count < numberOfTimes; count++ )
		{
			topPlayedLaunchEventProcessor.processMessage( "date", new DatabaseItem( game, database ) );
		}
	}
}
