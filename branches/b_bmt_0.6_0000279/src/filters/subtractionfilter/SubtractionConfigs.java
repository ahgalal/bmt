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

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;

/**
 * Configuration of the SubtractorFilter.
 * 
 * @author Creative
 */
public class SubtractionConfigs extends FilterConfigs {

	public static int defaultThreshold=20;
	/**
	 * subtraction threshold, pixel value> threshold will be white, while pixel
	 * value < threshold will be black in the output image.
	 */
	private int	threshold;

	/**
	 * Initializes the configurations.
	 * 
	 * @param filterName
	 *            name of the filter this configurations will be applied to
	 * @param threshold
	 *            subtraction threshold
	 * @param commonConfigs
	 *            CommonConfigurations used by all filters
	 */
	public SubtractionConfigs(final String filterName, final int threshold,
			final CommonFilterConfigs commonConfigs) {
		super(filterName,SubtractorFilter.ID, commonConfigs);
		this.setThreshold(threshold);
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs) {
		final SubtractionConfigs tmpSubtractionConfigs = (SubtractionConfigs) configs;
		if (tmpSubtractionConfigs.getCommonConfigs() != null)
			this.setCommonConfigs(tmpSubtractionConfigs.getCommonConfigs());
		if (tmpSubtractionConfigs.getThreshold() != -1)
			this.setThreshold(tmpSubtractionConfigs.getThreshold());
	}

	@Override
	public boolean validate() {
		if ((getCommonConfigs() == null) || (getThreshold() <= 0)) {
			PManager.log.print("Configs are not completely configured!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return super.toString()+", threshold: "+getThreshold();
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public int getThreshold() {
		return threshold;
	}

	@Override
	public FilterConfigs newInstance(String filterName,
			CommonFilterConfigs commonConfigs) {
		return new SubtractionConfigs(filterName, defaultThreshold, commonConfigs);
	}
}
