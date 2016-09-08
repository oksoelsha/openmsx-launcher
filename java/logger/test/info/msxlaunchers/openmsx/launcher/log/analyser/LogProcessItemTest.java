package info.msxlaunchers.openmsx.launcher.log.analyser;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class LogProcessItemTest
{
	@Test
	public void whenCreateLogProcessedItem_thenGetsReturnPassedValues()
	{
		String key = "key";
		List<String[]> items = Collections.emptyList();
		LogProcessItem logProcessItem = new LogProcessItem( key, items );

		assertEquals( key, logProcessItem.getKey() );
		assertEquals( items, logProcessItem.getItems() );
	}

	@Test( expected = NullPointerException.class )
	public void whenKeyIsNull_thenCreateLogProcessedItemThrowsException()
	{
		new LogProcessItem( null, Collections.emptyList() );
	}

	@Test( expected = NullPointerException.class )
	public void whenItemsIsNull_thenCreateLogProcessedItemThrowsException()
	{
		new LogProcessItem( "key", null );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void whenCreateLogProcessedItem_thenReturnItemsIsUnmodifiable()
	{
		String key = "key";
		List<String[]> items = Collections.emptyList();
		LogProcessItem logProcessItem = new LogProcessItem( key, items );

		logProcessItem.getItems().add( new String[] {"not allowed"} );
	}
}
