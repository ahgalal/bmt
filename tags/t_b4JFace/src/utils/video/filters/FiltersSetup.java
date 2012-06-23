package utils.video.filters;

import java.util.ArrayList;

/**
 * @author Creative
 */
public class FiltersSetup {
	private final ArrayList<String>	filtersNames;

	public FiltersSetup(final String[] filtersNames) {
		this.filtersNames = new ArrayList<String>();
		for (final String str : filtersNames)
			this.filtersNames.add(str);
	}

	public void addFilter(final String filterName) {
		filtersNames.add(filterName);
	}

	public String[] getFiltersNames() {
		return filtersNames.toArray(new String[0]);
	}

	public void removeFilter(final String filterName) {
		filtersNames.remove(filterName);
	}
}
