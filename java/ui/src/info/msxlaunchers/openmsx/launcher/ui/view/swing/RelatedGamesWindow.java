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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JTextFieldBorderless;
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
	private final String gameName;
	private final List<RelatedGame> relatedGames;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final JFrame mainWindow;
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

	public RelatedGamesWindow(RelatedGamesPresenter presenter, String gameName, List<RelatedGame> relatedGames, Language language, boolean rightToLeft)
	{
		this.presenter = presenter;
		this.gameName = gameName;
		this.relatedGames = relatedGames;
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

		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel titlePanel = new JPanel();
		JPanel closeButtonPanel = new JPanel();

		titlePanel.add(new JLabel(messages.get("RELATED_TO") + ": " + gameName));
		closeButton.setPreferredSize(CLOSE_BUTTON_SIZE);
		closeButtonPanel.add(titlePanel);
		closeButtonPanel.add(closeButton);

		if(rightToLeft)
		{
			titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			closeButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			topPanel.add(titlePanel, BorderLayout.EAST);
			topPanel.add(closeButtonPanel, BorderLayout.WEST);
		}
		else
		{
			titlePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			closeButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			topPanel.add(titlePanel, BorderLayout.WEST);
			topPanel.add(closeButtonPanel, BorderLayout.EAST);
		}

		contentPane.add(topPanel, BorderLayout.NORTH);

		JPanel table = new JPanel();
		table.setLayout(new BoxLayout(table, BoxLayout.Y_AXIS));

		if(!relatedGames.isEmpty())
		{
			for(RelatedGame relatedGame: relatedGames)
			{
				JPanel rowPanel = new JPanel();
				rowPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

				JLabel screenshotLabel = new JLabel(getScreenshot(relatedGame.getMSXGenId()));
				rowPanel.add(screenshotLabel);

				JPanel namePanel = new JPanel(DATA_LAYOUT);
				JTextFieldBorderless nameLabel = new JTextFieldBorderless(relatedGame.getGameName());
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
				if(presenter.isGenerationMSXIdValid(relatedGame))
				{
					JPanel msxGenerationPanel = new JPanel();
					msxGenerationPanel.setBackground(MSX_GENERATION_BACKGROUND_COLOR);
					msxGenerationPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
					msxGenerationPanel.add(HyperLink.address(presenter.getGenerationMSXURL(relatedGame)).label("Generation-MSX")
							.linkColor(Color.WHITE).noUnderline().bold().size(10).build());
					iconsPanel.add(msxGenerationPanel);
					iconsPanel.add(Box.createHorizontalStrut(5));
				}
				iconsPanel.add(HyperLink.address(presenter.getYouTubeURL(relatedGame.getGameName()))
						.icon(Icons.YOUTUBE.getImageIcon())
						.tooltip(messages.get("SEARCH_ON_YOUTUBE"))
						.build());
				dataPanel.add(iconsPanel);

				rowPanel.add(dataPanel);

				table.add(rowPanel);
			}
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.getVerticalScrollBar().setUnitIncrement(16);
			scrollPane.setPreferredSize(new Dimension(SCROLL_PANE_WIDTH, Math.min(5, relatedGames.size()) * SCROLL_PANE_UNIT_HEIGHT));
			contentPane.add(scrollPane, BorderLayout.SOUTH);
		}
		else
		{
			JPanel noResultsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
			JLabel noResultsLabel = new JLabel(messages.get("NO_RESULTS"));
			noResultsPanel.add(noResultsLabel);
			contentPane.add(noResultsPanel, BorderLayout.SOUTH);
		}

		pack();
		setLocationRelativeTo(mainWindow);
		greyoutBackground();
		setVisible(true);
	}

	private ImageIcon getScreenshot(int msxGenId)
	{
		Path path = presenter.getScreenshotPath(msxGenId);
		if(path.toFile().exists())
		{
			return getScaledImage(new ImageIcon(path.toString()));
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
			reenableBackground();
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

	//the following two methods can be moved to a more common place if they need to be reused in other parts of the application
	private void greyoutBackground()
	{
		JPanel glass = new JPanel() {
			@Override
			public void paintComponent(Graphics g)
			{
				g.setColor(new Color(0, 0, 0, 80));
				g.fillRect(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
			}
		};

		glass.setOpaque(false);
		mainWindow.setGlassPane(glass);
		glass.setVisible(true);
	}

	private void reenableBackground()
	{
		mainWindow.getGlassPane().setVisible(false);
	}
}
