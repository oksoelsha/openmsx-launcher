package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.UpdateCheckerView;
import info.msxlaunchers.openmsx.launcher.updater.FileUpdateFailedException;
import info.msxlaunchers.openmsx.launcher.updater.UpdateChecker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith( MockitoJUnitRunner.class )
public class UpdateCheckerPresenterImplTest
{
	@Mock UpdateCheckerView view;
	@Mock UpdateChecker updateChecker;
	@Mock SettingsPersister settingsPersister;
	@Mock ExtraDataGetter extraDataGetter;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void testConstructor() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( null, updateChecker, extraDataGetter, settingsPersister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, null, extraDataGetter, settingsPersister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg3Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, updateChecker, null, settingsPersister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg4Null() throws IOException
	{
		new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, null );
	}

	@Test
	public void test_whenOnRequestCheckForUpdatesScreen_ThenCollaboratorsAreCalled() throws IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		presenter.onRequestCheckForUpdatesScreen( Language.ITALIAN, false );

		verify( updateChecker, times( 1 ) ).getVersions();

		verify( view, times( 1 ) ).displayScreen( Language.ITALIAN, false );
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetVersionsThrowsException_whenOnRequestCheckForUpdatesScreen_ThenThrowException() throws IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( updateChecker.getVersions() ).thenThrow( new IOException() );

		presenter.onRequestCheckForUpdatesScreen( Language.ITALIAN, false );
	}

	@Test
	public void test_givenEmptyVersionsFromServer_whenIsNewOpenMSXLauncherVersionAvailable_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		assertFalse( presenter.isNewOpenMSXLauncherVersionAvailable() );
	}

	@Test
	public void test_givenOpenMSXLauncherDownloaded_whenIsNewOpenMSXLauncherVersionDownloaded_ThenReturnTrue() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( updateChecker.isNewOpenMSXLauncherDownloaded() ).thenReturn( true );

		assertTrue( presenter.isNewOpenMSXLauncherVersionDownloaded() );
	}

	@Test
	public void test_givenOpenMSXLauncherNotDownloaded_whenIsNewOpenMSXLauncherVersionDownloaded_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( updateChecker.isNewOpenMSXLauncherDownloaded() ).thenReturn( false );

		assertFalse( presenter.isNewOpenMSXLauncherVersionDownloaded() );
	}

	@Test
	public void test_whenIsNewExtraDataVersionAvailable_ThenCollaboratorsAreCalled() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		presenter.isNewExtraDataVersionAvailable();

		verify( extraDataGetter, times( 1 ) ).getExtraDataFileVersion();
	}

	@Test
	public void test_givenUninitializedVersions_whenIsNewExtraDataVersionAvailable_ThenReturnFalse()
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		assertFalse( presenter.isNewExtraDataVersionAvailable() );
	}

	@Test
	public void test_givenExtraDataGetterThrowsException_whenIsNewExtraDataVersionAvailable_ThenReturnFalse() throws IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( extraDataGetter.getExtraDataFileVersion() ).thenThrow( new IOException() );

		assertFalse( presenter.isNewExtraDataVersionAvailable() );
	}

	@Test
	public void test_givenScreenshotsPathIsNull_whenIsNewScreenshotsVersionAvailable_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, null, null ) );

		assertFalse( presenter.isNewScreenshotsVersionAvailable() );
	}

	@Test
	public void test_givenGetSettingsThrowsException_whenIsNewScreenshotsVersionAvailable_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		assertFalse( presenter.isNewScreenshotsVersionAvailable() );
	}

	@Test
	public void test_givenScreenshotsVersionExists_whenIsNewScreenshotsVersionAvailable_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, tmpFolder.getRoot().toString(), null, null ) );

		createTempVersionFile( "1.2" );

		assertFalse( presenter.isNewScreenshotsVersionAvailable() );
	}

	@Test
	public void test_givenScreenshotsPathIsNull_whenIsScreenshotsSetInSettings_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, null, null, null ) );

		assertFalse( presenter.isScreenshotsSetInSettings() );
	}

	@Test
	public void test_givenScreenshotsVersionExists_whenIsScreenshotsSetInSettings_ThenReturnTrue() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, tmpFolder.getRoot().toString(), null, null ) );

		createTempVersionFile( "1.2" );

		assertTrue( presenter.isScreenshotsSetInSettings() );
	}

	@Test
	public void test_givenScreenshotsPathIsSetButVersionDoesNotExist_whenIsScreenshotsSetInSettings_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( settingsPersister.getSettings() ).thenReturn( new Settings( null, null, tmpFolder.getRoot().toString(), null, null ) );

		assertFalse( presenter.isScreenshotsSetInSettings() );
	}

	@Test
	public void test_givenGetSettingsThrowsException_whenIsScreenshotsSetInSettings_ThenReturnFalse() throws IOException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		when( settingsPersister.getSettings() ).thenThrow( new IOException() );

		assertFalse( presenter.isScreenshotsSetInSettings() );
	}

	@Test
	public void test_whenOnRequestUpdateExtraData_ThenCollaboratorsAreCalled() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		presenter.onRequestUpdateExtraData();

		verify( updateChecker, times( 1 ) ).getNewExtraDataFile();
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetNewExtraDataFileThrowsIOException_whenOnRequestUpdateExtraData_ThenThrowException() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		Mockito.doThrow( new IOException() ).when( updateChecker ).getNewExtraDataFile();

		presenter.onRequestUpdateExtraData();
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetNewExtraDataFileThrowsFileUpdateFailedException_whenOnRequestUpdateExtraData_ThenThrowException() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		Mockito.doThrow( new FileUpdateFailedException() ).when( updateChecker ).getNewExtraDataFile();

		presenter.onRequestUpdateExtraData();
	}

	@Test
	public void test_whenOnRequestUpdateOpenMSXLauncher_ThenCollaboratorsAreCalled() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		presenter.onRequestUpdateOpenMSXLauncher();

		verify( updateChecker, times( 1 ) ).getNewOpenMSXLauncher();
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetNewOpenMSXLauncherThrowsIOException_whenOnRequestUpdateOpenMSXLauncher_ThenThrowException() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

		Mockito.doThrow( new IOException() ).when( updateChecker ).getNewOpenMSXLauncher();

		presenter.onRequestUpdateOpenMSXLauncher();
	}

	@Test( expected = LauncherException.class )
	public void test_givenGetNewOpenMSXLauncherThrowsFileUpdateFailedException_whenOnRequestUpdateOpenMSXLauncher_ThenThrowException() throws FileUpdateFailedException, IOException, LauncherException
	{
		UpdateCheckerPresenterImpl presenter = new UpdateCheckerPresenterImpl( view, updateChecker, extraDataGetter, settingsPersister );

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