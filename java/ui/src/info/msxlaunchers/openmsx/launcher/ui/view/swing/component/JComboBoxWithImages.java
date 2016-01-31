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
import java.awt.ComponentOrientation;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class JComboBoxWithImages extends JComboBox<String>
{
	private final String[] stringCodes;
	private final ImageIcon[] images;
	private final boolean rightToLeft;

	private final Border margin;

	private Map<String,Integer> codeToIndex = new HashMap<String,Integer>();

	public JComboBoxWithImages(String[] stringCodes,
			String[] localizedStrings,
			ImageIcon[] images,
			boolean rightToLeft)
	{
		 super(localizedStrings);

		 this.stringCodes = stringCodes;
		 this.images = images;

		 this.rightToLeft = rightToLeft;

		 if(rightToLeft)
		 {
			 margin = new EmptyBorder(2, 0, 2, 4);
		 }
		 else
		 {
			 margin = new EmptyBorder(2, 4, 2, 0);
		 }

		 for(int ix=0; ix < stringCodes.length; ix++)
		 {
			 codeToIndex.put(localizedStrings[ix], ix);
		 }

		 setRenderer(new ComboBoxRenderer());
	}

	public String getSelectedCode()
	{
		return stringCodes[getSelectedIndex()];
	}

	class ComboBoxRenderer extends JLabel implements ListCellRenderer<String>
    {
		public ComboBoxRenderer()
		{
            setOpaque(true);
            setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(
                    JList<? extends String> list,
                    String value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus)
    	{
			setBorder(margin);

			if(rightToLeft)
			{
				setHorizontalAlignment(SwingConstants.RIGHT);
				setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}

		    if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else
			{
                setBackground(Color.WHITE);
                setForeground(list.getForeground());
			}

		    if(!rightToLeft)
		    {
		    	setText(value);
		    }

		    if(index > -1)
			{
		    	setIcon(images[index]);
			}
			else
			{
				//index is not valid - this seems to be the case for when list is not expanded
				setIcon(images[codeToIndex.get(value)]);
			}

		    if(rightToLeft)
		    {
		    	setText(value);
		    }

		    return this;
    	}
    }
}
