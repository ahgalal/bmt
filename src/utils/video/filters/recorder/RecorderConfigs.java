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
public class RecorderConfigs extends FilterConfigs {

    /**
     * Initializes the configurations.
     * 
     * @param filt_name
     *            name of the filter this configurations will be applied to
     * @param common_configs
     *            CommonConfigurations used by all filters
     */
    public RecorderConfigs(final String filt_name,
	    final CommonFilterConfigs common_configs) {
	super(filt_name, common_configs);
    }

    @Override
    public void mergeConfigs(final FilterConfigs configs) {
	if (configs.common_configs != null)
	    this.common_configs = configs.common_configs;
    }

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
