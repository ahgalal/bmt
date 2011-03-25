package control.ui;

import utils.PManager;

/**
 * The parent of all GUI controllers.
 * 
 * @author Creative
 */
public abstract class ControllerUI
{

	protected PManager pm;

	/**
	 * Shows/Hides the GUI controlled by a child of this class.
	 * 
	 * @param visibility
	 */
	public abstract void show(boolean visibility);

	/**
	 * Passes an array of strings to the GUI window, the GUI will manipulate
	 * this array and display strings in their proper positions.
	 * 
	 * @param strs
	 */
	public abstract boolean setVars(String[] strs);

}
