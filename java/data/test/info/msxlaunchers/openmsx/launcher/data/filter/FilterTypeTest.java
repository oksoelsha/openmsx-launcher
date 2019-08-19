package info.msxlaunchers.openmsx.launcher.data.filter;

import org.junit.Test;

public class FilterTypeTest
{
	@Test
	public void testMediumValues()
	{
		//the following is to get complete code coverage even if there's no value in this test
		FilterType.valueOf( FilterType.COMPANY.toString() );
		FilterType.valueOf( FilterType.COUNTRY.toString() );
		FilterType.valueOf( FilterType.GENERATION.toString() );
		FilterType.valueOf( FilterType.GENRE.toString() );
		FilterType.valueOf( FilterType.MEDIUM.toString() );
		FilterType.valueOf( FilterType.SIZE.toString() );
		FilterType.valueOf( FilterType.SOUND.toString() );
		FilterType.valueOf( FilterType.YEAR.toString() );
		FilterType.valueOf( FilterType.VIDEO_SOURCE.toString() );
	}
}
