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
package info.msxlaunchers.openmsx.launcher.ui.view.swing.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;

/**
 * JMenuItem implementation to use in the Search JPopupMenu
 * 
 * @since v1.6
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class JSearchTextField extends JTextField
{
	private static final int TYPING_DELAY = 150;

	private final SearchFieldHandler searchFieldHandler;
	private final JPopupMenu parentMenu;
	private final boolean gradientResultHighlight;

	private final JWindow matchesPopup;
	private final JPanel matchesPanel;

	private final ActionListener timerListener = new TimerListener();
	private final Timer fieldUpdateTimer = new Timer(TYPING_DELAY, timerListener);

	private static final int MATCHES_MENU_ITEM_HEIGHT = 42;

	private static final String ENTER_MAP_KEY = "Enter-key";
	private static final String DOWN_MAP_KEY = "Down-key";
	private static final String UP_MAP_KEY = "Up-key";

	private static final int UNSELECTED_INDEX = -1;
	private int currentMatchSelectionIndex = UNSELECTED_INDEX;
	private int currentMatchSelectionMaximum = 0;

	private JMatchPanel[] matchesLabels;

	private static final Border LABEL_MARGIN = BorderFactory.createEmptyBorder(7, 7, 7, 7);
	private static final Border LABEL_LINE = BorderFactory.createMatteBorder(0, 1, 0, 1, Color.gray);
	private static final Border LABEL_COMPOUND_BORDER = BorderFactory.createCompoundBorder(LABEL_LINE, LABEL_MARGIN);
	private static final Font gameFont = new Font(null, Font.PLAIN, 14);
	private static final Font databaseFont = new Font(null, Font.PLAIN, 9);
	private static final Color databaseColor = new Color(100, 100, 100);

	@SuppressWarnings("unchecked")
	private static final Painter<JComponent> LABEL_BACKGROUND_PAINTER = (Painter<JComponent>)UIManager.get("MenuItem[MouseOver].backgroundPainter");

	public JSearchTextField(int columns, JPopupMenu parentMenu, SearchFieldHandler searchFieldHandler, boolean gradientResultHighlight)
	{
		super(columns);

		this.parentMenu = parentMenu;
		this.searchFieldHandler = searchFieldHandler;
		this.gradientResultHighlight = gradientResultHighlight;

		this.matchesPopup = new JWindow();

		this.matchesPanel = new JPanel();
		matchesPanel.setLayout(new GridLayout(0, 1));
		this.matchesPopup.getContentPane().add(matchesPanel);

		getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				fieldUpdateTimer.restart();
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				fieldUpdateTimer.restart();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				fieldUpdateTimer.restart();
			}
		});

		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), ENTER_MAP_KEY);
		getActionMap().put(ENTER_MAP_KEY, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				if(currentMatchSelectionIndex > UNSELECTED_INDEX)
				{
					processMatchSelection(matchesLabels[currentMatchSelectionIndex].getSelectedItem());
				}
			}
		});

		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), DOWN_MAP_KEY);
		getActionMap().put(DOWN_MAP_KEY, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				if(currentMatchSelectionMaximum > 0)
				{
					if(currentMatchSelectionIndex != UNSELECTED_INDEX)
					{
						matchesLabels[currentMatchSelectionIndex].unhighlight();
					}
					currentMatchSelectionIndex++;
					if(currentMatchSelectionIndex == currentMatchSelectionMaximum)
					{
						currentMatchSelectionIndex = 0;
					}
					matchesLabels[currentMatchSelectionIndex].highlight();
				}
			}
		});

		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), UP_MAP_KEY);
		getActionMap().put(UP_MAP_KEY, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				if(currentMatchSelectionMaximum > 0)
				{
					if(currentMatchSelectionIndex != UNSELECTED_INDEX)
					{
						matchesLabels[currentMatchSelectionIndex].unhighlight();
					}
					currentMatchSelectionIndex--;
					if(currentMatchSelectionIndex < 0)
					{
						currentMatchSelectionIndex = currentMatchSelectionMaximum - 1;
					}
					matchesLabels[currentMatchSelectionIndex].highlight();
				}
			}
		});

		parentMenu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e)
			{
				SwingUtilities.invokeLater(() -> requestFocusInWindow());
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
			{
				matchesPopup.setVisible(false);
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e)
			{
				matchesPopup.setVisible(false);
			}
		});
	}

	private class JMatchPanel extends JPanel implements MouseListener
	{
		private final int indexInMatchesList;
		private boolean mouseInsideLabel;
		private final DatabaseItem databaseItem;

		JMatchPanel(DatabaseItem databaseItem, int indexInMatchesList)
		{
			this.databaseItem = databaseItem;
			this.indexInMatchesList = indexInMatchesList;

			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			addGameLabel();
			addDatabaseLabel();

			setOpaque(!gradientResultHighlight);
			setBackground(UIManager.getColor("MenuItem.background"));
			addMouseListener(this);
			setBorder(LABEL_COMPOUND_BORDER);
			mouseInsideLabel = false;
		}

		public DatabaseItem getSelectedItem()
		{
			return databaseItem;
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			if(gradientResultHighlight && mouseInsideLabel)
			{
				Painter<JComponent> painter = LABEL_BACKGROUND_PAINTER;
				painter.paint((Graphics2D)g, this, getWidth() + 1, getHeight());
			}

			super.paintComponent(g);
		}

		private void addGameLabel()
		{
			JLabel gameName = new JLabel(databaseItem.getGameName());
			gameName.setFont(gameFont);
			add(gameName);
		}

		private void addDatabaseLabel()
		{
			JLabel databaseName = new JLabel(databaseItem.getDatabase());
			databaseName.setFont(databaseFont);
			databaseName.setForeground(databaseColor);
			add(databaseName);
		}

		private void highlight()
		{
			mouseInsideLabel = true;
			if(gradientResultHighlight)
			{
				repaint();
			}
			else
			{
				setBackground(UIManager.getColor("MenuItem.selectionBackground"));
			}
			currentMatchSelectionIndex = indexInMatchesList;
		}

		private void unhighlight()
		{
			mouseInsideLabel = false;
			setBackground(UIManager.getColor("MenuItem.background"));
			setForeground(UIManager.getColor("MenuItem.foreground"));
			if(gradientResultHighlight)
			{
				repaint();
			}
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			processMatchSelection(((JMatchPanel)e.getSource()).getSelectedItem());
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			if(currentMatchSelectionIndex != UNSELECTED_INDEX)
			{
				matchesLabels[currentMatchSelectionIndex].unhighlight();
			}
			highlight();
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
		}
	}

	private void processMatchSelection(DatabaseItem selection)
	{
		searchFieldHandler.handleSearchSelection(selection);
		parentMenu.setVisible(false);
	}

	private class TimerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			Set<DatabaseItem> matches = searchFieldHandler.getSearchMatches(getText());

			showMatches(matches);

			((Timer)evt.getSource()).stop();
		}

		private void showMatches(Set<DatabaseItem> matches)
		{
			if(matches.isEmpty())
			{
				matchesPopup.setVisible(false);
			}
			else
			{
				matchesPopup.setSize(parentMenu.getWidth(), matches.size() * MATCHES_MENU_ITEM_HEIGHT);
				matchesPopup.setLocation(parentMenu.getLocationOnScreen().x, parentMenu.getLocationOnScreen().y + parentMenu.getHeight() - 1);
				matchesPanel.removeAll();

				int index = 0;
				matchesLabels = new JMatchPanel[matches.size()];
				for(DatabaseItem match:matches)
				{
					matchesLabels[index] = new JMatchPanel(match, index);
					matchesPanel.add(matchesLabels[index++]);
				}

				matchesPopup.setAlwaysOnTop(true);
				matchesPopup.setVisible(true);
			}

			currentMatchSelectionIndex = UNSELECTED_INDEX;
			currentMatchSelectionMaximum = matches.size();
		}
	}
}
