package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.MainView;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class MoveGameActionDeciderTest
{
	@Mock MainView mainView;

	private final String gameName = "gameName";

	@Test
	public void testConstructor()
	{
		new MoveGameActionDecider( mainView, Language.ENGLISH, false );
	}

	@Test
	public void testPromptForAction()
	{
		MoveGameActionDecider actionDecider = new MoveGameActionDecider( mainView, Language.ENGLISH, false );

		actionDecider.promptForAction( gameName );

		verify( mainView, times( 1 ) ).displayAndGetActionDecider( gameName, Language.ENGLISH, false );
	}

	@Test
	public void testPromptForAction_Yes()
	{
		MoveGameActionDecider actionDecider = new MoveGameActionDecider( mainView, Language.ENGLISH, false );

		when( mainView.displayAndGetActionDecider( gameName, Language.ENGLISH, false ) ).thenReturn( 0 );

		actionDecider.promptForAction( gameName );

		assertTrue( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertFalse( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_YesAll()
	{
		MoveGameActionDecider actionDecider = new MoveGameActionDecider( mainView, Language.ENGLISH, false );

		when( mainView.displayAndGetActionDecider( gameName, Language.ENGLISH, false ) ).thenReturn( 1 );

		actionDecider.promptForAction( gameName );

		assertFalse( actionDecider.isYes() );
		assertTrue( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertFalse( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_No()
	{
		MoveGameActionDecider actionDecider = new MoveGameActionDecider( mainView, Language.ENGLISH, false );

		when( mainView.displayAndGetActionDecider( gameName, Language.ENGLISH, false ) ).thenReturn( 2 );

		actionDecider.promptForAction( gameName );

		assertFalse( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertTrue( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertFalse( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_NoAll()
	{
		MoveGameActionDecider actionDecider = new MoveGameActionDecider( mainView, Language.ENGLISH, false );

		when( mainView.displayAndGetActionDecider( gameName, Language.ENGLISH, false ) ).thenReturn( 3 );

		actionDecider.promptForAction( gameName );

		assertFalse( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertTrue( actionDecider.isNoAll() );
		assertFalse( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_CancelValue1()
	{
		MoveGameActionDecider actionDecider = new MoveGameActionDecider( mainView, Language.ENGLISH, false );

		when( mainView.displayAndGetActionDecider( gameName, Language.ENGLISH, false ) ).thenReturn( 4 );

		actionDecider.promptForAction( gameName );

		assertFalse( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertTrue( actionDecider.isCancel() );
	}

	@Test
	public void testPromptForAction_CancelValue2()
	{
		MoveGameActionDecider actionDecider = new MoveGameActionDecider( mainView, Language.ENGLISH, false );

		when( mainView.displayAndGetActionDecider( gameName, Language.ENGLISH, false ) ).thenReturn( -1 );

		actionDecider.promptForAction( gameName );

		assertFalse( actionDecider.isYes() );
		assertFalse( actionDecider.isYesAll() );
		assertFalse( actionDecider.isNo() );
		assertFalse( actionDecider.isNoAll() );
		assertTrue( actionDecider.isCancel() );
	}

	@Test( expected = RuntimeException.class )
	public void testPromptForAction_InvalidPromptValue()
	{
		MoveGameActionDecider actionDecider = new MoveGameActionDecider( mainView, Language.ENGLISH, false );

		when( mainView.displayAndGetActionDecider( gameName, Language.ENGLISH, false ) ).thenReturn( 5 );

		actionDecider.promptForAction( gameName );
	}
}