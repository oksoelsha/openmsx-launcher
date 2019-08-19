/*
 * Copyright 2019 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.data.filter;

import java.util.Objects;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.VideoSource;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

/**
 * Filter for Video Source
 * 
 * @since v1.12
 * @author Sam Elsharif
 *
 */
@FilterDescriptor( type = FilterType.VIDEO_SOURCE )
final class VideoSourceFilter implements Filter
{
	@Value1Field
	private final VideoSource videoSource;

	VideoSourceFilter( VideoSource videoSource )
	{
		this.videoSource = Objects.requireNonNull( videoSource );
	}

	@Override
	public int hashCode()
	{
		return videoSource.hashCode();
	}

	@Override
	public boolean equals( Object obj )
	{
		boolean isSame;
		
		if( obj == null || !(obj instanceof VideoSourceFilter) )
		{
			isSame = false;
		}
		else
		{
			isSame = videoSource.equals( ((VideoSourceFilter)obj).videoSource );
		}

		return isSame;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.filter.Filter#isFiltered(info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public boolean isFiltered( Game game, RepositoryGame repositoryGame )
	{
		Objects.requireNonNull( game );

		boolean filtered = false;

		switch( videoSource )
		{
			case MSX:
				filtered = game.isConnectGFX9000();
				break;
			case GFX9000:
				filtered = !game.isConnectGFX9000();
				break;
			default:
				throw new RuntimeException( "Update filter if Medium contains an extra medium" );
		}

		return filtered;
	}
}
