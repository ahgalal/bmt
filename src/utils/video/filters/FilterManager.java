package utils.video.filters;

import java.util.ArrayList;

import modules.ModulesManager;

public class FilterManager
{
	private final ArrayList<VideoFilter> arr_filters;

	public FilterManager()
	{
		arr_filters = new ArrayList<VideoFilter>();
	}

	public VideoFilter getFilterByName(final String name)
	{
		for (final VideoFilter vf : arr_filters)
			if (vf.getName().equals(name))
				return vf;
		return null;
	}

	public void enableFilter(final String filter_name, final boolean enable)
	{
		final VideoFilter tmp = getFilterByName(filter_name);
		if (tmp != null)
			tmp.enable(enable);
	}

	private VideoFilter getFilterByType(final Class<?> type)
	{
		for (final VideoFilter vf : arr_filters)
			if (vf.getClass() == type)
				return vf;
		return null;
	}

	public void enableFilter(final Class<?> type, final boolean enable)
	{
		final VideoFilter tmp = getFilterByType(type);
		if (tmp != null)
			tmp.enable(enable);
	}

	public ArrayList<VideoFilter> getFilters()
	{
		return arr_filters;
	}

	public void addFilter(final VideoFilter filter)
	{
		arr_filters.add(filter);
	}

	public void removeFilter(final String filter_name)
	{
		final VideoFilter tmp = getFilterByName(filter_name);
		if (tmp != null)
			arr_filters.remove(tmp);
	}

	public void removeFilter(final Class<?> type)
	{
		final VideoFilter tmp = getFilterByType(type);
		if (tmp != null)
			arr_filters.remove(tmp);
	}

	public void applyConfigsToFilter(final FilterConfigs f_cfgs)
	{
		final VideoFilter tmp_filter = getFilterByName(f_cfgs.getConfigurablename());
		if (tmp_filter != null)
			tmp_filter.updateConfigs(f_cfgs);
	}

	public void disableAll()
	{
		for (final VideoFilter vf : arr_filters)
			vf.enable(false);
	}

	public void submitDataObjects()
	{
		for (final VideoFilter v : arr_filters)
			if (v.getFilterData() != null)
				ModulesManager.getDefault().addDataObject(v.getFilterData());
	}

}
