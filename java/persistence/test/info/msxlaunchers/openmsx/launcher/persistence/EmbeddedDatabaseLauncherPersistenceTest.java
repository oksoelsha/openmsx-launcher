package info.msxlaunchers.openmsx.launcher.persistence;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersistenceException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Guice;
import com.google.inject.Injector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith( MockitoJUnitRunner.class )
public class EmbeddedDatabaseLauncherPersistenceTest
{
	@ClassRule
	public static TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void test_whenInstantiatingEmbeddedDatabaseLauncherPersistence_thenOldCSVFilesAreImported() throws SQLException, GamePersistenceException, IOException, LauncherPersistenceException
	{
		//create sample CSV files
		String database1 = "database1.dbo";
		String database2 = "database2.dbo";
		File databasesFolder = tmpFolder.newFolder( "databases" );

		String line1 = "name1,info1,machine1,romA1,,romB1,diskA1,diskB1,tape1,harddisk1,laserdisc1,,1,1,1,1,1,1,1,1,1,1,1,1,1,2,10,yy,ac4facb517e2383b4cb22504cda961b15af0a83e,100";
		String line2 = "name2,,machine2,,scc,,,,,,,script2,1,0,1,0,1,0,1,0,1,0,1,0,5,,20,,7e0083579f59e26311b7e2f65459caeb0bf46242,200";
		File tmpFile = new File( databasesFolder, database1 );
		PrintWriter writer = new PrintWriter( tmpFile );
		writer.println( line1 );
		writer.println( line2 );
		writer.close();

		String line3 = "name3,info3,machine3,romA3,,,diskA3,,,harddisk3,,,0,1,0,1,0,1,0,1,0,1,0,1,10,,,,65b618cbcde0690164f1c4f791635a0c83bf020f,300";
		tmpFile = new File( databasesFolder, database2 );
		writer = new PrintWriter( tmpFile );
		writer.println( line3 );
		writer.close();

		//create the database
		Injector injector = Guice.createInjector(
				new BasicTestModule( tmpFolder.getRoot().toString() ),
	    		new LauncherPersistenceModule()
				);

		//instantiating an object of type LauncherPersistence will intialise the database
		LauncherPersistence launcherPersistence = injector.getInstance( LauncherPersistence.class );
		launcherPersistence.initialize();

		//import and validate
		String db = new File( databasesFolder.getAbsolutePath(), "launcherdb" ).toString();
		String dbURL = "jdbc:derby:" + db;

		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
			//validate that data is in the embedded database
			Set<Game> games1 = launcherPersistence.getGamePersister().getGames( "database1" );

			assertEquals( 2, games1.size() );

			for( Game game: games1 )
			{
				if( game.getName().equals( "name1" ) )
				{
					assertEquals( "info1", game.getInfo() );
					assertEquals( "machine1", game.getMachine() );
					assertEquals( "romA1", game.getRomA() );
					assertNull( game.getExtensionRom() );
					assertEquals( "romB1", game.getRomB() );
					assertEquals( "diskA1", game.getDiskA() );
					assertEquals( "diskB1", game.getDiskB() );
					assertEquals( "tape1", game.getTape() );
					assertEquals( "harddisk1", game.getHarddisk() );
					assertEquals( "laserdisc1", game.getLaserdisc() );
					assertNull( game.getTclScript() );
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
					assertEquals( Genre.ADULT, game.getGenre2() );
					assertEquals( 10, game.getMsxGenID() );
					assertEquals( "yy", game.getScreenshotSuffix() );
					assertEquals( "ac4facb517e2383b4cb22504cda961b15af0a83e", game.getSha1Code() );
					assertEquals( 100, game.getSize() );
				}
				else if( game.getName().equals( "name2" ) )
				{
					assertNull( game.getInfo() );
					assertEquals( "machine2", game.getMachine() );
					assertNull( game.getRomA() );
					assertEquals( "scc", game.getExtensionRom() );
					assertNull( game.getRomB() );
					assertNull( game.getDiskA() );
					assertNull( game.getDiskB() );
					assertNull( game.getTape() );
					assertNull( game.getHarddisk() );
					assertNull( game.getLaserdisc() );
					assertEquals( "script2", game.getTclScript() );
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
					assertEquals( Genre.ADVENTURE_TEXT_AND_GFX, game.getGenre1() );
					assertEquals( Genre.UNKNOWN, game.getGenre2() );
					assertEquals( 20, game.getMsxGenID() );
					assertNull( game.getScreenshotSuffix() );
					assertEquals( "7e0083579f59e26311b7e2f65459caeb0bf46242", game.getSha1Code() );
					assertEquals( 200, game.getSize() );
				}
				else
				{
					throw new RuntimeException( "Failure - wrong game name in database1" );
				}
			}

			Set<Game> games2 = launcherPersistence.getGamePersister().getGames( "database2" );

			assertEquals( 1, games2.size() );

			for( Game game: games2 )
			{
				if( game.getName().equals( "name3" ) )
				{
					assertEquals( "info3", game.getInfo() );
					assertEquals( "machine3", game.getMachine() );
					assertEquals( "romA3", game.getRomA() );
					assertNull( game.getExtensionRom() );
					assertNull( game.getRomB() );
					assertEquals( "diskA3", game.getDiskA() );
					assertNull( game.getDiskB() );
					assertNull( game.getTape() );
					assertEquals( "harddisk3", game.getHarddisk() );
					assertNull( game.getLaserdisc() );
					assertNull( game.getTclScript() );
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
					assertEquals( Genre.UNKNOWN, game.getGenre2() );
					assertEquals( 0, game.getMsxGenID() );
					assertNull( game.getScreenshotSuffix() );
					assertEquals( "65b618cbcde0690164f1c4f791635a0c83bf020f", game.getSha1Code() );
					assertEquals( 300, game.getSize() );
				}
				else
				{
					throw new RuntimeException( "Failure - wrong game name in database1 " );
				}
			}

			//and also validate that the CSV files were renamed to add .bak to them
			File[] files = databasesFolder.listFiles();
			boolean foundDatabaseBackup1 = false;
			boolean foundDatabaseBackup2 = false;

			for( File file: files )
			{
				if( file.isFile() && file.getName().startsWith( database1 ) )
				{
					assertEquals( database1 + ".bak", file.getName() );
					foundDatabaseBackup1 = true;
				}
				else if( file.isFile() && file.getName().startsWith( database2 ) )
				{
					assertEquals( database2 + ".bak", file.getName() );
					foundDatabaseBackup2 = true;
				}
			}

			assertTrue( foundDatabaseBackup1 );
			assertTrue( foundDatabaseBackup2 );
		}
	}
}