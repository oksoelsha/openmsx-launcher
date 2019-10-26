package info.msxlaunchers.openmsx.launcher.ui.view.swing;

import info.msxlaunchers.openmsx.launcher.data.filter.FilterParameter;
import info.msxlaunchers.openmsx.launcher.data.filter.FilterType;

class FilterItemObject
{
	public final FilterType type;
	public final String value1;
	public final String value2;
	public final FilterParameter parameter;

	FilterItemObject(FilterType type, String value1, String value2, FilterParameter parameter)
	{
		this.type = type;
		this.value1 = value1;
		this.value2 = value2;
		this.parameter = parameter;
	}
}
