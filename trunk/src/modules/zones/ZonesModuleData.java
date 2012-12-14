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

/**
 * Data of the Zones Module.
 * 
 * @author Creative
 */
public class ZonesModuleData extends ModuleData {
	private int				allEntrance;
	private int	centralEntrance;
	private boolean			centralFlag;
	private int				centralZoneTime;
	private int				currentZoneNum;
	private float			scale;
	private long				totalDistance;
	private ZonesCollection	zones;

	/**
	 * Initializes the Data.
	 * 
	 * @param name
	 *            name of the data instance
	 */
	public ZonesModuleData(final String name) {
		super(name);
	}

	public void setAllEntrance(int allEntrance) {
		this.allEntrance = allEntrance;
	}

	public int getAllEntrance() {
		return allEntrance;
	}

	public void setCentralEntrance(int centralEntrance) {
		this.centralEntrance = centralEntrance;
	}

	public int getCentralEntrance() {
		return centralEntrance;
	}

	public void setCentralFlag(boolean centralFlag) {
		this.centralFlag = centralFlag;
	}

	public boolean isCentralFlag() {
		return centralFlag;
	}

	public void setCentralZoneTime(int centralZoneTime) {
		this.centralZoneTime = centralZoneTime;
	}

	public int getCentralZoneTime() {
		return centralZoneTime;
	}

	public void setCurrentZoneNum(int currentZoneNum) {
		this.currentZoneNum = currentZoneNum;
	}

	public int getCurrentZoneNum() {
		return currentZoneNum;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return scale;
	}

	public void setTotalDistance(long totalDistance) {
		this.totalDistance = totalDistance;
	}

	public long getTotalDistance() {
		return totalDistance;
	}

	public void setZones(ZonesCollection zones) {
		this.zones = zones;
	}

	public ZonesCollection getZones() {
		return zones;
	}

}
