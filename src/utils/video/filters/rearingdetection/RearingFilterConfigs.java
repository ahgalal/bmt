package utils.video.filters.rearingdetection;

import java.awt.Point;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;

/**
 * Configurations for the RearingFilter filter.
 * @author Creative
 *
 */
public class RearingFilterConfigs extends FilterConfigs
{
	/**
	 * Initializes the configurations.
	 * @param filt_name name of the filter this configurations will be applied to
	 * @param rearingThresh rearing threshold
	 * @param marginX x-margin for searching around the current object's position
	 * @param marginY y-margin for searching around the current object's position
	 * @param ref_center_point reference to the center point (current location of the object)
	 * @param common_configs CommonConfigurations used by all filters
	 */
	public RearingFilterConfigs(
			final String filt_name,
			final int rearingThresh,
			final int marginX,
			final int marginY,
			final Point ref_center_point,
			final CommonFilterConfigs common_configs)
	{
		super(filt_name, common_configs);
		rearing_thresh = rearingThresh;
		margin_x = marginX;
		margin_y = marginY;
		this.ref_center_point = ref_center_point;
	}

	/**
	 * if white pixels number > rearingthreshold => not rearing rat
	 */
	public int rearing_thresh;
	/**
	 * x-margin for white pixel counting (around the current location of the object).
	 *  _________________________________
	 *  |               |               |
	 *  |               |y_margin       |
	 *  |               |               |
	 * 	|---x_margin----0----x_margin---|
	 *  |               |               |
	 *  |               |y_margin       |
	 *  |               |               |
	 *  |--------------------------------
	 *  0: current position of the object.
	 *  count white pixels inside this area only (to save the processing power)
	 */
	public int margin_x,margin_y;
	/**
	 * reference to the current object's position.
	 */
	public Point ref_center_point;

	@Override
	public void mergeConfigs(final FilterConfigs configs)
	{
		final RearingFilterConfigs tmp_rearing_configs = (RearingFilterConfigs) configs;
		if (tmp_rearing_configs.margin_x != -1)
			this.margin_x = tmp_rearing_configs.margin_x;
		if (tmp_rearing_configs.margin_y != -1)
			this.margin_y = tmp_rearing_configs.margin_y;
		if (tmp_rearing_configs.rearing_thresh != -1)
			this.rearing_thresh = tmp_rearing_configs.rearing_thresh;
		if (tmp_rearing_configs.ref_center_point != null)
			this.ref_center_point = tmp_rearing_configs.ref_center_point;
		if (tmp_rearing_configs.common_configs != null)
			this.common_configs = tmp_rearing_configs.common_configs;
	}

	@Override
	public boolean validate()
	{
		if (common_configs==null)
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