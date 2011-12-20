/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

package control.ui;

import modules.ExperimentManager;
import modules.experiment.Exp2GUI;

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
public class CtrlExperimentForm extends ControllerUI<ExperimentForm> {
    private String name, user, date, notes, type;

    @Override
    public boolean setVars(final String[] strs) {
	name = strs[0];
	user = strs[1];
	date = strs[2];
	notes = strs[3];
	type = strs[4];
	return true;
    }

    /**
     * Initializes class attributes (ExperimentForm , PManager and
     * InfoController).
     */
    public CtrlExperimentForm() {
	pm = PManager.getDefault();
	ui = new ExperimentForm();
	ui.setController(this);
    }

    @Override
    public void show(final boolean visibility) {
	ui.show(visibility);
    }

    /**
     * Fills the GUI controls with data from the experiment object.
     * 
     * @param exp
     *            Experiment object that contains data to be displayed on GUI
     */
    public void fillForm(final Exp2GUI exp) {
	ui.loadData(new String[] { exp.getName(), exp.getUser(),
		exp.getNotes(), exp.getDate() });
    }

    /**
     * Saves experiment's data to file.
     * 
     * @param sShell
     *            Shell used to display the Save Dialog
     */
    public void btnSaveAction(final Shell sShell) {
	try {
	    if (pm.frm_grps.getNumberOfGroups() == 0)
		System.out.print("please specify at least one group.\n");
	    else {
		final FileDialog fileDialog = new FileDialog(sShell, SWT.SAVE);
		final String file_name = fileDialog.open();
		if (file_name != null) {
		    ExperimentManager.getDefault().setExpInfo(name, user, date,
			    notes, type);
		    pm.frm_grps.updateGroupsInformation();
		    ExperimentManager.getDefault().saveExperimentToFile(file_name);
		    ExperimentManager.getDefault().setExpFileName(file_name);
		    sShell.setVisible(false);
		}
	    }

	} catch (final NumberFormatException e1) {
	    System.out.print("Error in user input ... aborting !\n");
	}
    }

    /**
     * Shows GroupsForm GUI window.
     */
    public void btnMngGrpsAction() {
	ExperimentManager.getDefault().updateGroupGUIData();
	pm.frm_grps.show(true);
    }

    /**
     * Clears the GUI data.
     */
    public void clearForm() {
	ui.clearForm();
    }
}
