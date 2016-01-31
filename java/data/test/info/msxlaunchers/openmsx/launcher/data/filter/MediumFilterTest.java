package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import info.msxlaunchers.openmsx.launcher.data.filter.MediumFilter;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class MediumFilterTest
{
	@Test
	public void testConstructor()
	{
		new MediumFilter( Medium.ROM );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new MediumFilter( null );
	}

	@Test( expected = NullPointerException.class )
	public void testIsFilteredNullGame()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.ROM );

		mediumFilter.isFiltered( null, null );
	}

	@Test
	public void testIsFilteredForROM()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.ROM );

		Game game1 = Game.romA( "rom" ).build();
		assertFalse( mediumFilter.isFiltered( game1, null ) );

		Game game2 = Game.diskA( "disk" ).build();
		assertTrue( mediumFilter.isFiltered( game2, null ) );

		Game game3 = Game.tape( "tape" ).build();
		assertTrue( mediumFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testIsFilteredForDisk()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.DISK );

		Game game1 = Game.romA( "rom" ).build();
		assertTrue( mediumFilter.isFiltered( game1, null ) );

		Game game2 = Game.diskA( "disk" ).build();
		assertFalse( mediumFilter.isFiltered( game2, null ) );

		Game game3 = Game.tape( "tape" ).build();
		assertTrue( mediumFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testIsFilteredForTape()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.TAPE );

		Game game1 = Game.romA( "rom" ).build();
		assertTrue( mediumFilter.isFiltered( game1, null ) );

		Game game2 = Game.diskA( "disk" ).build();
		assertTrue( mediumFilter.isFiltered( game2, null ) );

		Game game3 = Game.tape( "tape" ).build();
		assertFalse( mediumFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testIsFilteredForHarddisk()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.HARDDISK );

		Game game1 = Game.romA( "rom" ).build();
		assertTrue( mediumFilter.isFiltered( game1, null ) );

		Game game2 = Game.diskA( "disk" ).build();
		assertTrue( mediumFilter.isFiltered( game2, null ) );

		Game game3 = Game.harddisk( "harddisk" ).build();
		assertFalse( mediumFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testIsFilteredForLaserdisc()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.LASERDISC );

		Game game1 = Game.romA( "rom" ).build();
		assertTrue( mediumFilter.isFiltered( game1, null ) );

		Game game2 = Game.tclScript( "script" ).build();
		assertTrue( mediumFilter.isFiltered( game2, null ) );

		Game game3 = Game.laserdisc( "laserdisc" ).build();
		assertFalse( mediumFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testIsFilteredForScript()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.SCRIPT );

		Game game1 = Game.romA( "rom" ).build();
		assertTrue( mediumFilter.isFiltered( game1, null ) );

		Game game2 = Game.tclScript( "script" ).build();
		assertFalse( mediumFilter.isFiltered( game2, null ) );

		Game game3 = Game.harddisk( "harddisk" ).build();
		assertTrue( mediumFilter.isFiltered( game3, null ) );
	}

	@Test
	public void testClassAnnotation()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.ROM );

		FilterDescriptor filterType = mediumFilter.getClass().getAnnotation( FilterDescriptor.class );

		assertEquals( FilterType.MEDIUM, filterType.type() );
	}

	@Test
	public void testValue1FieldAnnotationUsedOnce()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.ROM );

		Field[] fields = mediumFilter.getClass().getDeclaredFields();

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
		assertEquals( "medium", fieldName );
	}

	@Test
	public void testValue2FieldAnnotationNotUsed()
	{
		MediumFilter mediumFilter = new MediumFilter( Medium.ROM );

		Field[] fields = mediumFilter.getClass().getDeclaredFields();

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
		MediumFilter mediumFilter = new MediumFilter( Medium.ROM );

		Field[] fields = mediumFilter.getClass().getDeclaredFields();

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
		MediumFilter mediumFilter1 = new MediumFilter( Medium.LASERDISC );
		MediumFilter mediumFilter2 = new MediumFilter( Medium.LASERDISC );
		MediumFilter mediumFilter3 = new MediumFilter( Medium.HARDDISK );

		assertTrue( mediumFilter1.equals( mediumFilter2 ) );
		assertTrue( mediumFilter2.equals( mediumFilter1 ) );
		assertTrue( mediumFilter3.equals( mediumFilter3 ) );
		assertFalse( mediumFilter1.equals( mediumFilter3 ) );
		assertFalse( mediumFilter3.equals( mediumFilter2 ) );
		assertFalse( mediumFilter3.equals( null ) );
		assertFalse( mediumFilter3.equals( this ) );

		assertEquals( mediumFilter1.hashCode(), mediumFilter2.hashCode() );
		assertNotEquals( mediumFilter1.hashCode(), mediumFilter3.hashCode() );
		assertNotEquals( mediumFilter2.hashCode(), mediumFilter3.hashCode() );
	}
}
