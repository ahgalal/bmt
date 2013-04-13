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

import javax.annotation.Resource;

import modules.ModuleConfigs;

/**
 * Configuration class for the zones module.
 * 
 * @author Creative
 */
public class ZonesModuleConfigs extends ModuleConfigs {
	/**
	 * Hello there :D, the style checker recommended adding a Javadoc comment
	 * here, so, here we go!
	 */
	@Resource
	private int	hystValue	= 50;

	/**
	 * Initializations for the configurations.
	 * 
	 * @param moduleName
	 *            name of the module
	 * @param hystVal
	 *            initial hysteresis value
	 * @param width
	 *            image's width
	 * @param height
	 *            image's height
	 */
	public ZonesModuleConfigs(final String moduleName, final int hystVal,
			final int width, final int height) {
		super(moduleName);
		setHystValue(hystVal);
		this.setWidth(width);
		this.setHeight(height);
	}

	@Override
	protected void mergeConfigs(final ModuleConfigs configs) {
		final ZonesModuleConfigs tmpZonConfigs = (ZonesModuleConfigs) configs;
		if (tmpZonConfigs.getHystValue() != -1)
			setHystValue(tmpZonConfigs.getHystValue());
		if (tmpZonConfigs.getWidth() != -1)
			setWidth(tmpZonConfigs.getWidth());
		if (tmpZonConfigs.getHeight() != -1)
			setHeight(tmpZonConfigs.getHeight());
	}

	public void setHystValue(int hystValue) {
		this.hystValue = hystValue;
	}

	public int getHystValue() {
		return hystValue;
	}
}