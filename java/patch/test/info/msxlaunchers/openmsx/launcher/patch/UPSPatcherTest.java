package info.msxlaunchers.openmsx.launcher.patch;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class UPSPatcherTest
{
	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test( expected = PatchException.class )
	public void givenNonExistentUPSPatch_whenPatch_thenThrowException() throws IOException, PatchException
	{
		UPSPatcher patcher = new UPSPatcher();

		try
		{
			patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), Paths.get( tmpFolder.getRoot().getAbsolutePath(), "non-existent-patch-file" ), null, false, null );
		}
		catch( PatchException pe )
		{
			Assert.assertEquals( PatchExceptionIssue.IO, pe.getIssue() );
			throw pe;
		}
	}

	@Test( expected = PatchException.class )
	public void givenUPSPatchTooSmall_whenPatch_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'U', 'P', 'S', '1', 'A', 'B' };
		UPSPatcher patcher = new UPSPatcher();

		try
		{
			patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, null );
		}
		catch( PatchException pe )
		{
			Assert.assertEquals( PatchExceptionIssue.INVALID_PATCH_FILE, pe.getIssue() );
			throw pe;
		}
	}

	@Test( expected = PatchException.class )
	public void givenUPSPatchWithInvalidStartString_whenPatch_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'U', 'P', 'S', '0', '2', '1', '2', '1', '2', '3', '1', '2', '3' };
		UPSPatcher patcher = new UPSPatcher();

		try
		{
			patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, true, null );
		}
		catch( PatchException pe )
		{
			Assert.assertEquals( PatchExceptionIssue.INVALID_PATCH_FILE, pe.getIssue() );
			throw pe;
		}
	}
}
