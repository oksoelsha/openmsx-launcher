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
package info.msxlaunchers.openmsx.launcher.ui.view.swing;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.common.version.Application;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout.Alignment;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * Help window class
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class HelpWindow extends JDialog implements ActionListener
{
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final String extraDataVersion;
	private final String screenshotsVersion;
	private final Component parent;

	private JButton okButton;

	public HelpWindow(Language language, boolean rightToLeft, String extraDataVersion, String screenshotsVersion, Component parent)
	{
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.extraDataVersion = extraDataVersion;
		this.screenshotsVersion = screenshotsVersion;
		this.parent = parent;
	}

	public void display()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("ABOUT"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		if(rightToLeft)
		{
			setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		JPanel contentPanel = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		okButton = new JButton(messages.get("OK"));
		okButton.addActionListener(this);

		JLabel versionLabel = new JLabel("openMSX Launcher " + Application.VERSION);

		JLabel developerLabel = new JLabel("Sam Elsharif " + Application.RELEASE_YEARS);

		JLabel launcherIcon = new JLabel(Icons.APPLICATION_64.getImageIcon());

		JTextArea aboutMessageTextField = new JTextArea("");
		if(!Utils.isEmpty(extraDataVersion))
		{
			aboutMessageTextField.append(getComponentVersionLabel( messages.get("EXTRA_DATA"), extraDataVersion));
			aboutMessageTextField.append("\n");
		}
		if(!Utils.isEmpty(screenshotsVersion))
		{
			aboutMessageTextField.append(getComponentVersionLabel(messages.get("SCREENSHOTS"), screenshotsVersion));
			aboutMessageTextField.append("\n");
		}
		if(!Utils.isEmpty(extraDataVersion) || !Utils.isEmpty(screenshotsVersion))
		{
			aboutMessageTextField.append("\n");
		}
		aboutMessageTextField.append("http://msxlaunchers.info/");
		aboutMessageTextField.append("\n\n");
		aboutMessageTextField.append("JRE Version " + System.getProperty("java.version"));
		aboutMessageTextField.append("\n\n");
		aboutMessageTextField.append(messages.get("MESSAGE"));
		aboutMessageTextField.append("\n\n");
		aboutMessageTextField.append(messages.get("CREDITS"));
		aboutMessageTextField.append("\n");
		aboutMessageTextField.append(" Afsaneh Tajvidi - Persian translation and graphics\n");
		aboutMessageTextField.append(" Eric Chen - Chinese translation\n");
		aboutMessageTextField.append(" Giuseve - Beta testing and suggestions\n");
		aboutMessageTextField.append(" rderooy - Suggesting and testing the disable-FDD feature\n");
		aboutMessageTextField.append("\n");
		aboutMessageTextField.append(messages.get("TRANSLATION_SOURCES"));
		aboutMessageTextField.append("\n");
		aboutMessageTextField.append(" Microsoft Language Portal\n");
		aboutMessageTextField.append(" Google Translate\n");
		aboutMessageTextField.append(" blueMSX Launcher\n");
		aboutMessageTextField.append("\n");
		aboutMessageTextField.append(messages.get("ICONS"));
		aboutMessageTextField.append("\n");
		aboutMessageTextField.append(" http://www.iconfinder.com/\n");
		aboutMessageTextField.append(" http://www.iconarchive.com/\n");

		aboutMessageTextField.setCaretPosition(0);
		aboutMessageTextField.setWrapStyleWord(true);
		aboutMessageTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		aboutMessageTextField.setBackground(SystemColor.control);
		aboutMessageTextField.setLineWrap(true);
		aboutMessageTextField.setEditable(false);
		aboutMessageTextField.setBorder(new EmptyBorder(0, 3, 0, 3));
		JScrollPane aboutMessageScrollPane = new JScrollPane(aboutMessageTextField);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);

		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPanel.createSequentialGroup()
						.addGap(10)
						.addComponent(launcherIcon, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
						.addGap(10)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(versionLabel)
							.addComponent(developerLabel))
						.addGap(100)
						.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_contentPanel.createSequentialGroup()
						.addGap(10)
						.addComponent(aboutMessageScrollPane, GroupLayout.PREFERRED_SIZE, 410, GroupLayout.PREFERRED_SIZE)
						.addGap(10))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(1)
							.addComponent(launcherIcon, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(13)
							.addComponent(versionLabel)
							.addGap(15)
							.addComponent(developerLabel))
					.addComponent(okButton))
					.addGap(15)
					.addComponent(aboutMessageScrollPane, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
		);
		contentPanel.setLayout(gl_contentPanel);

		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == okButton)
		{
			dispose();
		}
	}

	private String getComponentVersionLabel(String component, String version)
	{
		if(rightToLeft)
		{
			return version + " " + component;
		}
		else
		{
			return component + " " + version;
		}
	}
}
