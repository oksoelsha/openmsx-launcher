package info.msxlaunchers.openmsx.launcher.ui.presenter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DatabaseInfoTest
{
	@Test
	public void testGettersPositives()
	{
		DatabaseInfo databaseInfo = new DatabaseInfo( 1, 2, 3 );

		assertEquals( 1, databaseInfo.getTotalDatabases() );
		assertEquals( 2, databaseInfo.getTotalGames() );
		assertEquals( 3, databaseInfo.getTotalBackups() );
	}

	@Test
	public void testGettersZeros()
	{
		DatabaseInfo databaseInfo = new DatabaseInfo( 0, 0, 0 );

		assertEquals( 0, databaseInfo.getTotalDatabases() );
		assertEquals( 0, databaseInfo.getTotalGames() );
		assertEquals( 0, databaseInfo.getTotalBackups() );
	}

	@Test
	public void testGettersNegatives()
	{
		DatabaseInfo databaseInfo = new DatabaseInfo( -1, -2, -3 );

		assertEquals( -1, databaseInfo.getTotalDatabases() );
		assertEquals( -2, databaseInfo.getTotalGames() );
		assertEquals( -3, databaseInfo.getTotalBackups() );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		DatabaseInfo databaseInfo1a = new DatabaseInfo( 2, 4, 6 );
		DatabaseInfo databaseInfo1b = new DatabaseInfo( 2, 4, 6 );
		DatabaseInfo databaseInfo2 = new DatabaseInfo( 2, 4, 7 );
		DatabaseInfo databaseInfo3 = new DatabaseInfo( 2, 5, 6 );
		DatabaseInfo databaseInfo4 = new DatabaseInfo( 3, 5, 6 );

		assertEquals( databaseInfo1a, databaseInfo1a );
		assertEquals( databaseInfo1a, databaseInfo1b );
		assertNotEquals( databaseInfo1a, databaseInfo2 );
		assertNotEquals( databaseInfo1b, databaseInfo3 );
		assertNotEquals( databaseInfo1b, databaseInfo4 );
		assertNotEquals( databaseInfo2, databaseInfo3 );
		assertNotEquals( databaseInfo1a, null );
		assertNotEquals( databaseInfo1a, "string" );

		assertEquals( databaseInfo1a.hashCode(), databaseInfo1b.hashCode() );
		assertNotEquals( databaseInfo1a.hashCode(), databaseInfo2.hashCode() );
		assertNotEquals( databaseInfo1b.hashCode(), databaseInfo2.hashCode() );
		assertNotEquals( databaseInfo2.hashCode(), databaseInfo3.hashCode() );
		assertNotEquals( databaseInfo3.hashCode(), databaseInfo4.hashCode() );
	}
}
