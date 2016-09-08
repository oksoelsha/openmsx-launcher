/*
 * Copyright 2016 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.log;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import info.msxlaunchers.openmsx.common.Utils;

/**
 * Logger class 
 * 
 * @since v1.8
 * @author Sam Elsharif
 */
public final class LauncherLogger
{
	private final static String logDirectory = Utils.getUserDataDirectory();

	private final static String MESSAGE_LOGGER_NAME = "MessageLogger";
	private final static String EXCEPTION_LOGGER_NAME = "ExceptionLogger";
	public final static String MESSAGE_LOG_FILENAME = "message.log";
	private final static String EXCEPTION_LOG_FILENAME = "exception.log";
	private final static int MAX_LOG_SIZE = 1024 * 50;
	private final static int MAX_LOGS_NUMBER = 2;
	private final static String LINE_SEPARATOR = System.getProperty( "line.separator" );

	private final static Logger messageLogger = Logger.getLogger( MESSAGE_LOGGER_NAME );
	private final static Logger exceptionLogger = Logger.getLogger( EXCEPTION_LOGGER_NAME );

	static
	{
		Handler messageHandler = null;
		Handler exceptionHandler = null;

		try
		{
			messageHandler = new FileHandler( new File( logDirectory, MESSAGE_LOG_FILENAME ).toString(), MAX_LOG_SIZE, MAX_LOGS_NUMBER, true );
			messageHandler.setEncoding( "UTF-8" );
			exceptionHandler = new FileHandler( new File( logDirectory, EXCEPTION_LOG_FILENAME ).toString(), MAX_LOG_SIZE, MAX_LOGS_NUMBER, true );
			exceptionHandler.setEncoding( "UTF-8" );
		}
		catch( SecurityException | IOException ex )
		{
			throw new RuntimeException( ex );
		}

		messageHandler.setFormatter( new MessageFormatter() );
		messageLogger.addHandler( messageHandler );
		messageLogger.setUseParentHandlers( false );

		exceptionHandler.setFormatter( new SimpleFormatter() );
		exceptionLogger.addHandler( exceptionHandler );
		exceptionLogger.setUseParentHandlers( false );
	}

	/**
	 * Log messages to the message log without specifying originating class. Currently this is only used to log game launch events
	 * 
	 * @param event Log event
	 * @param message Message to log
	 */
	public static void logMessage( LogEvent event, String message )
	{
		logMessage( event + " " + message );
	}

	/**
	 * Log messages to the message log without specifying originating class. Currently this is only used to log game launch events
	 * 
	 * @param message Message to log
	 */
	public static void logMessage( String message )
	{
		messageLogger.info( message );
	}

	/**
	 * Log exceptions to the exceptions log. these messages contain the class name where they originated
	 * 
	 * @param object Object of the log caller to get the class from and log it
	 * @param ex Exception to log
	 */
	public static void logException( Object object, Exception ex )
	{
		exceptionLogger.log( Level.SEVERE, object.getClass().getName() + " - " + ex.getMessage(), ex ); 
	}

	private static class MessageFormatter extends Formatter
	{
		@Override
		public String format( LogRecord record )
		{
			return new Date( record.getMillis() ) + ": " + record.getMessage() + LINE_SEPARATOR;
		}
	}
}
