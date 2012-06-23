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
		public Shape		shp_snap_to, shp_snap;

		/**
		 * Initializes the snap information.
		 * 
		 * @param shp_to_snap
		 *            Shape to snap (moving)
		 * @param shp_to_snap_to
		 *            Shape to snap to (still)
		 * @param direction
		 *            direction of the snap
		 * @param distance
		 *            distance to snap
		 */
		public SnapInfo(final Shape shp_to_snap, final Shape shp_to_snap_to,
				final Direction direction, final int distance) {
			this.shp_snap = shp_to_snap;
			this.shp_snap_to = shp_to_snap_to;
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
		public boolean	snapped_x, snapped_y;
	}

	private final ArrayList<Shape>	arr_shp;

	private boolean					enable_snap;

	private final SnapResults		snp_results;

	/**
	 * Initializes the Snapper.
	 * 
	 * @param arr_shp
	 *            array of shapes to snap
	 */
	public Snapper(final ArrayList<Shape> arr_shp) {
		snp_results = new SnapResults();
		this.arr_shp = arr_shp;
	}

	/**
	 * Enable/Disable snapping.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void enableSnap(final boolean enable) {
		enable_snap = enable;
	}

	/**
	 * Snap position preparation.
	 * 
	 * @param shp
	 *            shape to snap
	 * @param cur_x
	 *            cursor position x
	 * @param cur_y
	 *            cursor position y
	 * @param cursor_pos_in_shp_x
	 *            cursor's position relative to the top-left of the selected
	 *            shape on the x axis
	 * @param cursor_pos_in_shp_y
	 *            cursor's position relative to the top-left of the selected
	 *            shape on the y axis
	 * @return SnapResults indicating the direction of the snap if performed
	 */
	public SnapResults prepareSnapPosition(final Shape shp, final int cur_x,
			final int cur_y, final int cursor_pos_in_shp_x,
			final int cursor_pos_in_shp_y) {
		if (enable_snap) {
			snp_results.snapped_x = snp_results.snapped_y = false;
			final int edge_x = cur_x - cursor_pos_in_shp_x, edge_y = cur_y
					- cursor_pos_in_shp_y;
			int dist_to_tmpshp_x, dist_to_tmpshp_y;
			final int shp_x = shp.getX(), shp_y = shp.getY(), shp_w = shp
					.getWidth(), shp_h = shp.getHeight();
			int tmp_x, tmp_y, tmp_w, tmp_h;
			for (final Shape tmp_sh : arr_shp)
				if (tmp_sh != shp) {
					tmp_x = tmp_sh.getX();
					tmp_y = tmp_sh.getY();
					tmp_w = tmp_sh.getWidth();
					tmp_h = tmp_sh.getHeight();

					dist_to_tmpshp_x = edge_x - tmp_x - tmp_w;
					dist_to_tmpshp_y = edge_y - tmp_y - tmp_h;

					final ArrayList<SnapInfo> snap_array_x = new ArrayList<SnapInfo>();
					final ArrayList<SnapInfo> snap_array_y = new ArrayList<SnapInfo>();

					if (((shp_y >= tmp_y) & (shp_y < tmp_y + tmp_h))
							| ((tmp_y > shp_y) & (tmp_y < shp_y + shp_h)))
						/*
						 * _______ ______ | | ______ | || shp | | | | ||_____| |
						 * | | tmp | OR | tmp |_______ | | | || | |_____|
						 * |_____|| shp | |_____|
						 */
						if (edge_x > tmp_x + tmp_w) {
							// shp is to the RIGHT of tmp
							if ((dist_to_tmpshp_x < 10)
									& (dist_to_tmpshp_x > 0))
								snap_array_x.add(new SnapInfo(shp, tmp_sh,
										Direction.RIGHT, dist_to_tmpshp_x));
						} else if (edge_x + shp_w < tmp_x) {
							// shp is to the LEFT of tmp
							dist_to_tmpshp_x = tmp_x - edge_x - shp_w;
							if ((dist_to_tmpshp_x < 10)
									& (dist_to_tmpshp_x > 0))
								snap_array_x.add(new SnapInfo(shp, tmp_sh,
										Direction.LEFT, dist_to_tmpshp_x));
						} else
							/*
							 * cursor is in the middle of tmp, we must set
							 * snapped_x to disable moving shp
							 */
							snp_results.snapped_x = true;

					if (((shp_x >= tmp_x) & (shp_x < tmp_x + tmp_w))
							| ((tmp_x > shp_x) & (tmp_x < shp_x + shp_w)))
						/*
						 * _______ _______ | | | | | shp | | shp | |_____|
						 * |_____| ______ ______ | | | | | | | | | tmp | OR |
						 * tmp | | | | | |_____| |_____|
						 */
						if (edge_y > tmp_y + tmp_h) {
							// shp is BELOW tmp
							if ((dist_to_tmpshp_y < 10)
									& (dist_to_tmpshp_y > 0))
								snap_array_y.add(new SnapInfo(shp, tmp_sh,
										Direction.DOWN, dist_to_tmpshp_y));
						} else if (edge_y + shp_h < tmp_y) {
							// shp is ONTOP of tmp
							dist_to_tmpshp_y = tmp_y - edge_y - shp_h;
							if ((dist_to_tmpshp_y < 10)
									& (dist_to_tmpshp_y > 0))
								snap_array_y.add(new SnapInfo(shp, tmp_sh,
										Direction.UP, dist_to_tmpshp_y));
						} else
							/*
							 * cursor is in the middle of tmp, we must set
							 * snapped_y to disable moving shp
							 */
							snp_results.snapped_y = true;
					int smaller_dist = 1000, smaller_dist_idx = 0, dist;
					if (snap_array_x.size() > 0) {
						// SNAP_X:
						for (int i = 0; i < snap_array_x.size(); i++) {
							dist = snap_array_x.get(i).distance;
							if (dist < smaller_dist) {
								smaller_dist = dist;
								smaller_dist_idx = i;
							}
						}
						snap(snap_array_x.get(smaller_dist_idx));
					}
					if (snap_array_y.size() > 0) {
						// SNAP_Y:
						smaller_dist = 1000;
						smaller_dist_idx = 0;
						for (int i = 0; i < snap_array_y.size(); i++) {
							dist = snap_array_y.get(i).distance;
							if (dist < smaller_dist) {
								smaller_dist = dist;
								smaller_dist_idx = i;
							}
						}
						snap(snap_array_y.get(smaller_dist_idx));
					}
				}
		}
		return snp_results;
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
		if (enable_snap) {
			snp_results.snapped_x = snp_results.snapped_y = false;
			final int shp_x = shp.getX(), shp_y = shp.getY(), shp_w = shp
					.getWidth(), shp_h = shp.getHeight();
			int tmp_x, tmp_y, tmp_w, tmp_h;
			for (final Shape tmp_sh : arr_shp)
				if (tmp_sh != shp) {
					tmp_x = tmp_sh.getX();
					tmp_y = tmp_sh.getY();
					tmp_w = tmp_sh.getWidth();
					tmp_h = tmp_sh.getHeight();

					// RESIZE_SNAP_X:
					if (((shp_y >= tmp_y) & (shp_y < tmp_y + tmp_h))
							| ((tmp_y > shp_y) & (tmp_y < shp_y + shp_h)))
						if ((tmp_x - x < 10) & (tmp_x - x >= 0)) {
							shp.setWidth(tmp_x - shp_x);
							snp_results.snapped_x = true;
						} else if ((x - tmp_x - tmp_w < 10)
								& (x - tmp_x - tmp_w >= 0)) {
							shp.setX(tmp_x + tmp_w);
							shp.setWidth(shp_w + shp_x - tmp_x - tmp_w);
							snp_results.snapped_x = true;
						}

					// RESIZE_SNAP_Y:
					if (((shp_x >= tmp_x) & (shp_x < tmp_x + tmp_w))
							| ((tmp_x > shp_x) & (tmp_x < shp_x + shp_w)))
						if ((tmp_y - y < 10) & (tmp_y - y >= 0)) {
							shp.setHeight(tmp_y - shp_y);
							snp_results.snapped_y = true;
						} else if ((y - tmp_y - tmp_h < 10)
								& (y - tmp_y - tmp_h >= 0)) {
							shp.setY(tmp_y + tmp_h);
							shp.setHeight(shp_h + shp_y - tmp_y - tmp_h);
							snp_results.snapped_y = true;
						}
				}
		}
		return snp_results;
	}

	/**
	 * Snaps (moves) the active shape according to the Snap information.
	 * 
	 * @param snapInfo
	 *            snap information like direction
	 * @return SnapResults indicating the direction of the snap if performed
	 */
	public SnapResults snap(final SnapInfo snapInfo) {
		final Shape shp_snap_to = snapInfo.shp_snap_to, shp_snap = snapInfo.shp_snap;

		switch (snapInfo.direction) {
			case RIGHT:
				shp_snap.setX(shp_snap_to.getX() + shp_snap_to.getWidth());
				snp_results.snapped_x = true;
				break;
			case LEFT:
				shp_snap.setX(shp_snap_to.getX() - shp_snap.getWidth());
				snp_results.snapped_x = true;
				break;
			case DOWN:
				shp_snap.setY(shp_snap_to.getY() + shp_snap_to.getHeight());
				snp_results.snapped_y = true;
				break;
			case UP:
				shp_snap.setY(shp_snap_to.getY() - shp_snap.getHeight());
				snp_results.snapped_y = true;
				break;
		}
		return snp_results;
	}
}