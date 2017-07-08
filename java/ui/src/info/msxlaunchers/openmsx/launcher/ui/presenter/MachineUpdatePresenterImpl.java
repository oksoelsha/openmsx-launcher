/*
 * Copyright 2017 Sam Elsharif
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.common.Nullable;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceExceptionIssue;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.ui.view.MachineUpdateView;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.MachineLister;

import java.io.IOException;
import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Implementation of <code>MachineUpdatePresenter</code>
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
final class MachineUpdatePresenterImpl implements MachineUpdatePresenter
{
	private final MainPresenter mainPresenter;
	private final MachineLister machineLister;
	private final MachineUpdateView view;
	private final GamePersister gamePersister;
	private final String currentDatabase;

	@Inject
	MachineUpdatePresenterImpl( MainPresenter mainPresenter, MachineLister machineLister, MachineUpdateView view,
			GamePersister gamePersister, @Nullable @Assisted String currentDatabase )
	{
		this.mainPresenter = Objects.requireNonNull( mainPresenter );
		this.machineLister = Objects.requireNonNull( machineLister );
		this.view = Objects.requireNonNull( view );
		this.gamePersister = Objects.requireNonNull( gamePersister );
		this.currentDatabase = currentDatabase;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MachineUpdatePresenter#onRequestMachineUpdateScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestMachineUpdateScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException
	{
		if( currentDatabase == null )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_NOT_FOUND );
		}

		try
		{
			view.displayScreen( this, currentDatabase, currentLanguage, currentRightToLeft, machineLister.get() );
		}
		catch( InvalidMachinesDirectoryException imde )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_INVALID_MACHINES_DIRECTORY );
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.MachineUpdatePresenter#onRequestMachineUpdateAction(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public int onRequestMachineUpdateAction( String machineTo, String machineFrom, String database, boolean backupDatabases ) throws LauncherException
	{
		try
		{
			int totalUpdated = gamePersister.updateMachine( machineTo, machineFrom, database, backupDatabases );

			mainPresenter.onViewUpdatedDatabase( database );

			return totalUpdated;
		}
		catch ( GamePersistenceException gpe )
		{
			if( gpe.getIssue().equals( GamePersistenceExceptionIssue.DATABASE_MAX_BACKUPS_REACHED ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_DATABASE_MAX_BACKUPS_REACHED, gpe.getaffectedObject() );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}
	}
}
