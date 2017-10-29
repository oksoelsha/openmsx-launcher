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
package info.msxlaunchers.openmsx.launcher.ui.view;

import info.msxlaunchers.openmsx.launcher.ui.view.platform.ViewPlatformModule;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public class ViewModule extends AbstractModule
{
	@Override 
	protected void configure()
	{
		bind( MainView.class ).to( MainSwingView.class ).in( Singleton.class );
		bind( SettingsView.class ).to( SettingsSwingView.class );
		bind( ProfileEditingView.class ).to( ProfileEditingSwingView.class );
		bind( ScannerView.class ).to( ScannerSwingView.class );
		bind( FilterEditingView.class ).to( FilterEditingSwingView.class );
		bind( GamePropertiesView.class ).to( GamePropertiesSwingView.class );
		bind( BlueMSXLauncherDatabaseImporterView.class ).to( BlueMSXLauncherDatabaseImporterSwingView.class );
		bind( DatabaseManagerView.class ).to( DatabaseManagerSwingView.class );
		bind( UpdateCheckerView.class ).to( UpdateCheckerSwingView.class );
		bind( AddDraggedAndDroppedGamesView.class ).to( AddDraggedAndDroppedGamesSwingView.class );
		bind( DatabaseBackupsView.class ).to( DatabaseBackupsSwingView.class );
		bind( ActivityViewerView.class ).to( ActivityViewerSwingView.class );
		bind( PatcherView.class ).to( PatcherSwingView.class );
		bind( MachineUpdateView.class ).to( MachineUpdateSwingView.class );

		install( new ViewPlatformModule() );
	}
}