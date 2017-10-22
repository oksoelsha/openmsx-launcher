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

/**
 * Feed message persister interface
 * 
 * @since v1.10
 * @author Sam Elsharif
 *
 */
public interface FeedMessagePersister
{
	/**
	 * Saves the given feed message
	 * 
	 * @param message Message to save
	 * @throws IOException
	 */
	void saveMessage( String message ) throws IOException;

	/**
	 * Returns the old persisted feed message. There an exception is thrown while retrieving the message, return empty string
	 * 
	 * @return Persisted feed message
	 * @throws IOException
	 */
	String getMessage() throws IOException;

	/**
	 * Returns whether a previous feed message was persisted
	 * 
	 * @return True if a previous feed message was persisted, false otherwise
	 */
	boolean isMessagePersisted();
}