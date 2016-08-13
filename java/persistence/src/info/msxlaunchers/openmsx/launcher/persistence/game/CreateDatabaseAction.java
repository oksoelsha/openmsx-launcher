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

import info.msxlaunchers.openmsx.common.log.LauncherLogger;
import info.msxlaunchers.openmsx.launcher.persistence.DefaultDatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class to create empty games database
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class CreateDatabaseAction extends TransactionalDatabaseOperation<Boolean>
{
	private static final String INSERT_DATABASE_STATEMENT = "INSERT INTO database (name) VALUES (?)";

	private final String database;

	CreateDatabaseAction( String database )
	{
		this.database = database;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DefaultDatabaseResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		try( PreparedStatement statement = connection.prepareStatement( INSERT_DATABASE_STATEMENT ) )
		{
			statement.setString( 1, database );

			statement.executeUpdate();
		}
		catch( SQLException se )
		{
			if( isDuplicateError( se ) )
			{
				throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.DATABASE_ALREADY_EXISTS, database ) );
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
