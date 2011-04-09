package utils.video.filters.rearingdetection;

import utils.video.filters.FilterData;

/**
 * Stores RearingFilter's data.
 * 
 * @author Creative
 */
public class RearingFilterData extends FilterData
{
	/**
	 * Initialized data.
	 * 
	 * @param name
	 *            name of the data object
	 */
	public RearingFilterData(final String name)
	{
		super(name);
	}

	private boolean rearing;

	/**
	 * Gets the rearing status.
	 * 
	 * @return rearing status
	 */
	public boolean isRearing()
	{
		return rearing;
	}

	/**
	 * Set rearing status.
	 * 
	 * @param rearing
	 *            rearing status
	 */
	public void setRearing(final boolean rearing)
	{
		this.rearing = rearing;
	}

}
