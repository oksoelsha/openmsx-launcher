package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.VideoSource;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class VideoSourceFilterTest
{
	@Test
	public void testConstructor()
	{
		new VideoSourceFilter( VideoSource.MSX );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new VideoSourceFilter( null );
	}

	@Test( expected = NullPointerException.class )
	public void testIsFilteredNullGame()
	{
		VideoSourceFilter videoSourceFilter = new VideoSourceFilter( VideoSource.MSX );

		videoSourceFilter.isFiltered( null, null );
	}

	@Test
	public void testIsFilteredForMSX()
	{
		VideoSourceFilter videoSourceFilter = new VideoSourceFilter( VideoSource.MSX );

		Game game1 = Game.name( "name1" ).connectGFX9000( true ).build();
		assertTrue( videoSourceFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name2" ).connectGFX9000( false ).build();
		assertFalse( videoSourceFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testIsFilteredForGFX9000()
	{
		VideoSourceFilter videoSourceFilter = new VideoSourceFilter( VideoSource.GFX9000 );

		Game game1 = Game.name( "name1" ).connectGFX9000( true ).build();
		assertFalse( videoSourceFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name2" ).connectGFX9000( false ).build();
		assertTrue( videoSourceFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testClassAnnotation()
	{
		VideoSourceFilter videoSourceFilter = new VideoSourceFilter( VideoSource.MSX );

		FilterDescriptor filterType = videoSourceFilter.getClass().getAnnotation( FilterDescriptor.class );

		assertEquals( FilterType.VIDEO_SOURCE, filterType.type() );
	}

	@Test
	public void testValue1FieldAnnotationUsedOnce()
	{
		VideoSourceFilter videoSourceFilter = new VideoSourceFilter( VideoSource.MSX );

		Field[] fields = videoSourceFilter.getClass().getDeclaredFields();

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
		assertEquals( "videoSource", fieldName );
	}

	@Test
	public void testValue2FieldAnnotationNotUsed()
	{
		VideoSourceFilter videoSourceFilter = new VideoSourceFilter( VideoSource.MSX );

		Field[] fields = videoSourceFilter.getClass().getDeclaredFields();

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
		VideoSourceFilter videoSourceFilter = new VideoSourceFilter( VideoSource.MSX );

		Field[] fields = videoSourceFilter.getClass().getDeclaredFields();

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
		VideoSourceFilter videoSourceFilter1 = new VideoSourceFilter( VideoSource.MSX );
		VideoSourceFilter videoSourceFilter2 = new VideoSourceFilter( VideoSource.MSX );
		VideoSourceFilter videoSourceFilter3 = new VideoSourceFilter( VideoSource.GFX9000 );

		assertTrue( videoSourceFilter1.equals( videoSourceFilter2 ) );
		assertTrue( videoSourceFilter2.equals( videoSourceFilter1 ) );
		assertTrue( videoSourceFilter3.equals( videoSourceFilter3 ) );
		assertFalse( videoSourceFilter1.equals( videoSourceFilter3 ) );
		assertFalse( videoSourceFilter3.equals( videoSourceFilter2 ) );
		assertFalse( videoSourceFilter3.equals( null ) );
		assertFalse( videoSourceFilter3.equals( this ) );

		assertEquals( videoSourceFilter1.hashCode(), videoSourceFilter2.hashCode() );
		assertNotEquals( videoSourceFilter1.hashCode(), videoSourceFilter3.hashCode() );
		assertNotEquals( videoSourceFilter2.hashCode(), videoSourceFilter3.hashCode() );
	}
}
