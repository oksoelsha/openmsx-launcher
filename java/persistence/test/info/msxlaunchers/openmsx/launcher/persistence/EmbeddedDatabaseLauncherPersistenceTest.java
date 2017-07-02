package info.msxlaunchers.openmsx.launcher.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.persistence.favorite.FavoritePersister;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterPersister;
import info.msxlaunchers.openmsx.launcher.persistence.game.GamePersister;
import info.msxlaunchers.openmsx.launcher.persistence.machine.MachineUpdatePersister;
import info.msxlaunchers.openmsx.launcher.persistence.search.GameFinder;
import info.msxlaunchers.openmsx.launcher.persistence.settings.SettingsPersister;

import static org.junit.Assert.assertSame;

@RunWith( MockitoJUnitRunner.class )
public class EmbeddedDatabaseLauncherPersistenceTest
{
	@Mock GamePersister gamePersister;
	@Mock FavoritePersister favoritePersister;
	@Mock FilterPersister filterPersister;
	@Mock SettingsPersister settingsPersister;
	@Mock GameFinder gameFinder;
	@Mock MachineUpdatePersister machineUpdatePersister;

	private String userDataDirectory;
	private String databasesDirectoryName;
	private String databaseFullPath;

	private EmbeddedDatabaseLauncherPersistence launcherPersistence;

	@Before
	public void setup()
	{
		userDataDirectory = "userDataDirectory";
		databasesDirectoryName = "databasesDirectoryName";
		databaseFullPath = "databaseFullPath";

		launcherPersistence = new EmbeddedDatabaseLauncherPersistence( gamePersister, favoritePersister, filterPersister,
				settingsPersister, gameFinder, machineUpdatePersister, userDataDirectory, databasesDirectoryName, databaseFullPath );
	}

	@Test
	public void test_whenGetGamePersister_thenReturnGamePersister()
	{
		assertSame( gamePersister, launcherPersistence.getGamePersister() );
	}

	@Test
	public void test_whenGetFavoritePersister_thenReturnFavoritePersister()
	{
		assertSame( favoritePersister, launcherPersistence.getFavoritePersister() );
	}

	@Test
	public void test_whenGetFiltersPersister_thenReturnFiltersPersister()
	{
		assertSame( filterPersister, launcherPersistence.getFiltersPersister() );
	}

	@Test
	public void test_whenGetSettingsPersister_thenReturnSettingsPersister()
	{
		assertSame( settingsPersister, launcherPersistence.getSettingsPersister() );
	}

	@Test
	public void test_whenGetGameFinder_thenReturnGameFinder()
	{
		assertSame( gameFinder, launcherPersistence.getGameFinder() );
	}

	@Test
	public void test_whenGetMachineUpdatePersister_thenReturnMachineUpdatePersister()
	{
		assertSame( machineUpdatePersister, launcherPersistence.getMachineUpdatePersister() );
	}
}