package info.msxlaunchers.openmsx.launcher.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;

import com.google.inject.Guice;
import com.google.inject.Injector;

abstract public class DatabaseTest
{
	@ClassRule
	public static TemporaryFolder tmpFolder = new TemporaryFolder();

	static protected String dbLocation;
	static protected String dbURL;
	static protected LauncherPersistence launcherPersistence;

	@BeforeClass
	public static void createDatabaseOnce() throws SQLException, LauncherPersistenceException
	{
		dbLocation = new File( new File( tmpFolder.getRoot().toString(), "databases" ), "launcherdb" ).toString();
		dbURL = "jdbc:derby:" + dbLocation;

		Injector injector = Guice.createInjector(
				new BasicTestModule( tmpFolder.getRoot().toString() ),
	    		new LauncherPersistenceModule()
				);

		launcherPersistence = injector.getInstance( LauncherPersistence.class );
		launcherPersistence.initialize();
	}

	@Before
	public void cleanupDatabase() throws SQLException
	{
		try( Connection connection = DriverManager.getConnection( dbURL ) )
		{
			try( PreparedStatement statement = connection.prepareStatement( "DELETE FROM database" ) )
			{
				statement.executeUpdate();
			}
		}
	}
}
