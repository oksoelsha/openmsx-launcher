/*
 * Copyright 2017 Sam Elsharif
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
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.MachineUpdatePresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * @since v1.9
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class MachineUpdateWindow extends JDialog implements ActionListener
{
	private final MachineUpdatePresenter presenter;
	private final String database;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;
	private final Set<String> machines;

	private JRadioButton allDatabasesRadioButton;
	private JRadioButton currentDatabaseRadioButton;
	private JRadioButton changeAllMachinesRadioButton;
	private JComboBox<String> changeAllMachinesComboBox;
	private JRadioButton changeFromToMachineRadioButton;
	private JComboBox<String> changeFromMachineComboBox;
	private JComboBox<String> changeToMachineComboBox;
	private JCheckBox backupAffectedDatabasesCheckBox;
	private JButton okButton;
	private JButton cancelButton;

	public MachineUpdateWindow(MachineUpdatePresenter presenter, String database, Language language, boolean rightToLeft, Set<String> machines)
	{
		this.presenter = presenter;
		this.database = database;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.parent = GlobalSwingContext.getIntance().getMainWindow();
		this.machines = machines;
	}

	public void displayScreen()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("CHANGE_MACHINE"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		//databases
		JPanel databasePane = new JPanel();
		databasePane.setBorder(BorderFactory.createTitledBorder(messages.get("DATABASE")));
		databasePane.setLayout(new GridLayout(0, 1));

		currentDatabaseRadioButton = new JRadioButton(messages.get("UPDATE_CURRENT_DATABASE") + " [" + database + "]");
		currentDatabaseRadioButton.setSelected(true);
		currentDatabaseRadioButton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		databasePane.add(currentDatabaseRadioButton);
		allDatabasesRadioButton = new JRadioButton(messages.get("UPDATE_ALL_DATABASES"));
		allDatabasesRadioButton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		databasePane.add(allDatabasesRadioButton);

		ButtonGroup databaseButtonGroup = new ButtonGroup();
		databaseButtonGroup.add(allDatabasesRadioButton);
		databaseButtonGroup.add(currentDatabaseRadioButton);

		contentPane.add(databasePane);

		//machine selection
		JPanel machinesPane = new JPanel();
		machinesPane.setBorder(BorderFactory.createTitledBorder(messages.get("MACHINE")));
		GridBagLayout machinesLayout = new GridBagLayout();
		machinesPane.setLayout(machinesLayout);

		GridBagConstraints valueConstraints = new GridBagConstraints();
		valueConstraints.fill = GridBagConstraints.BOTH;
		valueConstraints.anchor = GridBagConstraints.WEST;
		valueConstraints.weightx = 1.0;
		valueConstraints.gridwidth = GridBagConstraints.REMAINDER;
		valueConstraints.insets = new Insets(2, 2, 2, 4);

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints = (GridBagConstraints)valueConstraints.clone();
		labelConstraints.weightx = 0.0;
		labelConstraints.gridwidth = 1;

		changeAllMachinesRadioButton = new JRadioButton(messages.get("CHANGE_ALL_TO"));
		changeAllMachinesRadioButton.setSelected(true);
		changeAllMachinesRadioButton.addActionListener(this);
		machinesLayout.setConstraints(changeAllMachinesRadioButton, labelConstraints);
		machinesPane.add(changeAllMachinesRadioButton);

		changeAllMachinesComboBox = new JComboBox<String>(Utils.getSortedCaseInsensitiveArray(machines));
		machinesLayout.setConstraints(changeAllMachinesComboBox, valueConstraints);
		machinesPane.add(changeAllMachinesComboBox);

		changeFromToMachineRadioButton = new JRadioButton(messages.get("CHANGE_FROM"));
		changeFromToMachineRadioButton.addActionListener(this);
		machinesLayout.setConstraints(changeFromToMachineRadioButton, labelConstraints);
		machinesPane.add(changeFromToMachineRadioButton);

		changeFromMachineComboBox = new JComboBox<String>(Utils.getSortedCaseInsensitiveArray(machines));
		changeFromMachineComboBox.setEnabled(false);
		machinesLayout.setConstraints(changeFromMachineComboBox, valueConstraints);
		machinesPane.add(changeFromMachineComboBox);

		JLabel toLabel = new JLabel(messages.get("TO"));
		toLabel.setBorder(new EmptyBorder(0, 22, 0, 0));
		machinesLayout.setConstraints(toLabel, labelConstraints);
		machinesPane.add(toLabel);

		changeToMachineComboBox = new JComboBox<String>(Utils.getSortedCaseInsensitiveArray(machines));
		changeToMachineComboBox.setEnabled(false);
		machinesLayout.setConstraints(changeToMachineComboBox, valueConstraints);
		machinesPane.add(changeToMachineComboBox);

		ButtonGroup machinesButtonGroup = new ButtonGroup();
		machinesButtonGroup.add(changeAllMachinesRadioButton);
		machinesButtonGroup.add(changeFromToMachineRadioButton);

		contentPane.add(machinesPane);

		JPanel backupPane = new JPanel();
		backupAffectedDatabasesCheckBox = new JCheckBox(messages.get("BACKUP_AFFECTED_DATABASES"));
		backupAffectedDatabasesCheckBox.setSelected(true);
		backupPane.add(backupAffectedDatabasesCheckBox);

		contentPane.add(backupPane);

		//buttons
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));

		okButton = new JButton(messages.get("OK"));
		okButton.addActionListener(this);
		okButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(okButton);

		cancelButton = new JButton(messages.get("CANCEL"));
		cancelButton.addActionListener(this);
		cancelButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(cancelButton);

		contentPane.add(buttonsPane);

		if(rightToLeft)
		{
			databasePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			currentDatabaseRadioButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			currentDatabaseRadioButton.setHorizontalTextPosition(SwingConstants.TRAILING);
			allDatabasesRadioButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			allDatabasesRadioButton.setHorizontalTextPosition(SwingConstants.TRAILING);
			machinesPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			changeAllMachinesRadioButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			changeAllMachinesRadioButton.setHorizontalTextPosition(SwingConstants.TRAILING);
			changeFromToMachineRadioButton.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			changeFromToMachineRadioButton.setHorizontalTextPosition(SwingConstants.TRAILING);
			backupPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 18, 12));
			backupAffectedDatabasesCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			buttonsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		else
		{
			backupPane.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 12));
		}

		pack();
        setLocationRelativeTo(parent);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == changeAllMachinesRadioButton)
		{
			changeAllMachinesComboBox.setEnabled(true);
			changeFromMachineComboBox.setEnabled(false);
			changeToMachineComboBox.setEnabled(false);
		}
		else if(source == changeFromToMachineRadioButton)
		{
			changeAllMachinesComboBox.setEnabled(false);
			changeFromMachineComboBox.setEnabled(true);
			changeToMachineComboBox.setEnabled(true);
		}
		else if(source == okButton)
		{
			processMachineUpdateRequest();
		}
		else if(source == cancelButton)
		{
			dispose();
		}
	}

	private void processMachineUpdateRequest()
	{
		try
		{
			int totalUpdated = presenter.onRequestMachineUpdateAction(getToMachine(), getFromMachine(), getDatabase(),
					backupAffectedDatabasesCheckBox.isSelected());

			dispose();
			
			MessageBoxUtil.showInformationMessageBox(parent, messages.get("TOTAL_UPDATED_PROFILES") + ": " + totalUpdated, messages, rightToLeft);
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(parent, le, messages, rightToLeft);
		}
	}

	private String getToMachine()
	{
		if(changeAllMachinesRadioButton.isSelected())
		{
			return changeAllMachinesComboBox.getSelectedItem().toString();
		}
		else
		{
			return changeToMachineComboBox.getSelectedItem().toString();
		}
	}

	private String getFromMachine()
	{
		if(changeAllMachinesRadioButton.isSelected())
		{
			return null;
		}
		else
		{
			return changeFromMachineComboBox.getSelectedItem().toString();
		}
	}

	private String getDatabase()
	{
		if(allDatabasesRadioButton.isSelected())
		{
			return null;
		}
		else
		{
			return database;
		}
	}
}
