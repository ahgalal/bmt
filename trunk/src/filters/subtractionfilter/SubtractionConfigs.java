/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package filters.subtractionfilter;

import filters.CommonFilterConfigs;
import filters.FilterConfigs;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * Configuration of the SubtractorFilter.
 * 
 * @author Creative
 */
public class SubtractionConfigs extends FilterConfigs {

	/**
	 * subtraction threshold, pixel value> threshold will be white, while pixel
	 * value < threshold will be black in the output image.
	 */
	public int	threshold;

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
	public SubtractionConfigs(final String filt_name, final int threshold,
			final CommonFilterConfigs common_configs) {
		super(filt_name, common_configs);
		this.threshold = threshold;
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs) {
		final SubtractionConfigs tmp_subtraction_configs = (SubtractionConfigs) configs;
		if (tmp_subtraction_configs.common_configs != null)
			this.common_configs = tmp_subtraction_configs.common_configs;
		if (tmp_subtraction_configs.threshold != -1)
			this.threshold = tmp_subtraction_configs.threshold;
	}

	@Override
	public boolean validate() {
		if ((common_configs == null) || (threshold <= 0)) {
			PManager.log.print("Configs are not completely configured!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}
}
