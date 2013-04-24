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

package filters.ratfinder;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;

/**
 * Configurations for the RatFinder filter.
 * 
 * @author Creative
 */
public class RatFinderFilterConfigs extends FilterConfigs {

	/**
	 * Initializes the configurations.
	 * 
	 * @param filterName
	 *            name of the filter this configurations will be applied to
	 * @param commonConfigs
	 *            CommonConfigurations used by all filters
	 */
	public RatFinderFilterConfigs(final String filterName,
			final CommonFilterConfigs commonConfigs) {
		super(filterName,RatFinder.ID, commonConfigs);
	}

	/**
	 * Checks that All configurations are set. (for testing purposes only)
	 * 
	 * @return true: success
	 */
	@Override
	public boolean validate() {
		if (getCommonConfigs() == null) {
			PManager.log.print("Configs are not completely configured!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

	@Override
	public FilterConfigs newInstance(String filterName,
			CommonFilterConfigs commonConfigs) {
		return new RatFinderFilterConfigs(filterName, commonConfigs);
	}

}
