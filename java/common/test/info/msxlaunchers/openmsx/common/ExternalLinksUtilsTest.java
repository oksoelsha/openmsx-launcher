package info.msxlaunchers.openmsx.common;

import org.junit.Assert;
import org.junit.Test;

public class ExternalLinksUtilsTest
{
	@Test
	public void test_whenInstantiateClass_thenGetAnInstance()
	{
		//this is not a useful test. It's just meant to get code coverage
		new ExternalLinksUtils();
	}

	@Test
	public void test_whenIsMSXGenerationIdValid_thenReturnCorrectResult()
	{
		Assert.assertTrue( ExternalLinksUtils.isMSXGenerationIdValid( 1 ) );
		Assert.assertTrue( ExternalLinksUtils.isMSXGenerationIdValid( 10000-1 ) );
		Assert.assertFalse( ExternalLinksUtils.isMSXGenerationIdValid( 0 ) );
		Assert.assertFalse( ExternalLinksUtils.isMSXGenerationIdValid( 10000 ) );
	}

	@Test
	public void test_whenGetMSXGenerationURL_thenReturnMSXGenURL()
	{
		Assert.assertEquals( "http://www.generation-msx.nl/msxdb/softwareinfo/34", ExternalLinksUtils.getMSXGenerationURL( 34 ) );
	}

	@Test
	public void test_whenGetYouTubeURL_thenReturnYouTubeSearchURL()
	{
		Assert.assertEquals( "https://www.youtube.com/results?search_query=MSX+name1+name2", ExternalLinksUtils.getYouTubeSearchURL( "name1 name2" ) );
		Assert.assertEquals( "https://www.youtube.com/results?search_query=MSX+name1+%26+name2", ExternalLinksUtils.getYouTubeSearchURL( "name1 & name2" ) );
	}
}
