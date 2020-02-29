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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.common.ExternalLinksUtils;
import info.msxlaunchers.openmsx.common.version.Application;
import info.msxlaunchers.openmsx.common.version.VersionUtils;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistence;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;
import info.msxlaunchers.openmsx.launcher.ui.view.UpdateCheckerView;
import info.msxlaunchers.openmsx.launcher.updater.FileUpdateFailedException;
import info.msxlaunchers.openmsx.launcher.updater.UpdateChecker;

/**
 * Implementation of <code>UpdateCheckerPresenter</code>
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class UpdateCheckerPresenterImpl implements UpdateCheckerPresenter
{
	private final UpdateCheckerView view;
	private final UpdateChecker updateChecker;
	private final ExtraDataGetter extraDataGetter;
	private final SettingsPersister settingsPersister;
	private final LauncherPersistence launcherPersistence;
	private final MainPresenter mainPresenter;

	private static final String DOWNLOAD_URL = "http://msxlaunchers.info/download.html";

	//Model
	private Map<String,String> versionsFromServer;

	@Inject
	UpdateCheckerPresenterImpl( UpdateCheckerView view, UpdateChecker updateChecker, ExtraDataGetter extraDataGetter, SettingsPersister settingsPersister,
			LauncherPersistence launcherPersistence, MainPresenter mainPresenter )
	{
		this.view = Objects.requireNonNull( view );
		this.updateChecker = Objects.requireNonNull( updateChecker );
		this.extraDataGetter = Objects.requireNonNull( extraDataGetter );
		this.settingsPersister = Objects.requireNonNull( settingsPersister );
		this.launcherPersistence = Objects.requireNonNull( launcherPersistence );
		this.mainPresenter = Objects.requireNonNull( mainPresenter );

		versionsFromServer = Collections.emptyMap();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter#onRequestCheckForUpdatesScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestCheckForUpdatesScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException
	{
		try
		{
			versionsFromServer = updateChecker.getVersions();
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_CONTACT_SERVER );
		}

		view.displayScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter#isNewOpenMSXLauncherVersionAvailable()
	 */
	@Override
	public boolean isNewOpenMSXLauncherVersionAvailable()
	{
		return VersionUtils.isVersionNewer( Application.VERSION, versionsFromServer.get( UpdateChecker.KEY_OPENMSX_LAUNCHER ) );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter#isNewOpenMSXLauncherVersionDownloaded()
	 */
	@Override
	public boolean isNewOpenMSXLauncherVersionDownloaded()
	{
		return updateChecker.isNewOpenMSXLauncherDownloaded();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter#isNewExtraDataVersionAvailable()
	 */
	@Override
	public boolean isNewExtraDataVersionAvailable()
	{
		String currentExtraDataVersion = null;

		try
		{
			currentExtraDataVersion = extraDataGetter.getExtraDataFileVersion();
		}
		catch( IOException ioe )
		{
			//TODO return new exception that current extra data file is not readable for some reason
		}

		return VersionUtils.isVersionNewer( currentExtraDataVersion, versionsFromServer.get( UpdateChecker.KEY_EXTRA_DATA ) );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter#isNewScreenshotsVersionAvailable()
	 */
	@Override
	public boolean isNewScreenshotsVersionAvailable()
	{
		boolean answer = false;

		try
		{
			String screenshotsPath = settingsPersister.getSettings().getScreenshotsFullPath();

			if( screenshotsPath != null )
			{
				answer = VersionUtils.isVersionNewer( VersionUtils.getScreenshotsVersion( screenshotsPath ), versionsFromServer.get( UpdateChecker.KEY_SCREENSHOTS ) );
			}
		}
		catch( IOException ioe )
		{
			//just ignore - the answer will be false anyway
		}

		return answer;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter#isScreenshotsSetInSettings()
	 */
	@Override
	public boolean isScreenshotsSetInSettings()
	{
		boolean answer = false;

		try
		{
			String screenshotsPath = settingsPersister.getSettings().getScreenshotsFullPath();

            answer = screenshotsPath != null && new File( screenshotsPath, "version.txt" ).exists();
		}
		catch( IOException ioe )
		{
			//just ignore - the answer will be false anyway
		}

		return answer;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter#onRequestUpdateExtraData()
	 */
	@Override
	public void onRequestUpdateExtraData() throws LauncherException
	{
		try
		{
			updateChecker.getNewExtraDataFile();
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_CONTACT_SERVER );
		}
		catch( FileUpdateFailedException fufe )
		{
			LauncherLogger.logException( this, fufe );
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_INSTALL_NEW_UPDATED_FILES );
		}

		//at this point we need to update the databases with new information obtained from the new extra data file
		Map<String, ExtraData> extraDataMap = null;

		try
		{
			extraDataMap = extraDataGetter.getExtraData();
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		try
		{
			launcherPersistence.getGamePersister().updateGameExtraDataInDatabases( extraDataMap );
		}
		catch( GamePersistenceException gpe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		mainPresenter.onUpdateExtraData();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter#onRequestUpdateOpenMSXLauncher()
	 */
	@Override
	public void onRequestUpdateOpenMSXLauncher() throws LauncherException
	{
		try
		{
			updateChecker.getNewOpenMSXLauncher();
		}
		catch( IOException e )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_CONTACT_SERVER );
		}
		catch( FileUpdateFailedException fufe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_INSTALL_NEW_UPDATED_FILES );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter#onRequestOpenDownloadPage()
	 */
	@Override
	public void onRequestOpenDownloadPage() throws LauncherException
	{
		browseToDownloadLink();
	}

	private void browseToDownloadLink() throws LauncherException
	{
		try
		{
			ExternalLinksUtils.startBrowser( DOWNLOAD_URL );
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}
}
