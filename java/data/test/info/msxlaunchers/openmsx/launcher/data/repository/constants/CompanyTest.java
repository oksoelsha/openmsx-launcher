package info.msxlaunchers.openmsx.launcher.data.repository.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompanyTest
{
	@Test
	public void testCompanyValues()
	{
		String[] companies = { "Activision", "Al Alamiah", "Anma", "ASCII", "Bothtec", "The Bytebusters", "Casio","Compile", "dB-SOFT",
				"ENIX", "German Gomez Herrera", "Hudson Soft", "Hudson Soft / Japanese Softbank", "Infinite", "Karoshi Corporation",
				"Kai Magazine", "Konami", "Mass Tael", "Microcabin", "NAMCO", "Nippon Columbia / Colpax / Universal", "Pack-In-Video",
				"Parallax", "Paxanga Soft", "Pony Canyon", "Sega", "Sony", "TAITO", "T&ESOFT", "Telenet Japan", "ZAP", "Zemina" };

		Company[] enumCompanies = Company.values();

		assertEquals( companies.length, enumCompanies.length );

		for( int index = 0; index < companies.length; index++ )
		{
			assertEquals( companies[index], enumCompanies[index].getDisplayName() );
		}
	}

	@Test
	public void testTotalCompaniesCount()
	{
		assertEquals( Company.values().length, 32 );
	}
}
