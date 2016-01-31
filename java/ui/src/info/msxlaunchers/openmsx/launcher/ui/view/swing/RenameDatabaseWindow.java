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
package info.msxlaunchers.openmsx.launcher.ui.view.swing;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.UserInputTextField;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.UserInputWindow;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.util.Map;

/**
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public class RenameDatabaseWindow
{
	private final DatabaseManagerPresenter presenter;
	private final String oldDatabase;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;

	public RenameDatabaseWindow(DatabaseManagerPresenter presenter, String oldDatabase, Language language, boolean rightToLeft, Component parent)
	{
		this.presenter = presenter;
		this.oldDatabase = oldDatabase;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.parent = parent;
	}

	public String displayAndGetNewDatabaseName()
	{
		UserInputWindow<String> userInputWindow = new UserInputWindow<String>(parent,
				messages,
				rightToLeft,
				messages.get("RENAME_DATABASE"),
				messages.get("RENAME_DATABASE_MESSAGE"),
				new UserInputTextField(oldDatabase));

		String newDatabase = userInputWindow.displayAndGetUserInput();

		if(newDatabase != null)
		{
			try
			{
				presenter.onRequestRenameDatabaseAction(oldDatabase, newDatabase);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(parent, le, messages, rightToLeft);
				newDatabase = null; //I hate to do this but this is the only way to return that the operation failed
			}
		}

		return newDatabase;
	}
}
