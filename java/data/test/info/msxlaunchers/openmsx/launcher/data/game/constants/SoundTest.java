package info.msxlaunchers.openmsx.launcher.data.game.constants;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SoundTest
{
	@Test
	public void testSoundValues()
	{
		//the following is to get complete code coverage even if there's no value in this test
		Sound.valueOf( Sound.PSG.toString() );
		Sound.valueOf( Sound.SCC.toString() );
		Sound.valueOf( Sound.SCC_I.toString() );
		Sound.valueOf( Sound.PCM.toString() );
		Sound.valueOf( Sound.MSX_MUSIC.toString() );
		Sound.valueOf( Sound.MSX_AUDIO.toString() );
		Sound.valueOf( Sound.MOONSOUND.toString() );
		Sound.valueOf( Sound.MIDI.toString() );
	}

	@Test
	public void testTotalSoundChipsCount()
	{
		assertEquals( Sound.values().length, 8 );
	}

	@Test
	public void testSoundChipsDisplayName()
	{
		assertEquals( Sound.PSG.getDisplayName(), "PSG" );
		assertEquals( Sound.SCC.getDisplayName(), "SCC" );
		assertEquals( Sound.SCC_I.getDisplayName(), "SCC-I" );
		assertEquals( Sound.PCM.getDisplayName(), "PCM" );
		assertEquals( Sound.MSX_MUSIC.getDisplayName(), "MSX-MUSIC" );
		assertEquals( Sound.MSX_AUDIO.getDisplayName(), "MSX-AUDIO" );
		assertEquals( Sound.MOONSOUND.getDisplayName(), "Moonsound" );
		assertEquals( Sound.MIDI.getDisplayName(), "MIDI" );
	}
}
