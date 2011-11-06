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

package modules.experiment;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import modules.ModulesManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.Data;

/**
 * Experiment Module, it saves, loads and updated experiment's data.
 * 
 * @author Creative
 */
public class ExperimentModule extends
		Module<ExperimentModuleGUI, ExperimentModuleConfigs, ExperimentModuleData>
{
	private final TextEngine text_engine;
	private final ExcelEngine excel_engine;
	private boolean exp_is_set;

	/**
	 * Initializes the Experiment module.
	 * 
	 * @param name
	 *            module's name
	 * @param config
	 *            module's configurations
	 */
	public ExperimentModule(final String name, final ExperimentModuleConfigs config)
	{
		super(name, config);
		gui = new ExperimentModuleGUI();
		data = new ExperimentModuleData("Experiment Module Data");
		data.exp = new Experiment();
		text_engine = new TextEngine();
		excel_engine = new ExcelEngine();

		initialize();
	}

	@Override
	public void deInitialize()
	{
		if (isExperimentPresent())
			saveRatInfo();
	}

	@Override
	public void process()
	{

	}

	@Override
	public void updateGUICargoData()
	{
		gui_cargo.setDataByTag(Constants.GUI_EXP_NAME, data.exp.getName());
		gui_cargo.setDataByTag(Constants.GUI_GROUP_NAME, data.currGrpName);
		gui_cargo.setDataByTag(
				Constants.GUI_RAT_NUMBER,
				Integer.toString(data.currRatNumber));
	}

	@Override
	public void updateFileCargoData()
	{
		file_cargo.setDataByTag(
				Constants.FILE_RAT_NUMBER,
				Integer.toString(data.currRatNumber));
		file_cargo.setDataByTag(Constants.FILE_GROUP_NAME, data.currGrpName);
	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
	}

	@Override
	public void registerFilterDataObject(final Data data)
	{
	}

	/**
	 * Sets the file name to save the experiment to.
	 * 
	 * @param f_name
	 *            file name to save the experiment to
	 */
	public void setExpFileName(final String f_name)
	{
		data.exp.fileName = f_name;
	}

	/**
	 * is there an active experiment?
	 * 
	 * @return true/false
	 */
	public boolean isExperimentPresent()
	{
		return exp_is_set;
	}

	/**
	 * Does the active experiment have any groups?
	 * 
	 * @return true/false
	 */
	public boolean isThereAnyGroups()
	{
		return (data.exp.getNoGroups() != 0);
	}

	/**
	 * Saves experiment's information.
	 * 
	 * @param name
	 *            experiment's name
	 * @param user
	 *            experiment's user name
	 * @param date
	 *            experiment's date of creation
	 * @param notes
	 *            notes on the experiment
	 */
	public void saveExpInfo(
			final String name,
			final String user,
			final String date,
			final String notes)
	{
		data.exp.setExperimentInfo(name, user, date, notes);
		exp_is_set = true;
		(gui).setExperimantLoaded(true);
	}

	/**
	 * Saves group information, if the group exists, it is updated; else, a new
	 * group is created.
	 * 
	 * @param grp_id
	 *            id of the coup to edit
	 * @param name
	 *            new name of the group
	 * @param notes
	 *            notes on the group
	 */
	public void saveGrpInfo(
			final int grp_id,
			final String name,
			final String notes)
	{
		final Group tmp_grp = data.exp.getGroupByID(grp_id);
		if (tmp_grp == null)
		{
			final Group gp = new Group(grp_id, name, notes);
			data.exp.addGroup(gp);
		}
		else
		// group is already existing ... edit it..
		{
			tmp_grp.setName(name);
			tmp_grp.setNotes(notes);
		}
	}

	/**
	 * Gets the index of an element in an array, using the element's value.
	 * 
	 * @param arr
	 *            array to get the index from
	 * @param val
	 *            value to get it's index in the array
	 * @return index of the value in the array
	 */
	private int getIndexByStringValue(final String[] arr, final String val)
	{
		for (int i = 0; i < arr.length; i++)
			if (arr[i].equals(val))
				return i;
		return -1;
	}

	/**
	 * Saves the rat's info to the experiment object.
	 * 
	 * @return true: success
	 */
	public boolean saveRatInfo()
	{
		if (data.exp.getExpParametersList() == null)
			data.exp.setParametersList(ModulesManager.getDefault()
					.getCodeNames());
		else if (getNumberOfExpParams() != ModulesManager.getDefault().getCodeNames().length)
		{
			PManager.getDefault().status_mgr.setStatus(
					"Experiment file loaded has some modules which are not active in this session, can't save",
					StatusSeverity.ERROR);
			return false;
		}
		final String[] params_list = data.exp.getExpParametersList();
		final String[] data = ModulesManager.getDefault().getFileData();
		final String[] code_names = ModulesManager.getDefault().getCodeNames();
		boolean override_rat = false;
		Rat rat_tmp = this.data.exp.getGroupByName(this.data.currGrpName)
				.getRatByNumber(this.data.currRatNumber);
		if (rat_tmp == null)
			rat_tmp = new Rat(params_list);
		else
			override_rat = true;

		for (int i = 0; i < params_list.length; i++)
		{
			rat_tmp.setValueByParameterName(
					params_list[i],
					data[getIndexByStringValue(code_names, params_list[i])]);
		}

		if (!override_rat)
			this.data.exp.getGroupByName(this.data.currGrpName).addRat(
					rat_tmp);
		writeToTXTFile(this.data.exp.fileName);
		return true;
	}

	/**
	 * Writes the experiment to a text file.
	 * 
	 * @param FilePath
	 *            file path to write to
	 */
	public void writeToTXTFile(final String FilePath)
	{
		text_engine.writeExpInfoToTXTFile(FilePath, data.exp);
	}

	/**
	 * Loads an experiment from a text file to an experiment object.
	 * 
	 * @param file_name
	 *            file name to load the experiment from
	 */
	public void loadInfoFromTXTFile(final String file_name)
	{
		if (text_engine.readExpInfoFromTXTFile(file_name, data.exp))
		{
			PManager.getDefault().frm_exp.fillForm(data.exp);
			updateGroupGUIData();
			exp_is_set = true;
			(gui).setExperimantLoaded(true);
		}
	}

	/**
	 * Updates the Groups GUI window with the latest groups information.
	 */
	public void updateGroupGUIData()
	{
		final Grp2GUI[] arr_grps = new Grp2GUI[data.exp.getNoGroups()];
		data.exp.getGroups().toArray(arr_grps);
		PManager.getDefault().frm_grps.clearForm();
		PManager.getDefault().frm_grps.loadDataToForm(arr_grps);
	}

	/**
	 * Saves the experiment to an Excel file.
	 * 
	 * @param FilePath
	 *            FilePath file path to write to
	 */
	public void writeToExcelFile(final String FilePath)
	{
		excel_engine.writeExpInfoToExcelFile(FilePath, data.exp);
	}

	/**
	 * Gets the names of groups in the active experiment.
	 * 
	 * @return String array containing groups' names
	 */
	public String[] getGroupsNames()
	{
		final String[] res = new String[data.exp.getGroups().size()];
		int i = 0;
		for (final Grp2GUI grp : data.exp.getGroups())
		{
			res[i] = grp.getName();
			i++;
		}
		return res;
	}

	/**
	 * Checks that the group already exists, and if the rat already exists.
	 * 
	 * @param rat_num
	 *            rat number to check its existence
	 * @param grp_name
	 *            group name to check its existence
	 * @return integer: 0: Group exists, rat doesn't exist (it's a new rat);
	 *         1:Group exists, rat also exists; -1: Group doesn't exist
	 */
	public int validateRatAndGroup(final int rat_num, final String grp_name)
	{
		final Group tmp_grp = data.exp.getGroupByName(grp_name);
		if (tmp_grp != null)
		{ // Group exists
			if (tmp_grp.getRatByNumber(rat_num) == null)
			{
				return 0;
			}
			return 1;
		}
		else
			return -1;
	}

	/**
	 * Sets the active group and rat number.
	 * 
	 * @param rat_num
	 *            rat number to be active and save exp_module_data.exp. info to
	 * @param grp_name
	 *            group name to be active and save the active rat to
	 * @return 0: success, -1: group doesn't exist
	 */
	public int setCurrentRatAndGroup(final int rat_num, final String grp_name)
	{
		final Group tmp_grp = data.exp.getGroupByName(grp_name);
		if (tmp_grp != null)
		{ // Group exists
			data.currRatNumber = rat_num;
			data.currGrpName = grp_name;
			return 0;
		}
		else
			return -1;
	}

	/**
	 * Clears and unloads the active experiment.
	 */
	public void unloadExperiment()
	{
		data.exp.clearExperimentData();
		excel_engine.reset();
		exp_is_set = false;
		(gui).setExperimantLoaded(false);
	}

	@Override
	public void initialize()
	{
		gui_cargo = new Cargo(
				new String[] {
						Constants.GUI_EXP_NAME,
						Constants.GUI_GROUP_NAME,
						Constants.GUI_RAT_NUMBER });

		file_cargo = new Cargo(new String[] {
				Constants.FILE_RAT_NUMBER,
				Constants.FILE_GROUP_NAME });
	}

	@Override
	public void deRegisterDataObject(final Data data)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Gets the number of Experiment's parameters.
	 * 
	 * @return integer representing the number of experiment's parameters
	 */
	public int getNumberOfExpParams()
	{
		return data.exp.getExpParametersList().length;
	}

	private boolean rat_frm_is_shown = false;
	private boolean msgbox_pending = true;
	private boolean force_ready = false;

	@Override
	public boolean amIReady(final Shell shell)
	{
		rat_frm_is_shown = false;
		msgbox_pending = true;
		force_ready = false;
		if (isExperimentPresent())
		{
			// if we had an empty experiment (no parameters), we assign the
			// set of parameters from the module manager to the exp.
			if (getNumberOfExpParams() == 0)
				data.exp.setParametersList(ModulesManager.getDefault()
						.getCodeNames());
			if (getNumberOfExpParams() != ModulesManager.getDefault()
					.getNumberOfFileParameters())
			{
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run()
					{
						final MessageBox mbox = new MessageBox(
								shell,
								SWT.ICON_QUESTION
										| SWT.YES
										| SWT.NO);
						mbox.setMessage("Experiment loaded has some Modules" +
								" which are not active now, you can't save the" +
								" experiment when done!, Continue?");
						mbox.setText("Continue?");
						if (mbox.open() == SWT.YES)
						{
							PManager.getDefault().frm_rat.show(true);
							rat_frm_is_shown = true;
							msgbox_pending = false;
						}
						else
							msgbox_pending = false;
					}
				});
			}
			else
			{
				PManager.getDefault().frm_rat.show(true);
				rat_frm_is_shown = true;
				msgbox_pending = false;
			}

		}
		else
		{
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run()
				{
					final MessageBox mbox = new MessageBox(
							shell,
							SWT.ICON_QUESTION
									| SWT.YES
									| SWT.NO);
					mbox.setMessage("No experiment is loaded!, Continue?");
					mbox.setText("Continue?");
					if (mbox.open() == SWT.YES)
						force_ready = true;
					else
						force_ready = false;
					msgbox_pending = false;
				}
			});
		}
		while (msgbox_pending)
			try
			{
				Thread.sleep(200);
			} catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
		if (force_ready)
			return true;
		return waitForRatFrm();
	}

	/**
	 * Waits till user takes action in the RatInfoForm.
	 * 
	 * @return true: a valid rat is entered, false: cancel is pressed
	 */
	private boolean waitForRatFrm()
	{
		while (rat_frm_is_shown
					&& (!PManager.getDefault().frm_rat.isValidRatEntered()
					&& !PManager.getDefault().frm_rat.isCancelled()))
			try
			{
				Thread.sleep(200);
			} catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
		if (PManager.getDefault().frm_rat.isValidRatEntered())
			return true;
		else
			return false;
	}

	@Override
	public void registerModuleDataObject(final Data data)
	{
		// TODO Auto-generated method stub

	}

}
