/*
 * Copyright 2016 Sam Elsharif
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

/**
 * Abstract custom button with mouse effects custom look
 * 
 * @since v1.7
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractActionButton extends JComponent
{
	private final Color normalColor;
	private final Color hoverColor;
	private final Color pressedColor;

	private boolean inside = false;
	private boolean pressed = false;

	public AbstractActionButton(ActionListener listener, Color normalColor, Color hoverColor, Color pressedColor)
	{
		this.normalColor = normalColor;
		this.hoverColor = hoverColor;
		this.pressedColor = pressedColor;

		final AbstractActionButton actionButtonRef = this;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if(isEnabled()) {
					listener.actionPerformed(new ActionEvent(actionButtonRef, 0, null));
				}
			}
			@Override
			public void mousePressed(MouseEvent me)
			{
				pressed = true;
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent me)
			{
				pressed = false;
				repaint();
			}
			@Override
			public void mouseEntered(MouseEvent me) {
				inside = true;
				repaint();
			}
			@Override
			public void mouseExited(MouseEvent me) {
				inside = false;
				repaint();
			}
		});
	}

	@Override
	public void paint(Graphics g)
	{
		if(inside)
		{
			if(isEnabled())
			{
				if(pressed)
				{
					g.setColor(pressedColor);
				}
				else
				{
					g.setColor(hoverColor);
				}
			}
			else
			{
				g.setColor(normalColor);
			}
		}
		else
		{
			g.setColor(normalColor);
		}

		drawButton(g);
	}

	abstract protected void drawButton(Graphics g);
}
