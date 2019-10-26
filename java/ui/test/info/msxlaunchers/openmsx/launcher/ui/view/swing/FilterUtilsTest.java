package info.msxlaunchers.openmsx.launcher.ui.view.swing;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterUtilsTest
{
	private Map<String,String> messages;

	@Before
	public void setup()
	{
		messages = new HashMap<>();

		messages.put( "COMPANY", "Co" );
		messages.put( "COUNTRY", "Cou" );
		messages.put( "JP", "Jp" );
		messages.put( "GENERATION", "Gen" );
		messages.put( "GENRE", "Genre" );
		messages.put( "MEDIUM", "Med" );
		messages.put( "ROM", "rom" );
		messages.put( "SIZE", "Size" );
		messages.put( "SOUND", "So" );
		messages.put( "YEAR", "Ye" );
		messages.put( "VIDEO_SOURCE", "Vid" );
	}

	@Test
	public void getFiltersStringRepresentation_listOfMonikers_displayStrings()
	{
		List<String> monikers = new ArrayList<>();
		monikers.add( "COMPANY:Konami::" );
		monikers.add( "COUNTRY:JP::" );
		monikers.add( "GENERATION:MSX2Plus::" );
		monikers.add( "GENRE:ACTION::" );
		monikers.add( "MEDIUM:ROM::" );
		monikers.add( "SIZE:2048:0:EQUAL_OR_GREATER" );
		monikers.add( "SIZE:4096:8192:BETWEEN_INCLUSIVE" );
		monikers.add( "SOUND:MSX_MUSIC::" );
		monikers.add( "YEAR:1985:0:EQUAL_OR_GREATER" );
		monikers.add( "YEAR:1985:1988:BETWEEN_INCLUSIVE" );
		monikers.add( "VIDEO_SOURCE:MSX::" );
		monikers.add( "SOUND:SCC::" );

		List<String> expectedList = Stream.of( "Co: Konami", "Cou: Jp", "Gen: MSX2+", "Genre: Action", "Med: rom",
				"Size: >= 2 KB", "Size: >= 4 KB , <= 8 KB", "So: MSX-MUSIC", "So: SCC", "Vid: MSX", "Ye: >= 1985", "Ye: >= 1985 , <= 1988").
				collect( Collectors.toList() );

		List<String> displayStrings = FilterUtils.getFiltersStringRepresentation(monikers, messages);

		assertEquals( expectedList, displayStrings );
	}

	@Test
	public void getFiltersStringRepresentation_moniker_displayStrings()
	{
		List<String> moniker;

		moniker = Collections.singletonList("COMPANY:ASCII::");
		assertEquals("Co: ASCII", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));

		moniker = Collections.singletonList("COUNTRY:JP::");
		assertEquals("Cou: Jp", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));

		moniker = Collections.singletonList("MEDIUM:ROM::");
		assertEquals("Med: rom", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));

		moniker = Collections.singletonList("GENRE:ADULT::");
		assertEquals("Genre: Adult", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));

		moniker = Collections.singletonList("SOUND:PCM::");
		assertEquals("So: PCM", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));

		moniker = Collections.singletonList("VIDEO_SOURCE:MSX::");
		assertEquals("Vid: MSX", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));

		moniker = Collections.singletonList("SIZE:8192:0:LESS");
		assertEquals("Size: < 8 KB", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));

		moniker = Collections.singletonList("SIZE:2048:0:GREATER");
		assertEquals("Size: > 2 KB", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));

		moniker = Collections.singletonList("YEAR:1998::EQUAL_OR_LESS");
		assertEquals("Ye: <= 1998", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));

		moniker = Collections.singletonList("YEAR:1985::EQUAL");
		assertEquals("Ye: = 1985", FilterUtils.getFiltersStringRepresentation(moniker, messages).get(0));
	}
}
