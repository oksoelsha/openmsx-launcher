/*
 * Copyright 2019 Sam Elsharif
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

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * JLabel based class that transitions from enabled to disabled image and vice versa
 * It only works with Icons of type ImageIcon
 * 
 * @since v1.12
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class JLabelTransitionedEnabledDisabledImage extends JLabel
{
	private static final int DELTA_TIME = 10;
	private static final int ELAPSED_TIME = 200;
	private static final int MAX_COUNT = ELAPSED_TIME / DELTA_TIME;
	private static final float MAX_ALPHA = 1.0f;
	private static final float MIN_ALPHA = 0.2f;

	private Image image;
	private Timer timer;
	private float alpha;
	private boolean enabledFlag;

	public JLabelTransitionedEnabledDisabledImage(Icon icon)
	{
		super(icon);
		image = convertIconToImage(icon);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		if(enabled != this.enabledFlag)
		{
			this.enabledFlag = enabled;
	
			if(timer != null)
			{
				stopTransition();
			}
	
			timer = new Timer(DELTA_TIME, new TransitionListener());
			timer.start();
		}
		else
		{
			showFinalState();
		}
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(((AlphaComposite)g2.getComposite()).derive(alpha));
		g2.drawImage(image, 0, 0, this);
		g2.dispose();
	}

	private class TransitionListener implements ActionListener
	{
		private int counter = 0;

		@Override
		public void actionPerformed(ActionEvent ae)
		{
			if (counter >= MAX_COUNT)
			{
				stopTransition();
			}
			else
			{
				if(enabledFlag)
				{
					alpha = (float) counter / (float) MAX_COUNT * (MAX_ALPHA - MIN_ALPHA) + MIN_ALPHA;
				}
				else
				{
					alpha = ((float)MAX_COUNT - counter) / (float) MAX_COUNT * (MAX_ALPHA - MIN_ALPHA) + MIN_ALPHA;
				}

				repaint();
				counter++;
			}
		}
	}

	private Image convertIconToImage(Icon icon)
	{
		return ((ImageIcon)icon).getImage();
	}

	private void stopTransition()
	{
		timer.stop();
	}

	private void showFinalState()
	{
		if(enabledFlag)
		{
			alpha = MAX_ALPHA;
		}
		else
		{
			alpha = MIN_ALPHA;
		}
		repaint();
	}
}
