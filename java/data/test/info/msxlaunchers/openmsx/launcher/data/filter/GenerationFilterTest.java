package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import info.msxlaunchers.openmsx.launcher.data.filter.GenerationFilter;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.MSXGeneration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class GenerationFilterTest
{
	@Test
	public void testConstructor()
	{
		new GenerationFilter( MSXGeneration.MSX );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new GenerationFilter( null );
	}

	@Test( expected = NullPointerException.class )
	public void testIsFilteredNullGame()
	{
		GenerationFilter generationFilter = new GenerationFilter( MSXGeneration.MSX );

		generationFilter.isFiltered( null, null );
	}

	@Test
	public void testIsFilteredForMSX()
	{
		GenerationFilter generationFilter = new GenerationFilter( MSXGeneration.MSX );

		Game game1 = Game.name( "name" ).isMSX( true ).build();
		assertFalse( generationFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isMSX2( true ).build();
		assertTrue( generationFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).isMSX2Plus( true ).build();
		assertTrue( generationFilter.isFiltered( game3, null ) );

		Game game4 = Game.name( "name" ).isTurboR( true ).build();
		assertTrue( generationFilter.isFiltered( game4, null ) );
	}

	@Test
	public void testIsFilteredForMSX2()
	{
		GenerationFilter generationFilter = new GenerationFilter( MSXGeneration.MSX2 );

		Game game1 = Game.name( "name" ).isMSX( true ).build();
		assertTrue( generationFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isMSX2( true ).build();
		assertFalse( generationFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).isMSX2Plus( true ).build();
		assertTrue( generationFilter.isFiltered( game3, null ) );

		Game game4 = Game.name( "name" ).isTurboR( true ).build();
		assertTrue( generationFilter.isFiltered( game4, null ) );
	}

	@Test
	public void testIsFilteredForMSX2Plus()
	{
		GenerationFilter generationFilter = new GenerationFilter( MSXGeneration.MSX2Plus );

		Game game1 = Game.name( "name" ).isMSX( true ).build();
		assertTrue( generationFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isMSX2( true ).build();
		assertTrue( generationFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).isMSX2Plus( true ).build();
		assertFalse( generationFilter.isFiltered( game3, null ) );

		Game game4 = Game.name( "name" ).isTurboR( true ).build();
		assertTrue( generationFilter.isFiltered( game4, null ) );
	}

	@Test
	public void testIsFilteredForTurboR()
	{
		GenerationFilter generationFilter = new GenerationFilter( MSXGeneration.TURBO_R );

		Game game1 = Game.name( "name" ).isMSX( true ).build();
		assertTrue( generationFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isMSX2( true ).build();
		assertTrue( generationFilter.isFiltered( game2, null ) );

		Game game3 = Game.name( "name" ).isMSX2Plus( true ).build();
		assertTrue( generationFilter.isFiltered( game3, null ) );

		Game game4 = Game.name( "name" ).isTurboR( true ).build();
		assertFalse( generationFilter.isFiltered( game4, null ) );
	}

	@Test
	public void testClassAnnotation()
	{
		GenerationFilter generationFilter = new GenerationFilter( MSXGeneration.MSX );

		FilterDescriptor filterType = generationFilter.getClass().getAnnotation( FilterDescriptor.class );

		assertEquals( FilterType.GENERATION, filterType.type() );
	}

	@Test
	public void testValue1FieldAnnotationUsedOnce()
	{
		GenerationFilter generationFilter = new GenerationFilter( MSXGeneration.MSX );

		Field[] fields = generationFilter.getClass().getDeclaredFields();

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
		assertEquals( "generation", fieldName );
	}

	@Test
	public void testValue2FieldAnnotationNotUsed()
	{
		GenerationFilter generationFilter = new GenerationFilter( MSXGeneration.MSX );

		Field[] fields = generationFilter.getClass().getDeclaredFields();

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
		GenerationFilter generationFilter = new GenerationFilter( MSXGeneration.MSX );

		Field[] fields = generationFilter.getClass().getDeclaredFields();

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
		GenerationFilter generationFilter1 = new GenerationFilter( MSXGeneration.MSX );
		GenerationFilter generationFilter2 = new GenerationFilter( MSXGeneration.MSX );
		GenerationFilter generationFilter3 = new GenerationFilter( MSXGeneration.MSX2Plus );

		assertTrue( generationFilter1.equals( generationFilter2 ) );
		assertTrue( generationFilter2.equals( generationFilter1 ) );
		assertTrue( generationFilter3.equals( generationFilter3 ) );
		assertFalse( generationFilter1.equals( generationFilter3 ) );
		assertFalse( generationFilter3.equals( generationFilter2 ) );
		assertFalse( generationFilter3.equals( null ) );
		assertFalse( generationFilter3.equals( this ) );

		assertEquals( generationFilter1.hashCode(), generationFilter2.hashCode() );
		assertNotEquals( generationFilter1.hashCode(), generationFilter3.hashCode() );
		assertNotEquals( generationFilter2.hashCode(), generationFilter3.hashCode() );
	}
}
