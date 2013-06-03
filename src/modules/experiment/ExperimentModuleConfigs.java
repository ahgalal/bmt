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

package modules.experiment;

import modules.ModuleConfigs;

/**
 * Configuration class for the Experiment module.
 * 
 * @author Creative
 */
public class ExperimentModuleConfigs extends ModuleConfigs {
	private String currGrpName;
	private int currRatNumber;
	public Experiment exp;

	/**
	 * Initializations for the configurations.
	 * 
	 * @param moduleName
	 *            name of the module instance
	 */
	public ExperimentModuleConfigs(final String moduleName,
			final Experiment exp, final String moduleID) {
		super(moduleName);
		this.exp = exp;
		this.moduleID = moduleID;
	}

	public String getCurrGrpName() {
		return currGrpName;
	}

	public int getCurrRatNumber() {
		return currRatNumber;
	}

	@Override
	protected void initializeModuleID() {
		// we don't know the moduleID, as this Config class is used for multiple
		// ExperimentModules (OF and FS)---> no specific moduleID
	}

	@Override
	public ModuleConfigs newInstance(final String moduleName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCurrGrpName(final String currGrpName) {
		this.currGrpName = currGrpName;
	}

	public void setCurrRatNumber(final int currRatNumber) {
		this.currRatNumber = currRatNumber;
	}

}
