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

package modules.session;

import modules.ModuleConfigs;

/**
 * Configuration class for the Session module.
 * 
 * @author Creative
 */
public class SessionModuleConfigs extends ModuleConfigs {

	/**
	 * Initializations for the configurations.
	 * 
	 * @param moduleName
	 *            name of the module instance
	 */
	public SessionModuleConfigs(final String moduleName) {
		super(moduleName);
	}

	@Override
	protected void initializeModuleID() {
		moduleID = SessionModule.moduleID;
	}

	@Override
	public void mergeConfigs(final ModuleConfigs config) {
		// Nothing! :D
	}

	@Override
	public ModuleConfigs newInstance(final String moduleName) {
		return new SessionModuleConfigs(moduleName);
	}

}
