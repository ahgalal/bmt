package control.ui;

import java.util.ArrayList;

import modules.ExperimentManager;
import modules.experiment.Exp2GUI;
import modules.experiment.Grp2GUI;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ui.wizards.newexperiment.NewExperimentWizard;

public class CtrlNewExperimentWizard {

	private final ArrayList<Grp2GUI>	groups	= new ArrayList<Grp2GUI>();

	private String						name, user, date, notes, type;

	public void setGroups(final Grp2GUI[] groups) {
		this.groups.clear();
		for (final Grp2GUI grp : groups)
			this.groups.add(grp);
	}

	/**
	 * Saves experiment's data to file.
	 * 
	 * @param sShell
	 *            Shell used to display the Save Dialog
	 */
	public void saveAction(final Composite parent) {
		final Shell sShell = parent.getShell();
		try {
			final FileDialog fileDialog = new FileDialog(sShell, SWT.SAVE);
			final String file_name = fileDialog.open();
			if (file_name != null) {
				ExperimentManager.getDefault().setExpInfo(name, user, date,
						notes, type);

				for (final Grp2GUI grp : groups) {
					ExperimentManager.getDefault().updateGrpInfo(grp.getId(),
							grp.getName(), grp.getNotes());
				}

				ExperimentManager.getDefault().saveExperimentToFile(file_name);
			}

		} catch (final NumberFormatException e1) {
			System.out.print("Error in user input ... aborting !\n");
		}
	}

	public boolean setExpVars(final String[] strs) {
		name = strs[0];
		user = strs[1];
		date = strs[2];
		notes = strs[3];
		type = strs[4];
		return true;
	}

	public void show(final Shell shell, final boolean newExp) {
		final NewExperimentWizard expWiz = new NewExperimentWizard(this);
		final WizardDialog wd = new WizardDialog(shell, expWiz);
		wd.create();
		if ((newExp == false)
				&& ExperimentManager.getDefault().isExperimentPresent()) {
			// load data to GUI
			final Exp2GUI expInfo = ExperimentManager.getDefault()
					.getExpBasicInfo();
			final Grp2GUI[] grpsInfo = ExperimentManager.getDefault()
					.getExpGroupsInfo();

			expWiz.loadInfoToGUI(expInfo, grpsInfo);
		}
		wd.open();
	}
}
