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
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
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
			laf.getDefaults().put("MenuBar:Menu[Selected].backgroundPainter", new MenuBackgroundColor());
			laf.getDefaults().put("MenuItem[MouseOver].backgroundPainter", new MenuItemBackgroundColor());
			laf.getDefaults().put("MenuBar:Menu.contentMargins", new Insets (1, 5, 2, 10));
			laf.getDefaults().put("MenuItem.contentMargins", new Insets(1, 10, 2, 13));
			laf.getDefaults().put("MenuItem.textIconGap", 3);
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

	/*
	 * The following Class implementation and its paint method were taken from the following page:
	 * http://stackoverflow.com/questions/24379251/change-ui-lookup-for-progressbar-swing-in-nimbus-theme 
	 */
	private class MenuItemBackgroundColor implements Painter<JMenuItem>
	{
        private final Color light = new Color(190,190,190);
        private final Color dark = new Color(160,160,160);
        private final Color outline = new Color(110, 110, 110);
        private GradientPaint gradPaint;

        @Override
        public void paint(Graphics2D g, JMenuItem c, int w, int h)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gradPaint = new GradientPaint((w / 2.0f), 0, light, (w / 2.0f), (h / 2.0f), dark, true);
            g.setPaint(gradPaint);
            g.fillRect(2, 1, (w - 5), (h - 3));

            g.setColor(outline);
            g.drawRect(2, 1, (w - 5), (h - 3));

            Color trans = new Color(outline.getRed(), outline.getGreen(), outline.getBlue(), 100);
            g.setColor(trans);
            g.drawRect(1, 0, (w - 3), (h - 1));
        }
    }
}
