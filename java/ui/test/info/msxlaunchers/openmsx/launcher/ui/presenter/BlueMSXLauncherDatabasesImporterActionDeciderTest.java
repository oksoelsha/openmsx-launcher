package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.BlueMSXLauncherDatabaseImporterView;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class BlueMSXLauncherDatabasesImporterActionDeciderTest
{
	@Mock BlueMSXLauncherDatabaseImporterView view;

	private final String databaseName = "databaseName";

	@Test
	public void testConstructor()
	{
		new BlueMSXLauncherDatabasesImporterActionDecider( view, Language.ENGLISH, false );
	}

	@Test
	public void testPromptForAction()
	{
		BlueMSXLauncherDatabasesImporterActionDecider actionDecider = new BlueMSXLauncherDatabasesImporterActionDecider( view, Language.ENGLISH, false );

		actionDecider.promptForAction( databaseName );

		verify( view, times( 1 ) ).displayAndGetActionDecider( databaseName, Language.ENGLISH, false );
	}

	@Test
	public void testPromptForAction_Yes()
	{
		BlueMSXLauncherDatabasesImporterActionDecider actionDecider = new BlueMSXLauncherDatabasesImporterActionDecider( view, Language.ENGLISH, false );

		when( view.displayAndGetActionDecider( databaseName, Language.ENGLISH, false ) ).thenReturn( 0 );

		actionDecider.promptForAction( databaseName );

		assertTrue( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertFalse( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_YesAll()
	{
		BlueMSXLauncherDatabasesImporterActionDecider actionDecider = new BlueMSXLauncherDatabasesImporterActionDecider( view, Language.ENGLISH, false );

		when( view.displayAndGetActionDecider( databaseName, Language.ENGLISH, false ) ).thenReturn( 1 );

		actionDecider.promptForAction( databaseName );

		assertFalse( actionDecider.isYes() );
		assertTrue( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertFalse( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_No()
	{
		BlueMSXLauncherDatabasesImporterActionDecider actionDecider = new BlueMSXLauncherDatabasesImporterActionDecider( view, Language.ENGLISH, false );

		when( view.displayAndGetActionDecider( databaseName, Language.ENGLISH, false ) ).thenReturn( 2 );

		actionDecider.promptForAction( databaseName );

		assertFalse( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertTrue( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertFalse( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_NoAll()
	{
		BlueMSXLauncherDatabasesImporterActionDecider actionDecider = new BlueMSXLauncherDatabasesImporterActionDecider( view, Language.ENGLISH, false );

		when( view.displayAndGetActionDecider( databaseName, Language.ENGLISH, false ) ).thenReturn( 3 );

		actionDecider.promptForAction( databaseName );

		assertFalse( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertTrue( actionDecider.isNoAll() );
		assertFalse( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_CancelValue1()
	{
		BlueMSXLauncherDatabasesImporterActionDecider actionDecider = new BlueMSXLauncherDatabasesImporterActionDecider( view, Language.ENGLISH, false );

		when( view.displayAndGetActionDecider( databaseName, Language.ENGLISH, false ) ).thenReturn( 4 );

		actionDecider.promptForAction( databaseName );

		assertFalse( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertTrue( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_CancelValue2()
	{
		BlueMSXLauncherDatabasesImporterActionDecider actionDecider = new BlueMSXLauncherDatabasesImporterActionDecider( view, Language.ENGLISH, false );

		when( view.displayAndGetActionDecider( databaseName, Language.ENGLISH, false ) ).thenReturn( -1 );

		actionDecider.promptForAction( databaseName );

		assertFalse( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertTrue( actionDecider.isCancel() );
	}

	@Test( expected = RuntimeException.class )
	public void testPromptForAction_InvalidPromptValue()
	{
		BlueMSXLauncherDatabasesImporterActionDecider actionDecider = new BlueMSXLauncherDatabasesImporterActionDecider( view, Language.ENGLISH, false );

		when( view.displayAndGetActionDecider( databaseName, Language.ENGLISH, false ) ).thenReturn( 5 );

		actionDecider.promptForAction( databaseName );
	}
}