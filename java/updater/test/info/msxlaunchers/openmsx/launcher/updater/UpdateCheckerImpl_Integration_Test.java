package info.msxlaunchers.openmsx.launcher.updater;

import info.msxlaunchers.openmsx.launcher.updater.LauncherUpdater;
import info.msxlaunchers.openmsx.launcher.updater.UpdateChecker;
import info.msxlaunchers.openmsx.launcher.updater.UpdateCheckerImpl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 * This test requires the MSX Launchers server to be up and responding to version checks and requests
 */
public class UpdateCheckerImpl_Integration_Test
{
	@Mock LauncherUpdater launcherUpdater;

	@Rule
	public final TemporaryFolder jarTmpFilesDirectory = new TemporaryFolder();

	@Rule
	public final TemporaryFolder exeDirectory = new TemporaryFolder();

	@Rule
	public final TemporaryFolder extraDataTmpDirectory = new TemporaryFolder();

	@Test @Ignore
	public void testGetExtraDataVersion() throws IOException
	{
		UpdateCheckerImpl updateChecker = new UpdateCheckerImpl( jarTmpFilesDirectory.newFolder().getAbsolutePath(), exeDirectory.newFolder().getAbsolutePath(),
				extraDataTmpDirectory.newFolder().getAbsolutePath(), launcherUpdater );

		Map<String,String> versions = updateChecker.getVersions();

		assertEquals( "1.5", versions.get( UpdateChecker.KEY_OPENMSX_LAUNCHER ) );
		assertEquals( "1.16", versions.get( UpdateChecker.KEY_EXTRA_DATA ) );
		assertEquals( "1.4", versions.get( UpdateChecker.KEY_SCREENSHOTS ) );

		assertEquals( 3, versions.size() );
	}

	@Test @Ignore
	public void testGetExtraDataFile() throws IOException
	{
		String temporaryFolder = extraDataTmpDirectory.newFolder().getAbsolutePath();

		UpdateCheckerImpl updateChecker = new UpdateCheckerImpl( jarTmpFilesDirectory.newFolder().getAbsolutePath(), exeDirectory.newFolder().getAbsolutePath(),
				temporaryFolder, launcherUpdater );

		updateChecker.getNewExtraDataFile();

		assertTrue( new File( temporaryFolder, "extra-data.dat" ).exists() );
	}
}
