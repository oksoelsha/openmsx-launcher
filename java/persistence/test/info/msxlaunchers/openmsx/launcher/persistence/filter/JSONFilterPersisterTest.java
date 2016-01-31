package info.msxlaunchers.openmsx.launcher.persistence.filter;

import info.msxlaunchers.openmsx.launcher.data.filter.Filter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterFactory;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterType;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterDescriptor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSONFilterPersisterTest
{
	private String filtersTestPath = System.getProperty( "user.dir" );

	@After
	public void cleanup()
	{
		File filtersTestFile = new File( filtersTestPath, "filters/filters.json" );
		filtersTestFile.delete();

		File createdFiltersDirectory = new File( filtersTestPath, "filters/filters" );
		createdFiltersDirectory.delete();
	}

	@Test
	public void testReadExistingFiltersFile() throws FilterSetNotFoundException, IOException
	{
		File existingFiltersDirectory = new File( filtersTestPath, "filters/existing" );

		JSONFilterPersister filterPersister = new JSONFilterPersister( existingFiltersDirectory.getAbsolutePath() );

		Set<String> filterNames = filterPersister.getFilterNames();

		assertEquals( 3, filterNames.size() );
		assertTrue( filterNames.contains( "Konami MSX SCC 1987 128<256" ) );
		assertTrue( filterNames.contains( "MSX-Music >1988" ) );
		assertTrue( filterNames.contains( "Scripts" ) );

		//---
		//filter set by the name "Konami MSX SCC 1987 128<256"
		Set<Filter> konamiFilters = filterPersister.getFilter( "Konami MSX SCC 1987 128<256" );

		assertEquals( 6, konamiFilters.size() );

		FilterType[] typesForKonami = new FilterType[] { FilterType.COMPANY, FilterType.COUNTRY, FilterType.SOUND,
				FilterType.GENERATION, FilterType.YEAR, FilterType.SIZE };

		List<FilterType> typesListForKonami = new ArrayList<FilterType>( Arrays.asList( typesForKonami ) );

		Filter companyFilter = FilterFactory.createFilter( FilterType.COMPANY, "Konami", null, null );
		Filter countryFilter = FilterFactory.createFilter( FilterType.COUNTRY, "JP", null, null );
		Filter soundFilter = FilterFactory.createFilter( FilterType.SOUND, "SCC", null, null );
		Filter generationFilter = FilterFactory.createFilter( FilterType.GENERATION, "MSX", null, null );
		Filter yearFilter = FilterFactory.createFilter( FilterType.YEAR, "1987", "0", FilterParameter.EQUAL );
		Filter sizeFilter = FilterFactory.createFilter( FilterType.SIZE, "131072", "262144", FilterParameter.BETWEEN_INCLUSIVE );

		Set<Filter> filterSet = new HashSet<Filter>();
		filterSet.add( companyFilter );
		filterSet.add( countryFilter );
		filterSet.add( soundFilter );
		filterSet.add( generationFilter );
		filterSet.add( yearFilter );
		filterSet.add( sizeFilter );

		for( Filter filter: konamiFilters )
		{
			assertTrue( typesListForKonami.contains( filter.getClass().getAnnotation( FilterDescriptor.class ).type() ) );
			assertTrue( filterSet.remove( filter ) );
		}

		//check that all filters were moved which means they were recreated correctly from JSON
		assertTrue( filterSet.isEmpty() );

		//---
		//filter set by the name "MSX-Music >1988"
		Set<Filter> msxmusicFilters = filterPersister.getFilter( "MSX-Music >1988" );

		assertEquals( 4, msxmusicFilters.size() );

		FilterType[] typesForMsxmusic = new FilterType[] { FilterType.SOUND, FilterType.YEAR, FilterType.GENRE };
		List<FilterType> typesListForMsxmusic = new ArrayList<FilterType>( Arrays.asList( typesForMsxmusic ) );

		Filter soundFilter1 = FilterFactory.createFilter( FilterType.SOUND, "MSX_MUSIC", null, null );
		Filter soundFilter2 = FilterFactory.createFilter( FilterType.SOUND, "MSX_AUDIO", null, null );
		Filter yearFilter2 = FilterFactory.createFilter( FilterType.YEAR, "1988", "0", FilterParameter.GREATER );
		Filter genreFilter = FilterFactory.createFilter( FilterType.GENRE, "SIMULATION", null, null );

		Set<Filter> filterSet2 = new HashSet<Filter>();
		filterSet2.add( soundFilter1 );
		filterSet2.add( soundFilter2 );
		filterSet2.add( yearFilter2 );
		filterSet2.add( genreFilter );

		for( Filter filter: msxmusicFilters )
		{
			assertTrue( typesListForMsxmusic.contains( filter.getClass().getAnnotation( FilterDescriptor.class ).type() ) );
			assertTrue( filterSet2.remove( filter ) );
		}

		//check that all filters were moved which means they were recreated correctly from JSON
		assertTrue( filterSet2.isEmpty() );

		//---
		//filter set by the name "Scripts"
		Set<Filter> scriptsFilters = filterPersister.getFilter( "Scripts" );

		assertEquals( 1, scriptsFilters.size() );

		Filter mediumFilter = FilterFactory.createFilter( FilterType.MEDIUM, "SCRIPT", null, null );

		assertEquals( FilterType.MEDIUM, scriptsFilters.iterator().next().getClass().getAnnotation( FilterDescriptor.class ).type() );
		assertTrue( scriptsFilters.iterator().next().equals( mediumFilter ) );
	}

	@Test
	public void testCreateFiltersDirectoryIfNotExists()
	{
		File tempFiltersDirectory = new File( filtersTestPath, "filters" );
		String tempFiltersDirectoryPath = tempFiltersDirectory.getAbsolutePath();

		new JSONFilterPersister( tempFiltersDirectoryPath );

		assertTrue( tempFiltersDirectory.exists() );
	}

	@Test( expected = IOException.class )
	public void testSaveFiltersException() throws FilterSetAlreadyExistsException, IOException
	{
		JSONFilterPersister filterPersister = new JSONFilterPersister( "non-existent-directory" );

		Set<Filter> filters = new HashSet<Filter>();

		filterPersister.saveFilter( "some-name", filters );
	}

	@Test( expected = IOException.class )
	public void testDeleteFiltersException() throws IOException
	{
		JSONFilterPersister filterPersister = new JSONFilterPersister( "non-existent-directory" );

		filterPersister.deleteFilter( "some-name" );
	}

	@Test
	public void testDeleteFilters() throws FilterSetAlreadyExistsException, IOException
	{
		Set<Filter> filters = new HashSet<Filter>();

		Filter companyFilter = FilterFactory.createFilter( FilterType.COMPANY, "ASCII", null, null );
		Filter countryFilter = FilterFactory.createFilter( FilterType.COUNTRY, "JP", null, null );

		filters.add( companyFilter );
		filters.add( countryFilter );

		JSONFilterPersister filterPersister = new JSONFilterPersister( filtersTestPath );

		filterPersister.saveFilter( "test", filters );

		//now create a second set of filters
		filters = new HashSet<Filter>();

		Filter genreFilter = FilterFactory.createFilter( FilterType.GENRE, "ADULT", null, null );
		Filter sizeFilter = FilterFactory.createFilter( FilterType.SIZE, "123456", null, FilterParameter.EQUAL_OR_GREATER );
		Filter yearFilter = FilterFactory.createFilter( FilterType.YEAR, "1986", "1988", FilterParameter.BETWEEN_INCLUSIVE );

		filters.add( genreFilter );
		filters.add( sizeFilter );
		filters.add( yearFilter );

		filterPersister.saveFilter( "test2", filters );

		//now delete filter set 'test'
		filterPersister.deleteFilter( "test" );

		//Create another file persister instance to read from the real file
		JSONFilterPersister filterPersister2 = new JSONFilterPersister( filtersTestPath );

		Set<String> filterNames = filterPersister2.getFilterNames();

		//remaining filter set should be 'test2'
		assertEquals( 1, filterNames.size() );
		assertTrue( filterNames.iterator().next().equals( "test2" ) );
	}

	@Test
	public void testSaveFilters() throws FilterSetAlreadyExistsException, FilterSetNotFoundException, IOException
	{
		Set<Filter> filters = new HashSet<Filter>();

		Filter companyFilter = FilterFactory.createFilter( FilterType.COMPANY, "ASCII", null, null );
		Filter countryFilter = FilterFactory.createFilter( FilterType.COUNTRY, "JP", null, null );
		Filter mediumFilter = FilterFactory.createFilter( FilterType.MEDIUM, "DISK", null, null );
		Filter generationFilter = FilterFactory.createFilter( FilterType.GENERATION, "MSX2", null, null );
		Filter soundFilter = FilterFactory.createFilter( FilterType.SOUND, "SCC", null, null );
		Filter soundFilter2 = FilterFactory.createFilter( FilterType.SOUND, "MSX_AUDIO", null, null );

		filters.add( companyFilter );
		filters.add( countryFilter );
		filters.add( mediumFilter );
		filters.add( generationFilter );
		filters.add( soundFilter );
		filters.add( soundFilter2 );

		JSONFilterPersister filterPersister = new JSONFilterPersister( filtersTestPath );

		filterPersister.saveFilter( "test", filters );

		//now create a second set of filters
		filters = new HashSet<Filter>();

		Filter genreFilter = FilterFactory.createFilter( FilterType.GENRE, "ADULT", null, null );
		Filter sizeFilter = FilterFactory.createFilter( FilterType.SIZE, "123456", null, FilterParameter.EQUAL_OR_GREATER );
		Filter yearFilter = FilterFactory.createFilter( FilterType.YEAR, "1986", "1988", FilterParameter.BETWEEN_INCLUSIVE );

		filters.add( genreFilter );
		filters.add( sizeFilter );
		filters.add( yearFilter );

		filterPersister.saveFilter( "test2", filters );

		//now get the saved filters - this will return the cached copy and not the one on disk
		Set<String> filterNames = filterPersister.getFilterNames();

		assertEquals( 2, filterNames.size() );
		assertTrue( filterNames.contains( "test" ) );
		assertTrue( filterNames.contains( "test2" ) );

		//filter set by the name "test"
		Set<Filter> testFilters = filterPersister.getFilter( "test" );

		assertEquals( 6, testFilters.size() );

		FilterType[] typesForTest1 = new FilterType[] { FilterType.COMPANY, FilterType.COUNTRY, FilterType.MEDIUM,
				FilterType.GENERATION, FilterType.SOUND };

		List<FilterType> typesListForTest1 = new ArrayList<FilterType>( Arrays.asList( typesForTest1 ) );

		for( Filter filter: testFilters )
		{
			assertTrue( typesListForTest1.contains( filter.getClass().getAnnotation( FilterDescriptor.class ).type() ) );
		}

		//filter set by the name "test2"
		Set<Filter> testFilters2 = filterPersister.getFilter( "test2" );

		assertEquals( 3, testFilters2.size() );

		FilterType[] typesForTest2 = new FilterType[] { FilterType.GENRE, FilterType.SIZE, FilterType.YEAR };

		List<FilterType> typesListForTest2 = new ArrayList<FilterType>( Arrays.asList( typesForTest2 ) );

		for( Filter filter: testFilters2 )
		{
			assertTrue( typesListForTest2.contains( filter.getClass().getAnnotation( FilterDescriptor.class ).type() ) );
		}
	}

	@Test( expected = FilterSetAlreadyExistsException.class )
	public void testSaveFiltersExistingName() throws FilterSetAlreadyExistsException, IOException
	{
		Set<Filter> filters = new HashSet<Filter>();

		JSONFilterPersister filterPersister = new JSONFilterPersister( filtersTestPath );

		filterPersister.saveFilter( "test", filters );

		filterPersister.saveFilter( "test", filters );
	}

	@Test( expected = FilterSetNotFoundException.class )
	public void testGetFiltersNotFound() throws FilterSetNotFoundException, IOException
	{
		JSONFilterPersister filterPersister = new JSONFilterPersister( filtersTestPath );

		filterPersister.getFilter( "non-existent" );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testCannotModifyFilterNames()
	{
		File existingFiltersDirectory = new File( filtersTestPath, "filters/existing" );

		JSONFilterPersister filterPersister = new JSONFilterPersister( existingFiltersDirectory.getAbsolutePath() );

		Set<String> filterNames = filterPersister.getFilterNames();

		filterNames.add( "should-not-be-added" );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testCannotModifyFiltersSet() throws FilterSetNotFoundException, IOException
	{
		File existingFiltersDirectory = new File( filtersTestPath, "filters/existing" );

		JSONFilterPersister filterPersister = new JSONFilterPersister( existingFiltersDirectory.getAbsolutePath() );

		Set<Filter> konamiFilters = filterPersister.getFilter( "Konami MSX SCC 1987 128<256" );

		konamiFilters.add( FilterFactory.createFilter( FilterType.MEDIUM, "DISK", null, null ) );
	}
}
