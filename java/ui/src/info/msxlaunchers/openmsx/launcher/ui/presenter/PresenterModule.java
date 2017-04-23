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

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public class PresenterModule extends AbstractModule
{
	@Override 
	protected void configure()
	{
		bind( MainPresenter.class ).to( MainPresenterImpl.class ).in( Singleton.class );
		bind( SettingsPresenter.class ).to( SettingsPresenterImpl.class );
		install( new FactoryModuleBuilder().implement( ProfileEditingPresenter.class, ProfileEditingPresenterImpl.class ).build( ProfileEditingPresenterFactory.class ) );
		bind( ScannerPresenter.class ).to( ScannerPresenterImpl.class );
		bind( FilterEditingPresenter.class ).to( FilterEditingPresenterImpl.class );
		bind( GamePropertiesPresenter.class ).to( GamePropertiesPresenterImpl.class );
		bind( BlueMSXLauncherDatabasesImporterPresenter.class ).to( BlueMSXLauncherDatabasesImporterPresenterImpl.class );
		install( new FactoryModuleBuilder().implement( DatabaseManagerPresenter.class, DatabaseManagerPresenterImpl.class ).build( DatabaseManagerPresenterFactory.class ) );
		bind( UpdateCheckerPresenter.class ).to( UpdateCheckerPresenterImpl.class );
		install( new FactoryModuleBuilder().implement( DraggedAndDroppedGamesPresenter.class, DraggedAndDroppedGamesPresenterImpl.class ).build( DraggedAndDroppedGamesPresenterFactory.class ) );
		install( new FactoryModuleBuilder().implement( DatabaseBackupsPresenter.class, DatabaseBackupsPresenterImpl.class ).build( DatabaseBackupsPresenterFactory.class ) );
		bind( ActivityViewerPresenter.class ).to( ActivityViewerPresenterImpl.class );
		bind( PatcherPresenter.class ).to( IPSPatcherPresenter.class );
	}
}