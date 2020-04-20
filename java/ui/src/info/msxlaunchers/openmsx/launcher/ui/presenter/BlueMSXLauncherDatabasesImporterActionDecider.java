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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.common.ActionDecider;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.BlueMSXLauncherDatabaseImporterView;

/**
 * Implementation of the <code>ActionDecider</code> that prompts user for action when an imported blueMSX Launcher database name existed already in
 * openMSX Launcher. This implementation will request input from the user through the View.
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
final class BlueMSXLauncherDatabasesImporterActionDecider extends ActionDecider
{
	private final BlueMSXLauncherDatabaseImporterView view;
	private final Language language;
	private final boolean rightToLeft;

	BlueMSXLauncherDatabasesImporterActionDecider( BlueMSXLauncherDatabaseImporterView view, Language language, boolean rightToLeft )
	{
		this.view = view;
		this.language = language;
		this.rightToLeft = rightToLeft;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.common.ActionDecider#promptForAction(java.lang.String)
	 */
	@Override
	public void promptForAction( String gameName )
	{
		int choice = view.displayAndGetActionDecider( gameName, language, rightToLeft );

		processChoice( choice );
	}
}
