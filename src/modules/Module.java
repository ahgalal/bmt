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

import org.eclipse.swt.widgets.Shell;

import ui.PluggedGUI;
import utils.video.filters.Data;

/**
 * Parent of all modules classes.
 * 
 * @author Creative
 */
public abstract class Module<GUIType extends PluggedGUI, ConfigsType extends ModuleConfigs, DataType extends Data> // implements
// StateListener
{
	protected Cargo gui_cargo;
	protected Cargo file_cargo;
	protected ConfigsType configs;
	protected String name;

	protected Data[] filters_data;
	protected Data[] modules_data;
	protected DataType data;
	protected GUIType gui;

	/**
	 * Gets the data cargo to be sent to GUI.
	 * 
	 * @return Cargo containing data for GUI
	 */
	public Cargo getGUICargo()
	{
		return gui_cargo;
	}

	/**
	 * Gets the data cargo to be sent to file writer.
	 * 
	 * @return Cargo containing data for file writer
	 */
	public Cargo getFileCargo()
	{
		return file_cargo;
	}

	/**
	 * Initializes a HashTable with empty data.
	 * 
	 * @param hash
	 *            HashTable to initialize
	 * @param keys
	 *            keys of the hashtable
	 */
	protected void initializeHashKeys(
			final Hashtable<String, String> hash,
			final String[] keys)
	{
		for (final String s : keys)
			hash.put(s, "");
	}

	/**
	 * Initializes the module.
	 */
	public abstract void initialize();

	/**
	 * Gets the module's name.
	 * 
	 * @return String containing the module's name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Deinitializes the module.
	 */
	public abstract void deInitialize();

	/**
	 * Updates the configurations of the module.
	 * 
	 * @param config
	 *            configurations object containing new values
	 */
	public abstract void updateConfigs(ModuleConfigs config);

	/**
	 * Process , do all the work the module should do at each time interval.
	 */
	public abstract void process();

	/**
	 * Updates the cargo data to be sent to GUI.
	 */
	public abstract void updateGUICargoData();

	/**
	 * Updates the cargo data to be sent to file writer.
	 */
	public abstract void updateFileCargoData();

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
	public abstract void registerModuleDataObject(Data data);

	/**
	 * Unregisters a data object of a video filter from the module.
	 * 
	 * @param data
	 *            data object of a video filer
	 */
	public abstract void deRegisterDataObject(Data data);

	/**
	 * Initializes the module.
	 * 
	 * @param name
	 *            module's name
	 * @param config
	 *            initial configurations of the module
	 */
	public Module(final String name, final ConfigsType config)
	{
		this.name = name;
		this.configs = config;
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
	 * Gets the Module data.
	 * 
	 * @return Data object containing the data of this module
	 */
	public Data getModuleData()
	{
		return data;
	}

	public PluggedGUI getGUI()
	{
		return gui;
	}
}