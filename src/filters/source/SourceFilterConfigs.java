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

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.FrameIntArray;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;

/**
 * Configuration of the SourceFilter.
 * 
 * @author Creative
 */
public class SourceFilterConfigs extends FilterConfigs {
	/**
	 * image data container.
	 */
	private FrameIntArray	fia;

	/**
	 * @param name
	 *            name of the filter this configurations will be applied to
	 * @param commonConfigs
	 *            subtraction threshold
	 * @param fia
	 *            image data container coming from the device
	 */
	public SourceFilterConfigs(final String name,
			final CommonFilterConfigs commonConfigs, final FrameIntArray fia) {
		super(name, commonConfigs);
		this.setFrameIntArray(fia);
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs) {
		final SourceFilterConfigs tmpSrcfilterConfigs = (SourceFilterConfigs) configs;
		if (tmpSrcfilterConfigs.getCommonConfigs() != null)
			setCommonConfigs(tmpSrcfilterConfigs.getCommonConfigs());
		if (tmpSrcfilterConfigs.getFrameIntArray() != null)
			this.setFrameIntArray(tmpSrcfilterConfigs.getFrameIntArray());
	}

	@Override
	public boolean validate() {
		if ((getCommonConfigs() == null) || (getFrameIntArray() == null)) {
			PManager.log.print("Configs are not completely configured!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

	public void setFrameIntArray(FrameIntArray fia) {
		this.fia = fia;
	}

	public FrameIntArray getFrameIntArray() {
		return fia;
	}

}
