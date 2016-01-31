/*
 * Copyright 2014 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.ui.view;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseAndBackups;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseInfo;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.DatabaseManagerWindow;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.RenameDatabaseWindow;

import java.util.Set;

/**
 * Swing-based implementation of <code>DatabaseManagerView</code>
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
class DatabaseManagerSwingView implements DatabaseManagerView
{
	DatabaseManagerWindow databaseManagerWindow;

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.DatabaseManagerView#displayScreen(info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean, java.util.Set)
	 */
	@Override
	public void displayScreen( DatabaseManagerPresenter presenter, Language language, boolean rightToLeft, Set<DatabaseAndBackups> databaseAndBackups )
	{
		databaseManagerWindow = new DatabaseManagerWindow( presenter, language, rightToLeft );

		databaseManagerWindow.display( databaseAndBackups );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.DatabaseManagerView#displayRenameDatabaseScreen(info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter, java.lang.String, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public String displayRenameDatabaseScreen( DatabaseManagerPresenter presenter, String database, Language language, boolean rightToLeft )
	{
		RenameDatabaseWindow window = new RenameDatabaseWindow( presenter, database, language, rightToLeft, databaseManagerWindow );

		return window.displayAndGetNewDatabaseName();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.DatabaseManagerView#updateDatabaseAndBackups(java.lang.String, int, int)
	 */
	@Override
	public void updateDatabaseAndBackups( String database, int gamesTotal, int backupsTotal )
	{
		databaseManagerWindow.updateDatabaseAndBackupsTable( database, gamesTotal, backupsTotal );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.DatabaseManagerView#updateDatabaseInfo(info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseInfo)
	 */
	@Override
	public void updateDatabaseInfo( DatabaseInfo databaseInfo )
	{
		databaseManagerWindow.updateDatabasesInfo( databaseInfo );
	}
}
