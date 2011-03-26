package utils.video.filters.RatFinder;

import java.awt.Point;

import utils.video.filters.FilterData;

public class RatFinderData extends FilterData
{

	private final Point center;

	@Override
	public Object getData()
	{
		return center;
	}

	public RatFinderData(final String name)
	{
		super(name);
		this.center = new Point();
	}

}
