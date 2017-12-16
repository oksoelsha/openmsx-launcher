package info.msxlaunchers.openmsx.launcher.data.game;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GameTest
{
	@Test
	public void testCompleteGameConstruction()
	{
		Game game = Game.name( "name" ).info( "info" ).machine( "machine").romA( "romA" ).romB( "romB" )
			.extensionRom( "extensionRom" ).diskA( "diskA" ).diskB( "diskB" ).tape( "tape" )
			.harddisk( "harddisk" ).laserdisc( "laserdisc" )
			.isMSX( true ).isMSX2( true ).isMSX2Plus( true ).isTurboR( true )
			.isPSG( true ).isSCC( true ).isSCCI( true ).isPCM( true )
			.isMSXMUSIC( true ).isMSXAUDIO( true ).isMoonsound( true ).isMIDI( true )
			.genre1( Genre.ACTION ).genre2( Genre.ADVENTURE_TEXT_AND_GFX )
			.msxGenID( 80 ).sha1Code( "123456789abcdef" ).size( 16 ).screenshotSuffix( "suffix" )
			.tclScript( "tclScript" ).fddMode( FDDMode.DISABLE_BOTH ).tclScriptOverride( true ).build();

		assertEquals( "name", game.getName() );
		assertEquals( "info", game.getInfo() );
		assertEquals( "machine", game.getMachine() );
		assertEquals( "romA", game.getRomA() );
		assertEquals( "romB", game.getRomB() );
		assertEquals( "extensionRom", game.getExtensionRom() );
		assertEquals( "diskA", game.getDiskA() );
		assertEquals( "diskB", game.getDiskB() );
		assertEquals( "tape", game.getTape() );
		assertEquals( "harddisk", game.getHarddisk() );
		assertEquals( "laserdisc", game.getLaserdisc() );
		assertTrue( game.isMSX() );
		assertTrue( game.isMSX2() );
		assertTrue( game.isMSX2Plus() );
		assertTrue( game.isTurboR() );
		assertTrue( game.isPSG() );
		assertTrue( game.isSCC() );
		assertTrue( game.isSCCI() );
		assertTrue( game.isPCM() );
		assertTrue( game.isMSXMUSIC() );
		assertTrue( game.isMSXAUDIO() );
		assertTrue( game.isMoonsound() );
		assertTrue( game.isMIDI() );
		assertEquals( Genre.ACTION, game.getGenre1() );
		assertEquals( Genre.ADVENTURE_TEXT_AND_GFX, game.getGenre2() );
		assertEquals( 80, game.getMsxGenID() );
		assertEquals( "123456789abcdef", game.getSha1Code() );
		assertEquals( 16, game.getSize() );
		assertEquals( "suffix", game.getScreenshotSuffix() );
		assertEquals( "tclScript", game.getTclScript() );
		assertEquals( FDDMode.DISABLE_BOTH, game.getFDDMode() );
		assertTrue( game.isTclScriptOverride() );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testMustHaveMinumumFields()
	{
		Game.info( "info" ).machine( "machine")
				.isMSX( true ).isMSX2( true ).isMSX2Plus( true ).isTurboR( true )
				.isPSG( true ).isSCC( true ).isSCCI( true ).isPCM( true )
				.isMSXMUSIC( true ).isMSXAUDIO( true ).isMoonsound( true ).isMIDI( true )
				.genre1( Genre.ACTION ).genre2( Genre.ADVENTURE_TEXT_AND_GFX )
				.msxGenID( 80 ).sha1Code( "123456789abcdef" ).size( 16 ).screenshotSuffix( "suffix" )
				.build();
	}

	@Test
	public void testEqualityAndHashcode()
	{
		Game game1 = Game.name( "sameName" ).romA( "romA1" ).build();
		Game game2 = Game.name( "sameName" ).romA( "romA2" ).build();
		Game game3 = Game.name( "differentName" ).romA( "romA" ).build();
		Game game4 = Game.romA( "romA" ).build();

		assertEquals( game1, game2 );
		assertEquals( game1.hashCode(), game2.hashCode() );
		assertEquals( game1, game1 );
		assertEquals( game1.hashCode(), game1.hashCode() );
		assertEquals( game2, game2 );
		assertEquals( game2.hashCode(), game2.hashCode() );
		assertNotEquals( game1, game3 );
		assertNotEquals( game1.hashCode(), game3.hashCode() );
		assertNotEquals( game2, game3 );
		assertNotEquals( game2.hashCode(), game3.hashCode() );
		assertNotEquals( game4, game3 );
		assertNotEquals( game4.hashCode(), game3.hashCode() );
		assertNotEquals( game1, null );

		assertFalse( game1.equals( null ) );
		assertFalse( game1.equals( this ) );

		//in case of name==null, objects will not be equal but their hash code is the default.
		//TODO: review
		assertNotEquals( game4, game4 );
		assertEquals( game4.hashCode(), game4.hashCode() );
	}

	@Test
	public void testMediaType()
	{
		Game game = Game.name( "name" ).build();

		assertFalse( game.isROM() );
		assertFalse( game.isDisk() );
		assertFalse( game.isTape() );
		assertFalse( game.isHarddisk() );
		assertFalse( game.isLaserdisc() );
		assertFalse( game.isScript() );

		Game gameROM = Game.romA( "romFile" ).build();

		assertTrue( gameROM.isROM() );
		assertFalse( gameROM.isDisk() );
		assertFalse( gameROM.isTape() );
		assertFalse( gameROM.isHarddisk() );
		assertFalse( gameROM.isLaserdisc() );
		assertFalse( gameROM.isScript() );

		Game gameDisk = Game.diskA( "diskFile" ).build();

		assertFalse( gameDisk.isROM() );
		assertTrue( gameDisk.isDisk() );
		assertFalse( gameDisk.isTape() );
		assertFalse( gameDisk.isHarddisk() );
		assertFalse( gameDisk.isLaserdisc() );
		assertFalse( gameDisk.isScript() );

		Game gameTape = Game.tape( "tapeFile" ).build();

		assertFalse( gameTape.isROM() );
		assertFalse( gameTape.isDisk() );
		assertTrue( gameTape.isTape() );
		assertFalse( gameTape.isHarddisk() );
		assertFalse( gameTape.isLaserdisc() );
		assertFalse( gameTape.isScript() );

		Game gameHarddisk = Game.harddisk( "harddiskFile" ).build();

		assertFalse( gameHarddisk.isROM() );
		assertFalse( gameHarddisk.isDisk() );
		assertFalse( gameHarddisk.isTape() );
		assertTrue( gameHarddisk.isHarddisk() );
		assertFalse( gameHarddisk.isLaserdisc() );
		assertFalse( gameHarddisk.isScript() );

		Game gameLaserdisk = Game.laserdisc( "laserdiscFile" ).build();

		assertFalse( gameLaserdisk.isROM() );
		assertFalse( gameLaserdisk.isDisk() );
		assertFalse( gameLaserdisk.isTape() );
		assertFalse( gameLaserdisk.isHarddisk() );
		assertTrue( gameLaserdisk.isLaserdisc() );
		assertFalse( gameLaserdisk.isScript() );

		Game gameScript = Game.tclScript( "scriptFile" ).build();

		assertFalse( gameScript.isROM() );
		assertFalse( gameScript.isDisk() );
		assertFalse( gameScript.isTape() );
		assertFalse( gameScript.isHarddisk() );
		assertFalse( gameScript.isLaserdisc() );
		assertTrue( gameScript.isScript() );
	}

	@Test
	public void testResetOfSomeFieldsIfEmpty()
	{
		//need to test that all String fields get set to null if they are empty Strings
		
		//for test 1, need to set at least the name field to be non-empty because we cannot have all fields
		//be empty
		Game game = Game.machine( "" ).name( "name" ).info( "" ).romA( "" ).romB( "" ).extensionRom( "" )
				.diskA( "" ).diskB( "" ).tape( "" ).harddisk( "" ).laserdisc( "" )
				.sha1Code( "" ).tclScript( "" ).build();

		assertNull( game.getMachine() );
		assertNull( game.getInfo() );
		assertNull( game.getRomA() );
		assertNull( game.getRomB() );
		assertNull( game.getExtensionRom() );
		assertNull( game.getDiskA() );
		assertNull( game.getDiskB() );
		assertNull( game.getTape() );
		assertNull( game.getHarddisk() );
		assertNull( game.getLaserdisc() );
		assertNull( game.getSha1Code() );
		assertNull( game.getTclScript() );

		//now make sure name fields also gets set to null if empty String
		game = Game.name( "" ).romA( "romA" ).build();

		assertNull( game.getName() );
	}

	@Test
	public void testIsExtraDataEqual()
	{
		Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).build();

		assertFalse( game.isExtraDataEqual( null ) );

		Game newGame1 = Game.name( "name1" ).info( "info1" ).machine( "machine" ).romA( "romA1" ).romA( "romB1" )
				.diskA( "diskA1" ).diskB( "diskB1" ).tape( "tape1" ).harddisk( "harddisk1" ).laserdisc( "laserdisc1" ).tclScript( "tclScript1" ).build();
		assertTrue( game.isExtraDataEqual( newGame1 ) );

		Game newGame2 = Game.name( "name2" ).machine( "machine" ).romA( "romA2" ).isMSX( true ).build();
		assertFalse( game.isExtraDataEqual( newGame2 ) );

		Game newGame3 = Game.name( "name3" ).machine( "machine" ).romA( "romA3" ).isMSX2( true ).build();
		assertFalse( game.isExtraDataEqual( newGame3 ) );

		Game newGame4 = Game.name( "name4" ).machine( "machine" ).romA( "romA4" ).isMSX2Plus( true ).build();
		assertFalse( game.isExtraDataEqual( newGame4 ) );

		Game newGame5 = Game.name( "name5" ).machine( "machine" ).romA( "romA5" ).isTurboR( true ).build();
		assertFalse( game.isExtraDataEqual( newGame5 ) );

		Game newGame6 = Game.name( "name6" ).machine( "machine" ).romA( "romA6" ).isPSG( true ).build();
		assertFalse( game.isExtraDataEqual( newGame6 ) );

		Game newGame7 = Game.name( "name7" ).machine( "machine" ).romA( "romA7" ).isSCC( true ).build();
		assertFalse( game.isExtraDataEqual( newGame7 ) );

		Game newGame8 = Game.name( "name8" ).machine( "machine" ).romA( "romA8" ).isSCCI( true ).build();
		assertFalse( game.isExtraDataEqual( newGame8 ) );

		Game newGame9 = Game.name( "name9" ).machine( "machine" ).romA( "romA9" ).isPCM( true ).build();
		assertFalse( game.isExtraDataEqual( newGame9 ) );

		Game newGame10 = Game.name( "name10" ).machine( "machine" ).romA( "romA10" ).isMSXMUSIC( true ).build();
		assertFalse( game.isExtraDataEqual( newGame10 ) );

		Game newGame11 = Game.name( "name11" ).machine( "machine" ).romA( "romA11" ).isMSXAUDIO( true ).build();
		assertFalse( game.isExtraDataEqual( newGame11 ) );

		Game newGame12 = Game.name( "name12" ).machine( "machine" ).romA( "romA12" ).isMoonsound( true ).build();
		assertFalse( game.isExtraDataEqual( newGame12 ) );

		Game newGame13 = Game.name( "name13" ).machine( "machine" ).romA( "romA13" ).isMIDI( true ).build();
		assertFalse( game.isExtraDataEqual( newGame13 ) );

		Game newGame14 = Game.name( "name14" ).machine( "machine" ).romA( "romA14" ).genre1( Genre.ACTION ).build();
		assertFalse( game.isExtraDataEqual( newGame14 ) );

		Game newGame15 = Game.name( "name15" ).machine( "machine" ).romA( "romA15" ).genre2( Genre.ACTION ).build();
		assertFalse( game.isExtraDataEqual( newGame15 ) );

		Game newGame16 = Game.name( "name16" ).machine( "machine" ).romA( "romA16" ).screenshotSuffix( "en" ).build();
		assertFalse( game.isExtraDataEqual( newGame16 ) );

		Game newGame17 = Game.name( "name17" ).machine( "machine" ).romA( "romA17" ).msxGenID( 1 ).build();
		assertFalse( game.isExtraDataEqual( newGame17 ) );
	}
}