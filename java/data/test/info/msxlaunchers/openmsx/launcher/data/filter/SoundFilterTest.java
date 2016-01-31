package info.msxlaunchers.openmsx.launcher.data.filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import info.msxlaunchers.openmsx.launcher.data.filter.SoundFilter;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Sound;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SoundFilterTest
{
	@Test
	public void testConstructor()
	{
		new SoundFilter( Sound.SCC );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new SoundFilter( null );
	}

	@Test( expected = NullPointerException.class )
	public void testIsFilteredNullGame()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.PSG );

		assertFalse( soundFilter.isFiltered( null, null ) );
	}

	@Test
	public void testIsFilteredForPSG()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.PSG );

		Game game1 = Game.name( "name" ).isPSG( true ).build();
		assertFalse( soundFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isSCC( true ).build();
		assertTrue( soundFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testIsFilteredForSCC()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.SCC );

		Game game1 = Game.name( "name" ).isSCC( true ).build();
		assertFalse( soundFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isSCCI( true ).build();
		assertTrue( soundFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testIsFilteredForSCCI()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.SCC_I );

		Game game1 = Game.name( "name" ).isSCCI( true ).build();
		assertFalse( soundFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isPCM( true ).build();
		assertTrue( soundFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testIsFilteredForPCM()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.PCM );

		Game game1 = Game.name( "name" ).isPCM( true ).build();
		assertFalse( soundFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isMSXMUSIC( true ).build();
		assertTrue( soundFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testIsFilteredForMSXMusic()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.MSX_MUSIC );

		Game game1 = Game.name( "name" ).isMSXMUSIC( true ).build();
		assertFalse( soundFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isMSXAUDIO( true ).build();
		assertTrue( soundFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testIsFilteredForMSXAudio()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.MSX_AUDIO );

		Game game1 = Game.name( "name" ).isMSXAUDIO( true ).build();
		assertFalse( soundFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isMoonsound( true ).build();
		assertTrue( soundFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testIsFilteredForM()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.MOONSOUND );

		Game game1 = Game.name( "name" ).isMoonsound( true ).build();
		assertFalse( soundFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isMIDI( true ).build();
		assertTrue( soundFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testIsFilteredForMIDI()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.MIDI );

		Game game1 = Game.name( "name" ).isMIDI( true ).build();
		assertFalse( soundFilter.isFiltered( game1, null ) );

		Game game2 = Game.name( "name" ).isPSG( true ).build();
		assertTrue( soundFilter.isFiltered( game2, null ) );
	}

	@Test
	public void testClassAnnotation()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.PSG );

		FilterDescriptor filterType = soundFilter.getClass().getAnnotation( FilterDescriptor.class );

		assertEquals( FilterType.SOUND, filterType.type() );
	}

	@Test
	public void testValue1FieldAnnotationUsedOnce()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.PSG );

		Field[] fields = soundFilter.getClass().getDeclaredFields();

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
		assertEquals( "sound", fieldName );
	}

	@Test
	public void testValue2FieldAnnotationNotUsed()
	{
		SoundFilter soundFilter = new SoundFilter( Sound.PSG );

		Field[] fields = soundFilter.getClass().getDeclaredFields();

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
		SoundFilter soundFilter = new SoundFilter( Sound.PSG );

		Field[] fields = soundFilter.getClass().getDeclaredFields();

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
		SoundFilter soundFilter1 = new SoundFilter( Sound.MOONSOUND );
		SoundFilter soundFilter2 = new SoundFilter( Sound.MOONSOUND );
		SoundFilter soundFilter3 = new SoundFilter( Sound.MIDI );

		assertTrue( soundFilter1.equals( soundFilter2 ) );
		assertTrue( soundFilter2.equals( soundFilter1 ) );
		assertTrue( soundFilter3.equals( soundFilter3 ) );
		assertFalse( soundFilter1.equals( soundFilter3 ) );
		assertFalse( soundFilter3.equals( soundFilter2 ) );
		assertFalse( soundFilter3.equals( null ) );
		assertFalse( soundFilter3.equals( this ) );

		assertEquals( soundFilter1.hashCode(), soundFilter2.hashCode() );
		assertNotEquals( soundFilter1.hashCode(), soundFilter3.hashCode() );
		assertNotEquals( soundFilter2.hashCode(), soundFilter3.hashCode() );
	}
}
