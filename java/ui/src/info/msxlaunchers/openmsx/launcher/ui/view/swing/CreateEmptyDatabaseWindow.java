/*
 * Copyright 2013 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.ui.view.swing;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.UserInputTextField;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.UserInputWindow;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.util.Map;

/**
 * @since v1.1
 * @author Sam Elsharif
 *
 */
public class CreateEmptyDatabaseWindow
{
	private final MainPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;

	public CreateEmptyDatabaseWindow(MainPresenter presenter, Language language, boolean rightToLeft, Component parent)
	{
		this.presenter = presenter;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.parent = parent;
	}

	public void displayAndGetNewDatabaseName()
	{
		UserInputWindow<String> userInputWindow = new UserInputWindow<String>(parent,
				messages,
				rightToLeft,
				messages.get("CREATE_EMPTY_DATABASE"),
				messages.get("CREATE_NEW_DATABASE_MESSAGE"),
				new UserInputTextField(null));

		String userInput = userInputWindow.displayAndGetUserInput();

		if(userInput != null)
		{
			try
			{
				presenter.onRequestCreateEmptyDatabaseAction(userInput);
				presenter.onViewUpdatedDatabase(userInput);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(parent, le, messages, rightToLeft);
			}
		}
	}
}
