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

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

/**
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class ProgressWindow extends JDialog implements ActionListener
{
	private final ProgressWindowTask<?> taskToExecute;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component mainWindow;

	private JButton cancelButton;

	public ProgressWindow(ProgressWindowTask<?> taskToExecute,
			Language language,
			boolean rightToLeft,
			Component parent)
    {
		this.taskToExecute = taskToExecute;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.mainWindow = parent;
    }

	public void showProgress()
	{
		drawComponents();

        new Task().execute();

        setVisible(true);		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == cancelButton)
		{
			cancelButton.setEnabled(false);
			taskToExecute.interrupt();
		}
	}

	private void drawComponents()
    {
		setBounds(100, 100, 288, 138);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setUndecorated(true);
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
		setContentPane(panel);
		setLocationRelativeTo(mainWindow);
		if(rightToLeft)
		{
			setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		JLabel lblNewLabel = new JLabel(messages.get("SCANNING") + "...");
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		
		cancelButton = new JButton(messages.get("CANCEL"));
		cancelButton.addActionListener(this);

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(25)
					.addComponent(lblNewLabel))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(25)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(92)
					.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addComponent(lblNewLabel)
					.addGap(11)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(cancelButton))
		);
		getContentPane().setLayout(groupLayout);
    }

    private class Task extends SwingWorker<Void, Void>
    {
        @Override
        public Void doInBackground()
        {
        	taskToExecute.execute();

        	return null;
        }

        @Override
        public void done()
        {
        	dispose();
        }
    }
}
