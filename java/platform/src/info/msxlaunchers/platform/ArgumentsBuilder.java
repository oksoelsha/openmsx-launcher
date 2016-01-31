/*
 * Copyright 2013 Sam Elsharif
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
package info.msxlaunchers.platform;

import java.util.List;

/**
 * Interface to build command line process arguments in an OS
 * 
 * @since v1.1
 * @author Sam Elsharif
 *
 */
public interface ArgumentsBuilder
{
	/**
	 * Appends the given command line switch and value to the list
	 * 
	 * @param argument Command line argument for the process - could start with - or / or whatever the process takes.
	 * Cannot be null
	 */
	void append( String argument );

	/**
	 * Appends the given command line switch and value to the list
	 * 
	 * @param cmdLineSwitch Command line switch for the process - could start with - or / or whatever the process takes.
	 * Cannot be null
	 * @param value Value for the command line switch. If value is null then the switch will not be appended
	 */
	void appendIfValueDefined( String cmdLineSwitch, String value );

	/**
	 * Returns a List of Strings of the arguments. Calling this will return a copy of the internal buffer and the
	 * internal buffer must be cleared
	 * 
	 * @return Unmodifiable List of Strings of the arguments. If no arguments were passed in, an empty List will by returned
	 */
	List<String> getArgumentList();
}
