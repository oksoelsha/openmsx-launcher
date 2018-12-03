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

import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.DatabaseBackupsPresenter;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JTableButtonColumn;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Database backups window class
 * 
 * @since v1.5
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class DatabaseBackupsWindow extends JDialog implements ActionListener
{
	private final DatabaseBackupsPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component mainWindow;
	private final Set<DatabaseBackup> backups;
	private final DateFormat dateFormat;

	private final DatabaseBackupsWindow thisWindow = this;

	private LabeledTableModel tableModel;
	private JButton backupNowButton;
	private JButton okButton;

	public DatabaseBackupsWindow(DatabaseBackupsPresenter presenter,
			Language language,
			boolean rightToLeft,
			Set<DatabaseBackup> backups)
	{
		this.presenter = presenter;
		this.rightToLeft = rightToLeft;

		this.mainWindow = GlobalSwingContext.getIntance().getMainWindow();
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.backups = backups;

		dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.forLanguageTag(language.getLocaleName().replace('_', '-')));
	}

	public void display()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("BACKUPS"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		//backups panel
		JPanel backupsTablePane = new JPanel();
		backupsTablePane.setLayout(new BoxLayout(backupsTablePane, BoxLayout.Y_AXIS));
		backupsTablePane.setBorder(BorderFactory.createTitledBorder(messages.get("BACKUPS")));

		JPanel tablePane = new JPanel();

		tableModel = new LabeledTableModel(backups.size());
		JTable table = new JTable(tableModel);
		table.setRowSelectionAllowed(false);
		table.setTableHeader(null);
		table.setPreferredScrollableViewportSize(new Dimension(350, 82));
		table.getColumnModel().getColumn(0).setPreferredWidth(330);
		table.getColumnModel().getColumn(1).setPreferredWidth(10);
		table.getColumnModel().getColumn(2).setPreferredWidth(10);

		Action backupAction = new AbstractAction()			
		{
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
				try
				{
					restoreTableRow(Integer.parseInt(e.getActionCommand()));
				}
				catch(LauncherException le)
				{
					MessageBoxUtil.showErrorMessageBox(thisWindow, le, messages, rightToLeft);
				}
		    }
		};
		new JTableButtonColumn(table, backupAction, 1, Icons.RESTORE_SMALL.getImageIcon(), messages.get("RESTORE"));

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
		new JTableButtonColumn(table, deleteAction, 2, Icons.DELETE_SMALL.getImageIcon(), messages.get("DELETE"));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		tablePane.add(scrollPane);
		backupsTablePane.add(tablePane);

		JPanel backupButtonPane = new JPanel();
		backupNowButton = new JButton(messages.get("BACKUP_NOW"));
		backupNowButton.addActionListener(this);
		backupButtonPane.add(backupNowButton);
		backupsTablePane.add(backupButtonPane);

		contentPane.add(backupsTablePane);

		backups.stream().forEach(db -> tableModel.addItem(db.getTimestamp(), dateFormat.format(db.getTimestamp())));

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
			backupsTablePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
			tableRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
			table.getColumnModel().getColumn(0).setCellRenderer(tableRenderer);
			backupButtonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		}
		else
		{
			backupButtonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
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
		else if(source == backupNowButton)
		{
			backupDatabaseNow();
		}
	}

	private void backupDatabaseNow()
	{
		try
		{
			DatabaseBackup databaseBackup = presenter.onRequestBackupDatabase();
			tableModel.addItem(databaseBackup.getTimestamp(), dateFormat.format(databaseBackup.getTimestamp()));
			presenter.updateDatabaseAndBackupsView(databaseBackup);
			presenter.updateDatabaseInfoView();
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(thisWindow, le, messages, rightToLeft);
		}
	}

	private class TableModel extends DefaultTableModel
	{
		private final String[] columnNames = { "", "", "" };

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

	private void restoreTableRow(int index) throws LauncherException
	{
		if(MessageBoxUtil.showYesNoMessageBox(this, messages.get("RESTORE_CONFIRMATION"), messages, rightToLeft) == 0)
		{
			Timestamp timestamp = tableModel.getTimestamp(index);
			presenter.onRequestRestoreBackup(timestamp);
			presenter.updateDatabaseAndBackupsView(timestamp);
			presenter.updateDatabaseInfoView();
			presenter.viewRestoredDatabase(timestamp);
			tableModel.removeRow(index);
		}
	}

	private void removeRowFromTable(int index) throws LauncherException
	{
		if(MessageBoxUtil.showYesNoMessageBox(this, messages.get("DELETE_CONFIRMATION"), messages, rightToLeft) == 0)
		{
			Timestamp timestamp = tableModel.getTimestamp(index);
			presenter.onRequestDeleteBackup(timestamp);
			presenter.updateDatabaseAndBackupsView(timestamp);
			presenter.updateDatabaseInfoView();
			tableModel.removeRow(index);
		}
	}

	private class LabeledTableModel extends TableModel
	{
		private final List<Timestamp> timestamps;

		LabeledTableModel(int size)
		{
			this.timestamps = new ArrayList<>(size);
		}

		void addItem(Timestamp timestamp, String localizedTimestamp)
		{
			addRow(new Object[] {localizedTimestamp});
			timestamps.add(timestamp);
		}

		@Override
		public void removeRow(int index)
		{
			super.removeRow(index);
			timestamps.remove(index);
		}

		Timestamp getTimestamp(int index)
		{
			return timestamps.get(index);
		}
	}
}