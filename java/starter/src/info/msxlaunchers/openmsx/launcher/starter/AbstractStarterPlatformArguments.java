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
package info.msxlaunchers.openmsx.launcher.starter;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.platform.ArgumentsBuilder;

import java.io.File;
import java.util.List;

/**
 * Abstract implementation of <code>StarterPlatformArguments</code> that contains common methods for all platforms
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
abstract class AbstractStarterPlatformArguments implements StarterPlatformArguments
{
	private static final String PRESS_CTRL_SCRIPT = "pressctrl.tcl";
	private static final String PRESS_SHIFT_SCRIPT = "pressshift.tcl";

	@Override
	abstract public List<String> getArguments( Settings settings, Game game );

	void buildArguments( String openMSXPath, String openMSXBinary, ArgumentsBuilder argumentsBuilder, Game game, String extraDataDirectory )
	{
		File openMSXDirectory = new File( openMSXPath );
		File fullpath = new File( openMSXDirectory, openMSXBinary );

		argumentsBuilder.append( fullpath.getAbsolutePath() );

		String script = game.getTclScript();

		if( Utils.isEmpty( script ) )
		{
			//if no script is used then look at the other arguments
			argumentsBuilder.appendIfValueDefined( "-carta", game.getRomA() );
			argumentsBuilder.appendIfValueDefined( "-cartb", game.getRomB() );
			argumentsBuilder.appendIfValueDefined( "-ext", game.getExtensionRom() );
			argumentsBuilder.appendIfValueDefined( "-diska", game.getDiskA() );
			argumentsBuilder.appendIfValueDefined( "-diskb", game.getDiskB() );
			argumentsBuilder.appendIfValueDefined( "-cassetteplayer", game.getTape() );
			argumentsBuilder.appendIfValueDefined( "-hda", game.getHarddisk() );
			argumentsBuilder.appendIfValueDefined( "-machine", game.getMachine() );
			argumentsBuilder.appendIfValueDefined( "-laserdisc", game.getLaserdisc() );
			if( game.getFDDMode() == FDDMode.DISABLE_SECOND )
			{
				argumentsBuilder.appendIfValueDefined( "-script", new File( extraDataDirectory, PRESS_CTRL_SCRIPT ).toString() );
			}
			else if( game.getFDDMode() == FDDMode.DISABLE_BOTH )
			{
				argumentsBuilder.appendIfValueDefined( "-script", new File( extraDataDirectory, PRESS_SHIFT_SCRIPT ).toString() );
			}
		}
		else
		{
			//script in this case overrides all other arguments
			argumentsBuilder.appendIfValueDefined( "-script", script );
		}
	}
}
