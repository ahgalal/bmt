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

package modules;

import utils.Configuration;

/**
 * Parent of all Configurations Classes of modules.
 * 
 * @author Creative
 */
public abstract class ModuleConfigs implements Configuration<ModuleConfigs> {

	protected boolean enable;
	protected String moduleID;
	protected String moduleName;
	protected CommonModuleConfigs commonConfigs;

	public CommonModuleConfigs getCommonConfigs() {
		return commonConfigs;
	}

	/**
	 * Initializes the configurations.
	 * 
	 * @param moduleID
	 *            id of the Module this configuration is to be applied to
	 */
	public ModuleConfigs(final String moduleName) {
		this.moduleName = moduleName;
		commonConfigs=new CommonModuleConfigs();
		initializeModuleID();
	}

	/**
	 * Get the module name this configuration object is to be applied to.
	 * 
	 * @return String containing the name of the module this configuration
	 *         object is to be applied to
	 */
	@Override
	public String getID() {
		return moduleID;
	}

	@Override
	public String getName() {
		return moduleName;
	}

	protected abstract void initializeModuleID();

	/**
	 * Merges the current configurations with the incoming configurations
	 * object, as most of the time, the incoming configurations object has some
	 * null fields (due to the caller not knowing the values), so the non-null
	 * fields ONLY should be merged with the current configurations.
	 * 
	 * @param config
	 *            incoming configurations object
	 */
	@Override
	public void mergeConfigs(final ModuleConfigs config) {
		if (config.commonConfigs.getWidth() != -1)
			commonConfigs.setWidth(config.commonConfigs.getWidth());
		if (config.commonConfigs.getHeight() != -1)
			commonConfigs.setHeight(config.commonConfigs.getHeight());
	}

	@Override
	public abstract ModuleConfigs newInstance(String moduleName);

	@Override
	public void setName(final String moduleName) {
		this.moduleName = moduleName;
	}
}
