package utils.video.filters.RatFinder;

import java.awt.Point;

import utils.video.filters.FilterConfigs;
import utils.video.filters.VideoFilter;

public class RatFinder extends VideoFilter
{

	private int tmp_max;
	private final RatFinderFilterConfigs ratfinder_configs;
	private final RatFinderData rat_finder_data;
	// private RatFinderAnalyzer rat_finder_analyzer;

	private final Point center_point;

	private void drawMarkerOnImg(final int[] binary_image)
	{
		final int[] buf_data = binary_image;

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
					buf_data[tmp_x] = 0x00FF0000; // red
					if (tmp_x + ratfinder_configs.common_configs.width < len)
						buf_data[tmp_x + ratfinder_configs.common_configs.width] = 0x00FF0000; // red
					buf_data[tmp_y] = 0x00FF0000; // red
					if (tmp_y + 1 < len)
						buf_data[tmp_y + 1] = 0x00FF0000; // red
				}
				tmp_x++;
				tmp_y += ratfinder_configs.common_configs.width;
			}
			/*
			 * int i=(ratfinder_configs.ref_center_point.x-mark_length +
			 * ratfinder_configs
			 * .ref_center_point.y*ratfinder_configs.common_configs.width); int
			 * j=(ratfinder_configs.ref_center_point.x +
			 * (ratfinder_configs.ref_center_point
			 * .y-mark_length)*ratfinder_configs.common_configs.width); for(int
			 * c=0;c<mark_length*2;c++)//i<(pos_x+mark_length + pos_y*width)*3 {
			 * if(i<0)i=ratfinder_configs.ref_center_point.y*ratfinder_configs.
			 * common_configs.width; if(j<0)
			 * j=ratfinder_configs.ref_center_point.x; if(i<len & j<len){
			 * buf_data[i]= 0x00FF0000; //red buf_data[j]= 0x00FF0000; //red }
			 * j+=ratfinder_configs.common_configs.width; i+=1; }
			 */
		} catch (final Exception e)
		{
			System.err.print("Error ya 3am el 7ag, fel index!");
			e.printStackTrace();
		}
	}

	public RatFinder(final String name, final FilterConfigs configs)
	{
		super(name, configs);
		ratfinder_configs = (RatFinderFilterConfigs) configs;
		rat_finder_data = new RatFinderData("Rat Finder Data");
		center_point = (Point) rat_finder_data.getData();
		// rat_finder_analyzer = new RatFinderAnalyzer(rat_finder_data);

		// super's stuff:
		filter_data = rat_finder_data;
	}

	private void updateCentroid(final int[] binary_image)
	{
		tmp_max = ratfinder_configs.max_thresh;
		final int[] hori_sum = new int[ratfinder_configs.common_configs.height];
		final int[] vert_sum = new int[ratfinder_configs.common_configs.width];
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

		tmp_max = ratfinder_configs.max_thresh;
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
		// System.out.print(Integer.toString(center_point.x) + " " +
		// Integer.toString(center_point.y) + "\n");
		// System.out.print("V: " +
		// Integer.toString(vert_sum[start_point.x])+"         H :" +
		// Integer.toString(vert_sum[start_point.y])+"\n");
	}

	/*
	 * (non-Javadoc)
	 * @see utils.video.processors.VideoUtility#process(int[])
	 */
	@Override
	public int[] process(final int[] imageData)
	{
		if (configs.enabled)
		{
			updateCentroid(imageData);
			drawMarkerOnImg(imageData);
		}
		return imageData;
	}

	@Override
	public boolean initialize()
	{
		return false;
	}

}
