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

import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.launcher.ui.view.SettingsView;
import info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;

/**
 * Implementation of <code>SettingsPresenter</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
final class SettingsPresenterImpl implements SettingsPresenter
{
	private final SettingsView view;
	private final SettingsPersister settingsPersister;
	private final MainPresenter mainPresenter;
	private final PlatformViewProperties platformViewProperties;

	@Inject
	SettingsPresenterImpl( SettingsView view,
			SettingsPersister settingsPersister,
			MainPresenter launcherPresenter,
			PlatformViewProperties platformViewProperties ) throws IOException
	{
		this.view = Objects.requireNonNull( view );
		this.settingsPersister = Objects.requireNonNull( settingsPersister );
		this.mainPresenter = Objects.requireNonNull( launcherPresenter );
		this.platformViewProperties = Objects.requireNonNull( platformViewProperties );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.SettingsPresenter#onRequestSettingsScreen(info.msxlaunchers.openmsx.launcher.data.settings.Settings, java.util.Set, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, java.lang.String, boolean)
	 */
	@Override
	public void onRequestSettingsScreen( Settings oldSettings, Set<String> databases, Language currentLanguage,
			String currentLanguageCode, boolean currentRightToLeft )
	{
		view.displaySettings( oldSettings, databases, currentLanguage, currentLanguageCode, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.SettingsPresenter#onRequestSettingsAction(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onRequestSettingsAction( String openMSXFullPath,
			String screenshotsFullPath,
			String defaultDatabase,
			String languageChoice ) throws LauncherException
	{
		String openMSXMachinesFullPath = null;
		if( platformViewProperties.isMachinesFolderInsideOpenMSX() )
		{
			openMSXMachinesFullPath = new File( openMSXFullPath, platformViewProperties.getOpenMSXMachinesPath() ).getAbsolutePath();
		}
		else
		{
			openMSXMachinesFullPath = platformViewProperties.getOpenMSXMachinesPath();
		}

		try
		{
			Language language = null;
			if( languageChoice != null )
			{
				language = Language.valueOf( languageChoice );
			}

			Settings newSettings = new Settings( openMSXFullPath,
					openMSXMachinesFullPath,
					screenshotsFullPath,
					defaultDatabase,
					language );

			settingsPersister.saveSettings( newSettings );

			mainPresenter.onAcceptSettingsAction( newSettings );
		}
		catch ( IOException e )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_SAVE_SETTINGS );
		}
	}
}
