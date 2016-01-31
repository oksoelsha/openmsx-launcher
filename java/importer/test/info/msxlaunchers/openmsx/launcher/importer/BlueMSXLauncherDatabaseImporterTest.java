package info.msxlaunchers.openmsx.launcher.importer;

import info.msxlaunchers.openmsx.common.FileUtils;
import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class BlueMSXLauncherDatabaseImporterTest
{
	@Mock GamePersister gamePersister;
	@Mock GameBuilder gameBuilder;
	@Mock ActionDecider actionDecider;
	@Mock ExtraDataGetter extraDataGetter;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test( expected = UnsupportedOperationException.class )
	public void test_WhenImportDatabases_ThenReturnedSetShouldBeUnmodifiable() throws IOException
	{
		BlueMSXLauncherDatabaseImporter importer = new BlueMSXLauncherDatabaseImporter( "machine", gameBuilder, gamePersister, extraDataGetter );

		File[] database = new File[1];
		database[0] = new File( "dir" );

		Set<String> result = importer.importDatabases( database, actionDecider );

		result.add( "should_not_be_allowed" );
	}

	@Test
	public void test_GivenConflict_WhenImportDatabases_thenUserIsPromptedForAction() throws IOException
	{
		Set<String> existingDatabase = new HashSet<String>();
		existingDatabase.add( "existing" );

		when( gamePersister.getDatabases() ).thenReturn( existingDatabase );
		when( actionDecider.isYesAll() ).thenReturn( false );
		when( actionDecider.isYes() ).thenReturn( false );
		when( actionDecider.isNo() ).thenReturn( true );

		BlueMSXLauncherDatabaseImporter importer = new BlueMSXLauncherDatabaseImporter( "machine", gameBuilder, gamePersister, extraDataGetter );

		File[] database = new File[1];
		database[0] = new File( "C:\\dir\\existing.db" );

		importer.importDatabases( database, actionDecider );

		verify( actionDecider, times( 1 ) ).promptForAction( "existing" );
	}

	@Test
	public void test_GivenConflict_WhenImportDatabasesAndUserDecidesYes_thenExistingDatabaseIsDeleted() throws IOException, GamePersistenceException
	{
		Set<String> existingDatabase = new HashSet<String>();
		existingDatabase.add( "existing" );

		when( gamePersister.getDatabases() ).thenReturn( existingDatabase );
		when( actionDecider.isYes() ).thenReturn( true );

		BlueMSXLauncherDatabaseImporter importer = new BlueMSXLauncherDatabaseImporter( "machine", gameBuilder, gamePersister, extraDataGetter );

		File[] database = new File[1];
		database[0] = new File( "C:\\dir\\existing.db" );

		importer.importDatabases( database, actionDecider );

		verify( gamePersister, times( 1 ) ).deleteDatabase( "existing" );
	}

	@Test
	public void test_GivenConflict_WhenImportDatabasesAndUserDecidesYesToAll_thenAllExistingDatabasesAreDeleted() throws IOException, GamePersistenceException
	{
		Set<String> existingDatabases = new HashSet<String>();
		existingDatabases.add( "existing1" );
		existingDatabases.add( "existing2" );

		when( gamePersister.getDatabases() ).thenReturn( existingDatabases );
		when( actionDecider.isYesAll() ).thenReturn( true );

		BlueMSXLauncherDatabaseImporter importer = new BlueMSXLauncherDatabaseImporter( "machine", gameBuilder, gamePersister, extraDataGetter );

		File[] databases = new File[2];
		databases[0] = new File( "C:\\dir\\existing1.db" );
		databases[1] = new File( "C:\\dir\\existing2.db" );

		importer.importDatabases( databases, actionDecider );

		verify( gamePersister, times( 1 ) ).deleteDatabase( "existing1" );
		verify( gamePersister, times( 1 ) ).deleteDatabase( "existing2" );
		verify( gamePersister, times( 2 ) ).deleteDatabase( anyString() );
	}

	@Test
	public void test_GivenConflict_WhenImportDatabasesAndUserDecidesNoToAll_thenNoExistingDatabasesAreDeleted() throws IOException, GamePersistenceException
	{
		Set<String> existingDatabases = new HashSet<String>();
		existingDatabases.add( "existing1" );
		existingDatabases.add( "existing2" );

		when( gamePersister.getDatabases() ).thenReturn( existingDatabases );
		when( actionDecider.isNoAll() ).thenReturn( true );

		BlueMSXLauncherDatabaseImporter importer = new BlueMSXLauncherDatabaseImporter( "machine", gameBuilder, gamePersister, extraDataGetter );

		File[] databases = new File[2];
		databases[0] = new File( "C:\\dir\\existing1.db" );
		databases[1] = new File( "C:\\dir\\existing2.db" );

		importer.importDatabases( databases, actionDecider );

		verify( gamePersister, never() ).deleteDatabase( anyString() );
	}

	@Test
	public void test_GivenConflictForTwoDatabases_WhenImportDatabasesAndUserDecidesNoToOneOfThem_thenOneExistingDatabasesIsDeleted() throws IOException, GamePersistenceException
	{
		//use a LinkedHashSet to preserve the insertion order
		Set<String> existingDatabases = new LinkedHashSet<String>();
		existingDatabases.add( "existing1" );
		existingDatabases.add( "existing2" );

		when( gamePersister.getDatabases() ).thenReturn( existingDatabases );
		when( actionDecider.isYes() ).thenReturn( true ).thenReturn( false );
		when( actionDecider.isNo() ).thenReturn( true ); //isNo() will only be called if isYes() and iYesAll() are false

		BlueMSXLauncherDatabaseImporter importer = new BlueMSXLauncherDatabaseImporter( "machine", gameBuilder, gamePersister, extraDataGetter );

		File[] databases = new File[2];
		databases[0] = new File( "C:\\dir\\existing1.db" );
		databases[1] = new File( "C:\\dir\\existing2.db" );

		importer.importDatabases( databases, actionDecider );

		//only the first database was replaced
		verify( gamePersister, times( 1 ) ).deleteDatabase( "existing1" );
		verify( gamePersister, never() ).deleteDatabase( "existing2" );
	}

	@Test @SuppressWarnings("unchecked")
	public void test_GivenNoConflicts_WhenImportDatabases_ThenDataAreImportedAndGamePersisterSaveGamesAndCreateDatabaseAreCalled()
			throws IOException, GamePersistenceException
	{
		File[] databases = new File[3];
		databases[0] = tmpFolder.newFile( "database1.db" );
		databases[1] = tmpFolder.newFile( "database2.db" );
		databases[2] = new File( "non-existing.db" );

		String machine = "MSX2";

		//fill database1 with data - all supported extensions
		PrintWriter writer = new PrintWriter( databases[0] );
		writer.println( "scc only|||||MSX2 - C-BIOS||||||||scc|||||||||||||0|0|0|||" );
		writer.println( "scc+ only|||||MSX2 - C-BIOS||||||||scc+|||||||||||||0|0|0|||" );
		writer.println( "fmpac only|||||MSX2 - C-BIOS||||||||fmpac|||||||||||||0|0|0|||" );
		writer.println( "fmpak only|||||MSX2 - C-BIOS||||||||fmpak|||||||||||||0|0|0|||" );
		writer.println( "symbos|||||MSX2+||||||||sunriseide||C:\\Games\\symboshd 1.1.dsk||||-machine MSXturboR -ext ide|T|T|||DE|MSX|0|0|22|||" );
		writer.close();

		//fill database2 with data - all kinds of media
		writer = new PrintWriter( databases[1] );
		writer.println( "rom|C:\\rom.rom||||MSX|http://www.generation-msx.nl/msxdb/softwareinfo/25|||||||||||C:\\Games\\extra-screenshots\\25a.png|C:\\Games\\extra-screenshots\\25b.png||||||||1|1|1|" );
		writer.println( "two roms|C:\\rom1.rom|C:\\rom2.rom|||MSX|C:\\helpfile.pdf|||||||||||C:\\Games\\extra-screenshots\\25a.png|C:\\Games\\extra-screenshots\\25b.png||||||||1|1|1|" );
		writer.println( "rom and disk|C:\\rom.rom||C:\\disk.dsk||MSX||||||||||||C:\\Games\\extra-screenshots\\25a.png|||||||||1|1|1|" );
		writer.println( "disk|||C:\\Games\\MSX\\disk.zip||MSX2||||disk.dsk|||||||||||||||||1|1|44|" );
		writer.println( "disk and scc|||C:\\Games\\MSX\\disk_needs_scc.zip||MSX2||||disk.dsk||||scc||||||||||||||||" );
		writer.println( "two disks|||C:\\Games\\MSX\\disk1.zip|C:\\Games\\MSX\\disk2.zip|MSX2||||disk1.dsk||||||||||||||||||||" );
		writer.println( "tape|||||MSX2+||||||C:\\Games\\tape.cas|tape.CAS||||||||||||||0|0|0|||" );
		writer.println( "" );	//test that this gets skipped

		writer.close();

		Map<String,ExtraData> extraDataMap = Collections.emptyMap();

		when( extraDataGetter.getExtraData() ).thenReturn( extraDataMap );

		BlueMSXLauncherDatabaseImporter importer = new BlueMSXLauncherDatabaseImporter( machine, gameBuilder, gamePersister, extraDataGetter );

		importer.importDatabases( databases, actionDecider );

		//verify that gameBuilder was called for entries in database1
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "scc only", null, machine, null, null, "scc", null, null, null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "scc+ only", null, machine, null, null, "scc+", null, null, null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "fmpac only", null, machine, null, null, "fmpac", null, null, null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "fmpak only", null, machine, null, null, "pac", null, null, null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "symbos", null, machine, null, null, "ide", null, null, null, "C:\\Games\\symboshd 1.1.dsk", extraDataMap );

		//verify that gameBuilder was called for entries in database2
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "rom", "http://www.generation-msx.nl/msxdb/softwareinfo/25", machine, "C:\\rom.rom", null, null, null, null, null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "two roms", "C:\\helpfile.pdf", machine, "C:\\rom1.rom", "C:\\rom2.rom", null, null, null, null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "rom and disk", null, machine, "C:\\rom.rom", null, null, "C:\\disk.dsk", null, null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "disk", null, machine, null, null, null, "C:\\Games\\MSX\\disk.zip", null, null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "disk and scc", null, machine, null, null, "scc", "C:\\Games\\MSX\\disk_needs_scc.zip", null, null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "two disks", null, machine, null, null, null, "C:\\Games\\MSX\\disk1.zip", "C:\\Games\\MSX\\disk2.zip", null, null, extraDataMap );
		verify( gameBuilder, times( 1 ) ).createGameObjectForImportedData( "tape", null, machine, null, null, null, null, null, "C:\\Games\\tape.cas", null, extraDataMap );

		//verify total number of gameBuilder calls
		verify( gameBuilder, times( 12 ) ).createGameObjectForImportedData( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), eq( extraDataMap ) );

		//verify collaborator calls per valid database
		verify( gamePersister, times( 1 ) ).createDatabase( FileUtils.getFileNameWithoutExtension( databases[0] ) );
		verify( gamePersister, times( 1 ) ).saveGames( anySet(), eq( FileUtils.getFileNameWithoutExtension( databases[0] ) ) );

		verify( gamePersister, times( 1 ) ).createDatabase( FileUtils.getFileNameWithoutExtension( databases[1] ) );
		verify( gamePersister, times( 1 ) ).saveGames( anySet(), eq( FileUtils.getFileNameWithoutExtension( databases[1] ) ) );

		verify( gamePersister, never() ).createDatabase( FileUtils.getFileNameWithoutExtension( databases[2] ) );
		verify( gamePersister, never() ).saveGames( anySet(), eq( FileUtils.getFileNameWithoutExtension( databases[2] ) ) );
	}

	//The following test is for a private method. I use reflection to get it and call it
	@Test
	public void test_GivenBlueMSXLauncherExtensionRom_WhenGetExtensionRom_ThenReturnCorrespomdingOpenMSXExtensionRom() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException
	{
		BlueMSXLauncherDatabaseImporter importer = new BlueMSXLauncherDatabaseImporter( "MSX1", gameBuilder, gamePersister, extraDataGetter );

		Method method = BlueMSXLauncherDatabaseImporter.class.getDeclaredMethod( "getExtensionRom", String.class );
		method.setAccessible( true );

		assertEquals( "scc", method.invoke( importer, "scc" ) );
		assertEquals( "scc+", method.invoke( importer, "scc+" ) );
		assertEquals( "fmpac", method.invoke( importer, "fmpac" ) );
		assertEquals( "ide", method.invoke( importer, "sunriseide" ) );
		assertEquals( "pac", method.invoke( importer, "fmpak" ) );
		assertNull( method.invoke( importer, "wrong" ) );
		assertNull( method.invoke( importer, "" ) );
		assertNull( method.invoke( importer, new Object[]{ null } ) );
	}
}