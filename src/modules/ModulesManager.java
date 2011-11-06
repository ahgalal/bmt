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

import modules.experiment.ExperimentModule;
import modules.experiment.ExperimentModuleConfigs;
import modules.movementmeter.MovementMeterModule;
import modules.rearing.RearingModule;
import modules.rearing.RearingModuleConfigs;
import modules.session.SessionModule;
import modules.session.SessionModuleConfigs;
import modules.zones.ZonesModule;
import modules.zones.ZonesModuleConfigs;

import org.eclipse.swt.widgets.Shell;

import ui.PluggedGUI;
import utils.PManager;
import utils.StatusManager.StatusSeverity;
import utils.video.filters.Data;
@SuppressWarnings("rawtypes")
/**
 * Manager for all modules.
 * 
 * @author Creative
 */
public class ModulesManager
{
	private static ModulesManager me;
	private final ArrayList<Data> filters_data;
	private final ArrayList<Module> modules;
	private Cargo[] gui_cargos;
	private Cargo[] file_cargos;

	private final ArrayList<Data> modules_data;

	private boolean run_modules;
	private RunnableModulesThread runnable_modules;
	private Thread th_modules;

	private String[] gui_data_array;
	private String[] gui_names_array;

	private String[] file_data_array;
	private String[] file_names_array;
	private int width;
	private int height;

	/**
	 * Starts/Stops running all modules.
	 * 
	 * @param run
	 *            true/false to start/stop
	 */
	public void runModules(final boolean run)
	{
		if (run & !run_modules)
		{
			run_modules = true;
			th_modules = new Thread(runnable_modules);
			th_modules.start();
		}
		else if (!run & run_modules)
		{
			run_modules = false;
			try
			{
				Thread.sleep(33);
			} catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
			th_modules = null;

			for (final Module mo : modules)
			{
				mo.deInitialize();
			}
		}
	}

	/**
	 * Get the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static ModulesManager getDefault()
	{
		return me;
	}

	/**
	 * Initializes modules.
	 */
	public ModulesManager()
	{
		me = this;
		filters_data = new ArrayList<Data>();
		modules_data = new ArrayList<Data>();
		modules = new ArrayList<Module>();

		// ////////////////////////////////
		// Rearing Module
		final RearingModuleConfigs rearing_configs = new RearingModuleConfigs(
				"Rearing Module");
		final RearingModule rearing_module = new RearingModule(
				"Rearing Module",
				rearing_configs);

		// ////////////////////////////////
		// Zones Module
		final ZonesModuleConfigs zones_configs = new ZonesModuleConfigs(
				"Zones Module",
				50, // TODO: change 50
				width,
				height);
		final ZonesModule zones_module = new ZonesModule("Zones Module", zones_configs);

		// ////////////////////////////////
		// Session Module
		final SessionModuleConfigs session_configs = new SessionModuleConfigs(
				"Session Module");
		final SessionModule session_module = new SessionModule(
				"Session Module",
				session_configs);

		// ////////////////////////////////
		// Experiment Module
		final ExperimentModuleConfigs experiment_configs = new ExperimentModuleConfigs(
				"Experiment Module");
		final ExperimentModule experiment_module = new ExperimentModule(
				"Experiment Module",
				experiment_configs);
		
		
		// ////////////////////////////////
		// MovementMeter Module
/*		final MovementMeter experiment_configs = new ExperimentModuleConfigs(
				"Experiment Module");*/
		final MovementMeterModule movementMeterModule = new MovementMeterModule(
				"Movement Meter Module",
				experiment_configs);

		modules.add(experiment_module);
		modules.add(rearing_module);
		modules.add(zones_module);
		modules.add(session_module);
		modules.add(movementMeterModule);


		setWidthandHeight(640, 480);

		for (final Module mo : modules)
			modules_data.add(mo.getModuleData());

		for (final Module mo : modules)
			for (final Data data : modules_data)
				mo.registerModuleDataObject(data);

		PManager.main_gui.loadPluggedGUI(getModulesGUI());
	}

	public PluggedGUI[] getModulesGUI()
	{
		int validGUIsNumber = 0;
		for (final Module module : modules)
		{
			if (module.getGUI() != null)
			{
				validGUIsNumber++;
			}
		}
		final PluggedGUI[] arr = new PluggedGUI[validGUIsNumber];
		int i = 0;
		for (final Module module : modules)
		{
			if (module.getGUI() != null)
			{
				arr[i] = module.getGUI();
				i++;
			}
		}
		return arr;
	}

	/**
	 * Initializes modules.
	 */
	public void initialize()
	{
		// TODO: CALLED at an incorrect timing ?? (inside "start_trackiing")
		filters_data.clear();
		for (final Module mo : modules)
			mo.initialize();

		constructCargoArray();

		runnable_modules = new RunnableModulesThread();
	}

	/**
	 * Passes incoming data object to all modules, only modules interested in
	 * that data object, will accept it.
	 * 
	 * @param data
	 *            incoming data object from a video filter
	 */
	public void addFilterDataObject(final Data data)
	{
		filters_data.add(data);
		for (final Module module : modules)
			module.registerFilterDataObject(data);
	}

	/**
	 * Removes a data object (deRegisters it) from all modules registered with
	 * it.
	 * 
	 * @param data
	 *            data object of a video filter
	 */
	public void removeDataObject(final Data data)
	{
		filters_data.remove(data);
		for (final Module mo : modules)
			mo.deRegisterDataObject(data);
	}

	/**
	 * Gets the information to be sent to GUI.
	 * 
	 * @return String array containing data to be sent to GUI
	 */
	public String[] getGUIData()
	{
		for (final Module mo : modules)
			mo.updateGUICargoData();

		int i = 0;
		for (final Cargo cargo : gui_cargos)
		{
			final String[] tmp_cargo_data = cargo.getData();
			for (int j = 0; j < tmp_cargo_data.length; j++)
			{
				gui_data_array[i] = tmp_cargo_data[j];
				i++;
			}
		}
		return gui_data_array;
	}

	/**
	 * Gets the information to be sent to file writer.
	 * 
	 * @return String array containing data to be sent to file writer
	 */
	public String[] getFileData()
	{
		for (final Module mo : modules)
			mo.updateFileCargoData();

		int i = 0;
		for (final Cargo cargo : file_cargos)
		{
			final String[] tmp_cargo_data = cargo.getData();
			for (int j = 0; j < tmp_cargo_data.length; j++)
			{
				file_data_array[i] = tmp_cargo_data[j];
				i++;
			}
		}
		return file_data_array;
	}

	/**
	 * Gets list of parameters (columns names) to be sent to GUI.
	 * 
	 * @return String array containing parameters (columns names) to be sent to
	 *         GUI
	 */
	public String[] getGUINames()
	{

		return gui_names_array;
	}

	/**
	 * Gets list of parameters (columns names) to be sent to file writer.
	 * 
	 * @return String array containing parameters (columns names) to be sent to
	 *         file writer
	 */
	public String[] getCodeNames()
	{
		constructCargoArray();
		return file_names_array;
	}

	/**
	 * Gets the number of Experiment parameters to be stored in file.
	 * 
	 * @return integer representing the number of file parameters for the active
	 *         experiment
	 */
	public int getNumberOfFileParameters()
	{
		constructCargoArray();
		return file_names_array.length;
	}

	/**
	 * Checks if all the modules are ready to run/process data.
	 * 
	 * @param shell
	 *            parent shell for displaying messageboxes
	 * @return true: all are ready, false: some modules are not ready
	 */
	public boolean areModulesReady(final Shell shell)
	{
		for (final Module mo : modules)
			if (!mo.amIReady(shell))
			{
				PManager.log.print(
						mo.getName() + " cancelled Tracking!",
						this,
						StatusSeverity.ERROR);
				return false;
			}
		return true;
	}

	/**
	 * Builds the Cargo array based on the number of instantiated modules.
	 */
	private void constructCargoArray()
	{
		gui_cargos = null;
		int num_gui_cargos = 0;
		int num_file_cargos = 0;
		for (final Module m : modules)
			if (m.getGUICargo() != null)
				num_gui_cargos++;

		for (final Module m : modules)
			if (m.getFileCargo() != null)
				num_file_cargos++;

		int num_strs = 0;
		gui_cargos = new Cargo[num_gui_cargos];
		file_cargos = new Cargo[num_file_cargos];

		for (int i = 0; i < num_gui_cargos; i++)
		{
			gui_cargos[i] = modules.get(i).getGUICargo();
			num_strs += gui_cargos[i].getData().length;
		}
		gui_data_array = new String[num_strs];
		gui_names_array = new String[num_strs];

		num_strs = 0;
		for (int i = 0; i < num_file_cargos; i++)
		{
			file_cargos[i] = modules.get(i).getFileCargo();
			num_strs += file_cargos[i].getData().length;
		}

		file_data_array = new String[num_strs];
		file_names_array = new String[num_strs];

		int i = 0;
		for (final Cargo cargo : gui_cargos)
		{
			final String[] tmp_cargo_names = cargo.getTags();
			for (int j = 0; j < tmp_cargo_names.length; j++)
			{
				gui_names_array[i] = tmp_cargo_names[j];
				i++;
			}
		}

		i = 0;
		for (final Cargo cargo : file_cargos)
		{
			final String[] tmp_cargo_names = cargo.getTags();
			for (int j = 0; j < tmp_cargo_names.length; j++)
			{
				file_names_array[i] = tmp_cargo_names[j];
				i++;
			}
		}
	}

	/**
	 * Runnable for running Modules.
	 * 
	 * @author Creative
	 */
	private class RunnableModulesThread implements Runnable
	{
		@Override
		public void run()
		{
			while (run_modules)
			{
				try
				{
					Thread.sleep(33);
				} catch (final InterruptedException e)
				{
					e.printStackTrace();
				}

				for (final Module mo : modules)
					// if(mo!=null)
					mo.process();
			}
		}
	}

	/**
	 * Gets a module using the module's name.
	 * 
	 * @param name
	 *            name of the module to return
	 * @return module instance having the name specified
	 */
	public Module getModuleByName(final String name)
	{
		for (final Module mo : modules)
			if (mo.getName().equals(name))
				return mo;
		return null;
	}

	/**
	 * Updates the configurations of the modules.
	 * 
	 * @param configs
	 *            array of ModuleConfigs, containing configurations objects for
	 *            the modules. each module will be configured according to its
	 *            configurations object in the array.
	 */
	public void updateModuleConfigs(final ModuleConfigs[] configs)
	{
		for (int i = 0; i < configs.length; i++)
		{
			final Module tmp = getModuleByName(configs[i].getModuleName());
			if (tmp != null)
				tmp.updateConfigs(configs[i]);
		}
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
	public void setWidthandHeight(final int width, final int height)
	{
		this.width = width;
		this.height = height;

		updateModuleConfigs(new ModuleConfigs[] { new ZonesModuleConfigs(
				"Zones Module",
				-1,
				width,
				height) });
	}

	/**
	 * Gets the names of active modules.
	 * 
	 * @return ArrayList containing the names of active modules
	 */
	public ArrayList<String> getModulesNames()
	{
		final ArrayList<String> tmp_arr = new ArrayList<String>();
		for (final Module m : modules)
			tmp_arr.add(m.getName());
		return tmp_arr;
	}

}
