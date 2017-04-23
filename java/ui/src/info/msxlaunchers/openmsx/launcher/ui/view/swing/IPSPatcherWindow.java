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

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
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
public class IPSPatcherWindow extends JDialog implements ActionListener
{
	private final PatcherPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;

	private JTextField ipsPatchFileTextField;
	private JButton ipsPatchFileButton;
	private JTextField sourceFileTextField;
	private JButton sourceFileButton;
	private JCheckBox verifyChecksumCheckBox;
	private JTextField checksumTextField;
	private JLabel targetFileLabel;
	private JTextField targetFileTextField;
	private JButton targetFileButton;
	private JRadioButton targetPatchedFileRadioButton;
	private JRadioButton patchDirectlyRadioButton;
	private JButton okButton;
	private JButton cancelButton;

	private final FlowLayout LEFT_FLOW_LAYOUT = new FlowLayout(FlowLayout.LEFT);
	private final FlowLayout RIGHT_FLOW_LAYOUT = new FlowLayout(FlowLayout.RIGHT);

	public IPSPatcherWindow(PatcherPresenter presenter, Language language, boolean rightToLeft)
	{
		this.presenter = presenter;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.parent = GlobalSwingContext.getIntance().getMainWindow();
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
		sourcesPane.setBorder(BorderFactory.createTitledBorder(messages.get("INPUTS")));
		sourcesPane.setLayout(new BoxLayout(sourcesPane, BoxLayout.Y_AXIS));

		JPanel ipsFilePane = new JPanel();

		JLabel ipsFileLabel = new JLabel("IPS");
		ipsFilePane.add(ipsFileLabel);

		ipsPatchFileTextField = new JTextFieldDragDrop();
		ipsPatchFileTextField.setColumns(25);
		ipsFilePane.add(ipsPatchFileTextField);

		ipsPatchFileButton = new JButton(Icons.FOLDER.getImageIcon());
		ipsPatchFileButton.addActionListener(this);
		ipsPatchFileButton.setToolTipText(messages.get("BROWSE"));
		ipsPatchFileButton.setPreferredSize(WindowUtils.iconButtonDimension);
		ipsFilePane.add(ipsPatchFileButton);

		sourcesPane.add(ipsFilePane);

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

		JPanel verifyChecksumPane = new JPanel();

		verifyChecksumCheckBox = new JCheckBox(messages.get("VERIFY_CHECKSUM"));
		verifyChecksumCheckBox.addActionListener(this);
		verifyChecksumPane.add(verifyChecksumCheckBox);
		checksumTextField = new JTextField();
		checksumTextField.setColumns(18);
		checksumTextField.setToolTipText("[ SHA1 , MD5 ]");
		verifyChecksumPane.add(checksumTextField);
		verifyChecksumPane.setBorder(new EmptyBorder(1, 1, 1, WindowUtils.iconButtonDimension.width + 6));

		sourcesPane.add(verifyChecksumPane);

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
		targetFilePane.setLayout(RIGHT_FLOW_LAYOUT);

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
		patchDirectlyPane.setLayout(LEFT_FLOW_LAYOUT);
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
			sourcesPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			targetPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			ipsFilePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			ipsFilePane.setLayout(LEFT_FLOW_LAYOUT);
			sourceFilePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			sourceFilePane.setLayout(LEFT_FLOW_LAYOUT);
			sourceFilePane.setBorder(new EmptyBorder(1, 20, 1, 1));
			verifyChecksumPane.setLayout(LEFT_FLOW_LAYOUT);
			verifyChecksumPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			verifyChecksumCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			targetFilePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			targetFilePane.setLayout(LEFT_FLOW_LAYOUT);
			targetPatchedFilePane.setLayout(RIGHT_FLOW_LAYOUT);
			patchDirectlyPane.setLayout(RIGHT_FLOW_LAYOUT);
			patchDirectlyRadioButton.setHorizontalTextPosition(SwingConstants.LEADING);
			targetPatchedFileRadioButton.setHorizontalTextPosition(SwingConstants.LEADING);
			buttonsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		else
		{
			ipsFilePane.setLayout(RIGHT_FLOW_LAYOUT);
			sourceFilePane.setLayout(RIGHT_FLOW_LAYOUT);
			sourceFilePane.setBorder(new EmptyBorder(1, 20, 1, 1));
			verifyChecksumPane.setLayout(RIGHT_FLOW_LAYOUT);
			targetFilePane.setLayout(RIGHT_FLOW_LAYOUT);
			
			targetPatchedFilePane.setLayout(LEFT_FLOW_LAYOUT);
			patchDirectlyPane.setLayout(LEFT_FLOW_LAYOUT);
		}

		pack();
        setLocationRelativeTo(parent);
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
		if(source == ipsPatchFileButton)
		{
			WindowUtils.browseFile(this, ipsPatchFileTextField, "IPS", FileTypeUtils.getIPSExtensions(), false);
		}
		else if(source == sourceFileButton)
		{
			WindowUtils.browseFile(this, sourceFileTextField);
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
		ipsPatchFileTextField.setText(null);
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
			if (presenter.onRequestPatchFileAction(ipsPatchFileTextField.getText(), sourceFileTextField.getText(),
					targetPatchedFileRadioButton.isSelected(), targetFileTextField.getText(),
					verifyChecksumCheckBox.isSelected(), checksumTextField.getText()))
			{
				MessageBoxUtil.showInformationMessageBox(parent, messages.get("FILE_PATCHED_SUCCESSFULLY"), messages, rightToLeft);
	
				resetFields();
			}
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(parent, le, messages, rightToLeft);
		}
	}
}
