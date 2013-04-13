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

import modules.zones.ShapeCollection;
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
	 * enable drawing of the secondary stream.
	 */
	private boolean			enableSecScreen;

	/**
	 * refGfxMainScreen reference to a Graphics object that the filter will
	 * draw on the main data stream. refGfxSecScreen reference to a Graphics
	 * object that the filter will draw on the secondary data stream.
	 */
	private Graphics			refGfxMainScreen;

	private Graphics	refGfxSecScreen;
	/**
	 * instance of a ShapeCollection that will draw its shapes on the main
	 * stream.
	 */
	private ShapeCollection	shapeController;

	/**
	 * Initializes the configurations.
	 * 
	 * @param filterName
	 *            name of the filter this configurations will be applied to
	 * @param refGfxMainScreen
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
	 */
	public ScreenDrawerConfigs(final String filterName,
			final Graphics refGfxMainScreen, final Graphics refGfxSecScreen,
			final CommonFilterConfigs commonConfigs,
			final boolean enableSecScreen,
			final ShapeCollection shpController) {
		super(filterName, commonConfigs);
		this.setRefGfxMainScreen(refGfxMainScreen);
		this.setRefGfxSecScreen(refGfxSecScreen);
		this.setEnableSecScreen(enableSecScreen);
		this.setShapeController(shpController);
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs) {
		final ScreenDrawerConfigs tmpScnDrwrCfgs = (ScreenDrawerConfigs) configs;
		if (tmpScnDrwrCfgs.getCommonConfigs() != null)
			setCommonConfigs(tmpScnDrwrCfgs.getCommonConfigs());
		if (tmpScnDrwrCfgs.getRefGfxMainScreen() != null)
			setRefGfxMainScreen(tmpScnDrwrCfgs.getRefGfxMainScreen());
		if (tmpScnDrwrCfgs.getRefGfxSecScreen() != null)
			setRefGfxSecScreen(tmpScnDrwrCfgs.getRefGfxSecScreen());
		this.setEnableSecScreen(tmpScnDrwrCfgs.isEnableSecScreen());
		if (tmpScnDrwrCfgs.getShapeController() != null)
			setShapeController(tmpScnDrwrCfgs.getShapeController());
	}

	@Override
	public boolean validate() {
		if ((getCommonConfigs() == null) || (getShapeController() == null)) {
			PManager.log.print("Configs are not completely configured!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

	public void setEnableSecScreen(boolean enableSecScreen) {
		this.enableSecScreen = enableSecScreen;
	}

	public boolean isEnableSecScreen() {
		return enableSecScreen;
	}

	public void setRefGfxMainScreen(Graphics refGfxMainScreen) {
		this.refGfxMainScreen = refGfxMainScreen;
	}

	public Graphics getRefGfxMainScreen() {
		return refGfxMainScreen;
	}

	public void setRefGfxSecScreen(Graphics refGfxSecScreen) {
		this.refGfxSecScreen = refGfxSecScreen;
	}

	public Graphics getRefGfxSecScreen() {
		return refGfxSecScreen;
	}

	public void setShapeController(ShapeCollection shapeController) {
		this.shapeController = shapeController;
	}

	public ShapeCollection getShapeController() {
		return shapeController;
	}

}
