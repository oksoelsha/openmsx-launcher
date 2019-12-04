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
	private String system = "system";
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
		RepositoryGame repositoryGame = RepositoryGame.title( title ).system( system ).company( company ).year( year ).country( country ).build();

		assertEquals( title, repositoryGame.getTitle() );
		assertEquals( company, repositoryGame.getCompany() );
		assertEquals( year, repositoryGame.getYear() );
		assertEquals( country, repositoryGame.getCountry() );
		assertEquals( RepositoryGame.MIRRORED_ROM, repositoryGame.getMapper() );
		assertFalse( repositoryGame.isOriginal() );
		assertNull( repositoryGame.getOriginalText() );
		assertNull( repositoryGame.getRemark() );
		assertNull( repositoryGame.getStart() );
	}

	@Test
	public void testRepositoryGameConstructor2()
	{
		RepositoryGame repositoryGame = RepositoryGame.title( title ).system( system ).company( company ).year( year ).country( country )
				.isOriginal( true ).originalText( originalText ).mapper( mapper ).start( start) .remark( remark ).build();

		assertEquals( title, repositoryGame.getTitle() );
		assertEquals( system, repositoryGame.getSystem() );
		assertEquals( company, repositoryGame.getCompany() );
		assertEquals( year, repositoryGame.getYear() );
		assertEquals( country, repositoryGame.getCountry() );
		assertEquals( mapper, repositoryGame.getMapper() );
		assertTrue( repositoryGame.isOriginal() );
		assertEquals( originalText, repositoryGame.getOriginalText() );
		assertEquals( remark, repositoryGame.getRemark() );
		assertEquals( start, repositoryGame.getStart() );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testRepositoryGameMissingTitle()
	{
		RepositoryGame.system( system ).company( company ).year( year ).country( country ).build();
	}

	@Test( expected = IllegalArgumentException.class )
	public void testRepositoryGameMissingSystem()
	{
		RepositoryGame.title( title ).company( company ).year( year ).country( country ).build();
	}

	@Test( expected = IllegalArgumentException.class )
	public void testRepositoryGameMissingCompany()
	{
		RepositoryGame.title( title ).system( system ).year( year ).country( country ).build();
	}

	@Test( expected = IllegalArgumentException.class )
	public void testRepositoryGameMissingYear()
	{
		RepositoryGame.title( title ).system( system ).company( company ).country( country ).build();
	}

	@Test( expected = IllegalArgumentException.class )
	public void testRepositoryGameMissingCountry()
	{
		RepositoryGame.title( title ).system( system ).company( company ).year( year ).build();
	}
}
