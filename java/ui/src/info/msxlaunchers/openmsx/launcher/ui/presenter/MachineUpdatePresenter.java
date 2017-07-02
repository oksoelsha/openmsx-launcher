/*
 * Copyright 2017 Sam Elsharif
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

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for updating machines UI Model and Presenter
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
public interface MachineUpdatePresenter
{
	/**
	 * Called when user requests machine update screen
	 *
	 * @param currentLanguage
	 * @param currentRightToLeft
	 */
	void onRequestMachineUpdateScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException;

	/**
	 * Called when user decides to update machines
	 *  
	 * @param machineTo Machine to update to. Cannot be null
	 * @param machineFrom Machine to update from. Can be null
	 * @param database Database name. Can be null
	 * @return Number of updated profiles
	 * @throws LauncherException
	 */
	int onRequestMachineUpdateAction( String machineTo, String machineFrom, String database ) throws LauncherException;
}
