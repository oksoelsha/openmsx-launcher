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
package info.msxlaunchers.openmsx.launcher.ui.view.platform;

import info.msxlaunchers.openmsx.common.OSUtils;
import info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public class PlatformViewPropertiesProvider implements Provider<PlatformViewProperties>
{
	private final MainPresenter mainPresenter;

	@Inject
	PlatformViewPropertiesProvider(MainPresenter mainPresenter)
	{
		this.mainPresenter = mainPresenter;
	}

	@Override
	public PlatformViewProperties get()
	{
		if(OSUtils.isWindows())
		{
			return new WindowsViewProperties();
		}
		else if(OSUtils.isMac())
		{
			return new MacViewProperties(mainPresenter);
		}
		else if(OSUtils.isLinux())
		{
			return new LinuxViewProperties();
		}
		else if( OSUtils.isBSD() )
		{
			return new BSDViewProperties();
		}
		else
		{
			return new OtherPlatformViewProperties();
		}
	}
}
