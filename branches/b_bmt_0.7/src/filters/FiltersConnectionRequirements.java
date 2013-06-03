package filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import utils.PManager;
import utils.StatusManager.StatusSeverity;

public class FiltersConnectionRequirements implements Serializable {
	/**
	 * 
	 */
	private static final long				serialVersionUID	= 2201049999499751252L;
	private final HashMap<String, String>	filtersConnections;

	public FiltersConnectionRequirements() {
		filtersConnections = new HashMap<String, String>();
	}

	public void clearConnections() {
		filtersConnections.clear();
	}

	public void connectFilters(final String srcFilterName,
			final String dstFilterName) {

		for (final String tmpDstFilter : filtersConnections.keySet())
			if (tmpDstFilter.equals(dstFilterName))
				PManager.log.print(
						"Destination filter is already connected to: "
								+ filtersConnections.get(tmpDstFilter), this,
						StatusSeverity.WARNING);
		filtersConnections.put(dstFilterName, srcFilterName);
	}

	/**
	 * Gets available connections as filter pairs: Src,Dst.
	 * 
	 * @return
	 */
	public ArrayList<String[]> getConnections() {
		final ArrayList<String[]> ret = new ArrayList<String[]>();

		for (final String dstFilterName : filtersConnections.keySet()) {
			final String srcFilterName = filtersConnections.get(dstFilterName);

			ret.add(new String[] { srcFilterName, dstFilterName });
		}

		return ret;
	}
}
