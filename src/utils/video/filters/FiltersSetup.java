package utils.video.filters;

import java.util.ArrayList;

/**
 * @author Creative
 *
 */
public class FiltersSetup
{
	private ArrayList<String> filtersNames;
	
	public FiltersSetup(String[] filtersNames)
	{
		this.filtersNames=new ArrayList<String>();
		for(String str:filtersNames)
			this.filtersNames.add(str);
	}
	
	public void addFilter(String filterName)
	{
		filtersNames.add(filterName);
	}
	
	public void removeFilter(String filterName)
	{
		filtersNames.remove(filterName);
	}
	
	public String[] getFiltersNames()
	{
		return filtersNames.toArray(new String[0]);
	}
}
