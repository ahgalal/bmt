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

/**
 * Configurations of a VideoFilter.
 * 
 * @author Creative
 */
public abstract class FilterConfigs {
	/**
	 * Needed by all filters, contains properties like: image width,height ..
	 * etc.
	 */
	public CommonFilterConfigs	common_configs;
	/**
	 * is the filter enable.
	 */
	public boolean				enabled;
	private final String		name;

	/**
	 * Initializes configurations.
	 * 
	 * @param name
	 *            name of the filter these configurations are intended for.
	 * @param common_configs
	 *            CommonFilterConfigs object, needed by almost all filters
	 */
	public FilterConfigs(final String name,
			final CommonFilterConfigs common_configs) {
		if(common_configs!=null)
			this.common_configs = common_configs;
		this.name = name;
	}

	/**
	 * Gets the filter name for this configuration object.
	 * 
	 * @return String containing the filter's name
	 */
	public String getConfigurablename() {
		return name;
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
	public abstract void mergeConfigs(FilterConfigs configs);

	/**
	 * Checks that All configurations are set. (for testing purposes only)
	 * 
	 * @return true: success
	 */
	public abstract boolean validate();
	
	@Override
	public String toString() {
		return "name: "+getConfigurablename();
	}
}