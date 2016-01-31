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
import info.msxlaunchers.openmsx.launcher.ui.presenter.ScannerPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.FillDatabaseWindow;

import java.util.Set;

import com.google.inject.Inject;

/**
 * Swing-based implementation of <code>ScannerView</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
class ScannerSwingView implements ScannerView
{
	private final ScannerPresenter presenter;

	@Inject
	ScannerSwingView( ScannerPresenter presenter )
	{
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.ScannerView#displayFillDatabase(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, java.util.Set, java.lang.String, java.util.Set, boolean)
	 */
	@Override
	public void displayFillDatabase( Language language, Set<String> databases, String currentDatabase, Set<String> machines, boolean rightToLeft )
	{
		FillDatabaseWindow fillDatabaseWindow = new FillDatabaseWindow( presenter, language, databases, currentDatabase, machines, rightToLeft );

		fillDatabaseWindow.display();
	}
}
