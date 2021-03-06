package filters;

import java.util.ArrayList;
import java.util.Iterator;

public class FiltersCollection {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= 4727846193802923762L;
	private final ArrayList<VideoFilter<?, ?>>	filters;

	public FiltersCollection() {
		filters = new ArrayList<VideoFilter<?, ?>>();
	}

	public void add(final VideoFilter<?, ?> filter) {
		filters.add(filter);
	}

	public void clear() {
		filters.clear();
	}

	/**
	 * Gets a filter using the filter's name.
	 * 
	 * @param name
	 *            name of the filter to retrieve
	 * @return VideoFilter having the name specified
	 */
	public VideoFilter<?, ?> getFilterByName(final String name) {
		for (final VideoFilter<?, ?> vf : filters)
			if (vf.getName().equals(name))
				return vf;
		return null;
	}

	/**
	 * Gets a filter using the filter's type.
	 * 
	 * @param type
	 *            type of filter to retrieve (child of VideoFilter class)
	 * @return VideoFilter having type specified (in case of many filters have
	 *         the same type, first one found will be returned)
	 */
	public VideoFilter<?, ?> getFilterByType(final Class<?> type) {
		for (final VideoFilter<?, ?> vf : filters)
			if (vf.getClass() == type)
				return vf;
		return null;
	}

	// TODO: are we breaking encapsulation with this methid????
	public ArrayList<VideoFilter<?, ?>> getFilters() {
		return filters;
	}

	public Iterator<VideoFilter<?, ?>> getIterator() {
		return filters.iterator();
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param type
	 *            type of the filter to remove (first occurrence only of the
	 *            type will be removed)
	 */
	public void removeFilter(final Class<?> type) {
		final VideoFilter<?, ?> tmp = getFilterByType(type);
		if (tmp != null)
			filters.remove(tmp);
	}

	/**
	 * Removes filter from the array.
	 * 
	 * @param filterName
	 *            name of the filter to remove
	 */
	public void removeFilter(final String filterName) {
		final VideoFilter<?, ?> tmp = getFilterByName(filterName);
		if (tmp != null)
			filters.remove(tmp);
	}
}
