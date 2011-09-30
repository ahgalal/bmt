/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

/**
 * 
 */
package utils.video.filters;

/**
 * Parent of all Video utilities & filters.
 * 
 * @author Creative
 */
public abstract class VideoFilter
{
	protected String name;
	protected FilterData filter_data;
	protected FilterConfigs configs;
	protected Link link_in, link_out;

	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param link_in
	 *            input Link for the filter
	 * @param link_out
	 *            output Link from the filter
	 */
	public VideoFilter(
			final String name, final Link link_in, final Link link_out)
	{
		this.name = name;
		this.link_in = link_in;
		this.link_out = link_out;
	}

	/**
	 * Returns the filter's configurations.
	 * 
	 * @return configuration object
	 */
	public FilterConfigs getConfigs()
	{
		return configs;
	}

	/**
	 * Updates the filter's configurations.
	 * 
	 * @param configs
	 *            configurations object for the filter
	 */
	public void updateConfigs(final FilterConfigs configs)
	{
		this.configs.mergeConfigs(configs);
	}

	/**
	 * Processes the data from the input link and put the processed data on the
	 * output link.
	 */
	public abstract void process();

	/**
	 * Enables/Disables the filter.
	 * 
	 * @param enable
	 *            enable/disable the filter
	 * @return confirmation that the filter was enabled/disabled
	 */
	public boolean enable(final boolean enable)
	{
		configs.enabled = enable;
		return configs.enabled;
	}

	/**
	 * Gets the name of the filter.
	 * 
	 * @return String containing the filter's name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the filter's data.
	 * 
	 * @return Data object of the filter
	 */
	public Data getFilterData()
	{
		return filter_data;
	}

	/**
	 * Sets the name of the filter.
	 * 
	 * @param name
	 *            new name for the filter
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Optional initializations for the filter.
	 * 
	 * @param configs
	 *            filter's configurations
	 * @return true: success, false: failure
	 */
	public boolean configure(final FilterConfigs configs)
	{
		this.configs = configs;
		return true;
	}

	/**
	 * Gets the Filter configurations object.
	 * 
	 * @return filter configurations object
	 */
	public FilterConfigs getConfigurations()
	{
		return configs;
	}

}