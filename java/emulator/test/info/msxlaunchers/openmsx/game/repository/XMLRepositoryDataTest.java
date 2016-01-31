package info.msxlaunchers.openmsx.game.repository;

import info.msxlaunchers.openmsx.game.repository.XMLRepositoryData;
import info.msxlaunchers.openmsx.game.repository.processor.XMLProcessor;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith( MockitoJUnitRunner.class )
public class XMLRepositoryDataTest
{
	@Mock XMLProcessor xmlProcessor;
	@Mock SettingsPersister settingsPersister;

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg1() throws IOException
	{
		new XMLRepositoryData( null, xmlProcessor, null );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg2() throws IOException
	{
		new XMLRepositoryData( settingsPersister, null, null );
	}

	@Test
	public void testGetRepositoryInfoValidXML() throws IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, "path-will-make-xml-file-valid", null, null, null ) );

		XMLRepositoryData xmlRepositoryData = new XMLRepositoryData( settingsPersister, xmlProcessor, null );

		xmlRepositoryData.getRepositoryInfo();

		verify( xmlProcessor, times( 1 ) ).getRepositoryInfo( anyString() );
	}

	@Test
	public void testGetRepositoryInfoInvalidXML() throws IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, null, null ) );

		XMLRepositoryData xmlRepositoryData = new XMLRepositoryData( settingsPersister, xmlProcessor, null );

		xmlRepositoryData.getRepositoryInfo();

		verify( xmlProcessor, never() ).getRepositoryInfo( anyString() );
	}

	@Test( expected = IOException.class )
	public void testGetRepositoryInfoIOException() throws IOException
	{
		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		XMLRepositoryData xmlRepositoryData = new XMLRepositoryData( settingsPersister, xmlProcessor, null );

		xmlRepositoryData.getRepositoryInfo();
	}

	@Test
	public void testGetGameInfo() throws IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, "machines-path", null, null, null ) );

		XMLRepositoryData xmlRepositoryData = new XMLRepositoryData( settingsPersister, xmlProcessor, null );

		xmlRepositoryData.getGameInfo( "123" );

		verify( xmlProcessor, times( 1 ) ).getGameInfo( anyString(), eq( "123" ) );
	}

	@Test( expected = IOException.class )
	public void testGetGameInfoIOException() throws IOException
	{
		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		XMLRepositoryData xmlRepositoryData = new XMLRepositoryData( settingsPersister, xmlProcessor, null );

		xmlRepositoryData.getGameInfo( "123" );
	}

	@Test
	public void testGetAllDumpCodes() throws IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, null, null ) );

		XMLRepositoryData xmlRepositoryData = new XMLRepositoryData( settingsPersister, xmlProcessor, null );

		xmlRepositoryData.getDumpCodes( "123" );

		verify( xmlProcessor, times( 1 ) ).getDumpCodes( anyString(), eq( "123" ) );
	}

	@Test( expected = IOException.class )
	public void testGetAllDumpCodesIOException() throws IOException
	{
		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		XMLRepositoryData xmlRepositoryData = new XMLRepositoryData( settingsPersister, xmlProcessor, null );

		xmlRepositoryData.getDumpCodes( "123" );
	}

	@Test( expected = NullPointerException.class )
	public void testGetGameInfoNullArgument() throws IOException
	{
		when( settingsPersister.getSettings() )
		.thenReturn( new Settings( "openMSXFullPath",
				"openMSXMachinesFullPath",
				"screenshotsFullPath",
				"defaultDatabase",
				null));

		XMLRepositoryData xmlRepositoryData = new XMLRepositoryData( settingsPersister, xmlProcessor, null );

		xmlRepositoryData.getGameInfo( null );
	}

	@Test( expected = NullPointerException.class )
	public void testGetAllDumpCodesNullArgument() throws IOException
	{
		when( settingsPersister.getSettings() )
		.thenReturn( new Settings( "openMSXFullPath",
				"openMSXMachinesFullPath",
				"screenshotsFullPath",
				"defaultDatabase",
				null));

		XMLRepositoryData xmlRepositoryData = new XMLRepositoryData( settingsPersister, xmlProcessor, null );

		xmlRepositoryData.getDumpCodes( null );
	}
}
