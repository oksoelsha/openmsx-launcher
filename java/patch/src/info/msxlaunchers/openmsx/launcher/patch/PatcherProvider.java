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

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * @since v1.9
 * @author Sam Elsharif
 *
 */
public class PatcherProvider
{
	private final Patcher ipsPatcher;
	private final Patcher upsPatcher;

	@Inject
	public PatcherProvider( @Named( "IPS" ) Patcher ipsPatcher, @Named( "UPS" ) Patcher upsPatcher )
	{
		this.ipsPatcher = ipsPatcher;
		this.upsPatcher = upsPatcher;
	}

	/**
	 * Return patcher instance (IPS or UPS)
	 * 
	 * @param patchMethod PatchMethod (IPS or UPS)
	 * @return Patcher instance (IPS or UPS)
	 */
	public Patcher get( PatchMethod patchMethod )
	{
		if( patchMethod.equals( PatchMethod.IPS ) )
		{
			return ipsPatcher;
		}
		else
		{
			return upsPatcher;
		}
	}
}
