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

import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;

import javax.swing.JFrame;

/**
 * BSD implementation of <code>PlatformViewProperties</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
final class BSDViewProperties implements PlatformViewProperties
{
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties#getSuggestedOpenMSXPath()
	 */
	@Override
	public String getSuggestedOpenMSXPath()
	{
		return "/usr/local/bin";
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties#isMachinesFolderInsideOpenMSX()
	 */
	@Override
	public boolean isMachinesFolderInsideOpenMSX()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties#getOpenMSXMachinesPath()
	 */
	@Override
	public String getOpenMSXMachinesPath()
	{
		return "/usr/local/share/openmsx/machines";
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties#setDisplayProperties(javax.swing.JFrame)
	 */
	@Override
	public void setDisplayProperties(JFrame window)
	{
		NimbusLookAndFeelSetup setup = new NimbusLookAndFeelSetup();

		setup.setupLookAndFeel();

		window.setIconImage(Icons.APPLICATION_32.getImage());
	}
}
