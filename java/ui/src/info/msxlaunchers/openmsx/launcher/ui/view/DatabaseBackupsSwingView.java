/*
 * Copyright 2015 Sam Elsharif
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

import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.DatabaseBackupsWindow;

import java.util.Set;

/**
 * Swing-based implementation of <code>DatabaseBackupsView</code>
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
final class DatabaseBackupsSwingView implements DatabaseBackupsView
{
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.DatabaseBackupsView#displayScreen(info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean, java.util.Set)
	 */
	@Override
	public void displayScreen( DatabaseBackupsPresenter presenter, Language language, boolean rightToLeft, Set<DatabaseBackup> backups )
	{
		DatabaseBackupsWindow databaseBackupsWindow = new DatabaseBackupsWindow( presenter, language, rightToLeft, backups );

		databaseBackupsWindow.display();
	}
}
