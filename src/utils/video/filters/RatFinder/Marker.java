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

package utils.video.filters.RatFinder;

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

	/**
	 * Initializes the marker.
	 * 
	 * @param img_width
	 *            image's width
	 * @param img_height
	 *            image's height
	 */
	public Marker(final int img_width, final int img_height)
	{
		this.img_width = img_width;
		this.img_height = img_height;
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
}
