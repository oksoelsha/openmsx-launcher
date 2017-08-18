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
package info.msxlaunchers.openmsx.game.repository;

import info.msxlaunchers.openmsx.game.repository.processor.XMLProcessor;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.inject.Inject;

/**
 * XML implementation of <code>RepositoryData</code>
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class XMLRepositoryData implements RepositoryData
{
	private final XMLProcessor xmlProcessor;
	private final Set<XMLFileGetter> xmlFileGetters;

	@Inject
	XMLRepositoryData( XMLProcessor xmlProcessor, Set<XMLFileGetter> xmlFileGetters )
	{
		this.xmlProcessor = Objects.requireNonNull( xmlProcessor );
		this.xmlFileGetters = Objects.requireNonNull( xmlFileGetters );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.RepositoryData#getRepositoryInfo()
	 */
	@Override
	public Map<String, RepositoryGame> getRepositoryInfo() throws IOException
	{
		Map<String, RepositoryGame> repositoryInfo = null;

		for( XMLFileGetter xmlFileGetter: xmlFileGetters )
		{
			File xmlFile = xmlFileGetter.get();

			if( xmlFile != null && xmlFile.exists() )
			{
				if( repositoryInfo == null )
				{
					repositoryInfo = new HashMap<>();
				}
				repositoryInfo.putAll( xmlProcessor.getRepositoryInfo( xmlFile ) );
			}
		}

		return repositoryInfo;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.RepositoryData#getDumpCodes(java.lang.String)
	 */
	@Override
	public Set<String> getDumpCodes( String code ) throws IOException
	{
		Objects.requireNonNull( code );

		for( XMLFileGetter xmlFileGetter: xmlFileGetters )
		{
			Set<String> dumpCodes = xmlProcessor.getDumpCodes( xmlFileGetter.get(), code );

			if( !dumpCodes.isEmpty() )
			{
				//dumps should only be in one of the XML files. If we found some in any of them, just return the set
				return dumpCodes;
			}
		}

		//at this point nothing was found so return an empty Set
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.RepositoryData#getGameInfo(java.lang.String)
	 */
	@Override
	public RepositoryGame getGameInfo( String code ) throws IOException
	{
		Objects.requireNonNull( code );

		for( XMLFileGetter xmlFileGetter: xmlFileGetters )
		{
			RepositoryGame repositoryGame = xmlProcessor.getGameInfo( xmlFileGetter.get(), code );

			if( repositoryGame != null )
			{
				return repositoryGame;
			}
		}

		//at this point nothing was found so just return null
		return null;
	}
}
