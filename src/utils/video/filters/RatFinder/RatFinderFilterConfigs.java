package utils.video.filters.RatFinder;

import java.awt.Point;

import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;

public class RatFinderFilterConfigs extends FilterConfigs
{

	public int max_thresh;

	// public Point ref_center_point;

	public RatFinderFilterConfigs(
			final String filt_name,
			final int max_thresh,
			final Point ref_center_point,
			final CommonFilterConfigs common_configs)
	{
		super(filt_name, common_configs);
		this.max_thresh = max_thresh;
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs)
	{
		final RatFinderFilterConfigs tmp_ratfiner_configs = (RatFinderFilterConfigs) configs;
		if (tmp_ratfiner_configs.common_configs != null)
			common_configs = tmp_ratfiner_configs.common_configs;
		if (tmp_ratfiner_configs.max_thresh != -1)
			max_thresh = tmp_ratfiner_configs.max_thresh;
		// if(tmp_ratfiner_configs.ref_center_point!=null)
		// ref_center_point=tmp_ratfiner_configs.ref_center_point;
	}

}
