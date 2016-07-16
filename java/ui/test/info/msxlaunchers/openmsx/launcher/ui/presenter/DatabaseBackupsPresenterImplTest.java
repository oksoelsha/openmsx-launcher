package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.ui.view.DatabaseBackupsView;

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
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class DatabaseBackupsPresenterImplTest
{
	@Mock LauncherPersistence launcherPersistence;
	@Mock DatabaseBackupsView view;
	@Mock GamePersister gamePersister;
	@Mock DatabaseManagerPresenter databaseManagerPresenter;

	private DatabaseBackupsPresenterImpl presenter;
	private String database = "database";
	private Timestamp timestamp = new Timestamp( 1 );
	private DatabaseBackup databaseBackup = new DatabaseBackup( database, timestamp );
	private Set<DatabaseBackup> backups = Stream.of( databaseBackup ).collect( Collectors.toSet() );

	@Before
	public void setUp() throws IOException
	{
		when( launcherPersistence.getGamePersister() ).thenReturn( gamePersister );
		presenter = new DatabaseBackupsPresenterImpl( view, launcherPersistence, databaseManagerPresenter, database, backups );
	}

	@Test
	public void testConstructor() throws IOException
	{
		new DatabaseBackupsPresenterImpl( view, launcherPersistence, databaseManagerPresenter, database, backups );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null() throws IOException
	{
		new DatabaseBackupsPresenterImpl( null, launcherPersistence, databaseManagerPresenter, database, backups );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null() throws IOException
	{
		new DatabaseBackupsPresenterImpl( view, null, databaseManagerPresenter, database, backups );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg3Null() throws IOException
	{
		new DatabaseBackupsPresenterImpl( view, launcherPersistence, null, database, backups );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg4Null() throws IOException
	{
		new DatabaseBackupsPresenterImpl( view, launcherPersistence, databaseManagerPresenter, null, backups );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg5Null() throws IOException
	{
		new DatabaseBackupsPresenterImpl( view, launcherPersistence, databaseManagerPresenter, database, null );
	}

	@Test
	public void test_WhenCallOnRequestDatabaseBackupsScreen_ThenViewDisplayScreenIsCalled()
	{
		presenter.onRequestDatabaseBackupsScreen( Language.GERMAN, false );

		verify( view, times( 1 ) ).displayScreen( presenter, Language.GERMAN, false, backups );
	}

	@Test
	public void test_WhenCallOnRequestDeleteBackup_ThenGamePersisterDeleteBackupIsCalled() throws LauncherException, GamePersistenceException
	{
		presenter.onRequestDeleteBackup( timestamp );

		verify( gamePersister, times( 1 ) ).deleteBackup( databaseBackup );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenBackupNotFound_WhenCallOnRequestDeleteBackup_ThenThrowException() throws LauncherException, GamePersistenceException
	{
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.BACKUP_NOT_FOUND ) ).when( gamePersister ).deleteBackup( databaseBackup );

		try
		{
			presenter.onRequestDeleteBackup( timestamp );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_BACKUP_NOT_FOUND, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_GivenIOIssue_WhenCallOnRequestDeleteBackup_ThenThrowException() throws LauncherException, GamePersistenceException
	{
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) ).when( gamePersister ).deleteBackup( databaseBackup );

		try
		{
			presenter.onRequestDeleteBackup( timestamp );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void test_WhenCallOnRequestRestoreBackup_ThenGamePersisterRestoreBackupIsCalled() throws LauncherException, GamePersistenceException
	{
		presenter.onRequestRestoreBackup( timestamp );

		verify( gamePersister, times( 1 ) ).restoreBackup( databaseBackup );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenBackupNotFound_WhenCallOnRequestRestoreBackup_ThenThrowException() throws LauncherException, GamePersistenceException
	{
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.BACKUP_NOT_FOUND ) ).when( gamePersister ).restoreBackup( databaseBackup );

		try
		{
			presenter.onRequestRestoreBackup( timestamp );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_BACKUP_NOT_FOUND, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_GivenIOIssue_WhenCallOnRequestRestoreBackup_ThenThrowException() throws LauncherException, GamePersistenceException
	{
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) ).when( gamePersister ).restoreBackup( databaseBackup );

		try
		{
			presenter.onRequestRestoreBackup( timestamp );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void test_GivenTimestamp_WhenCallUpdateDatabaseAndBackupsView_ThenViewUpdateIsCalled()
	{
		presenter.updateDatabaseAndBackupsView( timestamp );

		verify( databaseManagerPresenter, times( 1 ) ).updateDatabaseAndBackupsView( database );
	}

	@Test
	public void test_GivenDatabase_WhenCallUpdateDatabaseAndBackupsView_ThenViewUpdateIsCalled()
	{
		presenter.updateDatabaseAndBackupsView( databaseBackup );

		verify( databaseManagerPresenter, times( 1 ) ).updateDatabaseAndBackupsView( database );
	}

	@Test
	public void test_WhenCallUpdateDatabaseInfoView_ThenViewUpdateIsCalled()
	{
		presenter.updateDatabaseInfoView();

		verify( databaseManagerPresenter, times( 1 ) ).updateDatabaseInfoView();
	}

	@Test
	public void test_WhenCallUpdateViewedDatabase_ThenViewUpdateIsCalled() throws LauncherException
	{
		presenter.viewRestoredDatabase( timestamp );

		verify( databaseManagerPresenter, times( 1 ) ).viewRestoredDatabase( database );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenUpdateViewedDatabaseThrowsException_WhenCallUpdateViewedDatabase_ThenThrowException() throws LauncherException
	{
		Mockito.doThrow( new LauncherException( LauncherExceptionCode.ERR_IO ) ).when( databaseManagerPresenter ).viewRestoredDatabase( database );

		presenter.viewRestoredDatabase( timestamp );
	}

	@Test
	public void test_WhenCallOnRequestBackupDatabase_ThenGamePersisterBackupDatabaseIsCalled() throws LauncherException, GamePersistenceException
	{
		presenter.onRequestBackupDatabase();

		verify( gamePersister, times( 1 ) ).backupDatabase( database );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenMaxDatabasesReached_WhenCallOnRequestBackupDatabase_ThenThrowException() throws LauncherException, GamePersistenceException
	{
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_MAX_BACKUPS_REACHED ) ).when( gamePersister ).backupDatabase( database );

		try
		{
			presenter.onRequestBackupDatabase();
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_MAX_BACKUPS_REACHED, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_GivenIOIssue_WhenCallOnRequestBackupDatabase_ThenThrowException() throws LauncherException, GamePersistenceException
	{
		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) ).when( gamePersister ).backupDatabase( database );

		try
		{
			presenter.onRequestBackupDatabase();
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}
}