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

package modules.zones;

import modules.ModuleData;
import modules.experiment.Constants;

/**
 * Data of the Zones Module.
 * 
 * @author Creative
 */
public class ZonesModuleData extends ModuleData {
	public final static String	dataID	= Constants.MODULE_ID + ".zones.data";
	private int					allEntrance;
	private int					centralEntrance;
	private boolean				centralFlag;
	private int					centralZoneTime;
	private int					currentZoneNum;
	private float				scale;
	private long				totalDistance;

	public int getAllEntrance() {
		return allEntrance;
	}

	public int getCentralEntrance() {
		return centralEntrance;
	}

	public int getCentralZoneTime() {
		return centralZoneTime;
	}

	public int getCurrentZoneNum() {
		return currentZoneNum;
	}

	@Override
	public String getId() {
		return dataID;
	}

	public float getScale() {
		return scale;
	}

	public long getTotalDistance() {
		return totalDistance;
	}

	public boolean isCentralFlag() {
		return centralFlag;
	}

	public void setAllEntrance(final int allEntrance) {
		this.allEntrance = allEntrance;
	}

	public void setCentralEntrance(final int centralEntrance) {
		this.centralEntrance = centralEntrance;
	}

	public void setCentralFlag(final boolean centralFlag) {
		this.centralFlag = centralFlag;
	}

	public void setCentralZoneTime(final int centralZoneTime) {
		this.centralZoneTime = centralZoneTime;
	}

	public void setCurrentZoneNum(final int currentZoneNum) {
		this.currentZoneNum = currentZoneNum;
	}

	public void setScale(final float scale) {
		this.scale = scale;
	}

	public void setTotalDistance(final long totalDistance) {
		this.totalDistance = totalDistance;
	}
}
