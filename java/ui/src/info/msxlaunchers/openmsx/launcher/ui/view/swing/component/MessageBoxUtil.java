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
package info.msxlaunchers.openmsx.launcher.ui.view.swing.component;

import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.util.Map;

/**
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public class MessageBoxUtil
{
	public static void showErrorMessageBox(Component parent, LauncherException le, Map<String,String> messages, boolean rightToLeft)
	{
		StringBuilder buffer = new StringBuilder(messages.get(le.getCodeAsString()));

		String additionalString = le.getAdditionalString();
		if(additionalString != null)
		{
			buffer.append(": ").append(additionalString);
		}

		MessageWindow messageWindow = new MessageWindow(parent, messages.get("ERROR"),
				buffer.toString(), MessageWindow.ERROR, new String[] { messages.get("OK") }, rightToLeft);
		messageWindow.displayAndGetResult();
	}

	public static void showErrorMessageBox(Component parent, LauncherException le, Map<String,String> messages, ComponentOrientation orientation)
	{
		showErrorMessageBox(parent, le, messages, orientation == ComponentOrientation.RIGHT_TO_LEFT);
	}

	public static int showYesNoMessageBox(Component parent, String message, Map<String,String> messages, boolean rightToLeft)
	{
		MessageWindow messageWindow = new MessageWindow(parent, messages.get("CONFIRMATION"), message,
				MessageWindow.QUESTION, new String[] { messages.get("YES"), messages.get("NO") }, rightToLeft);
		return messageWindow.displayAndGetResult();
	}

	public static int showYesNoMessageBox(Component parent, String message, Map<String,String> messages, ComponentOrientation orientation)
	{
		return showYesNoMessageBox(parent, message, messages,  orientation == ComponentOrientation.RIGHT_TO_LEFT);
	}

	public static int showYesNoAllMessageBox(Component parent, String message, Map<String,String> messages, boolean rightToLeft)
	{
		MessageWindow messageWindow = new MessageWindow(parent, messages.get("CONFIRMATION"), message, MessageWindow.QUESTION,
				new String[] { messages.get("YES"), messages.get("YES_ALL"), messages.get("NO"), messages.get("NO_ALL"), messages.get("CANCEL")  }, rightToLeft);
		return messageWindow.displayAndGetResult();
	}

	public static int showInformationMessageBox(Component parent, String message, Map<String,String> messages, boolean rightToLeft)
	{
		MessageWindow messageWindow = new MessageWindow(parent, messages.get("RESULTS"), message,
				MessageWindow.INFORMATION, new String[] { messages.get("OK") }, rightToLeft);
		return messageWindow.displayAndGetResult();
	}

	public static int showInformationMessageBox(Component parent, String message, Map<String,String> messages, ComponentOrientation orientation)
	{
		return showInformationMessageBox(parent, message, messages,  orientation == ComponentOrientation.RIGHT_TO_LEFT);
	}
}
