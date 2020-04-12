/*
 * Copyright 2020 Sam Elsharif
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
import info.msxlaunchers.openmsx.launcher.ui.presenter.LHADecompressorPresenter;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JTextFieldDragDrop;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

/**
 * @since v1.14
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class LHADecompressorWindow extends JDialog implements ActionListener
{
	private final LHADecompressorPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component mainWindow;

	private JTextField compressedFileTextField;
	private JButton compressedFileButton;
	private JCheckBox onlyMSXFilesCheckBox;
	private JTextField targetDirectoryTextField;
	private JRadioButton targetOtherDirectoryRadioButton;
	private JRadioButton targetDirectorySameAsFileRadioButton;
	private JButton targetDirectoryButton;
	private JButton okButton;
	private JButton cancelButton;

	private static final FlowLayout LEFT_FLOW_LAYOUT = new FlowLayout(FlowLayout.LEFT);
	private static final FlowLayout RIGHT_FLOW_LAYOUT = new FlowLayout(FlowLayout.RIGHT);

	public LHADecompressorWindow(LHADecompressorPresenter presenter, Language language, boolean rightToLeft)
	{
		this.presenter = presenter;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.mainWindow = GlobalSwingContext.getIntance().getMainWindow();
	}

	public void displayScreen()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("LHA_DECOMPRESSOR"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel sourcePane = new JPanel();
		sourcePane.setBorder(BorderFactory.createTitledBorder(messages.get("INPUT")));
		sourcePane.setLayout(new BoxLayout(sourcePane, BoxLayout.Y_AXIS));

		JPanel compressedFilePane = new JPanel();

		JLabel compressedFileLabel = new JLabel(messages.get("COMPRESSED_FILE"));
		compressedFilePane.add(compressedFileLabel);

		compressedFileTextField = new JTextFieldDragDrop();
		compressedFileTextField.setColumns(25);
		compressedFilePane.add(compressedFileTextField);

		compressedFileButton = new JButton(Icons.FOLDER.getImageIcon());
		compressedFileButton.addActionListener(this);
		compressedFileButton.setToolTipText(messages.get("BROWSE"));
		compressedFileButton.setPreferredSize(WindowUtils.iconButtonDimension);
		compressedFilePane.add(compressedFileButton);

		sourcePane.add(compressedFilePane);

		JPanel onlyMSXFilesPane = new JPanel();

		JPanel ipsChecksumVerifyPane = new JPanel();
		onlyMSXFilesCheckBox = new JCheckBox(messages.get("ONLY_MSX_FILES"));
		onlyMSXFilesCheckBox.addActionListener(this);
		ipsChecksumVerifyPane.add(onlyMSXFilesCheckBox);
		ipsChecksumVerifyPane.setBorder(new EmptyBorder(1, 1, 1,
				WindowUtils.iconButtonDimension.width +
				6));

		onlyMSXFilesPane.add(ipsChecksumVerifyPane);

		sourcePane.add(onlyMSXFilesPane);

		contentPane.add(sourcePane);

		//target selection
		JPanel targetPane = new JPanel();
		targetPane.setBorder(BorderFactory.createTitledBorder(messages.get("OUTPUT")));
		targetPane.setLayout(new BoxLayout(targetPane, BoxLayout.Y_AXIS));

		JPanel targetDirectoryPane = new JPanel();
		targetOtherDirectoryRadioButton = new JRadioButton(messages.get("SPECIFY_DIRECTORY_OTHER"));
		targetOtherDirectoryRadioButton.addActionListener(this);
		targetDirectoryPane.add(targetOtherDirectoryRadioButton);
		targetPane.add(targetDirectoryPane);

		JPanel targetFilePane = new JPanel();
		JLabel targetDirectoryLabel = new JLabel(messages.get("DIRECTORY"));
		targetFilePane.add(targetDirectoryLabel);

		targetDirectoryTextField = new JTextFieldDragDrop();
		targetDirectoryTextField.setColumns(25);
		targetFilePane.add(targetDirectoryTextField);

		targetDirectoryButton = new JButton(Icons.FOLDER.getImageIcon());
		targetDirectoryButton.addActionListener(this);
		targetDirectoryButton.setToolTipText(messages.get("BROWSE"));
		targetDirectoryButton.setPreferredSize(WindowUtils.iconButtonDimension);
		targetFilePane.add(targetDirectoryButton);
		targetPane.add(targetFilePane);

		JPanel directorySameAsFilePane = new JPanel();
		targetPane.add(directorySameAsFilePane);

		contentPane.add(targetPane);

		targetDirectorySameAsFileRadioButton = new JRadioButton(messages.get("USE_SAME_DIRECTORY_AS_FILE"));
		targetDirectorySameAsFileRadioButton.addActionListener(this);
		directorySameAsFilePane.add(targetDirectorySameAsFileRadioButton);
		targetPane.add(directorySameAsFilePane);

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(targetOtherDirectoryRadioButton);
		buttonGroup.add(targetDirectorySameAsFileRadioButton);

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
			sourcePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			targetPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			compressedFilePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			compressedFilePane.setLayout(LEFT_FLOW_LAYOUT);
			ipsChecksumVerifyPane.setLayout(LEFT_FLOW_LAYOUT);
			ipsChecksumVerifyPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			onlyMSXFilesCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			targetFilePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			targetFilePane.setLayout(LEFT_FLOW_LAYOUT);
			targetDirectoryPane.setLayout(RIGHT_FLOW_LAYOUT);
			directorySameAsFilePane.setLayout(LEFT_FLOW_LAYOUT);
			directorySameAsFilePane.setLayout(RIGHT_FLOW_LAYOUT);
			buttonsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		else
		{
			compressedFilePane.setLayout(RIGHT_FLOW_LAYOUT);
			ipsChecksumVerifyPane.setLayout(RIGHT_FLOW_LAYOUT);
			targetFilePane.setLayout(RIGHT_FLOW_LAYOUT);			
			targetDirectoryPane.setLayout(LEFT_FLOW_LAYOUT);
			directorySameAsFilePane.setLayout(LEFT_FLOW_LAYOUT);
		}

		pack();
		setLocationRelativeTo(mainWindow);
		setVisible(true);
	}

	public boolean targetFileReplacementIsConfirmed()
	{
		return MessageBoxUtil.showYesNoMessageBox(this, messages.get("CONFIRM_REPLACE_DECOMPRESSED_FILE_MSG"), messages, rightToLeft) == 0;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == compressedFileButton)
		{
			WindowUtils.browseFile(this, compressedFileTextField, "LHA", FileTypeUtils.getLHAExtensions(), false);
		}
		else if(source == targetDirectoryButton)
		{
			WindowUtils.browseForDirectory(this, targetDirectoryTextField);
		}
		else if(source == targetOtherDirectoryRadioButton)
		{
			targetDirectoryTextField.setEnabled(true);
			targetDirectoryButton.setEnabled(true);
		}
		else if(source == targetDirectorySameAsFileRadioButton)
		{
			targetDirectoryTextField.setEnabled(false);
			targetDirectoryButton.setEnabled(false);
		}
		else if(source == okButton)
		{
			processDecompressRequest();
		}
		else if(source == cancelButton)
		{
			dispose();
		}
	}

	private void resetFields()
	{
		onlyMSXFilesCheckBox.setSelected(true);
		targetDirectoryTextField.setText(null);
		targetOtherDirectoryRadioButton.setSelected(true);
		targetDirectoryTextField.setEnabled(true);
		targetDirectoryButton.setEnabled(true);
	}

	private void processDecompressRequest()
	{
		try
		{
			presenter.onRequestLHADecompressAction(compressedFileTextField.getText(),
					targetOtherDirectoryRadioButton.isSelected()? targetDirectoryTextField.getText() : null, onlyMSXFilesCheckBox.isSelected());
		}
		catch( LauncherException le )
		{
			// TODO Auto-generated catch block
			le.printStackTrace();
		}

		MessageBoxUtil.showInformationMessageBox(mainWindow, messages.get("FILE_DECOMPRESSED_SUCCESSFULLY"), messages, rightToLeft);

		resetFields();
	}
}
