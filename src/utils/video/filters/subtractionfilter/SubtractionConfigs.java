package utils.video.filters.subtractionfilter;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;

/**
 * Configuration of the SubtractorFilter.
 * 
 * @author Creative
 */
public class SubtractionConfigs extends FilterConfigs
{

	/**
	 * Initializes the configurations.
	 * 
	 * @param filt_name
	 *            name of the filter this configurations will be applied to
	 * @param threshold
	 *            subtraction threshold
	 * @param common_configs
	 *            CommonConfigurations used by all filters
	 */
	public SubtractionConfigs(
			final String filt_name,
			final int threshold,
			final CommonFilterConfigs common_configs)
	{
		super(filt_name, common_configs);
		this.threshold = threshold;
	}

	/**
	 * subtraction threshold, pixel value> threshold will be white, while pixel
	 * value < threshold will be black in the output image
	 */
	public int threshold;

	@Override
	public void mergeConfigs(final FilterConfigs configs)
	{
		final SubtractionConfigs tmp_subtraction_configs = (SubtractionConfigs) configs;
		if (tmp_subtraction_configs.common_configs != null)
			this.common_configs = tmp_subtraction_configs.common_configs;
		if (tmp_subtraction_configs.threshold != -1)
			this.threshold = tmp_subtraction_configs.threshold;
	}

	@Override
	public boolean validate()
	{
		if (common_configs==null || threshold <=0)
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
