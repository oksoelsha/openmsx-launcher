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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser implementation for <code>XMLProcessor</code>
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
final class SAXXMLProcessor implements XMLProcessor
{
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.processor.XMLProcessor#getRepositoryInfo(java.io.File)
	 */
	@Override
	public Map<String,RepositoryGame> getRepositoryInfo( File xmlFile ) throws IOException
	{
		RepositoryInfoParseHandler handler = new RepositoryInfoParseHandler();

		parse( xmlFile, handler );

		return handler.getRepositoryInfo();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.processor.XMLProcessor#getDumpCodes(java.io.File, java.lang.String)
	 */
	@Override
	public Set<String> getDumpCodes( File xmlFile, String code ) throws IOException
	{
		AllDumpCodesParseHandler handler = new AllDumpCodesParseHandler( code );

		parse( xmlFile, handler );
		
		return handler.getAllDumpCodes();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.game.repository.processor.XMLProcessor#getGameInfo(java.io.File, java.lang.String)
	 */
	@Override
	public RepositoryGame getGameInfo( File xmlFile, String code ) throws IOException
	{
		GameInfoParseHandler handler = new GameInfoParseHandler( code );

		parse( xmlFile, handler );

		return handler.getGameInfo();
	}

	private static void parse( File xmlFile, DefaultHandler handler ) throws IOException
	{
		SAXParserFactory spfac = SAXParserFactory.newInstance();
		try
		{
			spfac.setFeature( "http://apache.org/xml/features/validation/schema", false );
			spfac.setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd", false );
		}
		catch( SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e )
		{
			//shouldn't happen
		}

		try( InputStream inputStream = new FileInputStream( xmlFile ) )
		{
			SAXParser parser = spfac.newSAXParser();
			Reader reader = new InputStreamReader( inputStream, "UTF-8" );
			InputSource is = new InputSource( reader );
			is.setEncoding( "UTF-8" );
			parser.parse( is, handler );
		}
		catch( StopSAXParsingException sspe )
		{
			//this exception means that parsing was stopped deliberately (e.g. data was found)
		}
		catch( ParserConfigurationException | SAXException e )
		{
			throw new IOException();
		}
	}
}
