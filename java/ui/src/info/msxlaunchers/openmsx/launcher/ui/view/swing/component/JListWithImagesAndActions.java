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
package info.msxlaunchers.openmsx.launcher.ui.view.swing.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import info.msxlaunchers.openmsx.common.Utils;

/**
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class JListWithImagesAndActions extends JList<Object>
{
	public final static String DOUBLE_CLICK_COMMAND = "doubleClickCommand";
	public final static String ENTER_KEY_COMMAND = "enterKeyCommand";
	public final static String DELETE_KEY_COMMAND = "deleteKeyCommand";
	public final static String INSERT_KEY_COMMAND = "insertKeyCommand";

	private final static Color BACKGROUND_COLOR = new Color(250, 250, 250);

	private int KEYBOARD_PRESS_DELAY = 800;
	private StringBuilder pressedKeysBuffer = new StringBuilder();
	private long pressTimeValue;

	private final DefaultListModel<Object> listModel;

	ActionListener actionListener;
 
	public JListWithImagesAndActions(DefaultListModel<Object> listModel)
	{
		super(listModel);

		this.listModel = listModel;

		setCellRenderer(new JListCellRenderer());
		setBackground(BACKGROUND_COLOR);

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent me)
			{
				if(actionListener == null)
				{
					return;
				}
				List<Object> ob = getSelectedValuesList();
				if(SwingUtilities.isLeftMouseButton(me))
				{
					if(ob.size() > 1)
					{
						return;
					}
					if(me.getClickCount() == 2)
					{
						if(ob.size() > 0)
						{
							actionListener.actionPerformed(new ActionEvent(this,
									ActionEvent.ACTION_PERFORMED,
									DOUBLE_CLICK_COMMAND));
						}
						me.consume();
					}
				}
			}
		});

		addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent ke)
			{
				if(actionListener == null || ke.isAltDown())
				{
					return;
				}
				List<Object> ob = getSelectedValuesList();
				if(ob.size() > 1 && ke.getKeyCode() != KeyEvent.VK_DELETE)
				{
					//return if multiple items were selected and a key other than Delete was pressed
					return;
				}
				if(ke.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(ob.size() > 0)
					{
						actionListener.actionPerformed(new ActionEvent(this,
								ActionEvent.ACTION_PERFORMED,
								ENTER_KEY_COMMAND));
					}
					ke.consume();
				} 
				else if(ke.getKeyCode() == KeyEvent.VK_DELETE)
				{
					if(ob.size() > 0)
					{
						actionListener.actionPerformed(new ActionEvent(this,
								ActionEvent.ACTION_PERFORMED,
								DELETE_KEY_COMMAND));
					}
					ke.consume();
				} 
				else if(ke.getKeyCode() == KeyEvent.VK_INSERT)
				{
					if(ob.size() > 0)
					{
						actionListener.actionPerformed(new ActionEvent(this,
								ActionEvent.ACTION_PERFORMED,
								INSERT_KEY_COMMAND));
					}
					ke.consume();
				} 
				else if(isPrintableChar(ke.getKeyChar()))
				{
					long currentTime = System.currentTimeMillis();
				    if(pressTimeValue + KEYBOARD_PRESS_DELAY < currentTime)
				    {
						//recreate string buffer if too much time has elapsed
				    	pressedKeysBuffer = new StringBuilder();
				    }

				    pressTimeValue = currentTime;
				    pressedKeysBuffer.append(Character.toLowerCase(ke.getKeyChar()));

				    int size = getListSize();
				    for(int index=0; index < size; index++)
				    {
				        String str = getElementAt(index).toLowerCase();
				        if(str.startsWith(pressedKeysBuffer.toString()))
				        {
				            setSelectedIndex(index);
				            ensureIndexIsVisible(index);
				            break;
				        }
				    }
					ke.consume();
				}
			}
		});
	}

	public void addElement(String name, String company, String year, long size, ImageIcon icon)
	{
		listModel.addElement(new TextIcon(name, company, year, size, icon));
	}

	public void clear()
	{
		listModel.clear();
	}

	public void remove(int index)
	{
		listModel.remove(index);
	}

	public void setSelectedValue(String name)
	{
		int index = listModel.indexOf(new TextIcon(name, null, null, 0, null));
		setSelectedIndex(index);
		SwingUtilities.invokeLater(() -> ensureIndexIsVisible(index));
		requestFocusInWindow();
	}

	public String[] getSelectedItems()
	{
		List<Object> selectionValues = getSelectedValuesList();
		int size = selectionValues.size();
		String[] selectedItems = new String[size];

		for(int ix=0; ix < size; ix++)
		{
			selectedItems[ix] = ((TextIcon)selectionValues.get(ix)).name;
		}

		return selectedItems;
	}

	public String getElementAt(int index)
	{
		String element;
		if(index < 0 || index >= listModel.getSize())
		{
			element = null;
		}
		else
		{
			element = ((TextIcon)listModel.elementAt(index)).name;
		}

		return element;
	}

	public int getListSize()
	{
		return listModel.getSize();
	}

	public void addActionListener(ActionListener actionListener)
	{
		this.actionListener = actionListener;
	}

	private static boolean isPrintableChar(char c)
	{
	    Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
	    return (!Character.isISOControl(c)) &&
	            c != KeyEvent.CHAR_UNDEFINED &&
	            block != null &&
	            block != Character.UnicodeBlock.SPECIALS;
	}

	private static class JListCellRenderer extends JPanel implements ListCellRenderer<Object>
	{
		private static final JLabel iconLabel = new JLabel();
		private static final JLabel nameLabel = new JLabel();
		private static final JLabel infoLabel = new JLabel();
		private static JPanel textPanel = new JPanel();
		private static final FlowLayout allLayout = new FlowLayout(FlowLayout.LEFT, 0, 2);
		private static final BorderLayout textLayout = new BorderLayout();
		private final static Color background = new Color(190, 220, 230);
		private final static Color infoColor = new Color(100, 100, 100);

		private static final String LABEL_DASH = " - ";
		private static final String LABEL_KB = " KB";
		private static final String LABEL_SPACE = " ";
		private final static Border LABEL_MARGIN = new EmptyBorder(0, 3, 0, 0);

		static
		{
			nameLabel.setFont(new Font(null, Font.PLAIN, 14));
			nameLabel.setBorder(LABEL_MARGIN);

			infoLabel.setFont(new Font(null, Font.PLAIN, 9));
			infoLabel.setBorder(LABEL_MARGIN);
			infoLabel.setForeground(infoColor);

			textPanel.setLayout(textLayout);
			textPanel.add(nameLabel, BorderLayout.NORTH);
			textPanel.add(infoLabel, BorderLayout.SOUTH);
		}

		public Component getListCellRendererComponent(
                    JList<? extends Object> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus)
		{
			removeAll();
			setLayout(allLayout);
			TextIcon textIcon = (TextIcon)value;

			if (isSelected)
			{
				setBackground(background);
				textPanel.setBackground(background);
			}
			else
			{
				setBackground(BACKGROUND_COLOR);
				textPanel.setBackground(BACKGROUND_COLOR);
			}

			iconLabel.setIcon(textIcon.icon);
			add(iconLabel);

			nameLabel.setText(textIcon.name);
			infoLabel.setText(getFormattedString(textIcon));
			add(textPanel, BorderLayout.CENTER);

			return this;
		}

		private String getFormattedString(TextIcon textIcon)
		{
			StringBuilder builder = new StringBuilder();

			if(Utils.isEmpty(textIcon.company) && Utils.isEmpty(textIcon.year))
			{
				//size 0 is for files with real size of 0, files that don't exist and scripts
				if(textIcon.size > 0.0)
				{
					builder.append(textIcon.size / 1024).append(LABEL_KB);
				}
			}
			else
			{
				builder.append(textIcon.company).append(LABEL_SPACE).append(textIcon.year).append(LABEL_DASH).append(textIcon.size / 1024).append(LABEL_KB);
			}

			return builder.toString();
		}
	}

	private class TextIcon
	{
		private final String name;
		private final String company;
		private final String year;
		private long size;
		private final ImageIcon icon;

		TextIcon(String name, String company, String year, long size, ImageIcon icon)
		{
			this.name = name;
			this.company = company;
			this.year = year;
			this.size = size;
			this.icon = icon;
		}

		@Override
		public boolean equals(Object obj)
		{
			//this is a simplified equals() method for a very specific case and that is setSelectedValue()
			//to locate an element in the list
			return ((TextIcon)obj).name.equals(name);
		}
	}
}
