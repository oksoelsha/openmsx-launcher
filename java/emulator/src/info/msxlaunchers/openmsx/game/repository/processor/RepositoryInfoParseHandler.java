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
package info.msxlaunchers.openmsx.game.repository.processor;

import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import org.xml.sax.SAXException;

/**
 * SAX Parser handler for getting title, company, year and country information for a game
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class RepositoryInfoParseHandler extends ParseHandler
{
	private Map<String,RepositoryGame> repositoryInfo = new HashMap<String, RepositoryGame>();

	private String title;
	private String company;
	private String year;
	private String country;

	private RepositoryGame repositoryGame = null;

	Map<String,RepositoryGame> getRepositoryInfo()
	{
		return Collections.unmodifiableMap( repositoryInfo );
	}

	@Override
	public void endElement( String uri, String localName, String qName )
            throws SAXException
    {
		String tempText = getNodeText();

		if( qName.equalsIgnoreCase( "title" ) )
		{
			title = tempText;
		}
		else if( qName.equalsIgnoreCase( "company" ) )
		{
			company = tempText;
		}
		else if( qName.equalsIgnoreCase( "year" ) )
		{
			year = tempText;
		}
		else if( qName.equalsIgnoreCase( "country" ) )
		{
			country = tempText;
			repositoryGame = new RepositoryGame( title, company, year, country );
		}
		else if( qName.equalsIgnoreCase( "hash" ) )
		{
			repositoryInfo.put( tempText, repositoryGame );
		}
		
		resetNodeTextHolder();
    }
}
