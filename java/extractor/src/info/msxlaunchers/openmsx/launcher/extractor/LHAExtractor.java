/*
 * Copyright 2020 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import info.msxlaunchers.openmsx.common.ActionDecider;
import info.msxlaunchers.openmsx.common.FileTypeUtils;
import jp.gr.java_conf.dangan.util.lha.LhaHeader;
import jp.gr.java_conf.dangan.util.lha.LhaInputStream;

/**
 * Implementation of the interface <code>Extractor</code> for the LHA file format. This implementation uses libjlha-java
 * source obtained from http://dangan.g.dgdg.jp/Content/Program/Java/jLHA/LhaLibrary.html. Jar file was built and added to
 * openMSX Launcher's source tree
 * 
 * @since v1.14
 * @author Sam Elsharif
 */
final class LHAExtractor implements Extractor
{
	private static final int READ_BUFFER_SIZE = 737280;

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.extractor.Extractor#extract(java.lang.String, java.lang.String, boolean, info.msxlaunchers.openmsx.common.ActionDecider)
	 */
	@Override
	public void extract( String fileToExtract, String targetDirectory, boolean extractOnlyMsxImages, ActionDecider actionDecider )
			throws ExtractionException
	{
		Path fileToExtractPath = Paths.get( fileToExtract );
		Path directoryToWriteTo = targetDirectory == null? fileToExtractPath.getParent() : Paths.get( targetDirectory );

		byte[] buffer = new byte[READ_BUFFER_SIZE];

		try( InputStream is = new FileInputStream( fileToExtractPath.toFile() ); LhaInputStream lis = new LhaInputStream( is ) )
		{
			LhaHeader header = lis.getNextEntry();
			while( header != null )
			{
				File file = new File( header.getPath() );
				File fullFilename = new File( directoryToWriteTo.toFile(), file.getName() );

				if( isExtract( extractOnlyMsxImages, file ) )
				{
					if( fullFilename.exists() )
					{
						if( !actionDecider.isYesAll() && !actionDecider.isNoAll() )
						{
							actionDecider.promptForAction( file.getName() );
						}

						if( actionDecider.isYes() || actionDecider.isYesAll() )
						{
							extractFile( fullFilename, lis, buffer );
						}
						else if( actionDecider.isCancel() )
						{
							break;
						}
					}
					else
					{
						extractFile( fullFilename, lis, buffer );
					}
				}
				header = lis.getNextEntry();
			}
		}
		catch( FileNotFoundException fnfe )
		{
			throw new ExtractionException( ExtractorExceptionIssue.COMPRESSED_FILE_NOT_FOUND );
		}
		catch( IOException ioe )
		{
			throw new ExtractionException( ExtractorExceptionIssue.IO );
		}
	}

	private void extractFile( File fullFilename, LhaInputStream lis, byte[] buffer ) throws IOException
	{
		try( OutputStream os = new FileOutputStream( fullFilename ) )
		{
			int len;
			while ( (len = lis.read( buffer )) > 0 )
			{
				os.write( buffer, 0, len );
			}
		}
	}

	private boolean isExtract( boolean extractOnlyMsxImages, File file )
	{
		return (!extractOnlyMsxImages) ||
				(FileTypeUtils.isDisk(file) || FileTypeUtils.isROM(file) || FileTypeUtils.isTape(file));
	}
}
