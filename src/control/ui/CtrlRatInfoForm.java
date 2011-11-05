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

import modules.ModulesManager;
import modules.experiment.openfield.ExperimentModule;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import ui.RatInfoForm;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * Controller of the RatInfoForm GUI window.
 * 
 * @author Creative
 */
public class CtrlRatInfoForm extends ControllerUI<RatInfoForm>
{
	private int rat_num;
	private String grp_name;
	private boolean iamready;
	private boolean cancelled;

	/**
	 * Initializes class attributes (RatInfoForm ,InfoController and PManager).
	 */
	public CtrlRatInfoForm()
	{
		pm = PManager.getDefault();
		ui = new RatInfoForm();
		ui.setController(this);
	}

	@Override
	public boolean setVars(final String[] strs)
	{
		if (!strs[0].isEmpty())
		{
			rat_num = Integer.parseInt(strs[0]);
			grp_name = strs[1];
			return true;
		}
		return false;
	}

	@Override
	public void show(final boolean visibility)
	{
		final String[] grps = ((ExperimentModule) ModulesManager.getDefault()
				.getModuleByName("Experiment Module")).getGroupsNames();
		final String[] params = new String[grps.length + 1];
		iamready = false;
		cancelled = false;
		params[0] = "";
		for (int i = 0; i < grps.length; i++)
			params[i + 1] = grps[i];
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run()
			{
				ui.clearForm();
				ui.loadData(params);
				ui.show(visibility);
			}
		});

	}

	/**
	 * Checks data entered and submit it if correct.
	 */
	public void btnOkAction()
	{
		if (checkAndSubmitData())
		{
			iamready = true;
			ui.show(false);
		}
	}

	/**
	 * Checks the validity of entered Rat Number & Group name. if the Rat Number
	 * already exists, it asks for confirmation; if the Group name is not valid,
	 * error message is displayed; if all data are correct, we start tracking
	 * activity.
	 * 
	 * @return true: success
	 */
	public boolean checkAndSubmitData()
	{
		try
		{

			final int tmp_confirmation = ((ExperimentModule) ModulesManager.getDefault()
					.getModuleByName("Experiment Module")).validateRatAndGroup(
					rat_num,
					grp_name);
			if (tmp_confirmation == 0)
			{
				((ExperimentModule) ModulesManager.getDefault().getModuleByName(
						"Experiment Module")).setCurrentRatAndGroup(rat_num, grp_name);
				return true;
			}
			else if (tmp_confirmation == 1)
			{ // Rat already exists
				final MessageBox msg = new MessageBox(ui.getShell(), SWT.ICON_QUESTION
						| SWT.YES
						| SWT.NO);
				msg.setText("Confirmation");
				msg.setMessage("Rat '" + rat_num + "' already exists, replace it?");
				final int res = msg.open();
				if (res == SWT.YES)
				{
					((ExperimentModule) ModulesManager.getDefault().getModuleByName(
							"Experiment Module")).setCurrentRatAndGroup(rat_num, grp_name);
					return true;
				}
			}
			else if (tmp_confirmation == -1) // Group not found
				pm.status_mgr.setStatus("Please select a group.", StatusSeverity.ERROR);
		} catch (final NumberFormatException e1)
		{
			pm.status_mgr.setStatus(
					"Please enter a valid Rat number.",
					StatusSeverity.ERROR);
		}
		return false;
	}

	/**
	 * Checks if a valid rat/group has been selected.
	 * 
	 * @return true: valid rat, false: invalid rat or user hasn't selected a rat
	 */
	public boolean isValidRatEntered()
	{
		return iamready;
	}

	/**
	 * Action taken when the cancel button is pressed.
	 */
	public void cancelAction()
	{
		cancelled = true;
		ui.show(false);
	}

	/**
	 * Checks if the user has canceled the RatInfoForm.
	 * 
	 * @return true: user has canceled the form, false: user hasn't cancel the
	 *         form
	 */
	public boolean isCancelled()
	{
		return cancelled;
	}
}
