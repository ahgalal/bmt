package modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import modules.experiment.ExcelEngine;
import modules.experiment.Exp2GUI;
import modules.experiment.Experiment;
import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleConfigs;
import modules.experiment.Group;
import modules.experiment.Grp2GUI;
import modules.experiment.Rat;
import modules.experiment.TextEngine;
import modules.experiment.forcedswimming.ForcedSwimmingExperimentModule;
import modules.experiment.openfield.OpenFieldExperimentModule;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

public class ExperimentManager {
	public static final String	DELETED_GROUP_NAME	= "-";
	private static ExperimentManager	me;

	public static ExperimentManager getDefault() {
		if (me == null)
			me = new ExperimentManager();
		return me;
	}

	private String					currGrpName;
	private int						currRatNumber;
	public ExcelEngine				excel_engine;
	private Experiment				exp;
	public boolean					exp_is_set;

	private ExperimentModuleConfigs	experiment_configs;

	private ExperimentModule		experiment_module;

	public TextEngine				text_engine;

	private ExperimentManager() {
		text_engine = new TextEngine();
		excel_engine = new ExcelEngine();
	}

	public String getCurrGrpName() {
		return currGrpName;
	}

	public int getCurrRatNumber() {
		return currRatNumber;
	}

	public Exp2GUI getExpBasicInfo() {
		return exp;
	}

	/**
	 * Gets the names of groups in the active experiment.
	 * 
	 * @return String array containing groups' names
	 */
	public String[] getGroupsNames() {
		final String[] res = new String[exp.getGroups().size()];
		int i = 0;
		for (final Grp2GUI grp : exp.getGroups()) {
			res[i] = grp.getName();
			i++;
		}
		return res;
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
	private int getIndexByStringValue(final String[] arr, final String val) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i].equals(val))
				return i;
		return -1;
	}

	/**
	 * Gets the number of Experiment's parameters.
	 * 
	 * @return integer representing the number of experiment's parameters
	 */
	public int getNumberOfExpParams() {
		if (exp.getExpParametersList() != null)
			return exp.getExpParametersList().length;
		else
			return 0;

	}

	public ExperimentModule instantiateExperimentModule() {
		experiment_configs = new ExperimentModuleConfigs("Experiment Module",
				exp);

		switch (exp.type) {
			case OPEN_FIELD:
				experiment_module = new OpenFieldExperimentModule(
						"Experiment Module", experiment_configs);
				break;
			case FORCED_SWIMMING:
				experiment_module = new ForcedSwimmingExperimentModule(
						"Experiment Module", experiment_configs);
				break;
			default:
				break;
		}

		return experiment_module;
	}

	/**
	 * is there an active experiment?
	 * 
	 * @return true/false
	 */
	public boolean isExperimentPresent() {
		return exp_is_set;
	}

	/**
	 * Loads an experiment from a text file to an experiment object.
	 * 
	 * @param file_name
	 *            file name to load the experiment from
	 */
	public static Experiment readExperimentFromFile(final String file_name) {
		ObjectInputStream ois;
		Experiment exp = null;
		try {
			ois = new ObjectInputStream(
					new FileInputStream(new File(file_name)));
			exp = (Experiment) ois.readObject();
			exp.fileName=file_name;
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			System.err.println("Experiment loading error, Experiment file may be corrupted");
		} catch (final ClassNotFoundException e) {
			System.err.println("incompatible experiment file!");
		}
		return exp;
	}

	public void loadExperiment(Experiment experiment){
		if (experiment != null) {
			exp_is_set = true;
			this.exp=experiment;
			ModulesManager.getDefault().setupModulesAndFilters(exp);
			setExperimantLoadedInGUI(true);
		}
	}

	/**
	 * Writes the experiment to a text file.
	 * 
	 * @param FilePath
	 *            file path to write to
	 */
	public void saveExperimentToFile(final String FilePath) {
		try {
			final ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File(FilePath)));
			oos.writeObject(exp);
			exp.fileName=FilePath;
			oos.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// text_engine.writeExpInfoToTXTFile(FilePath, exp);
	}

	public void addExpParams(ArrayList<String> arrayList){
		ArrayList<String> allParams = new ArrayList<String>();
		if(exp.getExpParametersList()!=null)
			for(String param: exp.getExpParametersList())
				allParams.add(param);

		outer:
			for(String param: arrayList){
				for(String str: allParams){
					if(param.equals(str)){
						continue outer;
					}
				}
				allParams.add(param);
			}

		exp.setParametersList(allParams.toArray(new String[0]));
	}

	/**
	 * Saves the rat's info to the experiment object.
	 * 
	 * @return true: success
	 */
	public boolean saveRatInfo() {
		if (isExperimentPresent()) {
			if (exp.getExpParametersList() == null)
				exp.setParametersList(ModulesManager.getDefault()
						.getCodeNames());
			else if (getNumberOfExpParams() != ModulesManager.getDefault()
					.getCodeNames().length) {
				PManager.getDefault().statusMgr
				.setStatus(
						"Experiment file loaded has some modules which are not active in this session, can't save",
						StatusSeverity.ERROR);
				return false;
			}
			final String[] params_list = exp.getExpParametersList();
			final String[] data = ModulesManager.getDefault().getFileData();
			final String[] code_names = ModulesManager.getDefault()
			.getCodeNames();
			boolean override_rat = false;

			Rat rat_tmp = this.exp.getGroupByName(getCurrGrpName())
			.getRatByNumber(getCurrRatNumber());
			if (rat_tmp == null)
				// Create a new rat object, and set its params
				rat_tmp = new Rat(params_list);
			else
				override_rat = true;

			// Fill the rate object with the values of params
			for (int i = 0; i < params_list.length; i++)
				rat_tmp.setValueByParameterName(params_list[i],
						data[getIndexByStringValue(code_names, params_list[i])]);

			if (!override_rat)
				this.exp.getGroupByName(getCurrGrpName()).addRat(rat_tmp);
			saveExperimentToFile(exp.fileName);
			return true;
		}
		return false;
	}

	/**
	 * Sets the active group and rat number.
	 * 
	 * @param rat_num
	 *            rat number to be active and save exp_module_exp. info to
	 * @param grp_name
	 *            group name to be active and save the active rat to
	 * @return 0: success, -1: group doesn't exist
	 */
	public int setCurrentRatAndGroup(final int rat_num, final String grp_name) {
		final Group tmp_grp = exp.getGroupByName(grp_name);
		if (tmp_grp != null) { // Group exists
			setCurrRatNumber(rat_num);
			setCurrGrpName(grp_name);
			experiment_configs.setCurrGrpName(currGrpName);
			experiment_configs.setCurrRatNumber(currRatNumber);
			return 0;
		} else
			return -1;
	}

	private void setCurrGrpName(final String currGrpName) {
		this.currGrpName = currGrpName;
	}

	private void setCurrRatNumber(final int currRatNumber) {
		this.currRatNumber = currRatNumber;
	}

	public void setExperimantLoadedInGUI(final boolean b) {
		PManager.main_gui.setExperimantLoaded(b);
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
	 * @param type
	 */
	public void setExpInfo(final String name, final String user,
			final String date, final String notes, final String type) {
		if (exp == null)
			exp = new Experiment();
		exp.setExperimentInfo(name, user, date, notes, type);
		exp_is_set = true;
		setExperimantLoadedInGUI(true);

		ModulesManager.getDefault().setupModulesAndFilters(exp);
	}

	/**
	 * Clears and unloads the active experiment.
	 */
	public void unloadExperiment() {
		if (exp != null)
			exp.clearExperimentData();
		excel_engine.reset();
		exp_is_set = false;
		setExperimantLoadedInGUI(false);
	}

	/**
	 * Saves group information, if the group exists, it is updated; else, a new
	 * group is created.
	 * 
	 * @param grp_id
	 *            id of the group to edit
	 * @param name
	 *            new name of the group
	 * @param notes
	 *            notes on the group
	 */
	public void updateGrpInfo(final int grp_id, final String name,
			final String notes) {
		final Group tmp_grp = exp.getGroupByID(grp_id);
		if (tmp_grp == null) {
			final Group gp = new Group(grp_id, name, notes);
			exp.addGroup(gp);
		} else
			// group is already existing ... edit it..
		{
			if(name.equals(DELETED_GROUP_NAME))
				exp.getGroups().remove(tmp_grp);
			else{
				tmp_grp.setName(name);
				tmp_grp.setNotes(notes);
			}
		}
	}

	public Grp2GUI[] getExpGroupsInfo(){
		return exp.getGroups().toArray(new Grp2GUI[0]);
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
	public int validateRatAndGroup(final int rat_num, final String grp_name) {
		final Group tmp_grp = exp.getGroupByName(grp_name);
		if (tmp_grp != null) { // Group exists
			if (tmp_grp.getRatByNumber(rat_num) == null)
				return 0;
			return 1;
		} else
			return -1;
	}

	/**
	 * Saves the experiment to an Excel file.
	 * 
	 * @param FilePath
	 *            FilePath file path to write to
	 */
	public void writeToExcelFile(final String FilePath) {
		excel_engine.writeExpInfoToExcelFile(FilePath, exp);
	}
}
