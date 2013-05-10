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

import java.util.Hashtable;

import modules.experiment.ExperimentType;

import org.eclipse.swt.widgets.Shell;

import ui.PluggedGUI;
import utils.Configurable;
import utils.PManager;
import filters.Data;

/**
 * Parent of all modules classes.
 * 
 * @author Creative
 */
@SuppressWarnings("rawtypes")
public abstract class Module<GUIType extends PluggedGUI, ConfigsType extends ModuleConfigs, DataType extends ModuleData>
		implements Configurable<ConfigsType>
// StateListener
{
	protected ConfigsType configs;
	protected DataType data;
	protected ExperimentType[] expType;
	protected Cargo fileCargo;

	protected Data[] filtersData;
	protected GUIType gui;
	protected Cargo guiCargo;
	protected Data[] modulesData;
	private final String name;
	
	public void filterConfiguration(){
	}

	/**
	 * Initializes the module.
	 * 
	 * @param name
	 *            module's name
	 * @param config
	 *            initial configurations of the module
	 */
	public Module(final String name, final ConfigsType config) {
		this.configs = config;
		this.name = name;
	}

	public boolean allowTracking() {
		return true;
	}

	/**
	 * Checks if the module is ready to run/process data.
	 * 
	 * @param shell
	 *            parent shell, used to display messageboxes on the screen
	 * @return true: ready, false: not ready(something is missing or
	 *         misconfiguration)
	 */
	public abstract boolean amIReady(Shell shell);

	/**
	 * Deinitializes the module.
	 */
	public abstract void deInitialize();

	/**
	 * Unregisters a data object of a video filter from the module.
	 * 
	 * @param data
	 *            data object of a video filer
	 */
	public abstract void deRegisterDataObject(Data data);

	/**
	 * Gets the data cargo to be sent to file writer.
	 * 
	 * @return Cargo containing data for file writer
	 */
	public Cargo getFileCargo() {
		return fileCargo;
	}

	public PluggedGUI getGUI() {
		return gui;
	}

	/**
	 * Gets the data cargo to be sent to GUI.
	 * 
	 * @return Cargo containing data for GUI
	 */
	public Cargo getGUICargo() {
		return guiCargo;
	}

	@Override
	public abstract String getID();

	/**
	 * Gets the Module data.
	 * 
	 * @return Data object containing the data of this module
	 */
	public ModuleData getModuleData() {
		return data;
	}

	@Override
	public String getName() {
		return name;
	}

	public ExperimentType[] getSupportedExperiments() {
		return expType;
	}

	/**
	 * Initializes the module.
	 */
	public abstract void initialize();

	/**
	 * Initializes a HashTable with empty data.
	 * 
	 * @param hash
	 *            HashTable to initialize
	 * @param keys
	 *            keys of the hashtable
	 */
	protected void initializeHashKeys(final Hashtable<String, String> hash,
			final String[] keys) {
		for (final String s : keys)
			hash.put(s, "");
	}

	public abstract Module newInstance(String name);

	public void pause() {
		// empty, overridden when needed
	}

	/**
	 * Process , do all the work the module should do at each time interval.
	 */
	public abstract void process();

	/**
	 * Registers a data object of a video filter with the module.
	 * 
	 * @param data
	 *            data object of a video filer
	 */
	public abstract void registerFilterDataObject(Data data);

	/**
	 * Registers a data object of another module with this module.
	 * 
	 * @param data
	 */
	public abstract void registerModuleDataObject(ModuleData data);

	public void resume() {
		// empty, overridden when needed
	}

	/**
	 * Unloads module from memory, including GUI controls.
	 */
	public void unload() {
		// unload data
		modulesData = null;
		filtersData = null;
		guiCargo = null;
		fileCargo = null;
		data = null;
		configs = null;

		// unload GUI
		if (gui != null) {
			PManager.getDefault().removeStateListener(gui);
			PManager.log.print("Unloading GUI: " + gui.getClass(), this);
			gui.deInitialize();
		}
	}

	/**
	 * Updates the configurations of the module.
	 * 
	 * @param config
	 *            configurations object containing new values
	 */
	public void updateConfigs(ConfigsType config){
		if (this.configs == null)
			this.configs = config;
		this.configs.mergeConfigs(config);
	}

	/**
	 * Updates the cargo data to be sent to file writer.
	 */
	public abstract void updateFileCargoData();

	/**
	 * Updates the cargo data to be sent to GUI.
	 */
	public abstract void updateGUICargoData();
}