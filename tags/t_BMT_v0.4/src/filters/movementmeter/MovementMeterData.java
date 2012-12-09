package filters.movementmeter;

import filters.FilterData;

public class MovementMeterData extends FilterData {

	private int	whiteSummation;

	public MovementMeterData(final String name) {
		super(name);
	}

	public int getWhiteSummation() {
		return whiteSummation;
	}

	public void setWhiteSummation(final int whiteSummation) {
		this.whiteSummation = whiteSummation;
	}

}
