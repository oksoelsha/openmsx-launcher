package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import info.msxlaunchers.openmsx.launcher.data.filter.CountryFilter;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CountryFilterTest
{
	@Test
	public void testConstructor()
	{
		new CountryFilter( "country" );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new CountryFilter( null );
	}

	@Test
	public void testIsFilteredRepositoryGameNull()
	{
		CountryFilter countryFilter = new CountryFilter( "country" );

		Game game = Game.name( "name" ).build();
		assertTrue( countryFilter.isFiltered( game, null ) );
	}

	@Test
	public void testIsFilteredCountryMatch()
	{
		String country = "country";

		RepositoryGame repositoryGame = new RepositoryGame( "title", "company", "year", country );

		CountryFilter countryFilter = new CountryFilter( country );

		Game game = Game.name( "name" ).build();
		assertFalse( countryFilter.isFiltered( game, repositoryGame ) );
	}

	@Test
	public void testIsFilteredCountryNotMatch()
	{
		RepositoryGame repositoryGame = new RepositoryGame( "title", "company", "year", "country" );

		CountryFilter countryFilter = new CountryFilter( "different-country" );

		Game game = Game.name( "name" ).build();
		assertTrue( countryFilter.isFiltered( game, repositoryGame ) );
	}

	@Test
	public void testClassAnnotation()
	{
		CountryFilter countryFilter = new CountryFilter( "countryCode" );

		FilterDescriptor filterType = countryFilter.getClass().getAnnotation( FilterDescriptor.class );

		assertEquals( FilterType.COUNTRY, filterType.type() );
	}

	@Test
	public void testValue1FieldAnnotationUsedOnce()
	{
		CountryFilter countryFilter = new CountryFilter( "countryCode" );

		Field[] fields = countryFilter.getClass().getDeclaredFields();

		int total = 0;
		String fieldName = null;

		for( int ix = 0; ix < fields.length; ix++ )
		{
			Annotation annotatedField = fields[ix].getAnnotation( Value1Field.class );

			if( annotatedField != null )
			{
				fieldName = fields[ix].getName();
				total++;
			}
		}

		assertEquals( 1, total );
		assertEquals( "country", fieldName );
	}

	@Test
	public void testValue2FieldAnnotationNotUsed()
	{
		CountryFilter countryFilter = new CountryFilter( "countryCode" );

		Field[] fields = countryFilter.getClass().getDeclaredFields();

		int total = 0;
		for( int ix = 0; ix < fields.length; ix++ )
		{
			Annotation annotatedField = fields[ix].getAnnotation( Value2Field.class );

			if( annotatedField != null )
			{
				total++;
			}
		}

		assertEquals( 0, total );
	}

	@Test
	public void testParameterFieldAnnotationNotUsed()
	{
		CountryFilter countryFilter = new CountryFilter( "countryCode" );

		Field[] fields = countryFilter.getClass().getDeclaredFields();

		int total = 0;
		for( int ix = 0; ix < fields.length; ix++ )
		{
			Annotation annotatedField = fields[ix].getAnnotation( ParameterField.class );

			if( annotatedField != null )
			{
				total++;
			}
		}

		assertEquals( 0, total );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		CountryFilter countryFilter1 = new CountryFilter( "countryCode" );
		CountryFilter countryFilter2 = new CountryFilter( "countryCode" );
		CountryFilter countryFilter3 = new CountryFilter( "countryCodeDiff" );

		assertTrue( countryFilter1.equals( countryFilter2 ) );
		assertTrue( countryFilter2.equals( countryFilter1 ) );
		assertTrue( countryFilter3.equals( countryFilter3 ) );
		assertFalse( countryFilter1.equals( countryFilter3 ) );
		assertFalse( countryFilter3.equals( countryFilter2 ) );
		assertFalse( countryFilter3.equals( null ) );
		assertFalse( countryFilter3.equals( this ) );

		assertEquals( countryFilter1.hashCode(), countryFilter2.hashCode() );
		assertNotEquals( countryFilter1.hashCode(), countryFilter3.hashCode() );
		assertNotEquals( countryFilter2.hashCode(), countryFilter3.hashCode() );
	}
}
