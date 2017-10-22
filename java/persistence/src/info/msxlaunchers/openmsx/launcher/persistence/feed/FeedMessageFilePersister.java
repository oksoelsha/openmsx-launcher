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
package info.msxlaunchers.openmsx.launcher.persistence.feed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;

/**
 * Implementation of the <code>FeedMessagePersister</code> interface that persists a feed message on and retrieves it
 * from the local hard disk
 * 
 * @since v1.10
 * @author Sam Elsharif
 *
 */
final class FeedMessageFilePersister implements FeedMessagePersister
{
	private final String FEED_DIRECTORY = "feed";
	private final String PREVIOUS_TOP_MESSAGE_FILENAME = "previousTopMessage";

	private final Path persistedTopMessageFile;

	@Inject
	FeedMessageFilePersister( @Named("UserDataDirectory") String userDataDirectory ) throws IOException
	{
		Path feedDirectory = Paths.get( userDataDirectory, FEED_DIRECTORY );

		if( Files.notExists( feedDirectory ) )
		{
			try
			{
				Files.createDirectory( feedDirectory );
			}
			catch( IOException ioe )
			{
				LauncherLogger.logException( this, ioe );
				throw ioe;
			}
		}

		persistedTopMessageFile = Paths.get( feedDirectory.toString(), PREVIOUS_TOP_MESSAGE_FILENAME );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.feed.FeedMessagePersister#saveMessage(java.lang.String)
	 */
	@Override
	public void saveMessage( String message ) throws IOException
	{
		try
		{
			Files.write( persistedTopMessageFile, message.getBytes() );
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );
			throw ioe;
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.feed.FeedMessagePersister#getMessage()
	 */
	@Override
	public String getMessage() throws IOException
	{
		try
		{
			return new String( Files.readAllBytes( persistedTopMessageFile ) );
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );
			throw ioe;
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.feed.FeedMessagePersister#isMessagePersisted()
	 */
	@Override
	public boolean isMessagePersisted()
	{
		return Files.exists( persistedTopMessageFile );
	}
}
