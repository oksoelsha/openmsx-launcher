package info.msxlaunchers.openmsx.launcher.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import info.msxlaunchers.openmsx.common.FileUtils;
import info.msxlaunchers.openmsx.common.HashUtils;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith( MockitoJUnitRunner.class )
public class GameBuilderTest
{
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void testConstructor()
	{
		new GameBuilder( "url" );
	}

	@Test( expected = NullPointerException.class )
	public void test_WhenCreatingGameBuilderWithNullArg1_ThenThrowNullPointerException()
	{
		new GameBuilder( null );
	}

	@Test
	public void test_GivenNullGameFields_WhenCallingCreateGameObjectForDataEnteredByUser_ThenGameShouldBeNull()
	{
		GameBuilder gameBuilder = new GameBuilder( "url" );

		Game game = gameBuilder.createGameObjectForDataEnteredByUser( null, null, null, null, null, null, null, null, null, null, null, null, null, null );

		assertNull( game );
	}

	@Test
	public void test_GivenNullGameFields_WhenCallingCreateGameObjectForImportedData_ThenGameShouldBeNull()
	{
		GameBuilder gameBuilder = new GameBuilder( "url" );

		Game game = gameBuilder.createGameObjectForImportedData( null, null, null, null, null, null, null, null, null, null, null );

		assertNull( game );
	}

	@Test
	public void test_GivenNullGameFields_WhenCallingCreateGameObjectForScannedFiles_ThenGameShouldBeNull()
	{
		GameBuilder gameBuilder = new GameBuilder( "url" );

		Game game = gameBuilder.createGameObjectForScannedFiles( null, null, null, null, null, null, null, null, null, 0, null );

		assertNull( game );
	}

	@Test( expected = NullPointerException.class )
	public void test_GivenNullGameObject_WhenCallingCreateGameObjectFromGameAndUpdateExtraData_ThenThrowNullPointerException()
	{
		GameBuilder gameBuilder = new GameBuilder( "url" );

		gameBuilder.createGameObjectFromGameAndUpdateExtraData( null, null );
	}

	@Test
	public void test_GivenRomAAndExtraData_WhenCallingCreateGameObjectForDataEnteredByUser_ThenGameExtraFieldsShouldBeSet() throws IOException
	{
		//create temporary ROM file and get its sha1Code
		File tmpFile = tmpFolder.newFile( "romfile.rom" );
		PrintWriter writer = new PrintWriter( tmpFile );
		writer.println( "romdata" );
		writer.close();
		String sha1Code = HashUtils.getSHA1Code( tmpFile );

		assertTrue( !Utils.isEmpty( sha1Code ) );

		//now create a mock extra data
		ExtraData extraData = new ExtraData( 99, 5, 48, 3, 9, "s" );
		Map<String,ExtraData> extraDataMap = new HashMap<String,ExtraData>();
		extraDataMap.put( sha1Code, extraData );

		String name = "name";
		String info = "info";

		GameBuilder gameBuilder = new GameBuilder( "url" );
		Game game = gameBuilder.createGameObjectForDataEnteredByUser( name, info, null, tmpFile.getAbsolutePath(), null, null, null, null, null, null, null, null, null, extraDataMap );

		//now check all game fields
		assertEquals( name, game.getName() );
		assertEquals( info, game.getInfo() );
		assertNull( game.getTclScript() );
		assertNull( game.getMachine() );
		assertEquals( tmpFile.getAbsolutePath(), game.getRomA() );
		assertNull( game.getExtensionRom() );
		assertNull( game.getRomB() );
		assertNull( game.getDiskA() );
		assertNull( game.getDiskB() );
		assertNull( game.getTape() );
		assertNull( game.getHarddisk() );
		assertNull( game.getLaserdisc() );
		assertEquals( 99, game.getMsxGenID() );
		assertEquals( sha1Code, game.getSha1Code() );
		assertEquals( tmpFile.length(), game.getSize() );
		assertEquals( "s", game.getScreenshotSuffix() );
		assertEquals( Genre.ADVENTURE_ALL, game.getGenre1() );
		assertEquals( Genre.BREAK_OUT, game.getGenre2() );
		assertTrue( game.isMSX() );
		assertFalse( game.isMSX2() );
		assertTrue( game.isMSX2Plus() );
		assertFalse( game.isTurboR() );
		assertFalse( game.isPSG() );
		assertFalse( game.isSCC() );
		assertFalse( game.isSCCI() );
		assertFalse( game.isPCM() );
		assertTrue( game.isMSXMUSIC() );
		assertTrue( game.isMSXAUDIO() );
		assertFalse( game.isMoonsound() );
		assertFalse( game.isMIDI() );
		assertNull( game.getFDDMode() );
	}

	@Test
	public void test_GivenRomAAsZipAndExtraData_WhenCallingCreateGameObjectForDataEnteredByUser_ThenGameExtraFieldsShouldBeSet() throws IOException
	{
		//create temporary ROM file and get its sha1Code
		File tmpFile = tmpFolder.newFile( "romfile.rom" );
		PrintWriter writer = new PrintWriter( tmpFile );
		writer.println( "romdata" );
		writer.close();
		String sha1Code = HashUtils.getSHA1Code( tmpFile );

		assertTrue( !Utils.isEmpty( sha1Code ) );

		//create a ZIP file of this ROM
		File tmpZipFile = tmpFolder.newFile( "romfile.zip" );
		createZiPFile( tmpFile, tmpZipFile );

		//now create a mock extra data
		ExtraData extraData = new ExtraData( 99, 5, 48, 3, 9, "s" );
		Map<String,ExtraData> extraDataMap = new HashMap<String,ExtraData>();
		extraDataMap.put( sha1Code, extraData );

		String name = "name";
		String info = "info";

		GameBuilder gameBuilder = new GameBuilder( "url" );
		Game game = gameBuilder.createGameObjectForDataEnteredByUser( name, info, null, tmpZipFile.getAbsolutePath(), null, null, null, null, null, null, null, null, null, extraDataMap );

		//now check all game fields
		assertEquals( name, game.getName() );
		assertEquals( info, game.getInfo() );
		assertNull( game.getTclScript() );
		assertNull( game.getMachine() );
		assertEquals( tmpZipFile.getAbsolutePath(), game.getRomA() );
		assertNull( game.getExtensionRom() );
		assertNull( game.getRomB() );
		assertNull( game.getDiskA() );
		assertNull( game.getDiskB() );
		assertNull( game.getTape() );
		assertNull( game.getHarddisk() );
		assertNull( game.getLaserdisc() );
		assertEquals( 99, game.getMsxGenID() );
		assertEquals( sha1Code, game.getSha1Code() );
		assertEquals( tmpFile.length(), game.getSize() );
		assertEquals( "s", game.getScreenshotSuffix() );
		assertEquals( Genre.ADVENTURE_ALL, game.getGenre1() );
		assertEquals( Genre.BREAK_OUT, game.getGenre2() );
		assertTrue( game.isMSX() );
		assertFalse( game.isMSX2() );
		assertTrue( game.isMSX2Plus() );
		assertFalse( game.isTurboR() );
		assertFalse( game.isPSG() );
		assertFalse( game.isSCC() );
		assertFalse( game.isSCCI() );
		assertFalse( game.isPCM() );
		assertTrue( game.isMSXMUSIC() );
		assertTrue( game.isMSXAUDIO() );
		assertFalse( game.isMoonsound() );
		assertFalse( game.isMIDI() );
		assertNull( game.getFDDMode() );
	}

	@Test
	public void test_GivenScriptAndNoExtraData_WhenCallingCreateGameObjectForDataEnteredByUser_ThenGameExtraFieldsShouldNotBeSet()
	{
		GameBuilder gameBuilder = new GameBuilder( "url" );

		String name = "name";
		String info = "info";
		String script = "script";

		Game game = gameBuilder.createGameObjectForDataEnteredByUser( name, info, null, null, null, null, null, null, null, null, null, script, FDDMode.DISABLE_SECOND, null );

		//the following fields are set
		assertEquals( name, game.getName() );
		assertEquals( info, game.getInfo() );
		assertEquals( script, game.getTclScript() );

		//the following fields are not set
		assertNull( game.getMachine() );
		assertNull( game.getRomA() );
		assertNull( game.getExtensionRom() );
		assertNull( game.getRomB() );
		assertNull( game.getDiskA() );
		assertNull( game.getDiskB() );
		assertNull( game.getTape() );
		assertNull( game.getHarddisk() );
		assertNull( game.getLaserdisc() );
		assertEquals( 0, game.getMsxGenID() );
		assertNull( game.getSha1Code() );
		assertEquals( 0, game.getSize() );
		assertNull( game.getScreenshotSuffix() );
		assertEquals( Genre.UNKNOWN, game.getGenre1() );
		assertEquals( Genre.UNKNOWN, game.getGenre2() );
		assertFalse( game.isMSX() );
		assertFalse( game.isMSX2() );
		assertFalse( game.isMSX2Plus() );
		assertFalse( game.isTurboR() );
		assertFalse( game.isPSG() );
		assertFalse( game.isSCC() );
		assertFalse( game.isSCCI() );
		assertFalse( game.isPCM() );
		assertFalse( game.isMSXMUSIC() );
		assertFalse( game.isMSXAUDIO() );
		assertFalse( game.isMoonsound() );
		assertFalse( game.isMIDI() );
		assertEquals( FDDMode.DISABLE_SECOND, game.getFDDMode() );
	}

	@Test
	public void test_GivenRomAAndExtraData_WhenCallingCreateGameObjectForImportedData_ThenGameExtraFieldsShouldBeSet() throws IOException
	{
		//create temporary ROM file and get its sha1Code
		File tmpFile = tmpFolder.newFile( "romfile.rom" );
		PrintWriter writer = new PrintWriter( tmpFile );
		writer.println( "gameRomData" );
		writer.close();
		String sha1Code = HashUtils.getSHA1Code( tmpFile );

		assertTrue( !Utils.isEmpty( sha1Code ) );

		//now create a mock extra data
		ExtraData extraData = new ExtraData( 199, 10, 6, 12, 20, "r" );
		Map<String,ExtraData> extraDataMap = new HashMap<String,ExtraData>();
		extraDataMap.put( sha1Code, extraData );

		String name = "name";
		String info = "info";
		String machine = "msx2+";

		GameBuilder gameBuilder = new GameBuilder( "url" );
		Game game = gameBuilder.createGameObjectForImportedData( name, info, machine, tmpFile.getAbsolutePath(), null, null, null, null, null, null, extraDataMap );

		//now check all game fields
		assertEquals( name, game.getName() );
		assertEquals( info, game.getInfo() );
		assertNull( game.getTclScript() );
		assertEquals( machine, game.getMachine() );
		assertEquals( tmpFile.getAbsolutePath(), game.getRomA() );
		assertNull( game.getExtensionRom() );
		assertNull( game.getRomB() );
		assertNull( game.getDiskA() );
		assertNull( game.getDiskB() );
		assertNull( game.getTape() );
		assertNull( game.getHarddisk() );
		assertNull( game.getLaserdisc() );
		assertEquals( 199, game.getMsxGenID() );
		assertEquals( sha1Code, game.getSha1Code() );
		assertEquals( tmpFile.length(), game.getSize() );
		assertEquals( "r", game.getScreenshotSuffix() );
		assertEquals( Genre.COMPILER, game.getGenre1() );
		assertEquals( Genre.MISCELLANEOUS, game.getGenre2() );
		assertFalse( game.isMSX() );
		assertTrue( game.isMSX2() );
		assertFalse( game.isMSX2Plus() );
		assertTrue( game.isTurboR() );
		assertFalse( game.isPSG() );
		assertTrue( game.isSCC() );
		assertTrue( game.isSCCI() );
		assertFalse( game.isPCM() );
		assertFalse( game.isMSXMUSIC() );
		assertFalse( game.isMSXAUDIO() );
		assertFalse( game.isMoonsound() );
		assertFalse( game.isMIDI() );
	}

	@Test
	public void test_GivenNonExistingRomAAndExtraData_WhenCallingCreateGameObjectForImportedData_ThenReturnNull() throws IOException
	{
		//create a mock extra data
		ExtraData extraData = new ExtraData( 199, 10, 6, 12, 20, "r" );
		Map<String,ExtraData> extraDataMap = new HashMap<String,ExtraData>();
		extraDataMap.put( "123456", extraData );

		String name = "name";
		String info = "info";
		String machine = "msx2+";
		String rom = "rom";

		GameBuilder gameBuilder = new GameBuilder( "url" );
		Game game = gameBuilder.createGameObjectForImportedData( name, info, machine, rom, null, null, null, null, null, null, extraDataMap );

		assertNull( game );
	}

	@Test
	public void test_GivenNoExtraData_WhenCallingCreateGameObjectForScannedFiles_ThenGameExtraFieldsShouldNotBeSet() throws FileNotFoundException, IOException
	{
		//create a mock extra data
		ExtraData extraData = new ExtraData( 199, 10, 32, 12, 20, "ar" );
		Map<String,ExtraData> extraDataMap = new HashMap<String,ExtraData>();
		extraDataMap.put( "123456", extraData );

		GameBuilder gameBuilder = new GameBuilder( "url/" );

		String name = "name";
		String machine = "machine";
		String rom = "rom";
		String extensionRom = "extensionRom";
		String disk = "disk";
		String tape = "tape";
		String harddisk = "harddisk";
		String laserdisc = "laserdisc";
		String sha1Code = "123456";
		long fileSize = 10;

		Game game = gameBuilder.createGameObjectForScannedFiles( name, machine, rom, extensionRom, disk, tape, harddisk, laserdisc, sha1Code, fileSize, extraDataMap );

		assertEquals( name, game.getName() );
		assertEquals( machine, game.getMachine() );
		assertEquals( "url/199", game.getInfo() );
		assertEquals( rom, game.getRomA() );
		assertNull( game.getRomB() );
		assertEquals( extensionRom, game.getExtensionRom() );
		assertEquals( disk, game.getDiskA() );
		assertNull( game.getDiskB() );
		assertEquals( tape, game.getTape() );
		assertEquals( harddisk, game.getHarddisk() );
		assertEquals( laserdisc, game.getLaserdisc() );
		assertEquals( sha1Code, game.getSha1Code() );
		assertEquals( fileSize, game.getSize() );
		assertEquals( 199, game.getMsxGenID() );
		assertEquals( "ar", game.getScreenshotSuffix() );
		assertNull( game.getTclScript() );
		assertEquals( Genre.COMPILER, game.getGenre1() );
		assertEquals( Genre.MISCELLANEOUS, game.getGenre2() );
		assertFalse( game.isMSX() );
		assertTrue( game.isMSX2() );
		assertFalse( game.isMSX2Plus() );
		assertTrue( game.isTurboR() );
		assertFalse( game.isPSG() );
		assertFalse( game.isSCC() );
		assertFalse( game.isSCCI() );
		assertFalse( game.isPCM() );
		assertFalse( game.isMSXMUSIC() );
		assertTrue( game.isMSXAUDIO() );
		assertFalse( game.isMoonsound() );
		assertFalse( game.isMIDI() );
	}

	@Test
	public void test_GivenGame_WhenCallingCreateGameObjectFromGameAndUpdateExtraData_ThenReturnGameWithExtraData() throws IOException
	{
		//create temporary Disk file and get its sha1Code
		File tmpFile = tmpFolder.newFile( "diskfile.dsk" );
		PrintWriter writer = new PrintWriter( tmpFile );
		writer.println( "gameDiskData" );
		writer.close();
		String sha1Code = HashUtils.getSHA1Code( tmpFile );

		assertTrue( !Utils.isEmpty( sha1Code ) );

		String name = "name";
		String machine = "machine";

		Game game = Game.name( name ).machine( machine ).diskA( tmpFile.getAbsolutePath() ).sha1Code( sha1Code ).size( tmpFile.length() ).build();

		//now create a mock extra data
		ExtraData extraData = new ExtraData( 299, 10, 129, 30, 40, "en" );
		Map<String,ExtraData> extraDataMap = new HashMap<String,ExtraData>();
		extraDataMap.put( sha1Code, extraData );

		GameBuilder gameBuilder = new GameBuilder( "url" );
		Game newGame = gameBuilder.createGameObjectFromGameAndUpdateExtraData( game, extraDataMap );

		assertEquals( name, newGame.getName() );
		assertEquals( machine, newGame.getMachine() );
		assertNull( newGame.getRomA() );
		assertNull( newGame.getRomB() );
		assertNull( newGame.getExtensionRom() );
		assertEquals( tmpFile.getAbsolutePath(), newGame.getDiskA() );
		assertNull( newGame.getDiskB() );
		assertNull( newGame.getTape() );
		assertNull( newGame.getHarddisk() );
		assertNull( newGame.getLaserdisc() );
		assertEquals( sha1Code, newGame.getSha1Code() );
		assertEquals( tmpFile.length(), newGame.getSize() );
		assertNull( newGame.getInfo() );
		assertNull( newGame.getTclScript() );
		assertEquals( 299, newGame.getMsxGenID() );
		assertEquals( "en", newGame.getScreenshotSuffix() );
		assertEquals( Genre.RPG, newGame.getGenre1() );
		assertEquals( Genre.SPORT_MANAGEMENT, newGame.getGenre2() );
		assertFalse( newGame.isMSX() );
		assertTrue( newGame.isMSX2() );
		assertFalse( newGame.isMSX2Plus() );
		assertTrue( newGame.isTurboR() );
		assertTrue( newGame.isPSG() );
		assertFalse( newGame.isSCC() );
		assertFalse( newGame.isSCCI() );
		assertFalse( newGame.isPCM() );
		assertFalse( newGame.isMSXMUSIC() );
		assertFalse( newGame.isMSXAUDIO() );
		assertFalse( newGame.isMoonsound() );
		assertTrue( newGame.isMIDI() );
	}

	@Test
	public void test_GivenGameAndNonEixtingExtraDataFile_WhenCallingCreateGameObjectFromGameAndUpdateExtraData_ThenReturnGameWithNoExtraData() throws IOException
	{
		//create temporary Disk file and get its sha1Code
		File tmpFile = tmpFolder.newFile( "diskfile.dsk" );
		PrintWriter writer = new PrintWriter( tmpFile );
		writer.println( "gameDiskData" );
		writer.close();
		String sha1Code = HashUtils.getSHA1Code( tmpFile );

		assertTrue( !Utils.isEmpty( sha1Code ) );

		String name = "name";
		String machine = "machine";
		String info = "info";

		Game game = Game.name( name ).machine( machine ).diskA( tmpFile.getAbsolutePath() ).info( info )
				.isMSX2( true ).isMSX2Plus( true ).isPCM( true ).isMoonsound( true )
				.sha1Code( sha1Code ).size( tmpFile.length() ).build();

		GameBuilder gameBuilder = new GameBuilder( "url" );
		Game newGame = gameBuilder.createGameObjectFromGameAndUpdateExtraData( game, null );

		assertEquals( name, newGame.getName() );
		assertEquals( machine, newGame.getMachine() );
		assertEquals( info, newGame.getInfo() );
		assertNull( newGame.getRomA() );
		assertNull( newGame.getRomB() );
		assertNull( newGame.getExtensionRom() );
		assertEquals( tmpFile.getAbsolutePath(), newGame.getDiskA() );
		assertNull( newGame.getDiskB() );
		assertNull( newGame.getTape() );
		assertNull( newGame.getHarddisk() );
		assertNull( newGame.getLaserdisc() );
		assertEquals( sha1Code, newGame.getSha1Code() );
		assertEquals( tmpFile.length(), newGame.getSize() );
		assertNull( newGame.getTclScript() );
		assertEquals( 0, newGame.getMsxGenID() );
		assertNull( newGame.getScreenshotSuffix() );
		assertEquals( Genre.UNKNOWN, newGame.getGenre1() );
		assertEquals( Genre.UNKNOWN, newGame.getGenre2() );
		assertFalse( newGame.isMSX() );
		assertFalse( newGame.isMSX2() );
		assertFalse( newGame.isMSX2Plus() );
		assertFalse( newGame.isTurboR() );
		assertFalse( newGame.isPSG() );
		assertFalse( newGame.isSCC() );
		assertFalse( newGame.isSCCI() );
		assertFalse( newGame.isPCM() );
		assertFalse( newGame.isMSXMUSIC() );
		assertFalse( newGame.isMSXAUDIO() );
		assertFalse( newGame.isMoonsound() );
		assertFalse( newGame.isMIDI() );
	}

	@Test
	public void test_GivenGameAndExtraDataGetterThrowingIOException_WhenCallingCreateGameObjectFromGameAndUpdateExtraData_ThenReturnGameWithNoExtraData() throws IOException
	{
		//create temporary Disk file and get its sha1Code
		File tmpFile = tmpFolder.newFile( "diskfile.dsk" );
		PrintWriter writer = new PrintWriter( tmpFile );
		writer.println( "gameDiskData" );
		writer.close();
		String sha1Code = HashUtils.getSHA1Code( tmpFile );

		assertTrue( !Utils.isEmpty( sha1Code ) );

		String name = "name";
		String machine = "machine";
		String info = "info";

		Game game = Game.name( name ).machine( machine ).diskA( tmpFile.getAbsolutePath() ).info( info )
				.isMSX2( true ).isMSX2Plus( true ).isPCM( true ).isMoonsound( true )
				.sha1Code( sha1Code ).size( tmpFile.length() ).build();

		GameBuilder gameBuilder = new GameBuilder( "url" );
		Game newGame = gameBuilder.createGameObjectFromGameAndUpdateExtraData( game, null );

		assertEquals( name, newGame.getName() );
		assertEquals( machine, newGame.getMachine() );
		assertEquals( info, newGame.getInfo() );
		assertNull( newGame.getRomA() );
		assertNull( newGame.getRomB() );
		assertNull( newGame.getExtensionRom() );
		assertEquals( tmpFile.getAbsolutePath(), newGame.getDiskA() );
		assertNull( newGame.getDiskB() );
		assertNull( newGame.getTape() );
		assertNull( newGame.getHarddisk() );
		assertNull( newGame.getLaserdisc() );
		assertEquals( sha1Code, newGame.getSha1Code() );
		assertEquals( tmpFile.length(), newGame.getSize() );
		assertNull( newGame.getTclScript() );
		assertEquals( 0, newGame.getMsxGenID() );
		assertNull( newGame.getScreenshotSuffix() );
		assertEquals( Genre.UNKNOWN, newGame.getGenre1() );
		assertEquals( Genre.UNKNOWN, newGame.getGenre2() );
		assertFalse( newGame.isMSX() );
		assertFalse( newGame.isMSX2() );
		assertFalse( newGame.isMSX2Plus() );
		assertFalse( newGame.isTurboR() );
		assertFalse( newGame.isPSG() );
		assertFalse( newGame.isSCC() );
		assertFalse( newGame.isSCCI() );
		assertFalse( newGame.isPCM() );
		assertFalse( newGame.isMSXMUSIC() );
		assertFalse( newGame.isMSXAUDIO() );
		assertFalse( newGame.isMoonsound() );
		assertFalse( newGame.isMIDI() );
	}

	//The following test is for a private method. I use reflection to get it and call it
	@Test
	public void test_GivenManyGameObjects_WhenCallingIsNotScript_ThenReturnTrueWhenScriptOnly() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException
	{
		GameBuilder gameBuilder = new GameBuilder( "url" );

		Method method = GameBuilder.class.getDeclaredMethod( "isNotScript", String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class );
		method.setAccessible( true );

		assertTrue( (Boolean)method.invoke( gameBuilder, "romA", null, null, null, null, null, null, null ) );
		assertTrue( (Boolean)method.invoke( gameBuilder, null, "romB", null, null, null, null, null, null ) );
		assertTrue( (Boolean)method.invoke( gameBuilder, null, null, "diskA", null, null, null, null, null ) );
		assertTrue( (Boolean)method.invoke( gameBuilder, null, null, null, "diskB", null, null, null, null ) );
		assertTrue( (Boolean)method.invoke( gameBuilder, null, null, null, null, "tape", null, null, null ) );
		assertTrue( (Boolean)method.invoke( gameBuilder, null, null, null, null, null, "harddisk", null, null ) );
		assertTrue( (Boolean)method.invoke( gameBuilder, null, null, null, null, null, null, "laserdisc", null ) );
		assertFalse( (Boolean)method.invoke( gameBuilder, null, null, null, null, null, null, null, "script" ) );
		assertTrue( (Boolean)method.invoke( gameBuilder, null, null, null, null, null, null, null, null ) );
	}

	private void createZiPFile( File src, File zipFile ) throws IOException
	{
		FileInputStream in = new FileInputStream( src );
		ZipOutputStream out = new ZipOutputStream( new FileOutputStream( zipFile ) );

		//name the file inside the ZIP file is the name of the source file minus extension
		String srcFileName = FileUtils.getFileNameWithoutExtension( src );
		out.putNextEntry( new ZipEntry( srcFileName ) ); 

		// buffer size
		byte[] b = new byte[1024];
		int count;

		while ( (count = in.read( b ) ) > 0 )
		{
			out.write( b, 0, count );
		}

		out.close();
		in.close();
	}
}
