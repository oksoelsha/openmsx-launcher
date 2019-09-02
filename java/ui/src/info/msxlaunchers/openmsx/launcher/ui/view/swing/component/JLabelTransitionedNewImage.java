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
 * JLabel based class that transitions from old image to new
 * It only works with Icons of type ImageIcon
 * 
 * @since v1.12
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class JLabelTransitionedNewImage extends JLabel
{
	private static final int DELTA_TIME = 10;
	private static final int ELAPSED_TIME = 200;
	private static final int MAX_COUNT = ELAPSED_TIME / DELTA_TIME;

	private Image currentImage;
	private Icon newIcon;
	private Image newImage;
	private Timer timer;
	private float alpha1;
	private float alpha2;

	public JLabelTransitionedNewImage(Icon icon)
	{
		super(icon);
	}

	@Override
	public void setIcon(Icon icon)
	{
		if(currentImage == null)
		{
			super.setIcon(icon);
			currentImage = convertIconToImage(icon);
			return;
		}

		if(timer != null)
		{
			//a new image is set while the timer is still transitioning. In this case stop it before recreating
			stopTransition();
		}
		newIcon = icon;
		newImage = convertIconToImage(icon);
		alpha1 = 1.0f;
		alpha2 = 0.0f;
		timer = new Timer(DELTA_TIME, new TransitionListener());
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (currentImage == null || newImage == null)
		{
			return;
		}

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(((AlphaComposite)g2.getComposite()).derive(alpha1));
		g2.drawImage(currentImage, 0, 0, this);
		g2.setComposite(((AlphaComposite)g2.getComposite()).derive(alpha2));
		g2.drawImage(newImage, 0, 0, this);
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
				alpha1 = ((float)MAX_COUNT - counter) / (float) MAX_COUNT;
				alpha2 = (float) counter / (float) MAX_COUNT;

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
		currentImage = newImage;
		super.setIcon(newIcon);
	}
}
