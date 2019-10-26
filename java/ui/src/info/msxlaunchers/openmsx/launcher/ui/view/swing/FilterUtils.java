/*
 * Copyright 2019 Sam Elsharif
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterType;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
import info.msxlaunchers.openmsx.launcher.data.game.constants.MSXGeneration;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Sound;

/**
 * Utility class containing methods to convert filter monikers to display strings for the UI
 * 
 * @since v1.12
 * @author Sam Elsharif
 *
 */
class FilterUtils
{
	/**
	 * Returns a list of filter descriptions to display on the UI given a list of monikers
	 * 
	 * @param filterMonikers List of filter monikers. Cannot be null
	 * @param messages Translations map. Cannot be null
	 * @return List of filter descriptions
	 */
	static List<String> getFiltersStringRepresentation(List<String> filterMonikers, Map<String, String> messages)
	{
		return filterMonikers.stream()
				.sorted()
				.map(FilterUtils::getFilterItemObject)
				.map(f -> messages.get(f.type.toString()) + ": " + getFilterDescriptor(f.type, f.value1, f.value2, f.parameter, messages))
				.collect(Collectors.toList());
	}

	/**
	 * Converts the given filter moniker to an instance of FilterItemObject
	 * 
	 * @param filterMoniker Filter moniker
	 * @return Instance of FilterItemObject
	 */
	static FilterItemObject getFilterItemObject(String filterMoniker)
	{
		String[] filterMonikerParts = filterMoniker.split(":");

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

		return new FilterItemObject(type, value1, value2, parameter);
	}

	/**
	 * Returns a string representation of the filter suitable for display on the UI
	 * 
	 * @param type Filter type
	 * @param value1 Value of filter. Cannot be null
	 * @param value2 Second value of some of the filters. Can be null
	 * @param parameter Parameter used by some of the filters
	 * @param messages Translation map
	 * @return String representation of the filter
	 */
	static String getFilterDescriptor(FilterType type, String value1, String value2, FilterParameter parameter, Map<String,String> messages)
	{
		String descriptor = null;

		switch(type)
		{
			case COMPANY:
			case VIDEO_SOURCE:
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

    private static String getFilterWithParameterDescriptor(String value1, String value2, FilterParameter parameter, String valueUnit)
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
}
