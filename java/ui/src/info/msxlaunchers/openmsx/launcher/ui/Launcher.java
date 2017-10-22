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
package info.msxlaunchers.openmsx.launcher.ui;

import info.msxlaunchers.openmsx.extension.ExtensionListerModule;
import info.msxlaunchers.openmsx.game.repository.RepositoryDataModule;
import info.msxlaunchers.openmsx.game.scan.ScannerModule;
import info.msxlaunchers.openmsx.launcher.builder.GameBuilderModule;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataModule;
import info.msxlaunchers.openmsx.launcher.feed.FeedServiceModule;
import info.msxlaunchers.openmsx.launcher.importer.DatabaseImporterModule;
import info.msxlaunchers.openmsx.launcher.log.analyser.LoggerModule;
import info.msxlaunchers.openmsx.launcher.patch.PatcherModule;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceModule;
import info.msxlaunchers.openmsx.launcher.starter.StarterModule;
import info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter;
import info.msxlaunchers.openmsx.launcher.ui.presenter.PresenterModule;
import info.msxlaunchers.openmsx.launcher.ui.view.ViewModule;
import info.msxlaunchers.openmsx.launcher.updater.UpdateCheckerModule;
import info.msxlaunchers.openmsx.machine.MachineListerModule;
import info.msxlaunchers.platform.PlatformModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Main class that starts the application
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public class Launcher
{
	public static void main( String args[] )
	{
		Injector injector = Guice.createInjector(
				new AppModule(),
	    		new LauncherPersistenceModule(),
	    		new StarterModule(),
	    		new PlatformModule(),
	    		new ViewModule(),
	    		new PresenterModule(),
	    		new ScannerModule(),
	    		new RepositoryDataModule(),
	    		new MachineListerModule(),
	    		new ExtensionListerModule(),
	    		new ExtraDataModule(),
	    		new DatabaseImporterModule(),
	    		new GameBuilderModule(),
	    		new UpdateCheckerModule(),
	    		new LoggerModule(),
	    		new PatcherModule(),
	    		new FeedServiceModule()
				);

		MainPresenter launcher = injector.getInstance( MainPresenter.class );

		launcher.start();
	}
}
