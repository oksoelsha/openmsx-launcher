package info.msxlaunchers.openmsx.launcher.persistence.game;

import info.msxlaunchers.openmsx.launcher.builder.GameBuilder;
import info.msxlaunchers.openmsx.launcher.data.backup.DatabaseBackup;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
import info.msxlaunchers.openmsx.launcher.persistence.DatabaseTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class EmbeddedDatabaseGamePersisterTest extends DatabaseTest
{
	@Mock GameBuilder gameBuilder;
	@Mock ActionDecider actionDecider;

	private static final String database1 = "database1";
	private static final String database2 = "database2";

	@Test( expected = GamePersistenceException.class )
	public void test_whenCreateDatabaseIsCalledWithNull_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		try
		{
			persister.createDatabase( null );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.DATABASE_NULL_NAME, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_whenCreateDatabaseIsCalledWithEmpty_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		try
		{
			persister.createDatabase( "" );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.DATABASE_NULL_NAME, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test
	public void test_whenCreateDatabase_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );
	}

	@Test( expected = GamePersistenceException.class )
	public void test_whenCreateDatabaseIsCalledWithExistingDatabase_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		try
		{
			persister.createDatabase( database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_whenCreateDatabaseIsCalledWithInvalidDatabase_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		//an invalid database is one that has more than 64 characters in its name
		try
		{
			String validName = "012345678901234567890123456789012345678901234567890123456789abcd";
			persister.createDatabase( validName );
	
			Set<String> databases = persister.getDatabases();
	
			assertEquals( 1, databases.size() );
		}
		catch( GamePersistenceException ioe )
		{
			//this is to guarantee that the first database is valid with exactly 64 characters
			throw new RuntimeException();
		}

		String invalidName = "012345678901234567890123456789012345678901234567890123456789abcde";

		try
		{
			persister.createDatabase( invalidName );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.IO, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test
	public void test_whenGetDatabases_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Set<String> databases = persister.getDatabases();

		assertEquals( databases.size(), 0 );

		persister.createDatabase( database1 );
		persister.createDatabase( "database2" );
		persister.createDatabase( "database3" );

		databases = persister.getDatabases();

		assertEquals( databases.size(), 3 );
		assertTrue( databases.contains( database1 ) );
		assertTrue( databases.contains( "database2" ) );
		assertTrue( databases.contains( "database3" ) );
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenGameIsDuplicate_whenSaveGame_thenThrowException() throws GamePersistenceException
	{
		Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).build();

		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );
		persister.saveGame( game, database1 );
		try
		{
			persister.saveGame( game, database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.GAME_ALREADY_EXISTS, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenGameWithoutName_whenSaveGame_thenThrowException() throws GamePersistenceException
	{
		Game game = Game.machine( "machine" ).romA( "romA" ).build();

		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );
		try
		{
			persister.saveGame( game, database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenGameWithoutMedia_whenSaveGame_thenThrowException() throws GamePersistenceException
	{
		Game game = Game.name( "name" ).machine( "machine" ).build();

		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );
		try
		{
			persister.saveGame( game, database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.GAME_WITH_MISSING_MEDIA, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test
	public void test_whenSaveGame_thenSuccess() throws GamePersistenceException
	{
		Game game1 = Game.name( "name" ).info( "info" ).machine( "machine" )
				.romA( "romA" ).extensionRom( "extensionRom" ).romB( "romB" )
				.diskA( "diskA" ).diskB( "diskB" )
				.tape( "tape" ).harddisk( "harddisk" ).laserdisc( "laserdisc" ).tclScript( "tclScript" )
				.isMSX( true ).isMSX2( true ).isMSX2Plus( true ).isTurboR( true )
				.isPSG( true ).isSCC( true ).isSCCI( true ).isPCM( true )
				.isMSXMUSIC( true ).isMSXAUDIO( true ).isMoonsound( true ).isMIDI( true )
				.genre1( Genre.ADVENTURE_ALL ).genre2( Genre.ARCADE )
				.msxGenID( 25 ).sha1Code( "sha1Code" ).screenshotSuffix( "ss" ).size( 100 )
				.fddMode( FDDMode.DISABLE_SECOND )
				.build();

		Game game2 = Game.name( "name2" ).machine( "machine2" ).diskA( "diskA2" ).build();

		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );
		persister.saveGame( game1, database1 );
		persister.saveGame( game2, database1 );

		Set<Game> games = persister.getGames( database1 );

		assertEquals( 2, games.size() );

		for( Game game:games )
		{
			if( game.getName().equals( "name" ) )
			{
				assertEquals( "info", game.getInfo() );
				assertEquals( "machine", game.getMachine() );
				assertEquals( "romA", game.getRomA() );
				assertEquals( "extensionRom", game.getExtensionRom() );
				assertEquals( "romB", game.getRomB() );
				assertEquals( "diskA", game.getDiskA() );
				assertEquals( "diskB", game.getDiskB() );
				assertEquals( "tape", game.getTape() );
				assertEquals( "harddisk", game.getHarddisk() );
				assertEquals( "laserdisc", game.getLaserdisc() );
				assertEquals( "tclScript", game.getTclScript() );
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
				assertEquals( Genre.ADVENTURE_ALL, game.getGenre1() );
				assertEquals( Genre.ARCADE, game.getGenre2() );
				assertEquals( 25, game.getMsxGenID() );
				assertEquals( "sha1Code", game.getSha1Code() );
				assertEquals( "ss", game.getScreenshotSuffix() );
				assertEquals( 100, game.getSize() );
				assertEquals( FDDMode.DISABLE_SECOND, game.getFDDMode() );
			}
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenInvalidGameField_whenSaveGame_thenThrowException() throws GamePersistenceException
	{
		//screen suffix must be at most 10 
		Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).screenshotSuffix( "qwertyuiopa" ).build();

		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		try
		{
			persister.saveGame( game, database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.IO, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test
	public void test_whenDeleteDatabase_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Set<String> databases = persister.getDatabases();

		assertEquals( 1, databases.size() );

		persister.deleteDatabase( database1 );

		databases = persister.getDatabases();

		assertEquals( 0, databases.size() );
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenNonExistingDatabase_whenDeleteDatabase_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		try
		{
			persister.deleteDatabase( database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test
	public void test_whenRenameDatabase_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );
		persister.renameDatabase( database1, "database2" );

		Set<String> databases = persister.getDatabases();

		assertEquals( 1, databases.size() );
		assertTrue( databases.contains( "database2" ) );
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenNonExistingDatabase_whenRenameDatabase_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		try
		{
			persister.renameDatabase( "oldDatabase", "newDatabase" );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenAlreadyExistingDatabase_whenRenameDatabase_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );
		persister.createDatabase( database2 );

		try
		{
			persister.renameDatabase( database1, database2 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test
	public void test_whenRecreateDatabase_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game = Game.name( "name" ).machine( "machine" ).romA( "romA" ).screenshotSuffix( "qwe" ).build();

		persister.saveGame( game, database1 );

		persister.recreateDatabase( database1 );

		Set<Game> games = persister.getGames( database1 );

		assertEquals( 0, games.size() );
	}

	@Test
	public void test_whenGetGames_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine" ).romA( "romA" ).build();
		Game game3 = Game.name( "name3" ).machine( "machine" ).romA( "romA" ).build();

		Set<Game> games = new HashSet<>();
		games.add( game1 );
		games.add( game2 );
		games.add( game3 );

		persister.saveGames( games, database1 );

		Set<Game> gamesFromDB = persister.getGames( database1 );

		assertEquals( 3, gamesFromDB.size() );
		assertTrue( gamesFromDB.contains( game1 ) );
		assertTrue( gamesFromDB.contains( game2 ) );
		assertTrue( gamesFromDB.contains( game3 ) );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void test_whenModifyReturnValueOfGetGames_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine" ).romA( "romA" ).build();
		Game game3 = Game.name( "name3" ).machine( "machine" ).romA( "romA" ).build();

		Set<Game> games = new HashSet<>();
		games.add( game1 );
		games.add( game2 );
		games.add( game3 );

		persister.saveGames( games, database1 );

		Set<Game> gamesFromDB = persister.getGames( database1 );

		gamesFromDB.add( Game.name( "name4" ).machine( "machine" ).romA( "romA" ).build() );
	}

	@Test
	public void test_whenDeleteGames_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine" ).romA( "romA" ).build();
		Game game3 = Game.name( "name3" ).machine( "machine" ).romA( "romA" ).build();

		Set<Game> games = new HashSet<>();
		games.add( game1 );
		games.add( game2 );
		games.add( game3 );

		persister.saveGames( games, database1 );

		Set<Game> gamesFromDB = persister.getGames( database1 );

		assertEquals( 3, gamesFromDB.size() );

		persister.deleteGames( games, database1 );

		gamesFromDB = persister.getGames( database1 );

		assertEquals( 0, gamesFromDB.size() );
	}

	@Test
	public void test_givenNewGameWithDifferentName_whenUpdateGame_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name" ).info( "info" ).machine( "machine" )
				.romA( "romA" ).extensionRom( "extensionRom" ).romB( "romB" )
				.diskA( "diskA" ).diskB( "diskB" )
				.tape( "tape" ).harddisk( "harddisk" ).laserdisc( "laserdisc" ).tclScript( "tclScript" )
				.isMSX( true ).isMSX2( true ).isMSX2Plus( true ).isTurboR( true )
				.isPSG( true ).isSCC( true ).isSCCI( true ).isPCM( true )
				.isMSXMUSIC( true ).isMSXAUDIO( true ).isMoonsound( true ).isMIDI( true )
				.genre1( Genre.ADVENTURE_ALL ).genre2( Genre.ARCADE )
				.msxGenID( 25 ).sha1Code( "sha1Code" ).screenshotSuffix( "ss" ).size( 100 )
				.build();

		persister.saveGame( game1, database1 );

		Game game2 = Game.name( "name2" ).info( "info2" ).machine( "machine2" )
				.romA( "romA2" ).extensionRom( "extensionRom2" ).romB( "romB2" )
				.diskA( "diskA2" ).diskB( "diskB2" )
				.tape( "tape2" ).harddisk( "harddisk2" ).laserdisc( "laserdisc2" ).tclScript( "tclScript2" )
				.isMSX( true ).isMSX2( false ).isMSX2Plus( true ).isTurboR( false )
				.isPSG( true ).isSCC( false ).isSCCI( true ).isPCM( false )
				.isMSXMUSIC( true ).isMSXAUDIO( false ).isMoonsound( true ).isMIDI( false )
				.genre1( Genre.BEAT_EM_UP ).genre2( Genre.BOARD_GAMES )
				.msxGenID( 26 ).sha1Code( "sha1Code2" ).screenshotSuffix( "rr" ).size( 110 )
				.fddMode( FDDMode.DISABLE_BOTH )
				.build();

		persister.updateGame( game1, game2, database1 );

		Set<Game> games = persister.getGames( database1 );

		assertEquals( 1, games.size() );

		for( Game game: games )
		{
			if( game.getName().equals( "name2" ) )
			{
				assertEquals( "info2", game.getInfo() );
				assertEquals( "machine2", game.getMachine() );
				assertEquals( "romA2", game.getRomA() );
				assertEquals( "extensionRom2", game.getExtensionRom() );
				assertEquals( "romB2", game.getRomB() );
				assertEquals( "diskA2", game.getDiskA() );
				assertEquals( "diskB2", game.getDiskB() );
				assertEquals( "tape2", game.getTape() );
				assertEquals( "harddisk2", game.getHarddisk() );
				assertEquals( "laserdisc2", game.getLaserdisc() );
				assertEquals( "tclScript2", game.getTclScript() );
				assertTrue( game.isMSX() );
				assertFalse( game.isMSX2() );
				assertTrue( game.isMSX2Plus() );
				assertFalse( game.isTurboR() );
				assertTrue( game.isPSG() );
				assertFalse( game.isSCC() );
				assertTrue( game.isSCCI() );
				assertFalse( game.isPCM() );
				assertTrue( game.isMSXMUSIC() );
				assertFalse( game.isMSXAUDIO() );
				assertTrue( game.isMoonsound() );
				assertFalse( game.isMIDI() );
				assertEquals( Genre.BEAT_EM_UP, game.getGenre1() );
				assertEquals( Genre.BOARD_GAMES, game.getGenre2() );
				assertEquals( 26, game.getMsxGenID() );
				assertEquals( "sha1Code2", game.getSha1Code() );
				assertEquals( "rr", game.getScreenshotSuffix() );
				assertEquals( 110, game.getSize() );
				assertEquals( FDDMode.DISABLE_BOTH, game.getFDDMode() );
			}
			else
			{
				//if we get here then the fail failed
				assertFalse( true );
			}
		}
	}

	@Test
	public void test_givenNewGameWithSameName_whenUpdateGame_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name" ).info( "info" ).machine( "machine" )
				.romA( "romA" ).extensionRom( "extensionRom" ).romB( "romB" )
				.diskA( "diskA" ).diskB( "diskB" )
				.tape( "tape" ).harddisk( "harddisk" ).laserdisc( "laserdisc" ).tclScript( "tclScript" )
				.isMSX( true ).isMSX2( true ).isMSX2Plus( true ).isTurboR( true )
				.isPSG( true ).isSCC( true ).isSCCI( true ).isPCM( true )
				.isMSXMUSIC( true ).isMSXAUDIO( true ).isMoonsound( true ).isMIDI( true )
				.genre1( Genre.ADVENTURE_ALL ).genre2( Genre.ARCADE )
				.msxGenID( 25 ).sha1Code( "sha1Code" ).screenshotSuffix( "ss" ).size( 100 )
				.fddMode( FDDMode.DISABLE_BOTH )
				.build();

		persister.saveGame( game1, database1 );

		Game game2 = Game.name( "name" ).info( "info2" ).machine( "machine2" )
				.romA( "romA2" ).extensionRom( "extensionRom2" ).romB( "romB2" )
				.diskA( "diskA2" ).diskB( "diskB2" )
				.tape( "tape2" ).harddisk( "harddisk2" ).laserdisc( "laserdisc2" ).tclScript( "tclScript2" )
				.isMSX( false ).isMSX2( true ).isMSX2Plus( false ).isTurboR( true )
				.isPSG( false ).isSCC( true ).isSCCI( false ).isPCM( true )
				.isMSXMUSIC( false ).isMSXAUDIO( true ).isMoonsound( false ).isMIDI( true )
				.genre1( Genre.CARD_GAMES ).genre2( Genre.COMMUNICATION )
				.msxGenID( 26 ).sha1Code( "sha1Code2" ).screenshotSuffix( "rr" ).size( 110 )
				.build();

		persister.updateGame( game1, game2, database1 );

		Set<Game> games = persister.getGames( database1 );

		assertEquals( 1, games.size() );

		for( Game game: games )
		{
			if( game.getName().equals( "name" ) )
			{
				assertEquals( "info2", game.getInfo() );
				assertEquals( "machine2", game.getMachine() );
				assertEquals( "romA2", game.getRomA() );
				assertEquals( "extensionRom2", game.getExtensionRom() );
				assertEquals( "romB2", game.getRomB() );
				assertEquals( "diskA2", game.getDiskA() );
				assertEquals( "diskB2", game.getDiskB() );
				assertEquals( "tape2", game.getTape() );
				assertEquals( "harddisk2", game.getHarddisk() );
				assertEquals( "laserdisc2", game.getLaserdisc() );
				assertEquals( "tclScript2", game.getTclScript() );
				assertFalse( game.isMSX() );
				assertTrue( game.isMSX2() );
				assertFalse( game.isMSX2Plus() );
				assertTrue( game.isTurboR() );
				assertFalse( game.isPSG() );
				assertTrue( game.isSCC() );
				assertFalse( game.isSCCI() );
				assertTrue( game.isPCM() );
				assertFalse( game.isMSXMUSIC() );
				assertTrue( game.isMSXAUDIO() );
				assertFalse( game.isMoonsound() );
				assertTrue( game.isMIDI() );
				assertEquals( Genre.CARD_GAMES, game.getGenre1() );
				assertEquals( Genre.COMMUNICATION, game.getGenre2() );
				assertEquals( 26, game.getMsxGenID() );
				assertEquals( "sha1Code2", game.getSha1Code() );
				assertEquals( "rr", game.getScreenshotSuffix() );
				assertEquals( 110, game.getSize() );
				assertEquals( FDDMode.ENABLE_BOTH, game.getFDDMode() );
			}
			else
			{
				//if we get here then the test failed
				assertFalse( true );
			}
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenNewGameWithoutName_whenUpdateGame_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name" ).info( "info" ).romA( "romA" ).build();
		Game game2 = Game.info( "info" ).romA( "romA" ).build();

		try
		{
			persister.updateGame( game1, game2, database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenNewGameWithoutMedia_whenUpdateGame_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name" ).info( "info" ).romA( "romA" ).build();
		Game game2 = Game.name( "name" ).info( "info" ).build();

		try
		{
			persister.updateGame( game1, game2, database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.GAME_WITH_MISSING_MEDIA, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenNonexistingNewGame_whenUpdateGame_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name" ).info( "info" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).info( "info" ).romA( "romA" ).build();

		try
		{
			persister.updateGame( game1, game2, database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.GAME_NOT_FOUND, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_givenAlreadyExistingNewGame_whenUpdateGame_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine" ).romA( "romA" ).build();
		Game game3 = Game.name( "name2" ).machine( "machine" ).romA( "romA" ).build();

		persister.saveGame( game1, database1 );
		persister.saveGame( game3, database1 );

		try
		{
			persister.updateGame( game1, game2, database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.GAME_ALREADY_EXISTS, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test
	public void test_whenUpdateGameExtraDataInDatabases_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		String database1 = "Games 1";
		String database2 = "Games 2";

		//create two new databases
		persister.createDatabase( database1 );
		persister.createDatabase( database2 );

		//add games to one of the databases
		Game game1 = Game.name( "game1" ).romA( "romA" ).machine( "machine" ).build();
		Game game2 = Game.name( "game2" ).diskA( "diskA" ).machine( "machine" ).build();
		Game game3 = Game.name( "game3" ).tape( "tape" ).machine( "machine" ).build();

		persister.saveGame( game1, database1 );
		persister.saveGame( game2, database1 );
		persister.saveGame( game3, database2 );

		//call the method under test
		Map<String,ExtraData> extraDataMap = Collections.emptyMap();

		Game newGame1 = Game.name( "game1" ).romA( "romA" ).machine( "machine" )
				.isMSX( true ).isMSX2( true ).isMSX2Plus( true ).isTurboR( true ).build();
		Game newGame2 = Game.name( "game2" ).diskA( "diskA" ).machine( "machine" )
				.isPSG( true ).isSCC( true ).isSCCI( true ).isPCM( true )
				.isMSXMUSIC( true ).isMSXAUDIO( true ).isMoonsound( true ).isMIDI( true ).build();
		Game newGame3 = Game.name( "game3" ).tape( "tape" ).machine( "machine" )
				.genre1( Genre.COMMUNICATION ).genre2( Genre.MISCELLANEOUS ).screenshotSuffix( "new" ).build();

		when( gameBuilder.createGameObjectFromGameAndUpdateExtraData( game1, extraDataMap ) ).thenReturn( newGame1 );
		when( gameBuilder.createGameObjectFromGameAndUpdateExtraData( game2, extraDataMap ) ).thenReturn( newGame2 );
		when( gameBuilder.createGameObjectFromGameAndUpdateExtraData( game3, extraDataMap ) ).thenReturn( newGame3 );

		int numberUpdatedProfiles = persister.updateGameExtraDataInDatabases( extraDataMap );
		assertEquals( 3, numberUpdatedProfiles );

		//get the games from the updated databases
		Set<Game> games1 = persister.getGames( database1 );
		assertEquals( 2, games1.size() );
		for( Game game:games1 )
		{
			if( game.getName().equals( "game1" ) )
			{
				assertTrue( game.isMSX() );
				assertTrue( game.isMSX2() );
				assertTrue( game.isMSX2Plus() );
				assertTrue( game.isTurboR() );
			}
			else if( game.getName().equals( "game2" ) )
			{
				assertTrue( game.isPSG() );
				assertTrue( game.isSCC() );
				assertTrue( game.isSCC() );
				assertTrue( game.isPCM() );
				assertTrue( game.isMSXMUSIC() );
				assertTrue( game.isMSXAUDIO() );
				assertTrue( game.isMoonsound() );
				assertTrue( game.isMIDI() );
			}
		}

		Set<Game> games2 = persister.getGames( database2 );
		assertEquals( 1, games2.size() );
		Game game = games2.iterator().next();
		assertEquals( Genre.COMMUNICATION, game.getGenre1() );
		assertEquals( Genre.MISCELLANEOUS, game.getGenre2() );
		assertEquals( "new", game.getScreenshotSuffix() );

		verify( gameBuilder, times( 3 ) ).createGameObjectFromGameAndUpdateExtraData( any( Game.class ), eq( extraDataMap ) );
	}

	@Test
	public void test_givenDuplicateGameInDestinationAndYesAllIsTrue_whenMoveGames_thenActionDeciderPromptForActionIsCalled() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Game game = Game.name( "testName1" )
				.romA( "testRomA1" )
				.diskA( "testDiskA1" )
				.machine( "testMachine1" )
				.build();

		Set<Game> games = new HashSet<Game>();
		games.add( game );

		persister.createDatabase( database2 );
		persister.saveGame( game, database2 );

		persister.createDatabase( database1 );
		persister.saveGame( game, database1 );

		when( actionDecider.isYesAll() ).thenReturn( false );
		when( actionDecider.isYes() ).thenReturn( true );
		persister.moveGames( games, database1, database2, actionDecider );

		//check that promptForAction was called once
		verify( actionDecider, times( 1 ) ).promptForAction( "testName1" );
	}

	@Test
	public void test_givenDuplicateGameInDestinationAndYesAllIsFalse_whenMoveGames_thenActionDeciderPromptForActionIsNeverCalled() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Game game = Game.name( "testName1" )
				.romA( "testRomA1" )
				.diskA( "testDiskA1" )
				.machine( "testMachine1" )
				.build();

		Set<Game> games = new HashSet<Game>();
		games.add( game );

		persister.createDatabase( database2 );
		persister.saveGame( game, database2 );

		persister.createDatabase( database1 );
		persister.saveGame( game, database1 );

		when( actionDecider.isYesAll() ).thenReturn( true );
		persister.moveGames( games, database1, database2, actionDecider );

		//check that promptForAction was not called
		verify( actionDecider, never() ).promptForAction( null );
	}

	@Test
	public void test_givenDuplicateGameInDestinationAndNoIsTrueAndNoAllFalse_whenMoveGames_thenActionDeciderPromptForActionIsCalled() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Game game = Game.name( "testName1" )
				.romA( "testRomA1" )
				.diskA( "testDiskA1" )
				.machine( "testMachine1" )
				.build();

		Set<Game> games = new HashSet<Game>();
		games.add( game );

		persister.createDatabase( database2 );
		persister.saveGame( game, database2 );

		persister.createDatabase( database1 );
		persister.saveGame( game, database1 );

		when( actionDecider.isNoAll() ).thenReturn( false );
		when( actionDecider.isNo() ).thenReturn( true );
		persister.moveGames( games, database1, database2, actionDecider );

		//check that promptForAction was called once
		verify( actionDecider, times( 1 ) ).promptForAction( "testName1" );
	}

	@Test
	public void test_givenDuplicateGameInDestinationAndNoAllTrue_whenMoveGames_thenActionDeciderPromptForActionIsNeverCalled() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Game game = Game.name( "testName1" )
				.romA( "testRomA1" )
				.diskA( "testDiskA1" )
				.machine( "testMachine1" )
				.build();

		Set<Game> games = new HashSet<Game>();
		games.add( game );

		persister.createDatabase( database2 );
		persister.saveGame( game, database2 );

		persister.createDatabase( database1 );
		persister.saveGame( game, database1 );

		when( actionDecider.isNoAll() ).thenReturn( true );
		persister.moveGames( games, database1, database2, actionDecider );

		//check that promptForAction was not called
		verify( actionDecider, never() ).promptForAction( null );
	}

	@Test
	public void test_givenDuplicateGameInDestinationAndYesTrue_whenMoveGames_thenGameIsOverriddenInDestination() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Game game1a = Game.name( "testName1" )
				.romA( "testRomA1a" )
				.diskA( "testDiskA1a" )
				.machine( "testMachine1a" )
				.build();

		Game game1b = Game.name( "testName1" )
				.romA( "testRomA1b" )
				.diskA( "testDiskA1b" )
				.machine( "testMachine1b" )
				.build();

		Game game2a = Game.name( "testName2a" )
				.romA( "testRomA2a" )
				.diskA( "testDiskA2a" )
				.machine( "testMachine2a" )
				.build();

		Game game2b = Game.name( "testName2b" )
				.romA( "testRomA2b" )
				.diskA( "testDiskA2b" )
				.machine( "testMachine2b" )
				.build();

		Set<Game> gamesToMove = new HashSet<Game>();
		gamesToMove.add( game1a );

		persister.createDatabase( database1 );
		persister.saveGame( game1a, database1 );
		persister.saveGame( game2a, database1 );

		persister.createDatabase( database2 );
		persister.saveGame( game1b, database2 );
		persister.saveGame( game2b, database2 );

		when( actionDecider.isYes() ).thenReturn( true );
		persister.moveGames( gamesToMove, database1, database2, actionDecider );

		//now get the games from both databases
		Set<Game> games = persister.getGames( database1 );

		//this database contains only one game
		assertEquals( 1, games.size() );
		assertEquals( "testName2a", games.iterator().next().getName() );

		games = persister.getGames( database2 );

		//this database contains two games
		assertEquals( 2, games.size() );
		assertTrue( games.contains( game1a ) );
		assertTrue( games.contains( game2b ) );

		//make sure that game1a was really moved
		for( Game game: games)
		{
			if( game.getName().equals( "testName1" ) )
			{
				assertEquals( "testRomA1a", game.getRomA() );
				assertEquals( "testDiskA1a", game.getDiskA() );
				assertEquals( "testMachine1a", game.getMachine() );
				break;
			}
		}
	}

	@Test
	public void test_givenDuplicateGameInDestinationAndNoTrue_whenMoveGames_thenGameIsNotOverriddenInDestination() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Game game1a = Game.name( "testName1" )
				.romA( "testRomA1a" )
				.diskA( "testDiskA1a" )
				.machine( "testMachine1a" )
				.build();

		Game game1b = Game.name( "testName1" )
				.romA( "testRomA1b" )
				.diskA( "testDiskA1b" )
				.machine( "testMachine1b" )
				.build();

		Game game2a = Game.name( "testName2a" )
				.romA( "testRomA2a" )
				.diskA( "testDiskA2a" )
				.machine( "testMachine2a" )
				.build();

		Game game2b = Game.name( "testName2b" )
				.romA( "testRomA2b" )
				.diskA( "testDiskA2b" )
				.machine( "testMachine2b" )
				.build();

		Set<Game> gamesToMove = new HashSet<Game>();
		gamesToMove.add( game1a );

		persister.createDatabase( database1 );
		persister.saveGame( game1a, database1 );
		persister.saveGame( game2a, database1 );

		persister.createDatabase( database2 );
		persister.saveGame( game1b, database2 );
		persister.saveGame( game2b, database2 );

		when( actionDecider.isNo() ).thenReturn( true );
		persister.moveGames( gamesToMove, database1, database2, actionDecider );

		//now get the games from both databases
		Set<Game> games = persister.getGames( database1 );

		//this database contains two games
		assertEquals( 2, games.size() );
		assertTrue( games.contains( game1a ) );
		assertTrue( games.contains( game2a ) );

		games = persister.getGames( database2 );

		//this database contains two games
		assertEquals( 2, games.size() );
		assertTrue( games.contains( game1b ) );
		assertTrue( games.contains( game2b ) );
	}

	@Test
	public void test_givenDuplicateGameInDestinationAndCancelTrue_whenMoveGames_thenGamesAreNotMoved() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Game game1a = Game.name( "testName1" )
				.romA( "testRomA1a" )
				.diskA( "testDiskA1a" )
				.machine( "testMachine1a" )
				.build();

		Game game1b = Game.name( "testName1" )
				.romA( "testRomA1b" )
				.diskA( "testDiskA1b" )
				.machine( "testMachine1b" )
				.build();

		Game game2a = Game.name( "testName2a" )
				.romA( "testRomA2a" )
				.diskA( "testDiskA2a" )
				.machine( "testMachine2a" )
				.build();

		Game game2b = Game.name( "testName2b" )
				.romA( "testRomA2b" )
				.diskA( "testDiskA2b" )
				.machine( "testMachine2b" )
				.build();

		Set<Game> gamesToMove = new HashSet<Game>();
		gamesToMove.add( game1a );

		persister.createDatabase( database1 );
		persister.saveGame( game1a, database1 );
		persister.saveGame( game2a, database1 );

		persister.createDatabase( database2 );
		persister.saveGame( game1b, database2 );
		persister.saveGame( game2b, database2 );

		//need to get the games from the old "current" database to initialise the internal cache
		Set<Game> games = persister.getGames( database1 );

		when( actionDecider.isCancel() ).thenReturn( true );
		persister.moveGames( gamesToMove, database1, database2, actionDecider );

		//now get the games from both databases
		games = persister.getGames( database1 );

		//this database contains two games
		assertEquals( 2, games.size() );
		assertTrue( games.contains( game1a ) );
		assertTrue( games.contains( game2a ) );

		games = persister.getGames( database2 );

		//this database contains two games
		assertEquals( 2, games.size() );
		assertTrue( games.contains( game1b ) );
		assertTrue( games.contains( game2b ) );
	}

	@Test( expected = RuntimeException.class )
	public void test_givenDuplicateGameInDestinationAndNoAction_whenMoveGames_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Game game1a = Game.name( "testName1" )
				.romA( "testRomA1a" )
				.diskA( "testDiskA1a" )
				.machine( "testMachine1a" )
				.build();

		Game game1b = Game.name( "testName1" )
				.romA( "testRomA1b" )
				.diskA( "testDiskA1b" )
				.machine( "testMachine1b" )
				.build();

		Set<Game> gamesToMove = new HashSet<Game>();
		gamesToMove.add( game1a );

		persister.createDatabase( database1 );
		persister.saveGame( game1a, database1 );

		persister.createDatabase( database2 );
		persister.saveGame( game1b, database2 );

		//need to get the games from the old "current" database to initialise the internal cache
		persister.getGames( database1 );

		persister.moveGames( gamesToMove, database1, database2, actionDecider );
	}

	@Test
	public void test_givenNoDuplicateGamesInDestination_whenMoveGames_thenAllGamesAreMoved() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Game game1a = Game.name( "testName1a" )
				.romA( "testRomA1a" )
				.diskA( "testDiskA1a" )
				.machine( "testMachine1a" )
				.build();

		Game game1b = Game.name( "testName1b" )
				.romA( "testRomA1b" )
				.diskA( "testDiskA1b" )
				.machine( "testMachine1b" )
				.build();

		Game game2a = Game.name( "testName2a" )
				.romA( "testRomA2a" )
				.diskA( "testDiskA2a" )
				.machine( "testMachine2a" )
				.build();

		Game game2b = Game.name( "testName2b" )
				.romA( "testRomA2b" )
				.diskA( "testDiskA2b" )
				.machine( "testMachine2b" )
				.build();

		Set<Game> gamesToMove = new HashSet<Game>();
		gamesToMove.add( game1a );

		persister.createDatabase( database1 );
		persister.saveGame( game1a, database1 );
		persister.saveGame( game2a, database1 );

		persister.createDatabase( database2 );
		persister.saveGame( game1b, database2 );
		persister.saveGame( game2b, database2 );

		//need to get the games from the old "current" database to initialise the internal cache
		Set<Game> games = persister.getGames( database1 );

		Set<Game> movedGames = persister.moveGames( gamesToMove, database1, database2, actionDecider );

		assertEquals( 1, movedGames.size() );
		assertTrue( movedGames.contains( game1a ) );

		//now get the games from both databases
		games = persister.getGames( database1 );

		//this database contains one game
		assertEquals( 1, games.size() );
		assertTrue( games.contains( game2a ) );

		games = persister.getGames( database2 );

		//this database contains three games
		assertEquals( 3, games.size() );
		assertTrue( games.contains( game1a ) );
		assertTrue( games.contains( game1b ) );
		assertTrue( games.contains( game2b ) );
	}

	@Test
	public void test_whenBackupDatabase_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );
		persister.createDatabase( database2 );

		persister.backupDatabase( database1 );
		persister.backupDatabase( database2 );
	}

	@Test
	public void test_whenGetDatabaseBackups_thenSuccess() throws GamePersistenceException, InterruptedException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		persister.backupDatabase( database1 );

		//wait in between backups otherwise the timestamps will be the same and the second backup will fail
		Thread.sleep( 50 );

		persister.backupDatabase( database1 );

		Set<DatabaseBackup> backups = persister.getBackups( database1 );

		//this database has two backups
		assertEquals( 2, backups.size() );

		backups.stream().forEach( b -> assertEquals( database1, b.getDatabase() ) );
	}

	@Test
	public void test_givenNonExistentDatabase_whenGetDatabaseBackups_thenReturnEmptySet() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Set<DatabaseBackup> backups = persister.getBackups( "non_existent_database" );

		assertEquals( 0, backups.size() );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void test_whenGetDatabaseBackups_thenSetCannotBeModified() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		Set<DatabaseBackup> backups = persister.getBackups( "non_existent_database" );

		backups.add( new DatabaseBackup( database1, new Timestamp( 123 ) ) );
	}

	/*
	 * this will test the ON DELETE CASCADE between the database_backup and game_backup tables. If a backup database is deleted then all backed up
	 * games in the game_backup will be deleted by cascade.
	 */
	@Test
	public void test_whenDeleteDatabaseBackup_thenBackupIsDeleted() throws SQLException, GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "testName1" ).romA( "testRomA1" ).machine( "testMachine1" ).build();
		Game game2 = Game.name( "testName2" ).romA( "testRomA2" ).machine( "testMachine2" ).build();
		
		Set<Game> games = new HashSet<Game>();
		games.add( game1 );
		games.add( game2 );

		persister.saveGames( games, database1 );

		persister.backupDatabase( database1 );

		int count = 0;
		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
			//check that the database and games have been backed up
			try( PreparedStatement statement = connection.prepareStatement( "SELECT COUNT(*) as rowCount FROM game_backup" ) )
			{
				try( ResultSet result = statement.executeQuery() )
				{
					if( result.next() )
					{
						count = result.getInt( 1 );
					}
				}
			}

			//there should be two backed up games
			assertEquals( 2, count );
		}

		Set<DatabaseBackup> backups = persister.getBackups( database1 );

		assertEquals( 1, backups.size() );

		DatabaseBackup backup = backups.iterator().next();

		persister.deleteBackup( backup );

		backups = persister.getBackups( database1 );

		assertEquals( 0, backups.size() );

		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
			//check that the database and games have been backed up
			try( PreparedStatement statement = connection.prepareStatement( "SELECT COUNT(*) as rowCount FROM game_backup" ) )
			{
				try( ResultSet result = statement.executeQuery() )
				{
					if( result.next() )
					{
						count = result.getInt( 1 );
					}
				}
			}

			//there should be no backed up games
			assertEquals( 0, count );
		}
	}

	/*
	 * this will test the ON DELETE CASCADE between the database and database_backup tables. If a database is deleted then all backed up
	 * databases and their backed up games will be deleted by cascade.
	 */
	@Test
	public void test_whenDeleteGamesDatabase_thenBackedUpDatabaseAndGamesAreDeleted() throws SQLException, GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "testName1" ).romA( "testRomA1" ).machine( "testMachine1" ).build();
		Game game2 = Game.name( "testName2" ).romA( "testRomA2" ).machine( "testMachine2" ).build();

		Set<Game> games = new HashSet<Game>();
		games.add( game1 );
		games.add( game2 );

		persister.saveGames( games, database1 );

		persister.backupDatabase( database1 );

		//check that the database and games have been backed up
		int count = 0;
		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
			try( PreparedStatement statement = connection.prepareStatement( "SELECT COUNT(*) as rowCount FROM database_backup" ) )
			{
				try( ResultSet result = statement.executeQuery() )
				{
					if( result.next() )
					{
						count = result.getInt( 1 );
					}
				}
			}

			//there should be one backed up database
			assertEquals( 1, count );

			try( PreparedStatement statement = connection.prepareStatement( "SELECT COUNT(*) as rowCount FROM game_backup" ) )
			{
				try( ResultSet result = statement.executeQuery() )
				{
					if( result.next() )
					{
						count = result.getInt( 1 );
					}
				}
			}

			//there should be two backed up games
			assertEquals( 2, count );
		}

		//now delete the database
		persister.deleteDatabase( database1 );

		//check that backed up database and its games are also deleted
		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
			try( PreparedStatement statement = connection.prepareStatement( "SELECT COUNT(*) as rowCount FROM database_backup" ) )
			{
				try( ResultSet result = statement.executeQuery() )
				{
					if( result.next() )
					{
						count = result.getInt( 1 );
					}
				}
			}

			//there should be no backed up database
			assertEquals( 0, count );

			try( PreparedStatement statement = connection.prepareStatement( "SELECT COUNT(*) as rowCount FROM game_backup" ) )
			{
				try( ResultSet result = statement.executeQuery() )
				{
					if( result.next() )
					{
						count = result.getInt( 1 );
					}
				}
			}

			//there should be no backed up games
			assertEquals( 0, count );
		}
	}

	@Test
	public void test_givenExistingBackup_whenRestoreDatabaseBackup_thenSuccess() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "testName1" ).machine( "testMachine1" ).info( "testInfo1" )
				.romA( "testRomA1" ).romB( "testRomB1" ).extensionRom( "testExtensionRom1" )
				.diskA( "testDiskA1" ).diskB( "testDiskB1" ).tape( "testTape" ).harddisk( "testHarddisk" )
				.tclScript( "testTclScript" ).laserdisc( "testLaserdisc" )
				.isMSX( true ).isMSX2( true ).isMSX2Plus( true ).isTurboR( true )
				.isPSG( true ).isSCC( true ).isSCCI( true ).isPCM( true ).isMSXMUSIC( true ).isMSXAUDIO( true ).isMoonsound( true ).isMIDI( true )
				.genre1( Genre.ADVENTURE_POINT_AND_CLICK ).genre2( Genre.BOARD_GAMES )
				.msxGenID( 4567 ).sha1Code( "testSha1Code" ).size( 102030 ).screenshotSuffix( "tx" )
				.fddMode( FDDMode.ENABLE_BOTH ).tclScriptOverride( true )
				.build();
		Game game2 = Game.name( "testName2" ).romA( "testRomA2" ).machine( "testMachine2" ).build();

		Set<Game> games = new HashSet<Game>();
		games.add( game1 );
		games.add( game2 );

		persister.saveGames( games, database1 );

		persister.backupDatabase( database1 );

		//now add an additional two games and delete an old one
		Game game3 = Game.name( "testName3" ).romA( "testRomA3" ).machine( "testMachine3" ).build();
		Game game4 = Game.name( "testName4" ).romA( "testRomA4" ).machine( "testMachine4" ).build();

		persister.saveGame( game3, database1 );
		persister.saveGame( game4, database1 );

		persister.deleteGame( game1, database1 );

		//make sure we have a total of three games now
		Set<Game> currentGames = persister.getGames( database1 );
		assertEquals( 3, currentGames.size() );

		//restore
		Set<DatabaseBackup> backups = persister.getBackups( database1 );
		assertEquals( 1, backups.size() );

		DatabaseBackup backup = backups.iterator().next();

		persister.restoreBackup( backup );

		//now get the games again
		currentGames = persister.getGames( database1 );
		assertEquals( 2, currentGames.size() );

		assertTrue( currentGames.contains( game1 ) );
		assertTrue( currentGames.contains( game2 ) );

		//make sure backups were deleted
		backups = persister.getBackups( database1 );
		assertEquals( 0, backups.size() );		

		//pick a game and make sure that all its fields were restored
		Game restoredGame1 = currentGames.stream().filter( g -> g.getName().equals( "testName1" ) ).findFirst().get();

		assertEquals( "testName1", restoredGame1.getName() );
		assertEquals( "testMachine1", restoredGame1.getMachine() );
		assertEquals( "testInfo1", restoredGame1.getInfo() );
		assertEquals( "testRomA1", restoredGame1.getRomA() );
		assertEquals( "testRomB1", restoredGame1.getRomB() );
		assertEquals( "testExtensionRom1", restoredGame1.getExtensionRom() );
		assertEquals( "testDiskA1", restoredGame1.getDiskA() );
		assertEquals( "testDiskB1", restoredGame1.getDiskB() );
		assertEquals( "testTape", restoredGame1.getTape() );
		assertEquals( "testHarddisk", restoredGame1.getHarddisk() );
		assertEquals( "testTclScript", restoredGame1.getTclScript() );
		assertEquals( "testLaserdisc", restoredGame1.getLaserdisc() );
		assertTrue( restoredGame1.isMSX() );
		assertTrue( restoredGame1.isMSX2() );
		assertTrue( restoredGame1.isMSX2Plus() );
		assertTrue( restoredGame1.isTurboR() );
		assertTrue( restoredGame1.isPSG() );
		assertTrue( restoredGame1.isSCC() );
		assertTrue( restoredGame1.isSCCI() );
		assertTrue( restoredGame1.isPCM() );
		assertTrue( restoredGame1.isMSXMUSIC() );
		assertTrue( restoredGame1.isMSXAUDIO() );
		assertTrue( restoredGame1.isMoonsound() );
		assertTrue( restoredGame1.isMIDI() );
		assertEquals( Genre.ADVENTURE_POINT_AND_CLICK, restoredGame1.getGenre1() );
		assertEquals( Genre.BOARD_GAMES, restoredGame1.getGenre2() );
		assertEquals( 4567, restoredGame1.getMsxGenID() );
		assertEquals( "testSha1Code", restoredGame1.getSha1Code() );
		assertEquals( 102030, restoredGame1.getSize() );
		assertEquals( "tx", restoredGame1.getScreenshotSuffix() );
		assertEquals( FDDMode.ENABLE_BOTH, restoredGame1.getFDDMode() );
		assertTrue( restoredGame1.isTclScriptOverride() );
	}

	@Test( expected = GamePersistenceException.class )
	public void test_whenRestoreNonExistentDatabaseBackup_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		try
		{
			persister.restoreBackup( new DatabaseBackup( database1, new Timestamp( 4545 ) ) );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.BACKUP_NOT_FOUND, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test( expected = GamePersistenceException.class )
	public void test_whenBackupDatabaseMoreThanLimit_thenThrowException() throws GamePersistenceException
	{
		EmbeddedDatabaseGamePersister persister = new EmbeddedDatabaseGamePersister( dbLocation, gameBuilder );

		persister.createDatabase( database1 );

		Game game1 = Game.name( "name" ).info( "info" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).info( "info" ).romA( "romA" ).build();

		persister.saveGame( game1, database1 );
		persister.saveGame( game2, database1 );

		//only a maximum of 9 backups are allowed
		persister.backupDatabase( database1 );
		persister.backupDatabase( database1 );
		persister.backupDatabase( database1 );
		persister.backupDatabase( database1 );
		persister.backupDatabase( database1 );
		persister.backupDatabase( database1 );
		persister.backupDatabase( database1 );
		persister.backupDatabase( database1 );
		persister.backupDatabase( database1 );

		try
		{
			persister.updateGame( game1, game2, database1 );
		}
		catch( GamePersistenceException gpe )
		{
			assertEquals( GamePersistenceExceptionIssue.DATABASE_MAX_BACKUPS_REACHED, gpe.getIssue() );
			throw gpe;
		}
	}

	@Test
	public void whenChangeAllMachines_thenAllMachinesAreChanged() throws GamePersistenceException
	{
		initializeDatabasesForMachineUpdateTests();

		int totalUpdated = launcherPersistence.getGamePersister().updateMachine( "newMachine", null, null, false );
		assertEquals( 4, totalUpdated );

		Set<Game> games = launcherPersistence.getGamePersister().getGames( database1 );

		assertEquals( 2, games.size() );
		assertTrue( games.stream().map( Game::getMachine ).allMatch( m -> "newMachine".equals( m ) ) );

		games = launcherPersistence.getGamePersister().getGames( database2 );

		assertEquals( 2, games.size() );
		assertTrue( games.stream().map( Game::getMachine ).allMatch( m -> "newMachine".equals( m ) ) );
	}

	@Test
	public void whenChangeSpecificMachineInAllDatabases_thenOnlySpecificMachineIsChanged() throws GamePersistenceException
	{
		initializeDatabasesForMachineUpdateTests();

		int totalUpdated = launcherPersistence.getGamePersister().updateMachine( "newMachine", "machine2", null, false );
		assertEquals( 2, totalUpdated );

		Set<Game> games = launcherPersistence.getGamePersister().getGames( database1 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name1" ) )
			{
				assertEquals( "machine", game.getMachine() );
			}
			else
			{
				assertEquals( "newMachine", game.getMachine() );
			}
		}

		games = launcherPersistence.getGamePersister().getGames( database2 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name3" ) )
			{
				assertEquals( "machine3", game.getMachine() );
			}
			else
			{
				assertEquals( "newMachine", game.getMachine() );
			}
		}
	}

	@Test
	public void whenChangeSpecificMachineInSpecificDatabase_thenOnlySpecificMachineInSpecificDatabaseIsChanged() throws GamePersistenceException
	{
		initializeDatabasesForMachineUpdateTests();

		int totalUpdated = launcherPersistence.getGamePersister().updateMachine( "newMachine", "machine2", database2, false );
		assertEquals( 1, totalUpdated );

		Set<Game> games = launcherPersistence.getGamePersister().getGames( database1 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name1" ) )
			{
				assertEquals( "machine", game.getMachine() );
			}
			else
			{
				assertEquals( "machine2", game.getMachine() );
			}
		}

		games = launcherPersistence.getGamePersister().getGames( database2 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name3" ) )
			{
				assertEquals( "machine3", game.getMachine() );
			}
			else
			{
				assertEquals( "newMachine", game.getMachine() );
			}
		}
	}

	@Test
	public void whenChangeAllMachinesInSpecificDatabase_thenAllMachinesInSpecificDatabaseAreChanged() throws GamePersistenceException
	{
		initializeDatabasesForMachineUpdateTests();

		int totalUpdated = launcherPersistence.getGamePersister().updateMachine( "newMachine", null, database1, false );
		assertEquals( 2, totalUpdated );

		Set<Game> games = launcherPersistence.getGamePersister().getGames( database1 );

		assertEquals( 2, games.size() );
		assertTrue( games.stream().map( Game::getMachine ).allMatch( m -> "newMachine".equals( m ) ) );

		games = launcherPersistence.getGamePersister().getGames( database2 );

		assertEquals( 2, games.size() );
		for( Game game: games )
		{
			if( game.getName().equals( "name3" ) )
			{
				assertEquals( "machine3", game.getMachine() );
			}
			else
			{
				assertEquals( "machine2", game.getMachine() );
			}
		}
	}

	private void initializeDatabasesForMachineUpdateTests() throws GamePersistenceException
	{
		Game game1 = Game.name( "name1" ).machine( "machine" ).romA( "romA" ).build();
		Game game2 = Game.name( "name2" ).machine( "machine2" ).diskA( "diskA2" ).build();

		launcherPersistence.getGamePersister().createDatabase( database1 );
		launcherPersistence.getGamePersister().saveGame( game1, database1 );
		launcherPersistence.getGamePersister().saveGame( game2, database1 );

		Game game3 = Game.name( "name3" ).machine( "machine3" ).romA( "romA3" ).build();
		Game game4 = Game.name( "name4" ).machine( "machine2" ).romA( "romA4" ).build();

		launcherPersistence.getGamePersister().createDatabase( database2 );
		launcherPersistence.getGamePersister().saveGame( game3, database2 );
		launcherPersistence.getGamePersister().saveGame( game4, database2 );
	}
}