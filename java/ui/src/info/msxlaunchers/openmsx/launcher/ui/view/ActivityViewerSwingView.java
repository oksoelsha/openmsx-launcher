/*
 * Copyright 2016 Sam Elsharif
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

import java.util.List;
import java.util.Map;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.ActivityViewerWindow;

/**
 * Swing-based implementation of <code>ActivityViewerView</code>
 * 
 * @since v1.8
 * @author Sam Elsharif
 *
 */
final class ActivityViewerSwingView implements ActivityViewerView
{
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.ActivityViewerView#displayActivityViewerScreen(java.util.Map, info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void displayActivityViewerScreen( Map<String,List<String[]>> logData, Language language, boolean rightToLeft )
	{
		ActivityViewerWindow activityViewerWindow = new ActivityViewerWindow( logData, language, rightToLeft );

		activityViewerWindow.display();
	}
}
