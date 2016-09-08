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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class that holds processed data per event log processor
 * 
 * @since v1.8
 * @author Sam Elsharif
 */
public class LogProcessItem
{
	private final String key;
	private final List<String[]> items;

	public LogProcessItem( String key, List<String[]> items )
	{
		this.key = Objects.requireNonNull( key );
		this.items = Objects.requireNonNull( items );
	}

	/**
	 * @return Key to the processed data to be used in the final map returned to the caller
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * @return Unmodifiable list of string arrays holding processed data. Cannot be null
	 */
	public List<String[]> getItems()
	{
		return Collections.unmodifiableList( items );
	}
}
