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
package info.msxlaunchers.openmsx.launcher.ui.view.swing;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.UpdateCheckerPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * Update Checker window class
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class UpdateCheckerWindow extends JDialog implements ActionListener
{
	private final UpdateCheckerPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;

	private Component updateOpenMSXLauncherButton;
	private Component updateExtraDataButton;
	private Component downloadScreenshotsButton;
	private JButton closeButton;
	private JPanel launcherVersionPane;
	private JPanel extraDataVersionPane;

	private final Dimension INSTALL_BUTTON_SIZE = new Dimension(100, 18);

	public UpdateCheckerWindow(UpdateCheckerPresenter presenter,
			Language language,
			boolean rightToLeft)
	{
		this.presenter = presenter;
		this.rightToLeft = rightToLeft;

		this.parent = GlobalSwingContext.getIntance().getMainWindow();
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
	}

	public void display()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("CHECK_FOR_UPDATES"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel tablePane = new JPanel();

		GridBagLayout tableLayout = new GridBagLayout();
		tablePane.setLayout(tableLayout);
		GridBagConstraints labelConstraints = new GridBagConstraints();
		GridBagConstraints valueConstraints = new GridBagConstraints();

		valueConstraints.fill = GridBagConstraints.HORIZONTAL;
		valueConstraints.anchor = GridBagConstraints.NORTHWEST;
		valueConstraints.weightx = 1.0;
		valueConstraints.gridwidth = GridBagConstraints.REMAINDER;
		valueConstraints.insets = new Insets(4, 4, 4, 8);

        labelConstraints = (GridBagConstraints)valueConstraints.clone();
        labelConstraints.weightx = 0.0;
        labelConstraints.gridwidth = 1;

		//openMSX Launcher version
		launcherVersionPane = new JPanel();

		JLabel launcherMessage = new JLabel();
		launcherVersionPane.add(launcherMessage);
		if(presenter.isNewOpenMSXLauncherVersionDownloaded())
		{
			launcherMessage.setText(messages.get("NEW_VERSION_DOWNLOADED_NEED_RESTART"));
		}
		else if(presenter.isNewOpenMSXLauncherVersionAvailable())
		{
			launcherMessage.setText(messages.get("NEW_VERSION_AVAILABLE"));
			updateOpenMSXLauncherButton = new ActionButton(this, messages.get("INSTALL"));
			launcherVersionPane.add(updateOpenMSXLauncherButton);
		}
		else
		{
			launcherMessage.setText(messages.get("UP_TO_DATE"));
		}

		addPropertyToDisplay(tablePane, tableLayout, "openMSX Launcher", launcherVersionPane, labelConstraints, valueConstraints, true);

		//Extra Data version
		extraDataVersionPane = new JPanel();

		JLabel extraDataMessage = new JLabel();
		extraDataVersionPane.add(extraDataMessage);
		if(presenter.isNewExtraDataVersionAvailable())
		{
			extraDataMessage.setText(messages.get("NEW_VERSION_AVAILABLE"));
			updateExtraDataButton = new ActionButton(this, messages.get("INSTALL"));
			extraDataVersionPane.add(updateExtraDataButton);
		}
		else
		{
			extraDataMessage.setText(messages.get("UP_TO_DATE"));
		}

		addPropertyToDisplay(tablePane, tableLayout, messages.get("EXTRA_DATA"), extraDataVersionPane, labelConstraints, valueConstraints, false);

		//Screenshots version
		JPanel screenshotsVersionPane = new JPanel();

		JLabel screenshotsMessage = new JLabel();
		screenshotsVersionPane.add(screenshotsMessage);
		if(!presenter.isScreenshotsSetInSettings())
		{
			screenshotsMessage.setText(messages.get("UNDEFINED"));
		}
		else if(presenter.isNewScreenshotsVersionAvailable())
		{
			screenshotsMessage.setText(messages.get("NEW_VERSION_AVAILABLE"));
			downloadScreenshotsButton = new ActionButton(this, messages.get("DOWNLOAD"));
			screenshotsVersionPane.add(downloadScreenshotsButton);
		}
		else
		{
			screenshotsMessage.setText(messages.get("UP_TO_DATE"));
		}

		addPropertyToDisplay(tablePane, tableLayout, messages.get("SCREENSHOTS"), screenshotsVersionPane, labelConstraints, valueConstraints, false);

		//Close button
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		closeButton = new JButton(messages.get("CLOSE"));
		closeButton.addActionListener(this);
		closeButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(closeButton);

		if(rightToLeft)
		{
			tablePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			launcherVersionPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			extraDataVersionPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			screenshotsVersionPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			launcherVersionPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
			extraDataVersionPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
			screenshotsVersionPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		}
		else
		{
			launcherVersionPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
			extraDataVersionPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
			screenshotsVersionPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		}

		contentPane.add(tablePane);
		contentPane.add(buttonsPane);

		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	private void addPropertyToDisplay(JPanel tablePane, GridBagLayout tableLayout, String attribute, JPanel value, GridBagConstraints labelConstraints, GridBagConstraints valueConstraints, boolean colonOnTheLeft)
	{
		JLabel attributeLabel;
		if(rightToLeft && colonOnTheLeft)
		{
			attributeLabel = new JLabel(":" + attribute);
		}
		else
		{
			attributeLabel = new JLabel(attribute + ":");				
		}
        tableLayout.setConstraints(attributeLabel, labelConstraints);
        tablePane.add(attributeLabel);

        if(rightToLeft)
        {
        	attributeLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        tableLayout.setConstraints(value, valueConstraints);
        tablePane.add(value);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == closeButton)
		{
			dispose();
		}
		else if(source == updateOpenMSXLauncherButton)
		{
			try
			{
				presenter.onRequestUpdateOpenMSXLauncher();
				showSuccessfulUpdateMessage(launcherVersionPane, messages.get("NEW_VERSION_DOWNLOADED_NEED_RESTART"));
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, rightToLeft);
			}
		}
		else if(source == updateExtraDataButton)
		{
			try
			{
				presenter.onRequestUpdateExtraData();

				showSuccessfulUpdateMessage(extraDataVersionPane, messages.get("UPDATED_SUCCESSFULLY"));
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, rightToLeft);
			}
		}
		else if(source == downloadScreenshotsButton)
		{
			try
			{
				presenter.onRequestOpenDownloadPage();
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, rightToLeft);
			}
		}
	}

	private void showSuccessfulUpdateMessage(JPanel panel, String message)
	{
		panel.removeAll();
		panel.add(new JLabel(message));
		panel.validate();
		panel.repaint();
	}

	private class ActionButton extends Component
	{
		private final String label;

		private final Color NORMAL_COLOR = new Color(40, 70, 170);
		private final Color HOVER_COLOR = new Color(50, 80, 180);
		private final Color PRESSED_COLOR = new Color(30, 40, 150);

	    private boolean inside = false;
	    private boolean pressed = false;

		public ActionButton(final ActionListener listener, final String label)
		{
			this.label = label;

			setPreferredSize(INSTALL_BUTTON_SIZE);
		    setCursor(new Cursor(Cursor.HAND_CURSOR));

		    final ActionButton actionButtonRef = this;

		    addMouseListener(new MouseAdapter() {
		        @Override
		        public void mouseClicked(MouseEvent me) {
		        	listener.actionPerformed(new ActionEvent(actionButtonRef, 0, null));
		        }
		        @Override
		        public void mousePressed(MouseEvent me)
		        {
		        	pressed = true;
		        	repaint();
		        }
		        @Override
		        public void mouseReleased(MouseEvent me)
		        {
		        	pressed = false;
		        	repaint();
		        }
		        @Override
		        public void mouseEntered(MouseEvent me) {
		        	inside = true;
		        	repaint();
		        }
		        @Override
		        public void mouseExited(MouseEvent me) {
		        	inside = false;
		        	repaint();
		        }
		    });
		}

	    @Override
	    public void paint(Graphics g)
	    {
	    	if(inside)
	    	{
	    		if(pressed)
	    		{
		    		g.setColor(PRESSED_COLOR);
	    		}
	    		else
	    		{
				    g.setColor(HOVER_COLOR);
	    		}
	    	}
	    	else
	    	{
			    g.setColor(NORMAL_COLOR);
	    	}
	        g.fillRoundRect(0, 0, getWidth(), getHeight() - 1, 10, 10);

	        g.drawRoundRect(0, 0, getWidth(), getHeight() - 1, 10, 10);

            g.setColor(Color.white);
		    FontMetrics fm = getFontMetrics(getFont());
		    
            g.drawString(label, getWidth() / 2 - fm.stringWidth(label) / 2, getHeight() / 2 + fm.getMaxDescent() + 1);
	    }
	}
}