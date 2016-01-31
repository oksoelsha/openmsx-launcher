package info.msxlaunchers.openmsx.launcher.persistence.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FilterSetNotFoundExceptionTest
{
	@Test
	public void testConstructor()
	{
		new FilterSetNotFoundException( "name" );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new FilterSetNotFoundException( null );
	}

	@Test
	public void testGetName()
	{
		FilterSetNotFoundException ex = new FilterSetNotFoundException( "name" );

		assertEquals( "name", ex.getName() );
	}
}
