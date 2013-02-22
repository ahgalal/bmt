/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package gfx_panel;

import gfx_panel.GfxPanel.Direction;

import java.util.ArrayList;

/**
 * Snapping utility, to snap shapes to each other.
 * 
 * @author Creative
 */
public class Snapper {
	/**
	 * Snap Information, to guid the snapping process.
	 * 
	 * @author Creative
	 */
	private class SnapInfo {
		/**
		 * Direction of the snap.
		 */
		public Direction	direction;
		/**
		 * Distance to snap.
		 */
		public int			distance;
		/**
		 * Shapes involved in the process.
		 */
		public Shape		shpSnapTo, shpSnap;

		/**
		 * Initializes the snap information.
		 * 
		 * @param shpToSnap
		 *            Shape to snap (moving)
		 * @param shpToSnapTo
		 *            Shape to snap to (still)
		 * @param direction
		 *            direction of the snap
		 * @param distance
		 *            distance to snap
		 */
		public SnapInfo(final Shape shpToSnap, final Shape shpToSnapTo,
				final Direction direction, final int distance) {
			this.shpSnap = shpToSnap;
			this.shpSnapTo = shpToSnapTo;
			this.direction = direction;
			this.distance = distance;
		}
	}

	/**
	 * An indication on how the snap process completed.
	 * 
	 * @author Creative
	 */
	public class SnapResults {
		/**
		 * Did we snap in the x direction? y direction?
		 */
		private boolean	snappedX;
		private boolean	snappedY;

		public boolean setSnappedY(boolean snappedY) {
			this.snappedY = snappedY;
			return snappedY;
		}

		public boolean isSnappedY() {
			return snappedY;
		}

		public void setSnappedX(boolean snappedX) {
			this.snappedX = snappedX;
		}

		public boolean isSnappedX() {
			return snappedX;
		}
	}

	private final ArrayList<Shape>	shapes;

	private boolean					enableSnap;

	private final SnapResults		snpResults;

	/**
	 * Initializes the Snapper.
	 * 
	 * @param arrShapes
	 *            array of shapes to snap
	 */
	public Snapper(final ArrayList<Shape> arrShapes) {
		snpResults = new SnapResults();
		this.shapes = arrShapes;
	}

	/**
	 * Enable/Disable snapping.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void enableSnap(final boolean enable) {
		enableSnap = enable;
	}

	/**
	 * Snap position preparation.
	 * 
	 * @param shp
	 *            shape to snap
	 * @param curX
	 *            cursor position x
	 * @param curY
	 *            cursor position y
	 * @param cursorPosInShpX
	 *            cursor's position relative to the top-left of the selected
	 *            shape on the x axis
	 * @param cursorPosInShpY
	 *            cursor's position relative to the top-left of the selected
	 *            shape on the y axis
	 * @return SnapResults indicating the direction of the snap if performed
	 */
	public SnapResults prepareSnapPosition(final Shape shp, final int curX,
			final int curY, final int cursorPosInShpX,
			final int cursorPosInShpY) {
		if (enableSnap) {
			snpResults.setSnappedX(snpResults.setSnappedY(false));
			final int edgeX = curX - cursorPosInShpX, edgeY = curY
					- cursorPosInShpY;
			int distToTmpshpX, distToTmpshpY;
			final int shpX = shp.getX(), shpY = shp.getY(), shpW = shp
					.getWidth(), shpH = shp.getHeight();
			int tmpX, tmpY, tmpW, tmpH;
			for (final Shape tmpSh : shapes)
				if (tmpSh != shp) {
					tmpX = tmpSh.getX();
					tmpY = tmpSh.getY();
					tmpW = tmpSh.getWidth();
					tmpH = tmpSh.getHeight();

					distToTmpshpX = edgeX - tmpX - tmpW;
					distToTmpshpY = edgeY - tmpY - tmpH;

					final ArrayList<SnapInfo> snapArrayX = new ArrayList<SnapInfo>();
					final ArrayList<SnapInfo> snapArrayY = new ArrayList<SnapInfo>();

					if (((shpY >= tmpY) & (shpY < tmpY + tmpH))
							| ((tmpY > shpY) & (tmpY < shpY + shpH)))
						/*        _______
						 * ______ |     |     ______ 
						 * |     || shp |     |     |
						 * |     ||_____|     |     |
						 * | tmp |        OR  | tmp |_______        
						 * |     |            |     ||     |
						 * |_____|            |_____|| shp |
						 *                           |_____|
						 */
						if (edgeX > tmpX + tmpW) {
							// shp is to the RIGHT of tmp
							if ((distToTmpshpX < 10)
									& (distToTmpshpX > 0))
								snapArrayX.add(new SnapInfo(shp, tmpSh,
										Direction.RIGHT, distToTmpshpX));
						} else if (edgeX + shpW < tmpX) {
							// shp is to the LEFT of tmp
							distToTmpshpX = tmpX - edgeX - shpW;
							if ((distToTmpshpX < 10)
									& (distToTmpshpX > 0))
								snapArrayX.add(new SnapInfo(shp, tmpSh,
										Direction.LEFT, distToTmpshpX));
						} else
							/*
							 * cursor is in the middle of tmp, we must set
							 * snapped_x to disable moving shp
							 */
							snpResults.setSnappedX(true);

					if (((shpX >= tmpX) & (shpX < tmpX + tmpW))
							| ((tmpX > shpX) & (tmpX < shpX + shpW)))
						/*   _______            _______
						 *   |     |            |     |
						 *   | shp |            | shp |
						 *   |_____|            |_____|
						 *       ______      ______ 
						 *       |     |     |     |
						 *       |     |     |     |
						 *       | tmp | OR  | tmp |        
						 *       |     |     |     |
						 *       |_____|     |_____|
						 */
						if (edgeY > tmpY + tmpH) {
							// shp is BELOW tmp
							if ((distToTmpshpY < 10)
									& (distToTmpshpY > 0))
								snapArrayY.add(new SnapInfo(shp, tmpSh,
										Direction.DOWN, distToTmpshpY));
						} else if (edgeY + shpH < tmpY) {
							// shp is ONTOP of tmp
							distToTmpshpY = tmpY - edgeY - shpH;
							if ((distToTmpshpY < 10)
									& (distToTmpshpY > 0))
								snapArrayY.add(new SnapInfo(shp, tmpSh,
										Direction.UP, distToTmpshpY));
						} else
							/*
							 * cursor is in the middle of tmp, we must set
							 * snapped_y to disable moving shp
							 */
							snpResults.setSnappedY(true);
					int smallerDist = 1000, smallerDistIdx = 0, dist;
					if (snapArrayX.size() > 0) {
						// SNAP_X:
						for (int i = 0; i < snapArrayX.size(); i++) {
							dist = snapArrayX.get(i).distance;
							if (dist < smallerDist) {
								smallerDist = dist;
								smallerDistIdx = i;
							}
						}
						snap(snapArrayX.get(smallerDistIdx));
					}
					if (snapArrayY.size() > 0) {
						// SNAP_Y:
						smallerDist = 1000;
						smallerDistIdx = 0;
						for (int i = 0; i < snapArrayY.size(); i++) {
							dist = snapArrayY.get(i).distance;
							if (dist < smallerDist) {
								smallerDist = dist;
								smallerDistIdx = i;
							}
						}
						snap(snapArrayY.get(smallerDistIdx));
					}
				}
			return snpResults;
		}
		return null;
	}

	/**
	 * Snap size preparation.
	 * 
	 * @param shp
	 *            shape to snap
	 * @param x
	 *            cursor position on the x axis
	 * @param y
	 *            cursor position on the y axis
	 * @return SnapResults indicating the direction of the snap if performed
	 */
	public SnapResults prepareSnapSize(final Shape shp, final int x, final int y) {
		if (enableSnap) {
			snpResults.setSnappedX(snpResults.setSnappedY(false));
			final int shpX = shp.getX(), shpY = shp.getY(), shpW = shp
					.getWidth(), shpH = shp.getHeight();
			int tmpX, tmpY, tmpW, tmpH;
			for (final Shape tmpSh : shapes)
				if (tmpSh != shp) {
					tmpX = tmpSh.getX();
					tmpY = tmpSh.getY();
					tmpW = tmpSh.getWidth();
					tmpH = tmpSh.getHeight();

					// RESIZE_SNAP_X:
					if (((shpY >= tmpY) & (shpY < tmpY + tmpH))
							| ((tmpY > shpY) & (tmpY < shpY + shpH)))
						if ((tmpX - x < 10) & (tmpX - x >= 0)) {
							shp.setWidth(tmpX - shpX);
							snpResults.setSnappedX(true);
						} else if ((x - tmpX - tmpW < 10)
								& (x - tmpX - tmpW >= 0)) {
							shp.setX(tmpX + tmpW);
							shp.setWidth(shpW + shpX - tmpX - tmpW);
							snpResults.setSnappedX(true);
						}

					// RESIZE_SNAP_Y:
					if (((shpX >= tmpX) & (shpX < tmpX + tmpW))
							| ((tmpX > shpX) & (tmpX < shpX + shpW)))
						if ((tmpY - y < 10) & (tmpY - y >= 0)) {
							shp.setHeight(tmpY - shpY);
							snpResults.setSnappedY(true);
						} else if ((y - tmpY - tmpH < 10)
								& (y - tmpY - tmpH >= 0)) {
							shp.setY(tmpY + tmpH);
							shp.setHeight(shpH + shpY - tmpY - tmpH);
							snpResults.setSnappedY(true);
						}
				}
			return snpResults;
		}
		return null;
	}

	/**
	 * Snaps (moves) the active shape according to the Snap information.
	 * 
	 * @param snapInfo
	 *            snap information like direction
	 * @return SnapResults indicating the direction of the snap if performed
	 */
	public SnapResults snap(final SnapInfo snapInfo) {
		final Shape shpSnapTo = snapInfo.shpSnapTo, shpSnap = snapInfo.shpSnap;

		switch (snapInfo.direction) {
			case RIGHT:
				shpSnap.setX(shpSnapTo.getX() + shpSnapTo.getWidth());
				snpResults.setSnappedX(true);
				break;
			case LEFT:
				shpSnap.setX(shpSnapTo.getX() - shpSnap.getWidth());
				snpResults.setSnappedX(true);
				break;
			case DOWN:
				shpSnap.setY(shpSnapTo.getY() + shpSnapTo.getHeight());
				snpResults.setSnappedY(true);
				break;
			case UP:
				shpSnap.setY(shpSnapTo.getY() - shpSnap.getHeight());
				snpResults.setSnappedY(true);
				break;
		}
		return snpResults;
	}
}