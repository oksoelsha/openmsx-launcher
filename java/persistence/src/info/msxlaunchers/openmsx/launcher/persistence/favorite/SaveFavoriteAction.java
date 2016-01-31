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
package info.msxlaunchers.openmsx.launcher.persistence.favorite;

import info.msxlaunchers.openmsx.launcher.data.favorite.Favorite;
import info.msxlaunchers.openmsx.launcher.persistence.DefaultDatabaseResponse;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class to save new favorite
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
final class SaveFavoriteAction extends TransactionalDatabaseOperation<Boolean>
{
	private static final String INSERT_FAVORITE_STATEMENT = "INSERT INTO favorite (IDGAME) VALUES " +
			"(SELECT game.ID FROM game join database on game.IDDB=database.ID and game.name=? and database.name=?)";
	private static final String NOT_FOUND_ERROR_CODE = "23502";

	private final Favorite favorite;

	SaveFavoriteAction( Favorite favorite )
	{
		this.favorite = favorite;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.TransactionalDatabaseOperation#executeTransactionalOperation(java.sql.Connection)
	 */
	@Override
	public DefaultDatabaseResponse executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException
	{
		try( PreparedStatement statement = connection.prepareStatement( INSERT_FAVORITE_STATEMENT ) )
		{
			statement.setString( 1, favorite.getGameName() );
			statement.setString( 2, favorite.getDatabase() );

			statement.executeUpdate();
		}
		catch( SQLException se )
		{
			if( isDuplicateError( se ) )
			{
				throwEncapsulatingException( new FavoritePersistenceException( FavoritePersistenceExceptionIssue.FAVORITE_ALREADY_EXISTS ) );
			}
			else if( se.getSQLState().equals( NOT_FOUND_ERROR_CODE ) )
			{
				throwEncapsulatingException( new FavoritePersistenceException( FavoritePersistenceExceptionIssue.GAME_OR_DATABASE_NOT_FOUND ) );
			}
			else
			{
				throwEncapsulatingException( new FavoritePersistenceException( FavoritePersistenceExceptionIssue.IO ) );
			}
		}

		return new DefaultDatabaseResponse();
	}
}
