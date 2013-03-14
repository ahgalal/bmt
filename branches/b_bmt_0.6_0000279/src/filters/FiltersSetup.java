package filters;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import filters.FiltersNamesRequirements.FilterRequirement;

/**
 * @author Creative
 */
public class FiltersSetup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -162165122516343763L;
	private final FiltersConnectionRequirements connectionRequirements;
	private transient FiltersCollection filters;
	private final FiltersNamesRequirements filtersRequirements;
	private transient ArrayList<Link> links;

	public FiltersSetup(final FiltersNamesRequirements filtersRequirements,
			final FiltersConnectionRequirements connectionRequirements) {
		this.filtersRequirements = filtersRequirements;
		this.connectionRequirements = connectionRequirements;
		links = new ArrayList<Link>();
	}

	/**
	 * Connects filters in FilterCollection instance according to specifications
	 * found in ConnectionRequirements instance.
	 */
	public void connectFilters() {
		if(links==null)
			links=new ArrayList<Link>();
		else
			links.clear();
		for (final String[] connection : connectionRequirements
				.getConnections()) {
			final String srcFilterName = connection[0];
			final String dstFilterName = connection[1];

			connectFilters(srcFilterName, dstFilterName);
		}
	}

	private void connectFilters(final String filterSrcName,
			final String filterDstName) {

		final VideoFilter<?, ?> srcFilter = filters
				.getFilterByName(filterSrcName);
		final VideoFilter<?, ?> dstFilter = filters
				.getFilterByName(filterDstName);

		Link lnk;
		if (srcFilter.getLinkOut() != null)
			lnk = srcFilter.getLinkOut();
		else {
			// TODO: make dims configurable instead of 640x480
			lnk = new Link(new Point(640, 480));
			srcFilter.setLinkOut(lnk);
		}

		dstFilter.setLinkIn(lnk);

		links.add(lnk);
		System.out.println("link added, from: " + filterSrcName + " to: "
				+ dstFilter.getName());
	}

	public FiltersConnectionRequirements getConnectionRequirements() {
		return connectionRequirements;
	}

	public ArrayList<VideoFilter<?, ?>> getFiltersByLinkIn(final Link linkIn) {
		final ArrayList<VideoFilter<?, ?>> ret = new ArrayList<VideoFilter<?, ?>>();
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> filter = it.next();
			if (filter.getLinkIn() == linkIn && linkIn !=null)
				ret.add(filter);
		}
		return ret;
	}
	
	public ArrayList<VideoFilter<?, ?>> getFiltersByLinkOut(final Link linkOut) {
		final ArrayList<VideoFilter<?, ?>> ret = new ArrayList<VideoFilter<?, ?>>();
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> filter = it.next();
			if (filter.getLinkOut() == linkOut && linkOut !=null)
				ret.add(filter);
		}
		return ret;
	}

	public FiltersNamesRequirements getFiltersNamesRequirements() {
		return filtersRequirements;
	}

	public void removeFilter(final String filterName) {
		final VideoFilter<?, ?> filter = filters.getFilterByName(filterName);

		// remove linkOut of the removed filter
		final Link linkOut = filter.getLinkOut();
		links.remove(linkOut);

		// remove linkOut from all dependent filters (i.e. linkOut is their
		// linkIn)
		for (final VideoFilter<?, ?> depFilter : getFiltersByLinkIn(linkOut)) {
			depFilter.setLinkIn(null);
		}

	}

	public void setFiltersCollection(final FiltersCollection filters) {
		for (final Iterator<FilterRequirement> it = filtersRequirements
				.getFilters(); it.hasNext();) {
			final FilterRequirement filterRequirement = it.next();

			// search in the input filtersCollection
			boolean found = false;
			for (final Iterator<VideoFilter<?, ?>> it2 = filters.getIterator(); it2
					.hasNext();) {
				final VideoFilter<?, ?> videoFilter = it2.next();

				if (videoFilter.getName().equals(filterRequirement.getName())
						&& videoFilter.getID().equals(filterRequirement.getID())) {
					found = true;
					break;
				}
			}

			if (found == false) {
				throw new RuntimeException(
						"Input Filters does not match that required by the Experiment");
			}
		}
		this.filters = filters;
	}

	/**
	 * Validates all connections, assures that all filters are connected and no
	 * broken links.
	 * 
	 * @return
	 */
	public boolean validateConnections() {
		// TODO
		return false;
	}
}
