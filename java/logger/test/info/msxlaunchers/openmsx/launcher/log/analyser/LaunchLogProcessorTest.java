package info.msxlaunchers.openmsx.launcher.log.analyser;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.log.analyser.processor.LaunchEventProcessor;

@RunWith( MockitoJUnitRunner.class )
public class LaunchLogProcessorTest
{
	@Mock LaunchEventProcessor launchEventProcessor1;
	@Mock LaunchEventProcessor launchEventProcessor2;
	@Mock LaunchEventProcessor launchEventProcessor3;

	Set<LaunchEventProcessor> launchEventProcessors;

	@Before
	public void setup()
	{
		launchEventProcessors = Stream.of( launchEventProcessor1, launchEventProcessor2, launchEventProcessor3 ).collect( Collectors.toSet() );
	}

	@Test( expected = NullPointerException.class )
	public void givenNullSet_whenCreateInstance_thenThrowException()
	{
		new LaunchLogProcessor( null );
	}

	@Test
	public void whenProcessMessage_thenLogEventProcessorsAreCalled()
	{
		LaunchLogProcessor launchLogProcessor = new LaunchLogProcessor( launchEventProcessors );

		String date = "date";
		String message = "message";
		DatabaseItem databaseItem = DatabaseItem.getDatabaseItem( message );

		launchLogProcessor.processMessage( date, message );

		verify( launchEventProcessor1, times( 1 ) ).processMessage( date, databaseItem );
		verify( launchEventProcessor2, times( 1 ) ).processMessage( date, databaseItem );
		verify( launchEventProcessor3, times( 1 ) ).processMessage( date, databaseItem );
	}

	@Test
	public void whenGetProcessedData_thenEachLaunchEventProcessorGetProcessedDataIsCalled()
	{
		String key1 = "key1";
		List<String[]> list1 = Collections.emptyList();
		LogProcessItem logProcessItem1 = new LogProcessItem( key1, list1 );

		String key2 = "key2";
		List<String[]> list2 = Collections.emptyList();
		LogProcessItem logProcessItem2 = new LogProcessItem( key2, list2 );

		String key3 = "key3";
		List<String[]> list3 = Collections.emptyList();
		LogProcessItem logProcessItem3 = new LogProcessItem( key3, list3 );

		when( launchEventProcessor1.getProcessedData() ).thenReturn( logProcessItem1 );
		when( launchEventProcessor2.getProcessedData() ).thenReturn( logProcessItem2 );
		when( launchEventProcessor3.getProcessedData() ).thenReturn( logProcessItem3 );

		LaunchLogProcessor launchLogProcessor = new LaunchLogProcessor( launchEventProcessors );

		Map<String,List<String[]>> processedData = launchLogProcessor.getProcessedData();

		assertEquals( 3, processedData.size() );
		assertTrue( processedData.keySet().contains( key1 ) );
		assertTrue( processedData.keySet().contains( key3 ) );
		assertTrue( processedData.keySet().contains( key2 ) );
		assertEquals( list1, processedData.get( key1 ) );
		assertEquals( list2, processedData.get( key2 ) );
		assertEquals( list3, processedData.get( key3 ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void whenAddToMapFromGetProcessedData_thenThrowException()
	{
		LogProcessItem logProcessItem = new LogProcessItem( "key", Collections.emptyList() );

		when( launchEventProcessor1.getProcessedData() ).thenReturn( logProcessItem );
		when( launchEventProcessor2.getProcessedData() ).thenReturn( logProcessItem );
		when( launchEventProcessor3.getProcessedData() ).thenReturn( logProcessItem );

		LaunchLogProcessor launchLogProcessor = new LaunchLogProcessor( launchEventProcessors );

		Map<String,List<String[]>> processedData = launchLogProcessor.getProcessedData();

		processedData.put( "key", Collections.emptyList() );
	}
}
