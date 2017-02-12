/*
 * Copyright 2016 Sam Elsharif
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
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Activity Viewer dialog class
 * 
 * @since v1.8
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class ActivityViewerWindow extends JDialog implements ActionListener
{
	final Map<String,List<String[]>> logData;
	private final boolean rightToLeft;
	private final Component parent;
	private final Map<String,String> messages;

	private final int GAME_COL_WIDTH = 300;
	private final int DATABASE_COL_WIDTH = 220;
	private final int TIME_COL_WIDTH = 420;
	private final int COUNT_COL_WIDTH = 60;

	private JButton okButton;

	public ActivityViewerWindow(Map<String,List<String[]>> logData, Language language, boolean rightToLeft)
	{
		this.logData = logData;
		this.rightToLeft = rightToLeft;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.parent = GlobalSwingContext.getIntance().getMainWindow();
	}

	public void display()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("ACTIVITY_VIEWER"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel tablesPane = new JPanel();
		JTabbedPane tabbedPane = new JTabbedPane();
		tablesPane.add(tabbedPane);

		JPanel launchHistoryPanel = new JPanel();
		List<String[]> launchHistoryData = logData.get("ALL_PLAY_HISTORY");

		Object launchHistoryColumnNames[] = {messages.get("TIME"), messages.get("GAME"), messages.get("DATABASE")};
		Object launchHistoryRowData[][] = launchHistoryData.<String[]>toArray(new String[0][]);
		JTable launchHistoryTable = new JTable(launchHistoryRowData, launchHistoryColumnNames);
		launchHistoryTable.setRowSelectionAllowed(false);
		launchHistoryTable.setPreferredScrollableViewportSize(new Dimension(450, 180));
		launchHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(TIME_COL_WIDTH);
		launchHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(GAME_COL_WIDTH);
		launchHistoryTable.getColumnModel().getColumn(2).setPreferredWidth(DATABASE_COL_WIDTH);

		JScrollPane launchHistoryScrollPane = new JScrollPane(launchHistoryTable);
		launchHistoryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		launchHistoryPanel.add(launchHistoryScrollPane);
		tabbedPane.addTab(messages.get("HISTORY"), launchHistoryPanel);

		JPanel mostPlayedPanel = new JPanel();
		List<String[]> mostPlayedData = logData.get("MOST_PLAYED");

		Object mostPlayedColumnNames[] = {messages.get("GAME"), messages.get("DATABASE"), messages.get("COUNT")};
		Object mostPlayedRowData[][] = mostPlayedData.<String[]>toArray(new String[0][]);
		JTable mostPlayedTable = new JTable(mostPlayedRowData, mostPlayedColumnNames);
		mostPlayedTable.setRowSelectionAllowed(false);
		mostPlayedTable.setPreferredScrollableViewportSize(new Dimension(450, 180));
		mostPlayedTable.getColumnModel().getColumn(0).setPreferredWidth(GAME_COL_WIDTH);
		mostPlayedTable.getColumnModel().getColumn(1).setPreferredWidth(DATABASE_COL_WIDTH);
		mostPlayedTable.getColumnModel().getColumn(2).setPreferredWidth(COUNT_COL_WIDTH);

		JScrollPane mostPlayedScrollPane = new JScrollPane(mostPlayedTable);
		mostPlayedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		mostPlayedPanel.add(mostPlayedScrollPane);
		tabbedPane.addTab(messages.get("MOST_PLAYED"), mostPlayedPanel);

		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		okButton = new JButton(messages.get("OK"));
		okButton.addActionListener(this);
		okButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(okButton);

		contentPane.add(tablesPane);
		contentPane.add(buttonsPane);

		if(rightToLeft)
		{
			tabbedPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
			tableRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

			launchHistoryTable.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			launchHistoryTable.getColumnModel().getColumn(0).setCellRenderer(tableRenderer);
			launchHistoryTable.getColumnModel().getColumn(1).setCellRenderer(tableRenderer);
			launchHistoryTable.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);
			((DefaultTableCellRenderer)launchHistoryTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.RIGHT);

			mostPlayedTable.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			mostPlayedTable.getColumnModel().getColumn(0).setCellRenderer(tableRenderer);
			mostPlayedTable.getColumnModel().getColumn(1).setCellRenderer(tableRenderer);
			mostPlayedTable.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);
			((DefaultTableCellRenderer)mostPlayedTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.RIGHT);
		}

		pack();
        setLocationRelativeTo(parent);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == okButton)
		{
			dispose();
		}
	}
}
