package utils.video.processors.recorder;

import utils.video.processors.CommonConfigs;
import utils.video.processors.FilterConfigs;

public class RecorderConfigs extends FilterConfigs {

	public RecorderConfigs(CommonConfigs common_configs)
	{
		this.common_configs=common_configs;
	}
	@Override
	public void mergeConfigs(FilterConfigs configs) {
		if(configs.common_configs!=null)
			this.common_configs=configs.common_configs;
	}

}
