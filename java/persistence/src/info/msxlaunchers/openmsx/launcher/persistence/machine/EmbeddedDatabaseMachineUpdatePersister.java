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
package info.msxlaunchers.openmsx.launcher.persistence.machine;

import java.util.Objects;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;

/**
 * Implementation of the <code>MachineUpdatePersister</code> interface that updates machines in an embedded database
 * 
 * @since v1.9
 * @author Sam Elsharif
 *
 */
final class EmbeddedDatabaseMachineUpdatePersister implements MachineUpdatePersister
{
	private final String databaseFullPath;

	@Inject
	EmbeddedDatabaseMachineUpdatePersister( @Named("EmbeddedDatabaseFullPath") String databaseFullPath )
	{
		this.databaseFullPath = databaseFullPath;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.machine.MachineUpdatePersister#update(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int update( String to, String from, String database ) throws MachineUpdatePersistenceException
	{
		Objects.requireNonNull( to );

		try
		{
			return new UpdateMachineAction( to, from, database ).execute( databaseFullPath ).getResult();
		}
		catch( LauncherPersistenceException lpe )
		{
			throw (MachineUpdatePersistenceException)lpe.getException();
		}
	}
}
