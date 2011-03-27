package modules;

import model.business.Experiment;
import model.business.Group;
import model.business.Grp2GUI;
import model.business.Rat;
import utils.PManager;
import utils.saveengines.ExcelEngine;
import utils.saveengines.TextEngine;
import utils.video.filters.Data;

public class ExperimentModule extends Module
{

	private int curr_rat_number;
	private String curr_grp_name;
	private final Experiment exp;
	private String exp_file_name;

	private final TextEngine text_engine;
	private final ExcelEngine excel_engine;
	private boolean exp_is_set;

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
		gui_cargo.setDataByIndex(0, exp.getName());
		gui_cargo.setDataByIndex(1, curr_grp_name);
		gui_cargo.setDataByIndex(2, Integer.toString(curr_rat_number));

	}

	@Override
	public void updateFileCargoData()
	{
		file_cargo.setDataByIndex(0, Integer.toString(curr_rat_number));
		file_cargo.setDataByIndex(1, curr_grp_name);
	}

	@Override
	public void updateConfigs(final ModuleConfigs config)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDataObject(final Data data)
	{
		// TODO Auto-generated method stub

	}

	public void setExpFileName(final String f_name)
	{
		exp_file_name = f_name;
	}

	public boolean isExperimentPresent()
	{
		return exp_is_set;
	}

	public boolean isThereAnyGroups()
	{
		return (exp.getNoGroups() != 0);
	}

	public void saveExpInfo(
			final String name,
			final String user,
			final String date,
			final String notes)
	{
		exp.setExperimentInfo(name, user, date, notes);
		exp_is_set=true;
	}

	public void saveGrpInfo(
			final int grp_id,
			final String name,
			final String rats_numbering,
			final String notes)
	{
		final Group tmp_grp = exp.getGroupByID(grp_id);
		if (tmp_grp == null)
		{
			final Group gp = new Group(grp_id, name, rats_numbering, notes);
			exp.addGroup(gp);
		} else
		// group is already existing ... edit it..
		{
			tmp_grp.setName(name);
			tmp_grp.setNotes(notes);
			tmp_grp.setRatsNumbering(rats_numbering);
		}
	}

	private int getIndexByStringValue(final String[] arr, final String val)
	{
		for (int i = 0; i < arr.length; i++)
			if (arr[i].equals(val))
				return i;
		return -1;
	}

	public void saveRatInfo()
	{
		if (exp.getMeasurementsList() == null)
			exp.setMeasurementsList(ModulesManager.getDefault().getCodeNames());
		final String[] measurements_list = exp.getMeasurementsList();
		final String[] data = ModulesManager.getDefault().getFileData();
		final String[] code_names = ModulesManager.getDefault().getCodeNames();
		boolean override_rat = false;
		Rat rat_tmp = exp.getGroupByName(curr_grp_name).getRatByNumber(curr_rat_number);
		if (rat_tmp == null)
			rat_tmp = new Rat(measurements_list);
		else
			override_rat = true;

		for (int i = 0; i < measurements_list.length; i++)
		{
			rat_tmp.setValueByMeasurementName(
					measurements_list[i],
					data[getIndexByStringValue(code_names, measurements_list[i])]);
		}

		if (!override_rat)
			exp.getGroupByName(curr_grp_name).addRat(rat_tmp);
		writeToTXTFile(exp_file_name);
	}

	public void writeToTXTFile(final String FilePath)
	{
		text_engine.writeExpInfoToTXTFile(FilePath, exp);
	}

	public void loadInfoFromTXTFile(final String file_name)
	{
		if (text_engine.readExpInfoFromTXTFile(file_name, exp))
		{
			PManager.getDefault().frm_exp.fillForm(exp);
			final Grp2GUI[] arr_grps = new Grp2GUI[exp.getNoGroups()];
			exp.getGroups().toArray(arr_grps);
			PManager.getDefault().frm_grps.loadDataToForm(arr_grps);
			exp_is_set=true;
		}
	}

	public void writeToExcelFile(final String FilePath)
	{
		excel_engine.writeExpInfoToExcelFile(FilePath, exp);
	}

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

	public void unloadExperiment()
	{
		exp.clearExperimentData();
		excel_engine.reset();
		exp_is_set = false;
	}

	@Override
	public void initialize()
	{
		gui_cargo = new Cargo(new String[] { "Experiment's Name", "Group's Name",
				"Rat Number" });

		file_cargo = new Cargo(new String[] { "Number", "Group" });
	}

}
