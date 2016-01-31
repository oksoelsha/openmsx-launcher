/*
 * Copyright 2015 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.ui.view.swing;

import java.awt.Component;
import java.util.Map;

import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.ProgressWindowTask;

/**
 * File scanning task
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
class FillDatabaseTask implements ProgressWindowTask<Integer>
{
	private int totalFound;
	private boolean exceptionThrown = false;

	private final FillDatabaseTaskExecutor taskExecutor;
	private final Component parentWindow;
	private final Map<String,String> messages;
	private final boolean rightToLeft;

	FillDatabaseTask(FillDatabaseTaskExecutor taskExecutor, Component parentWindow, Map<String,String> messages, boolean rightToLeft)
	{
		this.taskExecutor = taskExecutor;
		this.parentWindow = parentWindow;
		this.messages = messages;
		this.rightToLeft = rightToLeft;
	}
	
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.component.ProgressWindowTask#execute()
	 */
	@Override
	public void execute()
	{
		try
		{
			totalFound = taskExecutor.execute();
		}
		catch(LauncherException le)
		{
			totalFound = 0;
			exceptionThrown = true;

			MessageBoxUtil.showErrorMessageBox(parentWindow, le, messages, rightToLeft);
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.component.ProgressWindowTask#getResult()
	 */
	@Override
	public Integer getResult()
	{
		return totalFound;
	}		

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.component.ProgressWindowTask#interrupt()
	 */
	@Override
	public void interrupt()
	{
		taskExecutor.interrupt();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.component.ProgressWindowTask#isError()
	 */
	@Override
	public boolean isError()
	{
		return exceptionThrown;
	}
}
