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

/**
 * 
 */
package filters;

import ui.PluggedGUI;
import utils.Configurable;
import utils.Logger.Details;
import utils.PManager;
import utils.StateListener;

/**
 * Parent of all Video utilities & filters.
 * 
 * @author Creative
 */
public abstract class VideoFilter<ConfigsType extends FilterConfigs, DataType extends FilterData>
		implements StateListener, Configurable<ConfigsType> {
	protected ConfigsType	configs;
	protected DataType		filterData;
	protected PluggedGUI<?>	gui;
	protected Link			linkIn, linkOut;
	protected String		name;

	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param linkIn
	 *            input Link for the filter
	 * @param linkOut
	 *            output Link from the filter
	 */
	public VideoFilter(final String name, final Link linkIn, final Link linkOut) {
		this.name = name;
		this.linkIn = linkIn;
		this.linkOut = linkOut;
	}

	/**
	 * Optional initializations for the filter.
	 * 
	 * @param configs
	 *            filter's configurations
	 * @return true: success, false: failure
	 */
	@SuppressWarnings("unchecked")
	public boolean configure(final FilterConfigs configs) {
		this.configs = (ConfigsType) configs;
		PManager.log.print("initializing..", this, Details.VERBOSE);
		return true;
	}

	/**
	 * Enables/Disables the filter.
	 * 
	 * @param enable
	 *            enable/disable the filter
	 * @return confirmation that the filter was enabled/disabled
	 */
	public boolean enable(final boolean enable) {
		configs.setEnabled(enable);
		return configs.isEnabled();
	}

	/**
	 * Returns the filter's configurations.
	 * 
	 * @return configuration object
	 */
	public FilterConfigs getConfigs() {
		return configs;
	}

	/**
	 * Gets the Filter configurations object.
	 * 
	 * @return filter configurations object
	 */
	public FilterConfigs getConfigurations() {
		return configs;
	}

	/**
	 * Gets the filter's data.
	 * 
	 * @return Data object of the filter
	 */
	public Data getFilterData() {
		return filterData;
	}

	public PluggedGUI<?> getGUI() {
		return gui;
	}

	@Override
	public String getID() {
		throw new IllegalAccessError(
				"Cannot call this method in parent, are you missing a child implementation?");
	}

	public int getInPortCount() {
		return 1;
	}

	public Link getLinkIn() {
		return linkIn;
	}

	public Link getLinkOut() {
		return linkOut;
	}

	/**
	 * Gets the name of the filter.
	 * 
	 * @return String containing the filter's name
	 */
	@Override
	public String getName() {
		return name;
	}

	public int getOutPortCount() {
		return 1;
	}

	public abstract VideoFilter<?, ?> newInstance(String filterName);

	/**
	 * Processes the data from the input link and put the processed data on the
	 * output link.
	 */
	public abstract void process();

	public abstract void registerDependentData(FilterData data);

	public void setLinkIn(final Link linkIn) {
		this.linkIn = linkIn;
	}

	public void setLinkOut(final Link linkOut) {
		this.linkOut = linkOut;
	}

	/**
	 * Sets the name of the filter.
	 * 
	 * @param name
	 *            new name for the filter
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Updates the filter's configurations.
	 * 
	 * @param configs
	 *            configurations object for the filter
	 */
	@Override
	public void updateConfigs(final ConfigsType configs) {
		if (this.configs == null)
			this.configs = configs;
		else
			this.configs.mergeConfigs(configs);
		configure(getConfigs());
	}
}