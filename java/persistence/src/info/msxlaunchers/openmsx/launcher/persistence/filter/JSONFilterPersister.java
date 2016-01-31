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
package info.msxlaunchers.openmsx.launcher.persistence.filter;

import info.msxlaunchers.openmsx.launcher.data.filter.Filter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterFactory;
import info.msxlaunchers.openmsx.launcher.data.filter.ParameterField;
import info.msxlaunchers.openmsx.launcher.data.filter.Value1Field;
import info.msxlaunchers.openmsx.launcher.data.filter.Value2Field;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation of the <code>FilterPersister</code> interface that persists filters on and retrieves them from the
 * local hard disk. This implementation caches the retrieved filters to avoid unnecessarily getting them from disk every
 * time they're needed. That's why this class was designated with Guice's Singleton annotation to keep only one instance
 * of it in the lifetime of the Guice injector (which is the lifetime of the entire application)
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
@Singleton
final class JSONFilterPersister implements FilterPersister
{
	private final File filtersFile;

	private final static String FILTERS_DIRECTORY = "filters";
	private final static String FILTERS_FILENAME = "filters.json";

	private final static String JSON_NAME_FIELD = "name";
	private final static String JSON_FILTERS_FIELD = "filters";
	private final static String JSON_TYPE_FIELD = "type";
	private final static String JSON_VALUE1_FIELD = "value1";
	private final static String JSON_VALUE2_FIELD = "value2";
	private final static String JSON_FILTER_PRAMETER_FIELD = "filterParameter";

	private final static String UNUSED_NUMERIC_VALUE = "0";

	private Map<String,Set<Filter>> filtersMap = null;

	@Inject
	JSONFilterPersister( @Named("UserDataDirectory") String userDataDirectory )
	{
		File filtersDirectory = new File( userDataDirectory, FILTERS_DIRECTORY );

		//create this directory if it doesn't exist
		if( !filtersDirectory.exists() )
		{
			filtersDirectory.mkdir();
		}

		this.filtersFile = new File( filtersDirectory, FILTERS_FILENAME );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.filter.persistence.FilterPersister#saveFilters(java.lang.String, java.util.List)
	 */
	@Override
	public void saveFilter( String name, Set<Filter> filters ) throws FilterSetAlreadyExistsException, IOException
	{
		initializeFiltersMapIfNecessary();

		if( filtersMap.containsKey( name ) )
		{
			throw new FilterSetAlreadyExistsException( name );
		}

		filtersMap.put( name, filters );

		flushFiltersMapToDisk();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.filter.persistence.FilterPersister#deleteFilters(java.lang.String)
	 */
	@Override
	public void deleteFilter( String name ) throws IOException
	{
		initializeFiltersMapIfNecessary();

		filtersMap.remove( name );

		flushFiltersMapToDisk();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.filter.persistence.FilterPersister#getFilters(java.lang.String)
	 */
	@Override
	public Set<Filter> getFilter( String name ) throws FilterSetNotFoundException
	{
		initializeFiltersMapIfNecessary();

		if( !filtersMap.containsKey( name ) )
		{
			throw new FilterSetNotFoundException( name );
		}

		return Collections.unmodifiableSet( filtersMap.get( name ) );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.filter.FilterPersister#getFilterNames()
	 */
	@Override
	public Set<String> getFilterNames()
	{
		initializeFiltersMapIfNecessary();

		return Collections.unmodifiableSet( filtersMap.keySet() );
	}

	private void initializeFiltersMapIfNecessary()
	{
		if( filtersMap == null )
		{
			filtersMap = new HashMap<String,Set<Filter>>();

			//read all filters from the JSON file
			try( InputStream stream = new FileInputStream( filtersFile ) )
			{
				parseJASONFile( stream );
			}
			catch( IOException e )
			{
				//this means that there's no file. Most likely no filters were ever created before. Ignore.
			}
		}
	}

	private void parseJASONFile( InputStream stream )
	{
		JSONArray jsonArray = (JSONArray)JSONValue.parse( stream );

		for( Object array: jsonArray )
		{
			JSONObject jsonObj = (JSONObject)array;
			String filterName = jsonObj.get( JSON_NAME_FIELD ).toString();

			JSONArray jsonFiltersArray = (JSONArray)jsonObj.get( JSON_FILTERS_FIELD );
			Set<Filter> filtersSet = new HashSet<Filter>();
			for( Object jsonFilters: jsonFiltersArray )
			{
				JSONObject jsonFiltersObj = (JSONObject)jsonFilters;

				Filter filter = FilterFactory.createFilter(
						getJSONFieldValue( jsonFiltersObj.get( JSON_TYPE_FIELD ) ),
						getJSONFieldValue( jsonFiltersObj.get( JSON_VALUE1_FIELD ) ),
						getJSONFieldValue( jsonFiltersObj.get( JSON_VALUE2_FIELD ) ),
						getJSONFieldValue( jsonFiltersObj.get( JSON_FILTER_PRAMETER_FIELD ) ) );

				filtersSet.add( filter );
			}

			filtersMap.put( filterName, filtersSet );
		} 
	}

	private String getJSONFieldValue( Object field )
	{
		String value;

		if( field == null )
		{
			value = null;
		}
		else
		{
			value = field.toString();
		}

		return value;
	}

	private void flushFiltersMapToDisk() throws IOException
	{
		JSONArray jsonArray = new JSONArray();

		for( String name: filtersMap.keySet() )
		{
			JSONObject namedJsonObj = new JSONObject();
			namedJsonObj.put( JSON_NAME_FIELD, name );

			JSONArray jsonFiltersArray = new JSONArray();

			Set<Filter> filters = filtersMap.get( name );

			for( Filter filter: filters )
			{
				JSONObject filtersJsonObj = new JSONObject();

				filtersJsonObj.put( JSON_TYPE_FIELD, FilterFactory.getFilterType( filter ) );

				setFiltersJSONObject( filtersJsonObj, JSON_VALUE1_FIELD, FilterFactory.getAnnotatedFieldValue( filter, Value1Field.class ) );
				setFiltersJSONObject( filtersJsonObj, JSON_VALUE2_FIELD, FilterFactory.getAnnotatedFieldValue( filter, Value2Field.class ) );
				setFiltersJSONObject( filtersJsonObj, JSON_FILTER_PRAMETER_FIELD, FilterFactory.getAnnotatedFieldValue( filter, ParameterField.class ) );

				jsonFiltersArray.add( filtersJsonObj );
			}

			namedJsonObj.put( JSON_FILTERS_FIELD, jsonFiltersArray );

			jsonArray.add( namedJsonObj );
		}

		try( BufferedWriter bufferedWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( filtersFile ), "UTF-8" ) ) )
		{
			bufferedWriter.write( jsonArray.toJSONString() );
		}
	}

	private void setFiltersJSONObject( JSONObject jsonObject, String key, String value )
	{
		//for the time being, if a value of a field is "0" then consider it null. This is the case for the filters that
		//take in integers or long numbers, and a 0 is the string representation of zero, which means unused.
		if( value != null && !value.equals( UNUSED_NUMERIC_VALUE ) )
		{
			jsonObject.put( key, value );
		}
	}
}
