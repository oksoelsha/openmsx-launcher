package info.msxlaunchers.openmsx.launcher.data.settings;

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SettingsTest
{
	private String openMSXFullPath = "openMSX Path";
	private String openMSXMachinesFullPath = "openMSX Machines Path";
	private String screenshotsFullPath = "Screenshots Path";
	private String defaultDatabase = "default Database";
	private Language language = Language.CATALAN;
	private boolean showUpdateAllDatabases = true;

	@Test
	public void testSettings()
	{
		Settings settings;
		
		//test that all fields can be set
		settings = new Settings( openMSXFullPath, openMSXMachinesFullPath, screenshotsFullPath, defaultDatabase, language, showUpdateAllDatabases );

		assertEquals( settings.getOpenMSXFullPath(), openMSXFullPath );
		assertEquals( settings.getOpenMSXMachinesFullPath(), openMSXMachinesFullPath );
		assertEquals( settings.getScreenshotsFullPath(), screenshotsFullPath );
		assertEquals( settings.getDefaultDatabase(), defaultDatabase );
		assertEquals( settings.getLanguage(), language );
		assertEquals( settings.isShowUpdateAllDatabases(), true );
		
		//test that all fields can null
		settings = new Settings( null, null, null, null, null, false );

		assertEquals( settings.getOpenMSXFullPath(), null );
		assertEquals( settings.getOpenMSXMachinesFullPath(), null );
		assertEquals( settings.getScreenshotsFullPath(), null );
		assertEquals( settings.getDefaultDatabase(), null );
		assertEquals( settings.getLanguage(), null );
		assertEquals( settings.isShowUpdateAllDatabases(), false );
	}

	@Test
	public void testEquals()
	{
		Settings settings1 = new Settings( openMSXFullPath, openMSXMachinesFullPath, screenshotsFullPath, defaultDatabase, language, showUpdateAllDatabases );
		Settings settings2 = new Settings( openMSXFullPath, openMSXMachinesFullPath, screenshotsFullPath, defaultDatabase, language, showUpdateAllDatabases );
		Settings settings3 = new Settings( null, openMSXMachinesFullPath, screenshotsFullPath, defaultDatabase, language, showUpdateAllDatabases );
		Settings settings4 = new Settings( null, null, screenshotsFullPath, defaultDatabase, language, showUpdateAllDatabases );
		Settings settings5 = new Settings( null, null, null, defaultDatabase, language, showUpdateAllDatabases );
		Settings settings6 = new Settings( null, null, null, null, language, showUpdateAllDatabases );
		Settings settings7 = new Settings( null, null, null, null, null, false );
		Settings settings8 = new Settings( openMSXFullPath, openMSXMachinesFullPath, screenshotsFullPath, defaultDatabase, Language.KOREAN, showUpdateAllDatabases );
		Settings settings9 = new Settings( openMSXFullPath, openMSXMachinesFullPath, "screenshotsFullPath2", defaultDatabase, language, showUpdateAllDatabases );
		Settings settings10 = new Settings( openMSXFullPath, openMSXMachinesFullPath, "screenshotsFullPath2", defaultDatabase, language, false );

		assertTrue( settings1.equals( settings2 ) );
		assertEquals( settings1.hashCode(), settings2.hashCode() );
		assertTrue( settings1.equals( settings1 ) );
		assertEquals( settings1.hashCode(), settings1.hashCode() );
		assertFalse( settings2.equals( settings3 ) );
		assertFalse( settings3.equals( settings4 ) );
		assertFalse( settings3.equals( settings2 ) );
		assertFalse( settings4.equals( settings3 ) );
		assertFalse( settings3.equals( settings5 ) );
		assertFalse( settings3.equals( settings6 ) );
		assertFalse( settings3.equals( settings7 ) );
		assertFalse( settings5.equals( settings4 ) );
		assertFalse( settings5.equals( settings6 ) );
		assertFalse( settings6.equals( settings5 ) );
		assertFalse( settings7.equals( null ) );
		assertFalse( settings1.equals( "something" ) );
		assertFalse( settings1.equals( settings8 ) );
		assertFalse( settings1.equals( settings9 ) );
		assertFalse( settings10.equals( settings9 ) );
	}

	@Test
	public void testEmptyStringsShouldReturnNullOrFalse()
	{
		Settings settings = new Settings( " ", " ", " ", " ", null, false );

		assertNull( settings.getOpenMSXFullPath() );
		assertNull( settings.getOpenMSXMachinesFullPath() );
		assertNull( settings.getScreenshotsFullPath() );
		assertNull( settings.getDefaultDatabase() );
	}
}
