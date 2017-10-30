package info.msxlaunchers.openmsx.launcher.data.feed;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FeedMessageTest
{
	@Test
	public void testConstructor()
	{
		FeedMessage feedMessage1 = new FeedMessage( "title", "link", "Thu, 19 Oct 2017 20:06:48 +0000", "siteName", "siteUrl" );

		assertEquals( "title", feedMessage1.getTitle() );
		assertEquals( "link", feedMessage1.getLink() );
		assertEquals( "2017-10-19", feedMessage1.getPubDate().toString() );
		assertEquals( "19-10", feedMessage1.getPubDateDisplayName() );
		assertEquals( "siteName", feedMessage1.getFeedSiteName() );
		assertEquals( "siteUrl", feedMessage1.getFeedSiteUrl() );

		FeedMessage feedMessage2 = new FeedMessage( "title", "link", "Wed, 5 Oct 2016 14:36:23 +0000", "siteName", "siteUrl" );

		assertEquals( "05-10", feedMessage2.getPubDateDisplayName() );
	}

	@Test
	public void testToString()
	{
		FeedMessage feedMessage = new FeedMessage( "title", "link", "Thu, 19 Oct 2017 20:06:48 +0000", "siteName", "siteUrl" );

		assertEquals( "[title=title, link=link, pubDate=2017-10-19, feedSiteName=siteName, feedSiteUrl=siteUrl]", feedMessage.toString() );
	}
}
