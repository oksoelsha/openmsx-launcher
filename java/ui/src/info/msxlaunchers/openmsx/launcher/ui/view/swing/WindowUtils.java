/*
 * Copyright 2017 Sam Elsharif
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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import info.msxlaunchers.openmsx.common.FileTypeUtils;

/**
 * Utilities class for common Window operations
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
public class WindowUtils
{
	public static final Dimension iconButtonDimension = new Dimension(35, 26);
	private static final String ELLIPSES = "...";

	/**
	 * 
	 * @param window
	 * @param field
	 */
	public static void browseFile(JDialog window, JTextField field)
	{
		browseFile(window, field, null, null, false);
	}

	/**
	 * 
	 * @param window
	 * @param field
	 * @param filterDescription
	 * @param extensions
	 * @param includeZipFiles
	 */
	public static void browseFile(JDialog window, JTextField field, String filterDescription, Set<String> extensions, boolean includeZipFiles)
	{
		JFileChooser fileChooser = new JFileChooser();

		Set<String> extensionsEffectiveValue = extensions;

		if(filterDescription != null)
		{
			if(includeZipFiles)
			{
				extensionsEffectiveValue = new HashSet<>(extensions);
				extensionsEffectiveValue.addAll(FileTypeUtils.getZIPExtensions());
			}
			fileChooser.addChoosableFileFilter(new FileFilterImpl(filterDescription, extensionsEffectiveValue));
		}

        if(fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION)
        {
        	field.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }		
	}

	/**
	 * 
	 * @param window
	 * @param field
	 */
	public static void browseForDirectory(JDialog window, JTextField field)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if(chooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION)
		{
			field.setText(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	/**
	 * Truncate string if its width is longer than given available width and add ellipsis, otherwise return same string
	 * 
	 * @param string String to truncate if longer than available width
	 * @param fontMetrics Font metrics to get width of given string based on font type and size
	 * @param availableWidth Available width to fit the string
	 * @return Truncated string ending with ellipsis if longer than available width, otherwise same string
	 */
	public static String truncateStringAndDisplayEllipsis(String string, FontMetrics fontMetrics, int availableWidth)
	{
		String stringToDisplay = null;

		if(string == null || fontMetrics == null)
		{
			stringToDisplay = "";
		}
		else
		{
			if(fontMetrics.stringWidth(string) > availableWidth)
			{
				int index = string.length() - 1;
				stringToDisplay = string.substring(0, index) + ELLIPSES;
				while(fontMetrics.stringWidth(stringToDisplay) >= availableWidth)
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

	private static class FileFilterImpl extends FileFilter
	{
		private final String description;
		private final Set<String> extensions;

		FileFilterImpl(String description, Set<String> extensions)
		{
			this.description = description;
			this.extensions = extensions;
		}

		@Override
		public boolean accept(File file)
		{
			boolean accept;

			if(file.isDirectory())
			{
				accept = true;
			}
			else
			{
				String filename = file.getPath();
				String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

				accept = extensions.contains(extension);
			}

			return accept;
		}

		@Override
		public String getDescription()
		{
			return description;
		}
	}
}
