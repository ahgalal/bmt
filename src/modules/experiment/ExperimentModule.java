package modules.experiment;

import modules.Cargo;
import modules.Module;
import modules.ModuleConfigs;
import modules.ModulesManager;
import utils.PManager;
import utils.saveengines.ExcelEngine;
import utils.saveengines.TextEngine;
import utils.video.filters.Data;

/**
 * Experiment Module, it saves, loads and updated experiment's data.
 * 
 * @author Creative
 */
public class ExperimentModule extends Module
{
	public static final String GUI_EXP_NAME = "Experiment's Name";
	public static final String GUI_GROUP_NAME = "Group's Name";
	public static final String GUI_RAT_NUMBER = "Rat Number";
	public static final String FILE_RAT_NUMBER = "Number";
	public static final String FILE_GROUP_NAME = "Group";
	private int curr_rat_number;
	private String curr_grp_name;
	private final Experiment exp;
	private String exp_file_name;

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
	public ExperimentModule(final String name, final ModuleConfigs config)
	{
		super(name, config);
		exp = new Experiment();
		text_engine = new TextEngine();
		excel_engine = new ExcelEngine();

		initialize();
	}

	@Override
	public void deInitialize()
	{
		saveRatInfo();
	}

	@Override
	public void process()
	{

	}

	@Override
	public void updateGUICargoData()
	{
		gui_cargo.setDataByTag(GUI_EXP_NAME, exp.getName());
		gui_cargo.setDataByTag(GUI_GROUP_NAME, curr_grp_name);
		gui_cargo.setDataByTag(GUI_RAT_NUMBER, Integer.toString(curr_rat_number));
	}

	@Override
	public void updateFileCargoData()
	{
		file_cargo.setDataByTag(FILE_RAT_NUMBER, Integer.toString(curr_rat_number));
		file_cargo.setDataByTag(FILE_GROUP_NAME, curr_grp_name);

	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void registerDataObject(final Data data)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Sets the file name to save the experiment to.
	 * 
	 * @param f_name
	 *            file name to save the experiment to
	 */
	public void setExpFileName(final String f_name)
	{
		exp_file_name = f_name;
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
		return (exp.getNoGroups() != 0);
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
		exp.setExperimentInfo(name, user, date, notes);
		exp_is_set = true;
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
		final Group tmp_grp = exp.getGroupByID(grp_id);
		if (tmp_grp == null)
		{
			final Group gp = new Group(grp_id, name, notes);
			exp.addGroup(gp);
		} else
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
	 */
	public void saveRatInfo()
	{
		if (exp.getExpParametersList() == null)
			exp.setParametersList(ModulesManager.getDefault().getCodeNames());
		final String[] params_list = exp.getExpParametersList();
		final String[] data = ModulesManager.getDefault().getFileData();
		final String[] code_names = ModulesManager.getDefault().getCodeNames();
		boolean override_rat = false;
		Rat rat_tmp = exp.getGroupByName(curr_grp_name).getRatByNumber(curr_rat_number);
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
			exp.getGroupByName(curr_grp_name).addRat(rat_tmp);
		writeToTXTFile(exp_file_name);
	}

	/**
	 * Writes the experiment to a text file.
	 * 
	 * @param FilePath
	 *            file path to write to
	 */
	public void writeToTXTFile(final String FilePath)
	{
		text_engine.writeExpInfoToTXTFile(FilePath, exp);
	}

	/**
	 * Loads an experiment from a text file to an experiment object.
	 * 
	 * @param file_name
	 *            file name to load the experiment from
	 */
	public void loadInfoFromTXTFile(final String file_name)
	{
		if (text_engine.readExpInfoFromTXTFile(file_name, exp))
		{
			PManager.getDefault().frm_exp.fillForm(exp);
			updateGroupGUIData();
			exp_is_set = true;
		}
	}

	/**
	 * Updates the Groups GUI window with the latest groups information.
	 */
	public void updateGroupGUIData()
	{
		final Grp2GUI[] arr_grps = new Grp2GUI[exp.getNoGroups()];
		exp.getGroups().toArray(arr_grps);
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
		excel_engine.writeExpInfoToExcelFile(FilePath, exp);
	}

	/**
	 * Gets the names of groups in the active experiment.
	 * 
	 * @return String array containing groups' names
	 */
	public String[] getGroupsNames()
	{
		final String[] res = new String[exp.getGroups().size()];
		int i = 0;
		for (final Grp2GUI grp : exp.getGroups())
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
		final Group tmp_grp = exp.getGroupByName(grp_name);
		if (tmp_grp != null)
		{ // Group exists
			if (tmp_grp.getRatByNumber(rat_num) == null)
			{
				return 0;
			}
			return 1;
		} else
			return -1;
	}

	/**
	 * Sets the active group and rat number.
	 * 
	 * @param rat_num
	 *            rat number to be active and save exp. info to
	 * @param grp_name
	 *            group name to be active and save the active rat to
	 * @return
	 */
	public int setCurrentRatAndGroup(final int rat_num, final String grp_name)
	{
		final Group tmp_grp = exp.getGroupByName(grp_name);
		if (tmp_grp != null)
		{ // Group exists
			curr_rat_number = rat_num;
			curr_grp_name = grp_name;
			return 0;
		} else
			return -1;
	}

	/**
	 * Clears and unloads the active experiment.
	 */
	public void unloadExperiment()
	{
		exp.clearExperimentData();
		excel_engine.reset();
		exp_is_set = false;
	}

	@Override
	public void initialize()
	{
		gui_cargo = new Cargo(
				new String[] { GUI_EXP_NAME, GUI_GROUP_NAME, GUI_RAT_NUMBER });

		file_cargo = new Cargo(new String[] { FILE_RAT_NUMBER, FILE_GROUP_NAME });
	}

	@Override
	public void deRegisterDataObject(final Data data)
	{
		// TODO Auto-generated method stub

	}

}
