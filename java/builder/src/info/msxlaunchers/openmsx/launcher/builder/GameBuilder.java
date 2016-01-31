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
package info.msxlaunchers.openmsx.launcher.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.common.HashUtils;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;

/**
 * Factory/Builder class to create Game object with extra data given different arguments
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public class GameBuilder
{
	private final String generationMSXURL;

	@Inject
	GameBuilder( @Named("GenerationMSXURL") String generationMSXURL )
	{
		this.generationMSXURL = Objects.requireNonNull( generationMSXURL );
	}

	/*
	 * Create and return a Game object given data entered by the user (e.g. in the Add/Edit screen)
	 * 
	 * @param name Game name
	 * @param info Info file or link
	 * @param machine openMSX machine
	 * @param romA ROM A file name
	 * @param romB ROM B file name
	 * @param extensionRom openMSX Extension ROM
	 * @param diskA Disk A file name
	 * @param diskB Disk B file name
	 * @param tape Tape file name
	 * @param harddisk Hard disk file name
	 * @param laserdisc Laserdisc file name
	 * @param script Script file name
	 * @param extraDataMap Map containing extra data for game by sha1 code
	 * @return Game object or null if all main game fields are null
	 */
	public Game createGameObjectForDataEnteredByUser( String name,
		String info,
		String machine,
		String romA, String romB, String extensionRom,
		String diskA, String diskB,
		String tape,
		String harddisk,
		String laserdisc,
		String script,
		Map<String,ExtraData> extraDataMap )
	{
		boolean notScript = isNotScript( romA, romB, diskA, diskB, tape, harddisk, laserdisc, script );

		FileSha1CodeAndSize fileSha1CodeAndSize;
		if( notScript )
		{
			fileSha1CodeAndSize = getFileSha1CodeAndSize( romA, romB, diskA, diskB, tape, harddisk, laserdisc );
		}
		else
		{
			fileSha1CodeAndSize = new FileSha1CodeAndSize( null, 0 );
		}

		return createGameObject( name,
				info,
				false,
				machine,
				romA, romB, extensionRom,
				diskA, diskB,
				tape,
				harddisk,
				laserdisc,
				script,
				fileSha1CodeAndSize.sha1Code,
				fileSha1CodeAndSize.size,
				initIfNull( extraDataMap ) );
	}

	/*
	 * Create and return a Game object given data imported from somewhere (e.g. from blueMSX Launcher)
	 * 
	 * @param name Game name
	 * @param info Info file or link
	 * @param machine openMSX machine
	 * @param romA ROM A file name
	 * @param romB ROM B file name
	 * @param extensionRom openMSX Extension ROM
	 * @param diskA Disk A file name
	 * @param diskB Disk B file name
	 * @param tape Tape file name
	 * @param harddisk Hard disk file name
	 * @param extraDataMap Map containing extra data for game by sha1 code
	 * @return Game object or null if main file doesn't exist or if all main game fields are null
	 */
	public Game createGameObjectForImportedData( String name,
		String info,
		String machine,
		String romA, String romB, String extensionRom,
		String diskA, String diskB,
		String tape,
		String harddisk,
		Map<String,ExtraData> extraDataMap )
	{
		FileSha1CodeAndSize fileSha1CodeAndSize = getFileSha1CodeAndSize( romA, romB, diskA, diskB, tape, harddisk, null );

		Game game;
		if( fileSha1CodeAndSize.sha1Code != null )
		{
			game = createGameObject( name,
				info,
				false,
				machine,
				romA, romB, extensionRom,
				diskA, diskB,
				tape,
				harddisk,
				null,
				null,
				fileSha1CodeAndSize.sha1Code,
				fileSha1CodeAndSize.size,
				initIfNull( extraDataMap ) );
		}
		else
		{
			//otherwise return null if there was no sha1Code or size, which means the file doesn't exist
			game = null;
		}

		return game;
	}

	/*
	 * Create and return a Game object given data from a media scan process
	 * 
	 * @param name Game name
	 * @param machine openMSX machine
	 * @param rom ROM A file name
	 * @param extensionRom openMSX Extension ROM
	 * @param disk Disk A file name
	 * @param tape Tape file name
	 * @param harddisk Hard disk file name
	 * @param laserdisc Laserdisc file name
	 * @param sha1Code SHA1 code of the main file
	 * @param filesize Size of the main file
	 * @param extraDataMap Map containing extra data for game by sha1 code
	 * @return Game object or null if all main game fields are null
	 */
	public Game createGameObjectForScannedFiles( String name,
		String machine,
		String rom, String extensionRom,
		String disk,
		String tape,
		String harddisk,
		String laserdisc,
		String sha1Code,
		long fileSize,
		Map<String,ExtraData> extraDataMap )
	{
		return createGameObject( name,
			null,
			true,
			machine,
			rom, null, extensionRom,
			disk, null,
			tape,
			harddisk,
			laserdisc,
			null,
			sha1Code,
			fileSize,
			initIfNull( extraDataMap ) );
	}

	/*
	 * Create and return a Game object based on another Game and update the extra data
	 * 
	 * @param game Game object
	 * @param extraDataMap Map containing extra data for game by sha1 code
	 * @return New Game object with updated extra data
	 */
	public Game createGameObjectFromGameAndUpdateExtraData( Game game, Map<String,ExtraData> extraDataMap )
	{
		Objects.requireNonNull( game );

		return createGameObject( game.getName(),
				game.getInfo(),
				false,
				game.getMachine(),
				game.getRomA(), game.getRomB(), game.getExtensionRom(),
				game.getDiskA(), game.getDiskB(),
				game.getTape(),
				game.getHarddisk(),
				game.getLaserdisc(),
				game.getTclScript(),
				game.getSha1Code(),
				game.getSize(),
				initIfNull( extraDataMap ) );
	}

	private static boolean isNotScript( String romA, String romB, String diskA, String diskB,
			String tape, String harddisk, String laserdisc, String script )
	{
		return !Utils.isEmpty( romA ) || !Utils.isEmpty( romB ) ||
				!Utils.isEmpty( diskA ) || !Utils.isEmpty( diskB ) ||
				!Utils.isEmpty( tape ) || !Utils.isEmpty( harddisk ) || !Utils.isEmpty( laserdisc ) ||
				Utils.isEmpty( script );
	}

	private Game createGameObject( String name,
				String info,
				boolean useGenenerationMSXURLAsInfo,
				String machine,
				String romA, String romB, String extensionRom,
				String diskA, String diskB,
				String tape,
				String harddisk,
				String laserdisc,
				String script,
				String sha1Code,
				long fileSize,
				Map<String,ExtraData> extraDataMap )
	{
		int msxGenID = 0;
		boolean isMSX = false;
		boolean isMSX2 = false;
		boolean isMSX2Plus = false;
		boolean isTurboR = false;
		boolean isPSG = false;
		boolean isSCC = false;
		boolean isSCCI = false;
		boolean isPCM = false;
		boolean isMSXMUSIC = false;
		boolean isMSXAUDIO = false;
		boolean isMoonsound = false;
		boolean isMIDI = false;
		String screenshotSuffix = null;
		int genre1 = 0;
		int genre2 = 0;
		String infoField = info;

		ExtraData extraData = extraDataMap.get( sha1Code );

		if( extraData != null )
		{
			msxGenID = extraData.getMSXGenerationsID();
			if( useGenenerationMSXURLAsInfo )
			{
				infoField = generationMSXURL + msxGenID;
			}
			isMSX = extraData.isMSX();
			isMSX2 = extraData.isMSX2();
			isMSX2Plus = extraData.isMSX2Plus();
			isTurboR = extraData.isTurboR();
			isPSG = extraData.isPSG();
			isSCC = extraData.isSCC();
			isSCCI = extraData.isSCCI();
			isPCM = extraData.isPCM();
			isMSXMUSIC = extraData.isMSXMUSIC();
			isMSXAUDIO = extraData.isMSXAUDIO();
			isMoonsound = extraData.isMoonsound();
			isMIDI = extraData.isMIDI();
			screenshotSuffix = extraData.getSuffix();
			genre1 = extraData.getGenre1();
			genre2 = extraData.getGenre2();
		}

		Game game = null;
		try
		{
			game = Game.name( name )
				.info( infoField )
				.machine( machine )
				.romA( romA ).romB( romB ).extensionRom( extensionRom )
				.diskA( diskA ).diskB( diskB )
				.tape( tape )
				.harddisk( harddisk )
				.laserdisc( laserdisc )
				.tclScript( script )
				.sha1Code( sha1Code )
				.size( fileSize )
				.msxGenID( msxGenID )
				.isMSX( isMSX ).isMSX2( isMSX2 ).isMSX2Plus( isMSX2Plus ).isTurboR( isTurboR )
				.isPSG( isPSG ).isSCC( isSCC ).isSCCI( isSCCI ).isPCM( isPCM )
				.isMSXMUSIC( isMSXMUSIC ).isMSXAUDIO( isMSXAUDIO ).isMoonsound( isMoonsound ).isMIDI( isMIDI )
				.genre1( Genre.fromValue( genre1  ) ).genre2( Genre.fromValue( genre2 ) )
				.screenshotSuffix( screenshotSuffix )
				.build();
		}
		catch( IllegalArgumentException iae )
		{
			//happens because all main fields are null. This should not happen in normal circumstances, but if it does return null
		}

		return game;
	}

	private FileSha1CodeAndSize getFileSha1CodeAndSize( String romA, String romB, String diskA, String diskB, String tape, String harddisk, String laserdisc )
	{
		FileSha1CodeAndSize fileSha1CodeAndSize = null;

		String mainFile = FileTypeUtils.getMainFile( romA, romB, diskA, diskB, tape, harddisk, laserdisc, null );
		if( mainFile != null )
		{
			String sha1Code = null;
			long fileSize = 0;
			File file = new File( mainFile );
			if( file.exists() )
			{
				if( FileTypeUtils.isZIP( file ) )
				{
					try( ZipFile zip = new ZipFile( file ) )
					{
						ZipEntry firstZipEntry = zip.entries().nextElement();

						try( InputStream inputStream = zip.getInputStream( firstZipEntry ) )
						{
							sha1Code = HashUtils.getSHA1Code( inputStream );
							fileSize = firstZipEntry.getSize();
						}
					}
					catch( ZipException e )
					{
						//invalid ZIP file - skip and return null
					}
					catch( IOException e )
					{
						//shouldn't happen here
					}
				}
				else
				{
					fileSize = file.length();
					sha1Code = HashUtils.getSHA1Code( file );
				}
			}

			fileSha1CodeAndSize = new FileSha1CodeAndSize( sha1Code, fileSize );
		}
		else
		{
			//if there's no file, which shouldn't happen in reality, then return an empty data rather than null
			fileSha1CodeAndSize = new FileSha1CodeAndSize( null, 0 );
		}

		return fileSha1CodeAndSize;
	}

	private Map<String,ExtraData> initIfNull( Map<String,ExtraData> extraDataMap )
	{
		if( extraDataMap == null )
		{
			return Collections.emptyMap();
		}
		else
		{
			return extraDataMap;
		}
	}

	private static class FileSha1CodeAndSize
	{
		private final String sha1Code;
		private final long size;

		FileSha1CodeAndSize( String sha1Code, long size )
		{
			this.sha1Code = sha1Code;
			this.size = size;
		}
	}
}
