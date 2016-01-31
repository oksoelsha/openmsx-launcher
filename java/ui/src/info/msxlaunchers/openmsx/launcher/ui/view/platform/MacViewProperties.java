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

import info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Mac implementation of <code>PlatformViewProperties</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
final class MacViewProperties implements PlatformViewProperties
{
	private final MainPresenter mainPresenter;

	MacViewProperties(MainPresenter mainPresenter)
	{
		this.mainPresenter = Objects.requireNonNull( mainPresenter );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties#getSuggestedOpenMSXPath()
	 */
	@Override
	public String getSuggestedOpenMSXPath()
	{
		return "/Applications";
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties#isMachinesFolderInsideOpenMSX()
	 */
	@Override
	public boolean isMachinesFolderInsideOpenMSX()
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties#getOpenMSXMachinesPath()
	 */
	@Override
	public String getOpenMSXMachinesPath()
	{
		return "openmsx.app/share/machines";
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties#setDisplayProperties(javax.swing.JFrame)
	 */
	@Override
	public void setDisplayProperties(JFrame window)
	{
		//take the menu bar off the JFrame
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		//set the name of the application menu item
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "openMSX Launcher");

		//set the look and feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e)
		{
		    //should not happen
			throw new RuntimeException(e);
		}

		//define action for the Mac About in the application menu
	    try
	    {
	        Object app = Class.forName("com.apple.eawt.Application").getMethod("getApplication", (Class[]) null).invoke(null, (Object[]) null);

	        Object al = Proxy.newProxyInstance(Class.forName("com.apple.eawt.AboutHandler").getClassLoader(),
	        				new Class[] { Class.forName("com.apple.eawt.AboutHandler") },
	        				new AboutListener());
	        app.getClass().getMethod("setAboutHandler", new Class[] { Class.forName("com.apple.eawt.AboutHandler") }).invoke(app, new Object[] { al });
	    }
	    catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
	    {
	        throw new RuntimeException(e);
	    }

		window.setIconImage(Icons.APPLICATION_32.getImage());
	}

	private class AboutListener implements InvocationHandler
	{
	    public Object invoke(Object proxy, Method method, Object[] args)
	    {
	        //Show About Dialog
	    	mainPresenter.onRequestAboutScreen();

	        return null;
	    }
	}
}
