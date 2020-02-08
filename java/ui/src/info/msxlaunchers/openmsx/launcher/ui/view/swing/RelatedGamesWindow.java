/*
 * Copyright 2020 Sam Elsharif
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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

/**
 * @since v1.13
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class RelatedGamesWindow extends JDialog implements ActionListener
{
	private final List<RelatedGame> relatedGames;
	private final Map<String,String> messages;
	private final Component mainWindow;

	public RelatedGamesWindow(List<RelatedGame> relatedGames, Language language)
	{
		this.relatedGames = relatedGames;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.mainWindow = GlobalSwingContext.getIntance().getMainWindow();
	}

	public void displayScreen()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("FIND_RELATED"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JLabel table = new JLabel();
		StringBuilder buffer = new StringBuilder();
		buffer.append("<html><table border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
		buffer.append("<tr>");
		buffer.append(getTableHeader(messages.get("NAME"), 300));
		buffer.append(getTableHeader(messages.get("COMPANY")));
		buffer.append(getTableHeader(""));
		buffer.append("</tr>");

		boolean alternateColor = false;
		for(RelatedGame relatedGame: relatedGames )
		{
			buffer.append("<tr" + (alternateColor?"":" bgcolor=\"#EEDDEE\"") + ">");
			buffer.append(getTableRow(relatedGame.getGameName()));
			buffer.append(getTableRow(relatedGame.getCompany()));
			buffer.append(getTableRow("" + relatedGame.getMSXGenId()));
			buffer.append("</tr>");
			alternateColor = !alternateColor;
		}

		buffer.append("</table></html>");

		table.setText(buffer.toString());
		contentPane.add(table);

		pack();
		setLocationRelativeTo(mainWindow);
		setVisible(true);
	}

	private String getTableHeader(String header)
	{
		return getTableHeader(header, 0);
	}

	private String getTableHeader(String header, int width)
	{
		return "<th text-align:left" + (width==0? "" :" width=" + width + "px") + ">" + header + "</th>";
	}

	private String getTableRow(String row)
	{
		return "<td>" + row + "</td>";
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
//		if(source == cancelButton)
		{
			dispose();
		}
	}
}
