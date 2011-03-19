package utils.video.processors.recorder;

import utils.video.processors.CommonFilterConfigs;
import utils.video.processors.FilterConfigs;

public class RecorderConfigs extends FilterConfigs {

	public RecorderConfigs(String filt_name,CommonFilterConfigs common_configs)
	{
		super(filt_name,common_configs);
	}
	@Override
	public void mergeConfigs(FilterConfigs configs) {
		if(configs.common_configs!=null)
			this.common_configs=configs.common_configs;
	}

}
