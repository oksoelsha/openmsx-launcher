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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.importer.DatabaseImporterFactory;
import info.msxlaunchers.openmsx.launcher.ui.view.BlueMSXLauncherDatabaseImporterView;
import info.msxlaunchers.openmsx.machine.InvalidMachinesDirectoryException;
import info.msxlaunchers.openmsx.machine.MachineLister;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;

/**
 * Implementation of <code>BlueMSXLauncherImporterPresenter</code>
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
final class BlueMSXLauncherDatabasesImporterPresenterImpl implements BlueMSXLauncherDatabasesImporterPresenter
{
	private final MainPresenter mainPresenter;
	private final MachineLister machineLister;
	private final BlueMSXLauncherDatabaseImporterView view;
	private final DatabaseImporterFactory databaseImporterFactory;

	private final String DATABASE_EXTENSION = ".db";

	//model
	private Language currentLanguage = null;
	private boolean currentRightToLeft = false;

	@Inject
	BlueMSXLauncherDatabasesImporterPresenterImpl( MainPresenter mainPresenter,
			MachineLister machineLister,
			BlueMSXLauncherDatabaseImporterView view,
			DatabaseImporterFactory databaseImporterFactory ) throws IOException
	{
		this.mainPresenter = Objects.requireNonNull( mainPresenter );
		this.machineLister = Objects.requireNonNull( machineLister );
		this.view = Objects.requireNonNull( view );
		this.databaseImporterFactory = Objects.requireNonNull( databaseImporterFactory );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.BlueMSXLauncherDatabasesImporterPresenter#onRequestImportBlueMSXLauncherDatabasesScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestImportBlueMSXLauncherDatabasesScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException
	{
		this.currentLanguage = currentLanguage;
		this.currentRightToLeft = currentRightToLeft;

		try
		{
			view.displayScreen( currentLanguage, currentRightToLeft, machineLister.get() );
		}
		catch( InvalidMachinesDirectoryException imde )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_INVALID_MACHINES_DIRECTORY );
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.BlueMSXLauncherDatabasesImporterPresenter#onRequestImportBlueMSXLauncherDatabasesAction(java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public int onRequestImportBlueMSXLauncherDatabasesAction( String path, String[] databaseNames, String machine ) throws LauncherException
	{
		File databases[] = new File[databaseNames.length];
		for(int ix = 0; ix < databases.length; ix++)
		{
			databases[ix] = new File( path, databaseNames[ix] + DATABASE_EXTENSION );
		}

		Set<String> importedDatabases = null;

		try
		{
			importedDatabases = databaseImporterFactory.create( machine )
					.importDatabases( databases, new BlueMSXLauncherDatabasesImporterActionDecider( view, currentLanguage, currentRightToLeft ) );
		}
		catch( IOException ioe )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		mainPresenter.onAcceptImportBlueMSXLauncherDatabasesAction( importedDatabases );

		return importedDatabases.size();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.BlueMSXLauncherDatabasesImporterPresenter#onGetDatabasesInDirectory(java.io.File)
	 */
	@Override
	public Set<String> onGetDatabasesInDirectory( File directory )
	{
		Set<String> databases = new HashSet<>();

		File files[] = directory.listFiles();

		for( File file: files )
		{
			if( file.isFile() ) 
			{
				String filename = file.getName();
				if( filename.endsWith( DATABASE_EXTENSION ) )
				{
					databases.add( filename.substring( 0, filename.lastIndexOf( DATABASE_EXTENSION ) ) );
				}
			}
		}

		return databases;
	}
}
