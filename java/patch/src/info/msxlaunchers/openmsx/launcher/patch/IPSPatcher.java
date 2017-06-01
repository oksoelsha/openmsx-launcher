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
package info.msxlaunchers.openmsx.launcher.patch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import info.msxlaunchers.openmsx.common.HashUtils;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;

/**
 * IPS Patcher implementation of <code>Patcher</code> interface.
 * 
 * @author Sam Elsharif
 * @since v1.9
 */
final class IPSPatcher extends AbstractPatcher
{
	private final int MINIMUM_IPS_SIZE = 8;
	private final int MAXIMUM_IPS_SIZE = 16777216;
	private final int MAXIMUM_SOURCE_SIZE = 16777216;
	private final byte[] PATCH_HEADER = "PATCH".getBytes();
	private final int RECORD_OFFSET_BYTE_COUNT = 3;
	private final int RECORD_SIZE_BYTE_COUNT = 2;
	private final int RECORD_RLE_SIZE_BYTE_COUNT = 2;
	private final int IPS_TRUNCATE_BYTE_COUNT = 3;
	private final byte[] PATCH_EOF = "EOF".getBytes();

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.patch.AbstractPatcher#performValidation(java.nio.file.Path, java.nio.file.Path, boolean, java.lang.String)
	 */
	@Override
	protected void performValidation( Path fileToPatch, Path patchFile, boolean skipCheckcum, String checksum ) throws PatchException
	{
		validateFileSize( fileToPatch, 0, MAXIMUM_SOURCE_SIZE, PatchExceptionIssue.FILE_TO_PATCH_NOT_PATCHABLE );
		validateFileSize( patchFile, MINIMUM_IPS_SIZE, MAXIMUM_IPS_SIZE, PatchExceptionIssue.INVALID_PATCH_FILE );
		verifyChecksumIfRequested( fileToPatch, checksum );
	}

	@Override
	protected void patchFileData( Path fileToPatch, Path patchFile, Path targetFile, boolean skipChecksumValidation ) throws PatchException
	{
		byte[] fileToPatchData = readFileData( fileToPatch );
		byte[] patchFileData = readFileData( patchFile );

		if( !isByteSequenceEqualToString( patchFileData, 0, PATCH_HEADER ) )
		{
			throw new PatchException( PatchExceptionIssue.INVALID_PATCH_FILE );
		}

		boolean done = false;
		int ipsFileDataIndex = PATCH_HEADER.length;
		byte[] patchedBuffer = new byte[fileToPatchData.length];
		System.arraycopy( fileToPatchData, 0, patchedBuffer, 0, fileToPatchData.length );

		while( !done )
		{
			if( isByteSequenceEqualToString( patchFileData, ipsFileDataIndex, PATCH_EOF ) )
			{
				ipsFileDataIndex += PATCH_EOF.length;
				done = true;
			}
			else
			{
				int recordOffsetValue = byte3ToInt( patchFileData, ipsFileDataIndex );
				ipsFileDataIndex += RECORD_OFFSET_BYTE_COUNT;

				int recordSizeValue = byte2ToInt( patchFileData, ipsFileDataIndex );
				ipsFileDataIndex += RECORD_SIZE_BYTE_COUNT;

				if( recordSizeValue == 0 )
				{
					//RLE case
					int rleSizeValue = byte2ToInt( patchFileData, ipsFileDataIndex );

					//test if the offset and size are within the current target
					int offsetDataSize = recordOffsetValue + rleSizeValue;
					if( offsetDataSize > fileToPatchData.length )
					{
						//regenerate memory for the target
						patchedBuffer = resizeBuffer( patchedBuffer, offsetDataSize );
					}

					ipsFileDataIndex += RECORD_RLE_SIZE_BYTE_COUNT;

					Arrays.fill( patchedBuffer, recordOffsetValue, recordOffsetValue + rleSizeValue, patchFileData[ipsFileDataIndex] );

					ipsFileDataIndex++;
				}
				else
				{
					//test if the offset and size are within the current target
					int offsetDataSize = recordOffsetValue + recordSizeValue;
					if( offsetDataSize > fileToPatchData.length )
					{
						//regenerate memory for the target
						patchedBuffer = resizeBuffer( patchedBuffer, offsetDataSize );
					}

					validateArraySize( patchFileData, ipsFileDataIndex, recordSizeValue );

					System.arraycopy( patchFileData, ipsFileDataIndex, patchedBuffer, recordOffsetValue, recordSizeValue );

					ipsFileDataIndex += recordSizeValue;
				}
			}
		}

		//check if we need to truncate - ignore any data other than 3 bytes
		int truncateDataLength = patchFileData.length - ipsFileDataIndex;

		if( truncateDataLength == IPS_TRUNCATE_BYTE_COUNT )
		{
			int targetROMTruncatedSize = byte3ToInt( patchFileData, ipsFileDataIndex );

			if( targetROMTruncatedSize > 0 && targetROMTruncatedSize < fileToPatchData.length )
			{
				patchedBuffer = resizeBuffer( patchedBuffer, targetROMTruncatedSize );
			}
		}

		savePatchedData( patchedBuffer, fileToPatch, targetFile );
	}

	private byte[] readFileData( Path file ) throws PatchException
	{
		try
		{
			return Files.readAllBytes( file );
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );

			throw new PatchException( PatchExceptionIssue.IO );
		}
	}

	private byte[] resizeBuffer( byte[] buffer, int size )
	{
		byte[] tempBuffer = new byte[size];
		System.arraycopy( buffer, 0, tempBuffer, 0, Math.min( buffer.length, size ) );

		return tempBuffer;
	}

	private int byte3ToInt( byte[] data, int index ) throws PatchException
	{
		validateArraySize( data, index, 3 );

		return (((int)data[index] << 16) & 0xff0000) | ((data[index + 1] << 8) & 0xff00) | (data[index + 2] & 0xff);
	}

	private int byte2ToInt( byte[] data, int index ) throws PatchException
	{
		validateArraySize( data, index, 2 );

		return (((int)data[index] << 8) & 0xff00) | (data[index + 1] & 0xff);
	}

	private void validateArraySize( byte[] data, int index, int length ) throws PatchException
	{
		if( (index + length) > data.length )
		{
			throw new PatchException( PatchExceptionIssue.INVALID_PATCH_FILE );
		}
	}

	private void verifyChecksumIfRequested( Path fileToPatch, String checksum ) throws PatchException
	{
		if( checksum != null )
		{
			String trimmedChecksum = checksum.trim();
			String sha1Code = HashUtils.getSHA1Code( fileToPatch.toFile() );
	
			//first do the SHA1 check
			if( !sha1Code.equalsIgnoreCase( trimmedChecksum ) )
			{
				//do the additional MD5 check
				String md5Sum = HashUtils.getMD5Sum( fileToPatch.toFile() );
	
				if( !md5Sum.equalsIgnoreCase( trimmedChecksum ) )
				{
					//at last do the additional CRC32 check
					String crc32Code = HashUtils.getCRC32Code( fileToPatch.toFile() );

					if( !crc32Code.equalsIgnoreCase( trimmedChecksum ) )
					{
						throw new PatchException( PatchExceptionIssue.SOURCE_FILE_CHECKSUM_NOT_MATCH );
					}
				}
			}
		}
	}

	private void savePatchedData( byte[] patchedFileData, Path fileToPatch, Path targetFile ) throws PatchException
	{
		Path fileToWrite;
		if( targetFile == null )
		{
			fileToWrite = fileToPatch;
		}
		else
		{
			fileToWrite = targetFile;
		}

		try
		{
			Files.write( fileToWrite, patchedFileData );
		}
		catch( IOException ioe )
		{
			throw new PatchException( PatchExceptionIssue.TARGET_FILE_CANNOT_WRITE );
		}
	}
}
