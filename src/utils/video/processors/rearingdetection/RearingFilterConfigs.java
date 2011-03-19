package utils.video.processors.rearingdetection;

import java.awt.Point;

import utils.video.processors.CommonFilterConfigs;
import utils.video.processors.FilterConfigs;


public class RearingFilterConfigs extends FilterConfigs
{
	public RearingFilterConfigs(String filt_name,int rearingThresh, int marginX, int marginY,Point ref_center_point,CommonFilterConfigs common_configs) {
		super(filt_name,common_configs);
		rearing_thresh = rearingThresh;
		margin_x = marginX;
		margin_y = marginY;
		this.ref_center_point=ref_center_point;
	}
	public int rearing_thresh;
	public int margin_x;
	public int margin_y;
	public Point ref_center_point;

	public void mergeConfigs(FilterConfigs configs)
	{
		RearingFilterConfigs tmp_rearing_configs = (RearingFilterConfigs)configs;
		if(tmp_rearing_configs.margin_x!=-1)
			this.margin_x=tmp_rearing_configs.margin_x;
		if(tmp_rearing_configs.margin_y!=-1)
			this.margin_y=tmp_rearing_configs.margin_y;
		if(tmp_rearing_configs.rearing_thresh!=-1)
			this.rearing_thresh=tmp_rearing_configs.rearing_thresh;
		if(tmp_rearing_configs.ref_center_point!=null)
			this.ref_center_point=tmp_rearing_configs.ref_center_point;
		if(tmp_rearing_configs.common_configs!=null)
			this.common_configs = tmp_rearing_configs.common_configs;
	}
	
}