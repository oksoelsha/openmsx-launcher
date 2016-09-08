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
package info.msxlaunchers.openmsx.launcher.log.analyser.processor;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.log.analyser.LogProcessItem;

/**
 * Interface for processing Launch events in the launcher log
 * 
 * @since v1.8
 * @author Sam Elsharif
 */
public interface LaunchEventProcessor
{
	/**
	 * Process the launch event given the date and message
	 * 
	 * @param date Date as it appears in the log
	 * @param databaseItem Instance of DatabaseItem containing game and database names
	 */
	void processMessage( String date, DatabaseItem databaseItem );

	/**
	 * Return an instance that contains a key (or an identifier) and a list of String arrays
	 * 
	 * @return An instance of LogProcessItem. Cannot be null
	 */
	LogProcessItem getProcessedData();
}
