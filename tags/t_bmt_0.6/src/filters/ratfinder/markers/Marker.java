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

package filters.ratfinder.markers;

import java.awt.Color;

/**
 * Parent of all markers, a marker is an object drawn on an image.
 * 
 * @author Creative
 */
public abstract class Marker {
	protected Color	color;
	/**
	 * Image's dimensions.
	 */
	protected int	imgWidth, imgHeight;

	/**
	 * Initializes the marker.
	 * 
	 * @param imgWidth
	 *            image's width
	 * @param imgHeight
	 *            image's height
	 */
	public Marker(final int imgWidth, final int imgHeight, final Color color) {
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		this.color = color;
	}

	/**
	 * Draws the marker on the specified image.
	 * 
	 * @param img
	 *            input image as an integer array
	 * @param x
	 *            x co-ordinate of the marker on the image
	 * @param y
	 *            y co-ordinate of the marker on the image
	 */
	public abstract void draw(int[] img, int x, int y);

	/**
	 * Draws the specified rectangular area of the image using the specified
	 * color.
	 * 
	 * @param img
	 *            image to fill the rectangle on.
	 * @param x
	 *            x co-ordinate of the rectangle
	 * @param y
	 *            y co-ordinate of the rectangle
	 * @param width
	 *            rectangle's width
	 * @param height
	 *            rectangle's height
	 */
	protected void drawRect(final int[] img, int x, int y, final int width,
			int height) {
		if (x < 0)
			x = 0;
		if (x + width >= imgWidth)
			x = imgWidth - width - 1;
		if (y < 0)
			y = 0;
		if (y + height >= imgHeight)
			height = imgHeight - y - 1;

		for (int i = x; i < x + width; i++) {
			if(i + y * imgWidth<img.length)
				img[i + y * imgWidth] = color.getRGB();
			if(i + (y + height) * imgWidth<img.length)
				img[i + (y + height) * imgWidth] = color.getRGB();
		}

		for (int j = y; j < y + height; j++) {
			img[x + j * imgWidth] = color.getRGB();
			if(x + width + j * imgWidth<img.length)
				img[x + width + j * imgWidth] = color.getRGB();
		}
	}

	/**
	 * Fills the specified rectangular area of the image with the specified
	 * color.
	 * 
	 * @param img
	 *            image to fill the rectangle on.
	 * @param x
	 *            x co-ordinate of the rectangle
	 * @param y
	 *            y co-ordinate of the rectangle
	 * @param width
	 *            rectangle's width
	 * @param height
	 *            rectangle's height
	 */
	protected void fillRect(final int[] img, int x, int y, final int width,
			final int height) {
		if (x < 0)
			x = 0;
		if (x + width > imgWidth)
			x = imgWidth - width - 1;
		if (y < 0)
			y = 0;
		if (y + height > imgHeight)
			y = imgHeight - height - 1;

		for (int i = x; i < x + width; i++)
			for (int j = y; j < y + height; j++)
				img[i + j * imgWidth] = color.getRGB();
	}
}
