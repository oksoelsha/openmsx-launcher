package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class YearFilterTest
{
	private RepositoryGame repositoryGame = RepositoryGame.title( "title" ).system( "system" ).company( "company" ).year( "1983" ).country( "country" ).build();

	@Test
	public void testConstructor()
	{
		new YearFilter( 1990, 0, FilterParameter.EQUAL );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new YearFilter( 16, 32, null );
	}

	@Test( expected = NullPointerException.class )
	public void testIsFilteredNullGame()
	{
		YearFilter yearFilter = new YearFilter( 16, 0, null );

		yearFilter.isFiltered( null, null );
	}

	@Test
	public void testIsFilteredRepositoryGameNull()
	{
		YearFilter yearFilter = new YearFilter( 16, 0, FilterParameter.EQUAL );

		Game game = Game.name( "name" ).build();
		assertTrue( yearFilter.isFiltered( game, null ) );
	}

	@Test
	public void testEqualFilter()
	{
		Game game = Game.name( "name" ).build();

		YearFilter yearFilter1 = new YearFilter( 1983, 0, FilterParameter.EQUAL );
		assertFalse( yearFilter1.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter2 = new YearFilter( 1982, 0, FilterParameter.EQUAL );
		assertTrue( yearFilter2.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter3 = new YearFilter( 1984, 0, FilterParameter.EQUAL );
		assertTrue( yearFilter3.isFiltered( game, repositoryGame ) );
	}

	@Test
	public void testEqualOrLessFilter()
	{
		Game game = Game.name( "name" ).build();

		YearFilter yearFilter1 = new YearFilter( 1983, 0, FilterParameter.EQUAL_OR_LESS );
		assertFalse( yearFilter1.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter2 = new YearFilter( 1982, 0, FilterParameter.EQUAL_OR_LESS );
		assertTrue( yearFilter2.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter3 = new YearFilter( 1984, 0, FilterParameter.EQUAL_OR_LESS );
		assertFalse( yearFilter3.isFiltered( game, repositoryGame ) );
	}

	@Test
	public void testEqualOrGreaterFilter()
	{
		Game game = Game.name( "name" ).build();

		YearFilter yearFilter1 = new YearFilter( 1983, 0, FilterParameter.EQUAL_OR_GREATER );
		assertFalse( yearFilter1.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter2 = new YearFilter( 1982, 0, FilterParameter.EQUAL_OR_GREATER );
		assertFalse( yearFilter2.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter3 = new YearFilter( 1984, 0, FilterParameter.EQUAL_OR_GREATER );
		assertTrue( yearFilter3.isFiltered( game, repositoryGame ) );
	}

	@Test
	public void testLessFilter()
	{
		Game game = Game.name( "name" ).build();

		YearFilter yearFilter1 = new YearFilter( 1983, 0, FilterParameter.LESS );
		assertTrue( yearFilter1.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter2 = new YearFilter( 1982, 0, FilterParameter.LESS );
		assertTrue( yearFilter2.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter3 = new YearFilter( 1984, 0, FilterParameter.LESS );
		assertFalse( yearFilter3.isFiltered( game, repositoryGame ) );
	}

	@Test
	public void testGreaterFilter()
	{
		Game game = Game.name( "name" ).build();

		YearFilter yearFilter1 = new YearFilter( 1983, 0, FilterParameter.GREATER );
		assertTrue( yearFilter1.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter2 = new YearFilter( 1982, 0, FilterParameter.GREATER );
		assertFalse( yearFilter2.isFiltered( game, repositoryGame ) );

		YearFilter yearFilter3 = new YearFilter( 1984, 0, FilterParameter.GREATER );
		assertTrue( yearFilter3.isFiltered( game, repositoryGame ) );
	}

	@Test
	public void testBetweenInclusiveFilter()
	{
		RepositoryGame repositoryGameForThisTest =
				RepositoryGame.title( "title" ).system( "system" ).company( "company" ).year( "1985" ).country( "country" ).build();

		Game game = Game.name( "name" ).build();

		YearFilter yearFilter1 = new YearFilter( 1985, 1987, FilterParameter.BETWEEN_INCLUSIVE );
		assertFalse( yearFilter1.isFiltered( game, repositoryGameForThisTest ) );

		YearFilter yearFilter2 = new YearFilter( 1983, 1985, FilterParameter.BETWEEN_INCLUSIVE );
		assertFalse( yearFilter2.isFiltered( game, repositoryGameForThisTest ) );

		YearFilter yearFilter3 = new YearFilter( 1984, 1986, FilterParameter.BETWEEN_INCLUSIVE );
		assertFalse( yearFilter3.isFiltered( game, repositoryGameForThisTest ) );

		YearFilter yearFilter4 = new YearFilter( 1986, 1987, FilterParameter.BETWEEN_INCLUSIVE );
		assertTrue( yearFilter4.isFiltered( game, repositoryGameForThisTest ) );

		YearFilter yearFilter5 = new YearFilter( 1982, 1984, FilterParameter.BETWEEN_INCLUSIVE );
		assertTrue( yearFilter5.isFiltered( game, repositoryGameForThisTest ) );
	}

	@Test
	public void testClassAnnotation()
	{
		YearFilter yearFilter = new YearFilter( 1983, 0, FilterParameter.EQUAL );

		FilterDescriptor filterType = yearFilter.getClass().getAnnotation( FilterDescriptor.class );

		assertEquals( FilterType.YEAR, filterType.type() );
	}

	@Test
	public void testValue1FieldAnnotationUsedOnce()
	{
		YearFilter yearFilter = new YearFilter( 1983, 0, FilterParameter.EQUAL );

		Field[] fields = yearFilter.getClass().getDeclaredFields();

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
		assertEquals( "year1", fieldName );
	}

	@Test
	public void testValue2FieldAnnotationUsedOnce()
	{
		YearFilter yearFilter = new YearFilter( 1983, 0, FilterParameter.EQUAL );

		Field[] fields = yearFilter.getClass().getDeclaredFields();

		int total = 0;
		String fieldName = null;

		for( int ix = 0; ix < fields.length; ix++ )
		{
			Annotation annotatedField = fields[ix].getAnnotation( Value2Field.class );

			if( annotatedField != null )
			{
				fieldName = fields[ix].getName();
				total++;
			}
		}

		assertEquals( "year2", fieldName );
		assertEquals( 1, total );
	}

	@Test
	public void testParameterFieldAnnotationUsedOnce()
	{
		YearFilter yearFilter = new YearFilter( 1983, 0, FilterParameter.EQUAL );

		Field[] fields = yearFilter.getClass().getDeclaredFields();

		int total = 0;
		String fieldName = null;

		for( int ix = 0; ix < fields.length; ix++ )
		{
			Annotation annotatedField = fields[ix].getAnnotation( ParameterField.class );

			if( annotatedField != null )
			{
				fieldName = fields[ix].getName();
				total++;
			}
		}

		assertEquals( "filterParameter", fieldName );
		assertEquals( 1, total );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		YearFilter yearFilter1 = new YearFilter( 10, 20, FilterParameter.EQUAL_OR_GREATER );
		YearFilter yearFilter2 = new YearFilter( 10, 20, FilterParameter.EQUAL_OR_GREATER );
		YearFilter yearFilter3 = new YearFilter( 11, 20, FilterParameter.EQUAL_OR_GREATER );
		YearFilter yearFilter4 = new YearFilter( 10, 21, FilterParameter.EQUAL_OR_GREATER );
		YearFilter yearFilter5 = new YearFilter( 10, 0, FilterParameter.EQUAL_OR_LESS );

		assertEquals( yearFilter1, yearFilter1 );
		assertEquals( yearFilter1, yearFilter2 );
		assertEquals( yearFilter2, yearFilter1 );
		assertEquals( yearFilter1.hashCode(), yearFilter2.hashCode() );

		assertNotEquals( yearFilter1, this );
		assertNotEquals( yearFilter1, yearFilter3 );
		assertNotEquals( yearFilter2, yearFilter4 );
		assertNotEquals( yearFilter3, yearFilter5 );
		assertNotEquals( yearFilter4, null );
	}
}
