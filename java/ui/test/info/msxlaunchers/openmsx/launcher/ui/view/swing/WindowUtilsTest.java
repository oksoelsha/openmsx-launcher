package info.msxlaunchers.openmsx.launcher.ui.view.swing;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JMenu;

public class WindowUtilsTest
{
	@Test
	public void truncateStringAndDisplayEllipsis_nullString_emptyString()
	{
		assertEquals("", WindowUtils.truncateStringAndDisplayEllipsis(null, getFontMetrics(), 1));
	}

	@Test
	public void truncateStringAndDisplayEllipsis_nullFontMetrics_emptyString()
	{
		assertEquals("", WindowUtils.truncateStringAndDisplayEllipsis("a", null, 1));
	}

	@Test
	public void truncateStringAndDisplayEllipsis_stringThatFits_sameString()
	{
		assertEquals("short", WindowUtils.truncateStringAndDisplayEllipsis("short", getFontMetrics(), 50));
	}

	@Test
	public void truncateStringAndDisplayEllipsis_stringThatDoesNotFit_truncatedStringWithEllipsis()
	{
		assertEquals("very long...", WindowUtils.truncateStringAndDisplayEllipsis("very long string", getFontMetrics(), 54));
	}

	private FontMetrics getFontMetrics()
	{
		return new JMenu().getFontMetrics(new Font(null, Font.PLAIN, 10));
	}
}
