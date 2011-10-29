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
import java.awt.Point;

import utils.PManager.ProgramState;
import utils.video.filters.FilterConfigs;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

/**
 * Finds the moving object's position.
 * 
 * @author Creative
 */
public class RatFinder extends VideoFilter
{
	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param linkIn
	 *            input Link for the filter
	 * @param linkOut
	 *            output Link from the filter
	 */
	public RatFinder(final String name, final Link linkIn, final Link linkOut)
	{
		super(name, linkIn, linkOut);
		rat_finder_data = new RatFinderData("Rat Finder Data");
		filter_data = rat_finder_data;
		center_point = rat_finder_data.getCenterPoint();
	}

	private int tmp_max;
	private final int searchSideLength = 100;
	protected RatFinderFilterConfigs ratfinder_configs;
	protected final RatFinderData rat_finder_data;

	int[] hori_sum;
	int[] vert_sum;

	protected final Point center_point;
	protected int[] out_data;
	protected Marker marker, marker2;

	/**
	 * Draws a cross at the center of the moving object.
	 * 
	 * @param binary_image
	 *            image to draw the cross on
	 */
	protected void drawMarkerOnImg(final int[] binary_image)
	{
		System.arraycopy(binary_image, 0, out_data, 0, binary_image.length);

		try
		{
			marker.draw(out_data, center_point.x, center_point.y);
			marker2.draw(out_data, center_point.x-searchSideLength/2, center_point.y-searchSideLength/2);
		} catch (final Exception e)
		{
			System.err.print("Error in marker");
			e.printStackTrace();
		}
	}

	/**
	 * Updates the center point (ie: finds the location of the moving object).
	 * 
	 * @param binary_image
	 *            input image
	 */
	protected void updateCentroid(final int[] binary_image)
	{
		tmp_max = 0;

		int y1, y2;
		if (center_point.y == 0)
		{
			y1 = 0;
			y2 = ratfinder_configs.common_configs.height;
		}
		else
		{
			y1 = (center_point.y - searchSideLength) < 0 ? 0 : center_point.y - 40;
			y2 = (center_point.y + searchSideLength) > ratfinder_configs.common_configs.height ? ratfinder_configs.common_configs.height
					: center_point.y + searchSideLength;
		}
		for (int y = y1; y < y2; y++) // Horizontal
		// Sum
		{
			hori_sum[y] = 0;
			for (int x = 0; x < ratfinder_configs.common_configs.width; x++)
				hori_sum[y] += binary_image[y
						* ratfinder_configs.common_configs.width
						+ x] & 0xff;
			if (hori_sum[y] > tmp_max)
			{
				center_point.y = y;
				tmp_max = hori_sum[y];
			}
		}

		tmp_max = 0;

		int x1, x2;
		if (center_point.x == 0)
		{
			x1 = 0;
			x2 = ratfinder_configs.common_configs.width;
		}
		else
		{
			x1 = (center_point.x - searchSideLength) < 0 ? 0 : center_point.x - 40;
			x2 = (center_point.x + searchSideLength) > ratfinder_configs.common_configs.width ? ratfinder_configs.common_configs.width
					: center_point.x + searchSideLength;
		}

		for (int x = x1; x < x2; x++) // Vertical
		// Sum
		{
			vert_sum[x] = 0;
			for (int y = 0; y < ratfinder_configs.common_configs.height; y++)
				vert_sum[x] += binary_image[y
						* ratfinder_configs.common_configs.width
						+ x] & 0xff;
			if (vert_sum[x] > tmp_max)
			{
				center_point.x = x;
				tmp_max = vert_sum[x];
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoUtility#process(int[])
	 */
	@Override
	public void process()
	{
		if (configs.enabled)
		{
			updateCentroid(link_in.getData());
			drawMarkerOnImg(link_in.getData());
		}
	}

	@Override
	public boolean configure(
			final FilterConfigs configs)
	{
		ratfinder_configs = (RatFinderFilterConfigs) configs;

		marker = new CrossMarker(
				50,
				50,
				5,
				Color.RED,
				configs.common_configs.width,
				configs.common_configs.height);

		marker2 = new RectangularMarker(
				configs.common_configs.width,
				configs.common_configs.height,
				searchSideLength,
				searchSideLength,
				Color.RED
				);

		// super's stuff:

		out_data = new int[configs.common_configs.width * configs.common_configs.height];
		this.link_out.setData(out_data);
		specialConfiguration(ratfinder_configs);
		return super.configure(configs);
	}

	protected void specialConfiguration(final FilterConfigs configs)
	{
		hori_sum = new int[ratfinder_configs.common_configs.height];
		vert_sum = new int[ratfinder_configs.common_configs.width];
	}

	@Override
	public void updateProgramState(final ProgramState state)
	{
		// TODO Auto-generated method stub

	}

}
