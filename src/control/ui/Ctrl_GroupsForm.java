package control.ui;

import java.util.ArrayList;

import model.business.If_Grp2GUI;
import modules.ExperimentModule;
import modules.ModulesManager;
import ui.GroupsForm;
import ui.GroupsForm.TabContents;


/**
 * Controller of the GroupsForm GUI window
 * @author Creative
 *
 */
public class Ctrl_GroupsForm extends ControllerUI {
	private GroupsForm ui;

	/**
	 * Initializes class attributes (GroupsForm and InfoController)
	 */
	public Ctrl_GroupsForm()
	{
		ui=new GroupsForm();
		ui.setController(this);
	}

	@Override
	public boolean setVars(String[] strs) {
		return true;
	}

	/**
	 * Saves all entered groups by the user to the InfoController.
	 * @param arr_tabs ArrayList of tabs, each tab contains single group information
	 */
	public void btn_save_Action(ArrayList<TabContents> arr_tabs)
	{
		try {
			String name,rats_numbering,notes;

			for(TabContents tc: arr_tabs)
			{
				name=tc.txt_name.getText();
				rats_numbering=tc.txt_rats_numbers.getText();
				notes=tc.txt_notes.getText();
				((ExperimentModule)ModulesManager.getDefault().getModuleByName("Experiment Module")).saveGrpInfo(tc.grp_id,name, rats_numbering , notes);
			}

			show(false);
		} catch (NumberFormatException e1) {
			System.out.print("Error in user input ... aborting !\n");
		}

	}

	@Override
	public void show(boolean visibility) {
		ui.show(visibility);
	}

	/**
	 * Loads data of an array of Groups to the GUI window
	 * @param grp array of groups, containing information to be presented on GUI
	 */
	public void loadDataToForm(If_Grp2GUI[] grp)
	{
		for(If_Grp2GUI tmp_grp:grp)
		{
			ui.addNewTab(tmp_grp.getId(),tmp_grp.getName(),Integer.toString(tmp_grp.getNo_rats()),tmp_grp.getRats_numbering(),tmp_grp.getNotes());
		}
	}

	public void clearForm() {
		ui.clearForm();		
	}

}
