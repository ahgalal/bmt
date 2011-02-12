package control.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import ui.RatInfoForm;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import control.InfoController;

/**
 * Controller of the RatInfoForm GUI window
 * @author Creative
 *
 */
public class Ctrl_RatInfoForm extends ControllerUI {
	private InfoController info_controller;  //  @jve:decl-index=0:
	private int rat_num;
	private String grp_name;
	private RatInfoForm ui;

	/**
	 * Initializes class attributes (RatInfoForm ,InfoController and PManager)
	 */
	public Ctrl_RatInfoForm()
	{
		pm=PManager.getDefault();
		ui= new RatInfoForm();
		ui.setController(this);
		info_controller=InfoController.getDefault();
	}

	@Override
	public boolean setVars(String[] strs) {
		if(!strs[0].isEmpty())
		{
			rat_num = Integer.parseInt(strs[0]);
			grp_name = strs[1];
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see control.ui.ControllerUI#show(boolean)
	 * 
	 * Before we show the window, we collect groups' names from InfoController
	 * and load them to GUI.
	 * note: params[0] is reserved for the rat number.
	 */
	@Override
	public void show(boolean visibility) {
		String[] grps=info_controller.getGroupsNames();
		String[] params=new String[grps.length+1];

		params[0]="";
		for(int i=0;i<grps.length;i++)
			params[i+1]=grps[i];
		ui.clearForm();
		ui.loadData(params);
		ui.show(visibility);
	}

	/**
	 * Checks data entered and submit it if correct
	 */
	public void btn_ok_Action()
	{
		checkAndSubmitData();
	}

	/**
	 * Checks the validity of entered Rat Number & Group name.
	 * if the Rat Number already exists, it asks fdo confirmation;
	 * if the Group name is not valid, error message is displayed;
	 * if all data are correct, we start tracking activity. 
	 */
	public void checkAndSubmitData()
	{
		try {

			int tmp_confirmation=info_controller.setCurrentRatAndGroup(rat_num, grp_name,false); 
			if(tmp_confirmation==0){
				startTracking();
			}
			else if(tmp_confirmation==1){	//Rat already exists
				MessageBox msg = new  MessageBox(ui.getShell(),SWT.ICON_QUESTION|SWT.YES|SWT.NO);
				msg.setText("Confirmation");
				msg.setMessage("Rat '" + rat_num + "' already exists, replace it?");
				int res=msg.open();
				if(res==SWT.YES)
				{
					info_controller.setCurrentRatAndGroup(rat_num, grp_name,true);
					startTracking();
				}
			}
			else if(tmp_confirmation==-1)	//Group not found
				pm.status_mgr.setStatus("Please select a group.",StatusSeverity.ERROR);
		} catch (NumberFormatException e1) {
			pm.status_mgr.setStatus("Please enter a valid Rat number.",StatusSeverity.ERROR);
		}

	}

	/**
	 * Hides the window and starts the Tracking activity.
	 */
	private void startTracking()
	{
		pm.startTracking();
		show(false);
	}

}
