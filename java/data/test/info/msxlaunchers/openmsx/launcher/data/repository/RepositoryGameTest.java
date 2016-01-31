package info.msxlaunchers.openmsx.launcher.data.repository;

import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RepositoryGameTest
{
	private String title = "title";
	private String company = "company";
	private String year = "year";
	private String country = "country";
	private String mapper = "mapper";
	private String originalText = "originalText";
	private String start = "start";
	private String remark = "remark";

	@Test
	public void testRepositoryGameConstructor1()
	{
		RepositoryGame repositoryGame = new RepositoryGame( title, company, year, country );

		assertEquals( repositoryGame.getTitle(), title );
		assertEquals( repositoryGame.getCompany(), company );
		assertEquals( repositoryGame.getYear(), year );
		assertEquals( repositoryGame.getCountry(), country );
		assertEquals( repositoryGame.getMapper(), RepositoryGame.MIRRORED_ROM );
		assertFalse( repositoryGame.isOriginal() );
		assertNull( repositoryGame.getOriginalText() );
		assertNull( repositoryGame.getRemark() );
		assertNull( repositoryGame.getStart() );
	}

	@Test
	public void testRepositoryGameConstructor2()
	{
		RepositoryGame repositoryGame = new RepositoryGame( title, company, year, country, true,
				originalText, mapper, start, remark );

		assertEquals( repositoryGame.getTitle(), title );
		assertEquals( repositoryGame.getCompany(), company );
		assertEquals( repositoryGame.getYear(), year );
		assertEquals( repositoryGame.getCountry(), country );
		assertEquals( repositoryGame.getMapper(), mapper);
		assertTrue( repositoryGame.isOriginal() );
		assertEquals( repositoryGame.getOriginalText(), originalText );
		assertEquals( repositoryGame.getRemark(), remark );
		assertEquals( repositoryGame.getStart(), start );
	}
}
