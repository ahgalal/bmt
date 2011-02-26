package control.ui;

import ui.About;
import utils.PManager;

public class Ctrl_About extends ControllerUI {
	
	private About ui;
	
	public Ctrl_About()
	{
		pm=PManager.getDefault();
		ui = new About();
		ui.setController(this);
	}
	
	public void btn_ok_Action()
	{
		show(false);
	}

	@Override
	public boolean setVars(String[] strs) {
		return true;
	}

	@Override
	public void show(boolean visibility) {
		ui.show(visibility);
	}
}
