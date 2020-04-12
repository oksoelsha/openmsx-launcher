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
package info.msxlaunchers.openmsx.launcher.ui.view.swing;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import info.msxlaunchers.openmsx.common.OSUtils;
import info.msxlaunchers.openmsx.launcher.data.feed.FeedMessage;
import info.msxlaunchers.openmsx.launcher.data.filter.Filter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterFactory;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterType;
import info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem;
import info.msxlaunchers.openmsx.launcher.data.game.constants.MSXGeneration;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Sound;
import info.msxlaunchers.openmsx.launcher.data.repository.constants.Company;
import info.msxlaunchers.openmsx.launcher.data.repository.constants.Country;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.GameLabel;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.AbstractActionButton;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.HyperLink;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JCompositeLabel;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JLabelTransitionedEnabledDisabledImage;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JLabelTransitionedNewImage;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JListWithImagesAndActions;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JMenuItemWithIcon;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JSearchTextField;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.SearchFieldHandler;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;
import net.iharder.dnd.FileDrop;

/**
 * Main window class
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener, WindowFocusListener, SearchFieldHandler
{
	private final MainPresenter presenter;

	private Set<String> databases;
	private String currentDatabase;
	private boolean showUpdateAllDatabases;

	private JPanel contentPane;
	private JMenuBar topMenuBar;
	private JMenu optionsMenu;
	private JMenuItem optionsSettings;
	private JMenu actionsMenu;
	private JMenuItem actionsCreateEmptyDatabase;
	private JMenuItem actionsFillDatabase;
	private JMenuItem actionsChangeMachine;
	private JMenuItem actionsUpdateAllDatabase;
	private JMenuItem actionsImportBlueMSXLauncherDatabases;
	private JMenu toolsMenu;
	private JMenuItem databaseManager;
	private JMenuItem activityViewer;
	private JMenuItem patcher;
	private JMenuItem lhaDecompressor;
	private JMenu helpMenu;
	private JMenuItem helpAbout;
	private JMenuItem helpFile;
	private JMenuItem helpCheckForUpdates;
	private JListWithImagesAndActions gameList;
	private JComponent databaseSelectButton;
	private JCompositeLabel databaseLabel;
	private JCompositeLabel totalLabel;
	private JComponent filtersSelectButton;
	private JCompositeLabel filtersLabel;
	private JButton favoritesButton;
	private JButton searchButton;
	private JButton feedButton;
	private JButton launchButton;
	private JButton removeButton;
	private JButton addButton;
	private JButton editButton;

	private JPopupMenu contextMenu;
	private JMenuItem moveMenuItem;
	private JMenuItem locateFileMenuItem;
	private JMenuItem addFavoriteMenuItem;
	private JMenuItem findRelatedMenuItem;
	private JMenuItem infoMenuItem;
	private JMenuItem propertiesMenuItem;

	private Map<String,String> messages;

	private JLabel screenshot1Label;
	private JLabel screenshot2Label;

	private JPopupMenu favoritesContextMenu;

	private JPopupMenu filtersContextMenu;
	private JMenu quickFilterMenuItemList;
	private JMenu companyQuickFilterMenuItemList;
	private JMenu yearQuickFilterMenuItemList;
	private JMenu countryQuickFilterMenuItemList;
	private JMenu mediumQuickFilterMenuItemList;
	private JMenu generationQuickFilterMenuItemList;
	private JMenu soundQuickFilterMenuItemList;
	private JMenuItem newFilterMenuItem;
	private JMenuItem editCurrentUntitledFilterMenuItem;
	private JMenuItem resetFilterMenuItem;

	private JLabel soundIndicatorPSG;
	private JLabel soundIndicatorSCC;
	private JLabel soundIndicatorSCCI;
	private JLabel soundIndicatorPCM;
	private JLabel soundIndicatorMSXMusic;
	private JLabel soundIndicatorMSXAudio;
	private JLabel soundIndicatorMoonsound;
	private JLabel soundIndicatorMidi;

	private JLabel generationIndicatorMSX;
	private JLabel generationIndicatorMSX2;
	private JLabel generationIndicatorMSX2P;
	private JLabel generationIndicatorTurboR;

	private ComponentOrientation orientation = null;
	private int popupMenuOrientation = 0;
	private Insets favoritesMenuInsets = null;

	private String removeConfirmationMessage = null;
	private String updateAllDatabasesConfirmationMessage = null;

	private Map<Medium, ImageIcon> mediaIconsMap = new HashMap<>();

	public static final Dimension BUTTON_DIMENSION = new Dimension(109, 28);

	private static final Font gameFont = new Font(null, Font.PLAIN, 14);
	private static final Font databaseFont = new Font(null, Font.PLAIN, 9);
	private static final Color databaseColor = new Color(100, 100, 100);
	private static final Insets favoritesMenuMarginLeftToRight = new Insets(-2, 0, 22, 250);
	private static final Insets favoritesMenuMarginRightToLeft = new Insets(-2, -12, 22, 260);
	private static final Dimension favoritesLabelSize = new Dimension(300, 18);
	private static final Dimension favoriteMenuItemSizeOnMac = new Dimension(363, 40);

	private static final Color DEFAULT_BUTTON_BG_COLOR = UIManager.getLookAndFeelDefaults().getColor("Button.background");
	private static final Insets POPUP_MENU_ITEM_BUTTON_INSETS = new Insets(-1, 0, -1, -13);
	private static final Dimension POPUP_MENU_ITEM_BUTTON_DIMENSION = new Dimension(18, 18);
	public static final Color POPUP_MENU_ITEM_BUTTON_HOVER_BG_COLOR = new Color(140, 140, 220);

	private static final String SPACES = "                                ";
	private static final int SEARCH_TEXT_FIELD_COLUMNS = 25;

	private static final Dimension NEWS_DATE_DIMENSION = new Dimension(42, 10);
	private static final Color NEWS_SITE_BACKGROUND_COLOR = new Color(50, 200, 50);
	private static final LayoutManager NEWS_PANEL_LAYOUT_MANAGER = new FlowLayout(FlowLayout.LEFT, 5, 1);

	private final MainWindow ref = this;

	public MainWindow(MainPresenter presenter, PlatformViewProperties platformViewProperties)
	{
		this.presenter = presenter;

		platformViewProperties.setDisplayProperties(this);

		//Initialize media icons map
		mediaIconsMap.put(Medium.ROM, Icons.MEDIA_ROM.getImageIcon());
		mediaIconsMap.put(Medium.DISK, Icons.MEDIA_DISK.getImageIcon());
		mediaIconsMap.put(Medium.TAPE, Icons.MEDIA_TAPE.getImageIcon());
		mediaIconsMap.put(Medium.HARDDISK, Icons.MEDIA_HARDDISK.getImageIcon());
		mediaIconsMap.put(Medium.LASERDISC, Icons.MEDIA_LASERDISC.getImageIcon());
		mediaIconsMap.put(Medium.SCRIPT, Icons.MEDIA_SCRIPT.getImageIcon());

		new GlobalSwingContext(this);

		new FileDrop(this, this::addDraggedGames);
	}

	public void display(Language language,
			Set<GameLabel> games,
			Set<String> databases,
			String currentDatabase,
			boolean rightToLeft,
			boolean showUpdateAllDatabases,
			boolean enableFeedButton)
	{
		this.databases = databases;
		this.currentDatabase = currentDatabase;
		this.showUpdateAllDatabases = showUpdateAllDatabases;

		//allow the Enter key to work on buttons when in focus in all dialogs
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

		contentPane = new JPanelWithBackground();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("openMSX Launcher");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowCloseAdapter());
		setResizable(false);

		//main window
		drawScreen();
		
		//setup game list context menu
		setupGameListContextMenu();

		//setup filters context menu
		setupFiltersMenu();

		//set the orientation
		if(rightToLeft)
		{
			orientation = ComponentOrientation.RIGHT_TO_LEFT;
			popupMenuOrientation = FlowLayout.LEFT;
			favoritesMenuInsets = favoritesMenuMarginRightToLeft;
		}
		else
		{
			orientation = ComponentOrientation.LEFT_TO_RIGHT;
			popupMenuOrientation = FlowLayout.RIGHT;
			favoritesMenuInsets = favoritesMenuMarginLeftToRight;
		}
		flipOrientation();

		//set language labels
		refreshLanguage(language);

		//set the database and game list
		fillGameList(currentDatabase, games, null);

		//update the count
		updateGameCount(games.size());

		addWindowFocusListener(this);

		enableSoundIndicators(false, false, false, false, false, false, false, false);
		enableGenerationIndicators(false, false, false, false);

		enableFeedButton(enableFeedButton);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void drawMenu()
	{
		topMenuBar = new JMenuBar();
		setJMenuBar(topMenuBar);

		//options menu
		optionsMenu = new JMenu();
		topMenuBar.add(optionsMenu);

		optionsSettings = new JMenuItemWithIcon();
		optionsSettings.setIcon(Icons.SETTINGS.getImageIcon());
		optionsSettings.addActionListener(event -> presenter.onRequestSettingsScreen());
		optionsMenu.add(optionsSettings);

		//actions menu
		actionsMenu = new JMenu();
		topMenuBar.add(actionsMenu);

		actionsCreateEmptyDatabase = new JMenuItemWithIcon();
		actionsCreateEmptyDatabase.addActionListener(event -> presenter.onRequestCreateEmptyDatabaseScreen());
		actionsMenu.add(actionsCreateEmptyDatabase);

		actionsFillDatabase = new JMenuItemWithIcon();
		actionsFillDatabase.setIcon(Icons.FILL_DB.getImageIcon());
		actionsFillDatabase.addActionListener(event -> onRequestFillDatabaseScreen());
		actionsMenu.add(actionsFillDatabase);

		actionsChangeMachine = new JMenuItemWithIcon();
		actionsChangeMachine.addActionListener(event -> onRequestChangeMachineScreen());
		actionsMenu.add(actionsChangeMachine);

		actionsUpdateAllDatabase = new JMenuItemWithIcon();
		//update all databases only appears if added manually to the settings file
		if(showUpdateAllDatabases)
		{
			actionsUpdateAllDatabase.addActionListener(event -> onRequestUpdateAllDatabases());
			actionsMenu.add(actionsUpdateAllDatabase);
		}

		actionsImportBlueMSXLauncherDatabases = new JMenuItemWithIcon();
		//import blueMSX Launcher databases is only for Windows
		if(OSUtils.isWindows())
		{
			actionsImportBlueMSXLauncherDatabases.addActionListener(event -> onRequestImportBlueMSXLauncherDatabasesScreen());
			actionsMenu.addSeparator();
			actionsMenu.add(actionsImportBlueMSXLauncherDatabases);
		}

		//tools menu
		toolsMenu = new JMenu();
		topMenuBar.add(toolsMenu);

		databaseManager = new JMenuItemWithIcon();
		databaseManager.addActionListener(event -> onRequestDatabaseManagerScreen());
		toolsMenu.add(databaseManager);

		activityViewer = new JMenuItemWithIcon();
		activityViewer.addActionListener(event -> onRequestActivityViewerScreen());
		toolsMenu.add(activityViewer);
		toolsMenu.addSeparator();

		patcher = new JMenuItemWithIcon();
		patcher.setIcon(Icons.PATCH.getImageIcon());
		patcher.addActionListener(event -> onRequestPatcherScreen());
		toolsMenu.add(patcher);

		lhaDecompressor = new JMenuItem();
		lhaDecompressor.addActionListener(event -> onRequestLHADecompressorScreen());
		toolsMenu.add(lhaDecompressor);

		//help menu
		helpMenu = new JMenu();
		topMenuBar.add(helpMenu);

		helpFile = new JMenuItemWithIcon();
		helpFile.setIcon(Icons.HELP.getImageIcon());
		helpFile.addActionListener(event -> onRequestHelpFile());
		helpMenu.add(helpFile);

		helpCheckForUpdates = new JMenuItemWithIcon();
		helpCheckForUpdates.addActionListener(event -> onRequestUpdatesChecker());
		helpMenu.add(helpCheckForUpdates);
		helpMenu.addSeparator();

		helpAbout = new JMenuItemWithIcon();
		//don't show the About menu item in Mac - this appears in the Mac application menu
		if(!OSUtils.isMac())
		{
			helpAbout.addActionListener(event -> presenter.onRequestAboutScreen());
			helpMenu.add(helpAbout);
		}
	}

	public void removeDatabase(String database)
	{
		if(database.equals(currentDatabase))
		{
			currentDatabase = null;
			databaseLabel.setValue(null);
			gameList.clear();
			updateGameCount(0);
			filtersSelectButton.setEnabled(false);
			applyFilter(null);
			updateFilterNameLabel(null);
		}

		//disable the database selector button if there were no databases left
		databaseSelectButton.setEnabled(!databases.isEmpty());
	}

	public void renameDatabase(String oldDatabase, String newDatabase)
	{
		if(oldDatabase.equals(currentDatabase))
		{
			databaseLabel.setValue(newDatabase);
			currentDatabase = newDatabase;
		}
	}

	private void drawScreen()
	{
		databaseSelectButton = new ExpandMenuButton(this);
		databaseLabel = new JCompositeLabel(172, 131, false);
		totalLabel = new JCompositeLabel(50, 37, true);

		//disable the database selector button if there were no databases left
		databaseSelectButton.setEnabled(!databases.isEmpty());

		gameList = new JListWithImagesAndActions(new DefaultListModel<Object>());
		//need to unregister the gameList component from the ToolTipManager to allow the Ctrl+F1 to work
		ToolTipManager.sharedInstance().unregisterComponent(gameList);
		gameList.registerKeyboardAction(event -> moveSelectedGames(), getCtrlXKeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> showPropertiesOfSelectedGame(), getCtrlF1KeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> locateMainFileOfSelectedGame(), getCtrlShiftFKeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> editGame(), getCtrlEKeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> addSelectedGameToFavorites(), getCtrlDKeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> viewGameInfo(), getF1KeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> processShowSearchScreenRequest(), getCtrlFKeyStroke(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		gameList.registerKeyboardAction(event -> processShowDatabasesMenuRequest(), getCtrlQKeyStroke(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		gameList.registerKeyboardAction(event -> processShowFavoritesMenuRequest(), getCtrlIKeyStroke(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		gameList.registerKeyboardAction(event -> processShowFiltersMenuRequest(), getCtrlLKeyStroke(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		gameList.registerKeyboardAction(event -> applyFilter(null), getCtrlRKeyStroke(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		gameList.registerKeyboardAction(event -> findRelated(), getCtrlShiftRKeyStroke(), JComponent.WHEN_FOCUSED);

        JScrollPane gameListScrollBar = new JScrollPane(gameList);

		filtersLabel = new JCompositeLabel(118, 90, false);
		filtersSelectButton = new ExpandMenuButton(this);

		JPanel launchButtonPanel = new JPanel();
		launchButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel removeButtonPanel = new JPanel();
		removeButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel addButtonPanel = new JPanel();
		addButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel editButtonPanel = new JPanel();
		editButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		screenshot1Label = new JLabelTransitionedNewImage(Icons.NO_SCREENSHOT.getImageIcon());
		screenshot2Label = new JLabelTransitionedNewImage(Icons.NO_SCREENSHOT.getImageIcon());

		favoritesButton = new JButton();
		favoritesButton.setIcon(Icons.FAVORITE.getImageIcon());
		favoritesButton.addActionListener(this);
		favoritesButton.setFocusable(false);

		searchButton = new JButton();
		searchButton.setIcon(Icons.SEARCH.getImageIcon());
		searchButton.addActionListener(this);
		searchButton.setFocusable(false);

		feedButton = new JButton();
		feedButton.setIcon(Icons.FEED.getImageIcon());
		feedButton.addActionListener(this);
		feedButton.setFocusable(false);

		Color indicatorsPanelBackground = new Color(187, 187, 187);

		JPanel soundIndicatorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 1));
		soundIndicatorsPanel.setBackground(indicatorsPanelBackground);
		soundIndicatorPSG = new JLabelTransitionedEnabledDisabledImage(Icons.SOUND_PSG.getImageIcon());
		soundIndicatorSCC = new JLabelTransitionedEnabledDisabledImage(Icons.SOUND_SCC.getImageIcon());
		soundIndicatorSCCI = new JLabelTransitionedEnabledDisabledImage(Icons.SOUND_SCC_I.getImageIcon());
		soundIndicatorPCM = new JLabelTransitionedEnabledDisabledImage(Icons.SOUND_PCM.getImageIcon());
		soundIndicatorMSXMusic = new JLabelTransitionedEnabledDisabledImage(Icons.SOUND_MSX_MUSIC.getImageIcon());
		soundIndicatorMSXAudio = new JLabelTransitionedEnabledDisabledImage(Icons.SOUND_MSX_AUDIO.getImageIcon());
		soundIndicatorMoonsound = new JLabelTransitionedEnabledDisabledImage(Icons.SOUND_MOONSOUND.getImageIcon());
		soundIndicatorMidi = new JLabelTransitionedEnabledDisabledImage(Icons.SOUND_MIDI.getImageIcon());

		soundIndicatorsPanel.add(soundIndicatorPSG);
		soundIndicatorsPanel.add(soundIndicatorSCC);
		soundIndicatorsPanel.add(soundIndicatorSCCI);
		soundIndicatorsPanel.add(soundIndicatorPCM);
		soundIndicatorsPanel.add(soundIndicatorMSXMusic);
		soundIndicatorsPanel.add(soundIndicatorMSXAudio);
		soundIndicatorsPanel.add(soundIndicatorMoonsound);
		soundIndicatorsPanel.add(soundIndicatorMidi);

		JPanel generationIndicatorsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 1));
		generationIndicatorsPanel.setBackground(indicatorsPanelBackground);
		generationIndicatorMSX = new JLabelTransitionedEnabledDisabledImage(Icons.GENERATION_MSX.getImageIcon());
		generationIndicatorMSX2 = new JLabelTransitionedEnabledDisabledImage(Icons.GENERATION_MSX2.getImageIcon());
		generationIndicatorMSX2P = new JLabelTransitionedEnabledDisabledImage(Icons.GENERATION_MSX2P.getImageIcon());
		generationIndicatorTurboR = new JLabelTransitionedEnabledDisabledImage(Icons.GENERATION_TURBO_R.getImageIcon());

		generationIndicatorsPanel.add(generationIndicatorMSX);
		generationIndicatorsPanel.add(generationIndicatorMSX2);
		generationIndicatorsPanel.add(generationIndicatorMSX2P);
		generationIndicatorsPanel.add(generationIndicatorTurboR);

		GroupLayout groupLayout = new GroupLayout(contentPane);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(gameListScrollBar, GroupLayout.PREFERRED_SIZE, 370, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addComponent(false, databaseSelectButton, GroupLayout.PREFERRED_SIZE, 9, GroupLayout.PREFERRED_SIZE)
							.addComponent(false, databaseLabel, GroupLayout.PREFERRED_SIZE, databaseLabel.getComponentWidth(), GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(false, totalLabel, GroupLayout.PREFERRED_SIZE, totalLabel.getComponentWidth(), GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(false, filtersSelectButton, GroupLayout.PREFERRED_SIZE, 9, GroupLayout.PREFERRED_SIZE)
							.addComponent(false, filtersLabel, GroupLayout.PREFERRED_SIZE, filtersLabel.getComponentWidth(), GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addComponent(soundIndicatorsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(28)
							.addComponent(generationIndicatorsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(7)
					.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(favoritesButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(feedButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
						.addComponent(launchButtonPanel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addComponent(removeButtonPanel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addComponent(addButtonPanel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
						.addComponent(editButtonPanel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
					.addGap(13)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(screenshot1Label, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
						.addComponent(screenshot2Label, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE))
					.addGap(20))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(databaseSelectButton, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(databaseLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(totalLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(filtersSelectButton, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(filtersLabel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
							.addGap(4)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(soundIndicatorsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(generationIndicatorsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(2)
							.addComponent(gameListScrollBar, GroupLayout.PREFERRED_SIZE, 474, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(35)
									.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
											.addComponent(favoritesButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
											.addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
											.addComponent(feedButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
									.addGap(115)
									.addComponent(launchButtonPanel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(removeButtonPanel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(addButtonPanel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(editButtonPanel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(18)
									.addComponent(screenshot1Label, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
									.addGap(16)
									.addComponent(screenshot2Label, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE))))
						.addGap(20))
		);

		editButton = getIconButton(editButtonPanel, Icons.EDIT.getImageIcon());
		addButton = getIconButton(addButtonPanel, Icons.ADD.getImageIcon());
		removeButton = getIconButton(removeButtonPanel, Icons.REMOVE.getImageIcon());
		launchButton = getIconButton(launchButtonPanel, Icons.LAUNCH.getImageIcon());

		contentPane.setLayout(groupLayout);

	    ListSelectionListener listSelectionListener = new ListSelectionListener()
	    {
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent)
			{
				if( gameList.getSelectedIndex() < 0 )
				{
					//this is the case where there's no selection
					//This happens when the JList component is reloaded which causes this event to trigger
					presenter.resetAll();
				}
				else if(!listSelectionEvent.getValueIsAdjusting() )
				{
					presenter.onSelectGames(getSelectedGames());
				}
	    	}
	    };
	    gameList.addListSelectionListener(listSelectionListener);

	    ActionListener actionListener = new ActionListener()
	    {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(e.getActionCommand().equals(JListWithImagesAndActions.DOUBLE_CLICK_COMMAND) ||
						e.getActionCommand().equals(JListWithImagesAndActions.ENTER_KEY_COMMAND))
				{
					presenter.onSelectGames(getSelectedGames());
					launchGame();
				}
				else if(e.getActionCommand().equals(JListWithImagesAndActions.DELETE_KEY_COMMAND))
				{
					promptForRemovingSelection();
				}
				else if(e.getActionCommand().equals(JListWithImagesAndActions.INSERT_KEY_COMMAND))
				{
					addGame();
				}
			}
		};
		gameList.addActionListener(actionListener);

		MouseAdapter mouseAdapter = new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent me)
			{
				 if(SwingUtilities.isRightMouseButton(me))
				 {
					 int mouseSelectionIndex = gameList.locationToIndex(me.getPoint());
					 if(!isInSelection(gameList.getSelectedIndices(), mouseSelectionIndex))
					 {
						 gameList.setSelectedIndex(mouseSelectionIndex);
					 }
					 //need to disable the move menu item if there was only one database
					 moveMenuItem.setEnabled(databases.size() > 1);

					 //need to disable the add favorite, locate file and properties menu items if more than one game was selected
					 locateFileMenuItem.setEnabled(gameList.getSelectedIndices().length == 1);
					 addFavoriteMenuItem.setEnabled(gameList.getSelectedIndices().length == 1);
					 findRelatedMenuItem.setEnabled(gameList.getSelectedIndices().length == 1);
					 propertiesMenuItem.setEnabled(gameList.getSelectedIndices().length == 1);

					 contextMenu.show(gameList, me.getX(), me.getY());
				 }
			}
		};
		gameList.addMouseListener(mouseAdapter);

	}

	public void flipOrientationLeftToRight()
	{
		orientation = ComponentOrientation.LEFT_TO_RIGHT;
		popupMenuOrientation = FlowLayout.RIGHT;
		favoritesMenuInsets = favoritesMenuMarginLeftToRight;
		flipOrientation();
	}

	public void flipOrientationRightToLeft()
	{
		orientation = ComponentOrientation.RIGHT_TO_LEFT;
		popupMenuOrientation = FlowLayout.LEFT;
		favoritesMenuInsets = favoritesMenuMarginRightToLeft;
		flipOrientation();
	}

	public void refreshLanguage(Language language)
	{
		messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);

		optionsMenu.setText(messages.get("OPTIONS"));
		optionsMenu.setMnemonic(KeyStroke.getKeyStroke(messages.get("OPTIONS_MNEMONIC")).getKeyCode());
		optionsSettings.setText(messages.get("SETTINGS") + "...");
		optionsSettings.setMnemonic(KeyStroke.getKeyStroke(messages.get("SETTINGS_MNEMONIC")).getKeyCode());
		actionsMenu.setText(messages.get("ACTIONS"));
		actionsMenu.setMnemonic(KeyStroke.getKeyStroke(messages.get("ACTIONS_MNEMONIC")).getKeyCode());
		actionsCreateEmptyDatabase.setText(messages.get("CREATE_EMPTY_DATABASE") + "...");
		actionsCreateEmptyDatabase.setMnemonic(KeyStroke.getKeyStroke(messages.get("CREATE_EMPTY_DATABASE_MNEMONIC")).getKeyCode());
		actionsFillDatabase.setText(messages.get("FILL_DATABASE") + "...");
		actionsFillDatabase.setMnemonic(KeyStroke.getKeyStroke(messages.get("FILL_DATABASE_MNEMONIC")).getKeyCode());
		actionsChangeMachine.setText(messages.get("CHANGE_MACHINE") + "...");
		actionsChangeMachine.setMnemonic(KeyStroke.getKeyStroke(messages.get("CHANGE_MACHINE_MNEMONIC")).getKeyCode());
		actionsUpdateAllDatabase.setText(messages.get("UPDATE_ALL_DATABASES"));
		actionsUpdateAllDatabase.setMnemonic(KeyStroke.getKeyStroke(messages.get("UPDATE_ALL_DATABASES_MNEMONIC")).getKeyCode());
		actionsImportBlueMSXLauncherDatabases.setText(messages.get("IMPORT_BLUEMSXLAUNCHER_DATABASES"));
		actionsImportBlueMSXLauncherDatabases.setMnemonic(KeyStroke.getKeyStroke(messages.get("IMPORT_BLUEMSXLAUNCHER_DATABASES_MNEMONIC")).getKeyCode());
		toolsMenu.setText(messages.get("TOOLS"));
		toolsMenu.setMnemonic(KeyStroke.getKeyStroke(messages.get("TOOLS_MNEMONIC")).getKeyCode());
		databaseManager.setText(messages.get("DATABASE_MANAGER"));
		databaseManager.setMnemonic(KeyStroke.getKeyStroke(messages.get("DATABASE_MANAGER_MNEMONIC")).getKeyCode());
		activityViewer.setText(messages.get("ACTIVITY_VIEWER"));
		activityViewer.setMnemonic(KeyStroke.getKeyStroke(messages.get("ACTIVITY_VIEWER_MNEMONIC")).getKeyCode());
		patcher.setText(messages.get("PATCH_CENTER") + "...");
		patcher.setMnemonic(KeyStroke.getKeyStroke(messages.get("PATCH_CENTER_MNEMONIC")).getKeyCode());
		lhaDecompressor.setText(messages.get("LHA_DECOMPRESSOR") + "...");
		lhaDecompressor.setMnemonic(KeyStroke.getKeyStroke(messages.get("LHA_DECOMPRESSOR_MNEMONIC")).getKeyCode());
		helpMenu.setText(messages.get("HELP"));
		helpMenu.setMnemonic(KeyStroke.getKeyStroke(messages.get("HELP_MNEMONIC")).getKeyCode());
		helpFile.setText(messages.get("HELP"));
		helpFile.setMnemonic(KeyStroke.getKeyStroke(messages.get("HELP_MNEMONIC")).getKeyCode());
		helpCheckForUpdates.setText(messages.get("CHECK_FOR_UPDATES") + "...");
		helpCheckForUpdates.setMnemonic(KeyStroke.getKeyStroke(messages.get("CHECK_FOR_UPDATES_MNEMONIC")).getKeyCode());
		helpAbout.setText(messages.get("ABOUT"));
		helpAbout.setMnemonic(KeyStroke.getKeyStroke(messages.get("ABOUT_MNEMONIC")).getKeyCode());
		databaseLabel.setTitle(messages.get("DATABASE"));
		totalLabel.setTitle(messages.get("COUNT"));
		favoritesButton.setToolTipText(messages.get("FAVORITES"));
		searchButton.setToolTipText(messages.get("SEARCH"));
		feedButton.setToolTipText(messages.get("FEED"));
		filtersLabel.setTitle(messages.get("FILTERS"));
		launchButton.setToolTipText(messages.get("LAUNCH"));
		removeButton.setToolTipText(messages.get("REMOVE"));
		addButton.setToolTipText(messages.get("ADD"));
		editButton.setToolTipText(messages.get("EDIT"));

		moveMenuItem.setText(messages.get("MOVE") + "...");
		locateFileMenuItem.setText(messages.get("LOCATE_FILE"));
		addFavoriteMenuItem.setText(messages.get("ADD_FAVORITE"));
		findRelatedMenuItem.setText(messages.get("FIND_RELATED"));
		infoMenuItem.setText(messages.get("INFO"));
		propertiesMenuItem.setText(messages.get("PROPERTIES"));

		quickFilterMenuItemList.setText(messages.get("QUICK_FILTER"));
		companyQuickFilterMenuItemList.setText(messages.get("COMPANY"));
		yearQuickFilterMenuItemList.setText(messages.get("YEAR"));
		countryQuickFilterMenuItemList.setText(messages.get("COUNTRY"));
		mediumQuickFilterMenuItemList.setText(messages.get("MEDIUM"));
		generationQuickFilterMenuItemList.setText(messages.get("GENERATION"));
		soundQuickFilterMenuItemList.setText(messages.get("SOUND"));
		newFilterMenuItem.setText(messages.get("NEW") + "...");
		editCurrentUntitledFilterMenuItem.setText(messages.get("EDIT_UNTITLED_FILTER") + "...");
		resetFilterMenuItem.setText(messages.get("RESET"));

		removeConfirmationMessage = messages.get("REMOVE_CONFIRMATION_MESSAGE");
		updateAllDatabasesConfirmationMessage = messages.get("UPDATE_ALL_DATABASES_CONFIRMATION_MESSAGE");

		completeQuickFilterMenu();
	}

	public void fillGameList(String currentDatabase, Set<GameLabel> games, String selectedGame)
	{
		this.currentDatabase = currentDatabase;
		databaseLabel.setValue(currentDatabase);
		filtersSelectButton.setEnabled(currentDatabase != null);
		gameList.clear();

		for(GameLabel gameLabel: games)
		{
			gameList.addElement(gameLabel.getName(), gameLabel.getCommany(), gameLabel.getYear(), gameLabel.getSize(), mediaIconsMap.get(gameLabel.getMedium()));
		}

		if(selectedGame != null)
		{
			gameList.setSelectedValue(selectedGame);
		}
		else
		{
			enableButtons(false, false, currentDatabase != null, false, false);
		}

		//disable the database selector button if there were no databases left
		databaseSelectButton.setEnabled(!databases.isEmpty());
	}

	public void updateGameCount(int total)
	{
		totalLabel.setValue(String.valueOf(total));
	}

	/*
	 * screenshot1: string pointing to Screenshot1 - if null both screenshots will show noscreenshot
	 * screenshot2: string pointing to Screenshot2 - if null screenshot2 will show noscreenshot
	 */
	public void showGameScreenshot(String screenshot1, String screenshot2)
	{
		if(screenshot1 == null)
		{
			screenshot1Label.setIcon(Icons.NO_SCREENSHOT.getImageIcon());
			screenshot2Label.setIcon(Icons.NO_SCREENSHOT.getImageIcon());
		}
		else
		{
			screenshot1Label.setIcon(new ImageIcon(screenshot1));

			if(screenshot2 == null)
			{
				screenshot2Label.setIcon(Icons.NO_SCREENSHOT.getImageIcon());
			}
			else
			{
				screenshot2Label.setIcon(new ImageIcon(screenshot2));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();

		if(source == launchButton)
		{
			launchGame();
		}
		else if(source == addButton)
		{
			addGame();
		}
		else if(source == editButton)
		{
			editGame();
		}
		else if(source == removeButton)
		{
			promptForRemovingSelection();
		}
		else if(source == moveMenuItem)
		{
			moveSelectedGames();
		}
		else if(source == locateFileMenuItem)
		{
			locateMainFileOfSelectedGame();
		}
		else if(source == addFavoriteMenuItem)
		{
			addSelectedGameToFavorites();
		}
		else if(source == findRelatedMenuItem)
		{
			findRelated();
		}
		else if(source == infoMenuItem)
		{
			viewGameInfo();
		}
		else if(source == propertiesMenuItem)
		{
			showPropertiesOfSelectedGame();
		}
		else if(source == favoritesButton)
		{
			processShowFavoritesMenuRequest();
		}
		else if(source == searchButton)
		{
			processShowSearchScreenRequest();
		}
		else if(source == feedButton)
		{
			processShowNewsListRequest();
			indicateNewNews(false);
		}
		else if(source == filtersSelectButton)
		{
			processShowFiltersMenuRequest();
		}
		else if(source == databaseSelectButton)
		{
			processShowDatabasesMenuRequest();
		}
		else if(source == newFilterMenuItem)
		{
			//reset first then process the new filter request
			applyFilter(null);
			addFilter();
		}
		else if(source == editCurrentUntitledFilterMenuItem)
		{
			try
			{
				presenter.onRequestEditFilterScreen(null);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
			}
		}
		else if(source == resetFilterMenuItem)
		{
			applyFilter(null);
		}
		else if(source instanceof JMenuItem)
		{
			Action action = ((JMenuItem)source).getAction();
			if(action instanceof FilterMenuItemName)
			{
				applyFilter(((PopupMenuItemName)action).getPopupMenuItemName());
			}
			else if(action instanceof FavoriteMenuItemName)
			{
				selectGame(((FavoriteMenuItemName)action).favorite);
			}
			else if(action instanceof DatabaseMenuItemName)
			{
				onSelectDatabase(((PopupMenuItemName)action).getPopupMenuItemName());
			}
			else if(action instanceof QuickFilterMenuItemName)
			{
				applyQuickFilter(((QuickFilterMenuItemName)action).filter);
			}
			gameList.requestFocusInWindow();
		}
		else if(source instanceof JButton)
		{
			Action action = ((JButton)source).getAction();
			if(action instanceof FilterMenuItemDelete)
			{
				filtersContextMenu.setVisible(false);
				promptForDeletingFilter(((PopupMenuItemDelete)action).getPopupMenuItemName());
			}
			else if(action instanceof FilterMenuItemEdit)
			{
				filtersContextMenu.setVisible(false);
				try
				{
					String filterName = ((PopupMenuItemEdit)action).getPopupMenuItemName();
					applyFilter(filterName);
					presenter.onRequestEditFilterScreen(filterName);
				}
				catch(LauncherException le)
				{
					MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
				}
			}
			else if(action instanceof FavoriteMenuItemDelete)
			{
				favoritesContextMenu.setVisible(false);
				try
				{
					presenter.onRequestDeleteFavoriteAction(((FavoriteMenuItemDelete)action).favorite);
					gameList.requestFocusInWindow();
				}
				catch(LauncherException le)
				{
					MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
				}
			}
		}
	}

	public void showDatabasesList(Set<String> databases)
	{
		JPopupMenu databasesContextMenu = new JPopupMenu();
		databasesContextMenu.setComponentOrientation(orientation);

		for(String database:databases)
		{
			JMenuItem databaseMenuItem = new JMenuItem();
			databaseMenuItem.setComponentOrientation(orientation);
			databaseMenuItem.setAction(new DatabaseMenuItemName(database));
			databaseMenuItem.addActionListener(this);

			databasesContextMenu.add(databaseMenuItem);
		}

		showButtonMenu(databaseSelectButton, databasesContextMenu, 0);
	}

	public void showFavoritesMenu(Set<DatabaseItem> favorites)
	{
		favoritesContextMenu = new JPopupMenu();
		favoritesContextMenu.setBorder(new EmptyBorder(0, 0, 0, 0));

		for(DatabaseItem favorite:favorites)
		{
			JMenuItem favoriteMenuItem = new JMenuItem();
			favoriteMenuItem.setAction(new FavoriteMenuItemName(favorite));
			favoriteMenuItem.addActionListener(this);
			favoriteMenuItem.setMargin(favoritesMenuInsets);
			favoriteMenuItem.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

			JPanel menuItemPanel = new JPanel();
			menuItemPanel.setComponentOrientation(orientation);
			menuItemPanel.setOpaque(false);

			JPanel labelsPanel = new JPanel();
			labelsPanel.setOpaque(false);
			labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));

			JLabel gameName = new JLabel(favorite.getGameName());
			gameName.setFont(gameFont);
			gameName.setPreferredSize(favoritesLabelSize);
			labelsPanel.add(gameName);

			JLabel databaseName = new JLabel("- " + favorite.getDatabase());
			databaseName.setFont(databaseFont);
			databaseName.setForeground(databaseColor);
			databaseName.setPreferredSize(favoritesLabelSize);
			labelsPanel.add(databaseName);
			menuItemPanel.add(labelsPanel);

			JPanel buttonPanel = new JPanel();
			buttonPanel.setOpaque(false);
		    JButton deleteButton = new JButton();
		    buttonPanel.add(deleteButton);
		    deleteButton.setAction(new FavoriteMenuItemDelete(favorite));
		    deleteButton.addActionListener(this);
		    deleteButton.setToolTipText(messages.get("DELETE"));
		    menuItemPanel.add(buttonPanel);
		    if(OSUtils.isMac())
		    {
		    	favoriteMenuItem.setPreferredSize(favoriteMenuItemSizeOnMac);
		    }
			favoriteMenuItem.add(menuItemPanel);

		    favoritesContextMenu.add(favoriteMenuItem);			
		}

		showButtonMenu(favoritesButton, favoritesContextMenu, 2);
	}

	public void higlightGame(String gameName)
	{
		gameList.setSelectedValue(gameName);
	}

	public void showFiltersMenu(Set<String> filterNames, boolean isFilterSelected, boolean isEditCurrentUntitledFilter)
	{
		filtersContextMenu = new JPopupMenu();
		filtersContextMenu.setComponentOrientation(orientation);

		for(String filterName: filterNames)
		{
		    JMenuItem filterMenuItem = new JMenuItem();
		    filterMenuItem.setComponentOrientation(orientation);
		    filterMenuItem.setAction(new FilterMenuItemName(filterName));
		    filterMenuItem.addActionListener(this);

		    filterMenuItem.setMargin(POPUP_MENU_ITEM_BUTTON_INSETS);
		    filterMenuItem.setLayout(new FlowLayout(popupMenuOrientation, 0, 0));

		    final JButton editButton = new JButton();
		    editButton.setContentAreaFilled(false);
		    editButton.setPreferredSize(POPUP_MENU_ITEM_BUTTON_DIMENSION);
		    editButton.setAction(new FilterMenuItemEdit(filterName));
		    editButton.addActionListener(this);
		    editButton.setToolTipText(messages.get("EDIT"));
		    addFilterButtonHoverBehavior(editButton);
		    filterMenuItem.add(editButton);

		    JButton deleteButton = new JButton();
		    deleteButton.setContentAreaFilled(false);
		    deleteButton.setPreferredSize(POPUP_MENU_ITEM_BUTTON_DIMENSION);
		    deleteButton.setAction(new FilterMenuItemDelete(filterName));
		    deleteButton.addActionListener(this);
		    deleteButton.setToolTipText(messages.get("DELETE"));
		    addFilterButtonHoverBehavior(deleteButton);
		    filterMenuItem.add(deleteButton);

		    filtersContextMenu.add(filterMenuItem);
		}

		if(!filterNames.isEmpty())
		{
			filtersContextMenu.addSeparator();
		}

		if(isFilterSelected || isEditCurrentUntitledFilter)
		{
			filtersContextMenu.add(resetFilterMenuItem);
			filtersContextMenu.addSeparator();
		}

		if(isEditCurrentUntitledFilter)
		{
			filtersContextMenu.add(editCurrentUntitledFilterMenuItem);
		}
		filtersContextMenu.add(quickFilterMenuItemList);
		quickFilterMenuItemList.add(companyQuickFilterMenuItemList);
		quickFilterMenuItemList.add(yearQuickFilterMenuItemList);
		quickFilterMenuItemList.add(countryQuickFilterMenuItemList);
		quickFilterMenuItemList.add(mediumQuickFilterMenuItemList);
		quickFilterMenuItemList.add(generationQuickFilterMenuItemList);
		quickFilterMenuItemList.add(soundQuickFilterMenuItemList);

		filtersContextMenu.add(newFilterMenuItem);

		showButtonMenu(filtersSelectButton, filtersContextMenu, 0);
	}

	public void updateFilterNameLabel(String filterName)
	{
		filtersLabel.setValue(filterName);
	}

	public void setFilterNameLabelUntitled()
	{
		filtersLabel.setValue("(" + messages.get("UNTITLED_FILTER") + ")");
	}

	public void setFiltersToolTip(List<String> filterMonikers)
	{
		String descriptions;

		if(!filterMonikers.isEmpty())
		{
			List<String> filterDescriptions = FilterUtils.getFiltersStringRepresentation(filterMonikers, messages);
			StringBuilder builder = new StringBuilder("<html>");
			builder.append(filterDescriptions.stream().collect(Collectors.joining("<br>")));
			descriptions = builder.append("</html>").toString();
		}
		else
		{
			descriptions = null;
		}

		filtersLabel.setToolTipText(descriptions);
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.component.SearchFieldHandler#getSearchMatches(java.lang.String)
	 */
	@Override
	public Set<DatabaseItem> getSearchMatches(String searchString)
	{
		return presenter.onRequestSearchMatches(searchString);
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.component.SearchFieldHandler#handleSearchSelection(info.msxlaunchers.openmsx.launcher.data.game.DatabaseItem)
	 */
	@Override
	public void handleSearchSelection(DatabaseItem searchSelection)
	{
		selectGame(searchSelection);
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0)
	{
		gameList.requestFocusInWindow();
	}

	@Override
	public void windowLostFocus(WindowEvent arg0)
	{
		//just ignore
	}

	public void enableButtons(boolean launchFlag, boolean removeFlag, boolean addFlag, boolean editFlag, boolean infoFlag)
	{
		launchButton.setEnabled(launchFlag);
		removeButton.setEnabled(removeFlag);
		addButton.setEnabled(addFlag);
		editButton.setEnabled(editFlag);
		infoMenuItem.setEnabled(infoFlag);
	}

	public void enableSoundIndicators(boolean psgFlag, boolean sccFlag, boolean scciFlag, boolean pcmFlag,
			boolean msxMusicFlag, boolean msxAudioFlag, boolean moonsoundFlag, boolean midiFlag)
	{
		soundIndicatorPSG.setEnabled(psgFlag);
		soundIndicatorSCC.setEnabled(sccFlag);
		soundIndicatorSCCI.setEnabled(scciFlag);
		soundIndicatorPCM.setEnabled(pcmFlag);
		soundIndicatorMSXMusic.setEnabled(msxMusicFlag);
		soundIndicatorMSXAudio.setEnabled(msxAudioFlag);
		soundIndicatorMoonsound.setEnabled(moonsoundFlag);
		soundIndicatorMidi.setEnabled(midiFlag);
	}

	public void enableGenerationIndicators(boolean msxFlag, boolean msx2Flag, boolean msx2pFlag, boolean turboRFlag)
	{
		generationIndicatorMSX.setEnabled(msxFlag);
		generationIndicatorMSX2.setEnabled(msx2Flag);
		generationIndicatorMSX2P.setEnabled(msx2pFlag);
		generationIndicatorTurboR.setEnabled(turboRFlag);
	}

	public void enableFeedButton(boolean enable)
	{
		feedButton.setEnabled(enable);
	}

	public void indicateNewNews(boolean enable)
	{
		if( enable )
		{
			feedButton.setIcon(Icons.FEED_NEW.getImageIcon());
		}
		else
		{
			feedButton.setIcon(Icons.FEED.getImageIcon());
		}
	}

	public void showFeedMenu(List<FeedMessage> feedMessages)
	{
		JPopupMenu feedMessagesContextMenu = new JPopupMenu();
		feedMessagesContextMenu.setComponentOrientation(orientation);

		for(int index = 0; index < feedMessages.size(); index++)
		{
			JPanel newsPanel = new JPanel();
			newsPanel.setLayout(NEWS_PANEL_LAYOUT_MANAGER);

			FeedMessage feedMessage = feedMessages.get(index);

			JLabel date = new JLabel(feedMessage.getPubDateDisplayName());
			date.setPreferredSize(NEWS_DATE_DIMENSION);
			newsPanel.add(date);

			newsPanel.add(HyperLink.label(feedMessage.getTitle()).address(feedMessage.getLink()).build());

			JPanel sitePanel = new JPanel();
			sitePanel.setBackground(NEWS_SITE_BACKGROUND_COLOR);
			sitePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
			sitePanel.add(HyperLink.label(feedMessage.getFeedSiteName()).address(feedMessage.getFeedSiteUrl())
					.linkColor(Color.white).noUnderline().bold().size(10).build());
			newsPanel.add(sitePanel);

			feedMessagesContextMenu.add(newsPanel);
		}

		showButtonMenu(feedButton, feedMessagesContextMenu, 2);
	}

	public void showFeedProcessingMessage()
	{
		JPopupMenu feedMessagesProcessingContextMenu = new JPopupMenu();
		feedMessagesProcessingContextMenu.setComponentOrientation(orientation);
		JPanel messagePanel = new JPanel();
		JLabel message = new JLabel(messages.get("FEED_PROCESSING_MESSAGE"));
		messagePanel.add(message);
		feedMessagesProcessingContextMenu.add(messagePanel);
		showButtonMenu(feedButton, feedMessagesProcessingContextMenu, 2);
	}

	private void completeQuickFilterMenu()
	{
		companyQuickFilterMenuItemList.removeAll();
		List<Company> companies = Arrays.asList(Company.values());
		Collections.sort(companies, (c1, c2) -> c1.getDisplayName().compareToIgnoreCase(c2.getDisplayName()));
		for(Company company: companies)
		{
			JMenuItem companyQuickFilterMenuItem = new JMenuItem();
			Filter filter = FilterFactory.createFilter(FilterType.COMPANY, company.getDisplayName(), null, null);
			companyQuickFilterMenuItem.addActionListener(this);
			companyQuickFilterMenuItem.setAction(new QuickFilterMenuItemName(filter));
			companyQuickFilterMenuItem.setText(company.getDisplayName());
			companyQuickFilterMenuItemList.add(companyQuickFilterMenuItem);
		}

		yearQuickFilterMenuItemList.removeAll();
		for(int year = 1982; year <= 1994; year++)
		{
			JMenuItem yearQuickFilterMenuItem = new JMenuItem();
			Filter filter = FilterFactory.createFilter(FilterType.YEAR, year + "", null, FilterParameter.EQUAL);
			yearQuickFilterMenuItem.addActionListener(this);
			yearQuickFilterMenuItem.setAction(new QuickFilterMenuItemName(filter));
			yearQuickFilterMenuItem.setText(year + "");
			yearQuickFilterMenuItemList.add(yearQuickFilterMenuItem);
		}

		countryQuickFilterMenuItemList.removeAll();
		List<String> countries = Arrays.asList(Country.values()).stream().map(Object::toString).collect(Collectors.toList());
		Collections.sort(countries, (c1, c2) -> messages.get(c1).compareToIgnoreCase(messages.get(c2)));
		for(String country: countries)
		{
			JMenuItem countryQuickFilterMenuItem = new JMenuItem();
			countryQuickFilterMenuItem.setComponentOrientation(orientation);
			Filter filter = FilterFactory.createFilter(FilterType.COUNTRY, country, null, null);
			countryQuickFilterMenuItem.addActionListener(this);
			countryQuickFilterMenuItem.setAction(new QuickFilterMenuItemName(filter));
			countryQuickFilterMenuItem.setText(messages.get(country));
			countryQuickFilterMenuItemList.add(countryQuickFilterMenuItem);
		}

		mediumQuickFilterMenuItemList.removeAll();
		for(Medium medium: Medium.values())
		{
			JMenuItem mediumQuickFilterMenuItem = new JMenuItem();
			mediumQuickFilterMenuItem.setComponentOrientation(orientation);
			Filter filter = FilterFactory.createFilter(FilterType.MEDIUM, medium.toString(), null, null);
			mediumQuickFilterMenuItem.addActionListener(this);
			mediumQuickFilterMenuItem.setAction(new QuickFilterMenuItemName(filter));
			mediumQuickFilterMenuItem.setText(messages.get(medium.toString()));
			mediumQuickFilterMenuItemList.add(mediumQuickFilterMenuItem);
		}

		generationQuickFilterMenuItemList.removeAll();
		for(MSXGeneration generation: MSXGeneration.values())
		{
			JMenuItem generationQuickFilterMenuItem = new JMenuItem();
			Filter filter = FilterFactory.createFilter(FilterType.GENERATION, generation.toString(), null, null);
			generationQuickFilterMenuItem.addActionListener(this);
			generationQuickFilterMenuItem.setAction(new QuickFilterMenuItemName(filter));
			generationQuickFilterMenuItem.setText(generation.getDisplayName());
			generationQuickFilterMenuItemList.add(generationQuickFilterMenuItem);
		}

		soundQuickFilterMenuItemList.removeAll();
		for(Sound sound: Sound.values())
		{
			JMenuItem soundQuickFilterMenuItem = new JMenuItem();
			Filter filter = FilterFactory.createFilter(FilterType.SOUND, sound.toString(), null, null);
			soundQuickFilterMenuItem.addActionListener(this);
			soundQuickFilterMenuItem.setAction(new QuickFilterMenuItemName(filter));
			soundQuickFilterMenuItem.setText(sound.getDisplayName());
			soundQuickFilterMenuItemList.add(soundQuickFilterMenuItem);
		}
}

	private void showButtonMenu(JComponent button, JPopupMenu menu, int xOffset)
	{
		int x;
		if(orientation == ComponentOrientation.RIGHT_TO_LEFT)
		{
			x = button.getWidth() - menu.getPreferredSize().width - xOffset;
		}
		else
		{
			x = xOffset;
		}
		menu.show(button, x, button.getHeight()-1);
	}

	private void onRequestFillDatabaseScreen()
	{
		try
		{
			presenter.onRequestFillDatabaseScreen();
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(ref, le, messages, orientation);
		}
	}

	private void onRequestChangeMachineScreen()
	{
		try
		{
			presenter.onRequestMachineUpdateScreen();
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(ref, le, messages, orientation);
		}
	}

	private void onRequestUpdateAllDatabases()
	{
		try
		{
			if(MessageBoxUtil.showYesNoMessageBox(ref, updateAllDatabasesConfirmationMessage, messages, orientation) == 0)
			{
				int numberUpdatedProfiles = presenter.onRequestUpdateAllDatabases();
				presenter.onViewUpdatedDatabase(currentDatabase);

				MessageBoxUtil.showInformationMessageBox(ref, messages.get("TOTAL_UPDATED_PROFILES") + ": " + numberUpdatedProfiles,
						messages, orientation);
			}
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(ref, le, messages, orientation);
		}
	}

	private void onRequestImportBlueMSXLauncherDatabasesScreen()
	{
		try
		{
			presenter.onRequestImportBlueMSXLauncherDatabasesScreen();
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(ref, le, messages, orientation);
		}
	}

	private void onRequestDatabaseManagerScreen()
	{
		try
		{
			presenter.onRequestDatabaseManagerScreen();

			//after calling the database manager screen, the last database may have been deleted.
			//in this case, disable the add and filter buttons if no database is selected
			if(currentDatabase == null)
			{
				addButton.setEnabled(false);
				filtersSelectButton.setEnabled(false);
			}
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(ref, le, messages, orientation);
		}
	}

	private void onRequestActivityViewerScreen()
	{
		presenter.onRequestActivityViewerScreen();
	}

	private void onRequestPatcherScreen()
	{
		presenter.onRequestPatcherScreen();
	}

	private void onRequestLHADecompressorScreen()
	{
		presenter.onRequestLHADecompressorScreen();
	}

	private void onRequestHelpFile()
	{
		try
		{
			presenter.onRequestHelpFile();
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(ref, le, messages, orientation);
		}
	}

	private void onRequestUpdatesChecker()
	{
		try
		{
			presenter.onRequestUpdatesChecker();
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(ref, le, messages, orientation);
		}
	}

	private void onSelectDatabase(String database)
	{
		try
		{
    		presenter.onSelectDatabase(database);
		}
		catch(LauncherException le)
		{
			//here a non-existing database was selected.
			//In this case force selection of previous valid one
			databaseLabel.setValue(currentDatabase);

			MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
		}

		currentDatabase = database;
	}

	private JButton getIconButton(JPanel buttonPanel, ImageIcon icon)
	{
		JButton button = new JButton();
		button.addActionListener(this);
		button.setIcon(icon);

		buttonPanel.setOpaque(false);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder());
		buttonPanel.setBackground(new Color(0,0,0,0));

		buttonPanel.add(button);

		return button;
	}

	private void launchGame()
	{
		try
		{
			presenter.onLaunchGame(getSelectedGame());
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
		}
	}

	private void addGame()
	{
		try
		{
			presenter.onRequestAddGameScreen();
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
		}
	}

	private void editGame()
	{
		String selectedGame = getSelectedGame();
		if(selectedGame != null )
		{
			//selected games could be null if nothing was highlighted in the game list.
			//this can happen if Ctrl+E was pressed, for example, without selecting games
			try
			{
				presenter.onRequestEditGameScreen(getSelectedGame());
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
			}
		}
	}

	private void promptForRemovingSelection()
	{
		if(MessageBoxUtil.showYesNoMessageBox(ref, removeConfirmationMessage, messages, orientation) == 0)
		{
			try
			{
				presenter.onRequestRemoveGamesAction(getSelectedGames());

				//delete from the Jlist - start with higher indices then go down
				//the reason for this is that deleting lower indices first will shift elements up so indices of later items will be different
		        int[] selections = gameList.getSelectedIndices();
		        for(int index=selections.length-1; index >= 0; index--)
		        {
		        	gameList.remove(selections[index]);
		        }
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
			}
		}
	}

	private void viewGameInfo()
	{
		String selectedGame = getSelectedGame();
		if(selectedGame != null )
		{
			//selected game could be null if nothing was highlighted in the game list.
			//this can happen if F1 was pressed, for example, without selecting a game
			try
			{
				presenter.onRequestGameInfo(selectedGame);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
			}
		}
	}

	private void moveSelectedGames()
	{
		Set<String> games = getSelectedGames();
		if(games != null)
		{
			//selected games could be null if nothing was highlighted in the game list.
			//this can happen if Ctrl+X was pressed, for example, without selecting games
			Set<String> movedGames = presenter.onRequestMoveGamesScreen(games, currentDatabase);
	
			int gamesTotal = gameList.getListSize();
			for(int index = gamesTotal - 1; index > -1; index--)
			{
				if(movedGames.contains(gameList.getElementAt(index)))
				{
					gameList.remove(index);
				}
			}
		}
	}

	private void locateMainFileOfSelectedGame()
	{
		String selectedGame = getSelectedGame();
		if(selectedGame != null )
		{
			//selected game could be null if nothing was highlighted in the game list.
			//this can happen if Ctrl+Shift+F was pressed, for example, without selecting a game
			try
			{
				presenter.onRequestLocateFile(selectedGame);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
			}
		}
	}

	private void addSelectedGameToFavorites()
	{
		String selectedGame = getSelectedGame();
		if(selectedGame != null )
		{
			//selected game could be null if nothing was highlighted in the game list.
			//this can happen if Ctrl+D was pressed, for example, without selecting a game
			try
			{
				presenter.onRequestAddFavorite(selectedGame, currentDatabase);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
			}
		}
	}

	private void findRelated()
	{
		String selectedGame = getSelectedGame();
		if(selectedGame != null )
		{
			//selected game could be null if nothing was highlighted in the game list.
			//this can happen if Ctrl+Shift+R was pressed, for example, without selecting a game
			try
			{
				presenter.onRequestFindRelated(selectedGame);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
			}
		}
	}

	private void showPropertiesOfSelectedGame()
	{
		String selectedGame = getSelectedGame();

		if(selectedGame != null)
		{
			//selected game could be null if nothing was highlighted in the game list.
			//this can happen if Ctrl+F1 was pressed, for example, without selecting a game
			presenter.onRequestShowProperties(selectedGame);
		}
	}

	private void processShowFavoritesMenuRequest()
	{
		presenter.onRequestListOfFavorites();
	}

	private void processShowFiltersMenuRequest()
	{
		if(currentDatabase != null)
		{
			presenter.onRequestListOfSavedFilters();
		}
	}

	private void processShowDatabasesMenuRequest()
	{
		presenter.onRequestDatabasesList();
	}

	private void addFilter()
	{
		try
		{
			presenter.onRequestAddFilterScreen();
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
		}
	}

	private void processShowSearchScreenRequest()
	{
		JPopupMenu searchContextMenu = new JPopupMenu();
		searchContextMenu.setComponentOrientation(orientation);

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));

		boolean gradientResultHighlight;
		if(OSUtils.isMac())
		{
			gradientResultHighlight = false;
		}
		else
		{
			gradientResultHighlight = true;
		}
		JSearchTextField searchField = new JSearchTextField(SEARCH_TEXT_FIELD_COLUMNS, searchContextMenu, this, gradientResultHighlight);
		searchPanel.add(searchField);
		searchContextMenu.add(searchPanel);

		int x;
		if(orientation == ComponentOrientation.RIGHT_TO_LEFT)
		{
			x = searchButton.getWidth() - searchContextMenu.getPreferredSize().width - 2;
		}
		else
		{
			x = 2;
		}
		searchContextMenu.show(searchButton, x, searchButton.getHeight()-1);
	}

	private void processShowNewsListRequest()
	{
		presenter.onRequestNewsList();
	}

	private static void addFilterButtonHoverBehavior(final JButton button)
	{
	    button.getModel().addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e)
	        {
	            ButtonModel model = (ButtonModel) e.getSource();
	            if (model.isRollover())
	            {
	            	button.setBackground(POPUP_MENU_ITEM_BUTTON_HOVER_BG_COLOR);
	            	button.setBorder(BorderFactory.createEtchedBorder());
	            }
	            else
	            {
	            	button.setBackground(DEFAULT_BUTTON_BG_COLOR);
	            	button.setBorder(null);
	            }
	         }
	    });
	}

	private static boolean isInSelection(int[] selections, int mouseSelection)
	{
		for(int ix = 0; ix < selections.length; ix++)
		{
			if(selections[ix] == mouseSelection)
			{
				return true;
			}
		}
		return false;
	}

	private void promptForDeletingFilter(String filterName)
	{
		if(MessageBoxUtil.showYesNoMessageBox(ref, removeConfirmationMessage, messages, orientation) == 0)
		{
			try
			{
				presenter.onRequestDeleteFilterAction(filterName);
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
			}
		}
	}

	private void flipOrientation()
	{
		//I had to put this here because of the alignment of the Arabic menu when changing the language in Settings
		drawMenu();

		topMenuBar.setComponentOrientation(orientation);
		optionsMenu.applyComponentOrientation(orientation);
		actionsMenu.applyComponentOrientation(orientation);
		toolsMenu.applyComponentOrientation(orientation);
		helpMenu.applyComponentOrientation(orientation);
		contentPane.setComponentOrientation(orientation);
		contentPane.applyComponentOrientation(orientation);
		quickFilterMenuItemList.setComponentOrientation(orientation);
		companyQuickFilterMenuItemList.setComponentOrientation(orientation);
		yearQuickFilterMenuItemList.setComponentOrientation(orientation);
		countryQuickFilterMenuItemList.setComponentOrientation(orientation);
		mediumQuickFilterMenuItemList.setComponentOrientation(orientation);
		generationQuickFilterMenuItemList.setComponentOrientation(orientation);
		soundQuickFilterMenuItemList.setComponentOrientation(orientation);
		newFilterMenuItem.setComponentOrientation(orientation);
		editCurrentUntitledFilterMenuItem.setComponentOrientation(orientation);
		resetFilterMenuItem.setComponentOrientation(orientation);
		moveMenuItem.setComponentOrientation(orientation);
		locateFileMenuItem.setComponentOrientation(orientation);
		addFavoriteMenuItem.setComponentOrientation(orientation);
		findRelatedMenuItem.setComponentOrientation(orientation);
		infoMenuItem.setComponentOrientation(orientation);
		propertiesMenuItem.setComponentOrientation(orientation);
		databaseLabel.setComponentOrientation(orientation);
		totalLabel.setComponentOrientation(orientation);
		filtersLabel.setComponentOrientation(orientation);

		//force the following to always be left to right
		gameList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	}

	private String getSelectedGame()
	{
		String gameName = null;
		Set<String> selectedGames = getSelectedGames();

		if(selectedGames != null && selectedGames.size() == 1)
		{
			gameName = selectedGames.iterator().next();
		}

		return gameName;
	}

	private Set<String> getSelectedGames()
	{
		Set<String> gameNames = null;
        int[] selections = gameList.getSelectedIndices();
        if(selections != null && selections.length > 0)
        {
	        gameNames = new HashSet<>(Arrays.asList(gameList.getSelectedItems()));
        }
        return gameNames;
	}

	private void setupGameListContextMenu()
	{
	    contextMenu = new JPopupMenu();

	    moveMenuItem = new JMenuItemWithIcon();
	    moveMenuItem.addActionListener(this);
	    moveMenuItem.setAccelerator(getCtrlXKeyStroke());
	    contextMenu.add(moveMenuItem);

	    locateFileMenuItem = new JMenuItemWithIcon();
	    locateFileMenuItem.setAccelerator(getCtrlShiftFKeyStroke());
	    locateFileMenuItem.addActionListener(this);
	    contextMenu.add(locateFileMenuItem);

	    addFavoriteMenuItem = new JMenuItemWithIcon();
	    addFavoriteMenuItem.setAccelerator(getCtrlDKeyStroke());
	    addFavoriteMenuItem.setIcon(Icons.FAVORITE.getImageIcon());
	    addFavoriteMenuItem.addActionListener(this);
	    contextMenu.add(addFavoriteMenuItem);

	    findRelatedMenuItem = new JMenuItemWithIcon();
	    findRelatedMenuItem.setAccelerator(getCtrlShiftRKeyStroke());
	    findRelatedMenuItem.addActionListener(this);
	    contextMenu.add(findRelatedMenuItem);

	    contextMenu.addSeparator();

	    infoMenuItem = new JMenuItemWithIcon();
	    infoMenuItem.addActionListener(this);
	    infoMenuItem.setAccelerator(getF1KeyStroke());
	    contextMenu.add(infoMenuItem);

	    propertiesMenuItem = new JMenuItemWithIcon();
	    propertiesMenuItem.addActionListener(this);
	    propertiesMenuItem.setAccelerator(getCtrlF1KeyStroke());
	    contextMenu.add(propertiesMenuItem);
	}

	private void setupFiltersMenu()
	{
		quickFilterMenuItemList = new JMenu();
		companyQuickFilterMenuItemList = new JMenu();
		yearQuickFilterMenuItemList = new JMenu();
		countryQuickFilterMenuItemList = new JMenu();
		mediumQuickFilterMenuItemList = new JMenu();
		generationQuickFilterMenuItemList = new JMenu();
		soundQuickFilterMenuItemList = new JMenu();

		newFilterMenuItem = new JMenuItem();
		newFilterMenuItem.addActionListener(this);

		editCurrentUntitledFilterMenuItem = new JMenuItem();
		editCurrentUntitledFilterMenuItem.addActionListener(this);

		resetFilterMenuItem = new JMenuItem();
		resetFilterMenuItem.addActionListener(this);
	}

	private void selectGame(DatabaseItem databaseItem)
	{
		try
		{
			presenter.onSelectDatabaseItem(databaseItem);
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
		}
	}

	private void applyFilter(String filterName)
	{
		try
		{
			presenter.onSelectFilter(filterName);
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
		}
	}

	private void applyQuickFilter(Filter filter)
	{
		try
		{
			presenter.onSelectQuickFilter(filter);
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
		}
	}

	private KeyStroke getCtrlXKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrlEKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrlF1KeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_F1, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrlShiftFKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | KeyEvent.SHIFT_DOWN_MASK);
	}

	private KeyStroke getCtrlDKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getF1KeyStroke()
	{
		return KeyStroke.getKeyStroke("F1");
	}

	private KeyStroke getCtrlFKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrlQKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrlIKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrlLKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrlRKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrlShiftRKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | KeyEvent.SHIFT_DOWN_MASK);
	}

	private abstract class PopupMenuAction extends AbstractAction
	{
		private final String popupMenuItemName;

		PopupMenuAction(final String popupMenuItemName)
	    {
			this.popupMenuItemName = popupMenuItemName;
	    }

		public String getPopupMenuItemName()
		{
			return popupMenuItemName;
		}

		@Override
		public void actionPerformed(ActionEvent ae)
		{
			//Nothing to do
		}
	}

	private abstract class PopupMenuItemName extends PopupMenuAction
	{
		PopupMenuItemName(String popupMenuItemName)
		{
			super(popupMenuItemName);
			putValue(Action.NAME, popupMenuItemName);

			if(orientation == ComponentOrientation.LEFT_TO_RIGHT)
			{
				putValue(Action.NAME, popupMenuItemName + SPACES);
			}
			else
			{
				putValue(Action.NAME, SPACES + popupMenuItemName);
			}
		}
	}

	private abstract class PopupMenuItemDelete extends PopupMenuAction
	{
		PopupMenuItemDelete(String popupMenuItemName)
		{
			super(popupMenuItemName);
	        putValue(Action.SMALL_ICON, Icons.DELETE_SMALL.getImageIcon());
		}
	}

	private abstract class PopupMenuItemEdit extends PopupMenuAction
	{
		PopupMenuItemEdit(String popupMenuItemName)
		{
			super(popupMenuItemName);
	        putValue(Action.SMALL_ICON, Icons.EDIT_SMALL.getImageIcon());
		}
	}

	private class FilterMenuItemName extends PopupMenuItemName
	{
		FilterMenuItemName(String filterName)
		{
			super(filterName);
		}
	}

	private class FilterMenuItemDelete extends PopupMenuItemDelete
	{
		FilterMenuItemDelete(String filterName)
		{
			super(filterName);
		}
	}

	private class FilterMenuItemEdit extends PopupMenuItemEdit
	{
		FilterMenuItemEdit(String filterName)
		{
			super(filterName);
		}
	}

	private class FavoriteMenuItemName extends PopupMenuItemName
	{
		private final DatabaseItem favorite;

		FavoriteMenuItemName(DatabaseItem favorite)
		{
			super("");
			this.favorite = favorite;
		}
	}

	private class DatabaseMenuItemName extends PopupMenuItemName
	{
		DatabaseMenuItemName(String databaseName)
		{
			super(databaseName);
		}
	}

	private class QuickFilterMenuItemName extends PopupMenuItemName
	{
		private final Filter filter;

		QuickFilterMenuItemName(Filter filter)
		{
			super("");
			this.filter = filter;
		}
	}

	private class FavoriteMenuItemDelete extends PopupMenuItemDelete
	{
		private final DatabaseItem favorite;

		FavoriteMenuItemDelete(DatabaseItem favorite)
		{
			super("");
			this.favorite = favorite;
		}
	}

	private class WindowCloseAdapter extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent we)
		{
			presenter.onRequestExit();
		}
	}

	private void addDraggedGames(File[] files)
	{
		try
		{
			presenter.onRequestAddDraggedAndDroppedGamesScreen(files);
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
		}
	}

	private class JPanelWithBackground extends JPanel
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			g.drawImage(Icons.BACKGROUND.getImage(), 0, 0, null);
		}
	}

	private static class ExpandMenuButton extends AbstractActionButton
	{
		private static final Color arrowColor = new Color(235, 235, 235);

		ExpandMenuButton(final ActionListener listener)
		{
			super(listener, new Color(187, 187, 187), new Color(207, 207, 207), new Color(157, 157, 157));
		}

		@Override
		protected void drawButton(Graphics g)
		{
			int width = getWidth();
			int height = getHeight();

			int halfWidth = width / 2;
			int startOfArrowHeight = (height / 2) - halfWidth;

			g.fillRect(0, 0, width, height);
			g.setColor(arrowColor);

			for(int ix = 0; ix <= halfWidth; ix++)
			{
				g.drawLine(ix, startOfArrowHeight + ix, width - ix - 1, startOfArrowHeight + ix);
			}
		}
	}
}
