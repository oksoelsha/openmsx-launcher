package info.msxlaunchers.openmsx.launcher.log.analyser;

import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.log.LogEvent;

@RunWith( MockitoJUnitRunner.class )
public class LogAnalyserImplTest
{
	@Mock LogReader logReader;
	@Mock LogProcessor logProcessor;

	Map<LogEvent,LogProcessor> logProcessors;

	@Before
	public void setup()
	{
		logProcessors = new HashMap<>();

		logProcessors.put( LogEvent.LAUNCH, logProcessor );
	}

	@Test
	public void whenGetProcessedData_thenLogFileIsRead()
	{
		LogAnalyserImpl analyser = new LogAnalyserImpl( logReader, logProcessors );

		analyser.getProcessedData();

		verify( logReader, times( 1 ) ).read();
	}

	@Test
	public void givenDataInLog_whenGetProcessedData_thenLogLinesAreProcessed()
	{
		LogAnalyserImpl analyser = new LogAnalyserImpl( logReader, logProcessors );

		String logLine1 = "line1: LAUNCH game";
		String logLine2 = "line2 date: some event";
		String logLine3 = "line3: LAUNCH game2[db]";
		String logLine4 = "LAUNCH something";

		List<String> logLines = Stream.of( logLine1, logLine2, logLine3, logLine4 )
				.collect( Collectors.toList() );

		when( logReader.read() ).thenReturn( logLines );

		analyser.getProcessedData();

		verify( logProcessor, times( 1 ) ).processMessage( "line1", "game" );
		verify( logProcessor, times( 1 ) ).processMessage( "line3" ,"game2[db]" );
		verify( logProcessor, times( 2 ) ).processMessage( anyString(), anyString() );
	}

	@Test
	public void givenLogFileWithoutEvents_whenGetProcessedData_thenReturnEmptyMap()
	{
		LogAnalyserImpl analyser = new LogAnalyserImpl( logReader, logProcessors );

		String logLine1 = "line1: no event";
		String logLine2 = "line2: untracked event";
		String logLine3 = "line3: some event";
		String logLine4 = "line4: only date";

		List<String> logLines = Stream.of( logLine1, logLine2, logLine3, logLine4 )
				.collect( Collectors.toList() );

		when( logReader.read() ).thenReturn( logLines );

		Map<String,List<String[]>> result = analyser.getProcessedData();

		assertTrue( result.isEmpty() );
	}

	@Test
	public void givenValidLogLine_whenGetLogEvent_thenReturnLogEvent() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		LogAnalyserImpl analyser = new LogAnalyserImpl( logReader, logProcessors );

		Method method = LogAnalyserImpl.class.getDeclaredMethod( "getLogEvent", String.class );
		method.setAccessible( true );

		LogEvent event = (LogEvent)method.invoke( analyser, "Sat Aug 20 11:37:56 EDT 2016: LAUNCH Battle Cross[ROMs]" );

		assertEquals( LogEvent.LAUNCH, event );
	}

	@Test
	public void givenEmptyLogLine_whenGetLogEvent_thenReturnNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		LogAnalyserImpl analyser = new LogAnalyserImpl( logReader, logProcessors );

		Method method = LogAnalyserImpl.class.getDeclaredMethod( "getLogEvent", String.class );
		method.setAccessible( true );

		LogEvent event = (LogEvent)method.invoke( analyser, "" );

		assertNull( event );
	}

	@Test
	public void givenNull_whenGetLogEvent_thenReturnNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		LogAnalyserImpl analyser = new LogAnalyserImpl( logReader, logProcessors );

		Method method = LogAnalyserImpl.class.getDeclaredMethod( "getLogEvent", String.class );
		method.setAccessible( true );

		String line = null;
		LogEvent event = (LogEvent)method.invoke( analyser, line );

		assertNull( event );
	}

	@Test
	public void givenUnknownEvent_whenGetLogEvent_thenReturnNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		LogAnalyserImpl analyser = new LogAnalyserImpl( logReader, logProcessors );

		Method method = LogAnalyserImpl.class.getDeclaredMethod( "getLogEvent", String.class );
		method.setAccessible( true );

		LogEvent event = (LogEvent)method.invoke( analyser, "Sun Aug 21 10:38:57 EDT 2016: UNKNOWN Nemesis[ROMs]" );

		assertNull( event );
	}

	@Test
	public void givenRandomLogLine_whenGetLogEvent_thenReturnNull() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		LogAnalyserImpl analyser = new LogAnalyserImpl( logReader, logProcessors );

		Method method = LogAnalyserImpl.class.getDeclaredMethod( "getLogEvent", String.class );
		method.setAccessible( true );

		LogEvent event = (LogEvent)method.invoke( analyser, "qwertyuiopasdfghjklzxcvbnm" );

		assertNull( event );
	}

	@Test
	public void whenGetLineDateAndMessage_thenReturnValidData() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		LogAnalyserImpl analyser = new LogAnalyserImpl( logReader, logProcessors );

		Method method = LogAnalyserImpl.class.getDeclaredMethod( "getLineDateAndMessage", String.class, LogEvent.class );
		method.setAccessible( true );

		String data[] = (String[])method.invoke( analyser, "Tue Aug 06 22:55:04 EDT 2016: LAUNCH gameName[defaultDatabase]", LogEvent.LAUNCH );

		assertEquals( "Tue Aug 06 22:55:04 EDT 2016", data[0] );
		assertEquals( "gameName[defaultDatabase]", data[1] );
	}
}
