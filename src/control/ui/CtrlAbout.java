package control.ui;

import ui.About;
import utils.PManager;

/**
 * Controller of the About Dialog box.
 * 
 * @author Creative
 */
public class CtrlAbout extends ControllerUI
{

	private final About ui;

	/**
	 * Initializes class attributes.
	 */
	public CtrlAbout()
	{
		pm = PManager.getDefault();
		ui = new About();
		ui.setController(this);
	}

	/**
	 * Hides the window.
	 */
	public void btnOkAction()
	{
		show(false);
	}

	@Override
	public boolean setVars(final String[] strs)
	{
		return true;
	}

	@Override
	public void show(final boolean visibility)
	{
		ui.show(visibility);
	}
}
