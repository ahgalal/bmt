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

package filters.source;

import utils.PManager.ProgramState;
import filters.FilterConfigs;
import filters.FilterData;
import filters.Link;
import filters.VideoFilter;

/**
 * Takes the data coming from the input device and puts it on its output link,
 * to be used by other filters.
 * 
 * @author Creative
 */
public class SourceFilter extends
		VideoFilter<SourceFilterConfigs, SourceFilterData> {
	public static final String ID = "filters.source";

	/**
	 * Initializes the filter.
	 * 
	 * @param name
	 *            filter's name
	 * @param linkIn
	 *            input Link for the filter
	 * @param linkOut
	 *            output link that will distribute the data on other filters
	 */
	public SourceFilter(final String name, final Link linkIn, final Link linkOut) {
		super(name, linkIn, linkOut);
		filterData = new SourceFilterData();
	}
	@Override
	public int getInPortCount() {
		return 0;
	}
	@Override
	public boolean configure(final FilterConfigs configs) {
		this.configs = (SourceFilterConfigs) configs;
		return super.configure(configs);
	}

	@Override
	public void process() {
		int[] frameData = configs.getFrameIntArray().getFrameData();
		linkOut.setData(frameData);
		filterData.setData(frameData);
	}

	@Override
	public void updateProgramState(final ProgramState state) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String getID() {
		return ID;
	}

	@Override
	public VideoFilter<?, ?> newInstance(String filterName) {
		return new SourceFilter(filterName, null, null);
	}

	@Override
	public void registerDependentData(FilterData data) {
		// TODO Auto-generated method stub
		
	}

}
