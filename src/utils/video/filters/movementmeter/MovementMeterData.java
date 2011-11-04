package utils.video.filters.movementmeter;

import utils.video.filters.FilterData;

public class MovementMeterData extends FilterData
{

	private int whiteSummation;
	public MovementMeterData(String name)
	{
		super(name);
	}
	public void setWhiteSummation(int whiteSummation)
	{
		this.whiteSummation = whiteSummation;
	}
	public int getWhiteSummation()
	{
		return whiteSummation;
	}

}
