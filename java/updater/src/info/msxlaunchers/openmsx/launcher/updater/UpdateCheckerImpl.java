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
package info.msxlaunchers.openmsx.launcher.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation of <code>UpdateChecker</code>. It was implemented as Guice Singleton because it maintains a state (isDownloadedNewOpenLauncher flag)
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
@Singleton
final class UpdateCheckerImpl implements UpdateChecker
{
	private final String jarFilesDirectory;
	private final String extraDataDirectory;
	private final String helpFileDirectory;
	private final LauncherUpdater launcherUpdater;

	private final String UPDATED_JAR_FILENAME = "new-launcher.zip";
	private final int WRITE_BUFFER = 2048;

	private boolean isDownloadedNewOpenLauncher = false;

	@Inject
	UpdateCheckerImpl( @Named("JarFilesDirectory") String jarFilesDirectory, @Named("LauncherDataDirectory") String extraDataDirectory, LauncherUpdater launcherUpdater )
	{
		this.jarFilesDirectory = jarFilesDirectory;
		this.extraDataDirectory = extraDataDirectory;
		this.helpFileDirectory = extraDataDirectory;
		this.launcherUpdater = launcherUpdater;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.webclient.UpdateChecker#getVersions()
	 */
	@Override
	public Map<String,String> getVersions() throws IOException
	{
		Map<String,String> versions = new HashMap<String,String>();

        HttpURLConnection conn = getWebServiceConnection( "http://msxlaunchers.info/openmsx-launcher/versions" );
 
        try( BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) ) )
        {
        	boolean done = false;
        	while( !done )
        	{
        		String apiOutput = br.readLine();
	
		        if( apiOutput == null )
		        {
		        	done = true;;
		        }
		        else
		        {
		        	int index = apiOutput.indexOf( '=' );
		        	String key = apiOutput.substring( 0, index );
		        	String value = apiOutput.substring( index + 1 );

		        	versions.put( key, value );
		        }
        	}
	        conn.disconnect();
        }

        return versions;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.webclient.UpdateChecker#getNewOpenMSXLauncher()
	 */
	@Override
	public void getNewOpenMSXLauncher() throws FileUpdateFailedException, IOException
	{
        HttpURLConnection conn = getWebServiceConnection( "http://msxlaunchers.info/openmsx-launcher/new/launcher" );

        File zipFile = new File( jarFilesDirectory, UPDATED_JAR_FILENAME );
        writeDownloadedFile( conn, zipFile );

        launcherUpdater.installNewOpenMSXLauncher( jarFilesDirectory, helpFileDirectory, zipFile );

        isDownloadedNewOpenLauncher = true;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.webclient.UpdateChecker#isNewOpenMSXLauncherDownloaded()
	 */
	@Override
	public boolean isNewOpenMSXLauncherDownloaded()
	{
		return isDownloadedNewOpenLauncher;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.webclient.UpdateChecker#getNewExtraDataFile()
	 */
	@Override
	public void getNewExtraDataFile() throws IOException
	{
        HttpURLConnection conn = getWebServiceConnection( "http://msxlaunchers.info/openmsx-launcher/new/extra-data" );

        writeDownloadedFile( conn, new File( extraDataDirectory, "extra-data.dat" ) );
	}

	private HttpURLConnection getWebServiceConnection( String urlString ) throws IOException
	{
        URL url = new URL( urlString );
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod( "GET" );
 
        if( conn.getResponseCode() != 200 ) 
        {
        	throw new IOException();
        }

        return conn;
	}

	private void writeDownloadedFile( HttpURLConnection conn, File file ) throws IOException
	{
        InputStream in = conn.getInputStream();
        try( FileOutputStream out = new FileOutputStream( file ) )
        {
        	byte[] buf = new byte[WRITE_BUFFER];
            int n = in.read( buf );
            while( n >= 0 )
            {
            	out.write( buf, 0, n );
            	n = in.read( buf );
            }
            out.flush();
        }

        conn.disconnect();		
	}
}
