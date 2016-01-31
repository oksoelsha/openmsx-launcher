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

/**
 * @since v1.0
 * @author Sam Elsharif
 *
 */
@SuppressWarnings("serial")
public class LauncherException extends Exception
{
	private final LauncherExceptionCode code;
	private final String additionalString;

	public LauncherException( LauncherExceptionCode code )
	{
		this( code, null );
	}

	public LauncherException( LauncherExceptionCode code, String additionalString )
	{
		this.code = Objects.requireNonNull( code );
		this.additionalString = additionalString;
	}

	public LauncherExceptionCode getCode()
	{
		return code;
	}

	public String getCodeAsString()
	{
		return code.toString();
	}

	public String getAdditionalString()
	{
		return additionalString;
	}
}
