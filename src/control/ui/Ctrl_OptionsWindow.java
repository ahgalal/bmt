package control.ui;

import ui.OptionsWindow;
import utils.PManager;
import utils.video.processors.FilterConfigs;
import utils.video.processors.rearingdetection.RearingConfigs;
import utils.video.processors.rearingdetection.RearingDetector;
import utils.video.processors.subtractionfilter.SubtractionConfigs;
import control.StatsController;


/**
 * Controller of the OptionsWindow GUI window
 * @author Creative
 */
public class Ctrl_OptionsWindow extends ControllerUI {
	private OptionsWindow ui;
	private int subtraction_thresh;
	private int hyst;
	private int rearing_thresh;
	private boolean enable_auto_rearing;

	/**
	 * Initializes class attributes (OptionsWindow and PManager)
	 * and then loads default values into the GUI.
	 */
	public Ctrl_OptionsWindow()
	{
		pm=PManager.getDefault();
		ui=new OptionsWindow();
		ui.setController(this);
		ui.loadData(new String[] {"20","40"});
	}

	@Override
	public boolean setVars(String[] strs) {
		hyst = Integer.parseInt(strs[0]);
		subtraction_thresh = Integer.parseInt(strs[1]);
		rearing_thresh=Integer.parseInt(strs[2]);
		strs[3].substring(0, 0).toUpperCase();
		enable_auto_rearing=Boolean.valueOf(strs[3]);
		return true;
	}

	/**
	 * Updates StatsController and VideoProcessor with the new values entered
	 * by the user.
	 * note1: changing the scale of Subtraction Threshold is updated immediately,
	 * but other options (rearing & hyst.) are updated when OK is pressed.
	 * note2: to achieve "note1", the GUI sends '-1' as a value for (rearing & hyst.)
	 * so that their receivers (StatsController & VideoProcessor) ignore this update,
	 * and the values of(rearing & hyst.) are kept unchanged.
	 * @param show_window Show/hide the options window
	 */
	public void updateOptions(boolean show_window)
	{
		try {
			SubtractionConfigs subtraction_configs=new SubtractionConfigs(subtraction_thresh,null,null);
			RearingConfigs rearing_configs = new RearingConfigs(rearing_thresh, 200, 200,null,null);
			
			FilterConfigs[] filters_configs = new FilterConfigs[2];
			filters_configs[0]=subtraction_configs;
			filters_configs[1]=rearing_configs;
			
			pm.getVideoProcessor().updateFiltersConfigs(filters_configs);
			
			StatsController.getDefault().setHysteresis(hyst);
			pm.getVideoProcessor().getFilter_mgr().enableFilter(RearingDetector.class,enable_auto_rearing);
			show(show_window);
		} catch (NumberFormatException e1) {
			System.out.print("Error in user input ... aborting !\n");
		}
	}

	/**
	 * Updates the options with the values entered by the user & hides the 
	 * options window.
	 */
	public void btn_ok_Action()
	{
		updateOptions(false);
	}


	@Override
	public void show(boolean visibility) {
		ui.show(visibility);
	}

}
