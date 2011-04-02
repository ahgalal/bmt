package control.ui;

import modules.ModuleConfigs;
import modules.ModulesManager;
import modules.zones.ZonesModuleConfigs;
import ui.OptionsWindow;
import utils.PManager;
import utils.video.filters.FilterConfigs;
import utils.video.filters.rearingdetection.RearingDetector;
import utils.video.filters.rearingdetection.RearingFilterConfigs;
import utils.video.filters.subtractionfilter.SubtractionConfigs;

/**
 * Controller of the OptionsWindow GUI window.
 * 
 * @author Creative
 */
public class CtrlOptionsWindow extends ControllerUI
{
	private final OptionsWindow ui;
	private int subtraction_thresh;
	private int hyst;
	private int rearing_thresh;
	private boolean enable_auto_rearing;

	/**
	 * Initializes class attributes (OptionsWindow and PManager) and then loads
	 * default values into the GUI.
	 */
	public CtrlOptionsWindow()
	{
		pm = PManager.getDefault();
		ui = new OptionsWindow();
		ui.setController(this);
		ui.loadData(new String[] { "20", "40" });
	}

	@Override
	public boolean setVars(final String[] strs)
	{
		hyst = Integer.parseInt(strs[0]);
		subtraction_thresh = Integer.parseInt(strs[1]);
		rearing_thresh = Integer.parseInt(strs[2]);
		// strs[3].substring(0, 0).toUpperCase();
		enable_auto_rearing = Boolean.valueOf(strs[3].substring(0, 0).toUpperCase());
		return true;
	}

	/**
	 * Updates StatsController and VideoProcessor with the new values entered by
	 * the user. note1: changing the scale of Subtraction Threshold is updated
	 * immediately, but other options (rearing & hyst.) are updated when OK is
	 * pressed. note2: to achieve "note1", the GUI sends '-1' as a value for
	 * (rearing & hyst.) so that their receivers (StatsController &
	 * VideoProcessor) ignore this update, and the values of(rearing & hyst.)
	 * are kept unchanged.
	 * 
	 * @param show_window
	 *            Show/hide the options window
	 */
	public void updateOptions(final boolean show_window)
	{
		try
		{
			final SubtractionConfigs subtraction_configs = new SubtractionConfigs(
					"SubtractionFilter",
					subtraction_thresh,
					null);
			final RearingFilterConfigs rearing_configs = new RearingFilterConfigs(
					"RearingDetector",
					rearing_thresh,
					200, // TODO: add set margin option in GUI
					200, // TODO: add set margin option in GUI
					null,
					null);

			final FilterConfigs[] filters_configs = new FilterConfigs[2];
			filters_configs[0] = subtraction_configs;
			filters_configs[1] = rearing_configs;

			pm.getVideoProcessor().updateFiltersConfigs(filters_configs);
			final ZonesModuleConfigs zones_configs = new ZonesModuleConfigs(
					"Zones Module",
					hyst, -1, -1);

			ModulesManager.getDefault().updateModuleConfigs(
					new ModuleConfigs[] { zones_configs });

			pm.getVideoProcessor().getFilterManager().enableFilter(
					RearingDetector.class,
					enable_auto_rearing);
			show(show_window);
		} catch (final NumberFormatException e1)
		{
			System.out.print("Error in user input ... aborting !\n");
		}
	}

	/**
	 * Updates the options with the values entered by the user & hides the
	 * options window.
	 */
	public void btnOkAction()
	{
		updateOptions(false);
	}

	@Override
	public void show(final boolean visibility)
	{
		ui.show(visibility);
	}

}
