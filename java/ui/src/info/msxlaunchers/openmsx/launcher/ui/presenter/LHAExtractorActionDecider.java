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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.common.ActionDecider;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.LHAExtractorView;

/**
 * Implementation of the <code>ActionDecider</code> that prompts user for action when an extracted file exists already in the target directory.
 * This implementation will request input from the user through the View.
 * 
 * @since v1.14
 * @author Sam Elsharif
 *
 */
final class LHAExtractorActionDecider implements ActionDecider
{
	private final LHAExtractorView view;
	private final Language language;
	private final boolean rightToLeft;

	private boolean yes, yesAll, no, noAll, cancel;

	LHAExtractorActionDecider( LHAExtractorView view, Language language, boolean rightToLeft )
	{
		this.view = view;
		this.language = language;
		this.rightToLeft = rightToLeft;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider#promptForAction(java.lang.String)
	 */
	@Override
	public void promptForAction( String filename )
	{
		int choice = view.displayAndGetActionDecider( filename, language, rightToLeft );

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
				throw new RuntimeException( "Unsupported ActionDecider choice" );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider#isYes()
	 */
	@Override
	public boolean isYes()
	{
		return yes;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider#isYesAll()
	 */
	@Override
	public boolean isYesAll()
	{
		return yesAll;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider#isNo()
	 */
	@Override
	public boolean isNo()
	{
		return no;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider#isNoAll()
	 */
	@Override
	public boolean isNoAll()
	{
		return noAll;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider#isCancel()
	 */
	@Override
	public boolean isCancel()
	{
		return cancel;
	}
}
