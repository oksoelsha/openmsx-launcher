package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import info.msxlaunchers.openmsx.launcher.data.filter.CompanyFilter;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CompanyFilterTest
{
	@Test
	public void testConstructor()
	{
		new CompanyFilter( "company" );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new CompanyFilter( null );
	}

	@Test
	public void testIsFilteredRepositoryGameNull()
	{
		CompanyFilter countryFilter = new CompanyFilter( "company" );

		Game game = Game.name( "name" ).build();
		assertTrue( countryFilter.isFiltered( game, null ) );
	}

	@Test
	public void testIsFilteredCompanyMatch()
	{
		String company = "company";

		RepositoryGame repositoryGame = RepositoryGame.title( "title" ).system( "system" ).company( "company" ).year( "year" ).country( "country" ).build();

		CompanyFilter companyFilter = new CompanyFilter( company );

		Game game = Game.name( "name" ).build();
		assertFalse( companyFilter.isFiltered( game, repositoryGame ) );
	}

	@Test
	public void testIsFilteredCompanyNotMatch()
	{
		RepositoryGame repositoryGame = RepositoryGame.title( "title" ).system( "system" ).company( "company" ).year( "year" ).country( "country" ).build();

		CompanyFilter companyFilter = new CompanyFilter( "different-company" );

		Game game = Game.name( "name" ).build();
		assertTrue( companyFilter.isFiltered( game, repositoryGame ) );
	}

	@Test
	public void testClassAnnotation()
	{
		CompanyFilter companyFilter = new CompanyFilter( "companyName" );

		FilterDescriptor filterType = companyFilter.getClass().getAnnotation( FilterDescriptor.class );

		assertEquals( FilterType.COMPANY, filterType.type() );
	}

	@Test
	public void testValue1FieldAnnotationUsedOnce()
	{
		CompanyFilter companyFilter = new CompanyFilter( "companyName" );

		Field[] fields = companyFilter.getClass().getDeclaredFields();

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
		assertEquals( "company", fieldName );
	}

	@Test
	public void testValue2FieldAnnotationNotUsed()
	{
		CompanyFilter companyFilter = new CompanyFilter( "companyName" );

		Field[] fields = companyFilter.getClass().getDeclaredFields();

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
		CompanyFilter companyFilter = new CompanyFilter( "companyName" );

		Field[] fields = companyFilter.getClass().getDeclaredFields();

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
		CompanyFilter companyFilter1 = new CompanyFilter( "companyName" );
		CompanyFilter companyFilter2 = new CompanyFilter( "companyName" );
		CompanyFilter companyFilter3 = new CompanyFilter( "companyNameDiff" );

		assertTrue( companyFilter1.equals( companyFilter2 ) );
		assertTrue( companyFilter2.equals( companyFilter1 ) );
		assertTrue( companyFilter3.equals( companyFilter3 ) );
		assertFalse( companyFilter1.equals( companyFilter3 ) );
		assertFalse( companyFilter3.equals( companyFilter2 ) );
		assertFalse( companyFilter3.equals( null ) );
		assertFalse( companyFilter3.equals( this ) );

		assertEquals( companyFilter1.hashCode(), companyFilter2.hashCode() );
		assertNotEquals( companyFilter1.hashCode(), companyFilter3.hashCode() );
		assertNotEquals( companyFilter2.hashCode(), companyFilter3.hashCode() );
	}
}
