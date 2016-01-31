package info.msxlaunchers.openmsx.machine;

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.OpenMSXMachineLister;

import java.io.IOException;
import java.util.Set;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class OpenMSXMachineListerTest
{
	@Mock SettingsPersister settingsPersister;

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructor() throws IOException
	{
		new OpenMSXMachineLister( null );
	}

	@Test( expected = IOException.class )
	public void testGetIOException() throws InvalidMachinesDirectoryException, IOException
	{
		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		OpenMSXMachineLister openMSXMachineLister = new OpenMSXMachineLister( settingsPersister );

		openMSXMachineLister.get();
	}

	@Test
	public void testGetEmptyMachinesDirectory() throws InvalidMachinesDirectoryException, IOException
	{
		String machinesDirectory = getClass().getResource( "emptyMachinesDirectory" ).getFile();
		
		when( settingsPersister.getSettings() ).thenReturn( new Settings(null,
				machinesDirectory, null, null, null) );
		
		OpenMSXMachineLister openMSXMachineLister = new OpenMSXMachineLister( settingsPersister );

		Set<String> machines = openMSXMachineLister.get();

		//the return value is an empty set and not null
		assertNotNull( machines );
		assertTrue( machines.isEmpty() );
	}

	@Test( expected = InvalidMachinesDirectoryException.class )
	public void testGetNonExistentMachinesDirectory() throws InvalidMachinesDirectoryException, IOException
	{
		String machinesDirectory = "/non-existent-directory";
		
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null,	machinesDirectory, null, null, null ) );
		
		OpenMSXMachineLister openMSXMachineLister = new OpenMSXMachineLister( settingsPersister );

		openMSXMachineLister.get();
	}

	@Test( expected = InvalidMachinesDirectoryException.class )
	public void testGetNullMachinesDirectory() throws InvalidMachinesDirectoryException, IOException
	{
		when( settingsPersister.getSettings() ).thenReturn( new Settings( null,null, null, null, null ) );
		
		OpenMSXMachineLister openMSXMachineLister = new OpenMSXMachineLister( settingsPersister );

		openMSXMachineLister.get();
	}

	@Test
	public void testGetMachines() throws InvalidMachinesDirectoryException, IOException
	{
		String machinesDirectory = getClass().getResource( "machinesDirectory" ).getFile();
		
		when( settingsPersister.getSettings() ).thenReturn( new Settings(null,
				machinesDirectory, null, null, null) );
		
		OpenMSXMachineLister openMSXMachineLister = new OpenMSXMachineLister( settingsPersister );

		Set<String> machines = openMSXMachineLister.get();

		//there are only two machine directories in the test directory
		assertEquals( 3, machines.size() );

		//check the individual machines
		assertTrue( machines.contains( "machine1" ) );
		assertTrue( machines.contains( "machine2" ) );
		assertTrue( machines.contains( "machine3" ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testGetMachinesCannotModify() throws InvalidMachinesDirectoryException, IOException
	{
		String machinesDirectory = getClass().getResource( "machinesDirectory" ).getFile();
		
		when( settingsPersister.getSettings() ).thenReturn( new Settings(null,
				machinesDirectory, null, null, null) );
		
		OpenMSXMachineLister openMSXMachineLister = new OpenMSXMachineLister( settingsPersister );

		Set<String> machines = openMSXMachineLister.get();

		machines.add( "cannot-be-added" );
	}
}
