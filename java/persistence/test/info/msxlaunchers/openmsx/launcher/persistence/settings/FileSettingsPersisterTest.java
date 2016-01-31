package info.msxlaunchers.openmsx.launcher.persistence.settings;

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.settings.FileSettingsPersister;

import java.io.File;
import java.io.IOException;


import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FileSettingsPersisterTest
{
	private String settingsPath = System.getProperty( "user.dir" );

	private String openMSXFullPath = "/MSX/some folder/openmsx.exe";
	private String openMSXMachinesFullPath = "/MSX/some folder/machines";
	private String screenshotsFullPath = "/some folder/screenshots";
	private String defaultDatabase = "default database";
	
	@After
	public void cleanup()
	{
		//delete the settings file
		File file = new File( settingsPath, "settings-oml.ini" );

		file.delete();
	}

	@Test( expected = NullPointerException.class )
	public void testNullForSettingsPath() 
	{
		new FileSettingsPersister( null );
	}

	@Test
	public void testSaveLoadSettings() throws IOException
	{
		Settings settings = new Settings( openMSXFullPath,
				openMSXMachinesFullPath,
				screenshotsFullPath,
				defaultDatabase,
				Language.SPANISH );

		FileSettingsPersister fileSettingsPersister = new FileSettingsPersister( settingsPath );

		fileSettingsPersister.saveSettings( settings );

		Settings savedSettings = fileSettingsPersister.getSettings();

		assertEquals( settings, savedSettings );
	}

	@Test
	public void testLoadExistingSettings() throws IOException
	{
		String existingSettingsPath = getClass().getResource( "sample-settings" ).getFile();;

		FileSettingsPersister fileSettingsPersister = new FileSettingsPersister( existingSettingsPath );

		Settings savedSettings = fileSettingsPersister.getSettings();

		Settings correctSettings = new Settings( "/Games/MSX/openMSX/0.9.1 64bit",
				"/Games/MSX/openMSX/0.9.1 64bit/share/machines",
				"/Games/MSX/blueMSX/Launcher/extra-screenshots",
				"Games db",
				Language.CHINESE_SIMPLIFIED );

		assertEquals( correctSettings, savedSettings );
	}

	@Test
	public void testSettingsWithNullValuesPersistence() throws IOException
	{
		Settings settings;
		FileSettingsPersister fileSettingsPersister;

		//test that null openMSXFullPath will be returned as null
		settings = new Settings( null, openMSXMachinesFullPath, screenshotsFullPath, defaultDatabase, Language.ENGLISH );
		fileSettingsPersister = new FileSettingsPersister( settingsPath );
		fileSettingsPersister.saveSettings( settings );
		Settings savedSettings = fileSettingsPersister.getSettings();
		assertNull( savedSettings.getOpenMSXFullPath() );

		//test that null openMSXMachinesFullPath will be returned as null
		settings = new Settings( openMSXFullPath, null , screenshotsFullPath, defaultDatabase, Language.PORTUGUESE );
		fileSettingsPersister = new FileSettingsPersister( settingsPath );
		fileSettingsPersister.saveSettings( settings );
		savedSettings = fileSettingsPersister.getSettings();
		assertNull( savedSettings.getOpenMSXMachinesFullPath() );

		//test that null screenshotsFullPath will be returned as null
		settings = new Settings( openMSXFullPath, openMSXMachinesFullPath, null , defaultDatabase, Language.PORTUGUESE );
		fileSettingsPersister = new FileSettingsPersister( settingsPath );
		fileSettingsPersister.saveSettings( settings );
		savedSettings = fileSettingsPersister.getSettings();
		assertNull( savedSettings.getScreenshotsFullPath() );

		//test that null defaultDatabase will be returned as null
		settings = new Settings( openMSXFullPath, openMSXMachinesFullPath, screenshotsFullPath, null , Language.PORTUGUESE );
		fileSettingsPersister = new FileSettingsPersister( settingsPath );
		fileSettingsPersister.saveSettings( settings );
		savedSettings = fileSettingsPersister.getSettings();
		assertNull( savedSettings.getDefaultDatabase() );

		//test that null language will be saved as 0=English
		settings = new Settings( openMSXFullPath, openMSXMachinesFullPath, screenshotsFullPath, defaultDatabase, null );
		fileSettingsPersister = new FileSettingsPersister( settingsPath );

		//here's an interesting problem: I needed to add a delay here to get around memory-mapped files
		//problem that happens on Windows. Here's a link with details:
		//http://stackoverflow.com/questions/3602783/file-access-synchronized-on-java-object
		try { Thread.sleep(500); } catch( InterruptedException ie ) {/* */}
		
		fileSettingsPersister.saveSettings( settings );
		savedSettings = fileSettingsPersister.getSettings();
		assertNull( savedSettings.getLanguage() );
	}

	@Test
	public void testGetNonExistentSettingsFile() throws IOException
	{
		FileSettingsPersister fileSettingsPersister = new FileSettingsPersister( "/" );

		Settings settings = fileSettingsPersister.getSettings();
		
		assertNull( settings.getOpenMSXFullPath() );
		assertNull( settings.getDefaultDatabase() );
		assertNull( settings.getLanguage() );
	}
}
