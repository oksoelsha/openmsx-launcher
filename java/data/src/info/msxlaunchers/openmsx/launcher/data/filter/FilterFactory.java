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
package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
import info.msxlaunchers.openmsx.launcher.data.game.constants.MSXGeneration;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Sound;

/**
 * Factory that contains methods to create filters, get filter monikers and return filter field values
 * 
 * @since v1.2
 * @author Sam Elsharif
 *
 */
public class FilterFactory
{
	private static final String MONIKER_SEPARATOR = ":";

	/**
	 * Returns an filter instance based on the arguments
	 * 
	 * @param type Type of filter based on <code>FilterType</code> enum class
	 * @param value1 Value of the first field. Cannot be null
	 * @param value2 Value of the second field. Can be null for certain types of filters
	 * @param parameter Parameter field based on <code>FilterParameter</code> enum class. Can be null for certain types of filters 
	 * @return Instance of <code>Filter</code>
	 */
	public static Filter createFilter( String type, String value1, String value2, String parameter )
	{
		//value2 and parameter are optional so they could be null
		FilterParameter effectiveParameter = null;
		if( parameter != null )
		{
			effectiveParameter = FilterParameter.valueOf( parameter );
		}

		return createFilter( FilterType.valueOf( type ), value1, value2, effectiveParameter );
	}

	/**
	 * Returns an filter instance based on the arguments
	 * 
	 * @param type Type of filter based on <code>FilterType</code> enum class
	 * @param value1 Value of the first field. Cannot be null
	 * @param value2 Value of the second field. Can be null for certain types of filters
	 * @param parameter <code>FilterParameter</code> enum instance. Can be null for certain types of filters 
	 * @return Instance of <code>Filter</code>
	 */
	public static Filter createFilter( FilterType type, String value1, String value2, FilterParameter parameter )
	{
		Objects.requireNonNull( type );
		Objects.requireNonNull( value1 );

		Filter filter = null;

		try
		{
			switch( type )
			{
				case COMPANY:
					filter = new CompanyFilter( value1 );
					break;
				case COUNTRY:
					filter = new CountryFilter( value1 );
					break;
				case GENERATION:
					filter = new GenerationFilter( MSXGeneration.valueOf( value1 ) );
					break;
				case GENRE:
					filter = new GenreFilter( Genre.valueOf( value1 ) );
					break;
				case MEDIUM:
					filter = new MediumFilter( Medium.valueOf( value1 ) );
					break;
				case SIZE:
					filter = new SizeFilter( Utils.getNumber( value1 ), Utils.getNumber( value2 ), parameter );
					break;
				case SOUND:
					filter = new SoundFilter( Sound.valueOf( value1 ) );
					break;
				case YEAR:
					filter = new YearFilter( Utils.getNumber( value1 ), Utils.getNumber( value2 ), parameter );
					break;
			}
		}
		catch( IllegalArgumentException iae )
		{
			//this could happen when some enum arguments are wrong
			throw iae;
		}

		return filter;
	}

	/**
	 * Returns a moniker that represents the given filter. Monikers are of the format:
	 * Type:Field1:Field2:Parameter
	 * If Parameter is null then the format is simplified to:
	 * Type:Field1:Field2:
	 * If Field2 and Parameter are null then the format is simplified to:
	 * Type:Field1::
	 * 
	 * @param filter <code>Filter</code> instance
	 * @return Moniker of the given filter
	 */
	public static String getFilterMoniker( Filter filter )
	{
		Objects.requireNonNull( filter );

		StringBuilder builder = new StringBuilder();

		builder.append( getFilterType( filter ) ).append( MONIKER_SEPARATOR );
		builder.append( getAnnotatedFieldValue( filter, Value1Field.class ) ).append( MONIKER_SEPARATOR );

		//Value1Field and ParameterField can be null
		String value2 = getAnnotatedFieldValue( filter, Value2Field.class );
		builder.append( value2 == null ? "" : value2 ).append( MONIKER_SEPARATOR );

		String parameter = getAnnotatedFieldValue( filter, ParameterField.class );
		builder.append( parameter == null ? "" : parameter );

		return builder.toString();
	}

	/**
	 * Returns <code>FilterType</code> of given filter
	 * 
	 * @param filter <code>Filter</code> instance
	 * @return <code>FilterType</code> instance
	 */
	public static FilterType getFilterType( Filter filter )
	{
		Objects.requireNonNull( filter );

		return filter.getClass().getAnnotation( FilterDescriptor.class ).type();
	}

	/**
	 * Returns value of the field with the given annotation in a filter
	 * 
	 * @param filter <code>Filter</code> instance
	 * @param annotation Annotation of the field
	 * @return Value of the annotated field. If no field with the given annotation is found, return null
	 */
	public static String getAnnotatedFieldValue( Filter filter, Class<? extends Annotation> annotation )
	{
		Objects.requireNonNull( filter );
		Objects.requireNonNull( annotation );

		String value = null;
		Field[] fields = filter.getClass().getDeclaredFields();

		boolean done = false;
		for( int ix = 0; ix < fields.length && !done; ix++ )
		{
			Annotation annotatedField = fields[ix].getAnnotation( annotation );

			if( annotatedField != null )
			{
				try
				{
					fields[ix].setAccessible( true );
					value = fields[ix].get( filter ).toString();
					done = true;
				}
				catch( IllegalAccessException iae )
				{
					//this should not happen
				}
			}
		}

		return value;
	}
}
