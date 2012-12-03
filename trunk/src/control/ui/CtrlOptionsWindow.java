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

package control.ui;

import modules.ModuleConfigs;
import modules.ModulesManager;
import modules.zones.ZonesModuleConfigs;
import ui.OptionsWindow;
import utils.PManager;
import filters.FilterConfigs;
import filters.rearingdetection.RearingDetector;
import filters.rearingdetection.RearingFilterConfigs;
import filters.subtractionfilter.SubtractionConfigs;

/**
 * Controller of the OptionsWindow GUI window.
 * 
 * @author Creative
 */
public class CtrlOptionsWindow extends ControllerUI<OptionsWindow> {
	private boolean	enable_auto_rearing;
	private int		hyst;
	private int		rearing_thresh;
	private int		subtraction_thresh;

	/**
	 * Initializes class attributes (OptionsWindow and PManager) and then loads
	 * default values into the GUI.
	 */
	public CtrlOptionsWindow() {
		pm = PManager.getDefault();
		ui = new OptionsWindow();
		ui.setController(this);
		ui.loadData(new String[] { "20", Integer.toString(SubtractionConfigs.defaultThreshold) });
	}

	/**
	 * Updates the options with the values entered by the user & hides the
	 * options window.
	 */
	public void btnOkAction() {
		updateOptions(false);
	}

	@Override
	public boolean setVars(final String[] strs) {
		hyst = Integer.parseInt(strs[0]);
		rearing_thresh = Integer.parseInt(strs[1]);
		subtraction_thresh = Integer.parseInt(strs[2]);
		enable_auto_rearing = Boolean.valueOf(strs[3].substring(0, 0)
				.toUpperCase());
		return true;
	}

	@Override
	public void show(final boolean visibility) {
		ui.show(visibility);
	}

	/**
	 * Updates StatsController and VideoManager with the new values entered by
	 * the user.<br/>note1: changing the scale of Subtraction Threshold is updated
	 * immediately, but other options (rearing & hyst.) are updated when OK is
	 * pressed.<br/>note2: to achieve "note1", the GUI sends '-1' as a value for
	 * (rearing & hyst.) so that their receivers (StatsController &
	 * VideoManager) ignore this update, and the values of(rearing & hyst.) are
	 * kept unchanged.
	 * 
	 * @param show_window
	 *            Show/hide the options window
	 */
	public void updateOptions(final boolean show_window) {
		try {
			final SubtractionConfigs subtraction_configs = new SubtractionConfigs(
					"SubtractionFilter", subtraction_thresh, null);
			final RearingFilterConfigs rearing_configs = new RearingFilterConfigs(
					"RearingDetector", rearing_thresh, -1, // TODO: add set
					// margin option in
					// GUI
					-1, // TODO: add set margin option in GUI
					null, null);

			final FilterConfigs[] filters_configs = new FilterConfigs[2];
			filters_configs[0] = subtraction_configs;
			filters_configs[1] = rearing_configs;

			pm.getVideoManager().updateFiltersConfigs(filters_configs);
			final ZonesModuleConfigs zones_configs = new ZonesModuleConfigs(
					"Zones Module", hyst, -1, -1);

			ModulesManager.getDefault().updateModuleConfigs(
					new ModuleConfigs[] { zones_configs });

			pm.getVideoManager().getFilterManager()
					.enableFilter(RearingDetector.class, enable_auto_rearing);
			show(show_window);
		} catch (final NumberFormatException e1) {
			System.out.print("Error in user input ... aborting !\n");
		}
	}
}
