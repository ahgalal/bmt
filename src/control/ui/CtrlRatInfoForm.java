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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import ui.RatInfoForm;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * Controller of the RatInfoForm GUI window.
 * 
 * @author Creative
 */
public class CtrlRatInfoForm extends ControllerUI<RatInfoForm> {
	private boolean	cancelled;
	private String	grpName;
	private boolean	ready;
	private int		ratNum;

	/**
	 * Initializes class attributes (RatInfoForm ,InfoController and PManager).
	 */
	public CtrlRatInfoForm() {
		pm = PManager.getDefault();
		ui = new RatInfoForm();
		ui.setController(this);
	}

	/**
	 * Checks data entered and submit it if correct.
	 */
	public void btnOkAction() {
		if (checkAndSubmitData()) {
			ready = true;
			ui.show(false);
		}
	}

	/**
	 * Action taken when the cancel button is pressed.
	 */
	public void cancelAction() {
		cancelled = true;
		ui.show(false);
	}

	/**
	 * Checks the validity of entered Rat Number & Group name. if the Rat Number
	 * already exists, it asks for confirmation; if the Group name is not valid,
	 * error message is displayed; if all data are correct, we start tracking
	 * activity.
	 * 
	 * @return true: success
	 */
	public boolean checkAndSubmitData() {
		try {

			final int tmpConfirmation = ExperimentManager.getDefault()
					.validateRatAndGroup(ratNum, grpName);
			if (tmpConfirmation == 0) {
				ExperimentManager.getDefault().setCurrentRatAndGroup(ratNum,
						grpName);
				return true;
			} else if (tmpConfirmation == 1) { // Rat already exists
				final MessageBox msg = new MessageBox(ui.getShell(),
						SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				msg.setText("Confirmation");
				msg.setMessage("Rat '" + ratNum
						+ "' already exists, replace it?");
				final int res = msg.open();
				if (res == SWT.YES) {
					ExperimentManager.getDefault().setCurrentRatAndGroup(
							ratNum, grpName);
					return true;
				}
			} else if (tmpConfirmation == -1) // Group not found
				pm.getStatusMgr().setStatus("Please select a group.",
						StatusSeverity.ERROR);
		} catch (final NumberFormatException e1) {
			pm.getStatusMgr().setStatus("Please enter a valid Rat number.",
					StatusSeverity.ERROR);
		}
		return false;
	}

	/**
	 * Checks if the user has canceled the RatInfoForm.
	 * 
	 * @return true: user has canceled the form, false: user hasn't cancel the
	 *         form
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Checks if a valid rat/group has been selected.
	 * 
	 * @return true: valid rat, false: invalid rat or user hasn't selected a rat
	 */
	public boolean isValidRatEntered() {
		return ready;
	}

	@Override
	public boolean setVars(final String[] strs) {
		if (!strs[0].isEmpty()) {
			ratNum = Integer.parseInt(strs[0]);
			grpName = strs[1];
			return true;
		}
		return false;
	}

	@Override
	public void show(final boolean visibility) {
		final String[] grps = ExperimentManager.getDefault().getGroupsNames();
		final String[] params = new String[grps.length + 1];
		ready = false;
		cancelled = false;
		params[0] = "";
		for (int i = 0; i < grps.length; i++)
			params[i + 1] = grps[i];
		PManager.getDefault().displaySyncExec(new Runnable() {

			@Override
			public void run() {
				ui.clearForm();
				ui.loadData(params);
				ui.show(visibility);
			}
		});

	}
}
