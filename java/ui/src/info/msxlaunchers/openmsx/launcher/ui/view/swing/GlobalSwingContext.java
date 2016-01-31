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

/**
 * Singleton to store a reference to the main Swing window to be used by all child windows. Only one instance is allowed and is package private.
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
class GlobalSwingContext
{
	private static GlobalSwingContext instance = null;
	private final MainWindow mainWindow;

	GlobalSwingContext(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;

		if(instance == null)
		{
			instance = this;
		}
		else
		{
			throw new RuntimeException("Cannot instaniate more than once");
		}
	}

	/**
	 * @return reference to the singleton
	 */
	static GlobalSwingContext getIntance()
	{
		return instance;
	}

	/**
	 * @return reference to the main Swing class
	 */
	MainWindow getMainWindow()
	{
		return mainWindow;
	}
}
