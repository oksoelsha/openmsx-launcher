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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * Class containing Nimbus related setup (used by Windows and Linux/BSD versions)
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class NimbusLookAndFeelSetup
{
	void setupLookAndFeel()
	{
		NimbusLookAndFeel laf = new NimbusLookAndFeel();
		try
		{
			UIManager.setLookAndFeel(laf);

			final Color background = new Color(240, 240, 240);

			laf.getDefaults().put("Panel.background", background);
			laf.getDefaults().put("MenuBar:Menu.contentMargins", new Insets (1, 5, 2, 10));
			laf.getDefaults().put("MenuBar:Menu[Selected].backgroundPainter", new MenuBackgroundColor());
			laf.getDefaults().put("Menu[Enabled+Selected].backgroundPainter", new MenuItemBackgroundColor());
			laf.getDefaults().put("Menu.contentMargins", new Insets(2, 7, 3, 13));
			laf.getDefaults().put("Menu[Enabled+Selected].textForeground", new Color(10, 10, 10));
			laf.getDefaults().put("MenuItem[MouseOver].backgroundPainter", new MenuItemBackgroundColor());
			laf.getDefaults().put("MenuItem.contentMargins", new Insets(2, 7, 3, 13));
			laf.getDefaults().put("MenuItem.textIconGap", 6);
			laf.getDefaults().put("MenuItem[MouseOver].textForeground", new Color(10, 10, 10));
			laf.getDefaults().put("MenuItem:MenuItemAccelerator[MouseOver].textForeground", new Color(10, 10, 10));
			laf.getDefaults().put("List[Selected].textBackground", new Color(190, 220, 230));
			laf.getDefaults().put("List[Selected].textForeground", new Color(30, 30, 30));
			laf.getDefaults().put("Viewport.background", Color.white);
			laf.getDefaults().put("Table.alternateRowColor", Color.white);
			laf.getDefaults().put("FileChooser.background", background);
			laf.getDefaults().put("FileChooser[Enabled].backgroundPainter",
					new Painter<JFileChooser>()
					{
						@Override
						public void paint(Graphics2D g, JFileChooser object, int width, int height)
						{
							g.setColor(background);
							g.draw(object.getBounds());
						}
					});
		}
		catch(UnsupportedLookAndFeelException e)
		{
			//should not happen
			throw new RuntimeException(e);
		}
	}

	private class MenuBackgroundColor implements Painter<JMenu>
	{
		private final Color color = new Color(160, 160, 160);
		@Override
		public void paint(Graphics2D g, JMenu c, int w, int h)
		{
			g.setColor(color);
			g.fillRect(0, 0, w, h);
		}
	}

	private static class MenuItemBackgroundColor implements Painter<JComponent>
	{
		private static final Color insideRight = new Color(195, 225, 235);
		private static final Color border = new Color(100, 140, 150);

		@Override
		public void paint(Graphics2D g, JComponent c, int w, int h)
		{
			g.setColor(insideRight);
			g.fillRect(0, 0, w - 1, h - 1);

			g.setColor(border);
			g.drawRect(0, 0, w - 1, h - 1);
		}
	}
}
