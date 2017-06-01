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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import info.msxlaunchers.openmsx.common.HashUtils;
import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;

/**
 * UPS Patcher implementation of <code>Patcher</code> interface.
 * 
 * @author Sam Elsharif
 * @since v1.9
 */
final class UPSPatcher extends AbstractPatcher
{
	private final byte[] PATCH_HEADER = "UPS1".getBytes();
	private final String tempOutputFile = "tempOutputFile.tmp";
	private final int READ_WRITE_BUFFER_SIZE = 4194304;
	private final int CRC_LENGTH = 4;
	private final int CRC_CHECKS_LENGTH = CRC_LENGTH * 3;
	private final int XORED_DATA_CHUNK_SIZE= 4096;

	@Override
	protected void performValidation( Path fileToPatch, Path patchFile, boolean skipChecksumValidation, String checksum ) throws PatchException
	{
		//no limit on file sizes and also ignore the checksum
		//but we need to read the patch file and file-to-patch CRCs from the patch itself and validate
		if( !skipChecksumValidation )
		{
			String sourceChecksumFromPatch;
			String patchChecksumFromPatch;
			try( RandomAccessFile patchStream = new RandomAccessFile( patchFile.toFile(), "r" ) )
			{
				long patchDataSize = getPatchDataSize( patchFile );
	
				patchStream.seek( patchDataSize );
				sourceChecksumFromPatch = getCRCFromPatch( patchStream );
	
				patchStream.seek( patchDataSize + CRC_LENGTH * 2 );
				patchChecksumFromPatch = getCRCFromPatch( patchStream );
			}
			catch( IOException ioe )
			{
				LauncherLogger.logException( this, ioe );

				throw new PatchException( PatchExceptionIssue.IO );
			}

			String sourceChecksum = HashUtils.getCRC32Code( fileToPatch.toFile() );
			if( !sourceChecksum.equalsIgnoreCase( sourceChecksumFromPatch ) )
			{
				throw new PatchException( PatchExceptionIssue.SOURCE_FILE_CHECKSUM_NOT_MATCH );
			}
	
			//TODO check the CRC of the patch file (minus the last 12 bytes of CRC checks)
		}
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.patch.AbstractPatcher#patchFileData(java.nio.file.Path, java.nio.file.Path, java.nio.file.Path)
	 */
	@Override
	protected void patchFileData( Path fileToPatch, Path patchFile, Path targetFile, boolean skipChecksumValidation ) throws PatchException
	{
		Path temporaryOutputFile = createTempOutputfile( fileToPatch );

		try( PositionedBufferedInputStream patchStream =  new PositionedBufferedInputStream( new FileInputStream( patchFile.toFile() ) );
				BufferedRandomAccessFile resultStream = new BufferedRandomAccessFile( temporaryOutputFile.toFile(), "rw", XORED_DATA_CHUNK_SIZE ) )
		{
			validatePatchHeader( patchStream );
			long sourceFileSize = getValueFromPatch( patchStream );
			long targetFileSize = getValueFromPatch( patchStream );

			enlargeTempOutputfileIfNecessary( temporaryOutputFile, sourceFileSize, targetFileSize );

			patch( patchFile, patchStream, resultStream );

			shrinkTempOutputfileIfNecessary( temporaryOutputFile, sourceFileSize, targetFileSize );

			validateTargetFileChecksum( patchStream, temporaryOutputFile, skipChecksumValidation );

			moveToAppropriateTarget( temporaryOutputFile, fileToPatch, targetFile );
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );

			throw new PatchException( PatchExceptionIssue.IO );
		}
		finally
		{
			cleanupTemporaryOutputFile( temporaryOutputFile );
		}

	}

	private String getCRCFromPatch( InputStream in ) throws PatchException
	{
		byte[] crcBytes = new byte[CRC_LENGTH];
		try
		{
			in.read( crcBytes );
			return Integer.toHexString( ByteBuffer.wrap( crcBytes ).order( ByteOrder.LITTLE_ENDIAN ).getInt() );
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );

			throw new PatchException( PatchExceptionIssue.IO );
		}
	}

	private String getCRCFromPatch( RandomAccessFile in ) throws PatchException
	{
		byte[] crcBytes = new byte[CRC_LENGTH];
		try
		{
			in.read( crcBytes );
			return Integer.toHexString( ByteBuffer.wrap( crcBytes ).order( ByteOrder.LITTLE_ENDIAN ).getInt() );
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );

			throw new PatchException( PatchExceptionIssue.IO );
		}
	}

	private void patch( Path patchFile, PositionedBufferedInputStream patchStream, RandomAccessFile resultStream ) throws PatchException
	{
		long patchDataSize = getPatchDataSize( patchFile );

		long offset = 0;
		while( patchStream.getPosition() != patchDataSize )
		{
			offset += getValueFromPatch( patchStream );
			List<Integer> xoredData = new ArrayList<>( XORED_DATA_CHUNK_SIZE );
			try
			{
				long writeOffset = offset;
				resultStream.seek( offset );
				for( int b = patchStream.read(); b != 0; b = patchStream.read() )
				{
					xoredData.add( resultStream.read() ^ b );
					offset++;
				}
				resultStream.seek( writeOffset );
				resultStream.write( getDataByteArray( xoredData ) );
				offset++;
			}
			catch( IOException ioe )
			{
				LauncherLogger.logException( this, ioe );

				throw new PatchException( PatchExceptionIssue.IO );
			}
		}
	}

	private long getPatchDataSize( Path patchFile ) throws PatchException
	{
		try
		{
			long size = Files.size( patchFile ) - CRC_CHECKS_LENGTH;

			if( size <= 0 )
			{
				throw new PatchException( PatchExceptionIssue.INVALID_PATCH_FILE );
			}

			return size;
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );

			throw new PatchException( PatchExceptionIssue.IO );
		}
	}

	private byte[] getDataByteArray( List<Integer> data )
	{
		int length = data.size();
		byte[] byteArray = new byte[length];
		for( int index = 0; index < length; index++ )
		{
			byteArray[index] = data.get( index ).byteValue();
		}
		return byteArray;
	}

	private Path createTempOutputfile( Path source ) throws PatchException
	{
		try
		{
			Path tempFile = Files.createTempFile( tempOutputFile, null );
			Files.copy( source, tempFile, StandardCopyOption.REPLACE_EXISTING );

			return tempFile;
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );

			throw new PatchException( PatchExceptionIssue.IO );
		}
	}

	private void enlargeTempOutputfileIfNecessary( Path temporaryOutputFile, long sourceFileSize, long targetFileSize ) throws PatchException
	{
		if( targetFileSize > sourceFileSize  )
		{
			long totalToAdd = targetFileSize - sourceFileSize;
			long numberOfChunks = totalToAdd / READ_WRITE_BUFFER_SIZE;
			byte[] bytesToWrite = new byte[READ_WRITE_BUFFER_SIZE];
			int index;

			try
			{
				for(index = 0; index < numberOfChunks; index++) {
					Files.write( temporaryOutputFile, bytesToWrite, StandardOpenOption.APPEND );
				}
				int modulo = (int)(totalToAdd % READ_WRITE_BUFFER_SIZE);
				Files.write( temporaryOutputFile, new byte[modulo], StandardOpenOption.APPEND );
			}
			catch( IOException ioe )
			{
				LauncherLogger.logException( this, ioe );

				throw new PatchException( PatchExceptionIssue.IO );
			}
		}
	}

	private void shrinkTempOutputfileIfNecessary( Path temporaryOutputFile, long sourceFileSize, long targetFileSize ) throws PatchException
	{
		if( targetFileSize < sourceFileSize  )
		{
			try( FileOutputStream os = new FileOutputStream(temporaryOutputFile.toFile(), true); FileChannel outChan = os.getChannel() )
			{
				outChan.truncate( targetFileSize );
			}
			catch( IOException ioe )
			{
				LauncherLogger.logException( this, ioe );

				throw new PatchException( PatchExceptionIssue.IO );
			}
		}
	}

	private void validatePatchHeader( InputStream inputStream ) throws PatchException
	{
		byte[] header = new byte[PATCH_HEADER.length];

		try
		{
			inputStream.read( header, 0, PATCH_HEADER.length );

			if( !isByteSequenceEqualToString( header, 0, PATCH_HEADER ) )
			{
				throw new PatchException( PatchExceptionIssue.INVALID_PATCH_FILE );
			}
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );

			throw new PatchException( PatchExceptionIssue.IO );
		}
	}

	private long getValueFromPatch( InputStream in ) throws PatchException
	{
		try
		{
			int b = in.read();
			long value = b & 0x7F;

			for( int index = 1; (b & 0x80) == 0; index++ )
			{
				b = in.read();
				value += ((b & 0x7F) + 1) << (7 * index);
			}

			return value;
		}
		catch( IOException ioe )
		{
			throw new PatchException( PatchExceptionIssue.INVALID_PATCH_FILE );
		}
	}

	private void validateTargetFileChecksum( InputStream patchStream, Path outputFile, boolean skipChecksumValidation ) throws PatchException
	{
		if( !skipChecksumValidation )
		{
			//skip the next four bytes as we got them already earlier
			getCRCFromPatch( patchStream );
	
			//now get the target file CRC
			String targetChecksumFromPatch = getCRCFromPatch( patchStream );
	
			String targetChecksum = HashUtils.getCRC32Code( outputFile.toFile() );
			if( !targetChecksum.equalsIgnoreCase( targetChecksumFromPatch ) )
			{
				throw new PatchException( PatchExceptionIssue.INVALID_PATCH_FILE );
			}
		}
	}

	private void moveToAppropriateTarget( Path temporaryFile, Path fileToPatch, Path targetFile ) throws PatchException
	{
		Path finalPatchedFile;
		if( targetFile == null )
		{
			finalPatchedFile = fileToPatch;
		}
		else
		{
			finalPatchedFile = targetFile;
		}

		try
		{
			Files.copy( temporaryFile, finalPatchedFile, StandardCopyOption.REPLACE_EXISTING );
		}
		catch( IOException ioe )
		{
			LauncherLogger.logException( this, ioe );

			throw new PatchException( PatchExceptionIssue.IO );
		}
	}

	private void cleanupTemporaryOutputFile( Path temporaryOutputFile )
	{
		try
		{
			Files.deleteIfExists( temporaryOutputFile );
		}
		catch( IOException ioe )
		{
			//ignore
		}
	}

	private class PositionedBufferedInputStream extends BufferedInputStream
	{
		long position;

		PositionedBufferedInputStream( InputStream in )
		{
			super( in, READ_WRITE_BUFFER_SIZE );
			position = 0L;
		}

		@Override
		public int read() throws IOException
		{
			int b = super.read();
			if( b > -1 )
			{
				position++;
			}
			return b;
		}

		@Override
		public int read( byte[] data, int off, int len ) throws IOException
		{
			position = off + len;
			return super.read( data, off, len );
		}

		long getPosition()
		{
			return position;
		}
	}

	private class BufferedRandomAccessFile extends RandomAccessFile
	{
		private final int bufferSize;

		private byte[] buffer;
		private int bufferIndex;

		BufferedRandomAccessFile(File file, String mode, int bufferSize ) throws FileNotFoundException
		{
			super( file, mode );
			this.bufferSize = bufferSize;
			bufferIndex = 0;
		}

		@Override
		public int read() throws IOException
		{
			if( bufferIndex == 0 )
			{
				buffer = new byte[bufferSize];
				read( buffer );
			}

			int data = buffer[bufferIndex++];

			if( bufferIndex == bufferSize )
			{
				bufferIndex = 0;
			}

			return data;
		}

		@Override
		public void seek( long position ) throws IOException
		{
			super.seek( position );
			bufferIndex = 0;
		}
	}
}
