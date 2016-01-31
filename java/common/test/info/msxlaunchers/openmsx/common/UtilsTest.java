package info.msxlaunchers.openmsx.common;

import info.msxlaunchers.openmsx.common.NumericalEnum;
import info.msxlaunchers.openmsx.common.Utils;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UtilsTest
{
	@Test
	public void test_whenInstantiateClass_thenGetAnInstance()
	{
		//this is not a useful test. It's just meant to get code coverage
		new Utils();
	}

	@Test
	public void testGetNumber()
	{
		assertEquals( Utils.getNumber( null ), 0 );
		assertEquals( Utils.getNumber( "" ), 0 );
		assertEquals( Utils.getNumber( " " ), 0 );
		assertEquals( Utils.getNumber( "100" ), 100 );
		assertEquals( Utils.getNumber( "-100" ), -100 );
		assertEquals( Utils.getNumber( "a100" ), 0 );
		assertEquals( Utils.getNumber( "100a" ), 0 );
	}

	@Test
	public void testGetStringForInt()
	{
		assertEquals( Utils.getString( 0 ), "0" );
		assertEquals( Utils.getString( -10 ), "-10" );
		assertEquals( Utils.getString( 10 ), "10" );
		assertEquals( Utils.getString( Integer.MAX_VALUE ), "2147483647" );
		assertEquals( Utils.getString( Integer.MIN_VALUE ), "-2147483648" );
	}

	@Test
	public void testGetStringForLong()
	{
		assertEquals( Utils.getString( 0L ), "0" );
		assertEquals( Utils.getString( -10L ), "-10" );
		assertEquals( Utils.getString( 10L ), "10" );
		assertEquals( Utils.getString( Long.MAX_VALUE ), "9223372036854775807" );
		assertEquals( Utils.getString( Long.MIN_VALUE ), "-9223372036854775808" );
	}

	@Test
	public void testGetEnumValue()
	{
		assertEquals( Utils.getEnumValue( null ), "0" );
		assertEquals( Utils.getEnumValue( NumericalEnumImpl.TEST ), "1" );
	}

	@Test
	public void testIsEmpty()
	{
		assertTrue( Utils.isEmpty( null ) );
		assertTrue( Utils.isEmpty( "" ) );
		assertTrue( Utils.isEmpty( " " ) );
		assertFalse( Utils.isEmpty( "a" ) );
	}

	@Test
	public void testResetIfEmpty()
	{
		assertNull( Utils.resetIfEmpty( null ) );
		assertNull( Utils.resetIfEmpty( "" ) );
		assertNull( Utils.resetIfEmpty( " " ) );
		assertEquals( "abc", Utils.resetIfEmpty( "abc" ) );
		assertEquals( "abcd ", Utils.resetIfEmpty( "abcd " ) );
	}

	@Test
	public void testEqualStrings()
	{
		assertTrue( Utils.equalStrings( null, null ) );
		assertFalse( Utils.equalStrings( "str1", null ) );
		assertFalse( Utils.equalStrings( null, "str2" ) );
		assertFalse( Utils.equalStrings( "str1", "str2" ) );
		assertTrue( Utils.equalStrings( "str", "str" ) );
	}

	@Test(expected = NullPointerException.class )
	public void testGetSortedCaseInsensitiveArrayNullArg()
	{
		Utils.getSortedCaseInsensitiveArray( null );
	}

	@Test
	public void testGetSortedCaseInsensitiveArray()
	{
		Set<String> originalList = new HashSet<String>();
		originalList.add( "Z" );
		originalList.add( "n" );
		originalList.add( "K" );
		originalList.add( "t" );
		originalList.add( "k" );

		String[] result = Utils.getSortedCaseInsensitiveArray( originalList );

		assertEquals( result.length, 5 );

		//not testing "k" and "K" because I think that they can come in in any order

		assertEquals( result[2], "n" );
		assertEquals( result[3], "t" );
		assertEquals( result[4], "Z" );
	}

	@Test
	public void testGetSortedCaseInsensitiveSet()
	{
		Set<String> originalList = new HashSet<String>();
		originalList.add( "Zoo" );
		originalList.add( "nine" );
		originalList.add( "King" );
		originalList.add( "tea" );
		originalList.add( "Ant" );
		originalList.add( "key" );

		Set<String> result = Utils.getSortedCaseInsensitiveSet( originalList );

		assertEquals( result.size(), 6 );

		String[] resultAsArray = result.toArray( new String[0] );

		assertEquals( resultAsArray[0], "Ant" );
		assertEquals( resultAsArray[1], "key" );
		assertEquals( resultAsArray[2], "King" );
		assertEquals( resultAsArray[3], "nine" );
		assertEquals( resultAsArray[4], "tea" );
		assertEquals( resultAsArray[5], "Zoo" );
	}

	@Test
	public void testIsVersionNewer()
	{
		assertTrue( Utils.isVersionNewer( "1", "2" ) );
		assertTrue( Utils.isVersionNewer( "1.2", "1.3" ) );
		assertFalse( Utils.isVersionNewer( "1.2", "1.2" ) );
		assertFalse( Utils.isVersionNewer( "1.3", "1.2" ) );
		assertTrue( Utils.isVersionNewer( "1.3", "3.4.9" ) );
		assertFalse( Utils.isVersionNewer( "2.1", "2.1.0" ) );
		assertTrue( Utils.isVersionNewer( "2.1", "2.1.1" ) );
		assertFalse( Utils.isVersionNewer( "2.1.1", "2.1" ) );
		assertTrue( Utils.isVersionNewer( "1.12.13.3", "1.12.13.4" ) );
		assertFalse( Utils.isVersionNewer( "1.3", null ) );
		assertFalse( Utils.isVersionNewer( null, "3.4" ) );
		assertFalse( Utils.isVersionNewer( "1.3", "as" ) );
		assertFalse( Utils.isVersionNewer( "h", "3.2" ) );
		assertFalse( Utils.isVersionNewer( "", "2.4" ) );
		assertFalse( Utils.isVersionNewer( "1.1", "" ) );
	}

	private enum NumericalEnumImpl implements NumericalEnum
	{
		TEST;

		@Override
		public int getValue()
		{
			return 1;
		}		
	}
}
