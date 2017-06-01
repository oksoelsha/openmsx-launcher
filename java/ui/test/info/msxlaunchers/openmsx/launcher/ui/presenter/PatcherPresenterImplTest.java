package info.msxlaunchers.openmsx.launcher.ui.presenter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.patch.PatchException;
import info.msxlaunchers.openmsx.launcher.patch.PatchExceptionIssue;
import info.msxlaunchers.openmsx.launcher.patch.PatchMethod;
import info.msxlaunchers.openmsx.launcher.patch.Patcher;
import info.msxlaunchers.openmsx.launcher.patch.PatcherProvider;
import info.msxlaunchers.openmsx.launcher.ui.view.PatcherView;

@RunWith( MockitoJUnitRunner.class )
public class PatcherPresenterImplTest
{
	@Mock PatcherView view;
	@Mock PatcherProvider patcherProvider;
	@Mock Patcher patcher;

	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@Before
	public void setup()
	{
		when( patcherProvider.get( PatchMethod.IPS ) ).thenReturn( patcher );
		when( patcherProvider.get( PatchMethod.UPS ) ).thenReturn( patcher );
	}

	@Test
	public void givenLanguageAndOrientation_whenOnRequestIPSPatcherScreen_throwCallView()
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		presenter.onRequestIPSPatcherScreen( Language.POLISH, true );

		verify( view, times( 1 ) ).displayScreen( Language.POLISH, true );
	}

	@Test( expected = LauncherException.class )
	public void givenNonExistentPatchFile_whenOnRequestPatchFileActionForIPS_thenThrowException() throws LauncherException, IOException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		try
		{
			presenter.onRequestPatchFileActionForIPS( "non-existent-patch-file", tmpFolder.newFile().toString(), false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenEmptyPatchFile_whenOnRequestPatchFileActionForIPS_thenThrowException() throws LauncherException, IOException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		try
		{
			presenter.onRequestPatchFileActionForIPS( "", tmpFolder.newFile().toString(), false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenNonExistentFileToPatch_whenOnRequestPatchFileActionForIPS_thenThrowException() throws LauncherException, IOException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		try
		{
			presenter.onRequestPatchFileActionForIPS( tmpFolder.newFile().toString(), "non-existent-file-to-patch", false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenNonEmptyFileToPatch_whenOnRequestPatchFileActionForIPS_thenThrowException() throws LauncherException, IOException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		try
		{
			presenter.onRequestPatchFileActionForIPS( tmpFolder.newFile().toString(), "", false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE );
			throw le;
		}
	}

	@Test
	public void givenPatchFileAndFileToPatch_whenOnRequestPatchFileActionForIPS_thenPatchAndReturnTrue() throws LauncherException, IOException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		boolean returnValue = presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, false, null, false, null );

		verify( patcher, times( 1 ) ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, false, null );
		Assert.assertTrue( returnValue );
	}

	@Test
	public void givenPatchFileAndFileToPatch_whenOnRequestPatchFileActionForUPS_thenPatchAndReturnTrue() throws LauncherException, IOException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		boolean returnValue = presenter.onRequestPatchFileActionForUPS( patchFile, fileToPatch, false, null, true );

		verify( patcher, times( 1 ) ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, false, null );
		Assert.assertTrue( returnValue );
	}

	@Test
	public void givenPatchFileAndFileToPatchAndNonExistentTargetFile_whenOnRequestPatchFileActionForIPS_thenPatchAndReturnTrue() throws LauncherException, IOException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = new File( tmpFolder.getRoot(), "tmpFile" ).toString();

		boolean returnValue = presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, true, targetFile, false, null );

		verify( patcher, times( 1 ) ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), Paths.get( targetFile ), false, null );
		Assert.assertTrue( returnValue );
	}

	@Test
	public void givenPatchFileAndFileToPatchAndExistentingTargetFileWithConfirmReplace_whenOnRequestPatchFileActionForIPS_thenPatchAndReturnTrue() throws LauncherException, IOException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = tmpFolder.newFile().toString();

		when( view.confirmTargetFileReplacement() ).thenReturn( true );
		boolean returnValue = presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, true, targetFile, false, null );

		verify( patcher, times( 1 ) ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), Paths.get( targetFile ), false, null );
		Assert.assertTrue( returnValue );
	}

	@Test
	public void givenPatchFileAndFileToPatchAndExistentingTargetFileWithConfirmNotToReplace_whenOnRequestPatchFileActionForIPS_thenSkipPatchAndReturnFalse() throws LauncherException, IOException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = tmpFolder.newFile().toString();

		when( view.confirmTargetFileReplacement() ).thenReturn( false );
		boolean returnValue = presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, true, targetFile, false, null );

		verifyZeroInteractions( patcherProvider );
		Assert.assertFalse( returnValue );
	}

	@Test( expected = LauncherException.class )
	public void givenVerifyChecksumTrueAndNullChecksum_whenOnRequestPatchFileActionForIPS_thenThrowException() throws IOException, LauncherException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		try
		{
			presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, false, null, true, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_EMPTY_CHECKSUM );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenVerifyChecksumTrueAndEmptyChecksum_whenOnRequestPatchFileActionForIPS_thenThrowException() throws IOException, LauncherException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		try
		{
			presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, false, null, true, " " );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_EMPTY_CHECKSUM );
			throw le;
		}
	}

	@Test
	public void givenPatchFileAndFileToPatchAndChecksum_whenOnRequestPatchFileActionForIPS_thenPatchAndReturnTrue() throws LauncherException, IOException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		when( view.confirmTargetFileReplacement() ).thenReturn( false );
		boolean returnValue = presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, false, null, true, "123456" );

		verify( patcher, times( 1 ) ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, true, "123456" );
		Assert.assertTrue( returnValue );
	}

	@Test( expected = LauncherException.class )
	public void givenUnpatchableFile_whenOnRequestPatchFileActionForIPS_thenThrowException() throws IOException, LauncherException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.FILE_TO_PATCH_NOT_PATCHABLE ) ).when( patcher )
			.patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, false, null );

		try
		{
			presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_FILE_TO_PATCH_NOT_PATCHABLE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenInvalidPatchFile_whenOnRequestPatchFileActionForIPS_thenThrowException() throws IOException, LauncherException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.INVALID_PATCH_FILE ) ).when( patcher )
			.patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, false, null );

		try
		{
			presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_INVALID_PATCH_FILE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenNonMatchingChecksums_whenOnRequestPatchFileActionForIPS_thenThrowException() throws IOException, LauncherException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.SOURCE_FILE_CHECKSUM_NOT_MATCH ) ).when( patcher )
			.patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, true, "1234" );

		try
		{
			presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, false, null, true, "1234" );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_SOURCE_FILE_CHECKSUM_NOT_MATCH );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenZippedSourceAndPatchDirectly_whenOnRequestPatchFileActionForIPS_thenThrowException() throws IOException, LauncherException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY ) ).when( patcher )
			.patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, false, null );

		try
		{
			presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenTargetFileCannotBeWritten_whenOnRequestPatchFileActionForIPS_thenThrowException() throws IOException, LauncherException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = new File( tmpFolder.getRoot(), "tmpFile" ).toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.TARGET_FILE_CANNOT_WRITE ) ).when( patcher )
			.patch( Paths.get( fileToPatch ), Paths.get( patchFile ), Paths.get( targetFile ), false, null );

		try
		{
			presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, true, targetFile, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_IO );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenIOProblem_whenOnRequestPatchFileActionForIPS_thenThrowException() throws IOException, LauncherException, PatchException
	{
		PatcherPresenterImpl presenter = new PatcherPresenterImpl( view, patcherProvider );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = new File( tmpFolder.getRoot(), "tmpFile" ).toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.IO ) ).when( patcher )
			.patch( Paths.get( fileToPatch ), Paths.get( patchFile ), Paths.get( targetFile ), false, null );

		try
		{
			presenter.onRequestPatchFileActionForIPS( patchFile, fileToPatch, true, targetFile, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_IO );
			throw le;
		}
	}
}
