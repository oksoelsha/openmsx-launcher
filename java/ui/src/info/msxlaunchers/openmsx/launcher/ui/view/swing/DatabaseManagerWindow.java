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

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseAndBackups;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseInfo;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseManagerPresenter;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JTableButtonColumn;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Database Manager window class
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class DatabaseManagerWindow extends JDialog implements ActionListener
{
	private final DatabaseManagerPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component mainWindow;

	private final DatabaseManagerWindow thisWindow = this;

	private TableModel tableModel;
	private JButton deleteAllBackupsButton;
	private JButton okButton;
	private JLabel totalDatabasesValue;
	private JLabel totalGamesValue;
	private JLabel totalBackupsValue;

	public DatabaseManagerWindow(DatabaseManagerPresenter presenter,
			Language language,
			boolean rightToLeft)
	{
		this.presenter = presenter;
		this.rightToLeft = rightToLeft;

		this.mainWindow = GlobalSwingContext.getIntance().getMainWindow();
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
	}

	public void display(Set<DatabaseAndBackups> databasesAndBackups)
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("DATABASE_MANAGER"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		//databases panel
		JPanel databasesTablePane = new JPanel();
		databasesTablePane.setLayout(new BoxLayout(databasesTablePane, BoxLayout.Y_AXIS));
		databasesTablePane.setBorder(BorderFactory.createTitledBorder(messages.get("DATABASES")));

		JPanel tablePane = new JPanel();

		tableModel = new TableModel();
		JTable table = new JTable(tableModel);
		table.setRowSelectionAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(450, 82));
		table.getColumnModel().getColumn(0).setPreferredWidth(280);
		table.getColumnModel().getColumn(1).setPreferredWidth(70);
		table.getColumnModel().getColumn(2).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setPreferredWidth(10);
		table.getColumnModel().getColumn(4).setPreferredWidth(10);
		table.getColumnModel().getColumn(5).setPreferredWidth(10);

		Action backupAction = new AbstractAction()			
		{
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
				try
				{
					backupTableRow(Integer.parseInt(e.getActionCommand()));
				}
				catch(LauncherException le)
				{
					MessageBoxUtil.showErrorMessageBox(thisWindow, le, messages, rightToLeft);
				}
		    }
		};
		new JTableButtonColumn(table, backupAction, 3, Icons.BACKUP_SMALL.getImageIcon(), messages.get("MANAGE_BACKUPS"));

		Action editAction = new AbstractAction()			
		{
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
				try
				{
					editTableRow(Integer.parseInt(e.getActionCommand()));
				}
				catch(LauncherException le)
				{
					MessageBoxUtil.showErrorMessageBox(thisWindow, le, messages, rightToLeft);
				}
		    }
		};
		new JTableButtonColumn(table, editAction, 4, Icons.EDIT_SMALL.getImageIcon(), messages.get("RENAME"));

		Action deleteAction = new AbstractAction()			
		{
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
				try
				{
					removeRowFromTable(Integer.parseInt(e.getActionCommand()));
				}
				catch(LauncherException le)
				{
					MessageBoxUtil.showErrorMessageBox(thisWindow, le, messages, rightToLeft);
				}
		    }
		};
		new JTableButtonColumn(table, deleteAction, 5, Icons.DELETE_SMALL.getImageIcon(), messages.get("DELETE"));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		tablePane.add(scrollPane);
		databasesTablePane.add(tablePane);

		JPanel deleteBackupsButtonPane = new JPanel();

		deleteAllBackupsButton = new JButton(messages.get("DELETE_ALL_BACKUPS"));
		deleteAllBackupsButton.addActionListener(this);
		deleteBackupsButtonPane.add(deleteAllBackupsButton);

		databasesTablePane.add(deleteBackupsButtonPane);

		contentPane.add(databasesTablePane);

		databasesAndBackups.stream().forEach(db -> tableModel.addRow(new Object[] {db.getName(), db.getTotalGames(), db.getTotalBackups()}));

		//information panel
		JPanel informationPane = new JPanel();
		informationPane.setBorder(BorderFactory.createTitledBorder(messages.get("INFORMATION")));

		GridBagLayout gridBagLayout = new GridBagLayout();
		informationPane.setLayout(gridBagLayout);

		GridBagConstraints valueConstraints = new GridBagConstraints();
		valueConstraints.fill = GridBagConstraints.HORIZONTAL;
		valueConstraints.anchor = GridBagConstraints.NORTHWEST;
		valueConstraints.weightx = 1.0;
		valueConstraints.gridwidth = GridBagConstraints.REMAINDER;
		valueConstraints.insets = new Insets(2, 2, 2, 4);

		GridBagConstraints labelConstraints = (GridBagConstraints)valueConstraints.clone();
		labelConstraints.weightx = 0.0;
		labelConstraints.gridwidth = 1;
		labelConstraints.insets = new Insets(2, 14, 2, 4);

        totalDatabasesValue = new JLabel();
        totalGamesValue = new JLabel();
        totalBackupsValue = new JLabel();

        Set<String> databases = databasesAndBackups.stream().map(d -> d.getName()).collect(Collectors.toSet());
        DatabaseInfo databaseInfo = presenter.getDatabaseInfo(databases);
        addPropertyToDisplay(messages.get("TOTAL_DATABASES"), totalDatabasesValue, databaseInfo.getTotalDatabases(), gridBagLayout, labelConstraints, valueConstraints, informationPane);
        addPropertyToDisplay(messages.get("TOTAL_GAMES"), totalGamesValue, databaseInfo.getTotalGames(), gridBagLayout, labelConstraints, valueConstraints, informationPane);
        addPropertyToDisplay(messages.get("TOTAL_BACKUPS"), totalBackupsValue, databaseInfo.getTotalBackups(), gridBagLayout, labelConstraints, valueConstraints, informationPane);
        contentPane.add(informationPane);

        enableOrDisableDeleteAllBackupsButton(databaseInfo.getTotalBackups());

        //buttons pane
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		okButton = new JButton(messages.get("OK"));
		okButton.addActionListener(this);
		okButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(okButton);

		contentPane.add(buttonsPane);

		if(rightToLeft)
		{
			databasesTablePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
			tableRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
			table.getColumnModel().getColumn(0).setCellRenderer(tableRenderer);
			table.getColumnModel().getColumn(1).setCellRenderer(tableRenderer);
			table.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);
			((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.RIGHT);
			informationPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			deleteBackupsButtonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		}
		else
		{
			deleteBackupsButtonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		}

		pack();
		setLocationRelativeTo(mainWindow);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();

		if(source == okButton)
		{
			dispose();
		}
		else if(source == deleteAllBackupsButton)
		{
			deleteAllBackups();
		}
	}

	public void updateDatabaseAndBackupsTable(String database, int gamesTotal, int backupsTotal)
	{
    	boolean done = false;
    	int index = 0;
        while(!done || index < tableModel.getRowCount())
        {
        	if(((String)tableModel.getValueAt(index, 0)).equals(database))
        	{
        		tableModel.setValueAt(gamesTotal, index, 1);
        		tableModel.setValueAt(backupsTotal, index, 2);
        		done = true;
        	}
        	index++;
        }
	}

	public void updateDatabasesInfo(DatabaseInfo databaseInfo)
	{
        totalDatabasesValue.setText("" + databaseInfo.getTotalDatabases());
        totalGamesValue.setText("" + databaseInfo.getTotalGames());
        totalBackupsValue.setText("" + databaseInfo.getTotalBackups());

        enableOrDisableDeleteAllBackupsButton(databaseInfo.getTotalBackups());
	}

	private void deleteAllBackups()
	{
		if(MessageBoxUtil.showYesNoMessageBox(this, messages.get("DELETE_ALL_BACKUPS_CONFIRMATION"), messages, rightToLeft) == 0)
		{
			try
			{
				presenter.onRequestDeleteAllBackups();
				presenter.updateDatabaseInfoView();
				for(int index = 0; index < tableModel.getRowCount(); index++)
				{
					if((int)tableModel.getValueAt(index, 2) > 0)
					{
						presenter.updateDatabaseAndBackupsView((String)tableModel.getValueAt(index, 0));
					}
				}
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(thisWindow, le, messages, rightToLeft);
			}
		}
	}

	private void enableOrDisableDeleteAllBackupsButton(int totalBackups)
	{
		deleteAllBackupsButton.setEnabled(totalBackups > 0);
	}

	private class TableModel extends DefaultTableModel
	{
		private final String[] columnNames = { messages.get("DATABASE"), messages.get("GAMES"), messages.get("BACKUPS"), "", "", "" };

		@Override
		public boolean isCellEditable(int row, int col)
		{
			//the last two columns - the edit and delete buttons - are editable
			return (col > 0);
		}

		@Override
		public int getColumnCount() { return columnNames.length; }

		@Override
	    public String getColumnName(int col) { return columnNames[col]; }
	}

	private void backupTableRow(int index) throws LauncherException
	{
		presenter.onRequestDatabaseBackupsScreen((String)tableModel.getValueAt(index, 0));
	}

	private void editTableRow(int index) throws LauncherException
	{
		String newName = presenter.onRequestRenameDatabaseScreen((String)tableModel.getValueAt(index, 0));

		if(newName != null)
		{
			int currentTotalGames = (int)tableModel.getValueAt(index, 1);
			int currentTotalBackups = (int)tableModel.getValueAt(index, 2);
			tableModel.removeRow(index);

			//insert the new row in the correct alphabetical order
			int insertIndex = 0;
			while(insertIndex < tableModel.getRowCount() && ((String)tableModel.getValueAt(insertIndex, 0)).compareToIgnoreCase(newName) < 0)
			{
				insertIndex++;
			}
			tableModel.insertRow(insertIndex, new Object[] {newName, currentTotalGames, currentTotalBackups});
		}
	}

	private void removeRowFromTable(int index) throws LauncherException
	{
		if(MessageBoxUtil.showYesNoMessageBox(this, messages.get("DELETE_CONFIRMATION"), messages, rightToLeft) == 0)
		{
			String database = (String)tableModel.getValueAt(index, 0);
			presenter.onRequestDeleteDatabase(database);
			presenter.updateDatabaseInfoView();
			tableModel.removeRow(index);
		}
	}

	private void addPropertyToDisplay(String attribute, JLabel valueLabel, int value, GridBagLayout layout, GridBagConstraints labelConstraints, GridBagConstraints valueConstraints, JPanel panel)
	{
		JLabel attributeLabel = new JLabel(attribute + ":");
        layout.setConstraints(attributeLabel, labelConstraints);
        panel.add(attributeLabel);

        valueLabel.setText("" + value);
        if(rightToLeft)
        {
        	attributeLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        	valueLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        layout.setConstraints(valueLabel, valueConstraints);
        panel.add(valueLabel);
	}
}