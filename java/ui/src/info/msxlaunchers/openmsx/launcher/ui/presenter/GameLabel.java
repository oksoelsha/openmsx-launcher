/*
 * Copyright 2013 Sam Elsharif
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

import java.util.Objects;

import info.msxlaunchers.openmsx.launcher.data.game.constants.Medium;

/**
 * Class that holds game name and medium to be used by the View
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public final class GameLabel implements Comparable<GameLabel>
{
	private final String name;
	private final Medium medium;

	public GameLabel( String name, Medium medium )
	{
		this.name = Objects.requireNonNull( name );
		this.medium = Objects.requireNonNull( medium );
	}

	public String getName() { return name; }
	public Medium getMedium() { return medium; }

	@Override
	public boolean equals( Object obj )
	{
		boolean isSame;
		
		if( obj == null || !(obj instanceof GameLabel) )
		{
			isSame = false;
		}
		else
		{
			isSame = name.equals( ((GameLabel)obj).getName() );
		}

		return isSame;
	}

	@Override
	 public int hashCode()
	{
		   return name.hashCode();
	}
	
	@Override
	public int compareTo( GameLabel gameLabel )
	{
		Objects.requireNonNull( gameLabel );
			
		return name.compareTo( gameLabel.getName() );
	}
}