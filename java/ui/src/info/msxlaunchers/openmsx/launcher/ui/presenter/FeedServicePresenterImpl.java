/*
 * Copyright 2017 Sam Elsharif
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.launcher.data.feed.FeedMessage;
import info.msxlaunchers.openmsx.launcher.feed.FeedService;
import info.msxlaunchers.openmsx.launcher.ui.view.MainView;

/**
 * Implementation of <code>FeedServicePresenter</code>
 * 
 * @since v1.10
 * @author Sam Elsharif
 *
 */
class FeedServicePresenterImpl implements FeedServicePresenter
{
	private final FeedService feedService;
	private final MainView view;

	private NewNewsChecker newNewsChecker;
	private volatile boolean running = false;

	@Inject
	FeedServicePresenterImpl( FeedService feedService, MainView view )
	{
		this.feedService = feedService;
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FeedServicePresenter#startService()
	 */
	@Override
	public void startService()
	{
		if( !running )
		{
			running = true;
			feedService.start();
			newNewsChecker = new NewNewsChecker();
			newNewsChecker.start();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FeedServicePresenter#stopService()
	 */
	@Override
	public void stopService()
	{
		running = false;
		feedService.stop();

		//the following check for null is needed for the case when the service was never started
		//e.g. service was disabled and kept that way in the settings before saving
		if( newNewsChecker != null )
		{
			newNewsChecker.stop();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.FeedServicePresenter#onRequestNewsList()
	 */
	@Override
	public void onRequestNewsList() throws IOException
	{
		List<FeedMessage> feedMessages = feedService.getMessages();

		if( feedMessages.isEmpty() )
		{
			view.showFeedProcessingMessage();
		}
		else
		{
			view.showFeedMessagesList( feedMessages );
		}
	}

	private class NewNewsChecker
	{
		private volatile boolean running = false;

		void start()
		{
			if( !running )
			{
				running = true;
				new Thread( this::periodicChecker ).start();
			}
		}

		void stop()
		{
			running = false;
		}

		void periodicChecker()
		{
			while( running )
			{
				if( feedService.isNewMessagesFound() )
				{
					view.indicateNewFeedMessages( true );
				}

				try
				{
					Thread.sleep( 5000l );
				}
				catch( InterruptedException ie )
				{
					//ignore
				}
			}
		}
	}
}
