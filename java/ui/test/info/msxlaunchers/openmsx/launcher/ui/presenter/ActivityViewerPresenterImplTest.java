package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.log.analyser.LogAnalyser;
import info.msxlaunchers.openmsx.launcher.ui.view.ActivityViewerView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class ActivityViewerPresenterImplTest
{
	@Mock ActivityViewerView view;
	@Mock LogAnalyser analyser;

	@Test( expected = NullPointerException.class )
	public void givenNullViewer_whenConstruct_throwNullPointerException() throws IOException
	{
		new ActivityViewerPresenterImpl( null, analyser );
	}

	@Test( expected = NullPointerException.class )
	public void givenNullAnalyser_whenConstruct_throwNullPointerException() throws IOException
	{
		new ActivityViewerPresenterImpl( view, null );
	}

	@Test
	public void whenOnRequestActivityViewerScreen_thenViewerIsCalled()
	{
		ActivityViewerPresenterImpl presenter = new ActivityViewerPresenterImpl( view, analyser );

		Map<String,List<String[]>> logData = Collections.emptyMap();

		when( analyser.getProcessedData() ).thenReturn( logData );

		presenter.onRequestActivityViewerScreen( Language.CHINESE_TRADITIONAL, true );

		verify( view, times( 1 ) ).displayActivityViewerScreen( logData, Language.CHINESE_TRADITIONAL, true );
	}
}