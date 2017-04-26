package info.msxlaunchers.openmsx.launcher.ui.presenter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Assert;
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
import info.msxlaunchers.openmsx.launcher.patch.Patcher;
import info.msxlaunchers.openmsx.launcher.ui.view.PatcherView;

@RunWith( MockitoJUnitRunner.class )
public class IPSPatcherPresenterTest
{
	@Mock PatcherView view;
	@Mock Patcher patcher;

	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@Test
	public void givenLanguageAndOrientation_whenOnRequestIPSPatcherScreen_throwCallView()
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		presenter.onRequestIPSPatcherScreen( Language.POLISH, true );

		verify( view, times( 1 ) ).displayScreen( Language.POLISH, true );
	}

	@Test( expected = LauncherException.class )
	public void givenNonExistentPatchFile_whenOnRequestPatchFileAction_thenThrowException() throws LauncherException, IOException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		try
		{
			presenter.onRequestPatchFileAction( "non-existent-patch-file", tmpFolder.newFile().toString(), false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenEmptyPatchFile_whenOnRequestPatchFileAction_thenThrowException() throws LauncherException, IOException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		try
		{
			presenter.onRequestPatchFileAction( "", tmpFolder.newFile().toString(), false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenNonExistentFileToPatch_whenOnRequestPatchFileAction_thenThrowException() throws LauncherException, IOException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		try
		{
			presenter.onRequestPatchFileAction( tmpFolder.newFile().toString(), "non-existent-file-to-patch", false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenNonEmptyFileToPatch_whenOnRequestPatchFileAction_thenThrowException() throws LauncherException, IOException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		try
		{
			presenter.onRequestPatchFileAction( tmpFolder.newFile().toString(), "", false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE );
			throw le;
		}
	}

	@Test
	public void givenPatchFileAndFileToPatch_whenOnRequestPatchFileAction_thenPatchAndReturnTrue() throws LauncherException, IOException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		boolean returnValue = presenter.onRequestPatchFileAction( patchFile, fileToPatch, false, null, false, null );

		verify( patcher, times( 1 ) ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, null );
		Assert.assertTrue( returnValue );
	}

	@Test
	public void givenPatchFileAndFileToPatchAndNonExistentTargetFile_whenOnRequestPatchFileAction_thenPatchAndReturnTrue() throws LauncherException, IOException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = new File( tmpFolder.getRoot(), "tmpFile" ).toString();

		boolean returnValue = presenter.onRequestPatchFileAction( patchFile, fileToPatch, true, targetFile, false, null );

		verify( patcher, times( 1 ) ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), Paths.get( targetFile ), null );
		Assert.assertTrue( returnValue );
	}

	@Test
	public void givenPatchFileAndFileToPatchAndExistentingTargetFileWithConfirmReplace_whenOnRequestPatchFileAction_thenPatchAndReturnTrue() throws LauncherException, IOException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = tmpFolder.newFile().toString();

		when( view.confirmTargetFileReplacement() ).thenReturn( true );
		boolean returnValue = presenter.onRequestPatchFileAction( patchFile, fileToPatch, true, targetFile, false, null );

		verify( patcher, times( 1 ) ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), Paths.get( targetFile ), null );
		Assert.assertTrue( returnValue );
	}

	@Test
	public void givenPatchFileAndFileToPatchAndExistentingTargetFileWithConfirmNotToReplace_whenOnRequestPatchFileAction_thenSkipPatchAndReturnFalse() throws LauncherException, IOException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = tmpFolder.newFile().toString();

		when( view.confirmTargetFileReplacement() ).thenReturn( false );
		boolean returnValue = presenter.onRequestPatchFileAction( patchFile, fileToPatch, true, targetFile, false, null );

		verifyZeroInteractions( patcher );
		Assert.assertFalse( returnValue );
	}

	@Test( expected = LauncherException.class )
	public void givenVerifyChecksumTrueAndNullChecksum_whenOnRequestPatchFileAction_thenThrowException() throws IOException, LauncherException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		try
		{
			presenter.onRequestPatchFileAction( patchFile, fileToPatch, false, null, true, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_EMPTY_CHECKSUM );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenVerifyChecksumTrueAndEmptyChecksum_whenOnRequestPatchFileAction_thenThrowException() throws IOException, LauncherException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		try
		{
			presenter.onRequestPatchFileAction( patchFile, fileToPatch, false, null, true, " " );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_EMPTY_CHECKSUM );
			throw le;
		}
	}

	@Test
	public void givenPatchFileAndFileToPatchAndChecksum_whenOnRequestPatchFileAction_thenPatchAndReturnTrue() throws LauncherException, IOException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		when( view.confirmTargetFileReplacement() ).thenReturn( false );
		boolean returnValue = presenter.onRequestPatchFileAction( patchFile, fileToPatch, false, null, true, "123456" );

		verify( patcher, times( 1 ) ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, "123456" );
		Assert.assertTrue( returnValue );
	}

	@Test( expected = LauncherException.class )
	public void givenUnpatchableFile_whenOnRequestPatchFileAction_thenThrowException() throws IOException, LauncherException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.FILE_TO_PATCH_NOT_PATCHABLE ) ).when( patcher ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, null );

		try
		{
			presenter.onRequestPatchFileAction( patchFile, fileToPatch, false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_FILE_TO_PATCH_NOT_PATCHABLE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenInvalidPatchFile_whenOnRequestPatchFileAction_thenThrowException() throws IOException, LauncherException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.INVALID_PATCH_FILE ) ).when( patcher ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, null );

		try
		{
			presenter.onRequestPatchFileAction( patchFile, fileToPatch, false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_INVALID_PATCH_FILE );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenNonMatchingChecksums_whenOnRequestPatchFileAction_thenThrowException() throws IOException, LauncherException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.SOURCE_FILE_CHECKSUM_NOT_MATCH ) ).when( patcher ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, "1234" );

		try
		{
			presenter.onRequestPatchFileAction( patchFile, fileToPatch, false, null, true, "1234" );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_SOURCE_FILE_CHECKSUM_NOT_MATCH );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenZippedSourceAndPatchDirectly_whenOnRequestPatchFileAction_thenThrowException() throws IOException, LauncherException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY ) ).when( patcher ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), null, null );

		try
		{
			presenter.onRequestPatchFileAction( patchFile, fileToPatch, false, null, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenTargetFileCannotBeWritten_whenOnRequestPatchFileAction_thenThrowException() throws IOException, LauncherException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = new File( tmpFolder.getRoot(), "tmpFile" ).toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.TARGET_FILE_CANNOT_WRITE ) ).when( patcher ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), Paths.get( targetFile ), null );

		try
		{
			presenter.onRequestPatchFileAction( patchFile, fileToPatch, true, targetFile, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_IO );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenIOProblem_whenOnRequestPatchFileAction_thenThrowException() throws IOException, LauncherException, PatchException
	{
		IPSPatcherPresenter presenter = new IPSPatcherPresenter( view, patcher );

		String patchFile = tmpFolder.newFile().toString();
		String fileToPatch = tmpFolder.newFile().toString();
		String targetFile = new File( tmpFolder.getRoot(), "tmpFile" ).toString();

		Mockito.doThrow( new PatchException( PatchExceptionIssue.IO ) ).when( patcher ).patch( Paths.get( fileToPatch ), Paths.get( patchFile ), Paths.get( targetFile ), null );

		try
		{
			presenter.onRequestPatchFileAction( patchFile, fileToPatch, true, targetFile, false, null );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( le.getCode(), LauncherExceptionCode.ERR_IO );
			throw le;
		}
	}
}
