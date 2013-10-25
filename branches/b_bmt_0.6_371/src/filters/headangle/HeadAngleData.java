/**
 * 
 */
package filters.headangle;

import java.awt.Point;

import modules.experiment.Constants;
import filters.FilterData;

/**
 * @author Creative
 */
public class HeadAngleData extends FilterData {
	public final static String	dataID	= Constants.FILTER_ID
												+ ".headangle.data";
	private final Point			center;

	private int angle;

	public HeadAngleData() {
		center = new Point();
	}

	public Point getCenterPoint() {
		return center;
	}

	/*
	 * (non-Javadoc)
	 * @see filters.Data#getId()
	 */
	@Override
	public String getId() {
		return dataID;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
}
