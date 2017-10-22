package info.msxlaunchers.openmsx.extension;

import info.msxlaunchers.openmsx.extension.OpenMSXExtensionLister;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;

import java.io.IOException;
import java.util.Set;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class OpenMSXExtensionListerTest
{
	@Mock SettingsPersister settingsPersister;

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructor() throws IOException
	{
		new OpenMSXExtensionLister( null );
	}

	@Test( expected = IOException.class )
	public void testGetIOException() throws InvalidMachinesDirectoryException, IOException
	{
		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		OpenMSXExtensionLister openMSXExtensionLister = new OpenMSXExtensionLister( settingsPersister );

		openMSXExtensionLister.get();
	}

	@Test( expected = InvalidMachinesDirectoryException.class )
	public void testGetInvalidMachinesDirectoryForMachinesPathAsNull() throws InvalidMachinesDirectoryException, IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, null, null, false, false ) );

		OpenMSXExtensionLister openMSXExtensionLister = new OpenMSXExtensionLister( settingsPersister );

		openMSXExtensionLister.get();
	}

	@Test( expected = InvalidMachinesDirectoryException.class )
	public void testGetInvalidMachinesDirectoryForMachinesPathAsFile() throws InvalidMachinesDirectoryException, IOException
	{
		String machinesDirectory = getClass().getResource( "file.txt" ).getFile();
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, machinesDirectory, null, null, null, false, false ) );

		OpenMSXExtensionLister openMSXExtensionLister = new OpenMSXExtensionLister( settingsPersister );

		openMSXExtensionLister.get();
	}

	@Test
	public void testGetExtensionsDirectoryNotFound() throws InvalidMachinesDirectoryException, IOException
	{
		String machinesDirectory = getClass().getResource( "folder1/machines" ).getFile();
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, machinesDirectory, null, null, null, false, false ) );

		OpenMSXExtensionLister openMSXExtensionLister = new OpenMSXExtensionLister( settingsPersister );

		Set<String> extensions = openMSXExtensionLister.get();	

		assertTrue( extensions.isEmpty() );
	}

	@Test
	public void testGetExtensions() throws InvalidMachinesDirectoryException, IOException
	{
		String machinesDirectory = getClass().getResource( "folder2/machines" ).getFile();
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, machinesDirectory, null, null, null, false, false ) );

		OpenMSXExtensionLister openMSXExtensionLister = new OpenMSXExtensionLister( settingsPersister );

		Set<String> extensions = openMSXExtensionLister.get();	

		assertEquals( extensions.size(), 2 );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testGetExtensionsCannotBeModified() throws InvalidMachinesDirectoryException, IOException
	{
		String machinesDirectory = getClass().getResource( "folder2/machines" ).getFile();
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, machinesDirectory, null, null, null, false, false ) );

		OpenMSXExtensionLister openMSXExtensionLister = new OpenMSXExtensionLister( settingsPersister );

		Set<String> extensions = openMSXExtensionLister.get();	

		extensions.add( "should-not-be-extension" );
	}

	@Test
	public void testGetWhenExtensionsIsFile() throws InvalidMachinesDirectoryException, IOException
	{
		String machinesDirectory = getClass().getResource( "folder3/machines" ).getFile();
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, machinesDirectory, null, null, null, false, false ) );

		OpenMSXExtensionLister openMSXExtensionLister = new OpenMSXExtensionLister( settingsPersister );

		Set<String> extensions = openMSXExtensionLister.get();	

		assertTrue( extensions.isEmpty() );
	}
}
