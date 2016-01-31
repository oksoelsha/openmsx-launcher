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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import info.msxlaunchers.openmsx.common.Utils;

/**
 * Implementation of <code>ArgumentsBuilder</code> for Windows
 * 
 * @since v1.1
 * @author Sam Elsharif
 *
 */
final class WindowsArgumentsBuilder implements ArgumentsBuilder
{
	private List<String> argsList = new ArrayList<String>();

	/* (non-Javadoc)
	 * @see info.msxlaunchers.platform.ArgumentsBuilder#append(java.lang.String)
	 */
	@Override
	public void append( String argument )
	{
		Objects.requireNonNull( argument );

		argsList.add( argument );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.platform.ArgumentsBuilder#appendIfValueDefined(java.lang.String, java.lang.String)
	 */
	@Override
	public void appendIfValueDefined( String cmdLineSwitch, String value )
	{
		Objects.requireNonNull( cmdLineSwitch );

		if( !Utils.isEmpty( value ) )
		{
			argsList.add( cmdLineSwitch );
			argsList.add( "\"" + value + "\"" );
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.platform.ArgumentsBuilder#getArgumentList()
	 */
	@Override
	public List<String> getArgumentList()
	{
		List<String> argsListCopy = new ArrayList<String>( argsList );
		argsList.clear();

		return Collections.unmodifiableList( argsListCopy );
	}
}
