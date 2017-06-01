package info.msxlaunchers.openmsx.launcher.patch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class IPSPatcherTest
{
	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test( expected = PatchException.class )
	public void givenNonExistentFileToPatch_whenPatch_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 7, 0, 3, 1, 2, 3, 'E', 'O', 'F' };
		IPSPatcher patcher = new IPSPatcher();

		try
		{
			patcher.patch( Paths.get( tmpFolder.getRoot().getAbsolutePath(), "non-existent-file-to-patch" ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, null );
		}
		catch( PatchException pe )
		{
			Assert.assertEquals( PatchExceptionIssue.IO, pe.getIssue() );
			throw pe;
		}
	}

	@Test( expected = PatchException.class )
	public void givenNonExistentIPSPatch_whenPatch_thenThrowException() throws IOException, PatchException
	{
		IPSPatcher patcher = new IPSPatcher();

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
	public void givenIPSPatchTooSmall_whenPatch_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 'A', 'B' };
		IPSPatcher patcher = new IPSPatcher();

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
	public void givenIPSPatchWithInvalidStartString_whenPatch_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'G', 'X', 'Y', 'Z', '0' };
		IPSPatcher patcher = new IPSPatcher();

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

	@Test
	public void givenIPSPatch_whenPatchFileDirectly_thenOriginalFileIsPatched() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 0, 0, 6, 0, 0, 0, 2, 3, 'E', 'O', 'F' };
		IPSPatcher patcher = new IPSPatcher();

		patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, null );

		byte[] expectedPatchedFileData = new byte[] { 1, 2, 7, 8, 9, 6, 3, 3, 9 };
		byte[] actualPatchedFileData = Files.readAllBytes( Paths.get( tmpFolder.getRoot().toString(), "file-to-patch.rom" ) );

		Assert.assertArrayEquals( expectedPatchedFileData, actualPatchedFileData );
	}

	@Test
	public void givenIPSPatchAndTagetFile_whenPatch_thenTargetFileIsCreated() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 0, 0, 6, 0, 0, 0, 2, 3, 'E', 'O', 'F' };
		Path targetFile = Paths.get( tmpFolder.getRoot().toString(), "target-file.rom" );
		IPSPatcher patcher = new IPSPatcher();

		patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), targetFile, false, null );

		byte[] expectedPatchedFileData = new byte[] { 1, 2, 7, 8, 9, 6, 3, 3, 9 };
		byte[] actualPatchedFileData = Files.readAllBytes( targetFile );

		Assert.assertArrayEquals( expectedPatchedFileData, actualPatchedFileData );
	}

	@Test( expected = PatchException.class )
	public void givenIPSPatchAndInvalidTagetFilePath_whenPatch_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 0, 0, 6, 0, 0, 0, 2, 3, 'E', 'O', 'F' };
		Path targetFile = Paths.get( tmpFolder.getRoot().toString(), "non-existent-folder", "target-file.rom" );
		IPSPatcher patcher = new IPSPatcher();

		try
		{
			patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), targetFile, false, null );
		}
		catch( PatchException pe )
		{
			Assert.assertEquals( PatchExceptionIssue.TARGET_FILE_CANNOT_WRITE, pe.getIssue() );
			throw pe;
		}
	}

	@Test
	public void givenIPSPatchWithValidTruncateOption_whenPatch_thenPatchedFileIsTruncated() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 'E', 'O', 'F', 0, 0, 6 };
		IPSPatcher patcher = new IPSPatcher();

		patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, null );

		byte[] expectedPatchedFileData = new byte[] { 1, 2, 7, 8, 9, 6 };
		byte[] actualPatchedFileData = Files.readAllBytes( Paths.get( tmpFolder.getRoot().toString(), "file-to-patch.rom" ) );

		Assert.assertArrayEquals( expectedPatchedFileData, actualPatchedFileData );
	}

	@Test
	public void givenIPSPatchWithTruncateValueLargerThanFileToPatch_whenPatch_thenPatchedFileIsNotTruncated() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 'E', 'O', 'F', 0, 0, 9 };
		IPSPatcher patcher = new IPSPatcher();

		patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, null );

		byte[] expectedPatchedFileData = new byte[] { 1, 2, 7, 8, 9, 6, 7, 8, 9 };
		byte[] actualPatchedFileData = Files.readAllBytes( Paths.get( tmpFolder.getRoot().toString(), "file-to-patch.rom" ) );

		Assert.assertArrayEquals( expectedPatchedFileData, actualPatchedFileData );
	}

	@Test
	public void givenIPSPatchWithSimpleRecordFormatAndMoreDataThanFileToPatch_whenPatch_thenPatchedFileIsBigger() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 7, 0, 3, 1, 2, 3, 'E', 'O', 'F' };
		IPSPatcher patcher = new IPSPatcher();

		patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, null );

		byte[] expectedPatchedFileData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 1, 2, 3 };
		byte[] actualPatchedFileData = Files.readAllBytes( Paths.get( tmpFolder.getRoot().toString(), "file-to-patch.rom" ) );

		Assert.assertArrayEquals( expectedPatchedFileData, actualPatchedFileData );
	}

	@Test
	public void givenIPSPatchWithRLERecordFormatAndMoreDataThanFileToPatch_whenPatch_thenPatchedFileIsBigger() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 7, 0, 0, 0, 3, 2, 'E', 'O', 'F' };
		IPSPatcher patcher = new IPSPatcher();

		patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, null );

		byte[] expectedPatchedFileData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 2, 2, 2 };
		byte[] actualPatchedFileData = Files.readAllBytes( Paths.get( tmpFolder.getRoot().toString(), "file-to-patch.rom" ) );

		Assert.assertArrayEquals( expectedPatchedFileData, actualPatchedFileData );
	}

	@Test( expected = PatchException.class )
	public void givenIPSPatchWithMissingEOF_whenPatch_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 7, 0, 0, 0, 3, 2, 'E', 'O' };
		IPSPatcher patcher = new IPSPatcher();

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
	public void givenIPSPatchWithInvalidDataSizeValue_whenPatch_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 1 };
		IPSPatcher patcher = new IPSPatcher();

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

	@Test
	public void givenIPSPatch_whenSha1ChecksumMatches_thenFileIsPatched() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 0, 0, 6, 0, 0, 0, 2, 3, 'E', 'O', 'F' };
		IPSPatcher patcher = new IPSPatcher();

		patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, "b6c511873b07a73513161b142d344b7b845cacef" );

		byte[] expectedPatchedFileData = new byte[] { 1, 2, 7, 8, 9, 6, 3, 3, 9 };
		byte[] actualPatchedFileData = Files.readAllBytes( Paths.get( tmpFolder.getRoot().toString(), "file-to-patch.rom" ) );

		Assert.assertArrayEquals( expectedPatchedFileData, actualPatchedFileData );
	}

	@Test
	public void givenIPSPatch_whenMD5ChecksumMatches_thenFileIsPatched() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 0, 0, 6, 0, 0, 0, 2, 3, 'E', 'O', 'F' };
		IPSPatcher patcher = new IPSPatcher();

		patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, "8596c1af55b14b7b320112944fcb8536" );

		byte[] expectedPatchedFileData = new byte[] { 1, 2, 7, 8, 9, 6, 3, 3, 9 };
		byte[] actualPatchedFileData = Files.readAllBytes( Paths.get( tmpFolder.getRoot().toString(), "file-to-patch.rom" ) );

		Assert.assertArrayEquals( expectedPatchedFileData, actualPatchedFileData );
	}

	@Test( expected = PatchException.class )
	public void givenIPSPatch_whenChecksumNotMatch_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 0, 0, 6, 0, 0, 0, 2, 3, 'E', 'O', 'F' };
		IPSPatcher patcher = new IPSPatcher();

		try
		{
			patcher.patch( PatchTestUtils.createFileToPatch( tmpFolder ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, "12345" );
		}
		catch( PatchException pe )
		{
			Assert.assertEquals( PatchExceptionIssue.SOURCE_FILE_CHECKSUM_NOT_MATCH, pe.getIssue() );
			throw pe;
		}
	}

	@Test
	public void givenIPSPatchAndZippedSourceAndTargetFile_whenPatch_thenUnzippedSourceIsPatched() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 0, 0, 6, 0, 0, 0, 2, 3, 'E', 'O', 'F' };
		Path targetFile = Paths.get( tmpFolder.getRoot().toString(), "target-file.rom" );
		IPSPatcher patcher = new IPSPatcher();

		patcher.patch( zipFile( PatchTestUtils.createFileToPatch( tmpFolder ) ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), targetFile, false, null );

		byte[] expectedPatchedFileData = new byte[] { 1, 2, 7, 8, 9, 6, 3, 3, 9 };
		byte[] actualPatchedFileData = Files.readAllBytes( targetFile );

		Assert.assertArrayEquals( expectedPatchedFileData, actualPatchedFileData );
	}

	@Test( expected = PatchException.class )
	public void givenIPSPatchAndZippedSource_whenPatchFileDirectly_thenThrowException() throws IOException, PatchException
	{
		byte[] patchData = new byte[] { 'P', 'A', 'T', 'C', 'H', 0, 0, 2, 0, 3, 7, 8, 9, 0, 0, 6, 0, 0, 0, 2, 3, 'E', 'O', 'F' };
		IPSPatcher patcher = new IPSPatcher();

		try
		{
			patcher.patch( zipFile( PatchTestUtils.createFileToPatch( tmpFolder ) ), PatchTestUtils.createPatchFile( tmpFolder, patchData ), null, false, null );
		}
		catch( PatchException pe )
		{
			Assert.assertEquals( PatchExceptionIssue.ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY, pe.getIssue() );
			throw pe;
		}
	}

	private Path zipFile( Path file ) throws IOException
	{
		File tmpZipFile = tmpFolder.newFile( "zip-file.zip" );

		try( FileOutputStream fos = new FileOutputStream( tmpZipFile );
				FileInputStream fis = new FileInputStream( file.toFile() );
				ZipOutputStream zipOut = new ZipOutputStream( fos ) )
		{
			ZipEntry zipEntry = new ZipEntry( file.toString() );
			zipOut.putNextEntry( zipEntry );
	
			final byte[] bytes = new byte[1024];
			int length;
			while( (length = fis.read(bytes)) >= 0 ) {
				zipOut.write( bytes, 0, length );
			}
		}

        return Paths.get( tmpZipFile.toString() );
	}
}
