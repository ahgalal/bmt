package utils.video.processors.rearingdetection;

import utils.video.processors.FilterData;

public class RearingData extends FilterData
{
	public RearingData(final String name)
	{
		super(name);
	}

	private boolean rearing;

	@Override
	public Object getData()
	{
		return null;
	}

	public boolean isRearing()
	{
		return rearing;
	}

	public void setRearing(final boolean rearing)
	{
		this.rearing = rearing;
	}

}
