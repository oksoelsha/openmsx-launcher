package info.msxlaunchers.openmsx.launcher.data.filter;

import org.junit.Test;

public class FilterParameterTest
{
	@Test
	public void testMediumValues()
	{
		//the following is to get complete code coverage even if there's no value in this test
		FilterParameter.valueOf( FilterParameter.EQUAL.toString() );
		FilterParameter.valueOf( FilterParameter.EQUAL_OR_LESS.toString() );
		FilterParameter.valueOf( FilterParameter.EQUAL_OR_GREATER.toString() );
		FilterParameter.valueOf( FilterParameter.LESS.toString() );
		FilterParameter.valueOf( FilterParameter.GREATER.toString() );
		FilterParameter.valueOf( FilterParameter.BETWEEN_INCLUSIVE.toString() );
	}
}
