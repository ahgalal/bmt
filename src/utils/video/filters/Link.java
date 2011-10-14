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

package utils.video.filters;

import java.awt.Point;

/**
 * Link between filters, to transfer image data.
 * 
 * @author Creative
 */
public class Link
{
	private int[] img_data;

	/**
	 * Initializes image data.
	 * 
	 * @param dims
	 *            dimensions of the image the link is to deal with
	 */
	public Link(final Point dims)
	{
		img_data = new int[dims.x * dims.y];
	}

	/**
	 * Gets image's data on the link.
	 * 
	 * @return array of integers representing the image's data
	 */
	public int[] getData()
	{
		return img_data;
	}

	/**
	 * Sets the image data on the link.
	 * 
	 * @param newdata
	 *            new data to put on the link
	 */
	public void setData(final int[] newdata)
	{
		img_data = newdata;
	}
}
