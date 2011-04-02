package control.ui;

import modules.ModulesManager;
import modules.experiment.Exp2GUI;
import modules.experiment.ExperimentModule;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ui.ExperimentForm;
import utils.PManager;

/**
 * Controller of the ExperimentForm GUI window.
 * 
 * @author Creative
 */
public class CtrlExperimentForm extends ControllerUI
{
	private String name, user, date, notes;
	private final ExperimentForm ui;

	@Override
	public boolean setVars(final String[] strs)
	{
		name = strs[0];
		user = strs[1];
		date = strs[2];
		notes = strs[3];
		return true;
	}

	/**
	 * Initializes class attributes (ExperimentForm , PManager and
	 * InfoController).
	 */
	public CtrlExperimentForm()
	{
		pm = PManager.getDefault();
		ui = new ExperimentForm();
		ui.setController(this);
	}

	@Override
	public void show(final boolean visibility)
	{
		ui.show(visibility);
	}

	/**
	 * Fills the GUI controls with data from the experiment object.
	 * 
	 * @param exp
	 *            Experiment object that contains data to be displayed on GUI
	 */
	public void fillForm(final Exp2GUI exp)
	{
		ui.loadData(new String[] { exp.getName(), exp.getUser(), exp.getNotes(),
				exp.getDate() });
	}

	/**
	 * Saves experiment's data to file.
	 * 
	 * @param sShell
	 *            Shell used to display the Save Dialog
	 */
	public void btnSaveAction(final Shell sShell)
	{
		try
		{
			if (!((ExperimentModule) ModulesManager.getDefault().getModuleByName(
					"Experiment Module")).isThereAnyGroups())
				System.out.print("please specify at least one group.\n");
			else
			{
				final FileDialog fileDialog = new FileDialog(sShell, SWT.SAVE);
				final String file_name = fileDialog.open();
				if (file_name != null)
				{
					((ExperimentModule) ModulesManager.getDefault().getModuleByName(
							"Experiment Module")).saveExpInfo(name, user, date, notes);
					((ExperimentModule) ModulesManager.getDefault().getModuleByName(
							"Experiment Module")).writeToTXTFile(file_name);
					((ExperimentModule) ModulesManager.getDefault().getModuleByName(
							"Experiment Module")).setExpFileName(file_name);
					sShell.setVisible(false);
				}
			}

		} catch (final NumberFormatException e1)
		{
			System.out.print("Error in user input ... aborting !\n");
		}
	}

	/**
	 * Shows GroupsForm GUI window.
	 */
	public void btnMngGrpsAction()
	{
		((ExperimentModule) ModulesManager.getDefault().getModuleByName(
				"Experiment Module")).updateGroupGUIData();
		pm.frm_grps.show(true);
	}

	/**
	 * Clears the GUI data.
	 */
	public void clearForm()
	{
		ui.clearForm();
	}

}
