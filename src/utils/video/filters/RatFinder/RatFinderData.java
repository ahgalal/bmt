package utils.video.filters.RatFinder;

import java.awt.Point;

import utils.video.filters.FilterData;

/**
 * Data for the RatFinder filter.
 * @author Creative
 *
 */
public class RatFinderData extends FilterData
{

	private final Point center;

	/**
	 * Initialized the data.
	 * @param name name of the data object
	 */
	public RatFinderData(final String name)
	{
		super(name);
		this.center = new Point();
	}
	
	/**
	 * Gets the current location of the rat.
	 * @return Point object representing the rat's current position
	 */
	public Point getCenterPoint()
	{
		return center;
	}

}
