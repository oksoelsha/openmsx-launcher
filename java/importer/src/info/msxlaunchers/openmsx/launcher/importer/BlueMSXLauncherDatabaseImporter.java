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
package info.msxlaunchers.openmsx.launcher.importer;

import info.msxlaunchers.openmsx.common.FileUtils;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;
import info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * blueMSX Launcher implementation of the interface <code>DatabaseImporter</code>
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
final class BlueMSXLauncherDatabaseImporter implements DatabaseImporter
{
	private final String machine;
	private final GameBuilder gameBuilder;
	private final GamePersister gamePersister;
	private final ExtraDataGetter extraDataGetter;

	private Map<String,ExtraData> extraDataMap;

	@Inject
	BlueMSXLauncherDatabaseImporter( @Assisted String machine,
			GameBuilder gameBuilder,
			GamePersister gamePersister,
			ExtraDataGetter extraDataGetter )
	{
		this.machine = Objects.requireNonNull( machine );
		this.gameBuilder = Objects.requireNonNull( gameBuilder );
		this.gamePersister = Objects.requireNonNull( gamePersister );
		this.extraDataGetter = Objects.requireNonNull( extraDataGetter );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.importer.DatabaseImporter#importDatabases(java.io.File[], info.msxlaunchers.openmsx.launcher.persistence.game.ActionDecider)
	 */
	@Override
	public Set<String> importDatabases( File[] databases, ActionDecider actionDecider ) throws IOException
	{
		extraDataMap = extraDataGetter.getExtraData();

		Set<String> importedDatabases = new HashSet<String>();
		Set<String> currentOpenMSXLauncherDatabases = gamePersister.getDatabases();

		for ( File database: databases )
		{
			String databaseName = FileUtils.getFileNameWithoutExtension( database );

			if( currentOpenMSXLauncherDatabases.contains( databaseName ) )
			{
				if( !actionDecider.isYesAll() && !actionDecider.isNoAll() )
				{
					actionDecider.promptForAction( databaseName );
				}

				if( actionDecider.isYes() || actionDecider.isYesAll() )
				{
					//then this is the replace case - delete the current database
					try
					{
						gamePersister.deleteDatabase( databaseName );
					}
					catch( GamePersistenceException gpe )
					{
						//this shouldn't happen
					}

					migrateDatabaseToOpenMSX( importedDatabases, database, databaseName );
				}
				else if( actionDecider.isNo() || actionDecider.isNoAll() )
				{
					//do nothing - skip
				}
				else if( actionDecider.isCancel() )
				{
					break;
				}
				else
				{
					//this shouldn't happen
					throw new RuntimeException( "At least one action must be set" );
				}
			}
			else
			{
				migrateDatabaseToOpenMSX( importedDatabases, database, databaseName );
			}
		}

		return Collections.unmodifiableSet( importedDatabases );
	}

	private void migrateDatabaseToOpenMSX( Set<String> importedDatabases, File database, String databaseName ) throws IOException
	{
		Set<Game> games = new HashSet<Game>();
		String line;
		boolean srcDatabaseFound;
		String importedDatabaseName = null;

		try( BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( database ), "UTF-8" ) ) )
		{
			srcDatabaseFound = true;
			while( (line = reader.readLine()) != null )
			{
				if( !Utils.isEmpty( line ) )
				{
					String parts[] = line.split( "\\|" );
	
					String name = getElementFromDatabaseRowArray( parts, 0 );
					String info = getElementFromDatabaseRowArray( parts, 6 );
					String romA = getElementFromDatabaseRowArray( parts, 1 );
					String romB = getElementFromDatabaseRowArray( parts, 2 );
					String extensionRom = getExtensionRom( getElementFromDatabaseRowArray( parts, 13 ) );
					String diskA = getElementFromDatabaseRowArray( parts, 3 );
					String diskB = getElementFromDatabaseRowArray( parts, 4 );
					String tape = getElementFromDatabaseRowArray( parts, 11 );
					String harddisk = getElementFromDatabaseRowArray( parts, 15 );
	
					Game game = gameBuilder.createGameObjectForImportedData( name, info, machine, romA, romB, extensionRom, diskA, diskB, tape, harddisk, extraDataMap );
	
					if( game != null )
					{
						games.add( game );
					}
				}
			}
		}
		catch( FileNotFoundException fnfe )
		{
			//this shouldn't happen unless the database was deleted just before calling this method - skip
			srcDatabaseFound = false;
		}

		//now save these games
		if( srcDatabaseFound )
		{
			try
			{
				gamePersister.createDatabase( databaseName );
			}
			catch( GamePersistenceException gpe )
			{
				//this shouldn't happen
			}

			try
			{
				gamePersister.saveGames( games, databaseName );
				importedDatabaseName = databaseName;
			}
			catch( GamePersistenceException gpe )
			{
				//this shouldn't happen
			}
		}

		if( importedDatabaseName != null )
		{
			importedDatabases.add( importedDatabaseName );
		}
	}

	private String getElementFromDatabaseRowArray( String[] array, int index )
	{
		String element = null;

		if( index < array.length )
		{
			element = Utils.resetIfEmpty( array[index] );
		}

		return element;
	}

	private String getExtensionRom( String extensionRom )
	{
		String openMSXExtensionRom = null;

		//return quickly if extensionRom is null, which is most of the time
		if( extensionRom != null )
		{
			//blueMSX Launcher extension ROMs scc, scc+ and fmpac map to openMSX's but sunriseide and fmpak don't
			switch( extensionRom )
			{
				case "scc":
				case "scc+":
				case "fmpac":
					openMSXExtensionRom = extensionRom;
					break;
				case "sunriseide":
					openMSXExtensionRom = "ide";
					break;
				case "fmpak":
					openMSXExtensionRom = "pac";
					break;
				default:
					//anything else is invalid for blueMSX Launcher - just return null
					break;
			}
		}

		return openMSXExtensionRom;
	}
}
