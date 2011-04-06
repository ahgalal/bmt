package utils.video.filters.RatFinder;

import java.awt.Color;
import java.awt.Point;

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
	 * @param configs
	 *            filter's configurations
	 * @param link_in
	 *            input Link for the filter
	 * @param link_out
	 *            output Link from the filter
	 */
	public RatFinder(String name, Link linkIn, Link linkOut)
	{
		super(name, linkIn, linkOut);
		rat_finder_data = new RatFinderData("Rat Finder Data");
		filter_data = rat_finder_data;
		center_point = rat_finder_data.getCenterPoint();
	}

	private int tmp_max;
	private RatFinderFilterConfigs ratfinder_configs;
	private RatFinderData rat_finder_data;

	int[] hori_sum;
	int[] vert_sum;

	private Point center_point;
	private int[] local_data;
	private Marker marker;

	/**
	 * Draws a cross at the center of the moving object.
	 * 
	 * @param binary_image
	 *            image to draw the cross on
	 */
	private void drawMarkerOnImg(final int[] binary_image)
	{
		System.arraycopy(binary_image, 0, local_data, 0, binary_image.length);

		try
		{
			marker.draw(local_data, center_point.x, center_point.y);
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
	private void updateCentroid(final int[] binary_image)
	{
		tmp_max = 0;

		for (int y = 0; y < ratfinder_configs.common_configs.height; y++) // Horizontal
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
		for (int x = 0; x < ratfinder_configs.common_configs.width; x++) // Vertical
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

		hori_sum = new int[ratfinder_configs.common_configs.height];
		vert_sum = new int[ratfinder_configs.common_configs.width];
		marker = new CrossMarker(
				50,
				50,
				5,
				Color.RED,
				configs.common_configs.width,
				configs.common_configs.height);

		// super's stuff:
		
		local_data = new int[configs.common_configs.width * configs.common_configs.height];
		this.link_out.setData(local_data);
		return super.configure(configs);
	}

}
