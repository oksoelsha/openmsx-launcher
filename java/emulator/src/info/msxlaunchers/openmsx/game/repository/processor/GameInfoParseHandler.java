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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * SAX Parser Handler for the Game info
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class GameInfoParseHandler extends ParseHandler
{
	private String code;
	private RepositoryGame repositoryGame;

	private String title;
	private String company;
	private String year;
	private String country;
	private boolean original;
	private String originalString;
	private String mapper;
	private String start;
	private String remark;

	private boolean found = false;
	
	GameInfoParseHandler( String code )
	{
		this.code = code;
	}

	public RepositoryGame getGameInfo()
	{
		return repositoryGame;
	}

	@Override
	public void startElement( String uri, String localName, String qName, Attributes attributes)
			throws SAXException
	{
		super.startElement( uri, localName, qName, attributes );

		if( qName.equalsIgnoreCase( "original" ) )
		{
			original = attributes.getValue( "value" ).equals( "true" );
		}		
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
		}
		else if( qName.equalsIgnoreCase( "original" ) )
		{
			originalString = tempText;
		}
		else if( qName.equalsIgnoreCase( "type" ) || qName.equalsIgnoreCase( "boot" ) )
		{
			mapper = tempText;
		}
		else if( qName.equalsIgnoreCase( "start" ) )
		{
			start = tempText;
		}
		else if( qName.equalsIgnoreCase( "text" ) )
		{
			remark = tempText;
		}
		else if( qName.equalsIgnoreCase( "hash" ) )
		{
			if( tempText.equalsIgnoreCase( code ) )
			{
				found = true;
			}
		}
		else if( qName.equalsIgnoreCase( "dump" ) )
		{
			if( found )
			{
				repositoryGame = new RepositoryGame( title, company, year, country,
						original, originalString, mapper, start, remark );
				throw new StopSAXParsingException();
			}
			else
			{
				original = false;
				originalString = null;
				mapper = null;
				start = null;
				remark = null;
			}
		}

		resetNodeTextHolder();
    }
}
