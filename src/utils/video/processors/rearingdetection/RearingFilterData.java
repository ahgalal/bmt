package utils.video.processors.rearingdetection;

import utils.video.processors.FilterSpecialData;

public class RearingFilterData extends FilterSpecialData{
	private boolean rearing;

	@Override
	public Object getData() {
		return null;
	}
	
	public boolean isRearing()
	{
		return rearing;
	}

	public void setRearing(boolean rearing) {
		this.rearing = rearing;
	}
	
	
}
