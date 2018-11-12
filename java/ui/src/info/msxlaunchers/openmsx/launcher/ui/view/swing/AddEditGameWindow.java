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

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.ProfileEditingPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JTextFieldDragDrop;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * Add/Edit game dialog class
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class AddEditGameWindow extends JDialog implements ActionListener
{
	private final ProfileEditingPresenter presenter;
	private final Map<String,String> messages;
	private final Set<String> machines;
	private final Set<String> extensions;
	private final boolean rightToLeft;
	private final Component parent;
	private final boolean editMode;

	private JTextField nameTextField;
	private JTextFieldDragDrop infoTextField;
	private JButton browseInfoButton;
	private JComboBox<String> machinesComboBox;
	private JCheckBox extensionCheckBox;
	private JTextFieldDragDrop romATextField;
	private JButton browseRomAButton;
	private JTextFieldDragDrop romBTextField;
	private JButton browseRomBButton;
	private JComboBox<String> extensionComboBox;
	private JTextFieldDragDrop diskATextField;
	private JButton browseDiskAButton;
	private JTextFieldDragDrop diskBTextField;
	private JButton browseDiskBButton;
	private JComboBox<String> fddModesComboBox;
	private JTextFieldDragDrop tapeTextField;
	private JButton browseTapeButton;
	private JTextFieldDragDrop harddiskTextField;
	private JButton browseHarddiskButton;
	private JTextFieldDragDrop laserdiscTextField;
	private JButton browseLaserdiscButton;
	private JTextFieldDragDrop scriptTextField;
	private JCheckBox scriptOverrideCheckBox;
	private JButton browseScriptButton;
	private JButton launchButton;
	private JButton saveButton;
	private JButton cancelButton;

	private String oldGameName;

	public AddEditGameWindow(ProfileEditingPresenter presenter,
								Language language,
								Set<String> machines,
								Set<String> extensions,
								boolean rightToLeft,
								boolean editMode)
	{
		this.presenter = presenter;
		this.machines = machines;
		this.extensions = extensions;
		this.rightToLeft = rightToLeft;
		this.parent = GlobalSwingContext.getIntance().getMainWindow();
		this.editMode = editMode;

		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
	}

	public void display(String name,
			String info,
			String machine,
			String romA,
			String romB,
			String extension,
			String diskA,
			String diskB,
			String tape,
			String harddisk,
			String laserdisc,
			String script,
			int fddModeCode,
			boolean isScriptOverride)
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		if(editMode)
		{
			setTitle(messages.get("EDIT_GAME"));
		}
		else
		{
			setTitle(messages.get("ADD_GAME"));
		}
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel topPart = new JPanel();
		JPanel buttonsPart = new JPanel();
		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPaneLayout.setHorizontalGroup(
				contentPaneLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(topPart, GroupLayout.PREFERRED_SIZE, 524, GroupLayout.PREFERRED_SIZE)
				.addComponent(buttonsPart, GroupLayout.PREFERRED_SIZE, 524, GroupLayout.PREFERRED_SIZE)
		);
		contentPaneLayout.setVerticalGroup(
				contentPaneLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addComponent(topPart, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(buttonsPart, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
		);
		contentPane.setLayout(contentPaneLayout);

		//add tabs
		JTabbedPane tabbedPane = new JTabbedPane();

		addGeneralTab(tabbedPane);
		addROMTab(tabbedPane);
		addDiskTab(tabbedPane);
		addTapeTab(tabbedPane);
		addHarddiskTab(tabbedPane);
		addLaserdiscTab(tabbedPane);
		addScriptTab(tabbedPane);

		topPart.add(tabbedPane);

		launchButton = new JButton(messages.get("LAUNCH"));
		launchButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		launchButton.addActionListener(this);

		saveButton = new JButton(messages.get("SAVE"));
		saveButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		saveButton.addActionListener(this);

		cancelButton = new JButton(messages.get("CANCEL"));
		cancelButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		cancelButton.addActionListener(this);

		buttonsPart.add(launchButton);
		buttonsPart.add(saveButton);
		buttonsPart.add(cancelButton);

		if(rightToLeft)
		{
			topPart.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			topPart.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			buttonsPart.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		if(editMode)
		{
			oldGameName = name;
			nameTextField.setText(name);
			infoTextField.setText(info);
			machinesComboBox.setSelectedItem(machine);
			romATextField.setText(romA);
			romBTextField.setText(romB);
			if(extension == null)
			{
				extensionComboBox.setEnabled(false);
			}
			else
			{
				extensionCheckBox.setSelected(true);
				extensionComboBox.setSelectedItem(extension);
			}
			diskATextField.setText(diskA);
			diskBTextField.setText(diskB);
			fddModesComboBox.setSelectedIndex(fddModeCode);
			tapeTextField.setText(tape);
			harddiskTextField.setText(harddisk);
			laserdiscTextField.setText(laserdisc);
			scriptTextField.setText(script);
			scriptOverrideCheckBox.setSelected(isScriptOverride);
		}
		else
		{
			extensionComboBox.setEnabled(false);
			scriptOverrideCheckBox.setSelected(true);
		}

		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	public void display()
	{
		//this should only be called in Add mode
		if(editMode)
		{
			throw new RuntimeException("Cannot call this in Edit mode");
		}

		display(null, null, null, null, null, null, null, null, null, null, null, null, 0, false);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == launchButton)
		{
			try
			{
				presenter.onRequestLaunchAction(machinesComboBox.getSelectedItem().toString(),
					romATextField.getText(),
					romBTextField.getText(),
					getExtenstionRomSelection(),
					diskATextField.getText(),
					diskBTextField.getText(),
					tapeTextField.getText(),
					harddiskTextField.getText(),
					laserdiscTextField.getText(),
					scriptTextField.getText(),
					fddModesComboBox.getSelectedIndex(),
					scriptOverrideCheckBox.isSelected());
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(parent, le, messages, rightToLeft);
			}
		}
		else if(source == saveButton)
		{
			try
			{
				if(editMode)
				{
					presenter.onRequestEditGameSaveAction(oldGameName,
							nameTextField.getText(),
							infoTextField.getText(),
							machinesComboBox.getSelectedItem().toString(),
							romATextField.getText(),
							romBTextField.getText(),
							getExtenstionRomSelection(),
							diskATextField.getText(),
							diskBTextField.getText(),
							tapeTextField.getText(),
							harddiskTextField.getText(),
							laserdiscTextField.getText(),
							scriptTextField.getText(),
							fddModesComboBox.getSelectedIndex(),
							scriptOverrideCheckBox.isSelected());
				}
				else
				{
					presenter.onRequestAddGameSaveAction(nameTextField.getText(),
							infoTextField.getText(),
							machinesComboBox.getSelectedItem().toString(),
							romATextField.getText(),
							romBTextField.getText(),
							getExtenstionRomSelection(),
							diskATextField.getText(),
							diskBTextField.getText(),
							tapeTextField.getText(),
							harddiskTextField.getText(),
							laserdiscTextField.getText(),
							scriptTextField.getText(),
							fddModesComboBox.getSelectedIndex(),
							scriptOverrideCheckBox.isSelected());
				}

				dispose();
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(parent, le, messages, rightToLeft);
			}
		}
		else if(source == cancelButton)
		{
			dispose();
		}
		else if(source == browseInfoButton)
		{
			WindowUtils.browseFile(this, infoTextField);
		}
		else if(source == browseRomAButton)
		{
			WindowUtils.browseFile(this, romATextField, messages.get("ROM"), FileTypeUtils.getROMExtensions(), true);
		}
		else if(source == browseRomBButton)
		{
			WindowUtils.browseFile(this, romBTextField, messages.get("ROM"), FileTypeUtils.getROMExtensions(), true);
		}
		else if(source == browseDiskAButton)
		{
			WindowUtils.browseFile(this, diskATextField, messages.get("DISK"), FileTypeUtils.getDiskExtensions(), true);
		}
		else if(source == browseDiskBButton)
		{
			WindowUtils.browseFile(this, diskBTextField, messages.get("DISK"), FileTypeUtils.getDiskExtensions(), true);
		}
		else if(source == browseTapeButton)
		{
			WindowUtils.browseFile(this, tapeTextField, messages.get("TAPE"), FileTypeUtils.getTapeExtensions(), true);
		}
		else if(source == browseHarddiskButton)
		{
			WindowUtils.browseFile(this, harddiskTextField, messages.get("HARDDISK"), FileTypeUtils.getHarddiskExtensions(), true);
		}
		else if(source == browseLaserdiscButton)
		{
			WindowUtils.browseFile(this, laserdiscTextField, messages.get("LASERDISC"), FileTypeUtils.getLaserdiscExtensions(), true);
		}
		else if(source == browseScriptButton)
		{
			WindowUtils.browseFile(this, scriptTextField);
		}
		else if(source == extensionCheckBox)
		{
			extensionComboBox.setEnabled(extensionCheckBox.isSelected());
		}
	}

	private String getExtenstionRomSelection()
	{
		return extensionCheckBox.isSelected() ? extensionComboBox.getSelectedItem().toString() : null;
	}

	private void addGeneralTab(JTabbedPane tabbedPane)
	{
		JPanel generalPanel = new JPanel(false);

		JLabel nameLabel = new JLabel(messages.get("NAME"));
		nameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		
		JLabel infoLabel = new JLabel(messages.get("INFO"));
		infoLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		infoTextField = new JTextFieldDragDrop();
		infoTextField.setColumns(10);
		browseInfoButton = new JButton(Icons.FOLDER.getImageIcon());
		browseInfoButton.setToolTipText(messages.get("BROWSE"));
		browseInfoButton.addActionListener(this);

		JLabel machineLabel = new JLabel(messages.get("MACHINE"));
		machineLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		machinesComboBox = new JComboBox<>(Utils.getSortedCaseInsensitiveArray(machines));

		GroupLayout groupLayout = new GroupLayout(generalPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(infoLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(infoTextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
							.addComponent(browseInfoButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(machineLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(machinesComboBox, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(nameLabel))
						.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(infoLabel))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addComponent(infoTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(browseInfoButton))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(machineLabel))
						.addComponent(machinesComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		generalPanel.setLayout(groupLayout);

		tabbedPane.addTab(messages.get("GENERAL"), generalPanel);
	}

	private void addROMTab(JTabbedPane tabbedPane)
	{
		JPanel romPanel = new JPanel(false);

		JLabel romALabel = new JLabel(messages.get("ROM_A"));
		romALabel.setHorizontalAlignment(SwingConstants.TRAILING);
		romATextField = new JTextFieldDragDrop();
		romATextField.setColumns(10);
		browseRomAButton = new JButton(Icons.FOLDER.getImageIcon());
		browseRomAButton.setToolTipText(messages.get("BROWSE"));
		browseRomAButton.addActionListener(this);

		JLabel romBLabel = new JLabel(messages.get("ROM_B"));
		romBLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		romBTextField = new JTextFieldDragDrop();
		romBTextField.setColumns(10);
		browseRomBButton = new JButton(Icons.FOLDER.getImageIcon());
		browseRomBButton.setToolTipText(messages.get("BROWSE"));
		browseRomBButton.addActionListener(this);

		extensionCheckBox = new JCheckBox(messages.get("EXTENSION"));
		extensionCheckBox.addActionListener(this);

		extensionComboBox = new JComboBox<>(Utils.getSortedCaseInsensitiveArray(extensions));

		GroupLayout groupLayout = new GroupLayout(romPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(romALabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
									.addGap(5)
									.addComponent(romATextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
									.addComponent(browseRomAButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(romBLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
									.addGap(5)
									.addComponent(romBTextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
									.addComponent(browseRomBButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(100)
									.addComponent(extensionCheckBox)
									.addGap(5)
									.addComponent(extensionComboBox, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE))))))
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
						.addGap(35)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(3)
								.addComponent(romALabel))
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(romATextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(browseRomAButton)))
						.addGap(18)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(4)
								.addComponent(romBLabel))
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(1)
								.addComponent(romBTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(browseRomBButton))
						.addGap(18)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(extensionCheckBox)
							.addComponent(extensionComboBox)))
		);
		romPanel.setLayout(groupLayout);

		tabbedPane.addTab(messages.get("ROM"), romPanel);
	}

	private void addDiskTab(JTabbedPane tabbedPane)
	{
		JPanel diskPanel = new JPanel(false);
		
		JLabel diskALabel = new JLabel(messages.get("DISK_A"));
		diskALabel.setHorizontalAlignment(SwingConstants.TRAILING);
		diskATextField = new JTextFieldDragDrop();
		diskATextField.setColumns(10);
		browseDiskAButton = new JButton(Icons.FOLDER.getImageIcon());
		browseDiskAButton.setToolTipText(messages.get("BROWSE"));
		browseDiskAButton.addActionListener(this);

		JLabel diskBLabel = new JLabel(messages.get("DISK_B"));
		diskBLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		diskBTextField = new JTextFieldDragDrop();
		diskBTextField.setColumns(10);
		browseDiskBButton = new JButton(Icons.FOLDER.getImageIcon());
		browseDiskBButton.setToolTipText(messages.get("BROWSE"));
		browseDiskBButton.addActionListener(this);

		JLabel fddModeLabel = new JLabel(messages.get("CONFIGURATION"));
		fddModeLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		String[] fddModesModes = Arrays.asList(FDDMode.values()).stream().map(f -> messages.get(f.toString())).toArray(String[]::new);
		fddModesComboBox = new JComboBox<>(fddModesModes);

		GroupLayout groupLayout = new GroupLayout(diskPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(diskALabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(diskATextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
							.addComponent(browseDiskAButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(diskBLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(diskBTextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
							.addComponent(browseDiskBButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(fddModeLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
						.addGap(5)
						.addComponent(fddModesComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(diskALabel))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(diskATextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(browseDiskAButton)))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(diskBLabel))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addComponent(diskBTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(browseDiskBButton))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
					.addComponent(fddModeLabel)
					.addComponent(fddModesComboBox)))
		);
		diskPanel.setLayout(groupLayout);

		tabbedPane.addTab(messages.get("DISK"), diskPanel);
	}

	private void addTapeTab(JTabbedPane tabbedPane)
	{
		JPanel tapePanel = new JPanel(false);

		JLabel tapeLabel = new JLabel(messages.get("TAPE"));
		tapeLabel.setHorizontalAlignment(SwingConstants.TRAILING);

		tapeTextField = new JTextFieldDragDrop();
		tapeTextField.setColumns(10);
		browseTapeButton = new JButton(Icons.FOLDER.getImageIcon());
		browseTapeButton.setToolTipText(messages.get("BROWSE"));
		browseTapeButton.addActionListener(this);

		GroupLayout groupLayout = new GroupLayout(tapePanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(tapeLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(tapeTextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
					.addComponent(browseTapeButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(tapeLabel))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(tapeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(browseTapeButton)))
					.addGap(95))
		);
		tapePanel.setLayout(groupLayout);

		tabbedPane.addTab(messages.get("TAPE"), tapePanel);
	}

	private void addHarddiskTab(JTabbedPane tabbedPane)
	{
		JPanel harddiskPanel = new JPanel(false);

		JLabel harddiskLabel = new JLabel(messages.get("HARDDISK"));
		harddiskLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		harddiskTextField = new JTextFieldDragDrop();
		harddiskTextField.setColumns(10);
		browseHarddiskButton = new JButton(Icons.FOLDER.getImageIcon());
		browseHarddiskButton.setToolTipText(messages.get("BROWSE"));
		browseHarddiskButton.addActionListener(this);

		GroupLayout groupLayout = new GroupLayout(harddiskPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(harddiskLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(harddiskTextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
					.addComponent(browseHarddiskButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(harddiskLabel))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(harddiskTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(browseHarddiskButton)))
					.addGap(95))
		);
		harddiskPanel.setLayout(groupLayout);

		tabbedPane.addTab(messages.get("HARDDISK"), harddiskPanel);
	}

	private void addLaserdiscTab(JTabbedPane tabbedPane)
	{
		JPanel laserdiscPanel = new JPanel(false);

		JLabel laserdiscLabel = new JLabel(messages.get("LASERDISC"));
		laserdiscLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		laserdiscTextField = new JTextFieldDragDrop();
		laserdiscTextField.setColumns(10);
		browseLaserdiscButton = new JButton(Icons.FOLDER.getImageIcon());
		browseLaserdiscButton.setToolTipText(messages.get("BROWSE"));
		browseLaserdiscButton.addActionListener(this);

		GroupLayout groupLayout = new GroupLayout(laserdiscPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(laserdiscLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(laserdiscTextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
					.addComponent(browseLaserdiscButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(laserdiscLabel))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(laserdiscTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(browseLaserdiscButton)))
					.addGap(95))
		);
		laserdiscPanel.setLayout(groupLayout);

		tabbedPane.addTab(messages.get("LASERDISC"), laserdiscPanel);
	}

	private void addScriptTab(JTabbedPane tabbedPane)
	{
		JPanel scriptPanel = new JPanel(false);

		JLabel scriptLabel = new JLabel(messages.get("SCRIPT"));
		scriptLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		scriptTextField = new JTextFieldDragDrop();
		scriptTextField.setColumns(10);
		browseScriptButton = new JButton(Icons.FOLDER.getImageIcon());
		browseScriptButton.setToolTipText(messages.get("BROWSE"));
		browseScriptButton.addActionListener(this);
		scriptOverrideCheckBox = new JCheckBox(messages.get("OVERRIDE_OTHER_ARGUMENTS"));

		GroupLayout groupLayout = new GroupLayout(scriptPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(scriptLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(scriptTextField, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
					.addComponent(browseScriptButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(85)
					.addComponent(scriptOverrideCheckBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(scriptLabel)
							.addGap(32)
							.addComponent(scriptOverrideCheckBox))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(scriptTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(browseScriptButton)))
					.addGap(95))
		);
		scriptPanel.setLayout(groupLayout);

		tabbedPane.addTab(messages.get("SCRIPT"), scriptPanel);
	}
}
