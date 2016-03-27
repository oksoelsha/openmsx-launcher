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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.Painter;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * JMenuItem implementation to use in the Search JPopupMenu
 * 
 * @since v1.6
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class JSearchTextFieldMenuItem extends JMenuItem
{
	private static final int TYPING_DELAY = 300;

	private final SearchFieldHandler searchFieldHandler;
	private final JPanel fieldPanel;
	private final JTextField field;

	private final JWindow matchesPopup;
	private final JPanel matchesPanel;

	private final ActionListener timerListener = new TimerListener();
	private final Timer fieldUpdateTimer = new Timer(TYPING_DELAY, timerListener);

	private static final Border LABEL_MARGIN = BorderFactory.createEmptyBorder(3, 3, 3, 3);
	private static final int TEXT_FIELD_COLUMNS = 25;
	private static final int MATCHES_MENU_ITEM_HEIGHT = 20;

	@SuppressWarnings("unchecked")
	private static final Painter<JComponent> LABEL_BACKGROUND_PAINTER = (Painter<JComponent>)UIManager.get("MenuItem[MouseOver].backgroundPainter");

	public JSearchTextFieldMenuItem(JPopupMenu parentMenu, SearchFieldHandler searchFieldHandler)
	{
		super();

		this.searchFieldHandler = searchFieldHandler;

		fieldPanel = new JPanel();
		field = new JTextField(TEXT_FIELD_COLUMNS);
		fieldPanel.add(field);
		add(fieldPanel);
		setBorder(null);

		this.matchesPopup = new JWindow();
		this.matchesPanel = new JPanel();
		matchesPanel.setLayout(new GridLayout(0, 1));
		this.matchesPopup.getContentPane().add(matchesPanel);

		field.getDocument().addDocumentListener(new DocumentListener()
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

		parentMenu.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e)
			{
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

	@Override
	public Dimension getPreferredSize()
	{
		return fieldPanel.getPreferredSize(); 
	}

	@Override
	public boolean requestFocusInWindow()
	{
		return field.requestFocusInWindow();
	}

	private void showMatches(Set<String> matches)
	{
		if(matches.size() == 0)
		{
			matchesPopup.setVisible(false);
		}
		else
		{
			matchesPopup.setSize(field.getWidth(), matches.size() * MATCHES_MENU_ITEM_HEIGHT);
			matchesPopup.setLocation(getLocationOnScreen().x + field.getLocation().x,
					getLocationOnScreen().y + field.getLocation().y + field.getHeight());
			matchesPanel.removeAll();
			matches.stream().forEach(m -> matchesPanel.add(new JMatchLabel(m)));
			matchesPopup.setVisible(true);
		}
	}

	private class JMatchLabel extends JLabel implements MouseListener
	{
		private boolean mouseInsideLabel;

		JMatchLabel(String label)
		{
			super(label);
			setOpaque(false);
			setContentAreaFilled(false);
			addMouseListener(this);
			setBorder(LABEL_MARGIN);
			mouseInsideLabel = false;
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			if(mouseInsideLabel)
			{
				Painter<JComponent> painter = LABEL_BACKGROUND_PAINTER;
				painter.paint((Graphics2D)g, this, getWidth() + 1, getHeight());
			}

			super.paintComponent(g);
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			searchFieldHandler.handleSearchSelection(((JMatchLabel)e.getSource()).getText());
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			mouseInsideLabel = true;
			setForeground(Color.white);
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			mouseInsideLabel = false;
			setBackground(UIManager.getColor("MenuItem.background"));
			setForeground(UIManager.getColor("MenuItem.foreground"));
			repaint();
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

	private class TimerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			Set<String> matches = searchFieldHandler.getSearchMatches(field.getText());

			showMatches(matches);

			((Timer)evt.getSource()).stop();
		}
	}
}
