package info.msxlaunchers.openmsx.game.scan;

import info.msxlaunchers.openmsx.game.repository.RepositoryData;
import info.msxlaunchers.openmsx.game.scan.FileScanner;
import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class FileScannerTest
{
	private final String database = "testDatabase";
	private final String path1 = getClass().getResource( "dir1" ).getFile();
	private final String path2 = getClass().getResource( "dir2" ).getFile();
	private final String[] paths = new String[] { path1, path2 };

	@Mock GamePersister gamePersister;
	@Mock RepositoryData repositoryData;
	@Mock GameBuilder gameBuilder;
	@Mock ExtraDataGetter extraDataGetter;

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg1() throws IOException
	{
		new FileScanner( null, repositoryData, gameBuilder, extraDataGetter, null );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg2() throws IOException
	{
		new FileScanner( gamePersister, null, gameBuilder, extraDataGetter, null );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg3() throws IOException
	{
		new FileScanner( gamePersister, repositoryData, null, extraDataGetter, null );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg4() throws IOException
	{
		new FileScanner( gamePersister, repositoryData, gameBuilder, null, null );
	}

	@Test( expected = GamePersistenceException.class )
	public void testForBackupNonExistentDatabase() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, database ) ).when( gamePersister ).backupDatabase( database );

		//set backup to true
		//newDatabase must be false
		try
		{
			scanner.scan( paths, true, database, false, true, "machine", true, true, true, true, false, true );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void testForOverwriteNonExistentDatabase() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, database ) ).when( gamePersister ).recreateDatabase( database );

		//set append to false
		//newDatabase must be false
		try
		{
			scanner.scan( paths, true, database, false, false, "machine", true, true, true, true, false, false );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test
	public void testNewDatabaseOptionOn() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		//set newDatabase to true
		//setting the append flag to true is irrelevant in this case
		scanner.scan( paths, true, database, true, true, "machine", true, true, true, true, false, true );

		//check that the createDatabase was called
		verify( gamePersister ).createDatabase( database );
	}

	@Test
	public void testNewDatabaseOptionOff() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		//set newDatabase to false
		//setting the append flag to true is irrelevant in this case
		scanner.scan( paths, true, database, false, true, "machine", true, true, true, true, false, true );

		//check that the createDatabase was called
		verify( gamePersister, never() ).createDatabase( database );
	}

	@Test
	public void testBackupDatabaseOptionOn() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		//set backup to true
		//newDatabase must be false
		scanner.scan( paths, true, database, false, true, "machine", true, true, true, true, false, true );

		//check that the backupDatabase was called
		verify( gamePersister ).backupDatabase( database );
	}

	@Test
	public void testBackupDatabaseOptionOff() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		//set backup to false
		//newDatabase must be false
		scanner.scan( paths, true, database, false, true, "machine", true, true, true, true, false, false );

		//check that the backupDatabase was not called
		verify( gamePersister, never() ).backupDatabase( database );
	}

	@Test
	public void testAppendToDatabaseOptionOn() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		//set append to true
		//newDatabase must be false
		scanner.scan( paths, true, database, false, true, "machine", true, true, true, true, false, true );

		//check that the recreateDatabase was not called
		verify( gamePersister, never() ).recreateDatabase( database );
	}

	@Test
	public void testAppendToDatabaseOptionOff() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		//set append to false
		//newDatabase must be false
		scanner.scan( paths, true, database, false, false, "machine", true, true, true, true, false, false );

		//check that the recreateDatabase was called
		verify( gamePersister ).recreateDatabase( database );
	}

	@Test
	public void testGetNameFromOpenMSXDatabaseOptionOn() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		//set getNameFromOpenMSXDatabase to true
		scanner.scan( paths, true, database, false, true, "machine", true, true, true, true, true, true );

		//check that the getRepositoryInfo was called
		verify( repositoryData ).getRepositoryInfo();
	}

	@Test
	public void testGetNameFromOpenMSXDatabaseOptionOff() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		//set getNameFromOpenMSXDatabase to false
		scanner.scan( paths, true, database, false, false, "machine", true, true, true, true, false, false );

		//check that the getRepositoryInfo was not called
		verify( repositoryData, never() ).getRepositoryInfo();
	}

	@Test @SuppressWarnings("unchecked")
	public void testSearchForRomsOnly() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		when( gameBuilder
				.createGameObjectForScannedFiles( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyMap() ) )
				.thenReturn( Game.name("name").build() );

		//traverse all test directories
		int found = scanner.scan( paths, true, database, false, false, "machine", true, false, false, false, false, false );

		//there are 4 Roms in the test directories
		assertEquals( 4, found );
	}

	@Test @SuppressWarnings("unchecked")
	public void testSearchForDisksOnly() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		when( gameBuilder
				.createGameObjectForScannedFiles( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyMap() ) )
				.thenReturn( Game.name("name").build() );

		//traverse all test directories
		int found = scanner.scan( paths, true, database, false, false, "machine", false, true, false, false, false, false );

		//there are 4 disks in the test directories
		assertEquals( 4, found );
	}

	@Test @SuppressWarnings("unchecked")
	public void testSearchForTapesOnly() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		when( gameBuilder
				.createGameObjectForScannedFiles( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyMap() ) )
				.thenReturn( Game.name("name").build() );

		//traverse all test directories
		int found = scanner.scan( paths, true, database, false, false, "machine", false, false, true, false, false, false );

		//there are 3 tapes in the test directories
		assertEquals( 3, found );
	}

	@Test @SuppressWarnings("unchecked")
	public void testSearchForLaserdiscsOnly() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		when( gameBuilder
				.createGameObjectForScannedFiles( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyMap() ) )
				.thenReturn( Game.name("name").build() );

		//traverse all test directories
		int found = scanner.scan( paths, true, database, false, false, "machine", false, false, false, true, false, false );

		//there is 3 laserdiscs in the test directories
		assertEquals( 3, found );
	}

	@Test @SuppressWarnings("unchecked")
	public void testSearchForAllMediaWithTraverseOptionOn() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		when( gameBuilder
				.createGameObjectForScannedFiles( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyMap() ) )
				.thenReturn( Game.name("name").build() );

		//traverse all test directories
		int found = scanner.scan( paths, true, database, false, false, "machine", true, true, true, true, false, false );

		//there are 14 total in the test directories
		assertEquals( 14, found );
	}

	@Test @SuppressWarnings("unchecked")
	public void testSearchForAllMediaWithTraverseOptionOnAndBaseDirectory() throws GamePersistenceException, IOException
	{
		//traverse dir1 directory
		//split the path into <base> and <rest of path>
		File relativePath = new File( getClass().getResource( "dir1" ).getFile() );
		File base = null;
		File relative = new File( "" );
		for( File tmpPath = relativePath; tmpPath != null; )
		{
			base = tmpPath;
			relative = new File( new File( tmpPath.getName() ), relative.toString() );
			tmpPath = tmpPath.getParentFile();
		}

		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, base.toString() );

		when( gameBuilder
				.createGameObjectForScannedFiles( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyMap() ) )
				.thenReturn( Game.name("name").build() );

		int found = scanner.scan( new String[] { relative.toString() }, true, database, false, false, "machine", true, true, true, true, false, false );

		//there are 9 total in the dir1 test directory
		assertEquals( 9, found );
	}

	@Test @SuppressWarnings("unchecked")
	public void testSearchForAllMediaWithTraverseOptionOff() throws GamePersistenceException, IOException
	{
		FileScanner scanner = new FileScanner( gamePersister, repositoryData, gameBuilder, extraDataGetter, null );

		when( gameBuilder
				.createGameObjectForScannedFiles( anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyLong(), anyMap() ) )
				.thenReturn( Game.name("name").build() );

		//don't traverse
		int found = scanner.scan( paths, false, database, false, false, "machine", true, true, true, true, false, false );

		//there are 10 total in the test directories
		assertEquals( 10, found );
	}
}
