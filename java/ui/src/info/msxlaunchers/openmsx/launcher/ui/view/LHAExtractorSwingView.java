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
package info.msxlaunchers.openmsx.launcher.ui.view;

import java.util.Map;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LHAExtractorPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.LHAExtractorWindow;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

/**
 * Swing-based implementation of <code>LHAExtractorView</code>
 * 
 * @since v1.14
 * @author Sam Elsharif
 *
 */
class LHAExtractorSwingView implements LHAExtractorView
{
	private final LHAExtractorPresenter presenter;

	LHAExtractorWindow window = null;

	@Inject
	LHAExtractorSwingView( LHAExtractorPresenter presenter )
	{
		this.presenter = presenter;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.PatcherView#displayScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void displayScreen( Language language, boolean rightToLeft )
	{
		window = new LHAExtractorWindow( presenter, language, rightToLeft );

		window.displayScreen();
	}

	@Override
	public int displayAndGetActionDecider( String extractedFilename, Language language, boolean rightToLeft )
	{
		//TODO once I merge Swing classes with the views the following will be unnecessary
		Map<String,String> messages = LanguageDisplayFactory.getDisplayMessages(LHAExtractorWindow.class, language);

		return MessageBoxUtil.showYesNoAllMessageBox(window,
				"<html>\"" + extractedFilename + "\" " + messages.get("CONFIRM_REPLACE_EXTRACTED_FILE_MSG") + "</html>", messages, rightToLeft);
	}
}
