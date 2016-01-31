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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * @since v1.1
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class UserInputWindow<E> extends JDialog implements ActionListener
{
	private final Component parent;
	private final boolean rightToLeft;
	private final String title;
	private final String message;
	private final UserInputMethodComponent<E> userInputMethodComponent;

	private final JButton okButton;
	private final JButton cancelButton;

	private E userInput;

	public UserInputWindow(Component parent, Map<String,String> messages, boolean rightToLeft, String title,
			String message, UserInputMethodComponent<E> userInputMethodComponent)
	{
		this.parent = parent;
		this.rightToLeft = rightToLeft;
		this.title = title;
		this.message = message;
		this.userInputMethodComponent = userInputMethodComponent;

		okButton = new JButton(messages.get("OK"));
		cancelButton = new JButton(messages.get("CANCEL"));
	}

	public E displayAndGetUserInput()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(title);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setBounds(0, 0, 300, 150);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel messagePane = new JPanel();
			getContentPane().add(messagePane, BorderLayout.NORTH);
			if(rightToLeft)
			{
				messagePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 10));
			}
			else
			{
				messagePane.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 10));
			}
			{
				JLabel messageLabel = new JLabel(message);
				messagePane.add(messageLabel);
			}

			JPanel userInputPane = new JPanel();
			getContentPane().add(userInputPane, BorderLayout.CENTER);
			userInputPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
			{
				Component component = userInputMethodComponent.getMethodComponent();
				component.setPreferredSize(new Dimension(178, 28));
				userInputPane.add(component);
			}

			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				okButton.addActionListener(this);
				okButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
				buttonPane.add(okButton);
			}
			{
				cancelButton.addActionListener(this);
				cancelButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
				buttonPane.add(cancelButton);
			}
			if(rightToLeft)
			{
				buttonPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}
		}

        setLocationRelativeTo(parent);
		setVisible(true);

		return userInput;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == okButton)
		{
			userInput = userInputMethodComponent.getInput();
			dispose();
		}
		else if( source == cancelButton)
		{
			dispose();
		}
	}
}
