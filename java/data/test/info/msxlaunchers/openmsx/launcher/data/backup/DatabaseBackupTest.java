package info.msxlaunchers.openmsx.launcher.data.backup;

import java.sql.Timestamp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DatabaseBackupTest
{
	private String database = "database";
	private Timestamp timestamp = new Timestamp( 1234567 );

	@Test
	public void testDatabaseBackup()
	{
		DatabaseBackup databaseBackup = new DatabaseBackup( database, timestamp );

		assertEquals( database, databaseBackup.getDatabase() );
		assertEquals( timestamp, databaseBackup.getTimestamp() );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null()
	{
		new DatabaseBackup( null, timestamp );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null()
	{
		new DatabaseBackup( database, null );
	}

	@Test
	public void testEqualityAndHashcode()
	{
		String database1 = "database1";
		String database2 = "database2";
		Timestamp timestamp1 = new Timestamp( 23456 );
		Timestamp timestamp2 = new Timestamp( 45678 );

		DatabaseBackup databaseBackup1a = new DatabaseBackup( database1, timestamp1 );
		DatabaseBackup databaseBackup1b = new DatabaseBackup( database1, timestamp1 );
		DatabaseBackup databaseBackup2 = new DatabaseBackup( database1, timestamp2 );
		DatabaseBackup databaseBackup3 = new DatabaseBackup( database2, timestamp1 );

		assertEquals( databaseBackup1a, databaseBackup1a );
		assertEquals( databaseBackup1a, databaseBackup1b );
		assertNotEquals( databaseBackup1a, databaseBackup2 );
		assertNotEquals( databaseBackup1b, databaseBackup2 );
		assertNotEquals( databaseBackup2, databaseBackup3 );
		assertNotEquals( databaseBackup1a, null );
		assertNotEquals( databaseBackup1a, "string" );

		assertEquals( databaseBackup1a.hashCode(), databaseBackup1b.hashCode() );
		assertNotEquals( databaseBackup1a.hashCode(), databaseBackup2.hashCode() );
		assertNotEquals( databaseBackup1b.hashCode(), databaseBackup2.hashCode() );
		assertNotEquals( databaseBackup2.hashCode(), databaseBackup3.hashCode() );
	}
}