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
	public boolean			enable_sec_screen;

	/**
	 * ref_gfx_main_screen reference to a Graphics object that the filter will
	 * draw on the main data stream. ref_gfx_sec_screen reference to a Graphics
	 * object that the filter will draw on the secondary data stream.
	 */
	public Graphics			ref_gfx_main_screen, ref_gfx_sec_screen;
	/**
	 * instance of a ShapeCollection that will draw its shapes on the main
	 * stream.
	 */
	public ShapeCollection	shape_controller;

	/**
	 * Initializes the configurations.
	 * 
	 * @param filt_name
	 *            name of the filter this configurations will be applied to
	 * @param refGfxMainScreen
	 *            reference to a Graphics object that the filter will draw on
	 *            the main data stream
	 * @param refGfxSecScreen
	 *            reference to a Graphics object that the filter will draw on
	 *            the secondary data stream
	 * @param common_configs
	 *            CommonConfigurations used by all filters
	 * @param enable_sec_screen
	 *            enable drawing of the secondary stream
	 * @param shp_controller
	 *            instance of ShapeController that will draw its shapes on the
	 *            main stream
	 */
	public ScreenDrawerConfigs(final String filt_name,
			final Graphics refGfxMainScreen, final Graphics refGfxSecScreen,
			final CommonFilterConfigs common_configs,
			final boolean enable_sec_screen,
			final ShapeCollection shp_controller) {
		super(filt_name, common_configs);
		ref_gfx_main_screen = refGfxMainScreen;
		ref_gfx_sec_screen = refGfxSecScreen;
		this.enable_sec_screen = enable_sec_screen;
		this.shape_controller = shp_controller;
	}

	@Override
	public void mergeConfigs(final FilterConfigs configs) {
		final ScreenDrawerConfigs tmp_scn_drwr_cfgs = (ScreenDrawerConfigs) configs;
		if (tmp_scn_drwr_cfgs.common_configs != null)
			common_configs = tmp_scn_drwr_cfgs.common_configs;
		if (tmp_scn_drwr_cfgs.ref_gfx_main_screen != null)
			ref_gfx_main_screen = tmp_scn_drwr_cfgs.ref_gfx_main_screen;
		if (tmp_scn_drwr_cfgs.ref_gfx_sec_screen != null)
			ref_gfx_sec_screen = tmp_scn_drwr_cfgs.ref_gfx_sec_screen;
		this.enable_sec_screen = tmp_scn_drwr_cfgs.enable_sec_screen;
		if (tmp_scn_drwr_cfgs.shape_controller != null)
			shape_controller = tmp_scn_drwr_cfgs.shape_controller;
	}

	@Override
	public boolean validate() {
		if ((common_configs == null) || (shape_controller == null)) {
			PManager.log.print("Configs are not completely configured!", this,
					StatusSeverity.ERROR);
			return false;
		}
		return true;
	}

}
