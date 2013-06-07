/**
 * 
 */
package modules.movementmeter;

import java.awt.Point;

/**
 * @author Creative
 */
public class LocationVector {

	private int		distance;
	private Point	end;
	private int		phase;
	private Point	start;

	public LocationVector(final Point start, final Point end) {
		this.start = start;
		this.end = end;
		update();
	}

	public int getDistance() {
		return distance;
	}

	public Point getEnd() {
		return end;
	}

	public int getPhase() {
		return phase;
	}

	public Point getStart() {
		return start;
	}

	public void setEnd(final Point end) {
		this.end = end;
		update();
	}

	public void setStart(final Point start) {
		this.start = start;
		update();
	}

	private void update() {
		if ((start != null) && (end != null)) {
			distance = (int) end.distance(start);
			final double deltaX = end.x - start.x;
			final double deltaY = end.y - start.y;

			// (+,0) or same location --> east
			if ((deltaX == 0) && (deltaY == 0))
				phase = 0;
			// (+,+)
			else if ((deltaX > 0) && (deltaY > 0))
				phase = 45;
			// (0,+)
			else if ((deltaX == 0) && (deltaY > 0))
				phase = 90;
			// (-,+)
			else if ((deltaX < 0) && (deltaY > 0))
				phase = 135;
			// (-,0)
			else if ((deltaX < 0) && (deltaY == 0))
				phase = 180;
			// (-,-)
			else if ((deltaX < 0) && (deltaY < 0))
				phase = 225;
			// (0,-)
			else if ((deltaX == 0) && (deltaY < 0))
				phase = 270;
			// (+,-)
			else if ((deltaX > 0) && (deltaY < 0))
				phase = 315;
		}
	}

}
