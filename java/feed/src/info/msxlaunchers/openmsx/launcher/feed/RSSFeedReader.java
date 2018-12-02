/*
 * Copyright 2017 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import info.msxlaunchers.openmsx.launcher.data.feed.FeedMessage;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
/**
 * Implementation of the <code>FeadReader</code> interface that reads RSS feeds
 * 
 * @since v1.10
 * @author Sam Elsharif
 *
 */
final class RSSFeedReader implements FeedReader
{
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String PUB_DATE = "pubDate";

    RSSFeedReader()
	{
		trustAllCertificates();
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.feed.FeedReader#read(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<FeedMessage> read( String feedUrl, String siteName, String siteUrl ) throws IOException
	{
    	List<FeedMessage> messages = new ArrayList<>();

    	String title = "";
        String link = "";
        String pubdate = "";

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty( XMLInputFactory.IS_COALESCING, Boolean.TRUE );

        try( InputStream in = readRSSFeed( feedUrl ) )
        {
            XMLEventReader eventReader = inputFactory.createXMLEventReader( in );

            while( eventReader.hasNext() )
            {
                XMLEvent event = eventReader.nextEvent();
                if ( event.isStartElement() )
                {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    switch( localPart )
                    {
	                    case TITLE:
	                        title = getCharacterData( event, eventReader );
	                        break;
	                    case LINK:
	                        link = getCharacterData( event, eventReader );
	                        break;
	                    case PUB_DATE:
	                        pubdate = getCharacterData( event, eventReader );
	                        break;
                    }
                }
                else if( event.isEndElement() && event.asEndElement().getName().getLocalPart().equals( ITEM ) )
                {
                	FeedMessage message = new FeedMessage( title, link, pubdate, siteName, siteUrl );
                	messages.add( message );
                	event = eventReader.nextEvent();
                	continue;
                }
            }
        }
        catch( IOException | XMLStreamException e )
        {
        	LauncherLogger.logException( this, e );
        	throw new IOException( e );
        }

        return Collections.unmodifiableList( messages );
	}

	private void trustAllCertificates()
	{
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager()
				{
					public X509Certificate[] getAcceptedIssuers()
					{
						return null;
					}

					public void checkClientTrusted( X509Certificate[] certs, String authType )
					{
					}

					public void checkServerTrusted( X509Certificate[] certs, String authType )
					{
					}
				}
		};

    	try
    	{
    		SSLContext sc = SSLContext.getInstance( "TLSv1.2" );
    		sc.init( null, trustAllCerts, new SecureRandom() );
    		HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
    	}
    	catch( NoSuchAlgorithmException | KeyManagementException e )
    	{
    		//ignore
    	}
	}

    private InputStream readRSSFeed( String feedUrl ) throws IOException
    {
    	URLConnection con = new URL( feedUrl ).openConnection();
    	con.addRequestProperty( "user-agent", "oelsha@engineer.com" );

    	return con.getInputStream();
    }

    private String getCharacterData( XMLEvent event, XMLEventReader eventReader ) throws XMLStreamException
    {
        event = eventReader.nextEvent();
        if ( event instanceof Characters )
        {
        	return event.asCharacters().getData();
        }
        else
        {
        	return "";
        }
    }
}
