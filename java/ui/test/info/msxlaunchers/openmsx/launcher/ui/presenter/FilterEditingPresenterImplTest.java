package info.msxlaunchers.openmsx.launcher.ui.presenter;

import info.msxlaunchers.openmsx.launcher.data.filter.Filter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterFactory;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterType;
import info.msxlaunchers.openmsx.launcher.data.settings.constants.Language;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterPersister;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterSetAlreadyExistsException;
import info.msxlaunchers.openmsx.launcher.persistence.filter.FilterSetNotFoundException;
import info.msxlaunchers.openmsx.launcher.ui.presenter.LauncherException;
import info.msxlaunchers.openmsx.launcher.ui.view.FilterEditingView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class FilterEditingPresenterImplTest
{
	@Mock MainPresenter mainPresenter;
	@Mock FilterEditingView filterEditingView;
	@Mock FilterPersister filterPersister;

	private final String editMode = "editMode";
	private final String changedFilter = "changedFilter";

	@Test
	public void testConstructor()
	{
		new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg1Null()
	{
		new FilterEditingPresenterImpl( null, filterEditingView, filterPersister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg2Null()
	{
		new FilterEditingPresenterImpl( mainPresenter, null, filterPersister );
	}

	@Test( expected = NullPointerException.class )
	public void testConstructorArg3Null()
	{
		new FilterEditingPresenterImpl( mainPresenter, filterEditingView, null );
	}

	@Test
	public void testOnRequestAddFilterScreen()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		presenter.onRequestAddFilterScreen( Language.ENGLISH, false );

		verify( filterEditingView, times( 1 ) ).displayAddFilterScreen( eq( Language.ENGLISH ), eq( false ) );
		assertFalse( getPrivateFieldBooleanValue( presenter, editMode ) );
		assertFalse( getPrivateFieldBooleanValue( presenter, changedFilter ) );
	}

	@Test
	public void testOnRequestEditFilterScreen()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		String filterName = "filterName";
		Set<Filter> filterItems = new HashSet<Filter>();

		presenter.onRequestEditFilterScreen( Language.ENGLISH, false, filterName, filterItems );

		verify( filterEditingView, times( 1 ) ).displayEditFilterScreen( eq( Language.ENGLISH ), eq( false ), eq( filterName ), any( String[].class ) );
		assertTrue( getPrivateFieldBooleanValue( presenter, editMode ) );
		assertFalse( getPrivateFieldBooleanValue( presenter, changedFilter ) );
	}

	@Test
	public void testOnAddToFilterListAndApply_ExistingFilterAlready()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		FilterType type = FilterType.COMPANY;
		String value1 = "value1";
		String value2 = null;
		FilterParameter parameter = FilterParameter.EQUAL;

		Set<Filter> filterItemsSet = getPrivateFilterItemsSet( presenter );
		filterItemsSet.add( FilterFactory.createFilter( type, value1, value2, parameter ) );

		presenter.onAddToFilterListAndApply( type, value1, value2, parameter );

		verify( mainPresenter, never() ).onApplyFilter( filterItemsSet );
		assertFalse( getPrivateFieldBooleanValue( presenter, editMode ) );
		assertTrue( getPrivateFieldBooleanValue( presenter, changedFilter ) );
	}

	@Test
	public void testOnAddToFilterListAndApply_NonExistingFilter()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		FilterType type = FilterType.COMPANY;
		String value1 = "value1";
		String value2 = null;
		FilterParameter parameter = FilterParameter.EQUAL;

		Set<Filter> filterItemsSet = getPrivateFilterItemsSet( presenter );

		presenter.onAddToFilterListAndApply( type, value1, value2, parameter );

		verify( mainPresenter, times( 1 ) ).onApplyFilter( filterItemsSet );
		assertFalse( getPrivateFieldBooleanValue( presenter, editMode ) );
		assertTrue( getPrivateFieldBooleanValue( presenter, changedFilter ) );
	}

	@Test
	public void testOnRemoveFromFilterList()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		FilterType type = FilterType.COMPANY;
		String value1 = "value1";
		String value2 = null;
		FilterParameter parameter = FilterParameter.EQUAL;

		Set<Filter> filterItemsSet = getPrivateFilterItemsSet( presenter );

		presenter.onRemoveFromFilterList( type, value1, value2, parameter );

		assertEquals( filterItemsSet.size(), 0 );

		verify( mainPresenter, times( 1 ) ).onApplyFilter( filterItemsSet );
		assertFalse( getPrivateFieldBooleanValue( presenter, editMode ) );
		assertTrue( getPrivateFieldBooleanValue( presenter, changedFilter ) );
	}

	@Test
	public void testOnRequestSaveFilterAction()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FilterSetAlreadyExistsException, IOException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		Set<Filter> filterItemsSet = getPrivateFilterItemsSet( presenter );
		String filterName = "filterName";

		presenter.onRequestSaveFilterAction( filterName );

		verify( filterPersister, times( 1 ) ).saveFilter( filterName, filterItemsSet );
		verify( mainPresenter, times( 1 ) ).onRequestUpdateFilterName( filterName );
		assertFalse( getPrivateFieldBooleanValue( presenter, editMode ) );
		assertFalse( getPrivateFieldBooleanValue( presenter, changedFilter ) );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestSaveFilterActio_ExistingFilterAlready()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FilterSetAlreadyExistsException, IOException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		Set<Filter> filterItemsSet = getPrivateFilterItemsSet( presenter );
		String filterName = "filterName";

		Mockito.doThrow( new FilterSetAlreadyExistsException( filterName ) ).when( filterPersister ).saveFilter( filterName, filterItemsSet );

		presenter.onRequestSaveFilterAction( filterName );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestSaveFilterActio_SaveIOException()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, FilterSetAlreadyExistsException, IOException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		Set<Filter> filterItemsSet = getPrivateFilterItemsSet( presenter );
		String filterName = "filterName";

		Mockito.doThrow( new IOException() ).when( filterPersister ).saveFilter( filterName, filterItemsSet );

		presenter.onRequestSaveFilterAction( filterName );
	}

	@Test
	public void testOnRequestUpdateFilterAction_UpdateWithSameName()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, IOException, FilterSetAlreadyExistsException, FilterSetNotFoundException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		String oldFilterName = "name";
		String newFilterName = "name";

		presenter.onRequestUpdateFilterAction( oldFilterName, newFilterName );

		Set<Filter> filterItemsSet = getPrivateFilterItemsSet( presenter );

		verify( filterPersister, never() ).getFilter( oldFilterName );
		verify( filterPersister, times( 1 ) ).deleteFilter( oldFilterName );
		verify( filterPersister, times( 1 ) ).saveFilter( newFilterName, filterItemsSet );
		verify( mainPresenter, times( 1 ) ).onRequestUpdateFilterName( newFilterName );
	}

	@Test
	public void testOnRequestUpdateFilterAction_UpdateWithDifferentName()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, IOException, FilterSetAlreadyExistsException, FilterSetNotFoundException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		String oldFilterName = "oldName";
		String newFilterName = "newName";

		when( filterPersister.getFilter( newFilterName ) ).thenThrow( new FilterSetNotFoundException( oldFilterName ) );
		presenter.onRequestUpdateFilterAction( oldFilterName, newFilterName );

		Set<Filter> filterItemsSet = getPrivateFilterItemsSet( presenter );

		verify( filterPersister, times( 1 ) ).getFilter( newFilterName );
		verify( filterPersister, times( 1 ) ).deleteFilter( oldFilterName );
		verify( filterPersister, times( 1 ) ).saveFilter( newFilterName, filterItemsSet );
		verify( mainPresenter, times( 1 ) ).onRequestUpdateFilterName( newFilterName );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestUpdateFilterAction_NewNameExists()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, IOException, FilterSetAlreadyExistsException, FilterSetNotFoundException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		String oldFilterName = "oldName";
		String newFilterName = "newName";

		presenter.onRequestUpdateFilterAction( oldFilterName, newFilterName );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestUpdateFilterAction_DeleteFilterIOException()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, IOException, FilterSetAlreadyExistsException, FilterSetNotFoundException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		String oldFilterName = "name";
		String newFilterName = "name";

		Mockito.doThrow( new IOException() ).when( filterPersister ).deleteFilter( oldFilterName );
		presenter.onRequestUpdateFilterAction( oldFilterName, newFilterName );
	}

	@Test( expected = LauncherException.class )
	public void testOnRequestUpdateFilterAction_SaveFilterIOException()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, IOException, FilterSetAlreadyExistsException, FilterSetNotFoundException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		String oldFilterName = "name";
		String newFilterName = "name";

		Mockito.doThrow( new IOException() ).when( filterPersister ).saveFilter( oldFilterName, getPrivateFilterItemsSet( presenter ) );
		presenter.onRequestUpdateFilterAction( oldFilterName, newFilterName );
	}

	@Test
	public void testOnRequestClose()
			throws LauncherException, SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException
	{
		FilterEditingPresenterImpl presenter = new FilterEditingPresenterImpl( mainPresenter, filterEditingView, filterPersister );

		//case 1
		setPrivateFieldBooleanValue( presenter, editMode, false );
		setPrivateFieldBooleanValue( presenter, changedFilter, false );

		presenter.onRequestClose();
		verify( mainPresenter, never() ).onRequestSetFilterNameUntitled();

		//case 2
		setPrivateFieldBooleanValue( presenter, editMode, true );
		setPrivateFieldBooleanValue( presenter, changedFilter, false );

		presenter.onRequestClose();
		verify( mainPresenter, never() ).onRequestSetFilterNameUntitled();

		//case 3
		setPrivateFieldBooleanValue( presenter, editMode, false );
		setPrivateFieldBooleanValue( presenter, changedFilter, true );

		presenter.onRequestClose();
		verify( mainPresenter, never() ).onRequestSetFilterNameUntitled();

		//case 4
		setPrivateFieldBooleanValue( presenter, editMode, true );
		setPrivateFieldBooleanValue( presenter, changedFilter, true );

		presenter.onRequestClose();
		verify( mainPresenter, times( 1 ) ).onRequestSetFilterNameUntitled();
	}

	private boolean getPrivateFieldBooleanValue( FilterEditingPresenterImpl presenter, String fieldName )
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field field = presenter.getClass().getDeclaredField( fieldName );
		field.setAccessible( true );

		return field.getBoolean( presenter );
	}

	private void setPrivateFieldBooleanValue( FilterEditingPresenterImpl presenter, String fieldName, boolean value )
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field field = presenter.getClass().getDeclaredField( fieldName );
		field.setAccessible( true );

		field.setBoolean( presenter, value );
	}

	@SuppressWarnings("unchecked")
	private Set<Filter> getPrivateFilterItemsSet( FilterEditingPresenterImpl presenter )
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Field filterItemsSetField = presenter.getClass().getDeclaredField( "filterItemsSet" );
		filterItemsSetField.setAccessible( true );

		return Set.class.cast( filterItemsSetField.get( presenter ) );
	}
}