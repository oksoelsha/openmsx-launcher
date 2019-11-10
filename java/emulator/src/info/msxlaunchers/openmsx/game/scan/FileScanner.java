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
package info.msxlaunchers.openmsx.game.scan;

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.common.FileUtils;
import info.msxlaunchers.openmsx.common.HashUtils;
import info.msxlaunchers.openmsx.common.Nullable;
import info.msxlaunchers.openmsx.game.repository.RepositoryData;
import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Implementation for physical media scanning for games supported by openMSX
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class FileScanner implements Scanner
{
	private final GamePersister gamePersister;
	private final RepositoryData repositoryData;

	private boolean interrupted;

	private Set<Game> processedGames = null;
	private Set<String> processedGameNames = null;
	private Set<String> processedGameSha1Codes = null;
	private Map<String,RepositoryGame> sha1ToRepositoryGameMap = null;
	private final GameBuilder gameBuilder;
	private final ExtraDataGetter extraDataGetter;
	private final String baseDirectory;

	private boolean traverseSubDirectories;
	private String machine = null;
	private boolean searchROM = false;
	private boolean searchDisk = false;
	private boolean searchTape = false;
	private boolean searchLaserdisc = false;
	private boolean getNameFromOpenMSXDatabase;
	private Map<String,ExtraData> extraDataMap;

	private final String NAME_COLLISION_SEPARATOR = "__";

	private final String EXTENSION_ROM_IDE = "ide";

	@Inject
	FileScanner( GamePersister gamePersister, RepositoryData repositoryData, GameBuilder gameBuilder, ExtraDataGetter extraDataGetter,
			@Nullable @Named("BaseDirectory") String baseDirectory )
	{
		this.gamePersister = Objects.requireNonNull( gamePersister );
		this.repositoryData = Objects.requireNonNull( repositoryData );
		this.gameBuilder = Objects.requireNonNull( gameBuilder );
		this.extraDataGetter = Objects.requireNonNull( extraDataGetter );
		this.baseDirectory = baseDirectory;

		//the following flag can be set from a different thread to stop
		interrupted = false;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.scan.Scanner#scan(java.lang.String[], boolean, java.lang.String, boolean, boolean, java.lang.String, boolean, boolean, boolean, boolean, boolean, boolean)
	 */
	@Override
	public int scan( String[] paths,
			boolean traverseSubDirectories,
			String database,
			boolean newDatabase,
			boolean append,
			String machine,
			boolean searchROM,
			boolean searchDisk,
			boolean searchTape,
			boolean searchLaserdisc,
			boolean getNameFromOpenMSXDatabase,
			boolean backupDatabase )
		throws GamePersistenceException, IOException
	{
		interrupted = false;

		this.traverseSubDirectories = traverseSubDirectories;
		this.machine = machine;
		this.searchROM = searchROM;
		this.searchDisk = searchDisk;
		this.searchTape = searchTape;
		this.searchLaserdisc = searchLaserdisc;
		this.getNameFromOpenMSXDatabase = getNameFromOpenMSXDatabase;

		this.extraDataMap = extraDataGetter.getExtraData();

		processedGames = new HashSet<>();
		processedGameNames = new HashSet<>();
		processedGameSha1Codes = new HashSet<>();

		//append to or create new database
		if( newDatabase )
		{
			gamePersister.createDatabase( database );
			//TODO: what if this db exists?
		}
		else
		{
			if( backupDatabase )
			{
				gamePersister.backupDatabase( database );
			}

			if( !append )
			{
				gamePersister.recreateDatabase( database );
			}

			Set<Game> savedGames = gamePersister.getGames( database );

			for( Game processedGame: savedGames )
			{
				processedGameNames.add( processedGame.getName() );
				processedGameSha1Codes.add( processedGame.getSha1Code() );
			}
		}

		if( getNameFromOpenMSXDatabase )
		{
			sha1ToRepositoryGameMap = repositoryData.getRepositoryInfo();
		}

		//start the scanning
		int totalFound = 0;
		for( String path: paths )
		{
			if( interrupted )
			{
				break;
			}

			totalFound += traverse( new File( path ), getAbsolutePath( path, baseDirectory ), true );
		}

		try
		{
			gamePersister.saveGames( processedGames, database );
		}
		catch( GamePersistenceException gpe )
		{
			//TODO: what to do? just skip for now
		}

		return totalFound;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.scan.Scanner#interrupt()
	 */
	public void interrupt()
	{
		interrupted = true;
	}

	private int traverse( File relativePath, File absolutePath, boolean firstCall )
	{
		int totalFound = 0;

		if( absolutePath.isFile() )
		{
			totalFound += processFile( absolutePath, relativePath.getParentFile() );
		}
		else if ( traverseSubDirectories || firstCall )
		{
			File[] fileList = absolutePath.listFiles();

			if( fileList != null )
			{
				for( File file: fileList )
				{
					totalFound += traverse( new File( relativePath, file.getName() ), file, false );
				}
			}
		}

		return totalFound;
	}

	private File getAbsolutePath( String relativePath, String baseDirectory )
	{
		if( baseDirectory == null )
		{
			return new File( relativePath );
		}
		else
		{
			return new File( new File( baseDirectory ), relativePath );
		}
	}

	private String getRealFullFilePath( File file, File relativePath )
	{
		return new File( relativePath, file.getName() ).getAbsolutePath();
	}

	private int processFile( File file, File relativePath )
	{
		int added = 0;

		if( FileTypeUtils.isROM( file ) ||
				FileTypeUtils.isDisk( file ) ||
				FileTypeUtils.isTape( file ) ||
				FileTypeUtils.isHarddisk( file ) ||
				FileTypeUtils.isLaserdisc( file ) )
		{
			String sha1Code = HashUtils.getSHA1Code( file );
			added = processPotentialGame( file, getRealFullFilePath( file, relativePath ), file.length(), sha1Code );
		}
		else if( FileTypeUtils.isZIP( file ) )
		{
			added = processZipFile( file, relativePath );
		}

		return added;
	}

	private int processPotentialGame( File file, String fileNameToUse, long fileSize, String sha1Code )
    {
		int added = 0;

		if( file != null )
		{
			if( searchROM && FileTypeUtils.isROM( file ) )
			{
				added = addToProcessedGames( getGameName( file, sha1Code ),
						fileNameToUse,
						null,
						null,
						null,
						null,
						null,
						sha1Code,
						fileSize );
			}
			else if( searchDisk && FileTypeUtils.isDisk( file ) )
			{
				//the following will check whether this file is a regular disk or harddisk based on its size.
				//the disk extensions contain more than just dsk, even though dsk is the only one that can be a harddisk.
				//it's ok, we'll leave this logic the way it is. Later in this method we'll check for harddisks
				if( fileSize <= FileTypeUtils.MAX_DISK_FILE_SIZE )
				{
					added = addToProcessedGames( getGameName( file, sha1Code ),
							null,
							fileNameToUse,
							null,
							null,
							null,
							null,
							sha1Code,
							fileSize );
				}
				else
				{
					added = addToProcessedGames( getGameName( file, sha1Code ),
							null,
							null,
							fileNameToUse,
							EXTENSION_ROM_IDE,
							null,
							null,
							sha1Code,
							fileSize );
				}					
			}
			else if( searchTape && FileTypeUtils.isTape( file ) )
			{
				added = addToProcessedGames( getGameName( file, sha1Code ),
						null,
						null,
						null,
						null,
						fileNameToUse,
						null,
						sha1Code,
						fileSize );
			}
			else if( searchDisk && FileTypeUtils.isHarddisk( file ) )
			{
				//we're combining disks and harddisks in the searchDisk flag
				added = addToProcessedGames( getGameName( file, sha1Code ),
						null,
						null,
						fileNameToUse,
						EXTENSION_ROM_IDE,
						null,
						null,
						sha1Code,
						fileSize );
			}
			else if( searchLaserdisc && FileTypeUtils.isLaserdisc( file ) )
			{
				added = addToProcessedGames( getGameName( file, sha1Code ),
						null,
						null,
						null,
						null,
						null,
						fileNameToUse,
						sha1Code,
						fileSize );
			}
		}

		return added;
    }

	private int processZipFile( File zipFile, File relativePath )
	{
		int added = 0;

		try( ZipFile zip = new ZipFile( zipFile ) )
		{
			ZipEntry firstZipEntry = zip.entries().nextElement();

			try( InputStream inputStream = zip.getInputStream( firstZipEntry ) )
			{
				String sha1Code = HashUtils.getSHA1Code( inputStream );

				added = processPotentialGame( new File( firstZipEntry.getName() ),
					getRealFullFilePath( zipFile, relativePath ),
					firstZipEntry.getSize(),
					sha1Code );
			}
		}
		catch( IOException e )
		{
			//invalid ZIP file - skip and return null
		}

		return added;
	}

	private int addToProcessedGames( String name,
										String rom,
										String disk,
										String harddisk,
										String extensionRom,
										String tape,
										String laserdisc,
										String sha1Code,
										long fileSize )
	{
		int added = 0;

		if( !processedGameSha1Codes.contains( sha1Code ) )
		{
			String adjustedName = adjustedNameIfNecessary( name );

			Game game = gameBuilder.createGameObjectForScannedFiles( adjustedName, machine, rom, extensionRom, disk, tape, harddisk, laserdisc, sha1Code, fileSize, extraDataMap );

			if( game != null )
			{
				processedGames.add( game );
	
				processedGameNames.add( adjustedName );
				processedGameSha1Codes.add( sha1Code );
				
				added = 1;
			}
		}

		return added;
	}

	private String adjustedNameIfNecessary( String name )
	{
		StringBuilder nameBuffer = new StringBuilder( name );
		int counter = 0;

		while( processedGameNames.contains( nameBuffer.toString() ) )
		{
			nameBuffer = new StringBuilder( name ).append( NAME_COLLISION_SEPARATOR ).append( ++counter );
		}

		return nameBuffer.toString();
	}

	private String getGameName( File file, String sha1Code )
	{
		String name = null;

		if( getNameFromOpenMSXDatabase )
		{
			RepositoryGame repositoryGame = sha1ToRepositoryGameMap.get( sha1Code );
			if( repositoryGame != null )
			{
				name = repositoryGame.getTitle();
				if( name == null )
				{
					name = FileUtils.getFileNameWithoutExtension( file );
				}
			}
			else
			{
				name = FileUtils.getFileNameWithoutExtension( file );
			}
		}
		else
		{
			name = FileUtils.getFileNameWithoutExtension( file );
		}

		return name;
	}
}
