package info.msxlaunchers.openmsx.launcher.persistence.feed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class FeedMessageFilePersisterTest
{
	private final String feedMessageTestPath = System.getProperty( "user.dir" );

	@After
	public void cleanup() throws IOException
	{
		Path feedMessageTestFile = Paths.get( feedMessageTestPath, "feed/previousTopMessage" );
		Files.deleteIfExists( feedMessageTestFile );

		Path createdFiltersDirectory = Paths.get( feedMessageTestPath, "feed" );
		Files.deleteIfExists( createdFiltersDirectory );
	}

	@Test
	public void getMessage_messageSavedBySaveMessage_returnSavedMessage() throws IOException
	{
		FeedMessageFilePersister persister = new FeedMessageFilePersister( feedMessageTestPath );

		String message = "message to be persisted";

		persister.saveMessage( message );

		Assert.assertEquals( message, persister.getMessage() );
	}

	@Test( expected = IOException.class )
	public void saveMessage_directoryNotExist_throwException() throws IOException
	{
		FeedMessageFilePersister persister = new FeedMessageFilePersister( feedMessageTestPath );

		Path feedMessageTestFile = Paths.get( feedMessageTestPath, "feed/previousTopMessage" );
		Files.deleteIfExists( feedMessageTestFile );

		Path createdFiltersDirectory = Paths.get( feedMessageTestPath, "feed" );
		Files.deleteIfExists( createdFiltersDirectory );

		persister.saveMessage( "message" );
	}

	@Test( expected = IOException.class )
	public void getMessage_fileNotFound_throwException() throws IOException
	{
		Path feedMessageTestFile = Paths.get( feedMessageTestPath, "feed/previousTopMessage" );
		Files.deleteIfExists( feedMessageTestFile );

		FeedMessageFilePersister persister = new FeedMessageFilePersister( feedMessageTestPath );

		persister.getMessage();
	}

	@Test
	public void isMessagePersisted_noFileWasWrittenBefore_returnFalse() throws IOException
	{
		Path feedMessageTestFile = Paths.get( feedMessageTestPath, "feed/previousTopMessage" );
		Files.deleteIfExists( feedMessageTestFile );

		FeedMessageFilePersister persister = new FeedMessageFilePersister( feedMessageTestPath );

		Assert.assertFalse( persister.isMessagePersisted() );
	}
}
