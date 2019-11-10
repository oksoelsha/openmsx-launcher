package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.ui.view.DatabaseManagerView;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class DatabaseManagerPresenterImplTest
{
	@Mock LauncherPersistence launcherPersistence;
	@Mock DatabaseManagerView view;
	@Mock MainPresenter mainPresenter;
	@Mock GamePersister gamePersister;
	@Mock DatabaseBackupsPresenterFactory databaseBackupsPresenterFactory;
	@Mock DatabaseBackupsPresenter databaseBackupsPresenter;

	private DatabaseManagerPresenterImpl presenter;
	private Set<String> databases = Stream.of( "database1" ).collect( Collectors.toSet() );
	private String database = "database";

	@Before
	public void setUp() throws IOException
	{
		when( launcherPersistence.getGamePersister() ).thenReturn( gamePersister );
		presenter = new DatabaseManagerPresenterImpl( view, mainPresenter, launcherPersistence, databaseBackupsPresenterFactory, databases, Language.JAPANESE, true );
	}

	@Test
	public void testConstructor() throws IOException
	{
		new DatabaseManagerPresenterImpl( view, mainPresenter, launcherPersistence, databaseBackupsPresenterFactory, databases, Language.FRENCH, false );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null() throws IOException
	{
		new DatabaseManagerPresenterImpl( null, mainPresenter, launcherPersistence, databaseBackupsPresenterFactory, databases, Language.FRENCH, false );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null() throws IOException
	{
		new DatabaseManagerPresenterImpl( view, null, launcherPersistence, databaseBackupsPresenterFactory, databases, Language.FRENCH, false );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg3Null() throws IOException
	{
		new DatabaseManagerPresenterImpl( view, mainPresenter, null, databaseBackupsPresenterFactory, databases, Language.FRENCH, false );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg4Null() throws IOException
	{
		new DatabaseManagerPresenterImpl( view, mainPresenter, launcherPersistence, null, databases, Language.FRENCH, false );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg5Null() throws IOException
	{
		new DatabaseManagerPresenterImpl( view, mainPresenter, launcherPersistence, databaseBackupsPresenterFactory, null, Language.FRENCH, false );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg6Null() throws IOException
	{
		new DatabaseManagerPresenterImpl( view, mainPresenter, launcherPersistence, databaseBackupsPresenterFactory, databases, null, false );
	}

	@Test
	public void test_WhenCallOnRequestDatabaseManagerScreen_ThenViewDisplayScreenIsCalled()
	{
		presenter.onRequestDatabaseManagerScreen();

		verify( view, times( 1 ) ).displayScreen( any( DatabaseManagerPresenterImpl.class ), any( Language.class ), anyBoolean(), anySet() );
	}

	@Test
	public void test_WhenCallOnRequestDeleteDatabase_ThenGamePersisterDeleteDatabaseIsCalled() throws LauncherException, GamePersistenceException
	{
		presenter.onRequestDeleteDatabase( database );

		verify( gamePersister, times( 1 ) ).deleteDatabase( database );
		verify( mainPresenter, times( 1 ) ).onRequestDeleteDatabaseAction( database );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenNonExistingDatabase_WhenCallOnRequestDeleteDatabase_ThenExceptionIsThrown() throws LauncherException, GamePersistenceException
	{
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, database ) ).when( gamePersister ).deleteDatabase( database );

		presenter.onRequestDeleteDatabase( database );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenIOException_WhenCallOnRequestDeleteDatabase_ThenExceptionIsThrown() throws LauncherException, GamePersistenceException
	{
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) ).when( gamePersister ).deleteDatabase( database );

		presenter.onRequestDeleteDatabase( database );
	}

	@Test
	public void test_WhenCallOnRequestRenameDatabaseScreen_ThenViewDisplayScreenIsCalled() throws LauncherException
	{
		presenter.onRequestRenameDatabaseScreen( database );

		verify( view, times( 1 ) ).displayRenameDatabaseScreen( eq( presenter ), eq( database ), any( Language.class ), anyBoolean() );
	}

	@Test
	public void test_WhenCallOnRequestRenameDatabaseAction_ThenGamePersisterRenameDatabaseIsCalled() throws LauncherException, GamePersistenceException
	{
		String newDatabase = "newDatabase";

		presenter.onRequestRenameDatabaseAction( database, newDatabase );

		verify( gamePersister, times( 1 ) ).renameDatabase( database, newDatabase );
		verify( mainPresenter, times( 1 ) ).onRequestRenameDatabaseAction( database, newDatabase );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenNonExistingDatabase_WhenCallOnRequestRenameDatabaseAction_ThenExceptionIsThrown() throws LauncherException, GamePersistenceException
	{
		String newDatabase = "newDatabase";
		
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, database ) ).when( gamePersister ).renameDatabase( database, newDatabase );

		try
		{
			presenter.onRequestRenameDatabaseAction( database, newDatabase );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_GivenDatabaseWithSameName_WhenCallOnRequestRenameDatabaseAction_ThenExceptionIsThrown() throws LauncherException, GamePersistenceException
	{
		String newDatabase = "newDatabase";
		
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS, database ) ).when( gamePersister ).renameDatabase( database, newDatabase );

		try
		{
			presenter.onRequestRenameDatabaseAction( database, newDatabase );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_ALREADY_EXISTS, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_GivenIOException_WhenCallOnRequestRenameDatabaseAction_ThenExceptionIsThrown() throws LauncherException, GamePersistenceException
	{
		String newDatabase = "newDatabase";

		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) ).when( gamePersister ).renameDatabase( database, newDatabase );

		presenter.onRequestRenameDatabaseAction( database, newDatabase );
	}

	@Test
	public void test_WhenCallOnRequestDeleteAllBackups_ThenGamePersisterDeleteBackupIsCalled() throws LauncherException, GamePersistenceException
	{
		presenter.onRequestDatabaseManagerScreen();

		DatabaseBackup backup = new DatabaseBackup( "dbname", new Timestamp( 789 ) );
		Set<DatabaseBackup> backups = Stream.of( backup ).collect( Collectors.toSet() );

		when( gamePersister.getBackups( anyString() ) ).thenReturn( backups );

		presenter.onRequestDeleteAllBackups();

		verify( gamePersister, times( databases.size() ) ).deleteBackup( backup );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenDeleteBackupThrowsException_WhenCallOnRequestDeleteAllBackups_ThenThrowException() throws LauncherException, GamePersistenceException
	{
		presenter.onRequestDatabaseManagerScreen();

		DatabaseBackup backup = new DatabaseBackup( "dbname", new Timestamp( 789 ) );
		Set<DatabaseBackup> backups = Stream.of( backup ).collect( Collectors.toSet() );

		when( gamePersister.getBackups( anyString() ) ).thenReturn( backups );
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) ).when( gamePersister ).deleteBackup( backup );

		try
		{
			presenter.onRequestDeleteAllBackups();
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void test_WhenCallUpdateDatabaseAndBackupsView_ThenViewUpdateDatabaseAndBackupsIsCalled()
	{
		DatabaseBackup backup1 = new DatabaseBackup( "dbname1", new Timestamp( 234 ) );
		DatabaseBackup backup2 = new DatabaseBackup( "dbname2", new Timestamp( 789 ) );
		Set<DatabaseBackup> backups = Stream.of( backup1, backup2 ).collect( Collectors.toSet() );

		when( gamePersister.getBackups( database ) ).thenReturn( backups );

		presenter.updateDatabaseAndBackupsView( database );

		verify( view, times( 1 ) ).updateDatabaseAndBackups( database, 0, backups.size() );
	}

	@Test
	public void test_WhenCallUpdateDatabaseInfoView_ThenViewUpdateDatabaseInfoIsCalled()
	{
		presenter.onRequestDatabaseManagerScreen();

		presenter.updateDatabaseInfoView();

		verify( view, times( 1 ) ).updateDatabaseInfo( any( DatabaseInfo.class ) );
	}

	@Test
	public void test_WhenCallGetDatabaseInfo_ThenReturnInstanceOfDatabaseInfo() throws GamePersistenceException
	{
		DatabaseInfo databaseInfo = presenter.getDatabaseInfo( databases );

		verify( gamePersister, times( databases.size() ) ).getGames( anyString() );
		verify( gamePersister, times( databases.size() ) ).getBackups( anyString() );

		assertEquals( databases.size(), databaseInfo.getTotalDatabases() );
		assertEquals( 0, databaseInfo.getTotalGames() );
		assertEquals( 0, databaseInfo.getTotalBackups() );
	}

	@Test
	public void test_GivenGetGamesThrowsException_WhenCallGetDatabaseInfo_ThenReturnInstanceOfDatabaseInfo() throws GamePersistenceException
	{
		DatabaseInfo databaseInfo = presenter.getDatabaseInfo( databases );

		assertEquals( databases.size(), databaseInfo.getTotalDatabases() );
		assertEquals( 0, databaseInfo.getTotalGames() );
		assertEquals( 0, databaseInfo.getTotalBackups() );
	}

	@Test
	public void test_WhenCallOnRequestDatabaseBackupsScreen_ThenBackupsPresenterOnRequestDatabaseBackupsScreenIsCalled()
	{
		DatabaseBackup backup = new DatabaseBackup( database, new Timestamp( 789 ) );
		Set<DatabaseBackup> backups = Stream.of( backup ).collect( Collectors.toSet() );

		when( gamePersister.getBackups( database ) ).thenReturn( backups );
		when( databaseBackupsPresenterFactory.create( any( DatabaseManagerPresenter.class ), anyString(), anySet() ) ).thenReturn( databaseBackupsPresenter );
		presenter.onRequestDatabaseBackupsScreen( database );

		verify( databaseBackupsPresenter, times( 1 ) ).onRequestDatabaseBackupsScreen( any( Language.class ), anyBoolean() );
	}

	@Test
	public void test_WhenCallUpdateViewedDatabase_ThenMainPresenteronUpdateViewedDatabaseIsCalled() throws LauncherException
	{
		presenter.viewRestoredDatabase( database );

		verify( mainPresenter, times( 1 ) ).onViewUpdatedDatabase( database );
	}
}