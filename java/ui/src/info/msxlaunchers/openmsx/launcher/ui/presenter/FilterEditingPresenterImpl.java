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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.filter.Filter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterFactory;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterType;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterPersister;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterSetAlreadyExistsException;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterSetNotFoundException;
import info.msxlaunchers.openmsx.launcher.ui.view.FilterEditingView;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;

/**
 * Implementation of <code>FilterEditingPresenter</code>
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
final class FilterEditingPresenterImpl implements FilterEditingPresenter
{
	private final MainPresenter mainPresenter;
	private final FilterEditingView view;
	private final FilterPersister filterPersister;
	private final Set<Filter> filterItemsSet;

	//model
	private boolean editMode = false;
	private boolean changedFilter = false;

	@Inject
	FilterEditingPresenterImpl( MainPresenter mainPresenter, FilterEditingView view, FilterPersister filterPersister )
	{
		this.mainPresenter = Objects.requireNonNull( mainPresenter );
		this.view = Objects.requireNonNull( view );
		this.filterPersister = Objects.requireNonNull( filterPersister );
		this.filterItemsSet = new HashSet<Filter>();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FilterEditingPresenter#onRequestAddFilterScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestAddFilterScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException
	{
		editMode = false;

		view.displayAddFilterScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FilterEditingPresenter#onRequestEditFilterScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean, java.lang.String, java.util.Set)
	 */
	@Override
	public void onRequestEditFilterScreen( Language currentLanguage, boolean currentRightToLeft, String filterName, Set<Filter> filterItems ) throws LauncherException
	{
		editMode = true;

		filterItemsSet.clear();
		filterItemsSet.addAll( filterItems );

		String[] filterMonikerStrings = getFilterAsStringMonikers( filterItems );

		view.displayEditFilterScreen( currentLanguage, currentRightToLeft, filterName, filterMonikerStrings );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FilterEditingPresenter#onAddToFilterListAndApply(info.msxlaunchers.openmsx.launcher.data.filter.FilterType, java.lang.String, java.lang.String, info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter)
	 */
	@Override
	public boolean onAddToFilterListAndApply( FilterType type, String value1, String value2, FilterParameter parameter ) throws LauncherException
	{
		changedFilter = true;

		boolean added = filterItemsSet.add( FilterFactory.createFilter( type, value1, value2, parameter ) );

		if( added )
		{
			mainPresenter.onApplyFilter( filterItemsSet );
		}

		return added;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FilterEditingPresenter#onRemoveFromFilterList(info.msxlaunchers.openmsx.launcher.data.filter.FilterType, java.lang.String, java.lang.String, info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter)
	 */
	@Override
	public void onRemoveFromFilterList( FilterType type, String value1, String value2, FilterParameter parameter ) throws LauncherException
	{
		changedFilter = true;

		filterItemsSet.remove( FilterFactory.createFilter( type, value1, value2, parameter ) );

		mainPresenter.onApplyFilter( filterItemsSet );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FilterEditingPresenter#onRequestSaveFilterAction(java.lang.String)
	 */
	@Override
	public void onRequestSaveFilterAction( String filterName ) throws LauncherException
	{
		try
		{
			filterPersister.saveFilter( filterName, filterItemsSet );
			mainPresenter.onRequestUpdateFilterName( filterName );
		}
		catch( FilterSetAlreadyExistsException e )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_FILTER_ALREADY_EXISTS, filterName );
		}
		catch( IOException e )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FilterEditingPresenter#onRequestUpdateFilterAction(java.lang.String, java.lang.String)
	 */
	@Override
	public void onRequestUpdateFilterAction( String oldFilterName, String newFilterName ) throws LauncherException
	{
		//first make sure there's no filter with the same name as the new name
		if( !oldFilterName.equals( newFilterName ) )
		{
			try
			{
				filterPersister.getFilter( newFilterName );
				throw new LauncherException( LauncherExceptionCode.ERR_FILTER_ALREADY_EXISTS, newFilterName );
			}
			catch( FilterSetNotFoundException e )
			{
				//then this filter doesn't exist - proceed
			}
		}

		try
		{
			filterPersister.deleteFilter( oldFilterName );
			filterPersister.saveFilter( newFilterName, filterItemsSet );
		}
		catch( FilterSetAlreadyExistsException e )
		{
			//this was processed above already and should not happen here
		}
		catch( IOException e )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_IO );
		}

		mainPresenter.onRequestUpdateFilterName( newFilterName );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FilterEditingPresenter#onRequestClose()
	 */
	@Override
	public void onRequestClose()
	{
		if( editMode && changedFilter )
		{
			mainPresenter.onRequestSetFilterNameUntitled();
		}
	}

	private String[] getFilterAsStringMonikers( Set<Filter> filter )
	{
		String[] filtersAsStringArray = new String[filter.size()];
		int index = 0;
		for( Filter filterItem: filter )
		{
			filtersAsStringArray[index++] = FilterFactory.getFilterMoniker( filterItem );
		}

		return filtersAsStringArray;
	}
}
