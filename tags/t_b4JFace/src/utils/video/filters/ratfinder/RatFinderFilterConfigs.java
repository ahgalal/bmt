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

package utils.video.filters.ratfinder;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.CommonFilterConfigs;
import utils.video.filters.FilterConfigs;

/**
 * Configurations for the RatFinder filter.
 * 
 * @author Creative
 */
public class RatFinderFilterConfigs extends FilterConfigs {

	/**
	 * Initializes the configurations.
	 * 
	 * @param filt_name
	 *            name of the filter this configurations will be applied to
	 * @param common_configs
	 *            CommonConfigurations used by all filters
	 */
	public RatFinderFilterConfigs(final String filt_name,
			final CommonFilterConfigs common_configs) {
		super(filt_name, common_configs);
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs) {
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
	public boolean validate() {
		if (common_configs == null) {
			PManager.log.print("Configs are not completely configured!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

}
