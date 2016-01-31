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
import info.msxlaunchers.openmsx.launcher.ui.presenter.FilterEditingPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.AddEditFilterWindow;

import com.google.inject.Inject;

/**
 * Swing-based implementation of <code>FilterEditingView</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
class FilterEditingSwingView implements FilterEditingView
{
	private final FilterEditingPresenter presenter;

	@Inject
	FilterEditingSwingView( FilterEditingPresenter presenter )
	{
		this.presenter = presenter;
	}
	
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.FilterEditingView#displayAddFilterScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void displayAddFilterScreen( Language language, boolean rightToLeft )
	{
		AddEditFilterWindow addEditFiltersWindow = new AddEditFilterWindow( presenter, language, rightToLeft, null, null );

		addEditFiltersWindow.display();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.FilterEditingView#displayEditFilterScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean, java.lang.String, java.lang.String[])
	 */
	@Override
	public void displayEditFilterScreen( Language language, boolean rightToLeft, String filterName, String[] filter )
	{
		AddEditFilterWindow addEditFiltersWindow = new AddEditFilterWindow( presenter, language, rightToLeft, filterName, filter );

		addEditFiltersWindow.display();
	}
}
