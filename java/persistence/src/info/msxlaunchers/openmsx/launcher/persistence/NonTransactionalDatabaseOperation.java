/*
 * Copyright 2015 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.persistence;

import java.sql.Connection;

/**
 * Abstract implementation of <code>AbstractDatabaseOperation<code> that executes non-transactional
 * database operations
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
abstract public class NonTransactionalDatabaseOperation<E> extends AbstractDatabaseOperation<E>
{
	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.AbstractDatabaseOperation#executeOperation(java.sql.Connection)
	 * This implementation executes the operation without starting a transaction
	 */
	@Override
	public DatabaseResponse<E> executeOperation( Connection connection ) throws LauncherPersistenceException
	{
		return executeNonTransactionalOperation( connection );
	}

	abstract public DatabaseResponse<E> executeNonTransactionalOperation( Connection connection ) throws LauncherPersistenceException;
}
