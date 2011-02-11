package control.ui;

import control.StatsController;
import ui.OptionsWindow;
import utils.PManager;


/**
 * Controller of the OptionsWindow GUI window
 * @author Creative
 *
 */
public class Ctrl_OptionsWindow extends ControllerUI {
	private OptionsWindow ui;
	private int thresh;
	private int hyst;
	private int rearing_thresh;

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
	public void setVars(String[] strs) {
		hyst = Integer.parseInt(strs[0]);
		thresh = Integer.parseInt(strs[1]);
		rearing_thresh=Integer.parseInt(strs[2]);
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
			StatsController.getDefault().setHysteresis(hyst);
			pm.setThreshold(thresh,rearing_thresh);
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
