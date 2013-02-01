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

package filters.ratfinder;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import filters.FilterConfigs;
import filters.Link;


public class RatFinder2 extends RatFinder {
	public class ContigousArea {
		public Point	center;

		public ContigousArea() {
			center = new Point();
		}
	}

	int							border_color	= Color.RED.getRGB();
	int							botash_found	= 0;
	protected ArrayList<Point>	coveredGrid;

	public RatFinder2(final String name, final Link linkIn, final Link linkOut) {
		super(name, linkIn, linkOut);
		coveredGrid = new ArrayList<Point>();
		// TODO Auto-generated constructor stub
	}

	protected int contourEdge(final int[] img, final int x_start,
			final int y_start, final int width, final int height) {
		final int area;
		int pivot_x = 0, pivot_y = 0;
		int pivot_x_old = x_start, pivot_y_old = y_start;

		int start_i = -1;

		for (int j = 7; j >= 0; j--) // initial pivot
		{
			int x1, y1, x2, y2;
			x1 = (int) (Math.cos(0.785 * j) * 1.5);
			y1 = (int) (Math.sin(0.785 * j) * -1.5);
			x2 = (int) (Math.cos(0.785 * (j - 1 + 8) % 8) * 1.5);
			y2 = (int) (Math.sin(0.785 * (j - 1 + 8) % 8) * -1.5);

			if ((x_start + x1 < 0) || (x_start + x1 >= width)
					|| (y_start + y1 < 0) || (y_start + y1 >= height)
					|| (x_start + x2 < 0) || (x_start + x2 >= width)
					|| (y_start + y2 < 0) || (y_start + y2 >= height))
				continue;

			if ((img[x_start + x2 + width * (y2 + y_start)] == 0xFFFFFF)
					&& (img[x_start + x1 + width * (y1 + y_start)] == 0)) {
				pivot_x = x_start + x2;
				pivot_y = y_start + y2;
				// img[x_start+x2+width*(y2+y_start)]= border_color;
			}
		}

		int x = 0, y = 0;
		while ((pivot_x != x_start) || (pivot_y != y_start)) {
			/*
			 * Selecting the start angle "i"
			 */
			if (pivot_x - pivot_x_old == 1) {
				if (pivot_y - pivot_y_old == -1)
					start_i = 4;
				else if (pivot_y - pivot_y_old == 0)
					start_i = 3;
				else if (pivot_y - pivot_y_old == 1)
					start_i = 2;
			} else if (pivot_x - pivot_x_old == 0) {
				if (pivot_y - pivot_y_old == -1)
					start_i = 5;
				else if (pivot_y - pivot_y_old == 1)
					start_i = 1;
			} else if (pivot_x - pivot_x_old == -1)
				if (pivot_y - pivot_y_old == -1)
					start_i = 3;
				else if (pivot_y - pivot_y_old == 0)
					start_i = 7;
				else if (pivot_y - pivot_y_old == 1)
					start_i = 0;

			/*
			 * Selecting the next pivot point
			 */

			int trials = 0;
			for (int i = (start_i) % 8; i != (start_i + 1) % 8; i = (i - 1 + 8) % 8) {
				trials++;
				x = (int) (Math.cos(0.785 * i) * 1.5);
				y = (int) (Math.sin(0.785 * i) * -1.5);

				if ((pivot_x + x < 0) || (pivot_x + x >= width)
						|| (pivot_y + y < 0) || (pivot_y + y >= height))
					continue;
				else if ((img[pivot_x + x + width * (y + pivot_y)] == 0xFFFFFF)
						|| ((pivot_x + x == x_start) && (pivot_y + y == y_start))) {

					pivot_x_old = pivot_x;
					pivot_y_old = pivot_y;
					img[pivot_x + x + width * (y + pivot_y)] = border_color; // border
					pivot_x += x;
					pivot_y += y;
					break;
				}
			}
			if (trials == 7)
				return 0;
			// break;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.filters.RatFinder.RatFinder#drawMarkerOnImg(int[])
	 */
	@Override
	protected void drawMarkerOnImg(final int[] binaryImage) {
		// TODO Auto-generated method stub
		// super.drawMarkerOnImg(binaryImage);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * utils.video.filters.RatFinder.RatFinder#specialConfiguration(utils.video
	 * .filters.FilterConfigs)
	 */
	@Override
	protected void specialConfiguration(final FilterConfigs configs) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.filters.RatFinder.RatFinder#updateCentroid(int[])
	 */
	@Override
	protected void updateCentroid(final int[] binaryImage) {
		System.arraycopy(binaryImage, 0, out_data, 0, binaryImage.length);
		// walking on y, step=5
		y_loop: for (int y = 0; y < configs.common_configs.height; y += 5)
			// walking on x, step=1
			x_loop: for (int x = 0; x < configs.common_configs.width; x++)
				// if white ...
				if (out_data[x + y * configs.common_configs.width] == 0xFFFFFF) {
					contourEdge(out_data, x, y, configs.common_configs.width,
							configs.common_configs.height);
					botash_found++;
				} else if (out_data[x + y * configs.common_configs.width] == border_color)
					continue y_loop;
		System.out.print(botash_found + "\n");
		botash_found = 0;
	}

	public void writeImageToFile(final int[] img, final int width,
			final int height) {
		final File f = new File("C:\\img");
		try {
			final OutputStreamWriter osr = new OutputStreamWriter(
					new FileOutputStream(f));
			for (int ii = 0; ii < width; ii++) {
				for (int iii = 0; iii < height; iii++)
					try {
						osr.write(img[ii + iii * width]);
					} catch (final IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				try {
					osr.write("\n");
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}