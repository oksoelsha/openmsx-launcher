package info.msxlaunchers.openmsx.launcher.log.analyser.processor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.log.analyser.LogProcessItem;

public class AllPlayHistoryLaunchEventProcessorTest
{
	@Test
	public void whenGetItems_thenOrderedHistoryIsReturned()
	{
		AllPlayHistoryLaunchEventProcessor allPlayHistoryLaunchEventProcessor = new AllPlayHistoryLaunchEventProcessor();

		for( int count = 0; count < 100; count++ )
		{
			allPlayHistoryLaunchEventProcessor.processMessage( "date" + count, new DatabaseItem( "game" + count, "database" + count ) );
		}

		LogProcessItem logProcessItem = allPlayHistoryLaunchEventProcessor.getProcessedData();
		assertEquals( "ALL_PLAY_HISTORY", logProcessItem.getKey() );

		List<String[]> items = logProcessItem.getItems();
		assertEquals( 100, items.size() );

		for( int count = 0; count < 100; count++ )
		{
			assertEquals( "date" + count, items.get( count )[0] );
			assertEquals( "game" + count, items.get( count )[1] );
			assertEquals( "database" + count, items.get( count )[2] );
		}
	}
}
