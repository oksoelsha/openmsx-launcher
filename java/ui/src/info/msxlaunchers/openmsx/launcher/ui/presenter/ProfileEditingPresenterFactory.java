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

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;

/**
 * Interface to by used by Guice's assisted injection to inject <code>ProfileEditingPresenter</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface ProfileEditingPresenterFactory
{
	ProfileEditingPresenter create( Settings settings, String currentDatabase, Game currentSelectedGame );
}
