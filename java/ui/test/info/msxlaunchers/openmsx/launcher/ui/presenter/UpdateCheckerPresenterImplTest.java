package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.UpdateCheckerView;
import info.msxlaunchers.openmsx.launcher.updater.FileUpdateFailedException;
import info.msxlaunchers.openmsx.launcher.updater.UpdateChecker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyMap;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class UpdateCheckerPresenterImplTest
{
	@Mock UpdateCheckerView view;
	@Mock UpdateChecker updateChecker;
	@Mock SettingsPersister settingsPersister;
	@Mock ExtraDataGetter extraDataGetter;
	@Mock LauncherPersistence launcherPersistence;
	@Mock GamePersister gamePersister;
	@Mock MainPresenter mainPresenter;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void testConstructor() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( null, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, null, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg3Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, updateChecker, null, settingsPersister, launcherPersistence, mainPresenter );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg4Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, null, launcherPersistence, mainPresenter );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg5Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, null, mainPresenter );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg6Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, null );
	}

	@Test
	public void test_whenOnRequestCheckForUpdatesScreen_ThenCollaboratorsAreCalled() throws IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		presenter.onRequestCheckForUpdatesScreen( Language.ITALIAN, false );

		verify( updateChecker, times( 1 ) ).getVersions();

		verify( view, times( 1 ) ).displayScreen( Language.ITALIAN, false );
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetVersionsThrowsException_whenOnRequestCheckForUpdatesScreen_ThenThrowException() throws IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( updateChecker.getVersions() ).thenThrow( new IOException() );

		presenter.onRequestCheckForUpdatesScreen( Language.ITALIAN, false );
	}

	@Test
	public void test_givenEmptyVersionsFromServer_whenIsNewOpenMSXLauncherVersionAvailable_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		assertFalse( presenter.isNewOpenMSXLauncherVersionAvailable() );
	}

	@Test
	public void test_givenOpenMSXLauncherDownloaded_whenIsNewOpenMSXLauncherVersionDownloaded_ThenReturnTrue() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( updateChecker.isNewOpenMSXLauncherDownloaded() ).thenReturn( true );

		assertTrue( presenter.isNewOpenMSXLauncherVersionDownloaded() );
	}

	@Test
	public void test_givenOpenMSXLauncherNotDownloaded_whenIsNewOpenMSXLauncherVersionDownloaded_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( updateChecker.isNewOpenMSXLauncherDownloaded() ).thenReturn( false );

		assertFalse( presenter.isNewOpenMSXLauncherVersionDownloaded() );
	}

	@Test
	public void test_whenIsNewExtraDataVersionAvailable_ThenCollaboratorsAreCalled() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		presenter.isNewExtraDataVersionAvailable();

		verify( extraDataGetter, times( 1 ) ).getExtraDataFileVersion();
	}

	@Test
	public void test_givenUninitializedVersions_whenIsNewExtraDataVersionAvailable_ThenReturnFalse()
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		assertFalse( presenter.isNewExtraDataVersionAvailable() );
	}

	@Test
	public void test_givenExtraDataGetterThrowsException_whenIsNewExtraDataVersionAvailable_ThenReturnFalse() throws IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( extraDataGetter.getExtraDataFileVersion() ).thenThrow( new IOException() );

		assertFalse( presenter.isNewExtraDataVersionAvailable() );
	}

	@Test
	public void test_givenScreenshotsPathIsNull_whenIsNewScreenshotsVersionAvailable_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, null, null, false, false ) );

		assertFalse( presenter.isNewScreenshotsVersionAvailable() );
	}

	@Test
	public void test_givenGetSettingsThrowsException_whenIsNewScreenshotsVersionAvailable_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		assertFalse( presenter.isNewScreenshotsVersionAvailable() );
	}

	@Test
	public void test_givenScreenshotsVersionExists_whenIsNewScreenshotsVersionAvailable_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, tmpFolder.getRoot().toString(), null, null, false, false ) );

		createTempVersionFile( "1.2" );

		assertFalse( presenter.isNewScreenshotsVersionAvailable() );
	}

	@Test
	public void test_givenScreenshotsPathIsNull_whenIsScreenshotsSetInSettings_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, null, null, false, false ) );

		assertFalse( presenter.isScreenshotsSetInSettings() );
	}

	@Test
	public void test_givenScreenshotsVersionExists_whenIsScreenshotsSetInSettings_ThenReturnTrue() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, tmpFolder.getRoot().toString(), null, null, false, false ) );

		createTempVersionFile( "1.2" );

		assertTrue( presenter.isScreenshotsSetInSettings() );
	}

	@Test
	public void test_givenScreenshotsPathIsSetButVersionDoesNotExist_whenIsScreenshotsSetInSettings_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, tmpFolder.getRoot().toString(), null, null, false, false ) );

		assertFalse( presenter.isScreenshotsSetInSettings() );
	}

	@Test
	public void test_givenGetSettingsThrowsException_whenIsScreenshotsSetInSettings_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		assertFalse( presenter.isScreenshotsSetInSettings() );
	}

	@Test
	public void test_whenOnRequestUpdateExtraData_ThenCollaboratorsAreCalled() throws FileUpdateFailedException, IOException, GamePersistenceException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( launcherPersistence.getGamePersister() ).thenReturn( gamePersister );

		Map<String,ExtraData> extraDataMap = Collections.emptyMap();
		when( extraDataGetter.getExtraData() ).thenReturn( extraDataMap );

		presenter.onRequestUpdateExtraData();

		verify( updateChecker, times( 1 ) ).getNewExtraDataFile();
		verify( extraDataGetter, times( 1 ) ).getExtraData();
		verify( gamePersister, times( 1 ) ).updateGameExtraDataInDatabases( extraDataMap );
		verify( mainPresenter, times( 1 ) ).onUpdateExtraData();
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetNewExtraDataFileFromNetworkThrowsIOException_whenOnRequestUpdateExtraData_ThenThrowException() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		Mockito.doThrow( new IOException() ).when( updateChecker ).getNewExtraDataFile();

		try
		{
			presenter.onRequestUpdateExtraData();
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_CANNOT_CONTACT_SERVER, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetNewExtraDataFileThrowsFileUpdateFailedException_whenOnRequestUpdateExtraData_ThenThrowException() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		Mockito.doThrow( new FileUpdateFailedException() ).when( updateChecker ).getNewExtraDataFile();

		try
		{
			presenter.onRequestUpdateExtraData();
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_CANNOT_INSTALL_NEW_UPDATED_FILES, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetCurrentExtraDataFileThrowsIOException_whenOnRequestUpdateExtraData_ThenThrowException() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		Mockito.doThrow( new IOException() ).when( extraDataGetter ).getExtraData();

		try
		{
			presenter.onRequestUpdateExtraData();
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void test_givenUpdateGameExtraDataInDatabasesThrowsIOException_whenOnRequestUpdateExtraData_ThenThrowException() throws FileUpdateFailedException, IOException, GamePersistenceException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		when( launcherPersistence.getGamePersister() ).thenReturn( gamePersister );

		Mockito.doThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) ).when( gamePersister ).updateGameExtraDataInDatabases( anyMap() );

		try
		{
			presenter.onRequestUpdateExtraData();
		}
		catch( LauncherException le )
		{
			assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void test_whenOnRequestUpdateOpenMSXLauncher_ThenCollaboratorsAreCalled() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		presenter.onRequestUpdateOpenMSXLauncher();

		verify( updateChecker, times( 1 ) ).getNewOpenMSXLauncher();
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetNewOpenMSXLauncherThrowsIOException_whenOnRequestUpdateOpenMSXLauncher_ThenThrowException() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		Mockito.doThrow( new IOException() ).when( updateChecker ).getNewOpenMSXLauncher();

		presenter.onRequestUpdateOpenMSXLauncher();
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetNewOpenMSXLauncherThrowsFileUpdateFailedException_whenOnRequestUpdateOpenMSXLauncher_ThenThrowException() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister, launcherPersistence, mainPresenter );

		Mockito.doThrow( new FileUpdateFailedException() ).when( updateChecker ).getNewOpenMSXLauncher();

		presenter.onRequestUpdateOpenMSXLauncher();
	}

	private void createTempVersionFile( String version ) throws IOException
	{
		File tmpFile = tmpFolder.newFile( "version.txt" );
		PrintWriter writer = new PrintWriter( tmpFile );
		writer.println( version );
		writer.close();
	}
}