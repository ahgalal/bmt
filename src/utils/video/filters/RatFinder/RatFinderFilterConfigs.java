package utils.video.filters.RatFinder;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;

/**
 * Configurations for the RatFinder filter.
 * 
 * @author Creative
 */
public class RatFinderFilterConfigs extends FilterConfigs
{

	/**
	 * Initializes the configurations.
	 * 
	 * @param filt_name
	 *            name of the filter this configurations will be applied to
	 * @param common_configs
	 *            CommonConfigurations used by all filters
	 */
	public RatFinderFilterConfigs(
			final String filt_name,
			final CommonFilterConfigs common_configs)
	{
		super(filt_name, common_configs);
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs)
	{
		final RatFinderFilterConfigs tmp_ratfiner_configs = (RatFinderFilterConfigs) configs;
		if (tmp_ratfiner_configs.common_configs != null)
			common_configs = tmp_ratfiner_configs.common_configs;
	}

	/**
	 * Checks that All configurations are set. (for testing purposes only)
	 * 
	 * @return true: success
	 */
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
