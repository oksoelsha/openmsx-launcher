package info.msxlaunchers.openmsx.launcher.persistence.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FilterSetAlreadyExistsExceptionTest
{
	@Test
	public void testConstructor()
	{
		new FilterSetAlreadyExistsException( "name" );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorNullArg()
	{
		new FilterSetAlreadyExistsException( null );
	}

	@Test
	public void testGetName()
	{
		FilterSetAlreadyExistsException ex = new FilterSetAlreadyExistsException( "name" );

		assertEquals( "name", ex.getName() );
	}
}
