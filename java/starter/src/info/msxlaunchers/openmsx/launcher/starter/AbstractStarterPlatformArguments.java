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

import info.msxlaunchers.openmsx.common.NumericalEnum;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.game.constants.InputDevice;
import info.msxlaunchers.platform.ArgumentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of <code>StarterPlatformArguments</code> that contains common methods for all platforms
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
abstract class AbstractStarterPlatformArguments implements StarterPlatformArguments
{
	private static final String TEMP_FILE_PREFIX = "openmsx-launcher-script";
	private static final String TEMP_FILE_EXT = ".tmp";

	private static final String ENABLE_GFX9000_LINE = "ext gfx9000" + System.lineSeparator() +
			"ext slotexpander" + System.lineSeparator() +
			"after time 10 \"set videosource GFX9000\"";
	private static final Map<NumericalEnum, String> scriptLinesMap = new HashMap<>();
	static
	{
		scriptLinesMap.put( InputDevice.JOYSTICK, "plug joyporta joystick1" );
		scriptLinesMap.put( InputDevice.JOYSTICK_KEYBOARD, "plug joyporta keyjoystick1" );
		scriptLinesMap.put( InputDevice.MOUSE, "plug joyporta mouse" );
		scriptLinesMap.put( InputDevice.ARKANOID_PAD, "plug joyporta arkanoidpad" );
		scriptLinesMap.put( InputDevice.TRACKBALL, "plug joyportb trackball" );
		scriptLinesMap.put( InputDevice.TOUCHPAD, "plug joyportb touchpad" );

		scriptLinesMap.put( FDDMode.DISABLE_SECOND, "after boot { keymatrixdown 6 2; after time 14 \"keymatrixup 6 2\" }");
		scriptLinesMap.put( FDDMode.DISABLE_BOTH, "after boot { keymatrixdown 6 1; after time 14 \"keymatrixup 6 1\" }" );
	}

	void buildArguments( String openMSXPath, String openMSXBinary, ArgumentsBuilder argumentsBuilder, Game game ) throws IOException
	{
		File openMSXDirectory = new File( openMSXPath );
		File fullpath = new File( openMSXDirectory, openMSXBinary );

		argumentsBuilder.append( fullpath.getAbsolutePath() );

		String script = game.getTclScript();

		if( !Utils.isEmpty( script ) && game.isTclScriptOverride() )
		{
			//script in this case overrides all other arguments
			argumentsBuilder.appendIfValueDefined( "-script", script );
		}
		else
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
			argumentsBuilder.appendIfValueDefined( "-script", script );

			//there's a potential conflict if the user provides their own script
			argumentsBuilder.appendIfValueDefined( "-script", getScriptIfNeeded( game ) );
		}
	}

	private String getScriptIfNeeded( Game game ) throws IOException
	{
		String script = null;
		if( game.getInputDevice() != InputDevice.NONE || game.getFDDMode() != FDDMode.ENABLE_BOTH || game.isConnectGFX9000() )
		{
			deleteOldTempFiles( Paths.get( System.getProperty( "java.io.tmpdir" ) ) );

			Path tempFile = Files.createTempFile( TEMP_FILE_PREFIX, TEMP_FILE_EXT );

			StringBuilder scriptLines = new StringBuilder();

			addLineToScriptIfParamDefined( scriptLines, game.getInputDevice() );
			addLineToScriptIfParamDefined( scriptLines, game.getFDDMode() );
			addLineToScriptIfParamDefined( scriptLines, game.isConnectGFX9000() );

			Files.write( tempFile, scriptLines.toString().getBytes() );

			script = tempFile.toString();
		}
		return script;
	}

	private void addLineToScriptIfParamDefined( StringBuilder lines, NumericalEnum param )
	{
		if( param != null )
		{
			String line = scriptLinesMap.get( param );
			if( line != null )
			{
				lines.append( line ).append( System.lineSeparator() );
			}
		}
	}

	private void addLineToScriptIfParamDefined( StringBuilder lines, boolean enableGFX9000 )
	{
		if( enableGFX9000 )
		{
			lines.append( ENABLE_GFX9000_LINE ).append( System.lineSeparator() );
		}
	}

	private void deleteOldTempFiles( Path directory ) throws IOException
	{
		String fileMatch = TEMP_FILE_PREFIX + "*" + TEMP_FILE_EXT;

		try ( DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream( directory, fileMatch ) )
		{
			for( Path newDirectoryStreamItem : newDirectoryStream )
			{
				Files.delete( newDirectoryStreamItem );
			}
		}
	}
}
