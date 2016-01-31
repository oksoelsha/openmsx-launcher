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
import info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.AddEditGameWindow;

import java.util.Set;

/**
 * Swing-based implementation of <code>ProfileEditingView</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
class ProfileEditingSwingView implements ProfileEditingView
{
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.ProfileEditingView#displayAddGameScreen(info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, java.util.Set, java.util.Set, boolean)
	 */
	@Override
	public void displayAddGameScreen( ProfileEditingPresenter presenter, Language language, Set<String> machines, Set<String> extensions, boolean rightToLeft )
	{
		AddEditGameWindow addEditWindow = new AddEditGameWindow( presenter, language, machines, extensions, rightToLeft, false );

		addEditWindow.display();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.ProfileEditingView#displayEditGameScreen(info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, java.util.Set, java.util.Set, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void displayEditGameScreen( ProfileEditingPresenter presenter, Language language, Set<String> machines, Set<String> extensions, boolean rightToLeft,
			String name, String info, String machine, String romA, String romB, String extensionRom,
			String diskA, String diskB, String tape, String harddisk, String laserdisc, String script )
	{
		AddEditGameWindow addEditWindow = new AddEditGameWindow( presenter, language, machines, extensions, rightToLeft, true );

		addEditWindow.display( name, info, machine, romA, romB, extensionRom, diskA, diskB, tape, harddisk,
				laserdisc, script );
	}
}
