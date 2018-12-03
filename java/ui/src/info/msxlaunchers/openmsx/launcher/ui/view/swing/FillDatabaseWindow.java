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
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.ScannerPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.ProgressWindow;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * Fill Database Dialog class
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class FillDatabaseWindow extends JDialog implements ActionListener
{
	private final ScannerPresenter presenter;
	private final Language language;
	private final Map<String,String> messages;
	private final Set<String> databases;
	private final String currentDatabase;
	private final Set<String> machines;
	private final boolean rightToLeft;
	private final Component mainWindow;

	private JList<Object> directoriesList;
	private DefaultListModel<Object> directoriesListModel;
	private JButton addDirectoryButton;
	private JButton removeDirectoryButton;
	private JCheckBox searchSubdirectoriesCheckBox;
	private JRadioButton existingDatabaseRadioButton;
	private JComboBox<String> existingDatabasesComboBox;
	private JRadioButton newDatabaseRadioButton;
	private JTextField newDatabaseTextField;
	private JLabel addModeLabel;
	private JComboBox<String> addModesComboBox;
	private JComboBox<String> profileNamesComboBox;
	private JCheckBox backupDatabaseCheckBox;
	private JCheckBox searchROMCheckBox;
	private JCheckBox searchDiskCheckBox;
	private JCheckBox searchTapeCheckBox;
	private JCheckBox searchLaserdiscCheckBox;
	private JComboBox<String> machinesComboBox;
	private JButton okButton;
	private JButton cancelButton;

	private static File previousSelectedDirectory;

	public FillDatabaseWindow(ScannerPresenter presenter,
								Language language,
								Set<String> databases,
								String currentDatabase,
								Set<String> machines,
								boolean rightToLeft)
	{
		this.presenter = presenter;
		this.language = language;
		this.databases = databases;
		this.currentDatabase = currentDatabase;
		this.machines = machines;
		this.rightToLeft = rightToLeft;
		this.mainWindow = GlobalSwingContext.getIntance().getMainWindow();

		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
	}

	public void display()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("FILL_DATABASE"));
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
		JPanel directoriesPanel = new JPanel();
		directoriesPanel.setBorder(BorderFactory.createTitledBorder(messages.get("DIRECTORIES")));

		JPanel databaseOptionsPanel = new JPanel();
		databaseOptionsPanel.setBorder(BorderFactory.createTitledBorder(messages.get("DATABASE_OPTIONS")));
		
		JPanel mediumSearchPanel = new JPanel();
		mediumSearchPanel.setBorder(BorderFactory.createTitledBorder(messages.get("MEDIUM_SEARCH")));
		
		JPanel machinePanel = new JPanel();
		machinePanel.setBorder(BorderFactory.createTitledBorder(messages.get("MACHINE")));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(databaseOptionsPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
						.addComponent(machinePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
						.addComponent(directoriesPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
						.addComponent(mediumSearchPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(directoriesPanel, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(databaseOptionsPanel, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(mediumSearchPanel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(machinePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
		//Directories
		directoriesListModel = new DefaultListModel<>();
		directoriesList = new JList<>(directoriesListModel);
		addDirectoryButton = new JButton(messages.get("ADD"));
		addDirectoryButton.addActionListener(this);
		removeDirectoryButton = new JButton(messages.get("REMOVE"));
		removeDirectoryButton.addActionListener(this);
		
		searchSubdirectoriesCheckBox = new JCheckBox(messages.get("SEARCH_SUB_DIRECTORIES"));
		searchSubdirectoriesCheckBox.setSelected(true);
		GroupLayout gl_panel = new GroupLayout(directoriesPanel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(85)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(searchSubdirectoriesCheckBox)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(directoriesList, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(addDirectoryButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(removeDirectoryButton, GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))))
					.addContainerGap(28, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(3)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(directoriesList, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(addDirectoryButton)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(removeDirectoryButton)))
					.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
					.addComponent(searchSubdirectoriesCheckBox))
		);
		directoriesPanel.setLayout(gl_panel);

		//Database options
		addModeLabel = new JLabel(messages.get("ADD_MODE"));
		
		JLabel lblProfileNameIn = new JLabel(messages.get("PROFILE_NAME"));
		
		existingDatabaseRadioButton = new JRadioButton(messages.get("EXISTING"));
		existingDatabaseRadioButton.addActionListener(this);

		newDatabaseRadioButton = new JRadioButton(messages.get("NEW"));
		newDatabaseRadioButton.addActionListener(this);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(existingDatabaseRadioButton);
		buttonGroup.add(newDatabaseRadioButton);

		existingDatabasesComboBox = new JComboBox<>(Utils.getSortedCaseInsensitiveArray(databases));

		newDatabaseTextField = new JTextField();
		newDatabaseTextField.setColumns(10);

		String[] addModes = {messages.get("APPEND_TO_DATABASE"), messages.get("OVERWRITE_DATABASE")};
		addModesComboBox = new JComboBox<>(addModes);

		String[] profileNames = {messages.get("USE_FILENAME"), messages.get("USE_COMMON_NAME")};
		profileNamesComboBox = new JComboBox<>(profileNames);

		backupDatabaseCheckBox = new JCheckBox(messages.get("BACKUP_EXISTING_DATABASE"));

		GroupLayout gl_panel_2 = new GroupLayout(databaseOptionsPanel);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(92)
					.addComponent(existingDatabaseRadioButton)
					.addGap(65)
					.addComponent(existingDatabasesComboBox, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(125)
					.addComponent(addModeLabel)
					.addGap(15)
					.addComponent(addModesComboBox, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(127)
					.addComponent(backupDatabaseCheckBox))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(92)
					.addComponent(newDatabaseRadioButton)
					.addGap(81)
					.addComponent(newDatabaseTextField, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(92)
					.addComponent(lblProfileNameIn, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
					.addGap(34)
					.addComponent(profileNamesComboBox, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(11)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(existingDatabaseRadioButton)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(1)
							.addComponent(existingDatabasesComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(11)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(3)
							.addComponent(addModeLabel))
						.addComponent(addModesComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(6)
					.addComponent(backupDatabaseCheckBox)
					.addGap(11)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(newDatabaseRadioButton)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(1)
							.addComponent(newDatabaseTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(14)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(3)
							.addComponent(lblProfileNameIn))
						.addComponent(profileNamesComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		databaseOptionsPanel.setLayout(gl_panel_2);

		//Media search
		searchROMCheckBox = new JCheckBox(messages.get("ROM"), true);
		searchROMCheckBox.addActionListener(this);

		searchDiskCheckBox = new JCheckBox(messages.get("DISK"), true);
		searchDiskCheckBox.addActionListener(this);

		searchTapeCheckBox = new JCheckBox(messages.get("TAPE"), true);
		searchTapeCheckBox.addActionListener(this);

		searchLaserdiscCheckBox = new JCheckBox(messages.get("LASERDISC"), true);
		searchLaserdiscCheckBox.addActionListener(this);

		GroupLayout gl_panel_3 = new GroupLayout(mediumSearchPanel);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(87)
					.addComponent(searchROMCheckBox)
					.addGap(22)
					.addComponent(searchDiskCheckBox)
					.addGap(22)
					.addComponent(searchTapeCheckBox)
					.addGap(22)
					.addComponent(searchLaserdiscCheckBox)
					.addContainerGap(128, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap(15, Short.MAX_VALUE)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(searchROMCheckBox)
						.addComponent(searchDiskCheckBox)
						.addComponent(searchTapeCheckBox)
						.addComponent(searchLaserdiscCheckBox))
					.addContainerGap())
		);
		mediumSearchPanel.setLayout(gl_panel_3);

		//Machine selection
		machinesComboBox = new JComboBox<>(Utils.getSortedCaseInsensitiveArray(machines));
		GroupLayout gl_panel_4 = new GroupLayout(machinePanel);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(93)
					.addComponent(machinesComboBox, GroupLayout.PREFERRED_SIZE, 261, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(127, Short.MAX_VALUE))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap(5, Short.MAX_VALUE)
					.addComponent(machinesComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		machinePanel.setLayout(gl_panel_4);

		//Buttons
		JPanel buttonPanel = new JPanel();

		contentPanel.setLayout(gl_contentPanel);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));

		okButton = new JButton(messages.get("OK"));
		okButton.addActionListener(this);
		okButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		okButton.setEnabled(false);
		buttonPanel.add(okButton);

		cancelButton = new JButton(messages.get("CANCEL"));
		cancelButton.addActionListener(this);
		cancelButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonPanel.add(cancelButton);
		getRootPane().setDefaultButton(cancelButton);

		//decide which radio button is selected first
		if(databases.isEmpty())
		{
			newDatabaseRadioButton.setSelected(true);
			existingDatabaseRadioButton.setEnabled(false);
			addModeLabel.setEnabled(false);
			addModesComboBox.setEnabled(false);
			existingDatabasesComboBox.setEnabled(false);
			backupDatabaseCheckBox.setEnabled(false);
		}
		else
		{
			existingDatabaseRadioButton.setSelected(true);
			newDatabaseTextField.setEnabled(false);
			if(currentDatabase == null)
			{
				existingDatabasesComboBox.setSelectedItem(databases.iterator().next());
			}
			else
			{
				existingDatabasesComboBox.setSelectedItem(currentDatabase);
			}
		}

		if(rightToLeft)
		{
			directoriesPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			databaseOptionsPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			mediumSearchPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			machinePanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			searchSubdirectoriesCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			existingDatabaseRadioButton.setHorizontalTextPosition(SwingConstants.LEADING);
			newDatabaseRadioButton.setHorizontalTextPosition(SwingConstants.LEADING);
			backupDatabaseCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			searchROMCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			searchDiskCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			searchTapeCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			searchLaserdiscCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);

			//combo boxes
			DefaultListCellRenderer renderer = new DefaultListCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.RIGHT);
			addModesComboBox.setRenderer(renderer);
			profileNamesComboBox.setRenderer(renderer);
		}

		pack();
		setLocationRelativeTo(mainWindow);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == okButton)
		{
			startScan();
		}
		else if(source == cancelButton)
		{
			dispose();
		}
		else if(source == addDirectoryButton)
		{
		    JFileChooser chooser = new JFileChooser(previousSelectedDirectory); 
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);

		    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		    {
		    	File selectedFile = chooser.getSelectedFile();
		    	directoriesListModel.addElement(selectedFile);
		    	previousSelectedDirectory = selectedFile;
		    }
		    enableOrDisableOKButton();
		}
		else if(source == removeDirectoryButton)
		{
			int selectedIndex = directoriesList.getSelectedIndex();
			if(selectedIndex > -1)
			{
				directoriesListModel.remove(selectedIndex);
			}
		    enableOrDisableOKButton();
		}
		else if(source == existingDatabaseRadioButton)
		{
			addModeLabel.setEnabled(true);
			addModesComboBox.setEnabled(true);
			existingDatabasesComboBox.setEnabled(true);
			backupDatabaseCheckBox.setEnabled(true);
			newDatabaseTextField.setEnabled(false);
		}
		else if(source == newDatabaseRadioButton)
		{
			addModeLabel.setEnabled(false);
			addModesComboBox.setEnabled(false);
			existingDatabasesComboBox.setEnabled(false);
			backupDatabaseCheckBox.setEnabled(false);
			newDatabaseTextField.setEnabled(true);
		}
		else if(source == searchROMCheckBox || source.equals(searchDiskCheckBox) ||
				source == searchTapeCheckBox || source == searchLaserdiscCheckBox)
		{
			enableOrDisableOKButton();
		}
	}

	private String[] getAllPaths()
	{
		int size = directoriesListModel.getSize();
		String[] paths = new String[size];

		for(int index=0; index<size; index++)
		{
			paths[index] = directoriesListModel.getElementAt(index).toString();
		}

		return paths;
	}

	private String getDatabase()
	{
		if(existingDatabaseRadioButton.isSelected())
		{
			return existingDatabasesComboBox.getSelectedItem().toString();
		}
		else
		{
			return newDatabaseTextField.getText();
		}
	}

	private void enableOrDisableOKButton()
	{
		if(directoriesListModel.isEmpty() ||
				(
				!searchROMCheckBox.isSelected() &&
				!searchDiskCheckBox.isSelected() &&
				!searchTapeCheckBox.isSelected() &&
				!searchLaserdiscCheckBox.isSelected())
				)
		{
			okButton.setEnabled(false);
		}
		else
		{
			okButton.setEnabled(true);
		}
	}

	private void startScan()
	{
		FillDatabaseTask fillDatabaseTask = new FillDatabaseTask(new FillDatabaseTaskExecutorImpl(), mainWindow, messages, rightToLeft);

		ProgressWindow progressWindow = new ProgressWindow(fillDatabaseTask, language, rightToLeft, this);
		progressWindow.showProgress();

		//at this point progress window is finished
		if(!fillDatabaseTask.isError())
		{
			try
			{
				presenter.onViewUpdatedDatabase(getDatabase());
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(mainWindow, le, messages, rightToLeft);
			}

			dispose();

			MessageBoxUtil.showInformationMessageBox(mainWindow, messages.get("TOTAL_ADDED_TO_DATABASE") + ": " + fillDatabaseTask.getResult(),
					messages, rightToLeft);
		}
	}

	private class FillDatabaseTaskExecutorImpl implements FillDatabaseTaskExecutor
	{
		/* (non-Javadoc)
		 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.FillDatabaseTaskExecutor#execute()
		 */
		@Override
		public int execute() throws LauncherException
		{
			return presenter.onRequestFillDatabaseAction(getAllPaths(),
					searchSubdirectoriesCheckBox.isSelected(),
					getDatabase(),
					newDatabaseRadioButton.isSelected(),
					addModesComboBox.getSelectedIndex()==0,
					machinesComboBox.getSelectedItem().toString(),
					searchROMCheckBox.isSelected(),
					searchDiskCheckBox.isSelected(),
					searchTapeCheckBox.isSelected(),
					searchLaserdiscCheckBox.isSelected(),
					profileNamesComboBox.getSelectedIndex()==1,
					backupDatabaseCheckBox.isSelected());
		}

		/* (non-Javadoc)
		 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.FillDatabaseTaskExecutor#interrupt()
		 */
		@Override
		public void interrupt()
		{
			presenter.onRequestInterruptFillDatabaseProcess();
		}
	}
}
