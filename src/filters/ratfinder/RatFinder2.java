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

	private int							borderColor	= Color.RED.getRGB();
	private int							botashFound	= 0;
	protected ArrayList<Point>	coveredGrid;

	public RatFinder2(final String name, final Link linkIn, final Link linkOut) {
		super(name, linkIn, linkOut);
		coveredGrid = new ArrayList<Point>();
		// TODO Auto-generated constructor stub
	}

	protected int contourEdge(final int[] img, final int xStart,
			final int yStart, final int width, final int height) {
		final int area;
		int pivotX = 0, pivotY = 0;
		int pivotXOld = xStart, pivotYOld = yStart;

		int startI = -1;

		for (int j = 7; j >= 0; j--) // initial pivot
		{
			int x1, y1, x2, y2;
			x1 = (int) (Math.cos(0.785 * j) * 1.5);
			y1 = (int) (Math.sin(0.785 * j) * -1.5);
			x2 = (int) (Math.cos(0.785 * (j - 1 + 8) % 8) * 1.5);
			y2 = (int) (Math.sin(0.785 * (j - 1 + 8) % 8) * -1.5);

			if ((xStart + x1 < 0) || (xStart + x1 >= width)
					|| (yStart + y1 < 0) || (yStart + y1 >= height)
					|| (xStart + x2 < 0) || (xStart + x2 >= width)
					|| (yStart + y2 < 0) || (yStart + y2 >= height))
				continue;

			if ((img[xStart + x2 + width * (y2 + yStart)] == 0xFFFFFF)
					&& (img[xStart + x1 + width * (y1 + yStart)] == 0)) {
				pivotX = xStart + x2;
				pivotY = yStart + y2;
			}
		}

		int x = 0, y = 0;
		while ((pivotX != xStart) || (pivotY != yStart)) {
			/*
			 * Selecting the start angle "i"
			 */
			if (pivotX - pivotXOld == 1) {
				if (pivotY - pivotYOld == -1)
					startI = 4;
				else if (pivotY - pivotYOld == 0)
					startI = 3;
				else if (pivotY - pivotYOld == 1)
					startI = 2;
			} else if (pivotX - pivotXOld == 0) {
				if (pivotY - pivotYOld == -1)
					startI = 5;
				else if (pivotY - pivotYOld == 1)
					startI = 1;
			} else if (pivotX - pivotXOld == -1)
				if (pivotY - pivotYOld == -1)
					startI = 3;
				else if (pivotY - pivotYOld == 0)
					startI = 7;
				else if (pivotY - pivotYOld == 1)
					startI = 0;

			/*
			 * Selecting the next pivot point
			 */

			int trials = 0;
			for (int i = (startI) % 8; i != (startI + 1) % 8; i = (i - 1 + 8) % 8) {
				trials++;
				x = (int) (Math.cos(0.785 * i) * 1.5);
				y = (int) (Math.sin(0.785 * i) * -1.5);

				if ((pivotX + x < 0) || (pivotX + x >= width)
						|| (pivotY + y < 0) || (pivotY + y >= height))
					continue;
				else if ((img[pivotX + x + width * (y + pivotY)] == 0xFFFFFF)
						|| ((pivotX + x == xStart) && (pivotY + y == yStart))) {

					pivotXOld = pivotX;
					pivotYOld = pivotY;
					img[pivotX + x + width * (y + pivotY)] = borderColor; // border
					pivotX += x;
					pivotY += y;
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
		System.arraycopy(binaryImage, 0, outData, 0, binaryImage.length);
		// walking on y, step=5
		yLoop: for (int y = 0; y < configs.getCommonConfigs().getHeight(); y += 5)
			// walking on x, step=1
			xLoop: for (int x = 0; x < configs.getCommonConfigs().getWidth(); x++)
				// if white ...
				if (outData[x + y * configs.getCommonConfigs().getWidth()] == 0xFFFFFF) {
					contourEdge(outData, x, y, configs.getCommonConfigs().getWidth(),
							configs.getCommonConfigs().getHeight());
					botashFound++;
				} else if (outData[x + y * configs.getCommonConfigs().getWidth()] == borderColor)
					continue yLoop;
		System.out.print(botashFound + "\n");
		botashFound = 0;
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
