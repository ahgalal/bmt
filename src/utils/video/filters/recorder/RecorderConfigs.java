package utils.video.filters.recorder;

import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;

public class RecorderConfigs extends FilterConfigs
{

	public RecorderConfigs(
			final String filt_name,
			final CommonFilterConfigs common_configs)
	{
		super(filt_name, common_configs);
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs)
	{
		if (configs.common_configs != null)
			this.common_configs = configs.common_configs;
	}

}
