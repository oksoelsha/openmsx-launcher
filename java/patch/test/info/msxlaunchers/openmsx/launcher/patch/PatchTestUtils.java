package info.msxlaunchers.openmsx.launcher.patch;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.rules.TemporaryFolder;

class PatchTestUtils
{
	static Path createPatchFile( TemporaryFolder tmpFolder, byte[] patchData ) throws IOException
	{
		File tmpFile = tmpFolder.newFile( "patch.ips" );
		PrintWriter writer = new PrintWriter( tmpFile, "UTF-8" );
		writer.print( new String( patchData ) );
		writer.close();

		return Paths.get( tmpFile.getAbsolutePath() );
	}

	static Path createFileToPatch( TemporaryFolder tmpFolder ) throws IOException
	{
		byte[] fileToPatchData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		File tmpFile = tmpFolder.newFile( "file-to-patch.rom" );
		PrintWriter writer = new PrintWriter( tmpFile, "US-ASCII" );
		writer.print( new String( fileToPatchData ) );
		writer.close();

		return Paths.get( tmpFile.getAbsolutePath() );
	}
}
