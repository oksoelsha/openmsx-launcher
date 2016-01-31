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

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.SettingsPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.SettingsWindow;

import java.util.Set;

import com.google.inject.Inject;

/**
 * Swing-based implementation of <code>SettingsView</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
class SettingsSwingView implements SettingsView
{
	private final SettingsPresenter settingsPresenter;
	private final PlatformViewProperties platformViewProperties;

	@Inject
	SettingsSwingView( SettingsPresenter settingsPresenter, PlatformViewProperties platformViewProperties )
	{
		this.settingsPresenter = settingsPresenter;
		this.platformViewProperties = platformViewProperties;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.SettingsView#displaySettings(info.msxlaunchers.openmsx.launcher.data.settings.Settings, java.util.Set, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, java.lang.String, boolean)
	 */
	@Override
	public void displaySettings( Settings settings, Set<String> databases, Language language, String languageCode, boolean rightToLeft )
	{
		SettingsWindow settingsWindow = new SettingsWindow( settingsPresenter, settings, databases, language, languageCode, rightToLeft,
				platformViewProperties.getSuggestedOpenMSXPath() );

		settingsWindow.display();
	}
}
