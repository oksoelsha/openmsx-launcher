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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.patch.PatchException;
import info.msxlaunchers.openmsx.launcher.patch.PatchExceptionIssue;
import info.msxlaunchers.openmsx.launcher.patch.PatchMethod;
import info.msxlaunchers.openmsx.launcher.patch.PatcherProvider;
import info.msxlaunchers.openmsx.launcher.ui.view.PatcherView;

/**
 * IPS-specific implementation of <code>PatcherPresenter</code>
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
class PatcherPresenterImpl implements PatcherPresenter
{
	private final PatcherView view;
	private final PatcherProvider patcherProvider;

	@Inject
	public PatcherPresenterImpl( PatcherView view, PatcherProvider patcherProvider )
	{
		this.view = view;
		this.patcherProvider = patcherProvider;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.PatcherPresenter#onRequestIPSPatcherScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestIPSPatcherScreen( Language currentLanguage, boolean currentRightToLeft )
	{
		view.displayScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.PatcherPresenter#onRequestPatchFileActionForIPS(java.lang.String, java.lang.String, boolean, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public boolean onRequestPatchFileActionForIPS( String patchFile, String fileToPatch, boolean useTargetFile, String targetFile,
			boolean skipChecksumValidation, String checksum ) throws LauncherException
	{
		return patch( patchFile, fileToPatch, useTargetFile, targetFile, skipChecksumValidation, checksum, PatchMethod.IPS );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.PatcherPresenter#onRequestPatchFileActionForUPS(java.lang.String, java.lang.String, boolean, java.lang.String, boolean)
	 */
	@Override
	public boolean onRequestPatchFileActionForUPS( String patchFile, String fileToPatch, boolean useTargetFile, String targetFile,
			boolean skipChecksumValidation ) throws LauncherException
	{
		return patch( patchFile, fileToPatch, useTargetFile, targetFile, skipChecksumValidation, null, PatchMethod.UPS );
	}

	private boolean patch( String patchFile, String fileToPatch, boolean useTargetFile, String targetFile,
			boolean skipChecksumValidation, String checksum, PatchMethod patchMethod ) throws LauncherException
	{
		Path patchFilePath = Paths.get( checkIfEmpty( patchFile, LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE ) );
		validateThatFileExists( patchFilePath );

		Path fileToPatchPath = Paths.get( checkIfEmpty( fileToPatch, LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE ) );
		validateThatFileExists( fileToPatchPath );

		String checksumValue = null;
		if( patchMethod == PatchMethod.IPS && !skipChecksumValidation )
		{
			checksumValue = checkIfEmpty( checksum, LauncherExceptionCode.ERR_EMPTY_CHECKSUM );
		}

		Path targetFilePath;
		if( useTargetFile )
		{
			checkIfEmpty( targetFile, LauncherExceptionCode.ERR_IO );
			targetFilePath = Paths.get( targetFile );
			if( Files.exists( targetFilePath ) && !view.confirmTargetFileReplacement() )
			{
				//the target file exists and the user answered No to replacing the file
				return false;
			}
		}
		else
		{
			targetFilePath = null;
		}

		try
		{
			patcherProvider.get( patchMethod ).patch( fileToPatchPath, patchFilePath, targetFilePath, skipChecksumValidation, checksumValue );
		} 
		catch( PatchException pe )
		{
			PatchExceptionIssue issue = pe.getIssue();
			if( issue.equals( PatchExceptionIssue.FILE_TO_PATCH_NOT_PATCHABLE ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_FILE_TO_PATCH_NOT_PATCHABLE );
			}
			else if( issue.equals( PatchExceptionIssue.INVALID_PATCH_FILE ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_INVALID_PATCH_FILE );
			}
			else if( issue.equals( PatchExceptionIssue.SOURCE_FILE_CHECKSUM_NOT_MATCH ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_SOURCE_FILE_CHECKSUM_NOT_MATCH );
			}
			else if( issue.equals( PatchExceptionIssue.ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY );
			}
			else if( issue.equals( PatchExceptionIssue.TARGET_FILE_CANNOT_WRITE ) || issue.equals( PatchExceptionIssue.IO ) )
			{
				throw new LauncherException( LauncherExceptionCode.ERR_IO );
			}
		}

		return true;
	}

	private void validateThatFileExists( Path filePath ) throws LauncherException
	{
		if( Files.notExists( filePath ) )
		{
			throw new LauncherException( LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE, filePath.toString() );
		}
	}

	private String checkIfEmpty( String string, LauncherExceptionCode errorCode ) throws LauncherException
	{
		if( Utils.isEmpty( string ) )
		{
			throw new LauncherException( errorCode );
		}

		return string;
	}
}
