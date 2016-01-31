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
package info.msxlaunchers.openmsx.launcher.persistence.game;

/**
 * Interface to provide actions needed during an interactive operation
 * 
 * @since v1.1
 * @author Sam Elsharif
 *
 */
public interface ActionDecider
{
	/**
	 * Prompts the user for action
	 * 
	 * @param duplicateObjectName Name of object that is causing the conflict (e.g. duplicate game or database name)
	 */
	void promptForAction( String duplicateObjectName );

	/**
	 * Returns true if user's answer is yes
	 * 
	 * @return true if Yes
	 */
	boolean isYes();

	/**
	 * Returns true if user's answer is yes to all
	 * 
	 * @return true if Yes to all
	 */
	boolean isYesAll();

	/**
	 * Returns true if user's answer is no
	 * 
	 * @return true if No
	 */
	boolean isNo();

	/**
	 * Returns true if user's answer is no to all
	 * 
	 * @return true if No to all
	 */
	boolean isNoAll();

	/**
	 * Returns true if user's decision is to cancel operation
	 * 
	 * @return true if user requests to cancel operation
	 */
	boolean isCancel();
}
