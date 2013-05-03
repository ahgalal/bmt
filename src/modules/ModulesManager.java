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

package modules;

import java.util.ArrayList;
import java.util.Iterator;

import modules.ModulesNamesRequirements.ModuleRequirement;
import modules.experiment.Experiment;
import modules.experiment.ExperimentModule;
import modules.movementmeter.MovementMeterModule;
import modules.movementmeter.MovementMeterModuleConfigs;
import modules.rearing.RearingModule;
import modules.rearing.RearingModuleConfigs;
import modules.session.SessionModule;
import modules.session.SessionModuleConfigs;
import modules.zones.ZonesModule;
import modules.zones.ZonesModuleConfigs;

import org.eclipse.swt.widgets.Shell;

import sys.utils.Utils;
import ui.PluggedGUI;
import utils.ConfigurationManager;
import utils.Logger.Details;
import utils.PManager;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;
import utils.video.ConfigsListener;
import filters.CommonConfigs;
import filters.Data;

@SuppressWarnings("rawtypes")
/**
 * Manager for all modules.
 * 
 * @author Creative
 */
public class ModulesManager implements ConfigsListener {

	/**
	 * Runnable for running Modules.
	 * 
	 * @author Creative
	 */
	private class RunnableModulesThread implements Runnable {
		@Override
		public void run() {
			while (runModules) {
				doneProcessing = false;
				Utils.sleep(33);
				for (final Module mo : modules)
					mo.process();

				synchronized (this) {
					while (paused) {
						try {
							this.wait();
						} catch (final InterruptedException e) {
						}
					}
				}
			}
			doneProcessing = true;
		}
	}

	private static ModulesManager me;
	/**
	 * Get the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static ModulesManager getDefault() {
		return me;
	}

	private final ConfigurationManager<ModuleConfigs> configurationManager;

	private boolean doneProcessing = false;
	private final ArrayList<Cargo> fileCargos = new ArrayList<Cargo>();

	private String[] fileDataArray;

	private String[] fileNamesArray;
	private final ArrayList<Data> filtersData;
	private final ArrayList<Cargo> guiCargos = new ArrayList<Cargo>();

	private String[] guiDataArray;
	private String[] guiNamesArray;

	private final ModulesCollection installedModules;
	private final ArrayList<Module> modules;
	private final ArrayList<ModuleData> modulesData;
	private boolean paused;

	private boolean runModules;

	private RunnableModulesThread runnableModules;

	private Thread thModules;

	/**
	 * Initializes modules.
	 */
	public ModulesManager() {
		me = this;
		filtersData = new ArrayList<Data>();
		modulesData = new ArrayList<ModuleData>();
		modules = new ArrayList<Module>();
		
		configurationManager = new ConfigurationManager<ModuleConfigs>(modules);

		installedModules = new ModulesCollection();
		installedModules.addModule(new RearingModule(null, null));
		installedModules.addModule(new MovementMeterModule(null, null));
		installedModules.addModule(new SessionModule(null, null));
		installedModules.addModule(new ZonesModule(null, null));

		initializeConfigs();
	}
	/**
	 * Passes incoming data object to all modules, only modules interested in
	 * that data object, will accept it.
	 * 
	 * @param data
	 *            incoming data object from a video filter
	 */
	public void addFilterDataObject(final Data data) {
		filtersData.add(data);
		for (final Module module : modules)
			module.registerFilterDataObject(data);
	}

	public boolean allowTracking() {
		boolean ret = false;
		for (final Module m : modules) {
			ret = true;
			if (m.allowTracking() == false) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	/**
	 * Applies a configuration object to a module, using the name of the module
	 * specified in the configuration object.</br>Also adds the module
	 * configuration to the list if it doesn't exist.
	 * 
	 * @param cfgs
	 *            configurations object
	 */
	public void applyConfigsToModule(final ModuleConfigs cfgs) {
		configurationManager.applyConfigs(cfgs);
	}

	/**
	 * Checks if all the modules are ready to run/process data.
	 * 
	 * @param shell
	 *            parent shell for displaying messageboxes
	 * @return true: all are ready, false: some modules are not ready
	 */
	public boolean areModulesReady(final Shell shell) {
		for (final Module mo : modules)
			if (!mo.amIReady(shell)) {
				PManager.log.print(mo.getID() + " cancelled Tracking!", this,
						StatusSeverity.ERROR);
				return false;
			}
		return true;
	}

	private void connectModules() {
		PManager.log.print("registering modules data with each others", this,
				Details.VERBOSE);
		for (final Module mo : modules)
			modulesData.add(mo.getModuleData());

		for (final Module mo : modules)
			for (final ModuleData data : modulesData)
				mo.registerModuleDataObject(data);
		PManager.log.print(
				"finished registering modules data with each others", this,
				Details.VERBOSE);

	}

	/**
	 * Builds the Cargo array based on the number of instantiated modules.
	 */
	private void constructCargoArray() {
		int numStrs = 0;
		guiCargos.clear();
		fileCargos.clear();

		Cargo tmp;
		for (final Module m : modules) {
			tmp = m.getGUICargo();
			if (tmp != null) {
				guiCargos.add(tmp);
				numStrs += tmp.getData().length;
			}
		}

		guiDataArray = new String[numStrs];
		guiNamesArray = new String[numStrs];

		numStrs = 0;
		for (final Module m : modules) {
			tmp = m.getFileCargo();
			if (tmp != null) {
				fileCargos.add(tmp);
				numStrs += tmp.getData().length;
			}
		}

		fileDataArray = new String[numStrs];
		fileNamesArray = new String[numStrs];

		int i = 0;
		for (final Cargo cargo : guiCargos) {
			final String[] tmpCargoNames = cargo.getTags();
			for (int j = 0; j < tmpCargoNames.length; j++) {
				guiNamesArray[i] = tmpCargoNames[j];
				i++;
			}
		}

		i = 0;
		for (final Cargo cargo : fileCargos) {
			final String[] tmpCargoNames = cargo.getTags();
			for (int j = 0; j < tmpCargoNames.length; j++) {
				fileNamesArray[i] = tmpCargoNames[j];
				i++;
			}
		}
	}

	private Module createModule(final String name, final String ID) {
		Module module = null;
		for (final Iterator<Module<?, ?, ?>> it = installedModules.getModules(); it
				.hasNext();) {
			final Module tmpModule = it.next();

			if (tmpModule.getID().equals(ID)) {
				module = tmpModule.newInstance(name);
				break;
			}
		}
		return module;
	}

	/**
	 * Gets list of parameters (columns names) to be sent to file writer.
	 * 
	 * @return String array containing parameters (columns names) to be sent to
	 *         file writer
	 */
	public String[] getExperimentParams() {
		constructCargoArray();
		return fileNamesArray;
	}

	/**
	 * Gets the information to be sent to file writer.
	 * 
	 * @return String array containing data to be sent to file writer
	 */
	public String[] getFileData() {
		for (final Module mo : modules)
			mo.updateFileCargoData();

		int i = 0;
		for (final Cargo cargo : fileCargos) {
			final String[] tmpCargoData = cargo.getData();
			for (int j = 0; j < tmpCargoData.length; j++) {
				fileDataArray[i] = tmpCargoData[j];
				i++;
			}
		}
		return fileDataArray;
	}

	/**
	 * Gets the information to be sent to GUI.
	 * 
	 * @return String array containing data to be sent to GUI
	 */
	public String[] getGUIData() {
		for (final Module mo : modules)
			mo.updateGUICargoData();

		int i = 0;
		for (final Cargo cargo : guiCargos) {
			final String[] tmpCargoData = cargo.getData();
			
			for (int j = 0; j < tmpCargoData.length; j++) {
				guiDataArray[i] = tmpCargoData[j];
				i++;
			}
		}
		return guiDataArray;
	}

	/**
	 * Gets list of parameters (columns names) to be sent to GUI.
	 * 
	 * @return String array containing parameters (columns names) to be sent to
	 *         GUI
	 */
	public String[] getGUINames() {
		return guiNamesArray;
	}

	/**
	 * Gets a module using the module's name.
	 * 
	 * @param id
	 *            id of the module to return
	 * @return module instance having the name specified
	 */
	public Module getModuleByID(final String id) {
		for (final Module mo : modules)
			if (mo.getID().equals(id))
				return mo;
		return null;
	}

	public PluggedGUI[] getModulesGUI() {
		int validGUIsNumber = 0;
		for (final Module module : modules)
			if (module.getGUI() != null)
				validGUIsNumber++;
		final PluggedGUI[] arr = new PluggedGUI[validGUIsNumber];
		int i = 0;
		for (final Module module : modules)
			if (module.getGUI() != null) {
				arr[i] = module.getGUI();
				i++;
			}
		return arr;
	}

	/**
	 * Gets the names of active modules.
	 * 
	 * @return ArrayList containing the names of active modules
	 */
	public ArrayList<String> getModulesIDs() {
		final ArrayList<String> tmpArr = new ArrayList<String>();
		for (final Module m : modules)
			tmpArr.add(m.getID());
		return tmpArr;
	}

	public ArrayList<Module<?, ?, ?>> getModulesUnderID(final String id) {
		final ArrayList<Module<?, ?, ?>> ret = new ArrayList<Module<?, ?, ?>>();
		for (final Module mo : modules)
			if (mo.getID().startsWith(id))
				ret.add(mo);
		return ret;
	}

	/**
	 * Gets the number of Experiment parameters to be stored in file.
	 * 
	 * @return integer representing the number of file parameters for the active
	 *         experiment
	 */
	public int getNumberOfFileParameters() {
		constructCargoArray();
		for (final String fileCargoTag : fileNamesArray) {
			PManager.log
					.print("File Tag: " + fileCargoTag, this, Details.NOTES);
		}
		return fileNamesArray.length;
	}

	/**
	 * Initializes modules.
	 */
	public void initialize() {
		filtersData.clear();
		for (final Module mo : modules)
			mo.initialize();

		constructCargoArray();

		runnableModules = new RunnableModulesThread();
	}

	public void initializeConfigs() {

		final ModuleConfigs rearingConfigs = new RearingModuleConfigs(
				"rearingConfigs");
		final ModuleConfigs movementConfigs = new MovementMeterModuleConfigs(
				"movementConfigs");
		final ModuleConfigs sessionConfigs = new SessionModuleConfigs(
				"sessionConfigs");
		final ModuleConfigs zoneConfigs = new ZonesModuleConfigs("zoneConfigs",
				ZonesModule.DEFAULT_HYSTRISES_VALUE, 0, 0);

		configurationManager.reset();
		configurationManager.addConfiguration(rearingConfigs, false);
		configurationManager.addConfiguration(movementConfigs, false);
		configurationManager.addConfiguration(sessionConfigs, false);
		configurationManager.addConfiguration(zoneConfigs, false);
	}

	/**
     * 
     */
	private void loadModulesGUI() {
		PManager.log.print("loading Modules GUI..", this, Details.VERBOSE);
		PManager.mainGUI.loadPluggedGUI(getModulesGUI());
	}

	public void pauseModules() {
		for (final Module mod : modules)
			mod.pause();
		paused = true;
	}

	/**
	 * Removes a data object (deRegisters it) from all modules registered with
	 * it.
	 * 
	 * @param data
	 *            data object of a video filter
	 */
	public void removeDataObject(final Data data) {
		filtersData.remove(data);
		for (final Module mo : modules)
			mo.deRegisterDataObject(data);
	}

	public void resumeModules() {
		paused = false;
		if (thModules != null)
			thModules.interrupt();
		for (final Module mod : modules)
			mod.resume();
	}

	/**
	 * Starts/Stops running all modules.
	 * 
	 * @param run
	 *            true/false to start/stop
	 */
	public void runModules(final boolean run) {
		if (run & !runModules) {
			runModules = true;
			thModules = new Thread(runnableModules, "Modules process");
			thModules.start();
		} else if (!run & runModules) {
			runModules = false;

			// if paused, we need to resume to unlock the paused thread
			if (PManager.getDefault().getState().getStream() == StreamState.PAUSED)
				resumeModules();

			try {
				Thread.sleep(33);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			thModules = null;

			while (doneProcessing == false)
				Utils.sleep(50);

			for (final Module mo : modules)
				mo.deInitialize();
			ExperimentManager.getDefault().saveRatInfo();
		}
	}

	public boolean setupModules(final Experiment exp) {
		// unload old modules
		unloadModules();

		// remove old modules
		modules.clear();
		modulesData.clear();

		final ModulesSetup modulesSetup = exp.getModulesSetup();
		for (final Iterator<ModuleRequirement> it = modulesSetup
				.getModulesRequirements().getModuleRequirements(); it.hasNext();) {
			final ModuleRequirement moduleRequirement = it.next();

			Module module = createModule(moduleRequirement.getName(),
					moduleRequirement.getID());
			if(module==null)
				throw new RuntimeException("Could not find the required module: " + moduleRequirement.getName()+" : " + moduleRequirement.getID());
			modules.add(module);
		}

		// set modules' configurations to the default values
		for (final Module module : modules) {
			ModuleConfigs moduleConfig = configurationManager
					.getConfigByName(module.getName());
			if (moduleConfig == null) {
				// apply default configs to the module and display a warning
				moduleConfig = configurationManager.createDefaultConfigs(
						module.getName(), module.getID());
				configurationManager.addConfiguration(moduleConfig, false);
				PManager.log.print("Default Configs is applied to module: "
						+ module.getName(), this, StatusSeverity.WARNING);
			}

			configurationManager.applyConfigs(moduleConfig);
		}

		// ////////////////////////////////
		// Experiment Module
		final ExperimentModule expModule = ExperimentManager.getDefault()
				.instantiateExperimentModule();
		modules.add(expModule);
		configurationManager.addConfiguration(ExperimentManager.getDefault().getExperimentConfigs(), true);

		connectModules();
		loadModulesGUI();
		
		for (final Module m : modules)
			m.filterConfiguration();
		
		return true;
	}
	public void unloadModules() {
		for (final Module m : modules)
			m.unload();
	}

	@Override
	public void updateConfigs(CommonConfigs commonConfigs) {
		
		// loop on modules
		for(Iterator<Module> it = modules.iterator();it.hasNext();){
			Module module=it.next();
			
			ModuleConfigs configs = configurationManager.getConfigByName(module.getName());
			
			// update common configs
			configs.getCommonConfigs().merge(commonConfigs);
			
			// re-apply configs
			configurationManager.applyConfigs(configs);
		}
		
		
		
/*		// loop on all configurations available
		for(Iterator<ModuleConfigs> it=configurationManager.getConfigurations();it.hasNext();){
			ModuleConfigs config = it.next();
			// update common configs
			config.commonConfigs.merge(commonConfigs);
			
			// re-apply configs
			configurationManager.applyConfigs(config);
		}*/
	}

}
