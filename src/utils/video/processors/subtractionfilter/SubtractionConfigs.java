package utils.video.processors.subtractionfilter;

import utils.video.FrameIntArray;
import utils.video.processors.CommonConfigs;
import utils.video.processors.FilterConfigs;

public class SubtractionConfigs extends FilterConfigs {

	public SubtractionConfigs(int threshold,FrameIntArray bg_gray,CommonConfigs common_configs) {
		super();
		this.threshold = threshold;
		this.bg_image_gray = bg_gray;
		this.common_configs=common_configs;
	}

	public int threshold;
	public FrameIntArray bg_image_gray;
	
	@Override
	public void mergeConfigs(FilterConfigs configs) {
		SubtractionConfigs tmp_subtraction_configs = (SubtractionConfigs) configs;
		if(tmp_subtraction_configs.bg_image_gray!=null)
			this.bg_image_gray=tmp_subtraction_configs.bg_image_gray;
		if(tmp_subtraction_configs.common_configs!=null)
			this.common_configs=tmp_subtraction_configs.common_configs;
		if(tmp_subtraction_configs.threshold!=-1)
			this.threshold=tmp_subtraction_configs.threshold;
	}
}
