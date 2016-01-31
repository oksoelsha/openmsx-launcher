/*
 * Copyright 2015 Sam Elsharif
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
import info.msxlaunchers.openmsx.launcher.ui.presenter.DraggedAndDroppedGamesPresenter;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.ProgressWindow;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * @since v1.5
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class AddDraggedAndDroppedGamesWindow extends JDialog implements ActionListener
{
	private final DraggedAndDroppedGamesPresenter presenter;
	private String currentDatabase;
	private final Language language;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;
	private final File[] files;
	private final Set<String> machines;

	private JList<Object> fileList;
	private DefaultListModel<Object> fileListModel;
	private JComboBox<String> profileNamesComboBox;
	private JCheckBox backupDatabase;
	private JComboBox<String> machinesComboBox;
	private JButton okButton;
	private JButton cancelButton;

	private final Dimension fileListDimension = new Dimension(350, 150);

	public AddDraggedAndDroppedGamesWindow(DraggedAndDroppedGamesPresenter presenter, String currentDatabase, Language language,
			boolean rightToLeft, File[] files, Set<String> machines)
	{
		this.presenter = presenter;
		this.currentDatabase = currentDatabase;
		this.language = language;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.parent = GlobalSwingContext.getIntance().getMainWindow();
		this.files = files;
		this.machines = machines;
	}

	public void displayScreen()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("ADD_GAMES"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		//files and directories
		JPanel filesPane = new JPanel();
		filesPane.setBorder(BorderFactory.createTitledBorder(messages.get("FILES_AND_DIRS")));

		fileListModel = new DefaultListModel<>();
		fileList = new JList<>(fileListModel);

		for(File file:files)
		{
			fileListModel.addElement(file.getAbsolutePath());
		}

		JScrollPane fileListScrollBar = new JScrollPane(fileList);
		fileListScrollBar.setPreferredSize(fileListDimension);

		filesPane.add(fileListScrollBar);

		contentPane.add(filesPane);

		//options
		JPanel optionsPane = new JPanel();
		optionsPane.setBorder(BorderFactory.createTitledBorder(messages.get("DATABASE_OPTIONS")));
		optionsPane.setLayout(new BoxLayout(optionsPane, BoxLayout.PAGE_AXIS));

		JPanel profileNamePane = new JPanel();
		JLabel lblProfileNameIn = new JLabel(messages.get("PROFILE_NAME"));
		profileNamePane.add(lblProfileNameIn);

		String profileNames[] = {messages.get("USE_FILENAME"), messages.get("GET_FROM_OPENMSX")};
		profileNamesComboBox = new JComboBox<String>(profileNames);
		profileNamePane.add(profileNamesComboBox);

		optionsPane.add(profileNamePane);

		JPanel backupDatabasePane = new JPanel();
		backupDatabase = new JCheckBox(messages.get("BACKUP_EXISTING_DATABASE"));
		backupDatabasePane.add(backupDatabase);

		optionsPane.add(backupDatabasePane);

		contentPane.add(optionsPane);

		//machine selection
		JPanel machinesPane = new JPanel();
		machinesPane.setBorder(BorderFactory.createTitledBorder(messages.get("MACHINE")));

		JLabel machinesLabel = new JLabel(messages.get("MACHINE"));
		machinesPane.add(machinesLabel);

		machinesComboBox = new JComboBox<String>(Utils.getSortedCaseInsensitiveArray(machines));
		machinesPane.add(machinesComboBox);

		contentPane.add(machinesPane);

		//buttons
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

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
			filesPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			filesPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			optionsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			profileNamePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			profileNamePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			backupDatabasePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			backupDatabasePane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			backupDatabase.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			machinesPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			machinesPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			buttonsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		else
		{
			filesPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			profileNamePane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			backupDatabasePane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			machinesPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		}

		pack();
        setLocationRelativeTo(parent);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();

		if(source == okButton)
		{
			addDraggedAndDroppedGames();
		}
		else if(source == cancelButton)
		{
			dispose();
		}
	}

	private void addDraggedAndDroppedGames()
	{
		FillDatabaseTask fillDatabaseTask = new FillDatabaseTask(new AddDraggedAndDroppedGamesExecutor(), parent, messages, rightToLeft);

		ProgressWindow progressWindow = new ProgressWindow(fillDatabaseTask, language, rightToLeft, this);
		progressWindow.showProgress();

		//at this point progress window is finished
		if(!fillDatabaseTask.isError())
		{
			try
			{
				presenter.onUpdateViewedDatabase(currentDatabase);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(parent, le, messages, rightToLeft);
			}

			dispose();

			MessageBoxUtil.showInformationMessageBox(parent, messages.get("TOTAL_ADDED_TO_DATABASE") + ": " + fillDatabaseTask.getResult(),
					messages, rightToLeft);
		}
	}

	private class AddDraggedAndDroppedGamesExecutor implements FillDatabaseTaskExecutor
	{
		/* (non-Javadoc)
		 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.FillDatabaseTaskExecutor#execute()
		 */
		@Override
		public int execute() throws LauncherException
		{
			return presenter.onRequestAddDraggedAndDroppedGamesAddAction(profileNamesComboBox.getSelectedIndex()==1,
					backupDatabase.isSelected(), machinesComboBox.getSelectedItem().toString());
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
