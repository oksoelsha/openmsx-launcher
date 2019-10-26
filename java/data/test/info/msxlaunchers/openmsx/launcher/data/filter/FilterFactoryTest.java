package info.msxlaunchers.openmsx.launcher.data.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterFactoryTest
{
	@Test
	public void testCreateCompanyFilter()
	{
		Filter filter = FilterFactory.createFilter( FilterType.COMPANY, "company", null, null );
		assertTrue( filter instanceof CompanyFilter );

		filter = FilterFactory.createFilter( "COMPANY", "ASCII", null, null );
		assertTrue( filter instanceof CompanyFilter );
	}

	@Test( expected = NullPointerException.class )
	public void testCreateCompanyFilterNullValue()
	{
		FilterFactory.createFilter( FilterType.COMPANY, null, null, null );
	}

	@Test
	public void testCreateCountryFilter()
	{
		Filter filter = FilterFactory.createFilter( FilterType.COUNTRY, "country", null, null );
		assertTrue( filter instanceof CountryFilter );

		filter = FilterFactory.createFilter( "COUNTRY", "KW", null, null );
		assertTrue( filter instanceof CountryFilter );
	}

	@Test
	public void testCreateGenerationFilter()
	{
		Filter filter = FilterFactory.createFilter( FilterType.GENERATION, "MSX2", null, null );
		assertTrue( filter instanceof GenerationFilter );

		filter = FilterFactory.createFilter( "GENERATION", "TURBO_R", null, null );
		assertTrue( filter instanceof GenerationFilter );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testCreateGenerationFilterException()
	{
		FilterFactory.createFilter( FilterType.GENERATION, "wrong", null, null );
	}

	@Test
	public void testCreateGenreFilter()
	{
		Filter filter = FilterFactory.createFilter( FilterType.GENRE, "ACTION", null, null );
		assertTrue( filter instanceof GenreFilter );

		filter = FilterFactory.createFilter( "GENRE", "COMPILER", null, null );
		assertTrue( filter instanceof GenreFilter );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testCreateGenreFilterException()
	{
		FilterFactory.createFilter( FilterType.GENRE, "wrong", null, null );
	}

	@Test
	public void testCreateMediumFilter()
	{
		Filter filter = FilterFactory.createFilter( FilterType.MEDIUM, "ROM", null, null );
		assertTrue( filter instanceof MediumFilter );

		filter = FilterFactory.createFilter( "MEDIUM", "LASERDISC", null, null );
		assertTrue( filter instanceof MediumFilter );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testCreateMediumFilterException()
	{
		FilterFactory.createFilter( FilterType.MEDIUM, "wrong", null, null );
	}

	@Test
	public void testCreateSizeFilter()
	{
		Filter filter = FilterFactory.createFilter( FilterType.SIZE, "16", "", FilterParameter.EQUAL );
		assertTrue( filter instanceof SizeFilter );

		filter = FilterFactory.createFilter( "SIZE", "16", "", "GREATER" );
		assertTrue( filter instanceof SizeFilter );
	}

	@Test
	public void testCreateSoundFilter()
	{
		Filter filter = FilterFactory.createFilter( FilterType.SOUND, "PSG", null, null );
		assertTrue( filter instanceof SoundFilter );

		filter = FilterFactory.createFilter( "SOUND", "PCM", null, null );
		assertTrue( filter instanceof SoundFilter );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testCreateSoundFilterException()
	{
		FilterFactory.createFilter( FilterType.SOUND, "wrong", null, null );
	}

	@Test
	public void testCreateYearFilter()
	{
		Filter filter = FilterFactory.createFilter( FilterType.YEAR, "1986", "", FilterParameter.EQUAL );
		assertTrue( filter instanceof YearFilter );

		filter = FilterFactory.createFilter( "YEAR", "1987", "1992", "BETWEEN_INCLUSIVE" );
		assertTrue( filter instanceof YearFilter );
	}

	@Test
	public void testCreateVideoSourceFilter()
	{
		Filter filter = FilterFactory.createFilter( FilterType.VIDEO_SOURCE, "MSX", null, null );
		assertTrue( filter instanceof VideoSourceFilter );

		filter = FilterFactory.createFilter( "VIDEO_SOURCE", "GFX9000", null, null );
		assertTrue( filter instanceof VideoSourceFilter );
	}

	@Test( expected = IllegalArgumentException.class )
	public void testCreateVideoSourceFilterException()
	{
		FilterFactory.createFilter( FilterType.VIDEO_SOURCE, "wrong", null, null );
	}

	@Test
	public void testGetFilterMoniker()
	{
		String moniker;

		Filter companyFilter = FilterFactory.createFilter( FilterType.COMPANY, "Konami", null, null );
		moniker = FilterFactory.getFilterMoniker( companyFilter );
		assertEquals( "COMPANY:Konami::", moniker );

		Filter countryFilter = FilterFactory.createFilter( FilterType.COUNTRY, "JP", null, null );
		moniker = FilterFactory.getFilterMoniker( countryFilter );
		assertEquals( "COUNTRY:JP::", moniker );

		Filter generationFilter = FilterFactory.createFilter( FilterType.GENERATION, "MSX2Plus", null, null );
		moniker = FilterFactory.getFilterMoniker( generationFilter );
		assertEquals( "GENERATION:MSX2Plus::", moniker );

		Filter genreFilter = FilterFactory.createFilter( FilterType.GENRE, "ACTION", null, null );
		moniker = FilterFactory.getFilterMoniker( genreFilter );
		assertEquals( "GENRE:ACTION::", moniker );

		Filter mediumFilter = FilterFactory.createFilter( FilterType.MEDIUM, "HARDDISK", null, null );
		moniker = FilterFactory.getFilterMoniker( mediumFilter );
		assertEquals( "MEDIUM:HARDDISK::", moniker );

		Filter sizeFilter1 = FilterFactory.createFilter( FilterType.SIZE, "32", null, FilterParameter.EQUAL_OR_GREATER );
		moniker = FilterFactory.getFilterMoniker( sizeFilter1 );
		assertEquals( "SIZE:32:0:EQUAL_OR_GREATER", moniker );

		Filter sizeFilter2 = FilterFactory.createFilter( FilterType.SIZE, "32", "64", FilterParameter.BETWEEN_INCLUSIVE );
		moniker = FilterFactory.getFilterMoniker( sizeFilter2 );
		assertEquals( "SIZE:32:64:BETWEEN_INCLUSIVE", moniker );

		Filter soundFilter = FilterFactory.createFilter( FilterType.SOUND, "MSX_MUSIC", null, null );
		moniker = FilterFactory.getFilterMoniker( soundFilter );
		assertEquals( "SOUND:MSX_MUSIC::", moniker );

		Filter yearFilter1 = FilterFactory.createFilter( FilterType.YEAR, "1985", null, FilterParameter.EQUAL_OR_GREATER );
		moniker = FilterFactory.getFilterMoniker( yearFilter1 );
		assertEquals( "YEAR:1985:0:EQUAL_OR_GREATER", moniker );

		Filter yearFilter2 = FilterFactory.createFilter( FilterType.YEAR, "1985", "1988", FilterParameter.BETWEEN_INCLUSIVE );
		moniker = FilterFactory.getFilterMoniker( yearFilter2 );
		assertEquals( "YEAR:1985:1988:BETWEEN_INCLUSIVE", moniker );

		Filter videoSourceFilter = FilterFactory.createFilter( FilterType.VIDEO_SOURCE, "MSX", null, null );
		moniker = FilterFactory.getFilterMoniker( videoSourceFilter );
		assertEquals( "VIDEO_SOURCE:MSX::", moniker );
	}

	@Test( expected = NullPointerException.class )
	public void testGetFilterMonikerNullValue()
	{
		FilterFactory.getFilterMoniker( null );
	}

	@Test
	public void testGetFilterMonikers()
	{
		Set<Filter> filters = new HashSet<>();
		List<String> monikers = new ArrayList<>();

		Filter companyFilter = FilterFactory.createFilter( FilterType.COMPANY, "Konami", null, null );
		filters.add( companyFilter );
		monikers.add( "COMPANY:Konami::" );

		Filter countryFilter = FilterFactory.createFilter( FilterType.COUNTRY, "JP", null, null );
		filters.add( countryFilter );
		monikers.add( "COUNTRY:JP::" );

		Filter generationFilter = FilterFactory.createFilter( FilterType.GENERATION, "MSX2Plus", null, null );
		filters.add( generationFilter );
		monikers.add( "GENERATION:MSX2Plus::" );

		Filter genreFilter = FilterFactory.createFilter( FilterType.GENRE, "ACTION", null, null );
		filters.add( genreFilter );
		monikers.add( "GENRE:ACTION::" );

		Filter mediumFilter = FilterFactory.createFilter( FilterType.MEDIUM, "HARDDISK", null, null );
		filters.add( mediumFilter );
		monikers.add( "MEDIUM:HARDDISK::" );

		Filter sizeFilter1 = FilterFactory.createFilter( FilterType.SIZE, "32", null, FilterParameter.EQUAL_OR_GREATER );
		filters.add( sizeFilter1 );
		monikers.add( "SIZE:32:0:EQUAL_OR_GREATER" );

		Filter sizeFilter2 = FilterFactory.createFilter( FilterType.SIZE, "32", "64", FilterParameter.BETWEEN_INCLUSIVE );
		filters.add( sizeFilter2 );
		monikers.add( "SIZE:32:64:BETWEEN_INCLUSIVE" );

		Filter soundFilter = FilterFactory.createFilter( FilterType.SOUND, "MSX_MUSIC", null, null );
		filters.add( soundFilter );
		monikers.add( "SOUND:MSX_MUSIC::" );

		Filter yearFilter1 = FilterFactory.createFilter( FilterType.YEAR, "1985", null, FilterParameter.EQUAL_OR_GREATER );
		filters.add( yearFilter1 );
		monikers.add( "YEAR:1985:0:EQUAL_OR_GREATER" );

		Filter yearFilter2 = FilterFactory.createFilter( FilterType.YEAR, "1985", "1988", FilterParameter.BETWEEN_INCLUSIVE );
		filters.add( yearFilter2 );
		monikers.add( "YEAR:1985:1988:BETWEEN_INCLUSIVE" );

		Filter videoSourceFilter = FilterFactory.createFilter( FilterType.VIDEO_SOURCE, "MSX", null, null );
		filters.add( videoSourceFilter );
		monikers.add( "VIDEO_SOURCE:MSX::" );

		Filter soundFilter2 = FilterFactory.createFilter( FilterType.SOUND, "SCC", null, null );
		filters.add( soundFilter2 );
		monikers.add( "SOUND:SCC::" );

		List<String> returnedList = FilterFactory.getFilterMonikers( filters );

		assertTrue(returnedList.size() == monikers.size() &&  returnedList.containsAll( monikers ) && monikers.containsAll( returnedList ) );
	}

	@Test
	public void testGetFilterMonikersNullValue()
	{
		assertEquals( Collections.emptyList(), FilterFactory.getFilterMonikers( null ) );
	}

	@Test
	public void testGetFilterType()
	{
		Filter companyFilter = FilterFactory.createFilter( FilterType.COMPANY, "Konami", null, null );
		assertEquals( FilterType.COMPANY, FilterFactory.getFilterType( companyFilter ) );

		Filter countryFilter = FilterFactory.createFilter( FilterType.COUNTRY, "BR", null, null );
		assertEquals( FilterType.COUNTRY, FilterFactory.getFilterType( countryFilter ) );

		Filter generationFilter = FilterFactory.createFilter( FilterType.GENERATION, "MSX2Plus", null, null );
		assertEquals( FilterType.GENERATION, FilterFactory.getFilterType( generationFilter ) );

		Filter genreFilter = FilterFactory.createFilter( FilterType.GENRE, "ACTION", null, null );
		assertEquals( FilterType.GENRE, FilterFactory.getFilterType( genreFilter ) );

		Filter mediumFilter = FilterFactory.createFilter( FilterType.MEDIUM, "HARDDISK", null, null );
		assertEquals( FilterType.MEDIUM, FilterFactory.getFilterType( mediumFilter ) );

		Filter sizeFilter = FilterFactory.createFilter( FilterType.SIZE, "32", null, FilterParameter.EQUAL_OR_GREATER );
		assertEquals( FilterType.SIZE, FilterFactory.getFilterType( sizeFilter ) );

		Filter soundFilter = FilterFactory.createFilter( FilterType.SOUND, "MSX_MUSIC", null, null );
		assertEquals( FilterType.SOUND, FilterFactory.getFilterType( soundFilter ) );

		Filter yearFilter = FilterFactory.createFilter( FilterType.YEAR, "1985", null, FilterParameter.EQUAL_OR_GREATER );
		assertEquals( FilterType.YEAR, FilterFactory.getFilterType( yearFilter ) );

		Filter videoSourceFilter = FilterFactory.createFilter( FilterType.VIDEO_SOURCE, "GFX9000", null, null );
		assertEquals( FilterType.VIDEO_SOURCE, FilterFactory.getFilterType( videoSourceFilter ) );
	}

	@Test( expected = NullPointerException.class )
	public void testGetFilterTypeNullValue()
	{
		FilterFactory.getFilterType( null );
	}

	@Test
	public void testGetAnnotatedFieldValue()
	{
		Filter companyFilter = FilterFactory.createFilter( FilterType.COMPANY, "Konami", null, null );
		assertEquals( "Konami", FilterFactory.getAnnotatedFieldValue( companyFilter, Value1Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( companyFilter, Value2Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( companyFilter, ParameterField.class ) );

		Filter countryFilter = FilterFactory.createFilter( FilterType.COUNTRY, "BR", null, null );
		assertEquals( "BR", FilterFactory.getAnnotatedFieldValue( countryFilter, Value1Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( countryFilter, Value2Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( countryFilter, ParameterField.class ) );

		Filter generationFilter = FilterFactory.createFilter( FilterType.GENERATION, "MSX2Plus", null, null );
		assertEquals( "MSX2Plus", FilterFactory.getAnnotatedFieldValue( generationFilter, Value1Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( generationFilter, Value2Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( generationFilter, ParameterField.class ) );

		Filter genreFilter = FilterFactory.createFilter( FilterType.GENRE, "ACTION", null, null );
		assertEquals( "ACTION", FilterFactory.getAnnotatedFieldValue( genreFilter, Value1Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( genreFilter, Value2Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( genreFilter, ParameterField.class ) );

		Filter mediumFilter = FilterFactory.createFilter( FilterType.MEDIUM, "TAPE", null, null );
		assertEquals( "TAPE", FilterFactory.getAnnotatedFieldValue( mediumFilter, Value1Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( mediumFilter, Value2Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( mediumFilter, ParameterField.class ) );

		Filter sizeFilter1 = FilterFactory.createFilter( FilterType.SIZE, "32", null, FilterParameter.EQUAL_OR_GREATER );
		assertEquals( "32", FilterFactory.getAnnotatedFieldValue( sizeFilter1, Value1Field.class ) );
		assertEquals( "0", FilterFactory.getAnnotatedFieldValue( sizeFilter1, Value2Field.class ) );
		assertEquals( "EQUAL_OR_GREATER", FilterFactory.getAnnotatedFieldValue( sizeFilter1, ParameterField.class ) );

		Filter sizeFilter2 = FilterFactory.createFilter( FilterType.SIZE, "128", "256", FilterParameter.BETWEEN_INCLUSIVE );
		assertEquals( "128", FilterFactory.getAnnotatedFieldValue( sizeFilter2, Value1Field.class ) );
		assertEquals( "256", FilterFactory.getAnnotatedFieldValue( sizeFilter2, Value2Field.class ) );
		assertEquals( "BETWEEN_INCLUSIVE", FilterFactory.getAnnotatedFieldValue( sizeFilter2, ParameterField.class ) );

		Filter soundFilter = FilterFactory.createFilter( FilterType.SOUND, "MSX_MUSIC", null, null );
		assertEquals( "MSX_MUSIC", FilterFactory.getAnnotatedFieldValue( soundFilter, Value1Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( soundFilter, Value2Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( soundFilter, ParameterField.class ) );

		Filter yearFilter1 = FilterFactory.createFilter( FilterType.YEAR, "1985", null, FilterParameter.LESS );
		assertEquals( "1985", FilterFactory.getAnnotatedFieldValue( yearFilter1, Value1Field.class ) );
		assertEquals( "0", FilterFactory.getAnnotatedFieldValue( yearFilter1, Value2Field.class ) );
		assertEquals( "LESS", FilterFactory.getAnnotatedFieldValue( yearFilter1, ParameterField.class ) );

		Filter yearFilter2 = FilterFactory.createFilter( FilterType.YEAR, "1989", "1990", FilterParameter.BETWEEN_INCLUSIVE );
		assertEquals( "1989", FilterFactory.getAnnotatedFieldValue( yearFilter2, Value1Field.class ) );
		assertEquals( "1990", FilterFactory.getAnnotatedFieldValue( yearFilter2, Value2Field.class ) );
		assertEquals( "BETWEEN_INCLUSIVE", FilterFactory.getAnnotatedFieldValue( yearFilter2, ParameterField.class ) );

		Filter videoSourceFilter = FilterFactory.createFilter( FilterType.VIDEO_SOURCE, "MSX", null, null );
		assertEquals( "MSX", FilterFactory.getAnnotatedFieldValue( videoSourceFilter, Value1Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( videoSourceFilter, Value2Field.class ) );
		assertNull( FilterFactory.getAnnotatedFieldValue( videoSourceFilter, ParameterField.class ) );
	}

	@Test( expected = NullPointerException.class )
	public void testGetAnnotatedFieldValueNullArg1()
	{
		FilterFactory.getAnnotatedFieldValue( null, Value1Field.class );
	}

	@Test( expected = NullPointerException.class )
	public void testGetAnnotatedFieldValueNullArg2()
	{
		FilterFactory.getAnnotatedFieldValue( FilterFactory.createFilter( FilterType.COUNTRY, "BR", null, null ), null );
	}
}
