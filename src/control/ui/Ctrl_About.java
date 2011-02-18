package control.ui;

import ui.About;

public class Ctrl_About extends ControllerUI {
	
	private About ui;
	
	
	public void btn_ok_Action()
	{
		show(false);
	}


	@Override
	public boolean setVars(String[] strs) {
		return false;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show(boolean visibility) {
		ui.show(visibility);
		// TODO Auto-generated method stub
	}
}
