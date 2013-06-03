package filters;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.FiltersNamesRequirements.FilterRequirement;

/**
 * @author Creative
 */
public class FiltersSetup implements Serializable {
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -162165122516343763L;
	private final FiltersConnectionRequirements	connectionRequirements;
	private Point								dims;
	private transient FiltersCollection			filters;
	private final FiltersNamesRequirements		filtersRequirements;

	private transient ArrayList<Link>			links;

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
	public boolean connectFilters(final Point dims) {
		if (links == null)
			links = new ArrayList<Link>();
		else
			links.clear();
		this.dims = dims;

		if (validateConnections()) {
			for (final String[] connection : connectionRequirements
					.getConnections()) {
				final String srcFilterName = connection[0];
				final String dstFilterName = connection[1];

				connectFilters(srcFilterName, dstFilterName);
			}
			return true;
		}
		return false;
	}

	private void connectFilters(final String filterSrcName,
			final String filterDstName) {

		final VideoFilter<?, ?> srcFilter = filters
				.getFilterByName(filterSrcName);
		final VideoFilter<?, ?> dstFilter = filters
				.getFilterByName(filterDstName);

		Link lnk;
		final Link srcLinkOut = srcFilter.getLinkOut();
		if ((srcLinkOut != null)
				&& (dims.x * dims.y == srcFilter.getLinkOut().getData().length))
			lnk = srcFilter.getLinkOut();
		else {
			lnk = new Link(dims);
			srcFilter.setLinkOut(lnk);
		}

		dstFilter.setLinkIn(lnk);

		links.add(lnk);
/*		System.out.println("link added, from: " + filterSrcName + " to: "
				+ dstFilter.getName());*/
	}

	public FiltersConnectionRequirements getConnectionRequirements() {
		return connectionRequirements;
	}

	public ArrayList<VideoFilter<?, ?>> getFiltersByLinkIn(final Link linkIn) {
		final ArrayList<VideoFilter<?, ?>> ret = new ArrayList<VideoFilter<?, ?>>();
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> filter = it.next();
			if ((filter.getLinkIn() == linkIn) && (linkIn != null))
				ret.add(filter);
		}
		return ret;
	}

	public ArrayList<VideoFilter<?, ?>> getFiltersByLinkOut(final Link linkOut) {
		final ArrayList<VideoFilter<?, ?>> ret = new ArrayList<VideoFilter<?, ?>>();
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> filter = it.next();
			if ((filter.getLinkOut() == linkOut) && (linkOut != null))
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
						&& videoFilter.getID()
								.equals(filterRequirement.getID())) {
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
		for (final Iterator<VideoFilter<?, ?>> it = filters.getIterator(); it
				.hasNext();) {
			final VideoFilter<?, ?> filter = it.next();
			final int inPortCount = filter.getInPortCount();
			final int outPortCount = filter.getOutPortCount();

			int inPortCountActual = 0;
			int outPortCountActual = 0;
			for (final String[] connection : connectionRequirements
					.getConnections()) {
				final String srcFilterName = connection[0];
				final String dstFilterName = connection[1];

				// filter is connected on its input port
				if (dstFilterName.equals(filter.getName()))
					inPortCountActual++;

				// filter is connected on its output port
				if (srcFilterName.equals(filter.getName()))
					outPortCountActual++;
			}

			if (inPortCount != inPortCountActual) {
				PManager.log.print("Filter: " + filter.getName()
						+ " is not properly connected on its input side", this,
						StatusSeverity.ERROR);
				return false;
			}

			if ((outPortCount == 0) && (outPortCountActual > 0)) {
				PManager.log.print("Filter: " + filter.getName()
						+ " does not have an output port!", this,
						StatusSeverity.ERROR);
				return false;
			}
		}
		return true;
	}

}
