package info.msxlaunchers.openmsx.launcher.ui.presenter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.extractor.ExtractionException;
import info.msxlaunchers.openmsx.launcher.extractor.Extractor;
import info.msxlaunchers.openmsx.launcher.extractor.ExtractorExceptionIssue;
import info.msxlaunchers.openmsx.launcher.ui.view.LHAExtractorView;

@RunWith( MockitoJUnitRunner.class )
public class LHAExtractorPresenterImplTest
{
	@Mock LHAExtractorView view;
	@Mock Extractor extractor;

	@Test
	public void givenLanguageAndOritentaionFlag_whenOnRequestLHAExtractorScreen_thenCallView()
	{
		LHAExtractorPresenterImpl presenter = new LHAExtractorPresenterImpl( view, extractor );

		presenter.onRequestLHAExtractorScreen( Language.DUTCH, true );

		Mockito.verify( view, Mockito.times( 1 ) ).displayScreen( Language.DUTCH, true );
	}

	@Test
	public void givenInputs_whenOnRequestLHAExtractAction_thenCallExtractor() throws LauncherException, ExtractionException
	{
		LHAExtractorPresenterImpl presenter = new LHAExtractorPresenterImpl( view, extractor );

		presenter.onRequestLHAExtractAction( "compressedFile", "targetDirectory", true );

		Mockito.verify( extractor, Mockito.times( 1 ) ).extract( Mockito.eq( "compressedFile" ), Mockito.eq( "targetDirectory" ), Mockito.eq( true ), Mockito.any() );
	}

	@Test( expected = LauncherException.class )
	public void givenCompressedFileNotExisting_whenOnRequestLHAExtractAction_thenThrowException() throws LauncherException, ExtractionException
	{
		LHAExtractorPresenterImpl presenter = new LHAExtractorPresenterImpl( view, extractor );

		Mockito.when( extractor.extract( Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any() ) ).thenThrow( new ExtractionException( ExtractorExceptionIssue.COMPRESSED_FILE_NOT_FOUND  ) );

		try
		{
			presenter.onRequestLHAExtractAction( "compressedFile", "targetDirectory", true );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( LauncherExceptionCode.ERR_COMPRESSED_FILE_NOT_FOUND, le.getCode() );
			throw le;
		}
	}

	@Test( expected = LauncherException.class )
	public void givenExceptionThrownByExtractor_whenOnRequestLHAExtractAction_thenThrowException() throws LauncherException, ExtractionException
	{
		LHAExtractorPresenterImpl presenter = new LHAExtractorPresenterImpl( view, extractor );

		Mockito.when( extractor.extract( Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean(), Mockito.any() ) ).thenThrow( new ExtractionException( ExtractorExceptionIssue.IO  ) );

		try
		{
			presenter.onRequestLHAExtractAction( "compressedFile", "targetDirectory", true );
		}
		catch( LauncherException le )
		{
			Assert.assertEquals( LauncherExceptionCode.ERR_IO, le.getCode() );
			throw le;
		}
	}
}