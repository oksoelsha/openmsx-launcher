/*
 * Copyright 2014 Sam Elsharif
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

import java.util.HashSet;
import java.util.Set;

import org.xml.sax.SAXException;

/**
 * SAX Parser handler for getting all dumps of a game
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
final class AllDumpCodesParseHandler extends ParseHandler
{
	private final String code;
	private Set<String> allDumpCodes = new HashSet<String>();

	private boolean foundCode = false;
	
	AllDumpCodesParseHandler( String code )
	{
		this.code = code;
	}

	Set<String> getAllDumpCodes()
	{
		return allDumpCodes;
	}

	@Override
	public void endElement( String uri, String localName, String qName )
            throws SAXException
    {
		if( qName.equalsIgnoreCase( "hash" ) )
		{
			String tempText = getNodeText();

			if( tempText.equalsIgnoreCase( code ) )
			{
				foundCode = true;
			}

			allDumpCodes.add( tempText );
		}
		else if( qName.equalsIgnoreCase( "software" ) )
		{
			if( foundCode )
			{
				//then exit
				throw new StopSAXParsingException();
			}
			else
			{
				allDumpCodes.clear();
			}
		}
		
		resetNodeTextHolder();
    }
}
