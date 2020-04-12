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
package info.msxlaunchers.openmsx.launcher.decompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import info.msxlaunchers.openmsx.common.FileTypeUtils;
import jp.gr.java_conf.dangan.util.lha.LhaHeader;
import jp.gr.java_conf.dangan.util.lha.LhaInputStream;

/**
 * Implementation of the interface <code>Decompressor</code> for the LHA file format. This implementation uses libjlha-java
 * source obtained from http://dangan.g.dgdg.jp/Content/Program/Java/jLHA/LhaLibrary.html. Jar file was built and added to
 * openMSX Launcher's source tree
 * 
 * @since v1.14
 * @author Sam Elsharif
 */
final class LHADecompressor implements Decompressor
{
	private static final int READ_BUFFER_SIZE = 737280;

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.decompressor.Decompressor#decompress(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void decompress( String fileToDecompress, String targetDirectory, boolean decompressOnlyMsxFiles )
			throws DecompressionException
	{
		Path fileToDecompressPath = Paths.get( fileToDecompress );
		Path directoryToWriteTo = targetDirectory == null? fileToDecompressPath.getParent() : Paths.get( targetDirectory );

		byte[] buffer = new byte[READ_BUFFER_SIZE];

		try( InputStream is = new FileInputStream( fileToDecompressPath.toFile() ); LhaInputStream lis = new LhaInputStream( is ) )
		{
			LhaHeader header = lis.getNextEntry();
			while(header != null) {
				File file = new File(header.getPath());
				if( isDecompress( decompressOnlyMsxFiles, file ) )
				{
					try( OutputStream os = new FileOutputStream( new File( directoryToWriteTo.toFile(), file.getName() ) ) )
					{
		                int len;
		                while ( (len = lis.read( buffer )) > 0 )
		                {
		                	os.write( buffer, 0, len );
		                }
					}
				}
				header = lis.getNextEntry();
			}
		}
		catch( FileNotFoundException fnfe )
		{
			throw new DecompressionException( DecompressorExceptionIssue.COMPRESSED_FILE_NOT_FOUND );
		}
		catch( IOException ioe )
		{
			throw new DecompressionException( DecompressorExceptionIssue.IO );
		}
	}

	private boolean isDecompress( boolean decompressOnlyMsxFiles, File file )
	{
		return (!decompressOnlyMsxFiles) ||
				(FileTypeUtils.isDisk(file) || FileTypeUtils.isROM(file) || FileTypeUtils.isTape(file));
	}
}
