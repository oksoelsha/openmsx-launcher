package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.game.scan.Scanner;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.ScannerView;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.MachineLister;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
public class ScannerPresenterImplTest
{
	@Mock ScannerView view;
	@Mock MainPresenter mainPresenter;
	@Mock Scanner scanner;
	@Mock MachineLister machineLister;

	@Test
	public void testConstructor() throws IOException
	{
		new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null() throws IOException
	{
		new ScannerPresenterImpl( null, mainPresenter, scanner, machineLister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null() throws IOException
	{
		new ScannerPresenterImpl( view, null, scanner, machineLister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg3Null() throws IOException
	{
		new ScannerPresenterImpl( view, mainPresenter, null, machineLister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg4Null() throws IOException
	{
		new ScannerPresenterImpl( view, mainPresenter, scanner, null );
	}

	@Test
	public void testOnRequestFillDatabaseScreen() throws IOException, LauncherException, InvalidMachinesDirectoryException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		Set<String> databases = new HashSet<String>();
		String database = "db";

		presenter.onRequestFillDatabaseScreen( databases, database, Language.ARABIC, true );

		verify( view, times( 1 ) ).displayFillDatabase( Language.ARABIC, databases, database, machineLister.get(), true );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestFillDatabaseScreenIOException() throws IOException, LauncherException, InvalidMachinesDirectoryException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		Set<String> databases = new HashSet<String>();
		String database = "db";

		when( machineLister.get() ).thenThrow( new IOException() );

		presenter.onRequestFillDatabaseScreen( databases, database, Language.ARABIC, true );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestFillDatabaseScreenInvalidMachinesDirectoryException() throws IOException, LauncherException, InvalidMachinesDirectoryException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		Set<String> databases = new HashSet<String>();
		String database = "db";

		when( machineLister.get() ).thenThrow( new InvalidMachinesDirectoryException() );

		presenter.onRequestFillDatabaseScreen( databases, database, Language.ARABIC, true );
	}

	@Test
	public void testOnRequestFillDatabaseAction() throws IOException, LauncherException, GamePersistenceException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		String[] paths = new String[] { "path1", "path2" };
		boolean traverseSubDirectories = true;
		String database = "db";
		boolean newDatabase = true;
		boolean append = false;
		String machine = "msx";
		boolean searchROM = false;
		boolean searchDisk = true;
		boolean searchTape = false;
		boolean searchLaserdisc = true;
		boolean getNameFromOpenMSXDatabase = false;
		boolean backupDatabase = true;

		int totalFound = new Random().nextInt( 5000 );

		when( scanner.scan( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase ) )
			.thenReturn( totalFound );

		assertEquals( presenter.onRequestFillDatabaseAction( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase )
				, totalFound );
	}

	@Test
	public void testOnRequestFillDatabaseActionGameNullNameException() throws IOException, LauncherException, GamePersistenceException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		String[] paths = new String[] { "path1", "path2" };
		boolean traverseSubDirectories = true;
		String database = "db";
		boolean newDatabase = true;
		boolean append = false;
		String machine = "msx";
		boolean searchROM = false;
		boolean searchDisk = true;
		boolean searchTape = false;
		boolean searchLaserdisc = true;
		boolean getNameFromOpenMSXDatabase = false;
		boolean backupDatabase = true;

		when( scanner.scan( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase ) )
			.thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME ) );

		assertEquals( presenter.onRequestFillDatabaseAction( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase )
				, 0 );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestFillDatabaseActionDatabaseNullNameException() throws IOException, LauncherException, GamePersistenceException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		String[] paths = new String[] { "path1", "path2" };
		boolean traverseSubDirectories = true;
		String database = "db";
		boolean newDatabase = true;
		boolean append = false;
		String machine = "msx";
		boolean searchROM = false;
		boolean searchDisk = true;
		boolean searchTape = false;
		boolean searchLaserdisc = true;
		boolean getNameFromOpenMSXDatabase = false;
		boolean backupDatabase = true;

		when( scanner.scan( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase ) )
			.thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NULL_NAME ) );

		presenter.onRequestFillDatabaseAction( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestFillDatabaseActionDatabaseMaxBackupReachedException() throws IOException, LauncherException, GamePersistenceException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		String[] paths = new String[] { "path1", "path2" };
		boolean traverseSubDirectories = true;
		String database = "db";
		boolean newDatabase = true;
		boolean append = false;
		String machine = "msx";
		boolean searchROM = false;
		boolean searchDisk = true;
		boolean searchTape = false;
		boolean searchLaserdisc = true;
		boolean getNameFromOpenMSXDatabase = false;
		boolean backupDatabase = true;

		when( scanner.scan( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase ) )
			.thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_MAX_BACKUPS_REACHED, database ) );

		presenter.onRequestFillDatabaseAction( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestFillDatabaseActionDatabaseAlreadyExistsException() throws IOException, LauncherException, GamePersistenceException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		String[] paths = new String[] { "path1", "path2" };
		boolean traverseSubDirectories = true;
		String database = "db";
		boolean newDatabase = true;
		boolean append = false;
		String machine = "msx";
		boolean searchROM = false;
		boolean searchDisk = true;
		boolean searchTape = false;
		boolean searchLaserdisc = true;
		boolean getNameFromOpenMSXDatabase = false;
		boolean backupDatabase = true;

		when( scanner.scan( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase ) )
			.thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS, database ) );

		presenter.onRequestFillDatabaseAction( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestFillDatabaseActionDatabaseNotFoundException() throws IOException, LauncherException, GamePersistenceException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		String[] paths = new String[] { "path1", "path2" };
		boolean traverseSubDirectories = true;
		String database = "db";
		boolean newDatabase = true;
		boolean append = false;
		String machine = "msx";
		boolean searchROM = false;
		boolean searchDisk = true;
		boolean searchTape = false;
		boolean searchLaserdisc = true;
		boolean getNameFromOpenMSXDatabase = false;
		boolean backupDatabase = true;

		when( scanner.scan( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase ) )
			.thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, database ) );

		presenter.onRequestFillDatabaseAction( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestFillDatabaseActionIOException() throws IOException, LauncherException, GamePersistenceException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		String[] paths = new String[] { "path1", "path2" };
		boolean traverseSubDirectories = true;
		String database = "db";
		boolean newDatabase = true;
		boolean append = false;
		String machine = "msx";
		boolean searchROM = false;
		boolean searchDisk = true;
		boolean searchTape = false;
		boolean searchLaserdisc = true;
		boolean getNameFromOpenMSXDatabase = false;
		boolean backupDatabase = true;

		when( scanner.scan( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase ) )
			.thenThrow( new IOException() );

		presenter.onRequestFillDatabaseAction( paths, traverseSubDirectories, database, newDatabase, append, machine, searchROM, searchDisk, searchTape, searchLaserdisc, getNameFromOpenMSXDatabase, backupDatabase );
	}

	@Test
	public void testOnRequestInterruptFillDatabaseProcess() throws IOException, LauncherException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		presenter.onRequestInterruptFillDatabaseProcess();

		verify( scanner, times( 1 ) ).interrupt();
	}

	@Test
	public void testOnUpdateViewedDatabase() throws IOException, LauncherException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		String database = "db";

		presenter.onUpdateViewedDatabase( database );

		verify( mainPresenter, times( 1 ) ).onUpdateViewedDatabase( database );
	}

	@Test( expected = LauncherException.class )
	public void testOnUpdateViewedDatabaseLauncherException() throws IOException, LauncherException
	{
		ScannerPresenterImpl presenter = new ScannerPresenterImpl( view, mainPresenter, scanner, machineLister );

		String database = "db";

		Mockito.doThrow( new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND ) ).when( mainPresenter ).onUpdateViewedDatabase( database );

		presenter.onUpdateViewedDatabase( database );
	}
}