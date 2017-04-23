/*
 * Copyright 2014 Sam Elsharif
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
import info.msxlaunchers.openmsx.launcher.ui.presenter.BlueMSXLauncherDatabasesImporterPresenter;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @since v1.3
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class ImportBlueMSXLauncherDatabasesWindow extends JDialog implements ActionListener
{
	private final BlueMSXLauncherDatabasesImporterPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;
	private final Set<String> machines;

	private JTextField blueMSXLauncherDirectoryTextField;
	private JButton blueMSXLauncherDirectoryButton;
	private JList<Object> availableDatabasesList;
	private DefaultListModel<Object> availableDatabasesListModel;
	private JList<Object> selectedDatabasesList;
	private DefaultListModel<Object> selectedDatabasesListModel;
	private JButton rightArrowButton;
	private JButton leftArrowButton;
	private JComboBox<String> machinesComboBox;
	private JButton okButton;
	private JButton cancelButton;

	private final Dimension databaseListDimension = new Dimension(180, 150);

	public ImportBlueMSXLauncherDatabasesWindow(BlueMSXLauncherDatabasesImporterPresenter presenter, Language language, boolean rightToLeft, Set<String> machines)
	{
		this.presenter = presenter;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.parent = GlobalSwingContext.getIntance().getMainWindow();
		this.machines = machines;
	}

	public void displayScreen()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("IMPORT_BLUEMSXLAUNCHER_DATABASES"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		//blueMSX Launcher directory selection
		JPanel launcherDirectoryPane = new JPanel();
		launcherDirectoryPane.setBorder(BorderFactory.createTitledBorder("blueMSX Launcher"));

		JLabel bmlDirectory = new JLabel(messages.get("DIRECTORY"));
		launcherDirectoryPane.add(bmlDirectory);

		blueMSXLauncherDirectoryTextField = new JTextField(25);
		blueMSXLauncherDirectoryTextField.setEditable(false);
		launcherDirectoryPane.add(blueMSXLauncherDirectoryTextField);

		blueMSXLauncherDirectoryButton = new JButton(Icons.FOLDER.getImageIcon());
		blueMSXLauncherDirectoryButton.setPreferredSize(WindowUtils.iconButtonDimension);
		blueMSXLauncherDirectoryButton.addActionListener(this);
		blueMSXLauncherDirectoryButton.setToolTipText(messages.get("BROWSE"));
		launcherDirectoryPane.add(blueMSXLauncherDirectoryButton);

		contentPane.add(launcherDirectoryPane);

		//available and selected database lists
		JPanel databasesSelectionPane = new JPanel();
		databasesSelectionPane.setBorder(BorderFactory.createTitledBorder(messages.get("DATABASES")));

		JPanel availableDatabasesPane = new JPanel();
		availableDatabasesPane.setLayout(new BoxLayout(availableDatabasesPane, BoxLayout.Y_AXIS));

		JLabel availableLabel = new JLabel(messages.get("AVAILABLE"));
		availableDatabasesPane.add(availableLabel);

		availableDatabasesListModel = new DefaultListModel<>();
		availableDatabasesList = new JList<>(availableDatabasesListModel);
	    ListSelectionListener availableDatabasesListListener = new ListSelectionListener()
	    {
			@Override
	    	public void valueChanged(ListSelectionEvent listSelectionEvent)
	    	{
	    		if(!listSelectionEvent.getValueIsAdjusting())
	    		{
	    			int selection[] = availableDatabasesList.getSelectedIndices();
	    			if(selection.length == 1)
	    			{
						leftArrowButton.setEnabled(false);
						rightArrowButton.setEnabled(true);
		    			selectedDatabasesList.clearSelection();
		    			availableDatabasesList.setSelectedValue(availableDatabasesListModel.getElementAt(selection[0]), false);
	    			}
	    		}
	    	}
	    };
	    availableDatabasesList.addListSelectionListener(availableDatabasesListListener);

		JScrollPane availableDatabasesScrollBar = new JScrollPane(availableDatabasesList);
		availableDatabasesScrollBar.setPreferredSize(databaseListDimension);

		availableDatabasesPane.add(availableDatabasesScrollBar);
		databasesSelectionPane.add(availableDatabasesPane);

		JPanel arrowsPane = new JPanel();
		arrowsPane.setLayout(new BoxLayout(arrowsPane, BoxLayout.Y_AXIS));

		rightArrowButton = new JButton();
		rightArrowButton.addActionListener(this);
		rightArrowButton.setPreferredSize(WindowUtils.iconButtonDimension);
		rightArrowButton.setEnabled(false);
		arrowsPane.add(rightArrowButton);

		leftArrowButton = new JButton();
		leftArrowButton.addActionListener(this);
		leftArrowButton.setPreferredSize(WindowUtils.iconButtonDimension);
		leftArrowButton.setEnabled(false);
		arrowsPane.add(leftArrowButton);

		databasesSelectionPane.add(arrowsPane);

		JPanel selectedDatabasesPane = new JPanel();
		selectedDatabasesPane.setLayout(new BoxLayout(selectedDatabasesPane, BoxLayout.Y_AXIS));

		JLabel selectedLabel = new JLabel(messages.get("SELECTED"));
		selectedDatabasesPane.add(selectedLabel);

		selectedDatabasesListModel = new DefaultListModel<>();
		selectedDatabasesList = new JList<>(selectedDatabasesListModel);
	    ListSelectionListener selectedDatabasesListListener = new ListSelectionListener()
	    {
			@Override
	    	public void valueChanged(ListSelectionEvent listSelectionEvent)
	    	{
	    		if(!listSelectionEvent.getValueIsAdjusting())
	    		{
	    			int selection[] = selectedDatabasesList.getSelectedIndices();
	    			if(selection.length == 1)
	    			{
						rightArrowButton.setEnabled(false);
						leftArrowButton.setEnabled(true);
		    			availableDatabasesList.clearSelection();
		    			selectedDatabasesList.setSelectedValue(selectedDatabasesListModel.getElementAt(selection[0]), false);
	    			}
	    		}
	    	}
	    };
	    selectedDatabasesList.addListSelectionListener(selectedDatabasesListListener);

		JScrollPane selectedDatabasesScrollBar = new JScrollPane(selectedDatabasesList);
		selectedDatabasesScrollBar.setPreferredSize(databaseListDimension);

		selectedDatabasesPane.add(selectedDatabasesScrollBar);
		databasesSelectionPane.add(selectedDatabasesPane);

		contentPane.add(databasesSelectionPane);

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
		okButton.setEnabled(false);
		buttonsPane.add(okButton);

		cancelButton = new JButton(messages.get("CANCEL"));
		cancelButton.addActionListener(this);
		cancelButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(cancelButton);

		contentPane.add(buttonsPane);

		if(rightToLeft)
		{
			launcherDirectoryPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			launcherDirectoryPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			databasesSelectionPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			databasesSelectionPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			rightArrowButton.setIcon(Icons.LEFT_ARROW.getImageIcon());
			leftArrowButton.setIcon(Icons.RIGHT_ARROW.getImageIcon());
			machinesPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			machinesPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			buttonsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		else
		{
			launcherDirectoryPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			databasesSelectionPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
			leftArrowButton.setIcon(Icons.LEFT_ARROW.getImageIcon());
			rightArrowButton.setIcon(Icons.RIGHT_ARROW.getImageIcon());
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
		if(source == blueMSXLauncherDirectoryButton)
		{
			JFileChooser chooser = new JFileChooser();
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				blueMSXLauncherDirectoryTextField.setText(chooser.getSelectedFile().getAbsolutePath());
				populateAvailableDatabasesList(chooser.getSelectedFile());
			}
		}
		else if(source == rightArrowButton)
		{
			moveFromOneListToAnother(availableDatabasesList, selectedDatabasesList);
			okButton.setEnabled(true);
		}
		else if(source == leftArrowButton)
		{
			moveFromOneListToAnother(selectedDatabasesList, availableDatabasesList);
			if(selectedDatabasesListModel.size() == 0)
			{
				okButton.setEnabled(false);
			}
		}
		else if(source == okButton)
		{
			processImportRequest();
		}
		else if(source == cancelButton)
		{
			dispose();
		}
	}

	private void moveFromOneListToAnother(JList<Object> source, JList<Object> target)
	{
		int selections[] = source.getSelectedIndices();
		DefaultListModel<Object> sourceModel = (DefaultListModel<Object>)source.getModel();
		DefaultListModel<Object> targetModel = (DefaultListModel<Object>)target.getModel();

		//delete from the Jlist - start with higher indices then go down
		//the reason for this is that deleting lower indices first will shift elements up so indices of later items will be different
        for(int index=selections.length-1; index >= 0; index--)
        {
        	targetModel.addElement(sourceModel.getElementAt(selections[index]));
        	sourceModel.remove(selections[index]);
        }
	}

	private void populateAvailableDatabasesList(File selectedDirectory)
	{
		Set<String> databases = presenter.onGetDatabasesInDirectory(selectedDirectory);

		availableDatabasesListModel.clear();
		selectedDatabasesListModel.clear();

		rightArrowButton.setEnabled(false);
		leftArrowButton.setEnabled(false);

		okButton.setEnabled(false);

		for(String database:databases)
		{
			availableDatabasesListModel.addElement(database);
		}
	}

	private void processImportRequest()
	{
		int selectedSize = selectedDatabasesListModel.getSize();
		String databaseNames[] = new String[selectedSize];
		for(int ix = 0; ix < selectedSize; ix++)
		{
			databaseNames[ix] = (String)selectedDatabasesListModel.getElementAt(ix);
		}

		try
		{
			int totalImported = presenter.onRequestImportBlueMSXLauncherDatabasesAction(blueMSXLauncherDirectoryTextField.getText(), databaseNames, (String)machinesComboBox.getSelectedItem());

			dispose();

			MessageBoxUtil.showInformationMessageBox(parent, messages.get("TOTAL_IMPORTED_DATABASES") + ": " + totalImported, messages, rightToLeft);
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(parent, le, messages, rightToLeft);
		}
	}
}
