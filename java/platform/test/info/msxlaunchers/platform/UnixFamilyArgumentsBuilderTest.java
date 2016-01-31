package info.msxlaunchers.platform;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnixFamilyArgumentsBuilderTest
{
	@Test( expected = NullPointerException.class )
	public void testForNullAppendArg()
	{
		UnixFamilyArgumentsBuilder builder = new UnixFamilyArgumentsBuilder();

		builder.append( null );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullAppendIfValueDefinedArg()
	{
		UnixFamilyArgumentsBuilder builder = new UnixFamilyArgumentsBuilder();

		builder.appendIfValueDefined( null, "a" );
	}

	@Test
	public void testArgumentsBuilding()
	{
		UnixFamilyArgumentsBuilder builder = new UnixFamilyArgumentsBuilder();

		builder.append( "arg1" );
		builder.appendIfValueDefined( "switch1", "value1" );
		builder.appendIfValueDefined( "switch2", null );
		builder.appendIfValueDefined( "switch3", "" );
		builder.appendIfValueDefined( "switch4", "value 4" );
		builder.append( "arg2" );

		List<String> args = builder.getArgumentList();

		assertEquals( "bash", args.get( 0 ) );
		assertEquals( "-c", args.get( 1 ) );
		assertEquals( " arg1 switch1 \"value1\" switch4 \"value 4\" arg2", args.get( 2 ) );
		assertEquals( 3, args.size() );
	}

	@Test
	public void testGetArgumentsMustClearInternalBuffer()
	{
		UnixFamilyArgumentsBuilder builder = new UnixFamilyArgumentsBuilder();

		builder.append( "arg1" );

		List<String> args = builder.getArgumentList();

		assertEquals( "bash", args.get( 0 ) );
		assertEquals( "-c", args.get( 1 ) );
		assertEquals( " arg1", args.get( 2 ) );
		assertEquals( 3, args.size() );

		builder.append( "arg2" );

		args = builder.getArgumentList();

		assertEquals( "bash", args.get( 0 ) );
		assertEquals( "-c", args.get( 1 ) );
		assertEquals( " arg2", args.get( 2 ) );
		assertEquals( 3, args.size() );
	}
}
