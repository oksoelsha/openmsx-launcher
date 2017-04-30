/*
 * Copyright 2017 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.ui.view.swing.component;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JMenuItem;

/**
 * @since v1.9
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class JMenuItemWithIcon extends JMenuItem
{
	private static final EmptyIcon emptyIcon = new EmptyIcon();

	public JMenuItemWithIcon()
	{
		setIcon(emptyIcon);
	}

	private static class EmptyIcon implements Icon
	{
		private final int MENU_ITEM_ICON_SIZE = 16;

		@Override
		public int getIconHeight()
		{
			return MENU_ITEM_ICON_SIZE;
		}

		@Override
		public int getIconWidth()
		{
			return MENU_ITEM_ICON_SIZE;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			//do nothing
		}
	}
}
