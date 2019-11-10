package info.msxlaunchers.openmsx.launcher.ui.presenter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.ui.view.MachineUpdateView;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.MachineLister;

@RunWith( MockitoJUnitRunner.class )
public class MachineUpdatePresenterImplTest
{
	@Mock MainPresenter mainPresenter;
	@Mock MachineLister machineLister;
	@Mock MachineUpdateView view;
	@Mock GamePersister gamePersister;
	private String database = "database";

	@Test
	public void givenLanguageAndOrientation_whenOnRequestMachineUpdateScreen_thenCallView() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		MachineUpdatePresenterImpl presenter = new MachineUpdatePresenterImpl( mainPresenter, machineLister, view, gamePersister, database );

		Set<String> machines = Collections.singleton( "machine1" );
		when( machineLister.get() ).thenReturn( machines );

		presenter.onRequestMachineUpdateScreen( Language.ARABIC, true );

		verify( view, times( 1 ) ).displayScreen( presenter, database, Language.ARABIC, true, machines );
	}

	@Test( expected = LauncherException.class )
	public void givenMachineListerThrowsInvalidMachinesDirectoryException_whenOnRequestMachineUpdateScreen_thenThrowException() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		MachineUpdatePresenterImpl presenter = new MachineUpdatePresenterImpl( mainPresenter, machineLister, view, gamePersister, database );

		when( machineLister.get() ).thenThrow( new InvalidMachinesDirectoryException() );

		try
		{
			presenter.onRequestMachineUpdateScreen( Language.FRENCH, true );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( LauncherExceptionCode.ERR_INVALID_MACHINES_DIRECTORY, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenMachineListerThrowsIOException_whenOnRequestMachineUpdateScreen_thenThrowException() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		MachineUpdatePresenterImpl presenter = new MachineUpdatePresenterImpl( mainPresenter, machineLister, view, gamePersister, database );

		when( machineLister.get() ).thenThrow( new IOException() );

		try
		{
			presenter.onRequestMachineUpdateScreen( Language.PERSIAN, true );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void givenValidArguments_whenOnRequestMachineUpdateAction_thenCallMainPresenterToUpdateViewedDatabase() throws LauncherException, InvalidMachinesDirectoryException, IOException
	{
		MachineUpdatePresenterImpl presenter = new MachineUpdatePresenterImpl( mainPresenter, machineLister, view, gamePersister, database );

		presenter.onRequestMachineUpdateAction( "machineTo", null, database, false );

		verify( mainPresenter, times( 1 ) ).onViewUpdatedDatabase( database );
	}

	@Test( expected = LauncherException.class )
	public void givenUpdateMachinesMaxBackupsExceeded_whenOnRequestMachineUpdateAction_thenThrowException() throws LauncherException, InvalidMachinesDirectoryException, IOException, GamePersistenceException
	{
		MachineUpdatePresenterImpl presenter = new MachineUpdatePresenterImpl( mainPresenter, machineLister, view, gamePersister, database );

		when( gamePersister.updateMachine( "machineTo", null, database, true ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_MAX_BACKUPS_REACHED ) );

		try
		{
			presenter.onRequestMachineUpdateAction( "machineTo", null, database, true );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( LauncherExceptionCode.ERR_DATABASE_MAX_BACKUPS_REACHED, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenUpdateMachinesThrowsException_whenOnRequestMachineUpdateAction_thenThrowException() throws LauncherException, InvalidMachinesDirectoryException, IOException, GamePersistenceException
	{
		MachineUpdatePresenterImpl presenter = new MachineUpdatePresenterImpl( mainPresenter, machineLister, view, gamePersister, database );

		when( gamePersister.updateMachine( "machineTo", null, database, false ) ).thenThrow( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );

		try
		{
			presenter.onRequestMachineUpdateAction( "machineTo", null, database, false );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}

	@Test
	public void givenValidArguments_whenOnRequestMachineUpdateAction_thenReturnTotalUpdatedProfiles() throws LauncherException, InvalidMachinesDirectoryException, IOException, GamePersistenceException
	{
		MachineUpdatePresenterImpl presenter = new MachineUpdatePresenterImpl( mainPresenter, machineLister, view, gamePersister, database );

		when( gamePersister.updateMachine( "machineTo", null, database, false ) ).thenReturn( 5 );

		Assert.assertEquals( 5, presenter.onRequestMachineUpdateAction( "machineTo", null, database, false ) );
	}
}
