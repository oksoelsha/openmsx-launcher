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

import info.msxlaunchers.openmsx.common.EnumWithDisplayName;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterType;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterDescriptor;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
import info.msxlaunchers.openmsx.launcher.data.game.constants.MSXGeneration;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Sound;
import info.msxlaunchers.openmsx.launcher.data.repository.constants.Company;
import info.msxlaunchers.openmsx.launcher.data.repository.constants.Country;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.ui.presenter.FilterEditingPresenter;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.JTableButtonColumn;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.component.MessageBoxUtil;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.images.Icons;
import info.msxlaunchers.openmsx.launcher.ui.view.swing.language.LanguageDisplayFactory;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Add/Edit filter dialog class
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class AddEditFilterWindow extends JDialog implements ActionListener
{
	private final FilterEditingPresenter presenter;
	private final Map<String,String> messages;
	private final boolean rightToLeft;
	private final Component parent;
	private final String[] filterItemsToEdit;
	private final String filterName;
	private final boolean editMode;
	private final boolean editSavedFilterMode;

	private final AddEditFilterWindow thisWindow = this;

	private JPanel filterItemsPanel;
	private JTable table;
	private TableModel tableModel;
	private JCheckBox saveAsCheckBox;
	private JTextField filterNameTextField;
	private JButton saveButton;
	private JButton closeButton;

	public AddEditFilterWindow(FilterEditingPresenter presenter,
			Language language,
			boolean rightToLeft,
			String filterName,
			String[] filterItemsToEdit)
	{
		this.presenter = presenter;
		this.rightToLeft = rightToLeft;
		this.filterItemsToEdit = filterItemsToEdit;
		this.filterName = filterName;
		this.editMode = (filterItemsToEdit != null);
		this.editSavedFilterMode = editMode && (filterName != null);

		this.parent = GlobalSwingContext.getIntance().getMainWindow();
		this.messages = LanguageDisplayFactory.getDisplayMessages(getClass(), language);
	}

	public void display()
	{
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		if(editSavedFilterMode)
		{
			setTitle(messages.get("EDIT_FILTER") + " - " + filterName);
		}
		else if(editMode)
		{
			setTitle(messages.get("EDIT_FILTER"));
		}
		else
		{
			setTitle(messages.get("ADD_FILTER"));
		}
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { 
				presenter.onRequestClose();
				dispose();
			} 
		});
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		final JComboBox<ComboBoxWithDisplayNameItem> filterTypesComboBox = getComboBoxWithDisplayValuesLocalizedExternally(FilterType.class);
		filterTypesComboBox.addActionListener(event -> showCorrespondingSelector(FilterType.valueOf(((ComboBoxWithDisplayNameItem)filterTypesComboBox.getSelectedItem()).value)));

		filterItemsPanel = new JPanel();

		tableModel = new TableModel();
		table = new JTable(tableModel);
		table.setRowSelectionAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(450, 82));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(320);
		table.getColumnModel().getColumn(2).setPreferredWidth(10);

		Action deleteAction = new AbstractAction()			
		{
			@Override
		    public void actionPerformed(ActionEvent e)
		    {
				try
				{
					removeRowFromTable(Integer.parseInt(e.getActionCommand()));
				}
				catch(LauncherException le)
				{
					MessageBoxUtil.showErrorMessageBox(thisWindow, le, messages, rightToLeft);
				}
		    }
		};

		new JTableButtonColumn(table, deleteAction, 2, Icons.DELETE_SMALL.getImageIcon(), messages.get("DELETE"));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		saveButton = new JButton(messages.get("SAVE"));
		saveButton.addActionListener(this);
		saveButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(saveButton);

		closeButton = new JButton(messages.get("CLOSE"));
		closeButton.addActionListener(this);
		closeButton.setPreferredSize(MainWindow.BUTTON_DIMENSION);
		buttonsPane.add(closeButton);

		JPanel filterSelectorPanel = new JPanel();
		filterSelectorPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		filterSelectorPanel.add(filterTypesComboBox);
		filterSelectorPanel.add(filterItemsPanel);

		JPanel saveAsPanel = new JPanel();
		filterNameTextField = new JTextField(25);
		saveAsPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		if(!editSavedFilterMode)
		{
			saveAsCheckBox = new JCheckBox(messages.get("SAVE_AS"));
			saveAsCheckBox.addActionListener(this);
			filterNameTextField = new JTextField(25);
			filterNameTextField.setEnabled(false);

			saveAsPanel.add(saveAsCheckBox);
			saveAsPanel.add(filterNameTextField);

			saveButton.setEnabled(false);
		}
		else
		{
			JLabel nameLabel = new JLabel(messages.get("NAME"));
			saveAsPanel.add(nameLabel);
			filterNameTextField.setText(filterName);
			saveAsPanel.add(filterNameTextField);
		}

		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(filterSelectorPanel))
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPanel.createSequentialGroup()
						.addComponent(saveAsPanel))
				.addGroup(gl_contentPanel.createSequentialGroup()
						.addComponent(buttonsPane))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(filterSelectorPanel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(saveAsPanel))
					.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(buttonsPane))
		);
		contentPanel.setLayout(gl_contentPanel);

		filterItemsPanel.setLayout(new CardLayout(0, 0));

		filterItemsPanel.add(new CompaniesLayer().getLayer(), FilterType.COMPANY.toString());
		filterItemsPanel.add(new CountriesLayer().getLayer(), FilterType.COUNTRY.toString());
		filterItemsPanel.add(new GenerationsLayer().getLayer(), FilterType.GENERATION.toString());
		filterItemsPanel.add(new GenresLayer().getLayer(), FilterType.GENRE.toString());
		filterItemsPanel.add(new MediaLayer().getLayer(), FilterType.MEDIUM.toString());
		filterItemsPanel.add(new SizesLayer().getLayer(), FilterType.SIZE.toString());
		filterItemsPanel.add(new SoundChipsLayer().getLayer(), FilterType.SOUND.toString());
		filterItemsPanel.add(new YearsLayer().getLayer(), FilterType.YEAR.toString());

		//initialise the table with existing filter items in case of edit
		if(filterItemsToEdit != null)
		{
			for(String filterItem:filterItemsToEdit)
			{
				addRowToTable(filterItem);
			}
		}

		if(rightToLeft)
		{
			filterSelectorPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			table.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.RIGHT);
			DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
			tableRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
			//exclude the delete column
			for(int index=0; index < table.getColumnCount() - 1; index++)
			{
				table.getColumnModel().getColumn(index).setCellRenderer(tableRenderer);
			}

			if(!editSavedFilterMode)
			{
				saveAsCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
			}
			saveAsPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			buttonsPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

			//combo boxes
			DefaultListCellRenderer comboBoxRenderer = new DefaultListCellRenderer();
			comboBoxRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
			filterTypesComboBox.setRenderer(comboBoxRenderer);
		}

		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == saveButton)
		{
			String text = filterNameTextField.getText().trim();
			if(text.isEmpty())
			{
				filterNameTextField.setBorder(BorderFactory.createLineBorder(Color.red, 1));
				return;
			}
			try
			{
				if(editSavedFilterMode)
				{
					presenter.onRequestUpdateFilterAction(filterName, text);
				}
				else
				{
					presenter.onRequestSaveFilterAction(filterNameTextField.getText());
				}

				dispose();
			}
			catch(LauncherException le)
			{
				MessageBoxUtil.showErrorMessageBox(this, le, messages, rightToLeft);
			}
		}
		else if(source == closeButton)
		{
			presenter.onRequestClose();
			dispose();
		}
		else if(source == saveAsCheckBox)
		{
			filterNameTextField.setEnabled(saveAsCheckBox.isSelected());
			saveButton.setEnabled(saveAsCheckBox.isSelected());
		}
	}

	/*
	 * Layers
	 */
	private interface Layer
	{
		Component getLayer();
		FilterType getType();
		String getValue1();
		String getValue2();
		FilterParameter getParameter();
	}

	private abstract class AbstractLayer implements Layer
	{
		@Override
    	final public FilterType getType()
    	{
    		return getClass().getAnnotation(FilterDescriptor.class).type();
    	}

		@Override
		public String getValue2()
		{
			return null;
		}

		@Override
		public FilterParameter getParameter()
		{
			return null;
		}

		protected void addButton(final Layer instance, final JPanel panel)
		{
    		JButton addButton = getAddButton();
    		addButton.addActionListener(new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e)
    			{
    				try
    				{
    					addFilterItem(instance);
    				}
    				catch(LauncherException le)
    				{
    					MessageBoxUtil.showErrorMessageBox(thisWindow, le, messages, rightToLeft);
    				}
    			}
    		});
    		panel.add(addButton);
		}

		protected JPanel getFilterLayerPanel()
		{
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));

			if(rightToLeft)
			{
				panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}

			return panel;
		}

		private JButton getAddButton()
		{
			JButton addButton = new JButton(Icons.ADD_SMALL.getImageIcon());
			addButton.setPreferredSize(new Dimension(24, 24));

			return addButton;
		}

		private void addFilterItem(Layer layer) throws LauncherException
		{
			addRowToTableAndApply(layer.getType(), layer.getValue1(), layer.getValue2(), layer.getParameter());
			table.changeSelection(table.getRowCount() - 1, 0, false, false);
		}
	}

	@FilterDescriptor(type = FilterType.COMPANY)
	private class CompaniesLayer extends AbstractLayer
    {
		private final CompaniesLayer instance = this;
		private final JComboBox<ComboBoxWithDisplayNameItem> companiesComboBox = getComboBoxWithDisplayValuesLocalizedInternally(Company.class);

    	@Override
    	public Component getLayer()
    	{
    		JPanel companiesPanel = getFilterLayerPanel();

    		companiesPanel.add(companiesComboBox);
    		addButton(instance, companiesPanel);

    		return companiesPanel;
    	}
 
    	@Override
		public String getValue1()
		{
			return ((ComboBoxWithDisplayNameItem)companiesComboBox.getSelectedItem()).label;
		}
    }
 	
    @FilterDescriptor(type = FilterType.COUNTRY)
    private class CountriesLayer extends AbstractLayer
    {
    	private final CountriesLayer instance = this;
    	private final JComboBox<ComboBoxWithDisplayNameItem> countriesComboBox = getComboBoxWithDisplayValuesLocalizedExternally(Country.class);

		@Override
    	public Component getLayer()
    	{
    		JPanel countriesPanel = getFilterLayerPanel();

    		countriesPanel.add(countriesComboBox);
    		addButton(instance, countriesPanel);

    		return countriesPanel;
    	}
 
    	@Override
		public String getValue1()
		{
			return ((ComboBoxWithDisplayNameItem)countriesComboBox.getSelectedItem()).value;
		}
    }
	
    @FilterDescriptor(type = FilterType.GENERATION)
    private class GenerationsLayer extends AbstractLayer
    {
    	private final GenerationsLayer instance = this;
    	private final JComboBox<ComboBoxWithDisplayNameItem> generationsComboBox = getComboBoxWithDisplayValuesLocalizedInternally(MSXGeneration.class);

		@Override
    	public Component getLayer()
    	{
    		JPanel generationsPanel = getFilterLayerPanel();

    		generationsPanel.add(generationsComboBox);
    		addButton(instance, generationsPanel);

    		return generationsPanel;
    	}
 
    	@Override
		public String getValue1()
		{
			return ((ComboBoxWithDisplayNameItem)generationsComboBox.getSelectedItem()).value;
		}
    }

    @FilterDescriptor(type = FilterType.GENRE)
    private class GenresLayer extends AbstractLayer
    {
    	private final GenresLayer instance = this;
    	private final JComboBox<ComboBoxWithDisplayNameItem> genresComboBox = new JComboBox<ComboBoxWithDisplayNameItem>();

		@Override
    	public Component getLayer()
    	{
    		JPanel genresPanel = getFilterLayerPanel();

    		Genre[] genres = Genre.values();
    		for(int index = 1; index < genres.length; index++)
    		{
    			genresComboBox.addItem(new ComboBoxWithDisplayNameItem(genres[index].getDisplayName(), genres[index].toString()));
    		}

    		genresPanel.add(genresComboBox);
    		addButton(instance, genresPanel);

    		return genresPanel;
    	}
 
    	@Override
		public String getValue1()
		{
			return ((ComboBoxWithDisplayNameItem)genresComboBox.getSelectedItem()).value;
		}
    }

    @FilterDescriptor(type = FilterType.MEDIUM)
    private class MediaLayer extends AbstractLayer
    {
    	private final MediaLayer instance = this;
    	private final JComboBox<ComboBoxWithDisplayNameItem> mediaComboBox = getComboBoxWithDisplayValuesLocalizedExternally(Medium.class);

		@Override
    	public Component getLayer()
    	{
    		JPanel mediaPanel = getFilterLayerPanel();

    		mediaPanel.add(mediaComboBox);
    		addButton(instance, mediaPanel);

    		return mediaPanel;
    	}
 
    	@Override
		public String getValue1()
		{
			return ((ComboBoxWithDisplayNameItem)mediaComboBox.getSelectedItem()).value;
		}
    }

    @FilterDescriptor(type = FilterType.SIZE)
    private class SizesLayer extends AbstractLayer
    {
    	private final SizesLayer instance = this;
		private final JComboBox<ComboBoxWithDisplayNameItem> sizesComboBox = new JComboBox<ComboBoxWithDisplayNameItem>(getSizesModel());
		private final JComboBox<ComboBoxWithDisplayNameItem> parametersComboBox = getComboBoxWithDisplayValuesLocalizedExternally(FilterParameter.class);
		private final JComboBox<ComboBoxWithDisplayNameItem> secondSizesComboBox = new JComboBox<ComboBoxWithDisplayNameItem>(getSizesModel());

		private final static String KB = " KB";

		SizesLayer()
		{
			secondSizesComboBox.setEnabled(false);
		}

		@Override
    	public Component getLayer()
    	{
    		JPanel sizesPanel = getFilterLayerPanel();

    		sizesPanel.add(sizesComboBox);
    		sizesPanel.add(parametersComboBox);
    		sizesPanel.add(secondSizesComboBox);
    		addButton(instance, sizesPanel);

    		parametersComboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
		    		String parameter = ((ComboBoxWithDisplayNameItem)parametersComboBox.getSelectedItem()).value;
		    		if(parameter.equals(FilterParameter.BETWEEN_INCLUSIVE.toString()))
		    		{
		    			secondSizesComboBox.setEnabled(true);		
		    		}
		    		else
		    		{
		    			secondSizesComboBox.setEnabled(false);
		    		}
				}
			});

    		return sizesPanel;
    	}
 
    	private ComboBoxWithDisplayNameItem[] getSizesModel()
    	{
    		int[] sizes = { 16, 32, 128, 256, 360, 512, 720, 1024 };
    		ComboBoxWithDisplayNameItem[] comboBoxItems = new ComboBoxWithDisplayNameItem[sizes.length];

    		for(int index = 0; index < sizes.length; index++)
    		{
    			comboBoxItems[index] = new ComboBoxWithDisplayNameItem(sizes[index] + KB, sizes[index] * 1024);
    		}

    		return comboBoxItems;
    	}

    	@Override
		public String getValue1()
		{
			return ((ComboBoxWithDisplayNameItem)sizesComboBox.getSelectedItem()).value;
		}

    	@Override
		public String getValue2()
		{
			return ((ComboBoxWithDisplayNameItem)secondSizesComboBox.getSelectedItem()).value;
		}

    	@Override
		public FilterParameter getParameter()
		{
			return FilterParameter.valueOf(((ComboBoxWithDisplayNameItem)parametersComboBox.getSelectedItem()).value);
		}
    }

    @FilterDescriptor(type = FilterType.SOUND)
    private class SoundChipsLayer extends AbstractLayer
    {
    	private final SoundChipsLayer instance = this;
    	private final JComboBox<ComboBoxWithDisplayNameItem> soundChipsComboBox = getComboBoxWithDisplayValuesLocalizedInternally(Sound.class);

		@Override
    	public Component getLayer()
    	{
    		JPanel soundChipsPanel = getFilterLayerPanel();

    		soundChipsPanel.add(soundChipsComboBox);
    		addButton(instance, soundChipsPanel);

    		return soundChipsPanel;
    	}
 
    	@Override
		public String getValue1()
		{
			return ((ComboBoxWithDisplayNameItem)soundChipsComboBox.getSelectedItem()).value;
		}
    }

    @FilterDescriptor(type = FilterType.YEAR)
    private class YearsLayer extends AbstractLayer
    {
    	private final YearsLayer instance = this;
		private final JComboBox<String> yearsComboBox = new JComboBox<String>(getYears());
		private final JComboBox<ComboBoxWithDisplayNameItem> parametersComboBox = getComboBoxWithDisplayValuesLocalizedExternally(FilterParameter.class);
		private final JComboBox<String> secondYearsComboBox = new JComboBox<String>(getYears());

		private final static int FIRST_YEAR = 1982;
		private final static int LAST_YEAR = 2016;

		YearsLayer()
		{
			secondYearsComboBox.setEnabled(false);
		}

		@Override
    	public Component getLayer()
    	{
    		JPanel sizesPanel = getFilterLayerPanel();

    		sizesPanel.add(yearsComboBox);
    		sizesPanel.add(parametersComboBox);
    		sizesPanel.add(secondYearsComboBox);
    		addButton(instance, sizesPanel);

    		parametersComboBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
		    		String parameter = ((ComboBoxWithDisplayNameItem)parametersComboBox.getSelectedItem()).value;
		    		if(parameter.equals(FilterParameter.BETWEEN_INCLUSIVE.toString()))
		    		{
		    			secondYearsComboBox.setEnabled(true);		
		    		}
		    		else
		    		{
		    			secondYearsComboBox.setEnabled(false);
		    		}
				}
			});

    		return sizesPanel;
    	}

    	private String[] getYears()
    	{
    		String[] years = new String[LAST_YEAR - FIRST_YEAR + 1];

    		int count = 0;
    		for(int year = FIRST_YEAR; year <= LAST_YEAR; year++)
    		{
    			years[count++] = String.valueOf(year);
    		}

    		return years;
    	}

    	@Override
		public String getValue1()
		{
			return yearsComboBox.getSelectedItem().toString();
		}

    	@Override
		public String getValue2()
		{
			return secondYearsComboBox.getSelectedItem().toString();
		}

    	@Override
		public FilterParameter getParameter()
		{
			return FilterParameter.valueOf(((ComboBoxWithDisplayNameItem)parametersComboBox.getSelectedItem()).value);
		}
    }

    private void showCorrespondingSelector(FilterType filterType)
	{
		CardLayout cl = (CardLayout)(filterItemsPanel.getLayout());
        cl.show(filterItemsPanel, filterType.toString());
	}

    private String getFilterDescriptor(FilterType type, String value1, String value2, FilterParameter parameter)
    {
    	String descriptor = null;

    	switch(type)
    	{
	    	case COMPANY:
	    		descriptor = value1;
	    		break;
	    	case COUNTRY:
	    	case MEDIUM:
	    		descriptor = messages.get(value1);
	    		break;
	    	case GENERATION:
	    		descriptor = MSXGeneration.valueOf(value1).getDisplayName();
	    		break;
	    	case GENRE:
	    		descriptor = Genre.valueOf(value1).getDisplayName();
	    		break;
	    	case SOUND:
	    		descriptor = Sound.valueOf(value1).getDisplayName();
	    		break;
	    	case SIZE:
	    		descriptor = getFilterWithParameterDescriptor(Utils.getString(Integer.parseInt(value1)/1024), Utils.getString(Integer.parseInt(value2)/1024),
	    														parameter, " KB");
	    		break;
	    	case YEAR:
	    		descriptor = getFilterWithParameterDescriptor(value1, value2, parameter, "");
	    		break;
    	}

    	return descriptor;
    }

    private String getFilterWithParameterDescriptor(String value1, String value2, FilterParameter parameter, String valueUnit)
    {
    	StringBuilder buffer = new StringBuilder();

		switch(parameter)
		{
			case EQUAL:
				buffer.append("= ").append(value1).append(valueUnit);
				break;
			case EQUAL_OR_GREATER:
				buffer.append(">= ").append(value1).append(valueUnit);
				break;
			case EQUAL_OR_LESS:
				buffer.append("<= ").append(value1).append(valueUnit);
				break;
			case GREATER:
				buffer.append("> ").append(value1).append(valueUnit);
				break;
			case LESS:
				buffer.append("< ").append(value1).append(valueUnit);
				break;
			case BETWEEN_INCLUSIVE:
	    		buffer.append(">= ").append(value1).append(valueUnit).append(" , ").append("<= ").append(value2).append(valueUnit);
				break;
		}

		return buffer.toString();
    }

    private <E extends Enum<E>> JComboBox<ComboBoxWithDisplayNameItem> getComboBoxWithDisplayValuesLocalizedExternally(Class<E> enumClass)
	{
		ComboBoxWithDisplayNameItem[] comboBoxItems = new ComboBoxWithDisplayNameItem[enumClass.getEnumConstants().length];

		int index = 0;
		for (Enum<E> enumVal: enumClass.getEnumConstants())
		{  
			comboBoxItems[index++] = new ComboBoxWithDisplayNameItem(messages.get(enumVal.toString()), enumVal.toString());
        }

		JComboBox<ComboBoxWithDisplayNameItem> comboBox = new JComboBox<ComboBoxWithDisplayNameItem>(comboBoxItems);
		if(rightToLeft)
		{
			DefaultListCellRenderer renderer = new DefaultListCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.RIGHT);
			comboBox.setRenderer(renderer);
		}
		return comboBox;
	}

	private <E extends Enum<E>> JComboBox<ComboBoxWithDisplayNameItem> getComboBoxWithDisplayValuesLocalizedInternally(Class<E> enumClass)
	{
		ComboBoxWithDisplayNameItem[] comboBoxItems = new ComboBoxWithDisplayNameItem[enumClass.getEnumConstants().length];

		int index = 0;
		for (Enum<E> enumVal: enumClass.getEnumConstants())
		{  
			comboBoxItems[index++] = new ComboBoxWithDisplayNameItem(((EnumWithDisplayName)enumVal).getDisplayName(), enumVal.toString());
        }

		Arrays.sort( comboBoxItems, ( c1, c2 ) -> c1.label.compareToIgnoreCase( c2.label ) );

		return new JComboBox<ComboBoxWithDisplayNameItem>(comboBoxItems);
	}

	private static class ComboBoxWithDisplayNameItem
	{
		private final String label;
		private final String value;

		ComboBoxWithDisplayNameItem(String label, String value)
		{
			this.label = label;
			this.value = value;
		}

		ComboBoxWithDisplayNameItem(String label, int value)
		{
			this(label, Utils.getString(value));
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	private class TableModel extends DefaultTableModel
	{
		private final String[] columnNames = { messages.get("TYPE"), messages.get("VALUE"), "" };

		@Override
		public boolean isCellEditable(int row, int col)
		{
			//only last column - one with delete button - is editable
			return col == (columnNames.length - 1);
		}

		@Override
		public int getColumnCount() { return columnNames.length; }

		@Override
	    public String getColumnName(int col) { return columnNames[col]; }
	}

	//
	// The following is to keep track of the filter items added to and removed from the table
	//
	private List<FilterItemObject> filterItemObjectList = new ArrayList<FilterItemObject>();

	private void addRowToTable(String filter)
	{
		String[] filterMonikerParts = filter.split(":");

		FilterType type = FilterType.valueOf(filterMonikerParts[0]);
		String value1 = filterMonikerParts[1];
		String value2;
		FilterParameter parameter;

		if(filterMonikerParts.length == 4)
    	{
			value2 = filterMonikerParts[2];
			parameter = FilterParameter.valueOf(filterMonikerParts[3]);
    	}
    	else
    	{
			value2 = null;
			parameter = null;
    	}

		tableModel.addRow(new Object[] {messages.get(type.toString()), getFilterDescriptor(type, value1, value2, parameter)});

		filterItemObjectList.add(new FilterItemObject(type, value1, value2, parameter));
	}

	private void addRowToTableAndApply(FilterType type, String value1, String value2, FilterParameter parameter) throws LauncherException
	{
		if(presenter.onAddToFilterListAndApply(type, value1, value2, parameter))
		{
			//this means the filter didn't exist before -> proceed
			tableModel.addRow(new Object[] {messages.get(type.toString()), getFilterDescriptor(type, value1, value2, parameter)});

			filterItemObjectList.add(new FilterItemObject(type, value1, value2, parameter));
		}
	}

	private void removeRowFromTable(int index) throws LauncherException
	{
		FilterItemObject filterItemObject = filterItemObjectList.get(index);
		presenter.onRemoveFromFilterList(filterItemObject.type, filterItemObject.value1, filterItemObject.value2, filterItemObject.parameter);

		tableModel.removeRow(index);
		filterItemObjectList.remove(index);
	}

	private static class FilterItemObject
	{
		private final FilterType type;
		private final String value1;
		private final String value2;
		private final FilterParameter parameter;

		FilterItemObject(FilterType type, String value1, String value2, FilterParameter parameter)
		{
			this.type = type;
			this.value1 = value1;
			this.value2 = value2;
			this.parameter = parameter;
		}
	}
}