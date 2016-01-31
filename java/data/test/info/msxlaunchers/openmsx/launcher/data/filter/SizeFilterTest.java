package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.filter.SizeFilter;
import info.msxlaunchers.openmsx.launcher.data.game.Game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SizeFilterTest
{
	@Test
	public void testConstructor()
	{
		new SizeFilter( 16, 32, FilterParameter.EQUAL );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new SizeFilter( 16, 32, null );
	}

	@Test( expected = NullPointerException.class )
	public void testIsFilteredNullGame()
	{
		SizeFilter sizeFilter = new SizeFilter( 16, 0, FilterParameter.EQUAL );

		sizeFilter.isFiltered( null, null );
	}

	@Test
	public void testEqualFilter()
	{
		SizeFilter sizeFilter = new SizeFilter( 16, 0, FilterParameter.EQUAL );

		Game game1 = Game.name( "name" ).size( 15 ).build();
		assertTrue( sizeFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).size( 16 ).build();
		assertFalse( sizeFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).size( 17 ).build();
		assertTrue( sizeFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testEqualOrLessFilter()
	{
		SizeFilter sizeFilter = new SizeFilter( 16, 0, FilterParameter.EQUAL_OR_LESS );

		Game game1 = Game.name( "name" ).size( 15 ).build();
		assertFalse( sizeFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).size( 16 ).build();
		assertFalse( sizeFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).size( 17 ).build();
		assertTrue( sizeFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testEqualOrGreaterFilter()
	{
		SizeFilter sizeFilter = new SizeFilter( 16, 0, FilterParameter.EQUAL_OR_GREATER );

		Game game1 = Game.name( "name" ).size( 15 ).build();
		assertTrue( sizeFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).size( 16 ).build();
		assertFalse( sizeFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).size( 17 ).build();
		assertFalse( sizeFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testLessFilter()
	{
		SizeFilter sizeFilter = new SizeFilter( 16, 0, FilterParameter.LESS );

		Game game1 = Game.name( "name" ).size( 15 ).build();
		assertFalse( sizeFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).size( 16 ).build();
		assertTrue( sizeFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).size( 17 ).build();
		assertTrue( sizeFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testGreaterFilter()
	{
		SizeFilter sizeFilter = new SizeFilter( 16, 0, FilterParameter.GREATER );

		Game game1 = Game.name( "name" ).size( 15 ).build();
		assertTrue( sizeFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).size( 16 ).build();
		assertTrue( sizeFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).size( 17 ).build();
		assertFalse( sizeFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testBetweenInclusiveFilter()
	{
		SizeFilter sizeFilter = new SizeFilter( 16, 32, FilterParameter.BETWEEN_INCLUSIVE );

		Game game1 = Game.name( "name" ).size( 15 ).build();
		assertTrue( sizeFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).size( 16 ).build();
		assertFalse( sizeFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).size( 20 ).build();
		assertFalse( sizeFilter.isFiltered( game3, null ) );

		Game game4 = Game.name( "name" ).size( 31 ).build();
		assertFalse( sizeFilter.isFiltered( game4, null ) );

		Game game5 = Game.name( "name" ).size( 32 ).build();
		assertFalse( sizeFilter.isFiltered( game5, null ) );

		Game game6 = Game.name( "name" ).size( 33 ).build();
		assertTrue( sizeFilter.isFiltered( game6, null ) );
	}

	@Test
	public void testClassAnnotation()
	{
		SizeFilter sizeFilter = new SizeFilter( 10, 0, FilterParameter.EQUAL );

		FilterDescriptor filterType = sizeFilter.getClass().getAnnotation( FilterDescriptor.class );

		assertEquals( FilterType.SIZE, filterType.type() );
	}

	@Test
	public void testValue1FieldAnnotationUsedOnce()
	{
		SizeFilter sizeFilter = new SizeFilter( 10, 0, FilterParameter.EQUAL );

		Field[] fields = sizeFilter.getClass().getDeclaredFields();

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
		assertEquals( "size1", fieldName );
	}

	@Test
	public void testValue2FieldAnnotationUsedOnce()
	{
		SizeFilter sizeFilter = new SizeFilter( 10, 0, FilterParameter.EQUAL );

		Field[] fields = sizeFilter.getClass().getDeclaredFields();

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

		assertEquals( "size2", fieldName );
		assertEquals( 1, total );
	}

	@Test
	public void testParameterFieldAnnotationUsedOnce()
	{
		SizeFilter sizeFilter = new SizeFilter( 10, 0, FilterParameter.EQUAL );

		Field[] fields = sizeFilter.getClass().getDeclaredFields();

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
		SizeFilter sizeFilter1 = new SizeFilter( 10, 20, FilterParameter.EQUAL_OR_GREATER );
		SizeFilter sizeFilter2 = new SizeFilter( 10, 20, FilterParameter.EQUAL_OR_GREATER );
		SizeFilter sizeFilter3 = new SizeFilter( 11, 20, FilterParameter.EQUAL_OR_GREATER );
		SizeFilter sizeFilter4 = new SizeFilter( 10, 21, FilterParameter.EQUAL_OR_GREATER );
		SizeFilter sizeFilter5 = new SizeFilter( 10, 0, FilterParameter.EQUAL_OR_LESS );

		assertEquals( sizeFilter1, sizeFilter1 );
		assertEquals( sizeFilter1, sizeFilter2 );
		assertEquals( sizeFilter2, sizeFilter1 );
		assertEquals( sizeFilter1.hashCode(), sizeFilter2.hashCode() );
		assertNotEquals( sizeFilter1, this );
		assertNotEquals( sizeFilter1, sizeFilter3 );
		assertNotEquals( sizeFilter2, sizeFilter4 );
		assertNotEquals( sizeFilter3, sizeFilter5 );
		assertNotEquals( sizeFilter4, null );
	}
}
