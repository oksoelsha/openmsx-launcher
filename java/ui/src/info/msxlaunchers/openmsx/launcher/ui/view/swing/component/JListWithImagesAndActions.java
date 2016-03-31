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

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

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

	private final static Border MARGIN = new EmptyBorder(0, 1, 1, 0);
	private final static Color BACKGROUND_COLOR = new Color(250,250,250);

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
				if(actionListener == null)
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

	public void addElement(String text, ImageIcon icon)
	{
		listModel.addElement(new TextIcon(text, icon));
	}

	public void clear()
	{
		listModel.clear();
	}

	public void remove(int index)
	{
		listModel.remove(index);
	}

	public void setSelectedValue(String text)
	{
		int index = listModel.indexOf(new TextIcon(text, null));
		setSelectedIndex(index);
		ensureIndexIsVisible(index);
		requestFocusInWindow();
	}

	public String[] getSelectedItems()
	{
		List<Object> selectionValues = getSelectedValuesList();
		int size = selectionValues.size();
		String[] selectedItems = new String[size];

		for(int ix=0; ix < size; ix++)
		{
			selectedItems[ix] = ((TextIcon)selectionValues.get(ix)).text;
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
			element = ((TextIcon)listModel.elementAt(index)).text;
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

	private static class JListCellRenderer extends JLabel implements ListCellRenderer<Object>
    {
		public JListCellRenderer()
		{
            setOpaque(true);
            setVerticalAlignment(CENTER);
			setFont(new Font(null, Font.PLAIN, 11));
		}

		public Component getListCellRendererComponent(
                    JList<? extends Object> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus)
    	{
			TextIcon textIcon = (TextIcon)value;

			setBorder(MARGIN);

		    if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
                setBackground(BACKGROUND_COLOR);
                setForeground(list.getForeground());
			}

	    	setText(textIcon.text);
	    	setIcon(textIcon.icon);

		    return this;
    	}
    }

	private class TextIcon
	{
		private final String text;
		private final ImageIcon icon;

		public TextIcon(String text, ImageIcon icon)
		{
			this.text = text;
			this.icon = icon;
		}

		public String getText()
		{
			return text;
		}

		@Override
		public boolean equals(Object obj)
		{
			//this is a simplified equals() method for a very specific case and that is setSelectedValue()
			//to locate an element in the list
			return ((TextIcon)obj).getText().equals(text);
		}
	}
}
