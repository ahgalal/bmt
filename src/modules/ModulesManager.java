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

import ui.PluggedGUI;
import utils.Logger.Details;
import utils.PManager;
import utils.PManager.ProgramState.StreamState;
import utils.StatusManager.StatusSeverity;
import utils.Utils;
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
			while (run_modules) {
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

	private final ArrayList<Cargo>	file_cargos	= new ArrayList<Cargo>();
	private String[]				file_data_array;

	private String[]				file_names_array;

	private final ArrayList<Data>	filters_data;
	private final ArrayList<Cargo>	gui_cargos	= new ArrayList<Cargo>();
	private String[]				gui_data_array;

	private String[]				gui_names_array;
	private int						height;

	private final ArrayList<Module>	modules;
	private final ArrayList<ModuleData>	modules_data;
	private boolean					run_modules;
	private RunnableModulesThread	runnable_modules;

	private Thread					th_modules;

	private int						width;

	/**
	 * Initializes modules.
	 */
	public ModulesManager() {
		me = this;
		filters_data = new ArrayList<Data>();
		modules_data = new ArrayList<ModuleData>();
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
		filters_data.add(data);
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
				PManager.log.print(mo.getName() + " cancelled Tracking!", this,
						StatusSeverity.ERROR);
				return false;
			}
		return true;
	}

	private void connectModules() {
		PManager.log.print("registering modules data with each others", this,
				Details.VERBOSE);
		for (final Module mo : modules)
			modules_data.add(mo.getModuleData());

		for (final Module mo : modules)
			for (final ModuleData data : modules_data)
				mo.registerModuleDataObject(data);
		PManager.log.print(
				"finished registering modules data with each others", this,
				Details.VERBOSE);

	}

	/**
	 * Builds the Cargo array based on the number of instantiated modules.
	 */
	private void constructCargoArray() {
		int num_strs = 0;
		gui_cargos.clear();
		file_cargos.clear();

		Cargo tmp;
		for (final Module m : modules) {
			tmp = m.getGUICargo();
			if (tmp != null) {
				gui_cargos.add(tmp);
				num_strs += tmp.getData().length;
			}
		}

		gui_data_array = new String[num_strs];
		gui_names_array = new String[num_strs];

		num_strs = 0;
		for (final Module m : modules) {
			tmp = m.getFileCargo();
			if (tmp != null) {
				file_cargos.add(tmp);
				num_strs += tmp.getData().length;
			}
		}

		file_data_array = new String[num_strs];
		file_names_array = new String[num_strs];

		int i = 0;
		for (final Cargo cargo : gui_cargos) {
			if (cargo == null)
				System.out.println();
			final String[] tmp_cargo_names = cargo.getTags();
			for (int j = 0; j < tmp_cargo_names.length; j++) {
				gui_names_array[i] = tmp_cargo_names[j];
				i++;
			}
		}

		i = 0;
		for (final Cargo cargo : file_cargos) {
			final String[] tmp_cargo_names = cargo.getTags();
			for (int j = 0; j < tmp_cargo_names.length; j++) {
				file_names_array[i] = tmp_cargo_names[j];
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
	public String[] getCodeNames() {
		constructCargoArray();
		return file_names_array;
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
		for (final Cargo cargo : file_cargos) {
			final String[] tmp_cargo_data = cargo.getData();
			for (int j = 0; j < tmp_cargo_data.length; j++) {
				file_data_array[i] = tmp_cargo_data[j];
				i++;
			}
		}
		return file_data_array;
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
		for (final Cargo cargo : gui_cargos) {
			final String[] tmp_cargo_data = cargo.getData();
			for (int j = 0; j < tmp_cargo_data.length; j++) {
				gui_data_array[i] = tmp_cargo_data[j];
				i++;
			}
		}
		return gui_data_array;
	}

	/**
	 * Gets list of parameters (columns names) to be sent to GUI.
	 * 
	 * @return String array containing parameters (columns names) to be sent to
	 *         GUI
	 */
	public String[] getGUINames() {
		return gui_names_array;
	}

	/**
	 * Gets a module using the module's name.
	 * 
	 * @param name
	 *            name of the module to return
	 * @return module instance having the name specified
	 */
	public Module getModuleByName(final String name) {
		for (final Module mo : modules)
			if (mo.getName().equals(name))
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
	public ArrayList<String> getModulesNames() {
		final ArrayList<String> tmp_arr = new ArrayList<String>();
		for (final Module m : modules)
			tmp_arr.add(m.getName());
		return tmp_arr;
	}

	/**
	 * Gets the number of Experiment parameters to be stored in file.
	 * 
	 * @return integer representing the number of file parameters for the active
	 *         experiment
	 */
	public int getNumberOfFileParameters() {
		constructCargoArray();
		for(String fileCargoTag:file_names_array){
			PManager.log.print("File Tag: "+ fileCargoTag, this, Details.NOTES);
		}
		return file_names_array.length;
	}

	/**
	 * Initializes modules.
	 */
	public void initialize() {
		// TODO: CALLED at an incorrect timing ?? (inside "start_trackiing")
		filters_data.clear();
		for (final Module mo : modules)
			mo.initialize();

		constructCargoArray();

		runnable_modules = new RunnableModulesThread();
	}

	/**
     * 
     */

	private void instantiateModules(final String[] moduleNames) {
		PManager.log.print("instantiating Modules", this, Details.VERBOSE);
		// ////////////////////////////////
		// Rearing Module
		if (isWithinArray("Rearing Module", moduleNames)) {
			final RearingModuleConfigs rearingConfigs = new RearingModuleConfigs(
					"Rearing Module");
			final RearingModule rearingModule = new RearingModule(
					"Rearing Module", rearingConfigs);
			modules.add(rearingModule);
		}
		// ////////////////////////////////
		// Zones Module
		if (isWithinArray("Zones Module", moduleNames)) {
			final ZonesModuleConfigs zonesConfigs = new ZonesModuleConfigs(
					"Zones Module", 50, // TODO: change 50
					width, height);
			final ZonesModule zones_module = new ZonesModule("Zones Module",
					zonesConfigs);
			modules.add(zones_module);
		}
		// ////////////////////////////////
		// Session Module
		if (isWithinArray("Session Module", moduleNames)) {
			final SessionModuleConfigs sessionConfigs = new SessionModuleConfigs(
					"Session Module");
			final SessionModule sessionModule = new SessionModule(
					"Session Module", sessionConfigs);
			modules.add(sessionModule);
		}
		// ////////////////////////////////
		// MovementMeter Module
		if (isWithinArray("Movement Meter Module", moduleNames)) {
			final MovementMeterModuleConfigs movementModuleConfigs = new MovementMeterModuleConfigs(
					"Movement Meter Module");
			final MovementMeterModule movementMeterModule = new MovementMeterModule(
					"Movement Meter Module", movementModuleConfigs);
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
		PManager.main_gui.loadPluggedGUI(getModulesGUI());
	}

	/**
	 * Removes a data object (deRegisters it) from all modules registered with
	 * it.
	 * 
	 * @param data
	 *            data object of a video filter
	 */
	public void removeDataObject(final Data data) {
		filters_data.remove(data);
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
		if (run & !run_modules) {
			run_modules = true;
			th_modules = new Thread(runnable_modules);
			th_modules.start();
		} else if (!run & run_modules) {
			run_modules = false;
			
			// if paused, we need to resume to unlock the paused thread
			if(PManager.getDefault().getState().getStream()==StreamState.PAUSED)
				resumeModules();
			
			try {
				Thread.sleep(33);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			th_modules = null;
			
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
		if(th_modules!=null)
			th_modules.interrupt();
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
				"Zones Module", -1, width, height) });
	}

	public void setupModules(final Experiment exp) {
		// unload old modules
		for(Module m:modules)
			m.unload();
		
		// remove old modules
		modules.clear();
		modules_data.clear();
		
		// ////////////////////////////////
		// Experiment Module
		final ExperimentModule expModule = ExperimentManager.getDefault()
				.instantiateExperimentModule();
		modules.add(expModule);
		
		final ModulesSet openFieldModulesSetup = new ModulesSet(new String[] {
				"Rearing Module", "Zones Module", "Session Module" });

		final ModulesSet forcedSwimmingModulesSetup = new ModulesSet(
				new String[] { "Session Module", "Movement Meter Module" });

		switch (exp.type) {
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
			final Module tmp = getModuleByName(configs[i].getModuleName());
			if (tmp != null)
				tmp.updateConfigs(configs[i]);
		}
	}

}
