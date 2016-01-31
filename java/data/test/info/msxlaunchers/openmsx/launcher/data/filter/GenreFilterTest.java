package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import info.msxlaunchers.openmsx.launcher.data.filter.GenreFilter;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class GenreFilterTest
{
	@Test
	public void testConstructor()
	{
		new GenreFilter( Genre.ACTION );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new GenreFilter( null );
	}

	@Test( expected = NullPointerException.class )
	public void testIsFilteredNullGame()
	{
		GenreFilter genreFilter = new GenreFilter( Genre.ADVENTURE_ALL );

		genreFilter.isFiltered( null, null );
	}

	@Test
	public void testIsFiltered()
	{
		GenreFilter genreFilter = new GenreFilter( Genre.ADVENTURE_ALL );

		Game game1 = Game.name( "name" ).genre1( Genre.ARCADE ).build();
		assertTrue( genreFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).genre1( Genre.BEAT_EM_UP ).genre2( Genre.ADVENTURE_ALL ).build();
		assertFalse( genreFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).genre1( Genre.ADVENTURE_ALL ).genre2( Genre.BOARD_GAMES ).build();
		assertFalse( genreFilter.isFiltered( game3, null ) );

		Game game4 = Game.name( "name" ).genre1( Genre.CARD_GAMES ).genre2( Genre.GAMBLING_FRUIT_MACHINE ).build();
		assertTrue( genreFilter.isFiltered( game4, null ) );
	}

	@Test
	public void testClassAnnotation()
	{
		GenreFilter genreFilter = new GenreFilter( Genre.ACTION );

		FilterDescriptor filterType = genreFilter.getClass().getAnnotation( FilterDescriptor.class );

		assertEquals( FilterType.GENRE, filterType.type() );
	}

	@Test
	public void testValue1FieldAnnotationUsedOnce()
	{
		GenreFilter genreFilter = new GenreFilter( Genre.ACTION );

		Field[] fields = genreFilter.getClass().getDeclaredFields();

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
		assertEquals( "genre", fieldName );
	}

	@Test
	public void testValue2FieldAnnotationNotUsed()
	{
		GenreFilter genreFilter = new GenreFilter( Genre.ACTION );

		Field[] fields = genreFilter.getClass().getDeclaredFields();

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
		GenreFilter genreFilter = new GenreFilter( Genre.ACTION );

		Field[] fields = genreFilter.getClass().getDeclaredFields();

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
		GenreFilter genreFilter1 = new GenreFilter( Genre.ADVENTURE_TEXT_AND_GFX );
		GenreFilter genreFilter2 = new GenreFilter( Genre.ADVENTURE_TEXT_AND_GFX );
		GenreFilter genreFilter3 = new GenreFilter( Genre.ADVENTURE_TEXT_ONLY );

		assertTrue( genreFilter1.equals( genreFilter2 ) );
		assertTrue( genreFilter2.equals( genreFilter1 ) );
		assertTrue( genreFilter3.equals( genreFilter3 ) );
		assertFalse( genreFilter1.equals( genreFilter3 ) );
		assertFalse( genreFilter3.equals( genreFilter2 ) );
		assertFalse( genreFilter3.equals( null ) );
		assertFalse( genreFilter3.equals( this ) );

		assertEquals( genreFilter1.hashCode(), genreFilter2.hashCode() );
		assertNotEquals( genreFilter1.hashCode(), genreFilter3.hashCode() );
		assertNotEquals( genreFilter2.hashCode(), genreFilter3.hashCode() );
	}
}
