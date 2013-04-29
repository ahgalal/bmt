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

package filters;

import utils.Configuration;

/**
 * Configurations of a VideoFilter.
 * 
 * @author Creative
 */
public abstract class FilterConfigs implements Configuration<FilterConfigs> {
	/**
	 * Needed by all filters, contains properties like: image width,height ..
	 * etc.
	 */
	private CommonFilterConfigs	commonConfigs;
	/**
	 * is the filter enable.
	 */
	private boolean				enabled;
	private final String		filterId;
	private String				name;

	/**
	 * Initializes configurations.
	 * 
	 * @param name
	 *            name of the filter these configurations are intended for.
	 * @param commonConfigs
	 *            CommonFilterConfigs object, needed by almost all filters
	 */
	public FilterConfigs(final String name, final String filterId,
			final CommonFilterConfigs commonConfigs) {
		if (commonConfigs != null)
			this.setCommonConfigs(commonConfigs);
		this.name = name;
		this.filterId = filterId;
	}

	@Override
	public CommonFilterConfigs getCommonConfigs() {
		return commonConfigs;
	}

	@Override
	public String getID() {
		return filterId;
	}

	/**
	 * Gets the filter name for this configuration object.
	 * 
	 * @return String containing the filter's name
	 */
	@Override
	public String getName() {
		return name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Merges the incoming configurations object with "this", typically the
	 * incoming configurations object will have some null fields (due to lack of
	 * info on the caller side), those null fileds should be filtered out, and
	 * only valid fields are copied to "this" object.
	 * 
	 * @param configs
	 *            incoming configurations object
	 */
	@Override
	public void mergeConfigs(final FilterConfigs configs) {
		if (configs.getCommonConfigs() != null)
			setCommonConfigs(configs.getCommonConfigs());
	}

	@Override
	public FilterConfigs newInstance(final String name) {
		throw new RuntimeException(
				"Illegal method call, should have called the overload");
	}

	public abstract FilterConfigs newInstance(String filterName,
			CommonFilterConfigs commonConfigs);

	public void setCommonConfigs(final CommonFilterConfigs commonConfigs) {
		this.commonConfigs = commonConfigs;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "name: " + getName();
	}

	/**
	 * Checks that All configurations are set. (for testing purposes only)
	 * 
	 * @return true: success
	 */
	public abstract boolean validate();
}