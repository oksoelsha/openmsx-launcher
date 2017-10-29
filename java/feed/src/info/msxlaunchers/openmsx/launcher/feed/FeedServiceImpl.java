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
package info.msxlaunchers.openmsx.launcher.feed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.launcher.data.feed.FeedMessage;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.feed.FeedMessagePersister;

/**
 * Implementation of the <code>FeedService</code> interface
 * 
 * @since v1.10
 * @author Sam Elsharif
 *
 */
final class FeedServiceImpl implements FeedService
{
	private final String sites[][] = {
			{"https://www.msx.org/", "feed/news/", "MSX Resource Center"},
			{"https://www.msxblog.es/", "feed/", "MSX Blog"},
			{"http://www.icongames.com.br/msxfiles/blog-en/", "feed/", "The MSX Files v2"},
			{"http://www.lavandeira.net/", "feed/", "Javi Lavandeira"},
	};

	private final int MAX_MESSAGES_LIST_SIZE = 15;
	private final long CHECK_INTERVAL = 1000l; //one second
	private final long TOTAL_RSS_CHECK_PERIOD = 10*60*1000; //ten minutes
	private final long TOTAL_INTERVAL_CHECKS_PER_RSS_CHECK_PERIOD = TOTAL_RSS_CHECK_PERIOD / CHECK_INTERVAL;

	private Thread service;
	private final FeedReader feedReader;
	private final FeedMessagePersister feedMessagePersister;

	private volatile boolean running;
	private volatile boolean isNewMessages;
	private String previousTopMessage;
	private List<FeedMessage> messages;

	@Inject
	FeedServiceImpl( FeedReader feedReader, FeedMessagePersister feedMessagePersister )
	{
		this.feedReader = feedReader;
		this.feedMessagePersister = feedMessagePersister;

		messages = Collections.emptyList();
		previousTopMessage = null;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.feed.FeedService#start()
	 */
	@Override
	public void start()
	{
		if( !running )
		{
			running = true;
			service = new Thread( this::runService );
			service.start();
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.feed.FeedService#stop()
	 */
	@Override
	public void stop()
	{
		running = false;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.feed.FeedService#isNewMessagesFound()
	 */
	@Override
	public boolean isNewMessagesFound()
	{
		return isNewMessages;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.feed.FeedService#getMessages()
	 */
	@Override
	public List<FeedMessage> getMessages() throws IOException
	{
		if( isNewMessages )
		{
			isNewMessages = false;
			String firstMessage = messages.get( 0 ).toString();
			feedMessagePersister.saveMessage( firstMessage );
			previousTopMessage = firstMessage;
		}

		return Collections.unmodifiableList( messages );
	}

	private void runService()
	{
		int checksCounter = 0;
		while( running )
		{
			try
			{
				if( checksCounter == 0 )
				{
					readFeeds();
					checksCounter++;
				}
				else
				{
					if( checksCounter == TOTAL_INTERVAL_CHECKS_PER_RSS_CHECK_PERIOD )
					{
						checksCounter = 0;
					}
					else
					{
						checksCounter++;
					}

					Thread.sleep( CHECK_INTERVAL );
				}
			}
			catch( InterruptedException ie )
			{
				//nothing to do here
			}
			catch( IOException ioe )
			{
				LauncherLogger.logException( this, ioe );
			}
		}
	}

	private void readFeeds() throws IOException
	{
		List<FeedMessage> messagesFromAllSites = new ArrayList<>( 100 );

		for( int row = 0; row < sites.length; row++ )
		{
    		messagesFromAllSites.addAll( feedReader.read( sites[row][0] + sites[row][1], sites[row][2], sites[row][0] ) );
    	}

		//sort them and get the top maximum size (if available)
		Collections.sort( messagesFromAllSites, (m1, m2) -> m2.getPubDate().compareTo( m1.getPubDate() ) );
		messages = messagesFromAllSites.subList( 0,
				messagesFromAllSites.size() > MAX_MESSAGES_LIST_SIZE ? MAX_MESSAGES_LIST_SIZE : messagesFromAllSites.size() );

		//determine if there are new messages
		if( previousTopMessage == null && feedMessagePersister.isMessagePersisted() )
		{
			previousTopMessage = feedMessagePersister.getMessage();
		}

		if( !messages.get( 0 ).toString().equals( previousTopMessage ) )
		{
			isNewMessages = true;
		}
	}
}
