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

package filters.rearingdetection;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;

/**
 * Configurations for the RearingFilter filter.
 * 
 * @author Creative
 */
public class RearingFilterConfigs extends FilterConfigs {
	/**
	 * x-margin for white pixel counting (around the current location of the
	 * object).
	 */
	private int		marginX=200;
	/*
	 * _________________________________ 
	 * | 			   |			   | 
	 * | 			   |			   | 
	 * | 		   y_margin			   | 
	 * | 			   |			   | 
	 * | 			   |		 	   |
	 * |---x_margin----O----x_margin---| 
	 * |			   |			   | 
	 * | 			   |			   | 
	 * | 		   y_margin 		   | 
	 * | 			   |			   | 
	 * | 			   |			   |
	 * |-------------------------------| O: current position of the object.
	 * count white pixels inside this area only (to save the processing power)
	 */

	private int	marginY=200;

	/**
	 * if white pixels number > rearingthreshold => not rearing rat.
	 */
	private int		rearingThresh;



	/**
	 * Initializes the configurations.
	 * 
	 * @param filterName
	 *            name of the filter this configurations will be applied to
	 * @param rearingThresh
	 *            rearing threshold
	 * @param marginX
	 *            x-margin for searching around the current object's position
	 * @param marginY
	 *            y-margin for searching around the current object's position
	 * @param centerPoint
	 *            reference to the center point (current location of the object)
	 * @param commonConfigs
	 *            CommonConfigurations used by all filters
	 */
	public RearingFilterConfigs(final String filterName,
			final int rearingThresh, final int marginX, final int marginY,
			final CommonFilterConfigs commonConfigs) {
		super(filterName,RearingDetector.ID, commonConfigs);
		this.setRearingThresh(rearingThresh);
		this.setMarginX(marginX);
		this.setMarginY(marginY);
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs) {
		super.mergeConfigs(configs);
		final RearingFilterConfigs tmpRearingConfigs = (RearingFilterConfigs) configs;
		if (tmpRearingConfigs.getMarginX() != -1)
			this.setMarginX(tmpRearingConfigs.getMarginX());
		if (tmpRearingConfigs.getMarginY() != -1)
			this.setMarginY(tmpRearingConfigs.getMarginY());
		if (tmpRearingConfigs.getRearingThresh() != -1)
			this.setRearingThresh(tmpRearingConfigs.getRearingThresh());
	}

	@Override
	public String toString() {
		return super.toString()+", rearing thresh: "+ getRearingThresh();
	}

	public void setMarginX(int marginX) {
		this.marginX = marginX;
	}

	public int getMarginX() {
		return marginX;
	}

	public void setMarginY(int marginY) {
		this.marginY = marginY;
	}

	public int getMarginY() {
		return marginY;
	}

	public void setRearingThresh(int rearingThresh) {
		this.rearingThresh = rearingThresh;
	}

	public int getRearingThresh() {
		return rearingThresh;
	}

	@Override
	public FilterConfigs newInstance(String filterName,
			CommonFilterConfigs commonConfigs) {
		return new RearingFilterConfigs(filterName, 1000, 200, 200, commonConfigs);
	}
}