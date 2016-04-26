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

import info.msxlaunchers.openmsx.common.OSUtils;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.GameLabel;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.MainPresenter;
import info.msxlaunchers.openmsx.launcher.ui.view.platform.PlatformViewProperties;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JListWithImagesAndActions;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JSearchTextField;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.SearchFieldHandler;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private JMenuBar menuBar;
	private JMenu optionsMenu;
	private JMenuItem optionsSettings;
	private JMenu actionsMenu;
	private JMenuItem actionsCreateEmptyDatabase;
	private JMenuItem actionsFillDatabase;
	private JMenuItem actionsUpdateAllDatabase;
	private JMenuItem actionsImportBlueMSXLauncherDatabases;
	private JMenu toolsMenu;
	private JMenuItem databaseManager;
	private JMenu helpMenu;
	private JMenuItem helpAbout;
	private JMenuItem helpFile;
	private JMenuItem helpCheckForUpdates;
	private JListWithImagesAndActions gameList;
	private JLabel databaseLabel;
	private JLabel countLabel;
	private JLabel countValueLabel;
	private JLabel gameDataLabel;
	private JComboBox<String> databaseComboBox;
	private DefaultComboBoxModel<String> databaseComboBoxModel;
	private JButton favoritesButton;
	private JButton searchButton;
	private JButton filtersButton;
	private JLabel currentFilterLabel;
	private JButton launchButton;
	private JButton removeButton;
	private JButton addButton;
	private JButton editButton;

	private JPopupMenu contextMenu;
	private JMenuItem moveMenuItem;
	private JMenuItem locateFileMenuItem;
	private JMenuItem addFavoriteMenuItem;
	private JMenuItem infoMenuItem;
	private JMenuItem propertiesMenuItem;

	private Map<String,String> messages;

	private JLabel screenshot1Label;
	private JLabel screenshot2Label;

	private JPopupMenu favoritesContextMenu;
	private JPopupMenu searchContextMenu;

	private JPopupMenu filtersContextMenu;
	private JMenuItem newFilterMenuItem;
	private JMenuItem editCurrentUntitledFilterMenuItem;
	private JMenuItem resetFilterMenuItem;

	private ComponentOrientation orientation = null;
	private int popupMenuOrientation = 0;

	private String removeConfirmationMessage = null;
	private String updateAllDatabasesConfirmationMessage = null;

	private Map<Medium, ImageIcon> mediaIconsMap = new HashMap<Medium,ImageIcon>();

	public static final Dimension BUTTON_DIMENSION = new Dimension(109, 28);

	private static final Font FONT_SIZE_11 = new Font(null, Font.PLAIN, 11);
	private static final Font FONT_SIZE_10 = new Font(null, Font.PLAIN, 10);
	private static final Color DEFAULT_BUTTON_BG_COLOR = UIManager.getLookAndFeelDefaults().getColor("Button.background");
	private static final Insets POPUP_MENU_ITEM_BUTTON_INSETS = new Insets(-1, 0, -1, -13);
	private static final Dimension POPUP_MENU_ITEM_BUTTON_DIMENSION = new Dimension(18, 18);
	public static final Color POPUP_MENU_ITEM_BUTTON_HOVER_BG_COLOR = new Color(140, 140, 220);

	private static final String SPACES = "                                ";
	private static final int SEARCH_TEXT_FIELD_COLUMNS = 25;

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

		new FileDrop(this, (File[] files) -> addDraggedGames(files));
	}

	public void display(Language language,
			Set<GameLabel> games,
			Set<String> databases,
			String currentDatabase,
			boolean rightToLeft,
			boolean showUpdateAllDatabases)
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
		addWindowListener( new WindowCloseAdapter() );
		setResizable(false);

		//menus
//		drawMenu();

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
		}
		else
		{
			orientation = ComponentOrientation.LEFT_TO_RIGHT;
			popupMenuOrientation = FlowLayout.RIGHT;
		}
		flipOrientation();

		//set language labels
		refreshLanguage(language);

		//set the database and game list
		fillGameList(currentDatabase, games, null);

		//update the count
		updateGameCount(games.size());

		addWindowFocusListener(this);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void drawMenu()
	{
		menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        //options menu
		optionsMenu = new JMenu();
		menuBar.add(optionsMenu);

		optionsSettings = new JMenuItem();
		optionsSettings.addActionListener(event -> presenter.onRequestSettingsScreen());
        optionsMenu.add(optionsSettings);

        //actions menu
		actionsMenu = new JMenu();
		menuBar.add(actionsMenu);

        actionsCreateEmptyDatabase = new JMenuItem();
        actionsCreateEmptyDatabase.addActionListener(event -> presenter.onRequestCreateEmptyDatabaseScreen());
        actionsMenu.add(actionsCreateEmptyDatabase);

        actionsFillDatabase = new JMenuItem();
        actionsFillDatabase.addActionListener(event -> onRequestFillDatabaseScreen());
        actionsMenu.add(actionsFillDatabase);

        actionsUpdateAllDatabase = new JMenuItem();
        //update all databases only appears if added manually to the settings file
        if(showUpdateAllDatabases)
        {
        	actionsUpdateAllDatabase.addActionListener(event -> onRequestUpdateAllDatabases());
        	actionsMenu.add(actionsUpdateAllDatabase);
        }

        actionsImportBlueMSXLauncherDatabases = new JMenuItem();
        //import blueMSX Launcher databases is only for Windows
        if(OSUtils.isWindows())
        {
	        actionsImportBlueMSXLauncherDatabases.addActionListener(event -> onRequestImportBlueMSXLauncherDatabasesScreen());
	        actionsMenu.addSeparator();
	        actionsMenu.add(actionsImportBlueMSXLauncherDatabases);
        }

        //tools menu
        toolsMenu = new JMenu();
        menuBar.add(toolsMenu);

        databaseManager = new JMenuItem();
        databaseManager.addActionListener(event -> onRequestDatabaseManagerScreen());
        toolsMenu.add(databaseManager);

        //help menu
        helpMenu = new JMenu();
        menuBar.add(helpMenu);

        helpFile = new JMenuItem();
        helpFile.addActionListener(event -> onRequestHelpFile());
        helpMenu.add(helpFile);

        helpCheckForUpdates = new JMenuItem();
        helpCheckForUpdates.addActionListener(event -> onRequestUpdatesChecker());
        helpMenu.add(helpCheckForUpdates);
    	helpMenu.addSeparator();

        helpAbout = new JMenuItem();
        //don't show the About menu item in Mac - this appears in the Mac application menu
        if(!OSUtils.isMac())
        {
            helpAbout.addActionListener(event -> presenter.onRequestAboutScreen());
        	helpMenu.add(helpAbout);
        }
	}

	public void addDatabase(String database)
	{
		databaseComboBoxModel.addElement(database);
	}

	public void removeDatabase(String database)
	{
		databaseComboBoxModel.removeElement(database);
		if(databaseComboBoxModel.getSize() == 0)
		{
			//In this case force clear the game list. When there are other databases there's no need to do that because
			//removing the current selected database will automatically select another from the drop down list and will
			//therefore clear the list and re-populate it.
			gameList.clear();
			updateGameCount(0);
		}
	}

	public void renameDatabase(String oldDatabase, String newDatabase)
	{
		databaseComboBoxModel.addElement(newDatabase);
		if(oldDatabase.equals(currentDatabase))
		{
			databaseComboBox.setSelectedItem(newDatabase);
			currentDatabase = newDatabase;
		}
		databaseComboBoxModel.removeElement(oldDatabase);
	}

	private void drawScreen()
	{
		databaseLabel = new JLabel();
		databaseLabel.setFont(FONT_SIZE_11);

		databaseComboBoxModel = new SortedComboBoxModel(Utils.getSortedCaseInsensitiveArray(databases));
		databaseComboBox = new JComboBox<String>(databaseComboBoxModel);
		databaseComboBox.setMaximumRowCount(10);
		databaseComboBox.setFont(FONT_SIZE_11);
		databaseComboBox.addActionListener(event -> onSelectDatabase());
		databaseComboBox.setFocusable(false);

		countLabel = new JLabel();
		countLabel.setFont(FONT_SIZE_11);
		countLabel.setHorizontalAlignment(SwingConstants.CENTER);

		countValueLabel = new JLabel();
		countValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
		countValueLabel.setFont(FONT_SIZE_11);

		gameDataLabel = new JLabel();
		gameDataLabel.setFont(FONT_SIZE_10);
		gameDataLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 1),
				BorderFactory.createEmptyBorder(0, 3, 0, 3)));
		gameDataLabel.setOpaque(true);

		gameList = new JListWithImagesAndActions(new DefaultListModel<Object>());
		//need to unregister the gameList component from the ToolTipManager to allow the Ctrl+F1 to work
		ToolTipManager.sharedInstance().unregisterComponent(gameList);
		gameList.registerKeyboardAction(event -> moveSelectedGames(), getCtrl_XKeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> showPropertiesOfSelectedGame(), getCtrl_F1KeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> locateMainFileOfSelectedGame(), getCtrl_Shift_FKeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> editGame(), getCtrl_EKeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> addSelectedGameToFavorites(), getCtrl_DKeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> viewGameInfo(), getF1KeyStroke(), JComponent.WHEN_FOCUSED);
		gameList.registerKeyboardAction(event -> processShowSearchScreenRequest(), getCtrl_FKeyStroke(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		gameList.registerKeyboardAction(event -> processShowFavoritesMenuRequest(), getCtrl_IKeyStroke(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		gameList.registerKeyboardAction(event -> processShowFiltersMenuRequest(), getCtrl_LKeyStroke(), JComponent.WHEN_IN_FOCUSED_WINDOW);
		gameList.registerKeyboardAction(event -> applyFilter(null), getCtrl_RKeyStroke(), JComponent.WHEN_IN_FOCUSED_WINDOW);

        JScrollPane gameListScrollBar = new JScrollPane(gameList);

		currentFilterLabel = new JLabel();
		currentFilterLabel.setFont(FONT_SIZE_10);
		currentFilterLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 1),
				BorderFactory.createEmptyBorder(0, 3, 0, 3)));
		currentFilterLabel.setOpaque(true);

		JPanel launchButtonPanel = new JPanel();
		launchButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel removeButtonPanel = new JPanel();
		removeButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel addButtonPanel = new JPanel();
		addButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel editButtonPanel = new JPanel();
		editButtonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		screenshot1Label = new JLabel(Icons.NO_SCREENSHOT.getImageIcon());
		screenshot2Label = new JLabel(Icons.NO_SCREENSHOT.getImageIcon());

		favoritesButton = new JButton();
		favoritesButton.setIcon(Icons.FAVORITE.getImageIcon());
		favoritesButton.addActionListener(this);
		favoritesButton.setFocusable(false);

		searchButton = new JButton();
		searchButton.setIcon(Icons.SEARCH.getImageIcon());
		searchButton.addActionListener(this);
		searchButton.setFocusable(false);

		filtersButton = new JButton();
		filtersButton.setIcon(Icons.FILTER.getImageIcon());
		filtersButton.addActionListener(this);
		filtersButton.setFocusable(false);

		GroupLayout groupLayout = new GroupLayout(contentPane);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(gameListScrollBar, GroupLayout.PREFERRED_SIZE, 333, GroupLayout.PREFERRED_SIZE)
						.addComponent(gameDataLabel, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(filtersButton, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addGap(1)
								.addComponent(currentFilterLabel, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(databaseComboBox, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
							.addGap(30)
							.addComponent(false, countValueLabel, 30, GroupLayout.DEFAULT_SIZE, 30))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(false, databaseLabel, 100, GroupLayout.DEFAULT_SIZE, 100)
							.addGap(94)
							.addComponent(false, countLabel, 60, GroupLayout.DEFAULT_SIZE, 60)))
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(favoritesButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
						.addComponent(launchButtonPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(removeButtonPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(addButtonPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(editButtonPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(screenshot1Label, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
						.addComponent(screenshot2Label, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE))
					.addGap(25))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(databaseLabel)
								.addComponent(countLabel))
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(databaseComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(countValueLabel))
							.addGap(4)
							.addComponent(gameDataLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(gameListScrollBar, GroupLayout.PREFERRED_SIZE, 448, GroupLayout.PREFERRED_SIZE)
							.addGap(2)
							.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
								.addComponent(filtersButton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
								.addComponent(currentFilterLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
								.addGap(5))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(35)
									.addGroup(groupLayout.createParallelGroup(Alignment.CENTER)
											.addComponent(favoritesButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
											.addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
									.addGap(115)
									.addComponent(launchButtonPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(removeButtonPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(addButtonPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(editButtonPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(15)
									.addComponent(screenshot1Label, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
									.addGap(15)
									.addComponent(screenshot2Label, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)))))
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
	    		if(!listSelectionEvent.getValueIsAdjusting())
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
					 addFavoriteMenuItem.setEnabled(gameList.getSelectedIndices().length == 1);
					 locateFileMenuItem.setEnabled(gameList.getSelectedIndices().length == 1);
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
		flipOrientation();
	}

	public void flipOrientationRightToLeft()
	{
		orientation = ComponentOrientation.RIGHT_TO_LEFT;
		popupMenuOrientation = FlowLayout.LEFT;
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
		actionsUpdateAllDatabase.setText(messages.get("UPDATE_ALL_DATABASES"));
        actionsUpdateAllDatabase.setMnemonic(KeyStroke.getKeyStroke(messages.get("UPDATE_ALL_DATABASES_MNEMONIC")).getKeyCode());
		actionsImportBlueMSXLauncherDatabases.setText(messages.get("IMPORT_BLUEMSXLAUNCHER_DATABASES"));
        actionsImportBlueMSXLauncherDatabases.setMnemonic(KeyStroke.getKeyStroke(messages.get("IMPORT_BLUEMSXLAUNCHER_DATABASES_MNEMONIC")).getKeyCode());
		toolsMenu.setText(messages.get("TOOLS"));
        toolsMenu.setMnemonic(KeyStroke.getKeyStroke(messages.get("TOOLS_MNEMONIC")).getKeyCode());
		databaseManager.setText(messages.get("DATABASE_MANAGER"));
        databaseManager.setMnemonic(KeyStroke.getKeyStroke(messages.get("DATABASE_MANAGER_MNEMONIC")).getKeyCode());
		helpMenu.setText(messages.get("HELP"));
        helpMenu.setMnemonic(KeyStroke.getKeyStroke(messages.get("HELP_MNEMONIC")).getKeyCode());
		helpFile.setText(messages.get("HELP"));
        helpFile.setMnemonic(KeyStroke.getKeyStroke(messages.get("HELP_MNEMONIC")).getKeyCode());
        helpCheckForUpdates.setText(messages.get("CHECK_FOR_UPDATES") + "...");
        helpCheckForUpdates.setMnemonic(KeyStroke.getKeyStroke(messages.get("CHECK_FOR_UPDATES_MNEMONIC")).getKeyCode());
        helpAbout.setText(messages.get("ABOUT"));
        helpAbout.setMnemonic(KeyStroke.getKeyStroke(messages.get("ABOUT_MNEMONIC")).getKeyCode());
		databaseLabel.setText(messages.get("DATABASE"));
		countLabel.setText(messages.get("COUNT"));
		favoritesButton.setToolTipText(messages.get("FAVORITES"));
		searchButton.setToolTipText(messages.get("SEARCH"));
		filtersButton.setToolTipText(messages.get("FILTERS"));
		launchButton.setToolTipText(messages.get("LAUNCH"));
		removeButton.setToolTipText(messages.get("REMOVE"));
		addButton.setToolTipText(messages.get("ADD"));
		editButton.setToolTipText(messages.get("EDIT"));

		moveMenuItem.setText(messages.get("MOVE") + "...");
		locateFileMenuItem.setText(messages.get("LOCATE_FILE"));
		addFavoriteMenuItem.setText(messages.get("ADD_FAVORITE"));
		infoMenuItem.setText(messages.get("INFO"));
		propertiesMenuItem.setText(messages.get("PROPERTIES"));

		newFilterMenuItem.setText(messages.get("NEW") + "...");
		editCurrentUntitledFilterMenuItem.setText(messages.get("EDIT_UNTITLED_FILTER") + "...");
		resetFilterMenuItem.setText(messages.get("RESET"));

		removeConfirmationMessage = messages.get("REMOVE_CONFIRMATION_MESSAGE");
		updateAllDatabasesConfirmationMessage = messages.get("UPDATE_ALL_DATABASES_CONFIRMATION_MESSAGE");
	}

	public void fillGameList(String currentDatabase, Set<GameLabel> games, String selectedGame)
	{
		databaseComboBox.setSelectedItem(currentDatabase);
		filtersButton.setEnabled(currentDatabase != null);
		gameList.clear();

		for(GameLabel gameLabel: games)
		{
			gameList.addElement(gameLabel.getName(), mediaIconsMap.get(gameLabel.getMedium()));
		}

		if(selectedGame != null)
		{
			gameList.setSelectedValue(selectedGame);
		}
		else
		{
			enableButtons(false, false, currentDatabase != null, false, false);
		}
	}

	public void updateGameCount(int total)
	{
		countValueLabel.setText(String.valueOf(total));
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
		else if(source == filtersButton)
		{
			processShowFiltersMenuRequest();
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
				selectGame(((PopupMenuItemName)action).getPopupMenuItemName());
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
					presenter.onRequestDeleteFavoriteAction(((PopupMenuItemDelete)action).getPopupMenuItemName());
					gameList.requestFocusInWindow();
				}
				catch(LauncherException le)
				{
					MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
				}
			}
		}
	}

	public void showGameCompanyYearSizeData(String company, String year, long size)
	{
		String displayString;
		if(Utils.isEmpty(company) && Utils.isEmpty(year))
		{
			if(size == 0.0)
			{
				//size 0 is for files with real size of 0, files that don't exist and scripts
				displayString = "";
			}
			else
			{
				displayString = size + " KB";
			}
		}
		else
		{
			displayString = company + " " + year + " - " + size + " KB";
		}
		gameDataLabel.setText(displayString);
	}

	public void resetGameCompanyYearSizeData()
	{
		gameDataLabel.setText("");
	}

	public void showFavoritesMenu(Set<String> favoritesAsStrings)
	{
		favoritesContextMenu = new JPopupMenu();
		favoritesContextMenu.setComponentOrientation(orientation);

		for(String favoriteMoniker:favoritesAsStrings)
		{
			JMenuItem favoriteMenuItem = new JMenuItem();
			favoriteMenuItem.setComponentOrientation(orientation);
			favoriteMenuItem.setAction(new FavoriteMenuItemName(favoriteMoniker));
			favoriteMenuItem.addActionListener(this);

			favoriteMenuItem.setMargin(POPUP_MENU_ITEM_BUTTON_INSETS);
			favoriteMenuItem.setLayout(new FlowLayout(popupMenuOrientation, 0, 0));

		    JButton deleteButton = new JButton();
		    deleteButton.setContentAreaFilled(false);
		    deleteButton.setPreferredSize(POPUP_MENU_ITEM_BUTTON_DIMENSION);
		    deleteButton.setAction(new FavoriteMenuItemDelete(favoriteMoniker));
		    deleteButton.addActionListener(this);
		    deleteButton.setToolTipText(messages.get("DELETE"));
		    addFilterButtonHoverBehavior(deleteButton);
		    favoriteMenuItem.add(deleteButton);

		    favoritesContextMenu.add(favoriteMenuItem);			
		}

		int x;
		if(orientation == ComponentOrientation.RIGHT_TO_LEFT)
		{
			x = favoritesButton.getWidth() - favoritesContextMenu.getPreferredSize().width - 2;
		}
		else
		{
			x = 2;
		}
		favoritesContextMenu.show(favoritesButton, x, favoritesButton.getHeight()-1);
	}

	public void higlightGame( String gameName )
	{
		gameList.setSelectedValue( gameName );
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

		if(filterNames.size() > 0)
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
		filtersContextMenu.add(newFilterMenuItem);

		int x = filtersButton.getWidth();
		if(orientation == ComponentOrientation.RIGHT_TO_LEFT)
		{
			x -= filtersContextMenu.getPreferredSize().width + x;
		}
		filtersContextMenu.show(filtersButton, x, 0 - filtersContextMenu.getPreferredSize().height);
	}

	public void updateFilterNameLabel(String filterName)
	{
		currentFilterLabel.setText(filterName);
	}

	public void setFilterNameLabelUntitled()
	{
		currentFilterLabel.setText("(" + messages.get("UNTITLED_FILTER") + ")");
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.component.SearchFieldHandler#getSearchMatches(java.lang.String)
	 */
	@Override
	public Set<String> getSearchMatches(String searchString)
	{
		return presenter.onRequestSearchMatches(searchString);
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.view.swing.component.SearchFieldHandler#handleSearchSelection(java.lang.String)
	 */
	@Override
	public void handleSearchSelection(String searchSelection)
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

	private void onRequestUpdateAllDatabases()
	{
		try
		{
			if(MessageBoxUtil.showYesNoMessageBox(ref, updateAllDatabasesConfirmationMessage, messages, orientation) == 0)
			{
				int numberUpdatedProfiles = presenter.onRequestUpdateAllDatabases();
				presenter.onUpdateViewedDatabase(currentDatabase);

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
			addButton.setEnabled(currentDatabase != null);
			filtersButton.setEnabled(currentDatabase != null);
		}
		catch(LauncherException le)
		{
			MessageBoxUtil.showErrorMessageBox(ref, le, messages, orientation);
		}
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

	private void onSelectDatabase()
	{
    	try
    	{
    		presenter.onSelectDatabase((String)databaseComboBox.getSelectedItem());
		}
		catch(LauncherException le)
		{
			//here a non-existing database was selected.
			//In this case force selection of previous valid one
			databaseComboBox.setSelectedItem(currentDatabase);

			MessageBoxUtil.showErrorMessageBox(this, le, messages, orientation);
		}

    	currentDatabase = (String)databaseComboBox.getSelectedItem();
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
		        int selections[] = gameList.getSelectedIndices();
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
			Set<String> movedGames = presenter.onRequestMoveGamesScreen(games, (String)databaseComboBox.getSelectedItem());
	
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

	private void showPropertiesOfSelectedGame()
	{
		String selectedGame = getSelectedGame();

		if( selectedGame != null )
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
		presenter.onRequestListOfSavedFilters();
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
		searchContextMenu = new JPopupMenu();
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

	private static void addFilterButtonHoverBehavior( final JButton button )
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

		menuBar.setComponentOrientation(orientation);
		optionsMenu.applyComponentOrientation(orientation);
		actionsMenu.applyComponentOrientation(orientation);
		helpMenu.applyComponentOrientation(orientation);
		contentPane.setComponentOrientation(orientation);
		contentPane.applyComponentOrientation(orientation);
		newFilterMenuItem.setComponentOrientation(orientation);
		editCurrentUntitledFilterMenuItem.setComponentOrientation(orientation);
		resetFilterMenuItem.setComponentOrientation(orientation);
	    moveMenuItem.setComponentOrientation(orientation);
	    locateFileMenuItem.setComponentOrientation(orientation);
	    addFavoriteMenuItem.setComponentOrientation(orientation);
	    infoMenuItem.setComponentOrientation(orientation);
	    propertiesMenuItem.setComponentOrientation(orientation);

		//force the following to always be left to right
		gameList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		databaseComboBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
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
        int selections[] = gameList.getSelectedIndices();
        if(selections != null && selections.length > 0)
        {
	        gameNames = new HashSet<String>(Arrays.asList(gameList.getSelectedItems()));
        }
        return gameNames;
	}

	private void setupGameListContextMenu()
	{
	    contextMenu = new JPopupMenu();

	    moveMenuItem = new JMenuItem();
	    moveMenuItem.addActionListener(this);
	    moveMenuItem.setAccelerator(getCtrl_XKeyStroke());
	    contextMenu.add(moveMenuItem);

	    locateFileMenuItem = new JMenuItem();
	    locateFileMenuItem.setAccelerator(getCtrl_Shift_FKeyStroke());
	    locateFileMenuItem.addActionListener(this);
	    contextMenu.add(locateFileMenuItem);

	    addFavoriteMenuItem = new JMenuItem();
	    addFavoriteMenuItem.setAccelerator(getCtrl_DKeyStroke());
	    addFavoriteMenuItem.setIcon(Icons.FAVORITE.getImageIcon());
	    addFavoriteMenuItem.addActionListener(this);
	    contextMenu.add(addFavoriteMenuItem);

	    contextMenu.addSeparator();

	    infoMenuItem = new JMenuItem();
	    infoMenuItem.addActionListener(this);
	    infoMenuItem.setAccelerator(getF1KeyStroke());
	    contextMenu.add(infoMenuItem);

	    propertiesMenuItem = new JMenuItem();
	    propertiesMenuItem.addActionListener(this);
	    propertiesMenuItem.setAccelerator(getCtrl_F1KeyStroke());
	    contextMenu.add(propertiesMenuItem);
	}

	private void setupFiltersMenu()
	{
		newFilterMenuItem = new JMenuItem();
		newFilterMenuItem.addActionListener(this);

		editCurrentUntitledFilterMenuItem = new JMenuItem();
		editCurrentUntitledFilterMenuItem.addActionListener(this);

		resetFilterMenuItem = new JMenuItem();
		resetFilterMenuItem.addActionListener(this);
	}

	private void selectGame(String favoriteName)
	{
		try
		{
			presenter.onSelectFavorite(favoriteName);
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

	private KeyStroke getCtrl_XKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrl_EKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrl_F1KeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_F1, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrl_Shift_FKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | KeyEvent.SHIFT_DOWN_MASK);
	}

	private KeyStroke getCtrl_DKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getF1KeyStroke()
	{
		return KeyStroke.getKeyStroke("F1");
	}

	private KeyStroke getCtrl_FKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrl_IKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrl_LKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private KeyStroke getCtrl_RKeyStroke()
	{
		return KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
	}

	private static class SortedComboBoxModel extends DefaultComboBoxModel<String>
	{
		public SortedComboBoxModel(String[] elements)
		{
			super(elements);
		}

		@Override
        public void addElement(String element)
		{
			int index;
			int size = getSize();

			for(index = 0; index < size; index++)
			{
				if(element.compareToIgnoreCase(getElementAt(index)) < 0)
				{
					break;
				}
			}

			insertElementAt(element, index);
        }
	}

	abstract private class PopupMenuAction extends AbstractAction
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

	abstract private class PopupMenuItemName extends PopupMenuAction
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

	abstract private class PopupMenuItemDelete extends PopupMenuAction
	{
		PopupMenuItemDelete(String popupMenuItemName)
		{
			super(popupMenuItemName);
	        putValue(Action.SMALL_ICON, Icons.DELETE_SMALL.getImageIcon());
		}
	}

	abstract private class PopupMenuItemEdit extends PopupMenuAction
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
		FavoriteMenuItemName(String favoriteName)
		{
			super(favoriteName);
		}
	}

	private class FavoriteMenuItemDelete extends PopupMenuItemDelete
	{
		FavoriteMenuItemDelete(String favoriteName)
		{
			super(favoriteName);
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
}
