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
import info.msxlaunchers.openmsx.launcher.patch.Patcher;
import info.msxlaunchers.openmsx.launcher.ui.view.PatcherView;

/**
 * IPS-specific implementation of <code>PatcherPresenter</code>
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
class IPSPatcherPresenter implements PatcherPresenter
{
	private final PatcherView view;
	private final Patcher patcher;

	@Inject
	public IPSPatcherPresenter( PatcherView view, Patcher patcher )
	{
		this.view = view;
		this.patcher = patcher;
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
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.PatcherPresenter#onRequestPatchFileAction(java.lang.String, java.lang.String, boolean, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public boolean onRequestPatchFileAction( String patchFile, String fileToPatch, boolean useTargetFile, String targetFile,
			boolean verifyChecksum, String checksum ) throws LauncherException
	{
		Path patchFilePath = Paths.get( checkIfEmpty( patchFile, LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE ) );
		validateThatFileExists( patchFilePath );

		Path fileToPatchPath = Paths.get( checkIfEmpty( fileToPatch, LauncherExceptionCode.ERR_CANNOT_LOCATE_FILE ) );
		validateThatFileExists( fileToPatchPath );

		Path targetFilePath;
		if( useTargetFile )
		{
			targetFilePath = Paths.get( targetFile );
			if( Files.exists( targetFilePath ) && !view.confirmTargetFileReplacement() )
			{
				//the target file exists and the user answered No on replacing the file
				return false;
			}
		}
		else
		{
			targetFilePath = null;
		}

		String checksumValue;
		if( verifyChecksum )
		{
			checksumValue = checkIfEmpty( checksum, LauncherExceptionCode.ERR_EMPTY_CHECKSUM );
		}
		else
		{
			checksumValue = null;
		}

		try
		{
			patcher.patch( fileToPatchPath, patchFilePath, targetFilePath, checksumValue );
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
