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
package info.msxlaunchers.openmsx.launcher.extra;

import info.msxlaunchers.openmsx.common.HashUtils;
import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation for the <code>ExtraDataGetter</code> interface
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@Singleton
final class ExtraDataGetterImpl implements ExtraDataGetter
{
	private final String extraDataDirectory;

	private static final String EXTRA_DATA_FILENAME = "extra-data.dat";
	private static final String COMMENT_START = "--";
	private static final char GENERATION_MSX_ID_START = '#';
	private static final char COMMA = ',';
	private static final char PIPE = '|';
	private static final String VERSION_COMMENT = "-- Version ";

	private static String cachedExtraDataFileHash = null;
	private static Map<String,ExtraData> cachedExtraDataMap = null;

	@Inject
	ExtraDataGetterImpl( @Named("LauncherDataDirectory") String extraDataDirectory )
	{
		this.extraDataDirectory = extraDataDirectory;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter#getExtraData()
	 */
	@Override
	public Map<String,ExtraData> getExtraData() throws IOException
	{
		File extraDataFile = new File( extraDataDirectory, EXTRA_DATA_FILENAME );
		String extraDataFileHash = HashUtils.getSHA1Code( extraDataFile );

		if( extraDataFileHash == null )
		{
			IOException ioe = new FileNotFoundException();
			LauncherLogger.logException( this, ioe );
			throw ioe;
		}
		else if( !extraDataFileHash.equals( cachedExtraDataFileHash ) )
		{
			cachedExtraDataFileHash = extraDataFileHash;
			cachedExtraDataMap = readExtraDataFileAndGetMap( extraDataFile );
		}

		return cachedExtraDataMap;
	}

	private Map<String,ExtraData> readExtraDataFileAndGetMap( File extraDataFile ) throws IOException
	{
		Map<String,ExtraData> extraDataMap = null;
		try( BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( extraDataFile ), "UTF-8" ) ) )
		{
		    extraDataMap = new HashMap<>();
		    String text = null;
		    boolean done = false;

		    while ( !done )
		    {
		    	text = reader.readLine();
		    	if( text == null )
		    	{
		    		done = true;
		    	}
		    	else
		    	{
		    		//skip lines that start with -- as they are comments
		    		if( !text.startsWith( COMMENT_START ) )
		    		{
		    			if( text.charAt( 0 ) == GENERATION_MSX_ID_START )
		    			{
		    				//then this is the start of generation MSX Id
		    				int generationMSXId = Utils.getNumber( text.substring( 1 ) );
		    				
		    				//now read the next line
		    				text = reader.readLine();
		    				if( text == null )
		    				{
		    					//this shouldn't happen but we'll check it
		    					done = false;
		    				}
		    				else
		    				{
		    					String[] values = splitGenerationsSoundChipsGenres( text );

		    					ExtraData extraData = new ExtraData( generationMSXId,
		    							Utils.getNumber( values[0] ),
		    							Utils.getNumber( values[1] ),
		    							Utils.getNumber( values[2] ),
		    							Utils.getNumber( values[3] ),
		    							values[4] );

		    					//now process the sha1 codes
			    				text = reader.readLine();
			    				if( text == null )
			    				{
			    					done = true;
			    				}
			    				else
			    				{
			    					processSha1Codes( extraDataMap, extraData, text );
			    				}
		    				}
		    			}
		    		}
		    	}
		    }
		}
		return Collections.unmodifiableMap( extraDataMap );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter#getExtraDataFileVersion()
	 */
	@Override
	public String getExtraDataFileVersion() throws IOException
	{
		String version = null;

		File file = new File( extraDataDirectory, EXTRA_DATA_FILENAME );

		try( BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ), "UTF-8" ) ) )
		{
		    String text = null;
		    String versionLine = null;
		    boolean done = false;
		    boolean startedVersionSection = false;

		    while ( !done )
		    {
		    	text = reader.readLine();

		    	if( text == null )
		    	{
		    		//this means we got to the end without finding the versions section
		    		version = "0.0";
		    		done = true;
		    	}
		    	else if( text.startsWith( VERSION_COMMENT ) )
		    	{
		    		startedVersionSection = true;
		    		versionLine = text;
		    	}
		    	else
		    	{
		    		if( startedVersionSection )
		    		{
		    			//then we finished the version section -> just read the last version number
		    			int beginIndex = VERSION_COMMENT.length();
		    			int endIndex = versionLine.indexOf( ' ', beginIndex );

		    			if( endIndex > beginIndex )
		    			{
		    				version = versionLine.substring( beginIndex, endIndex );
		    			}
		    			else
		    			{
		    				version = "0.0";
		    			}

		    			done = true;
		    		}
		    	}
		    }
		}

		return version;
	}

	private String[] splitGenerationsSoundChipsGenres( String line )
	{
		String[] values = new String[5];

		int indexFirstComma = line.indexOf( COMMA );
		if( indexFirstComma > -1 )
		{
			values[0] = line.substring( 0, indexFirstComma );

			int indexSecondComma = line.indexOf( COMMA, indexFirstComma + 1 );
			if( indexSecondComma > -1 )
			{
				values[1] = line.substring( indexFirstComma + 1, indexSecondComma );

				int indexThirdComma = line.indexOf( COMMA, indexSecondComma + 1 );
				String[] genres;
				if( indexThirdComma > -1 )
				{
					genres = splitGenres( line.substring( indexSecondComma + 1, indexThirdComma ) );
					values[2] = genres[0];
					values[3] = genres[1];
					values[4] = line.substring( indexThirdComma + 1 );
				}
				else
				{
					genres = splitGenres( line.substring( indexSecondComma + 1 ) );
					values[2] = genres[0];
					values[3] = genres[1];
				}
			}
		}

		return values;
	}

	private String[] splitGenres( String text )
	{
		String[] genres = new String[2];

		int indexPipe = text.indexOf( PIPE );
		if( indexPipe > -1 )
		{
			genres[0] = text.substring( 0, indexPipe );
			genres[1] = text.substring( indexPipe + 1 );
		}
		else
		{
			genres[0] = text;
		}

		return genres;
	}

	private void processSha1Codes( Map<String,ExtraData> extraDataMap, ExtraData extraData, String line )
	{
		boolean done = false;
		int startDigit = 0;
		
		while( !done )
		{
			int indexPipe = line.indexOf( PIPE, startDigit );
			if( indexPipe > -1 )
			{
				extraDataMap.put( line.substring( startDigit, indexPipe ), extraData );
				startDigit = indexPipe + 1;
			}
			else
			{
				extraDataMap.put( line.substring( startDigit ), extraData );
				done = true;
			}
		}
	}
}
