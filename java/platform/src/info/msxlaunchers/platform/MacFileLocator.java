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

import java.io.IOException;
import java.util.Objects;

import com.google.inject.Inject;

/**
 * MacOS implementation of <code>FileLocator</code> that uses 'open' to bring up the Finder
 * 
 * @since v1.1
 * @author Sam Elsharif
 *
 */
final class MacFileLocator implements FileLocator
{
	private final ArgumentsBuilder argumentsBuilder;

	@Inject
	MacFileLocator( ArgumentsBuilder argumentsBuilder )
	{
		this.argumentsBuilder = Objects.requireNonNull( argumentsBuilder );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.platform.FileLocator#locateFile(java.lang.String)
	 */
	@Override
	public Process locateFile( String filePath ) throws IOException
	{
		argumentsBuilder.append( "open" );
		argumentsBuilder.appendIfValueDefined( "-R", filePath );

		return new ProcessBuilder( argumentsBuilder.getArgumentList() ).start();
	}
}
