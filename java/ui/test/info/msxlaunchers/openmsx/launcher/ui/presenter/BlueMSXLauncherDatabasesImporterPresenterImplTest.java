package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.importer.DatabaseImporter;
import info.msxlaunchers.openmsx.launcher.importer.DatabaseImporterFactory;
import info.msxlaunchers.openmsx.launcher.ui.view.BlueMSXLauncherDatabaseImporterView;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.MachineLister;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith( MockitoJUnitRunner.class )
public class BlueMSXLauncherDatabasesImporterPresenterImplTest
{
	@Mock MainPresenter mainPresenter;
	@Mock MachineLister machineLister;
	@Mock BlueMSXLauncherDatabaseImporterView view;
	@Mock DatabaseImporter databaseImporter;
	@Mock DatabaseImporterFactory databaseImporterFactory;
	@Mock Set<String> importedDatabases;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	private BlueMSXLauncherDatabasesImporterPresenterImpl presenter;

	private File databases[];
	private String path = "path";
	private String[] databaseNames;

	@Before
	public void setUp() throws IOException
	{
		presenter = new BlueMSXLauncherDatabasesImporterPresenterImpl( mainPresenter, machineLister, view, databaseImporterFactory );

		databaseNames = new String[2];
		databaseNames[0] = "databaseName1";
		databaseNames[1] = "databaseName2";
		databases = new File[databaseNames.length];
		for(int ix = 0; ix < databases.length; ix++)
		{
			databases[ix] = new File( path, databaseNames[ix] + ".db" );
		}
	}

	@Test
	public void testConstructor() throws IOException
	{
		new BlueMSXLauncherDatabasesImporterPresenterImpl( mainPresenter, machineLister, view, databaseImporterFactory );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null() throws IOException
	{
		new BlueMSXLauncherDatabasesImporterPresenterImpl( null, machineLister, view, databaseImporterFactory );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null() throws IOException
	{
		new BlueMSXLauncherDatabasesImporterPresenterImpl( mainPresenter, null, view, databaseImporterFactory );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg3Null() throws IOException
	{
		new BlueMSXLauncherDatabasesImporterPresenterImpl( mainPresenter, machineLister, null, databaseImporterFactory );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg4Null() throws IOException
	{
		new BlueMSXLauncherDatabasesImporterPresenterImpl( mainPresenter, machineLister, view, null );
	}

	@Test
	public void test_WhenCallOnRequestImportBlueMSXLauncherDatabasesScreen_ThenViewDisplayScreenIsCalled() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		presenter.onRequestImportBlueMSXLauncherDatabasesScreen( Language.FINNISH, false );

		verify( view, times( 1 ) ).displayScreen( Language.FINNISH, false, machineLister.get() );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenMachineListerThrowsInvalidMachineDirException_WhenCallOnRequestImportBlueMSXLauncherDatabasesScreen_ThenThrowException() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		when( machineLister.get() ).thenThrow( new InvalidMachinesDirectoryException() );

		presenter.onRequestImportBlueMSXLauncherDatabasesScreen( Language.JAPANESE, false );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenMachineListerThrowsIOException_WhenCallOnRequestImportBlueMSXLauncherDatabasesScreen_ThenThrowException() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		when( machineLister.get() ).thenThrow( new IOException() );

		presenter.onRequestImportBlueMSXLauncherDatabasesScreen( Language.KOREAN, false );
	}

	@Test
	public void test_WhenCallOnRequestImportBlueMSXLauncherDatabasesAction_ThenBlueMSXLauncherDatabaseImporterIsCalled() throws LauncherException, IOException
	{
		when( databaseImporterFactory.create( "machine" ) ).thenReturn( databaseImporter );

		presenter.onRequestImportBlueMSXLauncherDatabasesAction( path, databaseNames, "machine" );

		verify( databaseImporter, times( 1 ) ).importDatabases( aryEq( databases ), any( BlueMSXLauncherDatabasesImporterActionDecider.class ) );
	}

	@Test( expected = LauncherException.class )
	public void test_GivenImportThrowsIOException_WhenCallOnRequestImportBlueMSXLauncherDatabasesAction_ThenExceptionIsThrown() throws LauncherException, IOException
	{
		when( databaseImporterFactory.create( "machine" ) ).thenReturn( databaseImporter );
		when( databaseImporter.importDatabases( aryEq( databases ), any( BlueMSXLauncherDatabasesImporterActionDecider.class ) ) ).thenThrow( new IOException() );

		presenter.onRequestImportBlueMSXLauncherDatabasesAction( path, databaseNames, "machine" );
	}

	@Test
	public void test_WhenCallOnRequestImportBlueMSXLauncherDatabasesAction_ThenMainPresenterIsUsed() throws LauncherException, IOException
	{
		int randomSize = new Random().nextInt( 50 ) + 1;

		when( databaseImporterFactory.create( "machine" ) ).thenReturn( databaseImporter );
		when( databaseImporter.importDatabases( eq( databases ), any( BlueMSXLauncherDatabasesImporterActionDecider.class ) ) ).thenReturn( importedDatabases );
		when( importedDatabases.size() ).thenReturn( randomSize );

		int result = presenter.onRequestImportBlueMSXLauncherDatabasesAction( path, databaseNames, "machine" );

		verify( mainPresenter, times( 1 ) ).onAcceptImportBlueMSXLauncherDatabasesAction( importedDatabases );		
		assertEquals( randomSize, result );
	}

	@Test
	public void test_whenCallOnGetDatabasesInDirectory_thenReturnBlueMSXLauncherDatabases() throws IOException
	{
		String extension = ".db";
		String db1 = "database1";
		String db2 = "database2";

		tmpFolder.newFolder( "folder" );
		tmpFolder.newFile( db1 + extension );
		tmpFolder.newFile( db2 + extension );
		tmpFolder.newFile( "non_db1.abc" );
		tmpFolder.newFile( "non_db2.xyz" );
		tmpFolder.newFile( "non_db3" );

		Set<String> databases = presenter.onGetDatabasesInDirectory( tmpFolder.getRoot() );

		assertEquals( 2, databases.size() );
		assertTrue( databases.contains( db1 ) );
		assertTrue( databases.contains( db2 ) );
	}
}