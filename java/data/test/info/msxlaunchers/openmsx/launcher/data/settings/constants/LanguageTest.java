package info.msxlaunchers.openmsx.launcher.data.settings.constants;

import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LanguageTest
{
	@Test
	public void testLanguageValues()
	{
		Language[] languages = Language.values();

		for( Language language: languages )
		{
			assertEquals( Language.fromValue( language.getValue() ), language );
		}
	}

	@Test
	public void testLanguageLocales()
	{
		assertEquals( "en_US", Language.ENGLISH.getLocaleName() );
		assertEquals( "ar_KW", Language.ARABIC.getLocaleName() );
		assertEquals( "ca_ES", Language.CATALAN.getLocaleName() );
		assertEquals( "zh_CN", Language.CHINESE_SIMPLIFIED.getLocaleName() );
		assertEquals( "zh_TW", Language.CHINESE_TRADITIONAL.getLocaleName() );
		assertEquals( "nl_NL", Language.DUTCH.getLocaleName() );
		assertEquals( "fi_FI", Language.FINNISH.getLocaleName() );
		assertEquals( "fr_FR", Language.FRENCH.getLocaleName() );
		assertEquals( "de_DE", Language.GERMAN.getLocaleName() );
		assertEquals( "it_IT", Language.ITALIAN.getLocaleName() );
		assertEquals( "ja_JP", Language.JAPANESE.getLocaleName() );
		assertEquals( "ko_KR", Language.KOREAN.getLocaleName() );
		assertEquals( "fa_IR", Language.PERSIAN.getLocaleName() );
		assertEquals( "pt_BR", Language.PORTUGUESE.getLocaleName() );
		assertEquals( "ru_RU", Language.RUSSIAN.getLocaleName() );
		assertEquals( "es_ES", Language.SPANISH.getLocaleName() );
		assertEquals( "sv_SE", Language.SWEDISH.getLocaleName() );
		assertEquals( "pl_PL", Language.POLISH.getLocaleName() );
	}

	@Test
	public void testNonExistentLanguageLocales()
	{
		assertNull( Language.fromValue( -1 ) );
		assertNull( Language.fromValue( 0 ) );
		assertNull( Language.fromValue( 19 ) );
	}

	@Test
	public void testNonExistentLanguageValues()
	{
		assertNull( Language.fromLocale( "xx_YY" ) );
	}

	@Test
	public void testLanguageOrientation()
	{
		//null is left to right
		assertTrue( Language.isLeftToRight( null ) );
		assertFalse( Language.isRightToLeft( null ) );

		assertTrue( Language.isLeftToRight( Language.ENGLISH ) );
		assertTrue( Language.isRightToLeft( Language.ARABIC ) );
		assertTrue( Language.isLeftToRight( Language.CATALAN ) );
		assertTrue( Language.isLeftToRight( Language.CHINESE_SIMPLIFIED ) );
		assertTrue( Language.isLeftToRight( Language.CHINESE_TRADITIONAL ) );
		assertTrue( Language.isLeftToRight( Language.DUTCH ) );
		assertTrue( Language.isLeftToRight( Language.FINNISH ) );
		assertTrue( Language.isLeftToRight( Language.FRENCH ) );
		assertTrue( Language.isLeftToRight( Language.GERMAN ) );
		assertTrue( Language.isLeftToRight( Language.ITALIAN ) );
		assertTrue( Language.isLeftToRight( Language.JAPANESE ) );
		assertTrue( Language.isLeftToRight( Language.KOREAN ) );
		assertTrue( Language.isRightToLeft( Language.PERSIAN ) );
		assertTrue( Language.isLeftToRight( Language.POLISH ) );
		assertTrue( Language.isLeftToRight( Language.PORTUGUESE ) );
		assertTrue( Language.isLeftToRight( Language.RUSSIAN ) );
		assertTrue( Language.isLeftToRight( Language.SPANISH ) );
		assertTrue( Language.isLeftToRight( Language.SWEDISH ) );
	}
}
