/*
 * Copyright 2014 Sam Elsharif
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
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.Map;

import javax.swing.JLabel;

import info.msxlaunchers.openmsx.common.Utils;

/**
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class HyperLink extends JLabel
{
	private static final Color DEFAULT_LINK_COLOR = new Color(0, 0, 0x99);
	private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

	@SuppressWarnings("rawtypes")
	private Map attributes = getFont().getAttributes();

	private final boolean noUnderline;

	public static class HyperLinkParam
	{
		private String label;
		private String address;
		private Color linkColor;
		private boolean noUnderline;
		private boolean bold;
		private int size;

		public HyperLinkParam label(String label) { this.label = label; return this; }
		public HyperLinkParam address(String address) { this.address = address; return this; }
		public HyperLinkParam linkColor(Color linkColor) { this.linkColor = linkColor; return this; }
		public HyperLinkParam noUnderline() { this.noUnderline = true; return this; }
		public HyperLinkParam bold() { this.bold = true; return this; }
		public HyperLinkParam size(int size) { this.size = size; return this; }

		public HyperLink build()
		{
			return new HyperLink(this);
		}
	}

	public static HyperLinkParam label(String label) { return new HyperLinkParam().label(label); }
	public static HyperLinkParam address(String address) { return new HyperLinkParam().address(address); }
	public static HyperLinkParam linkColor(Color linkColor) { return new HyperLinkParam().linkColor(linkColor); }
	public static HyperLinkParam noUnderline() { return new HyperLinkParam().noUnderline(); }
	public static HyperLinkParam bold() { return new HyperLinkParam().noUnderline(); }
	public static HyperLinkParam size(int size) { return new HyperLinkParam().size(size); }

	@SuppressWarnings("unchecked")
	private HyperLink(HyperLinkParam param)
	{
		super(param.label);
		setCursor(HAND_CURSOR);

		if(param.bold)
		{
			attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
			setFont(getFont().deriveFont(attributes));
		}

		if(param.linkColor == null)
		{
			setForeground(DEFAULT_LINK_COLOR);
		}
		else
		{
			setForeground(param.linkColor);
		}

		this.noUnderline = param.noUnderline;

		if(param.size > 0)
		{
			attributes.put(TextAttribute.SIZE, param.size);
			setFont(getFont().deriveFont(attributes));
		}

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent me) {}

			@Override
			public void mousePressed(MouseEvent me) {}

			@Override
			public void mouseExited(MouseEvent me) {
				if(!noUnderline)
				{
					attributes.put(TextAttribute.UNDERLINE, -1);
					setFont(getFont().deriveFont(attributes));
				}
			}

			@Override
			public void mouseEntered(MouseEvent me) {
				if(!noUnderline)
				{
					attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					setFont(getFont().deriveFont(attributes));
				}
			}

			@Override
			public void mouseClicked(MouseEvent me)
			{
				try
				{
					Utils.startBrowser(param.address);
				}
				catch(IOException ioe)
				{
					//TODO log it
				}
			}
	    });
	}
}
