package info.msxlaunchers.openmsx.launcher.ui.presenter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DatabaseAndBackupsTest
{
	@Test
	public void testGetters()
	{
		DatabaseAndBackups databaseAneBackups1 = new DatabaseAndBackups( "name1", 23, 0 );
		DatabaseAndBackups databaseAneBackups2 = new DatabaseAndBackups( "name2", 68, 3 );

		assertEquals( "name1", databaseAneBackups1.getName() );
		assertEquals( 23, databaseAneBackups1.getTotalGames() );
		assertEquals( 0, databaseAneBackups1.getTotalBackups() );
		assertEquals( "name2", databaseAneBackups2.getName() );
		assertEquals( 68, databaseAneBackups2.getTotalGames() );
		assertEquals( 3, databaseAneBackups2.getTotalBackups() );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		DatabaseAndBackups databaseAndBackups1a = new DatabaseAndBackups( "name1", 74, 6 );
		DatabaseAndBackups databaseAndBackups1b = new DatabaseAndBackups( "name1", 74, 6 );
		DatabaseAndBackups databaseAndBackups2 = new DatabaseAndBackups( "name2", 74, 6 );
		DatabaseAndBackups databaseAndBackups3 = new DatabaseAndBackups( "name1", 51, 3 );
		DatabaseAndBackups databaseAndBackups4 = new DatabaseAndBackups( "name1", 74, 3 );

		assertEquals( databaseAndBackups1a, databaseAndBackups1a );
		assertEquals( databaseAndBackups1a, databaseAndBackups1b );
		assertNotEquals( databaseAndBackups1a, databaseAndBackups2 );
		assertNotEquals( databaseAndBackups1b, databaseAndBackups3 );
		assertNotEquals( databaseAndBackups2, databaseAndBackups3 );
		assertNotEquals( databaseAndBackups1a, databaseAndBackups4 );
		assertNotEquals( databaseAndBackups1a, null );
		assertNotEquals( databaseAndBackups1a, "string" );

		assertEquals( databaseAndBackups1a.hashCode(), databaseAndBackups1b.hashCode() );
		assertNotEquals( databaseAndBackups1a.hashCode(), databaseAndBackups2.hashCode() );
		assertNotEquals( databaseAndBackups1b.hashCode(), databaseAndBackups2.hashCode() );
		assertNotEquals( databaseAndBackups2.hashCode(), databaseAndBackups3.hashCode() );
	}
}
