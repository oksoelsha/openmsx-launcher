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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.RelatedGamesPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.AbstractActionButton;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.HyperLink;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

/**
 * @since v1.13
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class RelatedGamesWindow extends JDialog implements ActionListener
{
	private final RelatedGamesPresenter presenter;
	private final List<RelatedGame> relatedGames;
	private final String screenshotsPath;
	private final String generationMSXURL;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component mainWindow;
	private final ImageIcon noScreenshot;

	private JComponent closeButton;

	private static final Dimension CLOSE_BUTTON_SIZE = new Dimension(20, 19);
	private static final Font NAME_FONT = new Font(null, Font.PLAIN, 17);
	private static final Font INFO_FONT = new Font(null, Font.PLAIN, 11);
	private static final int SCROLL_PANE_WIDTH = 560;
	private static final int SCROLL_PANE_UNIT_HEIGHT = 104;
	private static final int SCREENSHOT_WIDTH = 102;
	private static final int SCREENSHOT_HEIGHT = 90;
	private static final FlowLayout DATA_LAYOUT = new FlowLayout(FlowLayout.LEADING, 0, 1);
	private static final Color MSX_GENERATION_BACKGROUND_COLOR = new Color(90, 90, 220);

	public RelatedGamesWindow(RelatedGamesPresenter presenter, List<RelatedGame> relatedGames, String screenshotsPath, String generationMSXURL,
			Language language, boolean rightToLeft)
	{
		this.presenter = presenter;
		this.relatedGames = relatedGames;
		this.screenshotsPath = screenshotsPath;
		this.generationMSXURL = generationMSXURL;
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
		this.rightToLeft = rightToLeft;
		this.mainWindow = GlobalSwingContext.getIntance().getMainWindow();
		this.noScreenshot = getScaledImage(Icons.NO_SCREENSHOT.getImageIcon());

		closeButton = new CloseButton(this);
	}

	public void displayScreen()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setUndecorated(true);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BorderLayout());

		closeButton.setPreferredSize(CLOSE_BUTTON_SIZE);
		JPanel closeButtonPanel = new JPanel();
		if(rightToLeft)
		{
			closeButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		}
		else
		{
			closeButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		}
		closeButtonPanel.add(closeButton);
		contentPane.add(closeButtonPanel, BorderLayout.NORTH);

		JPanel table = new JPanel();
		table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));
		for(RelatedGame relatedGame: relatedGames)
		{
			JPanel rowPanel = new JPanel();
			rowPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

			JLabel screenshotLabel = new JLabel(getScreenshot(relatedGame.getMSXGenId()));
			rowPanel.add(screenshotLabel);

			JPanel namePanel = new JPanel(DATA_LAYOUT);
			JLabel nameLabel = new JLabel(relatedGame.getGameName());
			nameLabel.setFont(NAME_FONT);
			namePanel.add(nameLabel);

			JPanel infoPanel =  new JPanel(DATA_LAYOUT);
			JLabel infoLabel = new JLabel(relatedGame.getCompany() + " " + relatedGame.getYear());
			infoLabel.setFont(INFO_FONT);
			infoPanel.add(infoLabel);

			JPanel dataPanel = new JPanel();
			dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
			dataPanel.add(namePanel);
			dataPanel.add(infoPanel);
			JPanel iconsPanel = new JPanel(DATA_LAYOUT);
			if(relatedGame.getMSXGenId() > 0 && relatedGame.getMSXGenId() < 10000)
			{
				JPanel msxGenerationPanel = new JPanel();
				msxGenerationPanel.setBackground(MSX_GENERATION_BACKGROUND_COLOR);
				msxGenerationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
				msxGenerationPanel.add(HyperLink.address(generationMSXURL + relatedGame.getMSXGenId()).label("MSX Generation")
						.linkColor(Color.WHITE).noUnderline().bold().size(10).build());
				iconsPanel.add(msxGenerationPanel);
				iconsPanel.add(Box.createHorizontalStrut(5));
			}
			iconsPanel.add(HyperLink.address(presenter.getYouTubeURL(relatedGame.getGameName())).icon(Icons.YOUTUBE.getImageIcon()).build());
			dataPanel.add(iconsPanel);

			rowPanel.add(dataPanel);

			table.add(rowPanel);
		}
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, Math.min(5, relatedGames.size()) * SCROLL_PANE_UNIT_HEIGHT));

		contentPane.add(scrollPane, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(mainWindow);
		setVisible(true);
	}

	private ImageIcon getScreenshot(int msxGenId)
	{
		File screenshot = new File(screenshotsPath, msxGenId + "b.png");
		if(screenshot.exists())
		{
			return getScaledImage(new ImageIcon(screenshot.getAbsolutePath()));
		}
		else
		{
			return noScreenshot;
		}
	}

	private ImageIcon getScaledImage(ImageIcon imageIcon)
	{
		BufferedImage bufferedImage = new BufferedImage(SCREENSHOT_WIDTH, SCREENSHOT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(imageIcon.getImage(), 0, 0, SCREENSHOT_WIDTH, SCREENSHOT_HEIGHT, null);
		g2d.dispose();

		return new ImageIcon(bufferedImage);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == closeButton)
		{
			dispose();
		}
	}

	private static class CloseButton extends AbstractActionButton
	{
		private static final Color CLOSE_COLOR = new Color(235, 235, 235);
		private static final Color NORMAL_BUTTON_BG_COLOR = UIManager.getLookAndFeelDefaults().getColor("Panel.background");
		private static final Color HOVER_BUTTON_BG_COLOR = new Color(207, 37, 37);
		private static final Color PRESSED_BUTTON_BG_COLOR = new Color(157, 157, 157);
		private static final int X_LENGTH = 12;

		CloseButton(final ActionListener listener)
		{
			super(listener, NORMAL_BUTTON_BG_COLOR, HOVER_BUTTON_BG_COLOR, PRESSED_BUTTON_BG_COLOR);
		}

		@Override
		protected void drawButton(Graphics g)
		{
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(CLOSE_COLOR);

			if(inside)
			{
				g.setColor(Color.WHITE);
			}
			else
			{
				g.setColor(Color.BLACK);
			}

			g.drawLine(3, 3, 3+X_LENGTH, 3+X_LENGTH);
			g.drawLine(3+X_LENGTH, 3, 3, 3+X_LENGTH);
			g.drawLine(4, 3, 4+X_LENGTH, 3+X_LENGTH);
			g.drawLine(4+X_LENGTH, 3, 4, 3+X_LENGTH);
		}
	}
}
