package utils.video.processors.subtractionfilter;

import utils.video.processors.CommonFilterConfigs;
import utils.video.processors.FilterConfigs;

public class SubtractionConfigs extends FilterConfigs {

	public SubtractionConfigs(String filt_name,int threshold,CommonFilterConfigs common_configs) {
		super(filt_name,common_configs);
		this.threshold = threshold;
	}

	public int threshold;
	
	@Override
	public void mergeConfigs(FilterConfigs configs) {
		SubtractionConfigs tmp_subtraction_configs = (SubtractionConfigs) configs;
		if(tmp_subtraction_configs.common_configs!=null)
			this.common_configs=tmp_subtraction_configs.common_configs;
		if(tmp_subtraction_configs.threshold!=-1)
			this.threshold=tmp_subtraction_configs.threshold;
	}
}
