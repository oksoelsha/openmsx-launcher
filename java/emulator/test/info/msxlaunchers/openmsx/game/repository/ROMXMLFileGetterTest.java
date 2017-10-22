package info.msxlaunchers.openmsx.game.repository;

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

@RunWith( MockitoJUnitRunner.class )
public class ROMXMLFileGetterTest
{
	@Mock SettingsPersister settingsPersister;

	@Test
	public void givenNullMachinesPath_whenGet_returnNull() throws IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, null, null, false, false ) );

		ROMXMLFileGetter romXmlFileGetter = new ROMXMLFileGetter( settingsPersister, null );

		Assert.assertNull( romXmlFileGetter.get() );
	}

	@Test
	public void givenMachinesPathAndNullBaseDirectory_whenGet_returnValidFileInstance() throws IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, "/main/machines-path", null, null, null, false, false ) );

		ROMXMLFileGetter romXmlFileGetter = new ROMXMLFileGetter( settingsPersister, null );

		Assert.assertEquals( new File( "/main", "softwaredb.xml" ), romXmlFileGetter.get() );
	}

	@Test
	public void givenMachinesPathAndBaseDirectory_whenGet_returnValidFileInstance() throws IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, "/main/machines-path", null, null, null, false, false ) );

		ROMXMLFileGetter romXmlFileGetter = new ROMXMLFileGetter( settingsPersister, "/base" );

		Assert.assertEquals( new File( "/base/main", "softwaredb.xml" ), romXmlFileGetter.get() );
	}
}
