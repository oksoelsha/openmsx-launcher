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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import info.msxlaunchers.openmsx.common.FileTypeUtils;

/**
 * Abstract implementation for <code>Patcher</Code> interface that contains common methods for all implementations
 * 
 * @author Sam Elsharif
 * @since v1.9
 */
abstract class AbstractPatcher implements Patcher
{
	private final String TEMP_FILENAME = "tempFile.tmp";

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.patch.Patcher#patch(java.nio.file.Path, java.nio.file.Path, java.nio.file.Path, boolean, java.lang.String)
	 */
	@Override
	public final void patch( Path fileToPatch, Path patchFile, Path targetFile, boolean skipChecksumValidation, String checksum )
			throws PatchException
	{
		Objects.requireNonNull( fileToPatch );
		Objects.requireNonNull( patchFile );

		Path realFileToPatch = null;
		try
		{
			realFileToPatch = unzipFileToPatchIfNeeded( fileToPatch, targetFile == null );

			performValidation( realFileToPatch, patchFile, skipChecksumValidation, checksum );

			patchFileData( realFileToPatch, patchFile, targetFile, skipChecksumValidation );
		}
		finally
		{
			cleanupTemporaryFile( fileToPatch, realFileToPatch );
		}
	}

	/**
	 * Perform initial validation which depends on the patch method
	 * 
	 * @param fileToPatch
	 * @param patchFile
	 * @param skipChecksumValidation
	 * @param checksum
	 * @throws PatchException
	 */
	abstract protected void performValidation( Path fileToPatch, Path patchFile, boolean skipChecksumValidation, String checksum ) throws PatchException;

	/**
	 * Start patching
	 * 
	 * @param fileToPatch
	 * @param patchFile
	 * @param targetFile
	 * @param skipChecksumValidation
	 * @throws PatchException
	 */
	abstract void patchFileData( Path fileToPatch, Path patchFile, Path targetFile, boolean skipChecksumValidation ) throws PatchException;

	protected void validateFileSize( Path file, int minLimit, int maxLimit, PatchExceptionIssue exceptionIssue ) throws PatchException
	{
		try
		{
			long size = Files.size( file );
			if( size < minLimit || size > maxLimit )
			{
				throw new PatchException( exceptionIssue );
			}
		}
		catch( IOException ioe )
		{
			throw new PatchException( PatchExceptionIssue.IO );
		}
	}

	protected boolean isByteSequenceEqualToString( byte[] data, int start, byte[] stringSequence )
	{
		return Arrays.equals( Arrays.copyOfRange( data, start, start + stringSequence.length ), stringSequence );
	}

	private Path unzipFileToPatchIfNeeded( Path fileToPatch, boolean patchSourceDirectly ) throws PatchException
	{
		if( FileTypeUtils.isZIP( fileToPatch.toFile() ) )
		{
			if( patchSourceDirectly )
			{
				//we don't allow patching the source directly if the file to patch is ZIP
				throw new PatchException( PatchExceptionIssue.ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY );
			}

			try
			{
				Path tempFile = Files.createTempFile( TEMP_FILENAME, null );

				try( ZipFile zip = new ZipFile( fileToPatch.toFile() ) )
				{
					ZipEntry firstZipEntry = zip.entries().nextElement();

					try( InputStream inputStream = zip.getInputStream( firstZipEntry ) )
					{
						Files.copy( inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING );
					}
				}
				catch( IOException e )
				{
					throw new PatchException( PatchExceptionIssue.IO );
				}

				return tempFile;
			}
			catch( IOException ioe )
			{
				throw new PatchException( PatchExceptionIssue.IO );
			}
		}
		else
		{
			return fileToPatch;
		}
	}

	private void cleanupTemporaryFile( Path file, Path temporaryUnzippedFile )
	{
		if( FileTypeUtils.isZIP( file.toFile() ) && temporaryUnzippedFile != null )
		{
			try
			{
				Files.deleteIfExists( temporaryUnzippedFile );
			}
			catch( IOException ioe )
			{
				//ignore
			}
		}
	}
}
