package utils.video.processors.RatFinder;

import java.awt.Point;

import utils.video.processors.FilterData;

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
