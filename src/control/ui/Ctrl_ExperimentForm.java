package control.ui;

import model.business.If_Exp2GUI;
import modules.ExperimentModule;
import modules.ModulesManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ui.ExperimentForm;
import utils.PManager;

/**
 * Controller of the ExperimentForm GUI window
 * @author Creative
 *
 */
public class Ctrl_ExperimentForm extends ControllerUI {
	private String name,user,date,notes;
	private ExperimentForm ui;
	/* (non-Javadoc)
	 * @see control.ui.ControllerUI#setVars(java.lang.String[])
	 */
	@Override
	public boolean setVars(String[] strs) {
		name=strs[0];
		user=strs[1];
		date=strs[2];
		notes=strs[3];
		return true;
	}

	/**
	 * Initializes class attributes (ExperimentForm , PManager and InfoController)
	 */
	public Ctrl_ExperimentForm()
	{
		pm=PManager.getDefault();
		ui=new ExperimentForm();
		ui.setController(this);
	}
	
	@Override
	public void show(boolean visibility) {
		ui.show(visibility);
	}

	/**
	 * Fills the GUI controls with data from the experiment object.
	 * @param exp Experiment object that contains data to be displayed on GUI
	 */
	public void fillForm(If_Exp2GUI exp)
	{
		ui.loadData(new String[]{exp.getName(),exp.getUser(),
				exp.getNotes(),exp.getDate()});
	}

	/**
	 * Saves experiment's data to file.
	 * @param sShell Shell used to display the Save Dialog
	 */
	public void btn_save_Action(Shell sShell)
	{
		try {
			if(!((ExperimentModule)ModulesManager.getDefault().getModuleByName("Experiment Module")).isThereAnyGroups())
				System.out.print("please specify at least one group.\n");
			else
			{
				FileDialog fileDialog = new FileDialog(sShell, SWT.SAVE);
				String file_name=fileDialog.open();
				if(file_name!=null)
				{
					((ExperimentModule)ModulesManager.getDefault().getModuleByName("Experiment Module")).saveExpInfo(name,user,date,notes);
					((ExperimentModule)ModulesManager.getDefault().getModuleByName("Experiment Module")).writeToTXTFile(file_name);
					((ExperimentModule)ModulesManager.getDefault().getModuleByName("Experiment Module")).setExpFileName(file_name);
					sShell.setVisible(false);
				}
			}

		} catch (NumberFormatException e1) {
			System.out.print("Error in user input ... aborting !\n");
		}
	}

	/**
	 * Shows GroupsForm GUI window
	 */
	public void btn_mng_grps_Action() {
		pm.frm_grps.show(true);
	}

	public void clearForm() {
		ui.clearForm();		
	}

}
