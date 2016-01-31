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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Common SAX parser handler for all openMSX softwaredb.xml parse cases.
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
class ParseHandler extends DefaultHandler
{
	private StringBuilder tempTextBuilder = new StringBuilder();

	@Override
	public void characters( char[] buffer, int start, int length )
	{
		tempTextBuilder.append( buffer, start, length );
	}

	@Override
	public void startElement( String uri, String localName, String qName, Attributes attributes )
            throws SAXException
    {
		if( qName.equalsIgnoreCase( "software" ) )
		{
			resetNodeTextHolder();
		}
    }

	protected String getNodeText()
	{
		return tempTextBuilder.toString().replaceAll( "\\n\t+", "" );
	}

	protected void resetNodeTextHolder()
	{
		tempTextBuilder = new StringBuilder();
	}
}
