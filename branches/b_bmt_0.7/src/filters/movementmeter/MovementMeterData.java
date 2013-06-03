package filters.movementmeter;

import modules.experiment.Constants;
import filters.FilterData;

public class MovementMeterData extends FilterData {
	public final static String dataID=Constants.FILTER_ID+".movementmeter.data";
	private int	whiteSummation;

	public int getWhiteSummation() {
		return whiteSummation;
	}

	public void setWhiteSummation(final int whiteSummation) {
		this.whiteSummation = whiteSummation;
	}

	@Override
	public String getId() {
		return dataID;
	}

}
