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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
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
	private static final int TYPING_DELAY = 250;

	private final SearchFieldHandler searchFieldHandler;
	private final JPopupMenu parentMenu;

	private final JPanel fieldPanel;
	private final JTextField field;

	private final JWindow matchesPopup;
	private final JPanel matchesPanel;

	private final ActionListener timerListener = new TimerListener();
	private final Timer fieldUpdateTimer = new Timer(TYPING_DELAY, timerListener);

	private static final Border LABEL_MARGIN = BorderFactory.createEmptyBorder(3, 5, 3, 5);
	private static final int TEXT_FIELD_COLUMNS = 25;
	private static final int MATCHES_MENU_ITEM_HEIGHT = 18;

	private static final String ENTER_MAP_KEY = "Enter-key";
	private static final String DOWN_MAP_KEY = "Down-key";
	private static final String UP_MAP_KEY = "Up-key";

	private static final int UNSELECTED_INDEX = -1;
	private int currentMatchSelectionIndex = UNSELECTED_INDEX;
	private int currentMatchSelectionMaximum = 0;

	private JMatchLabel matchesLabels[];

	@SuppressWarnings("unchecked")
	private static final Painter<JComponent> LABEL_BACKGROUND_PAINTER = (Painter<JComponent>)UIManager.get("MenuItem[MouseOver].backgroundPainter");

	public JSearchTextFieldMenuItem(JPopupMenu parentMenu, SearchFieldHandler searchFieldHandler)
	{
		super();

		this.parentMenu = parentMenu;
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

		field.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), ENTER_MAP_KEY);
        field.getActionMap().put(ENTER_MAP_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
            	if(currentMatchSelectionIndex > UNSELECTED_INDEX)
            	{
            		processMatchSelection(matchesLabels[currentMatchSelectionIndex].getText());
            	}
            }
        });

		field.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), DOWN_MAP_KEY);
        field.getActionMap().put(DOWN_MAP_KEY, new AbstractAction() {
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

		field.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), UP_MAP_KEY);
        field.getActionMap().put(UP_MAP_KEY, new AbstractAction() {
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
			matchesPopup.setLocation(field.getLocationOnScreen().x, field.getLocationOnScreen().y + field.getHeight());
			matchesPanel.removeAll();

			int index = 0;
			matchesLabels = new JMatchLabel[matches.size()];
			for(String match:matches)
			{
				matchesLabels[index] = new JMatchLabel(match, index);
				matchesPanel.add(matchesLabels[index++]);
			}

			matchesPopup.setAlwaysOnTop(true);
			matchesPopup.setVisible(true);
		}

		currentMatchSelectionIndex = UNSELECTED_INDEX;
		currentMatchSelectionMaximum = matches.size();
	}

	private class JMatchLabel extends JLabel implements MouseListener
	{
		private final int indexInMatchesList;
		private boolean mouseInsideLabel;

		JMatchLabel(String label, int indexInMatchesList)
		{
			super(label);
			this.indexInMatchesList = indexInMatchesList;

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

		void highlight()
		{
			mouseInsideLabel = true;
			setForeground(Color.white);
			currentMatchSelectionIndex = indexInMatchesList;
			repaint();
		}

		void unhighlight()
		{
			mouseInsideLabel = false;
			setBackground(UIManager.getColor("MenuItem.background"));
			setForeground(UIManager.getColor("MenuItem.foreground"));
			repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			processMatchSelection(((JMatchLabel)e.getSource()).getText());
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

	private void processMatchSelection(String selection)
	{
		searchFieldHandler.handleSearchSelection(selection);
		parentMenu.setVisible(false);
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
