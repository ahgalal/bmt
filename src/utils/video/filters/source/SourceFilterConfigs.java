package utils.video.filters.source;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.FrameIntArray;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;

/**
 * Configuration of the SourceFilter.
 * 
 * @author Creative
 */
public class SourceFilterConfigs extends FilterConfigs
{
	/**
	 * image data container.
	 */
	public FrameIntArray fia;

	/**
	 * @param name
	 *            name of the filter this configurations will be applied to
	 * @param commonConfigs
	 *            subtraction threshold
	 * @param fia
	 *            image data container coming from the device
	 */
	public SourceFilterConfigs(
			final String name,
			final CommonFilterConfigs commonConfigs,
			final FrameIntArray fia)
	{
		super(name, commonConfigs);
		this.fia = fia;
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs)
	{
		final SourceFilterConfigs tmp_srcfilter_configs = (SourceFilterConfigs) configs;
		if (tmp_srcfilter_configs.common_configs != null)
			common_configs = tmp_srcfilter_configs.common_configs;
		if(tmp_srcfilter_configs.fia!=null)
			this.fia=tmp_srcfilter_configs.fia;
	}

	@Override
	public boolean validate()
	{
		if (common_configs == null || fia == null)
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
