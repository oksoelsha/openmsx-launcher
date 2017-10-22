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
import java.util.List;

import info.msxlaunchers.openmsx.launcher.data.feed.FeedMessage;

/**
 * Feed Service interface
 * 
 * @since v1.10
 * @author Sam Elsharif
 *
 */
public interface FeedService
{
	/**
	 * Starts the service
	 */
	void start();

	/**
	 * Stops the service
	 */
	void stop();

	/**
	 * Returns whether a new message was received from the feed
	 * 
	 * @return True if a new message was received, false otherwise
	 */
	boolean isNewMessagesFound();

	/**
	 * Returns the list of last few messages from all the feeds
	 * 
	 * @return Unmodifiable list of messages from all feeds ordered by time started from most recent
	 * @throws IOException
	 */
	List<FeedMessage> getMessages() throws IOException;
}
