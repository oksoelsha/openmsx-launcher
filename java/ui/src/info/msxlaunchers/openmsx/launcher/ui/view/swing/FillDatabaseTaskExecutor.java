/*
 * Copyright 2015 Sam Elsharif
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

import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;

/**
 * Interface for executing and interrupting file scan process for games
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
public interface FillDatabaseTaskExecutor
{
	/*
	 * Executes task of filling database
	 * 
	 * @return Number of added profiles
	 */
	int execute() throws LauncherException;

	/*
	 * Interrupt the current scan process
	 */
	void interrupt();
}
