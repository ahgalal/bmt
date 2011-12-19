package modules;

import modules.experiment.ExcelEngine;
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
    public TextEngine text_engine;
    public ExcelEngine excel_engine;
    public boolean exp_is_set;
    private static ExperimentManager me;
    private Experiment exp;
    private int currRatNumber;
    private String currGrpName;

    private ExperimentManager() {
	text_engine = new TextEngine();
	excel_engine = new ExcelEngine();
    }

    public static ExperimentManager getDefault() {
	if (me == null)
	    me = new ExperimentManager();
	return me;
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
     * Sets the file name to save the experiment to.
     * 
     * @param f_name
     *            file name to save the experiment to
     */
    public void setExpFileName(final String f_name) {
	exp.fileName = f_name;
    }

    public void setExperimantLoadedInGUI(final boolean b) {
	PManager.main_gui.setExperimantLoaded(b);
    }

    private ExperimentModuleConfigs experiment_configs;
    private ExperimentModule experiment_module;

    public ExperimentModule instantiateExperimentModule() {
	experiment_configs = new ExperimentModuleConfigs("Experiment Module",
		exp);
	
	switch (exp.type) {
	case OPEN_FIELD:
	    experiment_module = new OpenFieldExperimentModule("Experiment Module",
			experiment_configs);
	    break;
	case FORCED_SWIMMING:
	    experiment_module = new ForcedSwimmingExperimentModule("Experiment Module",
			experiment_configs);
	    break;
	default:
	    break;
	}
	
	return experiment_module;
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
	    tmp_grp.setName(name);
	    tmp_grp.setNotes(notes);
	}
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
		rat_tmp = new Rat(params_list);
	    else
		override_rat = true;

	    for (int i = 0; i < params_list.length; i++)
		rat_tmp.setValueByParameterName(params_list[i],
			data[getIndexByStringValue(code_names, params_list[i])]);

	    if (!override_rat)
		this.exp.getGroupByName(getCurrGrpName()).addRat(rat_tmp);
	    writeToTXTFile(exp.fileName);
	    return true;
	}
	return false;
    }

    /**
     * Loads an experiment from a text file to an experiment object.
     * 
     * @param file_name
     *            file name to load the experiment from
     */
    public void loadInfoFromTXTFile(final String file_name) {
	exp = new Experiment();
	if (ExperimentManager.getDefault().text_engine.readExpInfoFromTXTFile(
		file_name, exp)) {
	    PManager.getDefault().frm_exp.fillForm(exp);
	    exp_is_set = true;
	    ModulesManager.getDefault().setupModulesAndFilters(exp);
	    setExperimantLoadedInGUI(true);
	}
    }

    /**
     * Updates the Groups GUI window with the latest groups information.
     */
    public void updateGroupGUIData() {
	PManager.getDefault().frm_grps.clearForm();
	if (exp != null) {
	    final Grp2GUI[] arr_grps = new Grp2GUI[exp.getNoGroups()];
	    exp.getGroups().toArray(arr_grps);
	    PManager.getDefault().frm_grps.loadDataToForm(arr_grps);
	}
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

    /**
     * Writes the experiment to a text file.
     * 
     * @param FilePath
     *            file path to write to
     */
    public void writeToTXTFile(final String FilePath) {
	text_engine.writeExpInfoToTXTFile(FilePath, exp);
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

    private void setCurrGrpName(final String currGrpName) {
	this.currGrpName = currGrpName;
    }

    public String getCurrGrpName() {
	return currGrpName;
    }

    private void setCurrRatNumber(final int currRatNumber) {
	this.currRatNumber = currRatNumber;
    }

    public int getCurrRatNumber() {
	return currRatNumber;
    }
}
