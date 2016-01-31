package info.msxlaunchers.openmsx.launcher.data.game.constants;

import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GenreTest
{
	@Test
	public void testGenreValues()
	{
		Genre[] genres = Genre.values();

		for( Genre genre: genres )
		{
			assertEquals( Genre.fromValue( genre.getValue() ), genre );
		}
	}

	@Test
	public void testFromValueBoundaries()
	{
		assertEquals( Genre.fromValue( -1 ), Genre.UNKNOWN );
		assertEquals( Genre.fromValue( 0 ), Genre.UNKNOWN );
		assertEquals( Genre.fromValue( 1 ), Genre.ACTION );
		assertEquals( Genre.fromValue( 50 ), Genre.DEXTERITY );
		assertEquals( Genre.fromValue( 51 ), Genre.UNKNOWN );
	}

	@Test
	public void testDisplayName()
	{
		assertEquals( "", Genre.UNKNOWN.getDisplayName() );
		assertEquals( "Action", Genre.ACTION.getDisplayName() );
		assertEquals( "Adult", Genre.ADULT.getDisplayName() );
		assertEquals( "Adventure (All)", Genre.ADVENTURE_ALL.getDisplayName() );
		assertEquals( "Adventure | Point and Click", Genre.ADVENTURE_POINT_AND_CLICK.getDisplayName() );
		assertEquals( "Adventure | Text and Gfx", Genre.ADVENTURE_TEXT_AND_GFX.getDisplayName() );
		assertEquals( "Adventure | Text only", Genre.ADVENTURE_TEXT_ONLY.getDisplayName() );
		assertEquals( "Arcade", Genre.ARCADE.getDisplayName() );
		assertEquals( "Board Games", Genre.BOARD_GAMES.getDisplayName() );
		assertEquals( "Break-out", Genre.BREAK_OUT.getDisplayName() );
		assertEquals( "Card Games", Genre.CARD_GAMES.getDisplayName() );
		assertEquals( "Communication", Genre.COMMUNICATION.getDisplayName() );
		assertEquals( "Compiler", Genre.COMPILER.getDisplayName() );
		assertEquals( "Database", Genre.DATABASE.getDisplayName() );
		assertEquals( "DTP", Genre.DTP.getDisplayName() );
		assertEquals( "Educational", Genre.EDUCATIONAL.getDisplayName() );
		assertEquals( "Fighting", Genre.FIGHTING.getDisplayName() );
		assertEquals( "Financial", Genre.FINANCIAL.getDisplayName() );
		assertEquals( "Gambling / Fruit Machine", Genre.GAMBLING_FRUIT_MACHINE.getDisplayName() );
		assertEquals( "Graphics", Genre.GRAPHICS.getDisplayName() );
		assertEquals( "Miscellaneous", Genre.MISCELLANEOUS.getDisplayName() );
		assertEquals( "Office", Genre.OFFICE.getDisplayName() );
		assertEquals( "Operating System", Genre.OPERATING_SYSTEM.getDisplayName() );
		assertEquals( "Parody", Genre.PARODY.getDisplayName() );
		assertEquals( "Pinball", Genre.PINBALL.getDisplayName() );
		assertEquals( "Platform", Genre.PLATFORM.getDisplayName() );
		assertEquals( "Puzzle", Genre.PUZZLE.getDisplayName() );
		assertEquals( "Quiz", Genre.QUIZ.getDisplayName() );
		assertEquals( "Racing", Genre.RACING.getDisplayName() );
		assertEquals( "Remake", Genre.REMAKE.getDisplayName() );
		assertEquals( "RPG", Genre.RPG.getDisplayName() );
		assertEquals( "Shoot-'em-up (All)", Genre.SHOOT_EM_UP_ALL.getDisplayName() );
		assertEquals( "Shoot-'em-up | First-person shooter", Genre.SHOOT_EM_UP_FIRST_PERSON_SHOOTER.getDisplayName() );
		assertEquals( "Shoot-'em-up | Horizontal", Genre.SHOOT_EM_UP_HORIZONTAL.getDisplayName() );
		assertEquals( "Shoot-'em-up | Isometric", Genre.SHOOT_EM_UP_ISOMETRIC.getDisplayName() );
		assertEquals( "Shoot-'em-up | Multi-directional", Genre.SHOOT_EM_UP_MULTI_DIRECTIONAL.getDisplayName() );
		assertEquals( "Shoot-'em-up | Vertical", Genre.SHOOT_EM_UP_MULTI_VERTICAL.getDisplayName() );
		assertEquals( "Simulation", Genre.SIMULATION.getDisplayName() );
		assertEquals( "Sound", Genre.SOUND.getDisplayName() );
		assertEquals( "Sport Games", Genre.SPORT_GAMES.getDisplayName() );
		assertEquals( "Sport Management", Genre.SPORT_MANAGEMENT.getDisplayName() );
		assertEquals( "Spreadsheet", Genre.SPREADSHEET.getDisplayName() );
		assertEquals( "Strategy", Genre.STRATEGY.getDisplayName() );
		assertEquals( "Tool", Genre.TOOL.getDisplayName() );
		assertEquals( "Variety", Genre.VARIETY.getDisplayName() );
		assertEquals( "War Games", Genre.WAR_GAMES.getDisplayName() );
		assertEquals( "Word Processor", Genre.WORD_PROCESSOR.getDisplayName() );
		assertEquals( "Maze", Genre.MAZE.getDisplayName() );
		assertEquals( "Pong", Genre.PONG.getDisplayName() );
		assertEquals( "Beat-'em-up", Genre.BEAT_EM_UP.getDisplayName() );
		assertEquals( "Dexterity", Genre.DEXTERITY.getDisplayName() );
	}
}
