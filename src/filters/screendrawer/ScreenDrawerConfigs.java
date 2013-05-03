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

package filters.screendrawer;

import java.awt.Graphics;
import java.awt.Point;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import filters.CommonFilterConfigs;
import filters.FilterConfigs;

/**
 * Configurations for the ScreenDrawer filter.
 * 
 * @author Creative
 */
public class ScreenDrawerConfigs extends FilterConfigs {
	/**
	 * refGfxMainScreen reference to a Graphics object that the filter will
	 * draw on the main data stream. refGfxSecScreen reference to a Graphics
	 * object that the filter will draw on the secondary data stream.
	 */
	private Graphics			refGfxScreen;
	private Point canvasDims;

	/**
	 * Initializes the configurations.
	 * 
	 * @param filterName
	 *            name of the filter this configurations will be applied to
	 * @param refGfxScreen
	 *            reference to a Graphics object that the filter will draw on
	 *            the main data stream
	 * @param refGfxSecScreen
	 *            reference to a Graphics object that the filter will draw on
	 *            the secondary data stream
	 * @param commonConfigs
	 *            CommonConfigurations used by all filters
	 * @param enableSecScreen
	 *            enable drawing of the secondary stream
	 * @param shpController
	 *            instance of ShapeController that will draw its shapes on the
	 *            main stream
	 * @param canvasDims 
	 */
	public ScreenDrawerConfigs(final String filterName,
			final Graphics refGfxScreen,
			final CommonFilterConfigs commonConfigs,
			Point canvasDims) {
		super(filterName,ScreenDrawer.ID, commonConfigs);
		this.setRefGfxMainScreen(refGfxScreen);
		
		this.setCanvasDims(canvasDims);
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs) {
		super.mergeConfigs(configs);
		final ScreenDrawerConfigs tmpScnDrwrCfgs = (ScreenDrawerConfigs) configs;
		if (tmpScnDrwrCfgs.getRefGfxScreen() != null)
			setRefGfxMainScreen(tmpScnDrwrCfgs.getRefGfxScreen());
		if(tmpScnDrwrCfgs.getCanvasDims()!=null)
			setCanvasDims(tmpScnDrwrCfgs.getCanvasDims());
	}

	public void setRefGfxMainScreen(Graphics refGfxMainScreen) {
		this.refGfxScreen = refGfxMainScreen;
	}

	public Graphics getRefGfxScreen() {
		return refGfxScreen;
	}

	public void setCanvasDims(Point canvasDims) {
		this.canvasDims = canvasDims;
	}

	public Point getCanvasDims() {
		return canvasDims;
	}

	@Override
	public FilterConfigs newInstance(String filterName,
			CommonFilterConfigs commonConfigs) {
		return new ScreenDrawerConfigs(filterName, null, commonConfigs, null);
	}

}
