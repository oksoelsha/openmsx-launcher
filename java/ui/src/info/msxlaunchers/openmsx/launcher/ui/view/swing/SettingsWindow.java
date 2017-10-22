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
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.SettingsPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JComboBoxWithImages;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.WindowConstants;

/**
 * Settings window class
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class SettingsWindow  extends JDialog implements ActionListener
{
	private final SettingsPresenter presenter;
	private final Settings settings;
	private final Set<String> databases;
	private final Map<String,String> messages;
	private final Language language;
	private final String languageCode;
	private final boolean rightToLeft;
	private final String suggestedOpenMSXPath;
	private final Component parent;

	private JTextField openMSXFullPathTextField;
	private JButton openMSXFullPathBrowseButton;
	private JButton openMSXFullPathDetectButton;
	private JTextField screenshotsFullPathTextField;
	private JButton screenshotsFullPathBrowseButton;
	private JComboBox<String> defaultDatabaseComboBox;
	private JComboBoxWithImages languageComboBox;
	private JCheckBox enableFeedServiceCheckBox;
	private JButton okButton;
	private JButton cancelButton;
	
	public SettingsWindow(SettingsPresenter presenter,
			Settings settings,
			Set<String> databases,
			Language language,
			String languageCode,
			boolean rightToLeft,
			String suggestedOpenMSXPath)
	{
		this.presenter = presenter;
		this.settings = settings;
		this.databases = databases;
		this.parent = GlobalSwingContext.getIntance().getMainWindow();

		messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.language = language;
		this.languageCode = languageCode;
		this.rightToLeft = rightToLeft;
		this.suggestedOpenMSXPath = suggestedOpenMSXPath;
	}

	public void display()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("SETTINGS"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JLabel openMSXFullPathLabel = new JLabel("openMSX");
		JLabel screenshotsFullPathLabel = new JLabel(messages.get("SCREENSHOTS"));
		JLabel defaultDatabaseLabel = new JLabel(messages.get("DATABASE"));
		JLabel languageLabel = new JLabel(messages.get("LANGUAGE"));
		if(rightToLeft)
		{
			openMSXFullPathLabel.setHorizontalAlignment(SwingConstants.LEADING);
			screenshotsFullPathLabel.setHorizontalAlignment(SwingConstants.LEADING);
			defaultDatabaseLabel.setHorizontalAlignment(SwingConstants.LEADING);
			languageLabel.setHorizontalAlignment(SwingConstants.LEADING);			
		}
		else
		{
			openMSXFullPathLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			screenshotsFullPathLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			defaultDatabaseLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			languageLabel.setHorizontalAlignment(SwingConstants.TRAILING);			
		}
		openMSXFullPathTextField = new JTextField(settings.getOpenMSXFullPath());
		openMSXFullPathTextField.setColumns(10);
		{
			openMSXFullPathBrowseButton = new JButton(Icons.FOLDER.getImageIcon());
			openMSXFullPathBrowseButton.addActionListener(this);
			openMSXFullPathBrowseButton.setToolTipText(messages.get("BROWSE"));

			openMSXFullPathDetectButton = new JButton(Icons.DETECT.getImageIcon());
			openMSXFullPathDetectButton.addActionListener(this);
			openMSXFullPathDetectButton.setToolTipText(messages.get("DETECT"));
		}
		screenshotsFullPathTextField = new JTextField(settings.getScreenshotsFullPath());
		screenshotsFullPathTextField.setColumns(10);
		{
			screenshotsFullPathBrowseButton = new JButton(Icons.FOLDER.getImageIcon());
			screenshotsFullPathBrowseButton.setToolTipText(messages.get("BROWSE"));
			screenshotsFullPathBrowseButton.addActionListener(this);
		}
		{
			defaultDatabaseComboBox = new JComboBox<String>(Utils.getSortedCaseInsensitiveArray(databases));
			defaultDatabaseComboBox.setSelectedItem(settings.getDefaultDatabase());
		}
		{
			Language[] languages = Language.values();
			List<LanguageAndIcon> languagesAndIcons = new ArrayList<>(languages.length + 1);

			for(int ix=0; ix < languages.length; ix++)
			{
				languagesAndIcons.add(new LanguageAndIcon(languages[ix].toString(),
						messages.get(languages[ix].toString()),
						Icons.valueOf("FLAG_" + languages[ix].getLocaleName()).getImageIcon()));
			}
			Collator collator = Collator.getInstance(Locale.forLanguageTag(language.getLocaleName()));
			languagesAndIcons = languagesAndIcons.stream().sorted((l1, l2) -> collator.compare(l1.language, l2.language)).collect(Collectors.toList());

			languagesAndIcons.add(0, new LanguageAndIcon(null, messages.get("SYSTEM_DEFAULT"), null));
			languageComboBox = new JComboBoxWithImages(languagesAndIcons.stream().map(li -> li.languageCode).toArray(size -> new String[size]),
					languagesAndIcons.stream().map(li -> li.language).toArray(size -> new String[size]),
					languagesAndIcons.stream().map(li -> li.icon).toArray(size -> new ImageIcon[size]),
					rightToLeft);
			languageComboBox.setMaximumRowCount(languages.length+1);
			languageComboBox.setSelectedItem(messages.get(languageCode));
		}
		JPanel fullPathPanel = new JPanel();
		fullPathPanel.setBorder(BorderFactory.createTitledBorder(messages.get("DIRECTORIES")));
		JPanel generalPanel = new JPanel();
		generalPanel.setBorder(BorderFactory.createTitledBorder(messages.get("GENERAL")));
		if(rightToLeft)
		{
			fullPathPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			generalPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		JPanel groupPane = new JPanel();
		GroupLayout groupLayout = new GroupLayout(groupPane);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(130)
							.addComponent(openMSXFullPathTextField, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(410)
							.addComponent(false, openMSXFullPathBrowseButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(445)
							.addComponent(false, openMSXFullPathDetectButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addComponent(openMSXFullPathLabel, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
					.addGroup(groupLayout.createSequentialGroup()
							.addGap(130)
							.addComponent(screenshotsFullPathTextField, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(410)
							.addComponent(false, screenshotsFullPathBrowseButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addComponent(screenshotsFullPathLabel, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addComponent(fullPathPanel, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE))
						.addGap(20))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(languageLabel, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(defaultDatabaseComboBox, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(languageComboBox, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
						.addComponent(defaultDatabaseLabel, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addComponent(generalPanel, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
								.addGroup(groupLayout.createSequentialGroup()))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
									.addGap(29)
									.addComponent(openMSXFullPathTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(29)
									.addComponent(false, openMSXFullPathBrowseButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(29)
									.addComponent(false, openMSXFullPathDetectButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(32)
									.addComponent(openMSXFullPathLabel))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(72)
							.addComponent(screenshotsFullPathTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(72)
							.addComponent(false, screenshotsFullPathBrowseButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(75)
							.addComponent(screenshotsFullPathLabel))
						.addComponent(fullPathPanel, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
					.addGap(14)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(70)
							.addComponent(languageLabel))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(28)
							.addComponent(defaultDatabaseComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(14)
							.addComponent(languageComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(32)
							.addComponent(defaultDatabaseLabel))
						.addComponent(generalPanel, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)))
		);
		groupPane.setLayout(groupLayout);

		contentPane.add(groupPane);

		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		okButton = new JButton(messages.get("OK"));
		okButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		okButton.addActionListener(this);

		cancelButton = new JButton(messages.get("CANCEL"));
		cancelButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		cancelButton.addActionListener(this);

		buttonsPane.add(okButton);
		buttonsPane.add(cancelButton);
		contentPane.add(buttonsPane);

		if(rightToLeft)
		{
			groupPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			buttonsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		pack();
        setLocationRelativeTo(parent);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == okButton)
		{
			String defaultDatabase = null;
			if(defaultDatabaseComboBox.getSelectedItem() != null)
			{
				defaultDatabase = defaultDatabaseComboBox.getSelectedItem().toString();
			}
			try
			{
				presenter.onRequestSettingsAction(openMSXFullPathTextField.getText(),
						screenshotsFullPathTextField.getText(),
						defaultDatabase,
						languageComboBox.getSelectedCode(),
						settings.isShowUpdateAllDatabases(),
						enableFeedServiceCheckBox.isSelected());
				dispose();
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, rightToLeft);
			}
		}
		else if(source == cancelButton)
		{
			dispose();
		}
		else if(source == openMSXFullPathBrowseButton)
		{
			JFileChooser chooser = new JFileChooser();
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				openMSXFullPathTextField.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
		else if(source == openMSXFullPathDetectButton)
		{
			openMSXFullPathTextField.setText(suggestedOpenMSXPath);
		}
		else if(source == screenshotsFullPathBrowseButton)
		{
			JFileChooser chooser = new JFileChooser();
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				screenshotsFullPathTextField.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

	private class LanguageAndIcon
	{
		private final String languageCode;
		private final String language;
		private final ImageIcon icon;

		LanguageAndIcon(String languageCode, String language, ImageIcon icon)
		{
			this.languageCode = languageCode;
			this.language = language;
			this.icon = icon;
		}
	}
}
