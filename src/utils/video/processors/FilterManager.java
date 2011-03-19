package utils.video.processors;

import java.util.ArrayList;

import modules.ModulesManager;


public class FilterManager {
	private ArrayList<VideoFilter> arr_filters;

	public FilterManager()
	{
		arr_filters=new ArrayList<VideoFilter>();
	}

	public VideoFilter getFilterByName(String name)
	{
		for(VideoFilter vf: arr_filters)
			if(vf.getName().equals(name))
				return vf;
		return null;
	}


	public void enableFilter(String filter_name,boolean enable)
	{
		VideoFilter tmp = getFilterByName(filter_name);
		if(tmp!=null)
			tmp.enable(enable);
	}

	private VideoFilter getFilterByType(Class<?> type)
	{
		for(VideoFilter vf: arr_filters)
			if(vf.getClass() == type)
				return vf;
		return null;
	}

	public void enableFilter(Class<?> type,boolean enable)
	{
		VideoFilter tmp = getFilterByType(type);
		if(tmp!=null)
			tmp.enable(enable);
	}

	public ArrayList<VideoFilter> getFilters()
	{
		return arr_filters;
	}

	public void addFilter(VideoFilter filter)
	{
		arr_filters.add(filter);
	}

	public void removeFilter(String filter_name)
	{
		VideoFilter tmp = getFilterByName(filter_name);
		if(tmp!=null)
			arr_filters.remove(tmp);
	}

	public void removeFilter(Class<?> type)
	{
		VideoFilter tmp = getFilterByType(type);
		if(tmp!=null)
			arr_filters.remove(tmp);
	}

	public void applyConfigsToFilter(FilterConfigs f_cfgs)
	{
		VideoFilter tmp_filter = getFilterByName(f_cfgs.getConfigurable_name());
		if(tmp_filter!=null)
			tmp_filter.updateConfigs(f_cfgs);
	}

	public void disableAll() {
		for(VideoFilter vf: arr_filters)
			vf.enable(false);
	}

	public void submitDataObjects()
	{
		for(VideoFilter v: arr_filters)
			if(v.getFilterData()!=null)
				ModulesManager.getDefault().addDataObject(v.getFilterData());
	}

}
