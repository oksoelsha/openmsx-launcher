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
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Label that shows two sections: a title (e.g. Database) and a value (name of database)
 * 
 * @since v1.7
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class JCompositeLabel extends JLabel
{
	private final int componentWidth;
	private final int divWidth;
	private final String valueTextAlign;
	private final int maxStringWidthInPixels;
	private final FontMetrics fontMetrics;

	private String currentTitle;
	private String currentValue;
	private String titleTextAlign;

	private static final Color BACKGROUND = new Color(187, 187, 187);
	private static final String ELLIPSES = "...";
	private static final String textAlignCenter = "center";
	private static final String textAlignLeft = "left";
	private static final String textAlignRight = "right";

	/**
	 * 
	 * @param componentWidth Width in pixels of the JLabel
	 * @param divWidth Width in pixels of the DIV in the HTML part
	 * @param valueCenterAligned If true then value text will be centered, otherwise left aligned
	 */
	public JCompositeLabel(int componentWidth, int divWidth, boolean valueCenterAligned)
	{
		this.componentWidth = componentWidth;
		this.divWidth = divWidth;
		this.valueTextAlign = valueCenterAligned ? textAlignCenter : textAlignLeft;
		this.maxStringWidthInPixels = componentWidth - 6;

		//set a default for the title text alignment
		this.titleTextAlign = textAlignLeft;

		Font font = new Font(null, Font.PLAIN, 10);
		setFont(font);
		this.fontMetrics = getFontMetrics(font);

		setOpaque(true);
		setBackground(BACKGROUND);
	}

	public void setTitle(String title)
	{
		this.currentTitle = title;
		setText(getHTMLFormattedText(title, currentValue));
	}

	public void setValue(String value)
	{
		this.currentValue = value;
		setText(getHTMLFormattedText(currentTitle, value));
	}

	@Override
	public void setComponentOrientation(ComponentOrientation orientation)
	{
		super.setComponentOrientation(orientation);

		if(orientation == ComponentOrientation.RIGHT_TO_LEFT)
		{
			titleTextAlign = textAlignRight;
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
		else
		{
			titleTextAlign = textAlignLeft;
			setHorizontalAlignment(SwingConstants.LEFT);
		}

		setText(getHTMLFormattedText(currentTitle, currentValue));
	}

	public int getComponentWidth()
	{
		return componentWidth;
	}

	private String getHTMLFormattedText(String title, String value)
	{
		return "<html><head><style>table, tr, td {border:1px solid #bbbbbb}</style></head>" +
				"<table cellspacing=\"0\" cellpadding=\"0\" style=\"width:" + divWidth + "px\"><tr><td style=\"background:#bbbbbb; text-align:" +
				titleTextAlign +
				"\">" +
				getDisplayString(title) +
				"</td></tr><tr><td style=\"background:#777777; color:white; padding:1px; white-space:nowrap; height:14px; text-align:" +
				valueTextAlign + "\">" +
				getDisplayString(value) +
				"</td></tr></table></html>";
	}

	private String getDisplayString(String string)
	{
		String stringToDisplay = null;

		if(string == null)
		{
			stringToDisplay = "";
		}
		else
		{
			if(fontMetrics.stringWidth(string) > maxStringWidthInPixels)
			{
				int index = string.length() - 1;
				stringToDisplay = string.substring(0, index) + ELLIPSES;
				while(fontMetrics.stringWidth(stringToDisplay) >= maxStringWidthInPixels)
				{
					stringToDisplay = string.substring(0, --index) + ELLIPSES;
				}
			}
			else
			{
				stringToDisplay = string;
			}
		}

		return stringToDisplay;
	}
}
