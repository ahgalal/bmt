package utils.video.filters;

import java.util.ArrayList;

import modules.ModulesManager;

/**
 * Manager of all filters, to enable, disable filters.
 * 
 * @author Creative
 */
public class FilterManager
{
	private final ArrayList<VideoFilter> arr_filters;

	/**
	 * Initializes the filters' array.
	 */
	public FilterManager()
	{
		arr_filters = new ArrayList<VideoFilter>();
	}

	/**
	 * Gets a filter using the filter's name.
	 * 
	 * @param name
	 *            name of the filter to retrieve
	 * @return VideoFilter having the name specified
	 */
	public VideoFilter getFilterByName(final String name)
	{
		for (final VideoFilter vf : arr_filters)
			if (vf.getName().equals(name))
				return vf;
		return null;
	}

	/**
	 * Enables/Disables a filter using its name.
	 * 
	 * @param filter_name
	 *            name of the filter to enable/disable
	 * @param enable
	 *            enable/disable
	 */
	public void enableFilter(final String filter_name, final boolean enable)
	{
		final VideoFilter tmp = getFilterByName(filter_name);
		if (tmp != null)
			tmp.enable(enable);
	}

	/**
	 * Gets a filter using the filter's type.
	 * 
	 * @param type
	 *            type of filter to retrieve (child of VideoFilter class)
	 * @return VideoFilter having type specified (in case of many filters have
	 *         the same type, first one found will be returned)
	 */
	private VideoFilter getFilterByType(final Class<?> type)
	{
		for (final VideoFilter vf : arr_filters)
			if (vf.getClass() == type)
				return vf;
		return null;
	}

	/**
	 * Enables/Disables a filter using its type.
	 * 
	 * @param type
	 *            type of the filter to enable/disable
	 * @param enable
	 *            enable/disable
	 */
	public void enableFilter(final Class<?> type, final boolean enable)
	{
		final VideoFilter tmp = getFilterByType(type);
		if (tmp != null)
			tmp.enable(enable);
	}

	/**
	 * Gets the array of filters.
	 * 
	 * @return array of filters
	 */
	public ArrayList<VideoFilter> getFilters()
	{
		return arr_filters;
	}

	/**
	 * Adds a filter to the array.
	 * 
	 * @param filter
	 *            filter to be added
	 */
	public void addFilter(final VideoFilter filter)
	{
		arr_filters.add(filter);
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param filter_name
	 *            name of the filter to remove
	 */
	public void removeFilter(final String filter_name)
	{
		final VideoFilter tmp = getFilterByName(filter_name);
		if (tmp != null)
			arr_filters.remove(tmp);
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param type
	 *            type of the filter to remove (first occurrence only of the
	 *            type will be removed)
	 */
	public void removeFilter(final Class<?> type)
	{
		final VideoFilter tmp = getFilterByType(type);
		if (tmp != null)
			arr_filters.remove(tmp);
	}

	/**
	 * Applies a configuration object to a filter, using the name of the filter
	 * specified in the configuration object.
	 * 
	 * @param cfgs
	 *            configurations object
	 */
	public void applyConfigsToFilter(final FilterConfigs cfgs)
	{
		final VideoFilter tmp_filter = getFilterByName(cfgs.getConfigurablename());
		if (tmp_filter != null)
			tmp_filter.updateConfigs(cfgs);
	}

	/**
	 * Disables all filter.
	 */
	public void disableAll()
	{
		for (final VideoFilter vf : arr_filters)
			vf.enable(false);
	}

	/**
	 * Submits/Registers data objects of all filters to the ModuleManager, for
	 * the modules to receive data from Filters.
	 */
	public void submitDataObjects()
	{
		for (final VideoFilter v : arr_filters)
			if (v.getFilterData() != null)
				ModulesManager.getDefault().addDataObject(v.getFilterData());
	}

}
