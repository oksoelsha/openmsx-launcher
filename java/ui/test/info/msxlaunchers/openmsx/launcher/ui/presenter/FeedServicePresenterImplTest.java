package info.msxlaunchers.openmsx.launcher.ui.presenter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import info.msxlaunchers.openmsx.launcher.data.feed.FeedMessage;
import info.msxlaunchers.openmsx.launcher.feed.FeedService;
import info.msxlaunchers.openmsx.launcher.ui.view.MainView;

@RunWith( MockitoJUnitRunner.class )
public class FeedServicePresenterImplTest
{
	@Mock FeedService feedService;
	@Mock MainView view;

	@Test
	public void startService_callTwice_starServiceOnceOnly()
	{
		FeedServicePresenterImpl presenter = new FeedServicePresenterImpl( feedService, view );

		presenter.startService();
		verify( feedService, times( 1 ) ).start();

		presenter.startService();
		verify( feedService, times( 1 ) ).start();
	}

	@Test
	public void startService_callTwiceWithStopInBetween_starServiceAgain()
	{
		FeedServicePresenterImpl presenter = new FeedServicePresenterImpl( feedService, view );

		presenter.startService();
		verify( feedService, times( 1 ) ).start();

		presenter.stopService();
		verify( feedService, times( 1 ) ).stop();

		presenter.startService();
		verify( feedService, times( 2 ) ).start();
	}

	@Test
	public void onRequestNewsList_feedServiceReturnsEmptyMessages_callViewToShowWaitMessage() throws IOException
	{
		List<FeedMessage> messages = Collections.emptyList();
		when( feedService.getMessages() ).thenReturn( messages );
		FeedServicePresenterImpl presenter = new FeedServicePresenterImpl( feedService, view );

		presenter.onRequestNewsList();
		verify( view, times( 1 ) ).showFeedProcessingMessage();
		verify( view, never() ).showFeedMessagesList( messages );
	}

	@Test
	public void onRequestNewsList_feedServiceReturnsMessages_callViewToShowMessages() throws IOException
	{
		List<FeedMessage> messages = Arrays.asList( new FeedMessage("t", "l", "Thu, 19 Oct 2017 20:06:48 +0000", "fsn", "fsu" ) );
		when( feedService.getMessages() ).thenReturn( messages );
		FeedServicePresenterImpl presenter = new FeedServicePresenterImpl( feedService, view );

		presenter.onRequestNewsList();
		verify( view, times( 1 ) ).showFeedMessagesList( messages );
		verify( view, never() ).showFeedProcessingMessage();
	}
}
