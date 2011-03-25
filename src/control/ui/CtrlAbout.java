package control.ui;

import ui.About;
import utils.PManager;

public class CtrlAbout extends ControllerUI
{

	private final About ui;

	public CtrlAbout()
	{
		pm = PManager.getDefault();
		ui = new About();
		ui.setController(this);
	}

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
