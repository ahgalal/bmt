package filters.rearingdetection;

import filters.FilterData;

/**
 * Stores RearingFilter's data.
 * 
 * @author Creative
 */
public class RearingData extends FilterData {
	private boolean	rearing;

	/**
	 * Initialized data.
	 * 
	 * @param name
	 *            name of the data object
	 */
	public RearingData(final String name) {
		super(name);
	}

	/**
	 * Gets the rearing status.
	 * 
	 * @return rearing status
	 */
	public boolean isRearing() {
		return rearing;
	}

	/**
	 * Set rearing status.
	 * 
	 * @param rearing
	 *            rearing status
	 */
	public void setRearing(final boolean rearing) {
		this.rearing = rearing;
	}

}
