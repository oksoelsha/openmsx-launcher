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

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
import info.msxlaunchers.openmsx.launcher.data.game.constants.MSXGeneration;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Sound;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.HyperLink;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Game Properties dialog class
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class GamePropertiesWindow extends JDialog implements ActionListener
{
	private final Game game;
	private final RepositoryGame repositoryGame;
	private final int knownDumps;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;
	private final String generationMSXURL;

	private JPanel tablePane = null;
	private GridBagLayout tableLayout = null;
	private GridBagConstraints labelConstraints = null;
	private GridBagConstraints valueConstraints = null;

	private JButton okButton;

	private final static String SEPARATOR = ", ";
	private final static int MSX_GEN_NON_EXISTING_CODES_START = 10000;

	public GamePropertiesWindow(Game game,
								RepositoryGame repositoryGame,
								int knownDumps,
								Language language,
								boolean rightToLeft,
								String generationMSXURL)
	{
		this.game = game;
		this.repositoryGame = repositoryGame;
		this.knownDumps = knownDumps;
		this.rightToLeft = rightToLeft;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.parent = GlobalSwingContext.getIntance().getMainWindow();
		this.generationMSXURL = generationMSXURL;
	}

	public void display()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(messages.get("PROPERTIES"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		tablePane = new JPanel();
		if(rightToLeft)
		{
			tablePane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}
		tableLayout = new GridBagLayout();
		tablePane.setLayout(tableLayout);
		labelConstraints = new GridBagConstraints();
		valueConstraints = new GridBagConstraints();

		valueConstraints.fill = GridBagConstraints.HORIZONTAL;
		valueConstraints.anchor = GridBagConstraints.NORTHWEST;
		valueConstraints.weightx = 1.0;
		valueConstraints.gridwidth = GridBagConstraints.REMAINDER;
		valueConstraints.insets = new Insets(2, 2, 2, 4);

        labelConstraints = (GridBagConstraints)valueConstraints.clone();
        labelConstraints.weightx = 0.0;
        labelConstraints.gridwidth = 1;

        addPropertyToDisplay(messages.get("NAME"), game.getName());
        if(game.isROM() && repositoryGame != null)
        {
            addPropertyToDisplay(messages.get("NAME") + " (openMSX)", repositoryGame.getTitle());
        }
        File mainFile = new File(FileTypeUtils.getMainFile(game.getRomA(), game.getRomB(), game.getDiskA(), game.getDiskB(),
        		game.getTape(), game.getHarddisk(), game.getLaserdisc(), game.getTclScript()));
        addPropertyToDisplay(messages.get("FILE"), mainFile.getAbsolutePath());
        addPropertyToDisplay(messages.get("MEDIUM"), messages.get(getMedium(game)));
        if(repositoryGame != null)
        {
            addPropertyToDisplay(messages.get("KNOWN_DUMPS"), String.valueOf(knownDumps));
        }
        if(game.getSize() > 0)
        {
        	addPropertyToDisplay(messages.get("SIZE"), Utils.getString(game.getSize() / 1024) + " KB");
        }
        addPropertyToDisplay("SHA1", game.getSha1Code(), true);
        if(repositoryGame != null)
        {
            addPropertyToDisplay(messages.get("COMPANY"), repositoryGame.getCompany());
            addPropertyToDisplay(messages.get("YEAR"), repositoryGame.getYear());
            addPropertyToDisplay(messages.get("COUNTRY"), messages.get(repositoryGame.getCountry()));
            addPropertyToDisplay(messages.get("MAPPER"), repositoryGame.getMapper());
            addPropertyToDisplay(messages.get("START"), repositoryGame.getStart());
            addPropertyToDisplay(messages.get("DUMP"), repositoryGame.getOriginalText());
            addPropertyToDisplay(messages.get("REMARK"), repositoryGame.getRemark());
        }
        addPropertyToDisplay(messages.get("GENERATION"), getGeneration(game));
        addPropertyToDisplay(messages.get("SOUND"), getSound(game));
        addPropertyToDisplay(messages.get("GENRE"), getGenre(game));
        if(game.getMsxGenID() > 0 && game.getMsxGenID() < MSX_GEN_NON_EXISTING_CODES_START)
        {
            addLinkToDisplay("Generation-MSX ID", Utils.getString(game.getMsxGenID()), generationMSXURL + game.getMsxGenID(), true);
        }

		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		okButton = new JButton(messages.get("OK"));
		okButton.addActionListener(this);
		okButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(okButton);

		contentPane.add(tablePane);
		contentPane.add(buttonsPane);

		pack();
        setLocationRelativeTo(parent);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == okButton)
		{
			dispose();
		}
	}

	private void addPropertyToDisplay(String attribute, String value)
	{
		addPropertyToDisplay(attribute, value, false);
	}

	private void addPropertyToDisplay(String attribute, String value, boolean colonOnTheLeft)
	{
		if(!Utils.isEmpty(value))
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

	        JTextField valueTextField = new JTextField(value) {
	        	@Override public void setBorder(Border border) {
	                // No border
	           	}
	        };
	
	        valueTextField.setEditable(false);
	        valueTextField.setOpaque(false);
	        valueTextField.setColumns(30);
	        valueTextField.setCaretPosition(0);
	        valueTextField.setBackground(new Color(0, 0, 0, 0));

	        if(rightToLeft)
	        {
	        	attributeLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	        	valueTextField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	        }

	        tableLayout.setConstraints(valueTextField, valueConstraints);
	        tablePane.add(valueTextField);
		}
	}

	private void addLinkToDisplay(String attribute, String label, String address, boolean colonOnTheLeft)
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

        JPanel linkPanel = new JPanel();
        linkPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        linkPanel.add(new HyperLink(label, address));
        tableLayout.setConstraints(linkPanel, valueConstraints);
        tablePane.add(linkPanel);

        if(rightToLeft)
        {
        	attributeLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        	linkPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
	}

	private String getMedium(Game game)
	{
		String medium = null;

		if(game.isROM())
		{
			medium = Medium.ROM.toString();
		}
		else if(game.isDisk())
		{
			medium = Medium.DISK.toString();
		}
		else if(game.isTape())
		{
			medium = Medium.TAPE.toString();
		}
		else if(game.isHarddisk())
		{
			medium = Medium.HARDDISK.toString();
		}
		else if(game.isLaserdisc())
		{
			medium = Medium.LASERDISC.toString();
		}
		else
		{
			medium = "";
		}

		return medium;
	}

	private String getGeneration(Game game)
	{
		StringBuilder generation = new StringBuilder("");

		if(game.isMSX())
		{
			generation.append(MSXGeneration.MSX.getDisplayName()).append(SEPARATOR);
		}
		if(game.isMSX2())
		{
			generation.append(MSXGeneration.MSX2.getDisplayName()).append(SEPARATOR);
		}
		if(game.isMSX2Plus())
		{
			generation.append(MSXGeneration.MSX2Plus.getDisplayName()).append(SEPARATOR);
		}
		if(game.isTurboR())
		{
			generation.append(MSXGeneration.TURBO_R.getDisplayName()).append(SEPARATOR);
		}

		if(generation.length() > 0)
		{
			generation.setLength(generation.length() - SEPARATOR.length());
		}

		return generation.toString();
	}

	private String getSound(Game game)
	{
		StringBuilder sound = new StringBuilder("");

		if(game.isPSG())
		{
			sound.append(Sound.PSG.getDisplayName()).append(SEPARATOR);
		}
		if(game.isSCC())
		{
			sound.append(Sound.SCC.getDisplayName()).append(SEPARATOR);
		}
		if(game.isSCCI())
		{
			sound.append(Sound.SCC_I.getDisplayName()).append(SEPARATOR);
		}
		if(game.isPCM())
		{
			sound.append(Sound.PCM.getDisplayName()).append(SEPARATOR);
		}
		if(game.isMSXMUSIC())
		{
			sound.append(Sound.MSX_MUSIC.getDisplayName()).append(SEPARATOR);
		}
		if(game.isMSXAUDIO())
		{
			sound.append(Sound.MSX_AUDIO.getDisplayName()).append(SEPARATOR);
		}
		if(game.isMoonsound())
		{
			sound.append(Sound.MOONSOUND.getDisplayName()).append(SEPARATOR);
		}
		if(game.isMIDI())
		{
			sound.append(Sound.MIDI.getDisplayName()).append(SEPARATOR);
		}

		if(sound.length() > 0)
		{
			sound.setLength(sound.length() - SEPARATOR.length());
		}

		return sound.toString();
	}

	private String getGenre(Game game)
	{
		StringBuilder genre = new StringBuilder("");

		if(game.getGenre1() != null && !game.getGenre1().equals(Genre.UNKNOWN))
		{
			genre.append(game.getGenre1().getDisplayName());

			if(game.getGenre2() != null && !game.getGenre2().equals(Genre.UNKNOWN))
			{
				genre.append(SEPARATOR).append(game.getGenre2().getDisplayName());
			}
		}

		return genre.toString();
	}
}
