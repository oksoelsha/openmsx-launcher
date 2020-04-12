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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

import com.google.inject.Inject;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.decompressor.DecompressionException;
import info.msxlaunchers.openmsx.launcher.decompressor.Decompressor;
import info.msxlaunchers.openmsx.launcher.ui.view.LHADecompressorView;

/**
 * Implementation of <code>LHADecompressorPresenter</code>
 * 
 * @since v1.14
 * @author Sam Elsharif
 *
 */
class LHADecompressorPresenterImpl implements LHADecompressorPresenter
{
	private final LHADecompressorView view;
	private final Decompressor decompressor;

	@Inject
	public LHADecompressorPresenterImpl( LHADecompressorView view, Decompressor decompressor )
	{
		this.view = view;
		this.decompressor = decompressor;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.LHADecompressorPresenter#onRequestLHADecompressorScreen(info.msxlaunchers.openmsx.launcher.data.settings.constants.Language, boolean)
	 */
	@Override
	public void onRequestLHADecompressorScreen( Language currentLanguage, boolean currentRightToLeft )
	{
		view.displayScreen( currentLanguage, currentRightToLeft );
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.ui.presenter.LHADecompressPresenter#onRequestLHADecompresAction(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void onRequestLHADecompressAction( String fileToDecompress, String targetDirectory, boolean onlyMSXFiles )
			throws LauncherException
	{
		try
		{
			decompressor.decompress( fileToDecompress, targetDirectory, onlyMSXFiles );
		}
		catch( DecompressionException de )
		{
			// TODO Auto-generated catch block
			de.printStackTrace();
		}
	}
}
