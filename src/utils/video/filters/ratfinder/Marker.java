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

package utils.video.filters.ratfinder;

import java.awt.Color;

/**
 * Parent of all markers, a marker is an object drawn on an image.
 * 
 * @author Creative
 */
public abstract class Marker
{
	/**
	 * Image's dimensions.
	 */
	protected int img_width, img_height;
	protected Color color;

	/**
	 * Initializes the marker.
	 * 
	 * @param img_width
	 *            image's width
	 * @param img_height
	 *            image's height
	 */
	public Marker(final int img_width, final int img_height,Color color)
	{
		this.img_width = img_width;
		this.img_height = img_height;
		this.color=color;
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
	protected void fillRect(
			final int[] img,
			int x,
			int y,
			final int width,
			final int height)
	{
		if (x < 0)
			x = 0;
		if (x + width > img_width)
			x = img_width - width - 1;
		if (y < 0)
			y = 0;
		if (y + height > img_height)
			y = img_height - height - 1;

		for (int i = x; i < x + width; i++)
		{
			for (int j = y; j < y + height; j++)
			{
				img[i + j * img_width] = color.getRGB();
			}
		}
	}

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
	protected void drawRect(
			final int[] img,
			int x,
			int y,
			final int width,
			final int height)
	{
		if (x < 0)
			x = 0;
		if (x + width > img_width)
			x = img_width - width - 1;
		if (y < 0)
			y = 0;
		if (y + height > img_height)
			y = img_height - height - 1;

		for (int i = x; i < x + width; i++)
		{
			img[i + y * img_width] = color.getRGB();
			img[i + (y + height) * img_width] = color.getRGB();
		}
		
		for (int j = y; j < y + height; j++)
		{
			img[x + j * img_width] = color.getRGB();
			img[x+width + j * img_width] = color.getRGB();
		}
	}
}
