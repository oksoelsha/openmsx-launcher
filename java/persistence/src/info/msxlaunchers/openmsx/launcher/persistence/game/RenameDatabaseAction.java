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
package info.msxlaunchers.openmsx.launcher.persistence.game;

import info.msxlaunchers.openmsx.launcher.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.DefaultDatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class to rename games database
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class RenameDatabaseAction extends TransactionalDatabaseOperation<Boolean>
{
	private static final String RENAME_DATABASE_STATEMENT = "UPDATE database SET name=? WHERE name=?";

	private final String oldDatabase;
	private final String newDatabase;

	RenameDatabaseAction( String oldDatabase, String newDatabase )
	{
		this.oldDatabase = oldDatabase;
		this.newDatabase = newDatabase;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DefaultDatabaseResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		try( PreparedStatement statement = connection.prepareStatement( RENAME_DATABASE_STATEMENT ) )
		{
			statement.setString( 1, newDatabase );
			statement.setString( 2, oldDatabase );

			int updateCount = statement.executeUpdate();

			if( updateCount != 1 )
			{
				throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_NOT_FOUND, oldDatabase ) );
			}
		}
		catch( SQLException se )
		{
			if( isDuplicateError( se ) )
			{
				throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS, newDatabase ) );
			}
			else
			{
				LauncherLogger.logException( this, se );

				throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.IO ) );
			}
		}

		return new DefaultDatabaseResponse();
	}
}
