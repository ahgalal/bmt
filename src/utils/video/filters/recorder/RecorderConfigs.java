package utils.video.filters.recorder;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;

/**
 * Configurations for the VideoRecorder filter.
 * 
 * @author Creative
 */
public class RecorderConfigs extends FilterConfigs
{

	/**
	 * Initializes the configurations.
	 * 
	 * @param filt_name
	 *            name of the filter this configurations will be applied to
	 * @param common_configs
	 *            CommonConfigurations used by all filters
	 */
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

	@Override
	public boolean validate()
	{
		if (common_configs == null)
		{
			PManager.log.print(
					"Configs are not completely configured!",
					this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

}
