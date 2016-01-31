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

import java.awt.Component;

import javax.swing.JComboBox;

/**
 * @since v1.1
 * @author Sam Elsharif
 *
 */
public class UserInputComboBoxSelector implements UserInputMethodComponent<String>
{
	private final JComboBox<String> comboBox;

	public UserInputComboBoxSelector(String[] comboBoxElements)
	{
		this.comboBox = new JComboBox<String>(comboBoxElements);
		this.comboBox.setMaximumRowCount(10);
	}

	@Override
	public Component getMethodComponent()
	{
		return comboBox;
	}

	@Override
	public String getInput()
	{
		return (String)comboBox.getSelectedItem();
	}

}
