package info.msxlaunchers.openmsx.launcher.data.repository.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountryTest
{
	@Test
	public void testCountryValues()
	{
		String[] countries = { "BR", "ES", "FR", "GB", "HK", "IT", "JP", "KR", "KW", "NL", "PT", "RU", "SA", "SE", "UK", "US", "TW", "CA", "EU" };
		Country[] enumCountries = Country.values();

		assertEquals( countries.length, enumCountries.length );

		for( int index = 0; index < countries.length; index++ )
		{
			assertEquals( countries[index], enumCountries[index].toString() );
		}
	}

	@Test
	public void testCountrySize()
	{
		assertEquals( 19, Country.values().length );
	}
}
