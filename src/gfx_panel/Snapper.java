package gfx_panel;

import gfx_panel.GfxPanel.Direction;

import java.util.ArrayList;

public class Snapper
{
	private boolean enable_snap;
	private final SnapResults snp_results;
	private final ArrayList<Shape> arr_shp;

	public Snapper(final ArrayList<Shape> arr_shp)
	{
		snp_results = new SnapResults();
		this.arr_shp = arr_shp;
	}

	public void enableSnap(final boolean enable)
	{
		enable_snap = enable;
	}

	public SnapResults snap(final SnapInfo snapInfo)
	{
		final Shape shp_snap_to = snapInfo.shp_snap_to, shp_snap = snapInfo.shp_snap;

		switch (snapInfo.direction)
		{
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

	public SnapResults prepareSnapPosition(
			final Shape shp,
			final int cur_x,
			final int cur_y,
			final int cursor_pos_in_shp_x,
			final int cursor_pos_in_shp_y)
	{
		if (enable_snap)
		{
			snp_results.snapped_x = snp_results.snapped_y = false;
			final int edge_x = cur_x - cursor_pos_in_shp_x, edge_y = cur_y
					- cursor_pos_in_shp_y;
			int dist_to_tmpshp_x, dist_to_tmpshp_y;
			final int shp_x = shp.getX(), shp_y = shp.getY(), shp_w = shp.getWidth(), shp_h = shp.getHeight();
			int tmp_x, tmp_y, tmp_w, tmp_h;
			for (final Shape tmp_sh : arr_shp)
			{
				if (tmp_sh != shp)
				{
					tmp_x = tmp_sh.getX();
					tmp_y = tmp_sh.getY();
					tmp_w = tmp_sh.getWidth();
					tmp_h = tmp_sh.getHeight();

					dist_to_tmpshp_x = edge_x - tmp_x - tmp_w;
					dist_to_tmpshp_y = edge_y - tmp_y - tmp_h;

					final ArrayList<SnapInfo> snap_array_x = new ArrayList<SnapInfo>();
					final ArrayList<SnapInfo> snap_array_y = new ArrayList<SnapInfo>();

					if ((shp_y >= tmp_y & shp_y < tmp_y + tmp_h)
							| (tmp_y > shp_y & tmp_y < shp_y + shp_h))
					{
						/*
						 * _______ ______ | | ______ | || shp | | | | ||_____| |
						 * | | tmp | OR | tmp |_______ | | | || | |_____|
						 * |_____|| shp | |_____|
						 */
						if (edge_x > tmp_x + tmp_w)
						{
							// shp is to the RIGHT of tmp
							if (dist_to_tmpshp_x < 10 & dist_to_tmpshp_x > 0)
								snap_array_x.add(new SnapInfo(
										shp,
										tmp_sh,
										Direction.RIGHT,
										dist_to_tmpshp_x));
						} else if (edge_x + shp_w < tmp_x)
						{
							// shp is to the LEFT of tmp
							dist_to_tmpshp_x = tmp_x - edge_x - shp_w;
							if (dist_to_tmpshp_x < 10 & dist_to_tmpshp_x > 0)
								snap_array_x.add(new SnapInfo(
										shp,
										tmp_sh,
										Direction.LEFT,
										dist_to_tmpshp_x));
						} else
							/*
							 * cursor is in the middle of tmp, we must set
							 * snapped_x to disable moving shp
							 */
							snp_results.snapped_x = true;
					}

					if ((shp_x >= tmp_x & shp_x < tmp_x + tmp_w)
							| (tmp_x > shp_x & tmp_x < shp_x + shp_w))
					{
						/*
						 * _______ _______ | | | | | shp | | shp | |_____|
						 * |_____| ______ ______ | | | | | | | | | tmp | OR |
						 * tmp | | | | | |_____| |_____|
						 */
						if (edge_y > tmp_y + tmp_h)
						{
							// shp is BELOW tmp
							if (dist_to_tmpshp_y < 10 & dist_to_tmpshp_y > 0)
								snap_array_y.add(new SnapInfo(
										shp,
										tmp_sh,
										Direction.DOWN,
										dist_to_tmpshp_y));
						} else if (edge_y + shp_h < tmp_y)
						{
							// shp is ONTOP of tmp
							dist_to_tmpshp_y = tmp_y - edge_y - shp_h;
							if (dist_to_tmpshp_y < 10 & dist_to_tmpshp_y > 0)
								snap_array_y.add(new SnapInfo(
										shp,
										tmp_sh,
										Direction.UP,
										dist_to_tmpshp_y));
						} else
							/*
							 * cursor is in the middle of tmp, we must set
							 * snapped_y to disable moving shp
							 */
							snp_results.snapped_y = true;
					}
					int smaller_dist = 1000, smaller_dist_idx = 0, dist;
					if (snap_array_x.size() > 0)
					{
						// SNAP_X:
						for (int i = 0; i < snap_array_x.size(); i++)
						{
							dist = snap_array_x.get(i).distance;
							if (dist < smaller_dist)
							{
								smaller_dist = dist;
								smaller_dist_idx = i;
							}
						}
						snap(snap_array_x.get(smaller_dist_idx));
					}
					if (snap_array_y.size() > 0)
					{
						// SNAP_Y:
						smaller_dist = 1000;
						smaller_dist_idx = 0;
						for (int i = 0; i < snap_array_y.size(); i++)
						{
							dist = snap_array_y.get(i).distance;
							if (dist < smaller_dist)
							{
								smaller_dist = dist;
								smaller_dist_idx = i;
							}
						}
						snap(snap_array_y.get(smaller_dist_idx));
					}
				}
			}
		}
		return snp_results;
	}

	public SnapResults prepareSnapSize(final Shape shp, final int x, final int y)
	{
		if (enable_snap)
		{
			snp_results.snapped_x = snp_results.snapped_y = false;
			final int shp_x = shp.getX(), shp_y = shp.getY(), shp_w = shp.getWidth(), shp_h = shp.getHeight();
			int tmp_x, tmp_y, tmp_w, tmp_h;
			for (final Shape tmp_sh : arr_shp)
			{
				if (tmp_sh != shp)
				{
					tmp_x = tmp_sh.getX();
					tmp_y = tmp_sh.getY();
					tmp_w = tmp_sh.getWidth();
					tmp_h = tmp_sh.getHeight();

					// RESIZE_SNAP_X:
					if ((shp_y >= tmp_y & shp_y < tmp_y + tmp_h)
							| (tmp_y > shp_y & tmp_y < shp_y + shp_h))
					{
						if (tmp_x - x < 10 & tmp_x - x >= 0)
						{
							shp.setWidth(tmp_x - shp_x);
							snp_results.snapped_x = true;
						} else if (x - tmp_x - tmp_w < 10 & x - tmp_x - tmp_w >= 0)
						{
							shp.setX(tmp_x + tmp_w);
							shp.setWidth(shp_w + shp_x - tmp_x - tmp_w);
							snp_results.snapped_x = true;
						}
					}

					// RESIZE_SNAP_Y:
					if ((shp_x >= tmp_x & shp_x < tmp_x + tmp_w)
							| (tmp_x > shp_x & tmp_x < shp_x + shp_w))
					{
						if (tmp_y - y < 10 & tmp_y - y >= 0)
						{
							shp.setHeight(tmp_y - shp_y);
							snp_results.snapped_y = true;
						} else if (y - tmp_y - tmp_h < 10 & y - tmp_y - tmp_h >= 0)
						{
							shp.setY(tmp_y + tmp_h);
							shp.setHeight(shp_h + shp_y - tmp_y - tmp_h);
							snp_results.snapped_y = true;
						}
					}
				}
			}
		}
		return snp_results;
	}

	private class SnapInfo
	{
		public Shape shp_snap_to, shp_snap;
		public Direction direction;
		public int distance;

		public SnapInfo(
				final Shape shp_to_snap,
				final Shape shp_to_snap_to,
				final Direction direction,
				final int distance)
		{
			this.shp_snap = shp_to_snap;
			this.shp_snap_to = shp_to_snap_to;
			this.direction = direction;
			this.distance = distance;
		}
	}

	public class SnapResults
	{
		public boolean snapped_x, snapped_y;
	}
}