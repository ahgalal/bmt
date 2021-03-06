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

import modules.experiment.Constants;
import filters.FilterData;

/**
 * Stores RearingFilter's data.
 * 
 * @author Creative
 */
public class RearingFilterData extends FilterData {
	private boolean	rearing;
	public final static String dataID=Constants.FILTER_ID+".rearing.data";
	/**
	 * Gets the rearing status.
	 * 
	 * @return rearing status
	 */
	public boolean isRearing() {
		return rearing;
	}
	@Override
	public String getId() {
		return dataID;
	}
	/**
	 * Set rearing status.
	 * 
	 * @param rearing
	 *            rearing status
	 */
	public void setRearing(final boolean rearing) {
		this.rearing = rearing;
	}
}
