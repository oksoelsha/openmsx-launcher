/*
 * Copyright 2016 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.log.analyser;

import java.util.List;
import java.util.Map;

/**
 * Interface for processing log data one line at a time and for returning the processed data
 * 
 * @since v1.8
 * @author Sam Elsharif
 */
public interface LogProcessor
{
	/**
	 * Process the date and message data from the log line by passing it to all registered implementations
	 * for the the event in the log line
	 * 
	 * @param date Date as it appears in the log line
	 * @param message Part of the log line that comes after the event
	 */
	void processMessage( String date, String message );

	/**
	 * Return a map of string arrays that represent the processed log data
	 * 
	 * @return Unmodifiable map of processed data where key is a unique identifier of the processed data
	 * (as defined by its implementation), and the value is a list of string arrays. The caller should know
	 * the key and the kind of data returned. If no data was found, then an empty map will be returned
	 */
	Map<String,List<String[]>> getProcessedData();
}
