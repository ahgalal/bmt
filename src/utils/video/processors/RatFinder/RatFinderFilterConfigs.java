package utils.video.processors.RatFinder;

import java.awt.Point;

import utils.video.processors.CommonFilterConfigs;
import utils.video.processors.FilterConfigs;

public class RatFinderFilterConfigs extends FilterConfigs {

	public int max_thresh;
	//public Point ref_center_point;


	public RatFinderFilterConfigs (String filt_name,int max_thresh, Point ref_center_point,CommonFilterConfigs common_configs)
	{
		super(filt_name,common_configs);
		this.max_thresh=max_thresh;
	}

	@Override
	public void mergeConfigs(FilterConfigs configs) {
		RatFinderFilterConfigs tmp_ratfiner_configs = (RatFinderFilterConfigs)configs;
		if(tmp_ratfiner_configs.common_configs!=null)
			common_configs=tmp_ratfiner_configs.common_configs;
		if(tmp_ratfiner_configs.max_thresh!=-1)
			max_thresh=tmp_ratfiner_configs.max_thresh;
		//if(tmp_ratfiner_configs.ref_center_point!=null)
			//ref_center_point=tmp_ratfiner_configs.ref_center_point;
	}

}
