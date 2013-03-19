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
import utils.Logger.Details;
import utils.PManager;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;
import filters.Data;

@SuppressWarnings("rawtypes")
/**
 * Manager for all modules.
 * 
 * @author Creative
 */
public class ModulesManager {
	
	private boolean doneProcessing=false;
	
	/**
	 * Runnable for running Modules.
	 * 
	 * @author Creative
	 */
	private class RunnableModulesThread implements Runnable {
		@Override
		public void run() {
			while (runModules) {
				doneProcessing=false;
				Utils.sleep(33);
				for (final Module mo : modules)
					mo.process();
				
				synchronized (this) {
					while (paused) {
						try {
							this.wait();
						} catch (InterruptedException e) {
						}
					}	
				}
			}
			doneProcessing=true;
		}
	}

	private static ModulesManager	me;

	/**
	 * Get the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static ModulesManager getDefault() {
		return me;
	}

	private final ArrayList<Cargo>	fileCargos	= new ArrayList<Cargo>();
	private String[]				fileDataArray;

	private String[]				fileNamesArray;

	private final ArrayList<Data>	filtersData;
	private final ArrayList<Cargo>	guiCargos	= new ArrayList<Cargo>();
	private String[]				guiDataArray;

	private String[]				guiNamesArray;
	private int						height;

	private final ArrayList<Module>	modules;
	private final ArrayList<ModuleData>	modulesData;
	private boolean					runModules;
	private RunnableModulesThread	runnableModules;

	private Thread					thModules;

	private int						width;

	/**
	 * Initializes modules.
	 */
	public ModulesManager() {
		me = this;
		filtersData = new ArrayList<Data>();
		modulesData = new ArrayList<ModuleData>();
		modules = new ArrayList<Module>();
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
			if (cargo == null)
				System.out.println();
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
	
	public ArrayList<Module<?, ?, ?>> getModulesUnderID(final String id){
		ArrayList<Module<?, ?, ?>> ret=new ArrayList<Module<?, ?, ?>>();
		for (final Module mo : modules)
			if (mo.getID().startsWith(id))
				ret.add(mo);
		return ret;
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

	/**
	 * Gets the number of Experiment parameters to be stored in file.
	 * 
	 * @return integer representing the number of file parameters for the active
	 *         experiment
	 */
	public int getNumberOfFileParameters() {
		constructCargoArray();
		for(String fileCargoTag:fileNamesArray){
			PManager.log.print("File Tag: "+ fileCargoTag, this, Details.NOTES);
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

	/**
     * 
     */

	private void instantiateModules(final String[] moduleNames) {
		PManager.log.print("instantiating Modules", this, Details.VERBOSE);
		// ////////////////////////////////
		// Rearing Module
		if (isWithinArray(RearingModule.moduleID, moduleNames)) {
			final RearingModuleConfigs rearingConfigs = new RearingModuleConfigs(
					RearingModule.moduleID);
			final RearingModule rearingModule = new RearingModule(
					rearingConfigs);
			modules.add(rearingModule);
		}
		// ////////////////////////////////
		// Zones Module
		if (isWithinArray(ZonesModule.moduleID, moduleNames)) {
			
			final ZonesModuleConfigs zonesConfigs = new ZonesModuleConfigs(
					ZonesModule.moduleID, ZonesModule.DEFAULT_HYSTRISES_VALUE,
					width, height);
			final ZonesModule zonesModule = new ZonesModule(
					zonesConfigs);
			modules.add(zonesModule);
		}
		// ////////////////////////////////
		// Session Module
		if (isWithinArray(SessionModule.moduleID, moduleNames)) {
			final SessionModuleConfigs sessionConfigs = new SessionModuleConfigs(
					SessionModule.moduleID);
			final SessionModule sessionModule = new SessionModule(
					 sessionConfigs);
			modules.add(sessionModule);
		}
		// ////////////////////////////////
		// MovementMeter Module
		if (isWithinArray(MovementMeterModule.moduleID, moduleNames)) {
			final MovementMeterModuleConfigs movementModuleConfigs = new MovementMeterModuleConfigs(
					MovementMeterModule.moduleID);
			final MovementMeterModule movementMeterModule = new MovementMeterModule(
					movementModuleConfigs);
			modules.add(movementMeterModule);
		}
		PManager.log.print("finished instantiating Modules", this,
				Details.VERBOSE);
	}

	private boolean isWithinArray(final String name, final String[] array) {
		for (final String str : array)
			if (str.equals(name))
				return true;
		return false;
	}

	/**
     * 
     */
	private void loadModulesGUI() {
		PManager.log.print("loading Modules GUI..", this, Details.VERBOSE);
		PManager.mainGUI.loadPluggedGUI(getModulesGUI());
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

	/**
	 * Starts/Stops running all modules.
	 * 
	 * @param run
	 *            true/false to start/stop
	 */
	public void runModules(final boolean run) {
		if (run & !runModules) {
			runModules = true;
			thModules = new Thread(runnableModules,"Modules process");
			thModules.start();
		} else if (!run & runModules) {
			runModules = false;
			
			// if paused, we need to resume to unlock the paused thread
			if(PManager.getDefault().getState().getStream()==StreamState.PAUSED)
				resumeModules();
			
			try {
				Thread.sleep(33);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			thModules = null;
			
			while(doneProcessing==false)
				Utils.sleep(50);

			for (final Module mo : modules)
				mo.deInitialize();
			ExperimentManager.getDefault().saveRatInfo();
		}
	}
	private boolean paused;
	public void pauseModules(){
		for(Module mod:modules)
			mod.pause();
		paused=true;
	}

	public void resumeModules(){
		paused=false;
		if(thModules!=null)
			thModules.interrupt();
		for(Module mod:modules)
			mod.resume();
	}

	/**
	 * Sets the width and height of the module manager, to be used by any module
	 * later.
	 * 
	 * @param width
	 *            webcam image's width
	 * @param height
	 *            webcam image's height
	 */
	public void setModulesWidthandHeight(final int width, final int height) {
		this.width = width;
		this.height = height;

		updateModuleConfigs(new ModuleConfigs[] { new ZonesModuleConfigs(
				ZonesModule.moduleID, -1, width, height) });
	}

	public void setupModules(final Experiment exp) {
		// unload old modules
		for(Module m:modules)
			m.unload();
		
		// remove old modules
		modules.clear();
		modulesData.clear();
		
		// ////////////////////////////////
		// Experiment Module
		final ExperimentModule expModule = ExperimentManager.getDefault()
				.instantiateExperimentModule();
		modules.add(expModule);
		
		final ModulesSet openFieldModulesSetup = new ModulesSet(new String[] {
				RearingModule.moduleID,ZonesModule.moduleID, SessionModule.moduleID });

		final ModulesSet forcedSwimmingModulesSetup = new ModulesSet(
				new String[] { SessionModule.moduleID, MovementMeterModule.moduleID });

		switch (exp.getType()) {
			case FORCED_SWIMMING:
				instantiateModules(forcedSwimmingModulesSetup.getModulesNames());
				break;
			case OPEN_FIELD:
				instantiateModules(openFieldModulesSetup.getModulesNames());
				break;
		}
		// setWidthandHeight(640, 480);
		connectModules();
		loadModulesGUI();
	}

	/**
	 * Updates the configurations of the modules.
	 * 
	 * @param configs
	 *            array of ModuleConfigs, containing configurations objects for
	 *            the modules. each module will be configured according to its
	 *            configurations object in the array.
	 */
	public void updateModuleConfigs(final ModuleConfigs[] configs) {
		for (int i = 0; i < configs.length; i++) {
			final Module tmp = getModuleByID(configs[i].getModuleID());
			if (tmp != null)
				tmp.updateConfigs(configs[i]);
		}
	}

}
