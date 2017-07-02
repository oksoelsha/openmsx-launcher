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
package info.msxlaunchers.openmsx.launcher.ui.view;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.MachineUpdatePresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.MachineUpdateWindow;

import java.util.Set;

/**
 * Swing-based implementation of <code>MachineUpdateView</code>
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
class MachineUpdateSwingView implements MachineUpdateView
{
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.MachineUpdateView#displayScreen(info.msxlaunchers.openmsx.launcher.ui.presenter.MachineUpdatePresenter, java.lang.String, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean, java.util.Set)
	 */
	@Override
	public void displayScreen( MachineUpdatePresenter presenter, String database, Language language, boolean rightToLeft, Set<String> machines )
	{
		MachineUpdateWindow window = new MachineUpdateWindow( presenter, database, language, rightToLeft, machines );

		window.displayScreen();
	}
}
