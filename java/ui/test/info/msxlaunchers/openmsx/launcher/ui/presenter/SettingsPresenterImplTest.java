package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.SettingsView;
import info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith( MockitoJUnitRunner.class )
public class SettingsPresenterImplTest
{
	@Mock SettingsView settingsView;
	@Mock SettingsPersister settingsPersister;
	@Mock MainPresenter mainPresenter;
	@Mock PlatformViewProperties platformViewProperties;

	@Test
	public void testConstructor() throws IOException
	{
		new SettingsPresenterImpl( settingsView, settingsPersister, mainPresenter, platformViewProperties );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null() throws IOException
	{
		new SettingsPresenterImpl( null, settingsPersister, mainPresenter, platformViewProperties );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null() throws IOException
	{
		new SettingsPresenterImpl( settingsView, null, mainPresenter, platformViewProperties );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg3Null() throws IOException
	{
		new SettingsPresenterImpl( settingsView, settingsPersister, null, platformViewProperties );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg4Null() throws IOException
	{
		new SettingsPresenterImpl( settingsView, settingsPersister, mainPresenter, null );
	}

	@Test
	public void testOnRequestSettingsScreen() throws IOException
	{
		SettingsPresenterImpl presenter = new SettingsPresenterImpl( settingsView, settingsPersister, mainPresenter, platformViewProperties );

		String defaultDatabase = "database";
		Set<String> databases = new HashSet<String>();
		Language language = Language.FRENCH;
		String languageCode = "FR";

		Settings settings = new Settings( null, null, null, defaultDatabase, null, false );

		presenter.onRequestSettingsScreen( settings, databases, language, languageCode, false );

		verify( settingsView, times( 1 ) ).displaySettings( settings, databases, language, languageCode, false );
	}

	@Test
	public void testOnRequestSettingsActionMachinesInsideOpenMSX() throws IOException, LauncherException
	{
		SettingsPresenterImpl presenter = new SettingsPresenterImpl( settingsView, settingsPersister, mainPresenter, platformViewProperties );

		String openMSXPath = "openMSXPath";
		String machinesPath = "machinesPath";
		String screenshotsPath = "screenshotsPath";
		String defaultDatabase = "database";
		String language = "ENGLISH";

		when( platformViewProperties.isMachinesFolderInsideOpenMSX() ).thenReturn( true );
		when( platformViewProperties.getOpenMSXMachinesPath() ).thenReturn( machinesPath );

		Settings settings = new Settings( openMSXPath, new File( openMSXPath, machinesPath ).getAbsolutePath(), screenshotsPath, defaultDatabase, Language.valueOf( language ), false );

		presenter.onRequestSettingsAction( openMSXPath, screenshotsPath, defaultDatabase, language, false );

		verify( settingsPersister, times( 1 ) ).saveSettings( settings );
		verify( mainPresenter, times( 1 ) ).onAcceptSettingsAction( settings );		
	}

	@Test
	public void testOnRequestSettingsActionMachinesOutsideOpenMSX() throws IOException, LauncherException
	{
		SettingsPresenterImpl presenter = new SettingsPresenterImpl( settingsView, settingsPersister, mainPresenter, platformViewProperties );

		String openMSXPath = "openMSXPath";
		String machinesPath = "machinesPath";
		String screenshotsPath = "screenshotsPath";
		String defaultDatabase = "database";
		String language = "ENGLISH";

		when( platformViewProperties.isMachinesFolderInsideOpenMSX() ).thenReturn( false );
		when( platformViewProperties.getOpenMSXMachinesPath() ).thenReturn( machinesPath );

		Settings settings = new Settings( openMSXPath, machinesPath, screenshotsPath, defaultDatabase, Language.valueOf( language ), false );

		presenter.onRequestSettingsAction( openMSXPath, screenshotsPath, defaultDatabase, language, false );

		verify( settingsPersister, times( 1 ) ).saveSettings( settings );
		verify( mainPresenter, times( 1 ) ).onAcceptSettingsAction( settings );		
	}

	@Test
	public void testOnRequestSettingsActionNullLanguage() throws IOException, LauncherException
	{
		SettingsPresenterImpl presenter = new SettingsPresenterImpl( settingsView, settingsPersister, mainPresenter, platformViewProperties );

		String openMSXPath = "openMSXPath";
		String machinesPath = "machinesPath";
		String screenshotsPath = "screenshotsPath";
		String defaultDatabase = "database";

		when( platformViewProperties.isMachinesFolderInsideOpenMSX() ).thenReturn( false );
		when( platformViewProperties.getOpenMSXMachinesPath() ).thenReturn( machinesPath );

		Settings settings = new Settings( openMSXPath, machinesPath, screenshotsPath, defaultDatabase, null, false );

		presenter.onRequestSettingsAction( openMSXPath, screenshotsPath, defaultDatabase, null, false );

		verify( settingsPersister, times( 1 ) ).saveSettings( settings );
		verify( mainPresenter, times( 1 ) ).onAcceptSettingsAction( settings );		
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestSettingsActionIOException() throws IOException, LauncherException
	{
		SettingsPresenterImpl presenter = new SettingsPresenterImpl( settingsView, settingsPersister, mainPresenter, platformViewProperties );

		String openMSXPath = "openMSXPath";
		String screenshotsPath = "screenshotsPath";
		String defaultDatabase = "database";

		Mockito.doThrow( new IOException() ).when( settingsPersister ).saveSettings( (Settings)any() );

		presenter.onRequestSettingsAction( openMSXPath, screenshotsPath, defaultDatabase, null, false );
	}
}