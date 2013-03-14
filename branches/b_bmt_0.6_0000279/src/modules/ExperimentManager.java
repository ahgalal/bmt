package modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import filters.FiltersSetup;

import modules.experiment.ExcelEngine;
import modules.experiment.Exp2GUI;
import modules.experiment.Experiment;
import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleConfigs;
import modules.experiment.ForcedSwimmingExperiment;
import modules.experiment.Group;
import modules.experiment.Grp2GUI;
import modules.experiment.OpenFieldExperiment;
import modules.experiment.Rat;
import modules.experiment.TextEngine;
import modules.experiment.forcedswimming.ForcedSwimmingExperimentModule;
import modules.experiment.openfield.OpenFieldExperimentModule;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

public class ExperimentManager {
	public static final String			DELETED_GROUP_NAME	= "-";
	private static ExperimentManager	me;

	public static ExperimentManager getDefault() {
		if (me == null)
			me = new ExperimentManager();
		return me;
	}

	/**
	 * Loads an experiment from a text file to an experiment object.
	 * 
	 * @param fileName
	 *            file name to load the experiment from
	 */
	public static Experiment readExperimentFromFile(final String fileName) {
		ObjectInputStream ois;
		Experiment exp = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(fileName)));
			exp = (Experiment) ois.readObject();
			exp.setFileName(fileName);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			System.err
					.println("Experiment loading error, Experiment file may be corrupted");
		} catch (final ClassNotFoundException e) {
			System.err.println("incompatible experiment file!");
		}
		return exp;
	}

	private String					currGrpName;
	private int						currRatNumber;
	private ExcelEngine				excelEngine;
	private Experiment				exp;

	private ExperimentModuleConfigs	experimentConfigs;

	private ExperimentModule		experimentModule;

	private boolean					expLoaded;

	private TextEngine				textEngine;

	private ExperimentManager() {
		setTextEngine(new TextEngine());
		setExcelEngine(new ExcelEngine());
	}

	public void addExpParams(final ArrayList<String> arrayList) {
		final ArrayList<String> allParams = new ArrayList<String>();
		if (exp.getExpParametersList() != null)
			for (final String param : exp.getExpParametersList())
				allParams.add(param);

		outer: for (final String param : arrayList) {
			for (final String str : allParams) {
				if (param.equals(str)) {
					continue outer;
				}
			}
			allParams.add(param);
		}

		//exp.setParametersList(allParams.toArray(new String[0]));
	}

	public String getCurrGrpName() {
		return currGrpName;
	}

	public int getCurrRatNumber() {
		return currRatNumber;
	}

	public ExcelEngine getExcelEngine() {
		return excelEngine;
	}

	public Exp2GUI getExpBasicInfo() {
		return exp;
	}

	public Grp2GUI[] getExpGroupsInfo() {
		return exp.getGroups().toArray(new Grp2GUI[0]);
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

	public TextEngine getTextEngine() {
		return textEngine;
	}

	public ExperimentModule instantiateExperimentModule() {

		switch (exp.getType()) {
			case OPEN_FIELD:
				experimentConfigs = new ExperimentModuleConfigs(
						OpenFieldExperimentModule.moduleID, exp);
				experimentModule = new OpenFieldExperimentModule(
						experimentConfigs);
				break;
			case FORCED_SWIMMING:
				experimentConfigs = new ExperimentModuleConfigs(
						ForcedSwimmingExperimentModule.moduleID, exp);
				experimentModule = new ForcedSwimmingExperimentModule(
						experimentConfigs);
				break;
			default:
				break;
		}

		return experimentModule;
	}

	/**
	 * is there an active experiment?
	 * 
	 * @return true/false
	 */
	public boolean isExperimentPresent() {
		return isExpLoaded();
	}

	public boolean isExpLoaded() {
		return expLoaded;
	}

	public void loadExperiment(final Experiment experiment) {
		if (experiment != null) {
			setExpLoaded(true);
			this.exp = experiment;
			PManager.getDefault().getVideoManager().setupModulesAndFilters(exp);
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
			exp.setFileName(FilePath);
			oos.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves the rat's info to the experiment object.
	 * 
	 * @return true: success
	 */
	public boolean saveRatInfo() {
		if (isExperimentPresent()) {
			if (exp.getExpParametersList() == null){
				throw new NullPointerException();
				/*exp.setParametersList(ModulesManager.getDefault()
						.getCodeNames());*/
			}
			else if (getNumberOfExpParams() != ModulesManager.getDefault()
					.getCodeNames().length) {
				PManager.getDefault()
						.getStatusMgr()
						.setStatus(
								"Experiment file loaded has some modules which are not active in this session, can't save",
								StatusSeverity.ERROR);
				return false;
			}
			final String[] paramsList = exp.getExpParametersList();
			final String[] data = ModulesManager.getDefault().getFileData();
			final String[] codeNames = ModulesManager.getDefault()
					.getCodeNames();
			boolean overrideRat = false;

			Rat ratTmp = this.exp.getGroupByName(getCurrGrpName())
					.getRatByNumber(getCurrRatNumber());
			if (ratTmp == null)
				// Create a new rat object, and set its params
				ratTmp = new Rat(paramsList);
			else
				overrideRat = true;

			// Fill the rate object with the values of params
			for (int i = 0; i < paramsList.length; i++)
				ratTmp.setValueByParameterName(paramsList[i],
						data[getIndexByStringValue(codeNames, paramsList[i])]);

			if (!overrideRat)
				this.exp.getGroupByName(getCurrGrpName()).addRat(ratTmp);
			saveExperimentToFile(exp.getFileName());
			return true;
		}
		return false;
	}

	/**
	 * Sets the active group and rat number.
	 * 
	 * @param ratNum
	 *            rat number to be active and save exp_module_exp. info to
	 * @param grpName
	 *            group name to be active and save the active rat to
	 * @return 0: success, -1: group doesn't exist
	 */
	public int setCurrentRatAndGroup(final int ratNum, final String grpName) {
		final Group tmpGrp = exp.getGroupByName(grpName);
		if (tmpGrp != null) { // Group exists
			setCurrRatNumber(ratNum);
			setCurrGrpName(grpName);
			experimentConfigs.setCurrGrpName(currGrpName);
			experimentConfigs.setCurrRatNumber(currRatNumber);
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

	public void setExcelEngine(final ExcelEngine excelEngine) {
		this.excelEngine = excelEngine;
	}

	public void setExperimantLoadedInGUI(final boolean b) {
		PManager.mainGUI.setExperimantLoaded(b);
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
			if(type.equals("Open Field")){
				exp = new OpenFieldExperiment();
			}else if(type.equals("Forced Swimming")){
				exp = new ForcedSwimmingExperiment();
			}else{
				throw new RuntimeException("Unknown Experiment type");
			}
		exp.setExperimentInfo(name, user, date, notes, type);
		setExpLoaded(true);
		setExperimantLoadedInGUI(true);

		PManager.getDefault().getVideoManager().setupModulesAndFilters(exp);
	}

	public void setExpLoaded(final boolean expLoaded) {
		this.expLoaded = expLoaded;
	}

	public void setTextEngine(final TextEngine textEngine) {
		this.textEngine = textEngine;
	}

	/**
	 * Clears and unloads the active experiment.
	 */
	public void unloadExperiment() {
		if (exp != null)
			exp.clearExperimentData();
		getExcelEngine().reset();
		setExpLoaded(false);
		setExperimantLoadedInGUI(false);
	}

	/**
	 * Saves group information, if the group exists, it is updated; else, a new
	 * group is created.
	 * 
	 * @param grpId
	 *            id of the group to edit
	 * @param name
	 *            new name of the group
	 * @param notes
	 *            notes on the group
	 */
	public void updateGrpInfo(final int grpId, final String name,
			final String notes) {
		final Group tmpGrp = exp.getGroupByID(grpId);
		if (tmpGrp == null) {
			final Group gp = new Group(grpId, name, notes);
			exp.addGroup(gp);
		} else
		// group is already existing ... edit it..
		{
			if (name.equals(DELETED_GROUP_NAME))
				exp.getGroups().remove(tmpGrp);
			else {
				tmpGrp.setName(name);
				tmpGrp.setNotes(notes);
			}
		}
	}

	/**
	 * Checks that the group already exists, and if the rat already exists.
	 * 
	 * @param ratNum
	 *            rat number to check its existence
	 * @param grpName
	 *            group name to check its existence
	 * @return integer: 0: Group exists, rat doesn't exist (it's a new rat);
	 *         1:Group exists, rat also exists; -1: Group doesn't exist
	 */
	public int validateRatAndGroup(final int ratNum, final String grpName) {
		final Group tmpGrp = exp.getGroupByName(grpName);
		if (tmpGrp != null) { // Group exists
			if (tmpGrp.getRatByNumber(ratNum) == null)
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
		getExcelEngine().writeExpInfoToExcelFile(FilePath, exp);
	}

	public FiltersSetup getFilterSetup() {
		return exp.getFiltersSetup();
	}

	public void signalFiltersSetupChange() {
		loadExperiment(exp);
	}
}
