package info.msxlaunchers.openmsx.launcher.feed;

import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

import info.msxlaunchers.openmsx.launcher.data.feed.FeedMessage;
import info.msxlaunchers.openmsx.launcher.persistence.feed.FeedMessagePersisterModule;

/**
 * This is an integration test that reads RSS feeds from the different sites. It prints the messages (or news) and prints
 * whether there are new updates since the last time the test ran.
 * The @ignore annotation will be added to prevent the test from running during the build
 */
public class FeedServiceTest
{
	@Test @Ignore
	public void test() throws InterruptedException, IOException
	{
		Injector injector = Guice.createInjector( new TestModule(),
				new FeedServiceModule(), new FeedMessagePersisterModule() );

		FeedService feedService = injector.getInstance( FeedService.class );

		feedService.start();

		//let it run for 1 min
		Thread.sleep(60000);

		System.out.println( "New messages found: " + feedService.isNewMessagesFound() );
		System.out.println();

		List<FeedMessage> messages = feedService.getMessages();
		for( FeedMessage message: messages )
		{
			System.out.println(message.getPubDate() + " " + message.getTitle() + " [" + message.getFeedSiteName() + "]");
		}
		//let it run for 5 seconds
		Thread.sleep(5000);
	}

	private class TestModule extends AbstractModule
	{
		@Override
		protected void configure()
		{
			bind( String.class ).annotatedWith( Names.named( "UserDataDirectory" ) ).toInstance( System.getProperty( "user.dir" ) );
		}
	}
}
