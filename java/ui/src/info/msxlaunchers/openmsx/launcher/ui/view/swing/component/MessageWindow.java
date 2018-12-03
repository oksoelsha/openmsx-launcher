/*
 * Copyright 2013 Sam Elsharif
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

import info.msxlaunchers.openmsx.launcher.ui.view.swing.MainWindow;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * @since v1.1
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class MessageWindow extends JDialog implements ActionListener
{
	private final Component mainWindow;
	private final String windowTitle;
	private final String message;
	private final int mode;
	private final String[] buttonLabels;
	private final boolean rightToLeft;

	private JButton[] buttons;
	private int result;

	public static final int ERROR = 0;
	public static final int INFORMATION = 1;
	public static final int WARNING = 2;
	public static final int QUESTION = 3;

	public MessageWindow(Component parent, String title, String message, int mode, String[] buttonLabels, ComponentOrientation orientation)
	{
		this(parent, title, message, mode, buttonLabels, orientation == ComponentOrientation.RIGHT_TO_LEFT);
	}

	public MessageWindow(Component parent, String title, String message, int mode, String[] buttonLabels, boolean rightToLeft)
	{
		this.mainWindow = parent;
		this.windowTitle = title;
		this.message = message;
		this.mode = mode;
		this.buttonLabels = buttonLabels;
		this.rightToLeft = rightToLeft;

		buttons = new JButton[buttonLabels.length];

		//when clicking the close button, return a generic -1, which should be interpreted as cancel
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				result = -1;
			}

			@Override
			public void windowClosing(WindowEvent e)
			{
				result = -1;
			}
		});
	}

	public int displayAndGetResult()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(windowTitle);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel messagePane = new JPanel();
		if(rightToLeft)
		{
			messagePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		}
		else
		{
			messagePane.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
		}
		contentPane.add(messagePane);

		JLabel messageLabel = new JLabel(message, getIconFromMode(mode), SwingConstants.TRAILING);
		if(rightToLeft)
		{
			messageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			messageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		}
		else
		{
			messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
			messageLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		}
		messagePane.add(messageLabel);

		JPanel buttonsPane = new JPanel();
		contentPane.add(buttonsPane);

		for(int counter = 0; counter < buttons.length; counter++)
		{
			buttons[counter] = new JButton(buttonLabels[counter]);
			buttons[counter].addActionListener(this);
			buttons[counter].setPreferredSize(MainWindow.BUTTON_DIMENSION);
			buttonsPane.add(buttons[counter]);
		}

		if(rightToLeft)
		{
			buttonsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		pack();
		setLocationRelativeTo(mainWindow);
		setVisible(true);

		return result;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		for(int counter = 0; counter < buttons.length; counter++)
		{
			if(source == buttons[counter])
			{
				result = counter;
				dispose();
				break;
			}
		}
	}

	private ImageIcon getIconFromMode(int mode)
	{
		ImageIcon icon = null;

		switch(mode)
		{
			case ERROR:
				icon = Icons.ERROR.getImageIcon();
				break;
			case INFORMATION:
				icon = Icons.INFORMATION.getImageIcon();
				break;
			case WARNING:
				//not needed for now
				icon = null;
				break;
			case QUESTION:
				icon = Icons.QUESTION.getImageIcon();
				break;
			default:
				throw new IllegalArgumentException("Mode " + mode + " is incorrent");
		}

		return icon;
	}
}
