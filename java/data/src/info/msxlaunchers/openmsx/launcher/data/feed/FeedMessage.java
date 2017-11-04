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
package info.msxlaunchers.openmsx.launcher.data.feed;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Data object that represents an individual feed message
 * 
 * @since v1.10
 * @author Sam Elsharif
 *
 */
public class FeedMessage
{
	private final String title;
	private final String link;
	private final LocalDate pubDate;
	private final String feedSiteName;
	private final String feedSiteUrl;

	public FeedMessage( String title, String link, String pubDate, String feedSiteName, String feedSiteUrl )
	{
		this.title = title;
		this.link = link;
		this.pubDate = LocalDate.parse( pubDate, DateTimeFormatter.RFC_1123_DATE_TIME );
		this.feedSiteName = feedSiteName;
		this.feedSiteUrl = feedSiteUrl;
	}

	public String getTitle()
	{
		return title;
	}

	public String getLink()
	{
		return link;
	}

	public LocalDate getPubDate()
	{
		return pubDate;
	}

	public String getPubDateDisplayName()
	{
		return getPaddedNumber( pubDate.getDayOfMonth() ) + "-" + getPaddedNumber( pubDate.getMonthValue() );
	}

	public String getFeedSiteName()
	{
		return feedSiteName;
	}

	public String getFeedSiteUrl()
	{
		return feedSiteUrl;
	}

	@Override
	public String toString()
	{
		return "[title=" + title + ", link=" + link + ", pubDate=" + pubDate  +
				", feedSiteName=" + feedSiteName + ", feedSiteUrl=" + feedSiteUrl + "]";
	}

	private String getPaddedNumber( int number )
	{
		return number < 10 ? "0" + number : "" + number;
	}
}