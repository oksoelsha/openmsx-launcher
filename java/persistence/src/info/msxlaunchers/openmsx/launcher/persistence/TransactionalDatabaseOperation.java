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

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Abstract implementation of <code>AbstractDatabaseOperation<code> that executes transactional
 * database operations
 * 
 * @since v1.4
 * @author Sam Elsharif
 *
 */
abstract public class TransactionalDatabaseOperation<E> extends AbstractDatabaseOperation<E>
{
	private static final String DUPLICATE_ERROR_CODE = "23505";
	private static final int MAXIMUM_GAME_NAME_LENGTH = 128;

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.persistence.game.AbstractDatabaseOperation#executeOperation(java.sql.Connection)
	 * This implementation starts a transaction, executes the operation and commits it
	 */
	@Override
	public DatabaseResponse<E> executeOperation( Connection connection ) throws LauncherPersistenceException
	{
		beginTransaction( connection );

		DatabaseResponse<E> response = executeTransactionalOperation( connection );

		commitTransaction( connection );

		return response;
	}

	abstract public DatabaseResponse<E> executeTransactionalOperation( Connection connection ) throws LauncherPersistenceException;

	protected boolean isDuplicateError( SQLException se )
	{
		return se.getSQLState().equals( DUPLICATE_ERROR_CODE );
	}

	protected void setGameStatementFields( PreparedStatement statement, Game game, long databaseId ) throws SQLException
	{
		statement.setString( 1, game.getName().substring( 0, Math.min( game.getName().length(), MAXIMUM_GAME_NAME_LENGTH ) ) );
		statement.setString( 2, game.getInfo() );
		statement.setString( 3, game.getMachine() );
		statement.setString( 4, game.getRomA() );
		statement.setString( 5, game.getExtensionRom() );
		statement.setString( 6, game.getRomB() );
		statement.setString( 7, game.getDiskA() );
		statement.setString( 8, game.getDiskB() );
		statement.setString( 9, game.getTape() );
		statement.setString( 10, game.getHarddisk() );
		statement.setString( 11, game.getLaserdisc() );
		statement.setString( 12, game.getTclScript() );
		statement.setBoolean( 13, game.isMSX() );
		statement.setBoolean( 14, game.isMSX2() );
		statement.setBoolean( 15, game.isMSX2Plus() );
		statement.setBoolean( 16, game.isTurboR() );
		statement.setBoolean( 17, game.isPSG() );
		statement.setBoolean( 18, game.isSCC() );
		statement.setBoolean( 19, game.isSCCI() );
		statement.setBoolean( 20, game.isPCM() );
		statement.setBoolean( 21, game.isMSXMUSIC() );
		statement.setBoolean( 22, game.isMSXAUDIO() );
		statement.setBoolean( 23, game.isMoonsound() );
		statement.setBoolean( 24, game.isMIDI());
		statement.setInt( 25, getGenreEnumValue( game.getGenre1() ) );
		statement.setInt( 26, getGenreEnumValue( game.getGenre2() ) );
		statement.setInt( 27, game.getMsxGenID() );
		statement.setString( 28, game.getScreenshotSuffix() );
		statement.setString( 29, game.getSha1Code() );
		statement.setLong( 30, game.getSize() );
		statement.setLong( 31, databaseId );
	}

	protected int getGenreEnumValue( Genre genre )
	{
		int value = 0;

		if( genre != null )
		{
			value = genre.getValue();
		}

		return value;
	}

	private void beginTransaction( Connection connection ) throws LauncherPersistenceException
	{
		try
		{
			connection.setAutoCommit( false );
		}
		catch( SQLException se )
		{
			throw new LauncherPersistenceException();
		}
	}

	private void commitTransaction( Connection connection ) throws LauncherPersistenceException
	{
		try
		{
			connection.commit();
		}
		catch( SQLException se )
		{
			throw new LauncherPersistenceException();
		}
	}
}
