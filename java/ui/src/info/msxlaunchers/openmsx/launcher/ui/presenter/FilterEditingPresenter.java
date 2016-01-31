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

import java.util.Set;

import info.msxlaunchers.openmsx.launcher.data.filter.Filter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterType;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

/**
 * Interface for Filter Editing UI Model and Presenter
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public interface FilterEditingPresenter
{
	/**
	 * Called when user requests the add new filter screen (e.g. by clicking on the New filter menu item)
	 * 
	 * @param currentLanguage Current language
	 * @param currentRightToLeft Orientation of the current language
	 * @throws LauncherException
	 */
	void onRequestAddFilterScreen( Language currentLanguage, boolean currentRightToLeft ) throws LauncherException;

	/**
	 * Called when user requests the edit an existing filter (e.g. by clicking on the Edit filter menu item button)
	 * 
	 * @param currentLanguage Current language
	 * @param currentRightToLeft Orientation of the current language
	 * @param filterName Filter name. Is null when editing is for an untitled filter
	 * @param filterItems Set containing filter items
	 * @throws LauncherException
	 */
	void onRequestEditFilterScreen( Language currentLanguage, boolean currentRightToLeft, String filterName, Set<Filter> filterItems ) throws LauncherException;

	/**
	 * Called when user requests to add a filter item to the current filter
	 * 
	 * @param type Filter type
	 * @param value1 First value of the filter. Cannot be null
	 * @param value2 Second value of the filter. Only applicable to some filter types (e.g. size), and therefore can be null
	 * @param parameter Filter parameter. Only applicable to some filter types (e.g. size), and therefore can be null
	 * @return True if filter was added successfully and false if it existed already
	 * @throws LauncherException
	 */
	boolean onAddToFilterListAndApply( FilterType type, String value1, String value2, FilterParameter parameter ) throws LauncherException;

	/**
	 * Called when user requests to remove a filter item from the current filter
	 * 
	 * @param type Filter type
	 * @param value1 First value of the filter. Cannot be null
	 * @param value2 Second value of the filter. Only applicable to some filter types (e.g. size), and therefore can be null
	 * @param parameter Filter parameter. Only applicable to some filter types (e.g. size), and therefore can be null
	 * @throws LauncherException
	 */
	void onRemoveFromFilterList( FilterType type, String value1, String value2, FilterParameter parameter ) throws LauncherException;

	/**
	 * Called when user requests to save the filter
	 * 
	 * @param filterName Name of filter to save
	 * @throws LauncherException
	 */
	void onRequestSaveFilterAction( String filterName ) throws LauncherException;

	/**
	 * Called when user requests to update a filter including renaming
	 * 
	 * @param oldFilterName Old filter name
	 * @param newFilterName New filter name. Can be the same as the old filter name
	 * @throws LauncherException
	 */
	void onRequestUpdateFilterAction( String oldFilterName, String newFilterName ) throws LauncherException;

	/**
	 * Called when user requests to close the window (e.g. by clicking on the Close button)
	 */
	void onRequestClose();
}
