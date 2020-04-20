/*
 * Copyright 2020 Sam Elsharif
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
package info.msxlaunchers.openmsx.common;

/**
 * Class to ask for confirmation and process choices when a duplicate object is encountered
 * 
 * @since v1.14
 * @author Sam Elsharif
 *
 */
public abstract class ActionDecider
{
	private boolean yes;
	private boolean yesAll;
	private boolean no;
	private boolean noAll;
	private boolean cancel;

	/**
	 * Displays the dialog that will ask the user about replacing the given duplicate object (e.g. file).
	 * Choices are yes, yes-to-all, no, no-to-all and cancel
	 * 
	 * @param nameOfDuplicate Name of the duplicate object (e.g. filename)
	 */
	public abstract void promptForAction( String nameOfDuplicate );

	/**
	 * Process the given choice by setting the corresponding field in the decider.
	 * 
	 * @param choice 0-based integer that represents the desired action
	 */
	protected void processChoice( int choice )
	{
		switch( choice )
		{
			case 0:
				yes = true;
				yesAll = no = noAll = cancel = false;
				break;
			case 1:
				yesAll = true;
				yes = no = noAll = cancel = false;
				break;
			case 2:
				no = true;
				yes = yesAll = noAll = cancel = false;
				break;
			case 3:
				noAll = true;
				yes = yesAll = no = cancel = false;
				break;
			case 4:
			case -1:
				cancel = true;
				yes = yesAll = no = noAll = false;
				break;
			default:
				//this shouldn't happen
		}
	}

	public boolean isYes()
	{
		return yes;
	}

	public boolean isYesAll()
	{
		return yesAll;
	}

	public boolean isNo()
	{
		return no;
	}

	public boolean isNoAll()
	{
		return noAll;
	}

	public boolean isCancel()
	{
		return cancel;
	}
}
