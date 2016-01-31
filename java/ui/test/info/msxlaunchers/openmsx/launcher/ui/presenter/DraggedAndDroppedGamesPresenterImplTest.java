package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.game.scan.Scanner;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.AddDraggedAndDroppedGamesView;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.MachineLister;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class DraggedAndDroppedGamesPresenterImplTest
{
	@Mock MainPresenter mainPresenter;
	@Mock AddDraggedAndDroppedGamesView view;
	private String currentDatabase = "currentDatabase";
	private File[] files = null;
	@Mock MachineLister machineLister;
	@Mock Scanner scanner;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	@Before
	public void setUp() throws IOException
	{
		//default files list of one rom - can be overridden in individual tests
		files = new File[] { tmpFolder.newFile( "file.rom" ) };
	}

	@Test
	public void testConstructor() throws IOException
	{
		new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null() throws IOException
	{
		new DraggedAndDroppedGamesPresenterImpl( null, view, currentDatabase, files, machineLister, scanner );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null() throws IOException
	{
		new DraggedAndDroppedGamesPresenterImpl( mainPresenter, null, currentDatabase, files, machineLister, scanner );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg3Null() throws IOException
	{
		new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, null, files, machineLister, scanner );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg4Null() throws IOException
	{
		new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, null, machineLister, scanner );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg5Null() throws IOException
	{
		new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, null, scanner );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg6Null() throws IOException
	{
		new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, null );
	}

	@Test
	public void test_givenAllKindsOfFiles_whenOnRequestAddDraggedAndDroppedGamesScreen_thenDisplayScreenWithFilteredOpenMSXFiles() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		Set<File> allKindsOfFiles = new HashSet<>();

		//add openMSX files to the set
		allKindsOfFiles.addAll( FileTypeUtils.getROMExtensions().stream().map( ext -> new File( tmpFolder.getRoot(), "file." + ext ) ).collect( Collectors.toSet() ) );
		allKindsOfFiles.addAll( FileTypeUtils.getDiskExtensions().stream().map( ext -> new File( tmpFolder.getRoot(), "file." + ext ) ).collect( Collectors.toSet() ) );
		allKindsOfFiles.addAll( FileTypeUtils.getTapeExtensions().stream().map( ext -> new File( tmpFolder.getRoot(), "file." + ext ) ).collect( Collectors.toSet() ) );
		allKindsOfFiles.addAll( FileTypeUtils.getLaserdiscExtensions().stream().map( ext -> new File( tmpFolder.getRoot(), "file." + ext ) ).collect( Collectors.toSet() ) );
		allKindsOfFiles.addAll( FileTypeUtils.getZIPExtensions().stream().map( ext -> new File( tmpFolder.getRoot(), "file." + ext ) ).collect( Collectors.toSet() ) );
		allKindsOfFiles.add( tmpFolder.newFolder() );

		Set<File> openMSXFiles = new HashSet<>();
		openMSXFiles.addAll( allKindsOfFiles );

		//also add other files
		allKindsOfFiles.add( new File( tmpFolder.getRoot(), "file.abc" ) );
		allKindsOfFiles.add( new File( tmpFolder.getRoot(), "file" ) );
		allKindsOfFiles.add( new File( tmpFolder.getRoot(), "file.xy" ) );

		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, allKindsOfFiles.stream().sorted().toArray( File[]::new ), machineLister, scanner );

		presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.ARABIC, true );

		verify( view, times( 1 ) ).displayScreen( presenter, currentDatabase, Language.ARABIC, true, openMSXFiles.stream().sorted().toArray( File[]::new ), machineLister.get() );
	}

	@Test
	public void test_givenNonOpenMSXFiles_whenOnRequestAddDraggedAndDroppedGamesScreen_thenDoNotDisplayScreen() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		Set<File> nonOpenMSXFiles = new HashSet<>();

		nonOpenMSXFiles.add( new File( tmpFolder.getRoot(), "file.abc" ) );
		nonOpenMSXFiles.add( new File( tmpFolder.getRoot(), "file" ) );
		nonOpenMSXFiles.add( new File( tmpFolder.getRoot(), "file.xy" ) );

		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, nonOpenMSXFiles.stream().sorted().toArray( File[]::new ), machineLister, scanner );

		presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.ARABIC, true );

		verify( view, never() ).displayScreen( presenter, currentDatabase, Language.ARABIC, true, nonOpenMSXFiles.stream().sorted().toArray( File[]::new ), machineLister.get() );
	}

	@Test( expected = LauncherException.class )
	public void test_machineListerThrowsInvalidDirectoryException_whenOnRequestAddDraggedAndDroppedGamesScreen_thenThrowLauncherException() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		when( machineLister.get() ).thenThrow( new InvalidMachinesDirectoryException() );

		try
		{
			presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.ENGLISH, false );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_INVALID_MACHINES_DIRECTORY, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_machineListerThrowsIOException_whenOnRequestAddDraggedAndDroppedGamesScreen_thenThrowLauncherException() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		when( machineLister.get() ).thenThrow( new IOException() );

		try
		{
			presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.ENGLISH, false );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void test_givenValidDirectoriesAndOpenMSXFiles_whenOnRequestAddDraggedAndDroppedGamesAddAction_thenReturnTotalFoundGames() throws GamePersistenceException, IOException, LauncherException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		int totalFound = new Random().nextInt( 500 );

		when( scanner.scan( any( String[].class ), anyBoolean(), anyString(), anyBoolean(), anyBoolean(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean() ) ).thenReturn( totalFound );

		//initialise filteredFiles array
		presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.DUTCH, false );

		assertEquals( totalFound, presenter.onRequestAddDraggedAndDroppedGamesAddAction( true, true, "machine" ) );
	}

	@Test
	public void test_givenScannerThrowsGamePersistenceExceptionGameNullName_whenOnRequestAddDraggedAndDroppedGamesAddAction_thenReturnZeroFoundGames() throws GamePersistenceException, IOException, LauncherException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		when( scanner.scan( any( String[].class ), anyBoolean(), anyString(), anyBoolean(), anyBoolean(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean() ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME ) );

		//initialise filteredFiles array
		presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.DUTCH, false );

		assertEquals( 0, presenter.onRequestAddDraggedAndDroppedGamesAddAction( false, false, "machine" ) );
	}

	@Test( expected = LauncherException.class )
	public void test_givenScannerThrowsGamePersistenceExceptionDatabaseNullName_whenOnRequestAddDraggedAndDroppedGamesAddAction_thenThrowLauncherException() throws GamePersistenceException, IOException, LauncherException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		when( scanner.scan( any( String[].class ), anyBoolean(), anyString(), anyBoolean(), anyBoolean(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean() ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NULL_NAME ) );

		//initialise filteredFiles array
		presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.DUTCH, false );

		try
		{
			presenter.onRequestAddDraggedAndDroppedGamesAddAction( false, true, "machine" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_NULL_NAME, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_givenScannerThrowsGamePersistenceExceptionDatabaseMaxBackupsReached_whenOnRequestAddDraggedAndDroppedGamesAddAction_thenThrowLauncherException() throws GamePersistenceException, IOException, LauncherException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		when( scanner.scan( any( String[].class ), anyBoolean(), anyString(), anyBoolean(), anyBoolean(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean() ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_MAX_BACKUPS_REACHED ) );

		//initialise filteredFiles array
		presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.FRENCH, false );

		try
		{
			presenter.onRequestAddDraggedAndDroppedGamesAddAction( false, true, "machine" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_MAX_BACKUPS_REACHED, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_givenScannerThrowsGamePersistenceExceptionDatabaseDatabaseAlreadyExists_whenOnRequestAddDraggedAndDroppedGamesAddAction_thenThrowLauncherException() throws GamePersistenceException, IOException, LauncherException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		when( scanner.scan( any( String[].class ), anyBoolean(), anyString(), anyBoolean(), anyBoolean(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean() ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS ) );

		//initialise filteredFiles array
		presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.PERSIAN, true );

		try
		{
			presenter.onRequestAddDraggedAndDroppedGamesAddAction( true, false, "machine" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_ALREADY_EXISTS, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_givenScannerThrowsGamePersistenceExceptionDatabaseDatabaseNotFound_whenOnRequestAddDraggedAndDroppedGamesAddAction_thenThrowLauncherException() throws GamePersistenceException, IOException, LauncherException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		when( scanner.scan( any( String[].class ), anyBoolean(), anyString(), anyBoolean(), anyBoolean(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean() ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND ) );

		//initialise filteredFiles array
		presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.JAPANESE, true );

		try
		{
			presenter.onRequestAddDraggedAndDroppedGamesAddAction( false, false, "machine" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_givenScannerThrowsIOException_whenOnRequestAddDraggedAndDroppedGamesAddAction_thenThrowLauncherException() throws GamePersistenceException, IOException, LauncherException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		when( scanner.scan( any( String[].class ), anyBoolean(), anyString(), anyBoolean(), anyBoolean(), anyString(), anyBoolean(), anyBoolean(),
				anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean() ) ).thenThrow( new IOException() );

		//initialise filteredFiles array
		presenter.onRequestAddDraggedAndDroppedGamesScreen( Language.JAPANESE, true );

		try
		{
			presenter.onRequestAddDraggedAndDroppedGamesAddAction( true, true, "machine" );
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void test_whenOnRequestInterruptFillDatabaseProcess_thenCallScannerInterrupt()
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		presenter.onRequestInterruptFillDatabaseProcess();

		verify( scanner, times( 1 ) ).interrupt();
	}

	@Test
	public void test_whenOnUpdateViewedDatabase_thenCallMainPresenterOnUpdateViewedDatabase() throws LauncherException
	{
		DraggedAndDroppedGamesPresenter presenter = new DraggedAndDroppedGamesPresenterImpl( mainPresenter, view, currentDatabase, files, machineLister, scanner );

		String database = "database";

		presenter.onUpdateViewedDatabase( database );

		verify( mainPresenter, times( 1 ) ).onUpdateViewedDatabase( database );
	}
}