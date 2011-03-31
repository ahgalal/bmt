package utils.video.filters.RatFinder;

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
	private int tmp_max;
	private final RatFinderFilterConfigs ratfinder_configs;
	private final RatFinderData rat_finder_data;

	final int[] hori_sum;
	final int[] vert_sum;

	private final Point center_point;
	private final int[] local_data;

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
			final int mark_length = 30;
			final int len = binary_image.length;

			int tmp_x = center_point.x
					- mark_length
					+ center_point.y
					* ratfinder_configs.common_configs.width;
			int tmp_y = (center_point.y - mark_length)
					* ratfinder_configs.common_configs.width
					+ center_point.x;

			for (int c = 0; c < mark_length * 2; c++)
			{
				if (tmp_x < 0)
					tmp_x = 0;
				if (tmp_y < 0)
					tmp_y = 0;
				if (tmp_x < len & tmp_y < len)
				{
					local_data[tmp_x] = 0x00FF0000; // red
					if (tmp_x + ratfinder_configs.common_configs.width < len)
						local_data[tmp_x + ratfinder_configs.common_configs.width] = 0x00FF0000; // red
					local_data[tmp_y] = 0x00FF0000; // red
					if (tmp_y + 1 < len)
						local_data[tmp_y + 1] = 0x00FF0000; // red
				}
				tmp_x++;
				tmp_y += ratfinder_configs.common_configs.width;
			}
		} catch (final Exception e)
		{
			System.err.print("Error ya 3am el 7ag, fel index!");
			e.printStackTrace();
		}
	}

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
	public RatFinder(
			final String name,
			final FilterConfigs configs,
			final Link link_in,
			final Link link_out)
	{
		super(name, configs, link_in, link_out);
		ratfinder_configs = (RatFinderFilterConfigs) configs;
		rat_finder_data = new RatFinderData("Rat Finder Data");
		center_point = (Point) rat_finder_data.getCenterPoint();

		hori_sum = new int[ratfinder_configs.common_configs.height];
		vert_sum = new int[ratfinder_configs.common_configs.width];

		// super's stuff:
		filter_data = rat_finder_data;
		local_data = new int[configs.common_configs.width * configs.common_configs.height];
		this.link_out.setData(local_data);
	}

	/**
	 * Updates the center point (ie: finds the location of the moving object).
	 * 
	 * @param binary_image
	 */
	private void updateCentroid(final int[] binary_image)
	{
		tmp_max = 0;

		for (int y = 0; y < ratfinder_configs.common_configs.height; y++) // Horizontal
		// Sum
		{
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
	public boolean initialize()
	{
		return true;
	}

}
