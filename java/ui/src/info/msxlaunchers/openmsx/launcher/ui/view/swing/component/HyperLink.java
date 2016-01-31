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
package info.msxlaunchers.openmsx.launcher.ui.view.swing.component;

import info.msxlaunchers.openmsx.common.Utils;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JLabel;

/**
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class HyperLink extends JLabel
{
	private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

	public HyperLink(final String label, final String address)
	{
		super("<HTML><FONT color=\"#000099\">" + label + "</FONT></HTML>");

		setCursor(HAND_CURSOR);

	    addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent me) {}
			
			@Override
			public void mousePressed(MouseEvent me) {}
			
			@Override
			public void mouseExited(MouseEvent me) {}
			
			@Override
			public void mouseEntered(MouseEvent me) {}
			
			@Override
			public void mouseClicked(MouseEvent me)
			{
				try
				{
					Utils.startBrowser( address );
				}
				catch( IOException ioe )
				{
					//what to do?
				}
			}
	    });
	}
}
