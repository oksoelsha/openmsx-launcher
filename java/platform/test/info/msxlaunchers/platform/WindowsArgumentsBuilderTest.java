package info.msxlaunchers.platform;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WindowsArgumentsBuilderTest
{
	@Test( expected = NullPointerException.class )
	public void testForNullAppendArg()
	{
		WindowsArgumentsBuilder builder = new WindowsArgumentsBuilder();

		builder.append( null );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullAppendIfValueDefinedArg()
	{
		WindowsArgumentsBuilder builder = new WindowsArgumentsBuilder();

		builder.appendIfValueDefined( null, "a" );
	}

	@Test
	public void testArgumentsBuilding()
	{
		WindowsArgumentsBuilder builder = new WindowsArgumentsBuilder();

		builder.append( "arg1" );
		builder.appendIfValueDefined( "switch1", "value1" );
		builder.appendIfValueDefined( "switch2", null );
		builder.appendIfValueDefined( "switch3", "" );
		builder.appendIfValueDefined( "switch4", "value 4" );
		builder.append( "arg2" );

		List<String> args = builder.getArgumentList();

		assertEquals( "arg1", args.get( 0 ) );
		assertEquals( "switch1", args.get( 1 ) );
		assertEquals( "\"value1\"", args.get( 2 ) );
		assertEquals( "switch4", args.get( 3 ) );
		assertEquals( "\"value 4\"", args.get( 4 ) );
		assertEquals( "arg2", args.get( 5 ) );
		assertEquals( 6, args.size() );
	}

	@Test
	public void testGetArgumentsMustClearInternalBuffer()
	{
		WindowsArgumentsBuilder builder = new WindowsArgumentsBuilder();

		builder.append( "arg1" );

		List<String> args = builder.getArgumentList();

		assertEquals( "arg1", args.get( 0 ) );
		assertEquals( 1, args.size() );

		builder.append( "arg2" );

		args = builder.getArgumentList();

		assertEquals( "arg2", args.get( 0 ) );
		assertEquals( 1, args.size() );
	}
}
