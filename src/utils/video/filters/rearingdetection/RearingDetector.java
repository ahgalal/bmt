package utils.video.filters.rearingdetection;

import utils.video.filters.FilterConfigs;
import utils.video.filters.Link;
import utils.video.filters.VideoFilter;

/**
 * Detects whether the rat is rearing or not.
 * 
 * @author Creative
 */
public class RearingDetector extends VideoFilter
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
	public RearingDetector(String name, Link linkIn, Link linkOut)
	{
		super(name, linkIn, linkOut);
		rearing_configs = (RearingFilterConfigs) configs;
		rearing_data = new RearingData("Rearing Data");

		// super's stuff:
		filter_data = rearing_data;

		rearing_data.setRearing(false);
	}

	private RearingFilterConfigs rearing_configs;
	@Override
	public boolean configure(FilterConfigs configs)
	{
		rearing_configs= (RearingFilterConfigs)configs;
		return super.configure(configs);
	}

	private RearingData rearing_data;

	private boolean rearing_now;
	private int normal_rat_area;
	private boolean is_rearing;
	private int current_rat_area;


	/**
	 * Used to train the filter of the white area of the rat when
	 * (walking/rearing).
	 * 
	 * @param rearing
	 *            whether the rat is rearing now or not
	 */
	public void rearingNow(final boolean rearing)
	{

		if (rearing)
		{
			rearing_now = true;
			rearing_configs.rearing_thresh = (normal_rat_area + current_rat_area) / 2;
			System.out.print("Rearing threshold: "
					+ rearing_configs.rearing_thresh
					+ "\n");
		} else
		{
			rearing_now = false;
			final Thread th_rearing = new Thread(new NormalRatAreaThread());
			th_rearing.start();
			System.out.print("Rearing Training Started" + "\n");
		}

	}

	/**
	 * Runnable to calculate the mean rat area.
	 * 
	 * @author Creative
	 */
	private class NormalRatAreaThread implements Runnable
	{
		private static final long rat_area_training_time = 3;

		@Override
		public void run()
		{
			final long timer_start = (System.currentTimeMillis() / 1000);
			int tmp_rat_area_sum = 0, num_samples = 0;
			while (!rearing_now
					&& (System.currentTimeMillis() / 1000) - timer_start < rat_area_training_time)
			{
				num_samples++;
				tmp_rat_area_sum += current_rat_area;

				try
				{
					Thread.sleep(400);
				} catch (final InterruptedException e)
				{
				}
			}
			rearing_now = false;
			normal_rat_area = tmp_rat_area_sum / num_samples;
			System.out.print("normal rat area: " + normal_rat_area + "\n");
		}
	}

	@Override
	public void process()
	{
		final int[] imageData = link_in.getData();
		if (configs.enabled)
		{
			if (imageData != null)
			{
				int white_area = 0;
				for (int x = rearing_configs.ref_center_point.x
						- (rearing_configs.margin_x / 2); x < rearing_configs.ref_center_point.x
						+ (rearing_configs.margin_x / 2); x++)
					for (int y = rearing_configs.ref_center_point.y
							- (rearing_configs.margin_y / 2); y < rearing_configs.ref_center_point.y
							+ (rearing_configs.margin_y / 2); y++)
						if (x < rearing_configs.common_configs.width
								& x >= 0
								& y < rearing_configs.common_configs.height
								& y >= 0)
							if (imageData[x + y * rearing_configs.common_configs.width] == 0x00FFFFFF)
								white_area++;

				current_rat_area = white_area;
				if (current_rat_area < rearing_configs.rearing_thresh)
					is_rearing = true;
				else
					is_rearing = false;
			}
		}
		rearing_data.setRearing(is_rearing);
	}

}
