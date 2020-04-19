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

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.patch.PatchMethod;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.PatcherPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JTextFieldDragDrop;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

/**
 * @since v1.9
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class PatcherWindow extends JDialog implements ActionListener
{
	private final PatcherPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component mainWindow;

	private JComboBox<PatchMethod> patchMethodComboBox;
	private JTextField patchFileTextField;
	private JButton patchFileButton;
	private JTextField sourceFileTextField;
	private JButton sourceFileButton;
	private JPanel verifyChecksumPane;
	private JCheckBox verifyChecksumCheckBox;
	private JTextField checksumTextField;
	private JCheckBox skipCRCVerificationCheckBox;
	private JLabel targetFileLabel;
	private JTextField targetFileTextField;
	private JButton targetFileButton;
	private JRadioButton targetPatchedFileRadioButton;
	private JRadioButton patchDirectlyRadioButton;
	private JButton okButton;
	private JButton cancelButton;

	private static final FlowLayout LEFT_FLOW_LAYOUT = new FlowLayout(FlowLayout.LEFT);
	private static final FlowLayout RIGHT_FLOW_LAYOUT = new FlowLayout(FlowLayout.RIGHT);

	public PatcherWindow(PatcherPresenter presenter, Language language, boolean rightToLeft)
	{
		this.presenter = presenter;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.mainWindow = GlobalSwingContext.getIntance().getMainWindow();
	}

	public void displayScreen()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("PATCH_CENTER"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		//Sources selection
		JPanel sourcesPane = new JPanel();
		sourcesPane.setBorder(BorderFactory.createTitledBorder(messages.get("INPUT")));
		sourcesPane.setLayout(new BoxLayout(sourcesPane, BoxLayout.Y_AXIS));

		JPanel patchMethodPane = new JPanel();

		JLabel patchMethodLabel = new JLabel(messages.get("METHOD"));
		patchMethodPane.add(patchMethodLabel);

		patchMethodComboBox = new JComboBox<>();
		patchMethodComboBox.addItem(PatchMethod.IPS);
		patchMethodComboBox.addItem(PatchMethod.UPS);
		patchMethodComboBox.setPreferredSize(new Dimension(patchMethodComboBox.getPreferredSize().width + 20, patchMethodComboBox.getPreferredSize().height));
		patchMethodComboBox.addActionListener(this);
		patchMethodPane.add(patchMethodComboBox);

		sourcesPane.add(patchMethodPane);

		JPanel patchFilePane = new JPanel();

		JLabel patchFileLabel = new JLabel(messages.get("PATCH"));
		patchFilePane.add(patchFileLabel);

		patchFileTextField = new JTextFieldDragDrop();
		patchFileTextField.setColumns(25);
		patchFilePane.add(patchFileTextField);

		patchFileButton = new JButton(Icons.FOLDER.getImageIcon());
		patchFileButton.addActionListener(this);
		patchFileButton.setToolTipText(messages.get("BROWSE"));
		patchFileButton.setPreferredSize(WindowUtils.iconButtonDimension);
		patchFilePane.add(patchFileButton);

		sourcesPane.add(patchFilePane);

		JPanel sourceFilePane = new JPanel();

		JLabel sourceFileLabel = new JLabel(messages.get("SOURCE"));
		sourceFilePane.add(sourceFileLabel);

		sourceFileTextField = new JTextFieldDragDrop();
		sourceFileTextField.setColumns(25);
		sourceFilePane.add(sourceFileTextField);

		sourceFileButton = new JButton(Icons.FOLDER.getImageIcon());
		sourceFileButton.addActionListener(this);
		sourceFileButton.setToolTipText(messages.get("BROWSE"));
		sourceFileButton.setPreferredSize(WindowUtils.iconButtonDimension);
		sourceFilePane.add(sourceFileButton);

		sourcesPane.add(sourceFilePane);

		verifyChecksumPane = new JPanel();
		CardLayout cardLayout = new CardLayout();
		verifyChecksumPane.setLayout(cardLayout);

		JPanel ipsChecksumVerifyPane = new JPanel();
		verifyChecksumCheckBox = new JCheckBox(messages.get("VERIFY_CHECKSUM"));
		verifyChecksumCheckBox.addActionListener(this);
		ipsChecksumVerifyPane.add(verifyChecksumCheckBox);
		checksumTextField = new JTextField();
		checksumTextField.setColumns(18);
		checksumTextField.setToolTipText("[ SHA1 , MD5 , CRC32 ]");
		ipsChecksumVerifyPane.add(checksumTextField);
		ipsChecksumVerifyPane.setBorder(new EmptyBorder(1, 1, 1,
				WindowUtils.iconButtonDimension.width +
				6));

		verifyChecksumPane.add(ipsChecksumVerifyPane, PatchMethod.IPS.toString());

		JPanel upsChecksumVerifyPane = new JPanel();
		skipCRCVerificationCheckBox = new JCheckBox(messages.get("SKIP_CRC"));
		upsChecksumVerifyPane.add(skipCRCVerificationCheckBox);
		upsChecksumVerifyPane.setBorder(new EmptyBorder(6, 1, 1,
				WindowUtils.iconButtonDimension.width +
				6 +
				patchFileTextField.getPreferredSize().width -
				skipCRCVerificationCheckBox.getPreferredSize().width));

		verifyChecksumPane.add(upsChecksumVerifyPane, PatchMethod.UPS.toString());

		cardLayout.show(verifyChecksumPane, PatchMethod.IPS.toString());

		sourcesPane.add(verifyChecksumPane);

		//adjust the alignment of the patch method pane
		patchMethodPane.setBorder(new EmptyBorder(1, 1, 1,
				WindowUtils.iconButtonDimension.width + 6 + patchFileTextField.getPreferredSize().width - patchMethodComboBox.getPreferredSize().width));

		contentPane.add(sourcesPane);

		//target selection
		JPanel targetPane = new JPanel();
		targetPane.setBorder(BorderFactory.createTitledBorder(messages.get("OUTPUT")));
		targetPane.setLayout(new BoxLayout(targetPane, BoxLayout.Y_AXIS));

		JPanel targetPatchedFilePane = new JPanel();
		targetPatchedFileRadioButton = new JRadioButton(messages.get("SPECIFY_LOCATION_PATCHED_FILE"));
		targetPatchedFileRadioButton.addActionListener(this);
		targetPatchedFilePane.add(targetPatchedFileRadioButton);
		targetPane.add(targetPatchedFilePane);

		JPanel targetFilePane = new JPanel();
		targetFileLabel = new JLabel(messages.get("TARGET"));
		targetFilePane.add(targetFileLabel);

		targetFileTextField = new JTextFieldDragDrop();
		targetFileTextField.setColumns(25);
		targetFilePane.add(targetFileTextField);

		targetFileButton = new JButton(Icons.FOLDER.getImageIcon());
		targetFileButton.addActionListener(this);
		targetFileButton.setToolTipText(messages.get("BROWSE"));
		targetFileButton.setPreferredSize(WindowUtils.iconButtonDimension);
		targetFilePane.add(targetFileButton);
		targetPane.add(targetFilePane);

		JPanel patchDirectlyPane = new JPanel();
		patchDirectlyRadioButton = new JRadioButton(messages.get("PATCH_DIRECTLY"));
		patchDirectlyRadioButton.addActionListener(this);
		patchDirectlyPane.add(patchDirectlyRadioButton);
		targetPane.add(patchDirectlyPane);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(targetPatchedFileRadioButton);
		buttonGroup.add(patchDirectlyRadioButton);

		contentPane.add(targetPane);

		//buttons
		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		okButton = new JButton(messages.get("OK"));
		okButton.addActionListener(this);
		okButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(okButton);

		cancelButton = new JButton(messages.get("CANCEL"));
		cancelButton.addActionListener(this);
		cancelButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(cancelButton);

		contentPane.add(buttonsPane);

		resetFields();

		if(rightToLeft)
		{
			patchMethodPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			sourcesPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			targetPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			patchFilePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			patchFilePane.setLayout(LEFT_FLOW_LAYOUT);
			sourceFilePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			sourceFilePane.setLayout(LEFT_FLOW_LAYOUT);
			sourceFilePane.setBorder(new EmptyBorder(1, 20, 1, 1));
			ipsChecksumVerifyPane.setLayout(LEFT_FLOW_LAYOUT);
			ipsChecksumVerifyPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			verifyChecksumCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			upsChecksumVerifyPane.setLayout(LEFT_FLOW_LAYOUT);
			upsChecksumVerifyPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			skipCRCVerificationCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			targetFilePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			targetFilePane.setLayout(LEFT_FLOW_LAYOUT);
			targetPatchedFilePane.setLayout(RIGHT_FLOW_LAYOUT);
			patchDirectlyPane.setLayout(LEFT_FLOW_LAYOUT);
			patchDirectlyPane.setLayout(RIGHT_FLOW_LAYOUT);
			targetPatchedFileRadioButton.setHorizontalTextPosition(SwingConstants.LEADING);
			patchDirectlyRadioButton.setHorizontalTextPosition(SwingConstants.LEADING);
			buttonsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		else
		{
			patchMethodPane.setLayout(RIGHT_FLOW_LAYOUT);
			patchFilePane.setLayout(RIGHT_FLOW_LAYOUT);
			sourceFilePane.setLayout(RIGHT_FLOW_LAYOUT);
			sourceFilePane.setBorder(new EmptyBorder(1, 20, 1, 1));
			ipsChecksumVerifyPane.setLayout(RIGHT_FLOW_LAYOUT);
			upsChecksumVerifyPane.setLayout(RIGHT_FLOW_LAYOUT);
			targetFilePane.setLayout(RIGHT_FLOW_LAYOUT);			
			targetPatchedFilePane.setLayout(LEFT_FLOW_LAYOUT);
			patchDirectlyPane.setLayout(LEFT_FLOW_LAYOUT);
		}

		pack();
		setLocationRelativeTo(mainWindow);
		setVisible(true);
	}

	public boolean targetFileReplacementIsConfirmed()
	{
		return MessageBoxUtil.showYesNoMessageBox(this, messages.get("CONFIRM_REPLACE_TARGET_FILE_MSG"), messages, rightToLeft) == 0;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == patchMethodComboBox)
		{
			((CardLayout)verifyChecksumPane.getLayout()).show(verifyChecksumPane, patchMethodComboBox.getSelectedItem().toString());
		}
		else if(source == patchFileButton)
		{
			WindowUtils.browseFile(this, patchFileTextField, messages.get("PATCH"), FileTypeUtils.getPatchExtensions(), false);
		}
		else if(source == sourceFileButton)
		{
			WindowUtils.browseFile(this, sourceFileTextField);
		}
		else if(source == targetFileButton)
		{
			WindowUtils.browseFile(this, targetFileTextField);
		}
		else if(source == verifyChecksumCheckBox)
		{
			checksumTextField.setEnabled(verifyChecksumCheckBox.isSelected());
		}
		else if(source == patchDirectlyRadioButton)
		{
			targetFileTextField.setEnabled(false);
			targetFileButton.setEnabled(false);
			targetFileLabel.setEnabled(false);
		}
		else if(source == targetPatchedFileRadioButton)
		{
			targetFileTextField.setEnabled(true);
			targetFileButton.setEnabled(true);
			targetFileLabel.setEnabled(true);
		}
		else if(source == okButton)
		{
			processPatchRequest();
		}
		else if(source == cancelButton)
		{
			dispose();
		}
	}

	private void resetFields()
	{
		patchFileTextField.setText(null);
		sourceFileTextField.setText(null);
		verifyChecksumCheckBox.setSelected(false);
		checksumTextField.setText(null);
		checksumTextField.setEnabled(false);
		targetPatchedFileRadioButton.setSelected(true);
		targetFileTextField.setText(null);
		targetFileTextField.setEnabled(true);
		targetFileButton.setEnabled(true);
		targetFileLabel.setEnabled(true);
	}

	private void processPatchRequest()
	{
		try
		{
			boolean isIPSPatchMethod = patchMethodComboBox.getSelectedItem().equals(PatchMethod.IPS);
			if(presenter.onValidate(sourceFileTextField.getText(), patchFileTextField.getText(), targetPatchedFileRadioButton.isSelected(),
					targetFileTextField.getText(), isIPSPatchMethod, !verifyChecksumCheckBox.isSelected(), checksumTextField.getText()))
			{
				BusyIndicator busyIndicator = new BusyIndicator();
				busyIndicator.prepareIndicator();

				PatchTask patchTask = new PatchTask(isIPSPatchMethod, busyIndicator);
				patchTask.execute();

				busyIndicator.showIndicator();

				//here the patching task finished
				//the next call is meant to throws a LauncherException that may have been thrown in the task
				patchTask.get();

				//here patching was successful
				MessageBoxUtil.showInformationMessageBox(mainWindow, messages.get("FILE_PATCHED_SUCCESSFULLY"), messages, rightToLeft);
				
				resetFields();
			}
		}
		catch(LauncherException le)
		{
			//this can only be thrown from the validation not the patching step
			MessageBoxUtil.showErrorMessageBox(mainWindow, le, messages, rightToLeft);
		}
		catch (InterruptedException ie)
		{
			//shouldn't happen
		}
		catch(ExecutionException ee)
		{
			Throwable ex = ee.getCause();
			if(ex instanceof LauncherException)
			{
				MessageBoxUtil.showErrorMessageBox(mainWindow, (LauncherException)ex, messages, rightToLeft);
			}
		}
	}

	private class PatchTask extends SwingWorker<Void, Void>
	{
		private final boolean isIPSPatchMethod;
		private final BusyIndicator busyIndicator;

		PatchTask(boolean isIPSPatchMethod, BusyIndicator busyIndicator)
		{
			this.isIPSPatchMethod = isIPSPatchMethod;
			this.busyIndicator = busyIndicator;
		}

		@Override
		protected Void doInBackground() throws LauncherException
		{
			if(isIPSPatchMethod)
			{
				presenter.onRequestPatchFileActionForIPS(patchFileTextField.getText(), sourceFileTextField.getText(),
						targetPatchedFileRadioButton.isSelected(), targetFileTextField.getText(),
						!verifyChecksumCheckBox.isSelected(), checksumTextField.getText());
			}
			else
			{
				presenter.onRequestPatchFileActionForUPS(patchFileTextField.getText(), sourceFileTextField.getText(),
						targetPatchedFileRadioButton.isSelected(), targetFileTextField.getText(),
						skipCRCVerificationCheckBox.isSelected());
			}

			return null;
		}

		@Override
		protected void done()
		{
			busyIndicator.dispose();
        }
    }

	private class BusyIndicator extends JDialog
	{
		void prepareIndicator()
		{
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setResizable(false);
			setUndecorated(true);
		    JPanel panel = new JPanel();
		    panel.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.GRAY));
		    setContentPane(panel);

			if(rightToLeft)
			{
				panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 8));
			}
			else
			{
				panel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
			}

			panel.add(new JLabel(Icons.BUSY_INDICATOR.getImageIcon()));
			panel.add(new JLabel(messages.get("PATCHING") + "..."));

			pack();
			setLocationRelativeTo(mainWindow);
		}

		void showIndicator()
		{
			setVisible(true);
		}
	}
}
