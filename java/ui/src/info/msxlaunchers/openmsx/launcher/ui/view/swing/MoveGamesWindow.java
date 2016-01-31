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

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.UserInputComboBoxSelector;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.UserInputWindow;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @since v1.1
 * @author Sam Elsharif
 *
 */
public class MoveGamesWindow
{
	private final MainPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;
	private final Set<String> gameNames;
	private final String oldDatabase;
	private final Set<String> targetDatabases;

	Set<String> movedGameNames = null;

	public MoveGamesWindow(MainPresenter presenter, Language language, boolean rightToLeft, Component parent,
			Set<String> gameNames, String oldDatabase, Set<String> targetDatabases)
	{
		this.presenter = presenter;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.parent = parent;
		this.gameNames = gameNames;
		this.oldDatabase = oldDatabase;
		this.targetDatabases = targetDatabases;
	}

	public Set<String> displayAndGetMovedGames()
	{
		UserInputWindow<String> userInputWindow = new UserInputWindow<String>(parent,
				messages,
				rightToLeft,
				messages.get("MOVE_GAMES"),
				messages.get("MOVE_MESSAGE"),
				new UserInputComboBoxSelector(Utils.getSortedCaseInsensitiveArray(targetDatabases)));

		String userInput = userInputWindow.displayAndGetUserInput();

		movedGameNames = new HashSet<String>();

		if(userInput != null)
		{
			try
			{
				movedGameNames = presenter.onRequestMoveGamesAction(gameNames, oldDatabase, userInput);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(parent, le, messages, rightToLeft);
			}
		}

		return movedGameNames;
	}
}
