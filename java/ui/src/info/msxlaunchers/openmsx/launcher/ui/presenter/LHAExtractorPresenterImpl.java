/*
 * Copyright 2020 Sam Elsharif
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

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.common.ActionDecider;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.extractor.ExtractionException;
import info.msxlaunchers.openmsx.launcher.extractor.Extractor;
import info.msxlaunchers.openmsx.launcher.extractor.ExtractorData;
import info.msxlaunchers.openmsx.launcher.extractor.ExtractorExceptionIssue;
import info.msxlaunchers.openmsx.launcher.ui.view.LHAExtractorView;

/**
 * Implementation of <code>LHAExtractorPresenter</code>
 * 
 * @since v1.14
 * @author Sam Elsharif
 *
 */
class LHAExtractorPresenterImpl implements LHAExtractorPresenter
{
	private final LHAExtractorView view;
	private final Extractor extractor;

	//model
	private Language currentLanguage = null;
	private boolean currentRightToLeft = false;

	@Inject
	public LHAExtractorPresenterImpl( LHAExtractorView view, Extractor extractor )
	{
		this.view = view;
		this.extractor = extractor;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.LHAExtractorPresenter#onRequestLHAExtractorScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestLHAExtractorScreen( Language currentLanguage, boolean currentRightToLeft )
	{
		this.currentLanguage = currentLanguage;
		this.currentRightToLeft = currentRightToLeft;

		view.displayScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.LHAExtractorPresenter#onRequestLHAExtractAction(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ExtractorData onRequestLHAExtractAction( String fileToUncompress, String targetDirectory, boolean extractOnlyMSXImages )
			throws LauncherException
	{
		try
		{
			ActionDecider actionDecider = new LHAExtractorActionDecider( view, currentLanguage, currentRightToLeft );
			return extractor.extract( fileToUncompress, targetDirectory, extractOnlyMSXImages, actionDecider );
		}
		catch( ExtractionException de )
		{
			if( de.getIssue().equals( ExtractorExceptionIssue.COMPRESSED_FILE_NOT_FOUND ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_COMPRESSED_FILE_NOT_FOUND );
			}
			else
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}
	}
}
