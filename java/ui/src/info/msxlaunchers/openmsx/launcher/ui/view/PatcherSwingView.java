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

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.PatcherPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.PatcherWindow;

/**
 * Swing-based implementation of <code>PatcherView</code>
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
class PatcherSwingView implements PatcherView
{
	private final PatcherPresenter presenter;

	PatcherWindow window = null;

	@Inject
	PatcherSwingView( PatcherPresenter presenter )
	{
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.PatcherView#displayScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void displayScreen( Language language, boolean rightToLeft )
	{
		window = new PatcherWindow( presenter, language, rightToLeft );

		window.displayScreen();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.PatcherView#confirmTargetFileReplacement()
	 */
	@Override
	public boolean confirmTargetFileReplacement()
	{
		return window.targetFileReplacementIsConfirmed();
	}
}
