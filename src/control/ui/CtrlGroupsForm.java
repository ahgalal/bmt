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

import java.util.ArrayList;

import modules.ModulesManager;
import modules.experiment.ExperimentModule;
import modules.experiment.Grp2GUI;
import ui.GroupsForm;
import ui.GroupsForm.TabContents;

/**
 * Controller of the GroupsForm GUI window.
 * 
 * @author Creative
 */
public class CtrlGroupsForm extends ControllerUI
{
	private final GroupsForm ui;

	/**
	 * Initializes class attributes (GroupsForm and InfoController).
	 */
	public CtrlGroupsForm()
	{
		ui = new GroupsForm();
		ui.setController(this);
	}

	@Override
	public boolean setVars(final String[] strs)
	{
		return true;
	}

	/**
	 * Saves all entered groups by the user to the InfoController.
	 * 
	 * @param arr_tabs
	 *            ArrayList of tabs, each tab contains single group information
	 */
	public void btnSaveAction(final ArrayList<TabContents> arr_tabs)
	{
		try
		{
			String name, notes;

			for (final TabContents tc : arr_tabs)
			{
				name = tc.txt_name.getText();
				notes = tc.txt_notes.getText();
				((ExperimentModule) ModulesManager.getDefault().getModuleByName(
						"Experiment Module")).saveGrpInfo(
						tc.grp_id,
						name,
						notes);
			}

			show(false);
		} catch (final NumberFormatException e1)
		{
			System.out.print("Error in user input ... aborting !\n");
		}

	}

	@Override
	public void show(final boolean visibility)
	{
		ui.show(visibility);
	}

	/**
	 * Loads data of an array of Groups to the GUI window.
	 * 
	 * @param grp
	 *            array of groups, containing information to be presented on GUI
	 */
	public void loadDataToForm(final Grp2GUI[] grp)
	{
		for (final Grp2GUI tmp_grp : grp)
		{
			ui.addNewTab(
					tmp_grp.getId(),
					tmp_grp.getName(),
					Integer.toString(tmp_grp.getNoRats()),
					tmp_grp.getRatsNumbering(),
					tmp_grp.getNotes());
		}
	}

	/**
	 * Clears the GUI data.
	 */
	public void clearForm()
	{
		ui.clearForm();
	}

}
