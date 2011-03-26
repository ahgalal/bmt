package modules;

import java.util.ArrayList;

import utils.video.filters.Data;

public class ModulesManager
{

	private static ModulesManager me;
	private final ArrayList<Data> data_objects;
	private final ArrayList<Module> modules;
	private Cargo[] gui_cargos;
	private Cargo[] file_cargos;

	private boolean run_modules;
	private RunnableModulesThread runnable_modules;
	private Thread th_modules;

	private String[] gui_data_array;
	private String[] gui_names_array;

	private String[] file_data_array;
	private String[] file_names_array;

	public void runAnalyzers(final boolean run)
	{
		if (run & !run_modules)
		{
			run_modules = true;
			th_modules = new Thread(runnable_modules);
			th_modules.start();
		} else if (!run & run_modules)
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

	public static ModulesManager getDefault()
	{
		return me;
	}

	public ModulesManager()
	{
		me = this;
		data_objects = new ArrayList<Data>();
		modules = new ArrayList<Module>();

		final RearingModuleConfigs rearing_configs = new RearingModuleConfigs(
				"Rearing Module");
		final ZonesModuleConfigs zones_configs = new ZonesModuleConfigs(
				"Zones Module",
				50);// TODO:
		// change
		// 50
		final SessionModuleConfigs session_configs = new SessionModuleConfigs(
				"Session Module");
		final ExperimentModuleConfigs experiment_configs = new ExperimentModuleConfigs(
				"Experiment Module");

		final RearingModule rearing_module = new RearingModule(
				"Rearing Module",
				rearing_configs);
		final ZonesModule zones_module = new ZonesModule("Zones Module", zones_configs);
		final SessionModule session_module = new SessionModule(
				"Session Module",
				session_configs);
		final ExperimentModule experiment_module = new ExperimentModule(
				"Experiment Module",
				experiment_configs);

		modules.add(experiment_module);
		modules.add(rearing_module);
		modules.add(zones_module);
		modules.add(session_module);
	}

	public void initialize()
	{
		data_objects.clear();
		for (final Module mo : modules)
			mo.initialize();

		constructCargoArray();

		runnable_modules = new RunnableModulesThread();
	}

	public void addDataObject(final Data data)
	{
		data_objects.add(data);
		for (final Module module : modules)
			module.updateDataObject(data);
	}

	public void removeDataObject(final Data data)
	{
		data_objects.remove(data);
		// constructCargoArray();
	}

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

	public String[] getGUINames()
	{

		return gui_names_array;
	}

	public String[] getCodeNames()
	{
		return file_names_array;
	}

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

	public Module getModuleByName(final String name)
	{
		for (final Module mo : modules)
			if (mo.getName().equals(name))
				return mo;
		return null;
	}

	public void updateModuleConfigs(final ModuleConfigs[] configs)
	{
		for (int i = 0; i < configs.length; i++)
		{
			final Module tmp = getModuleByName(configs[i].getModuleName());
			if (tmp != null)
				tmp.updateConfigs(configs[i]);
		}
	}

}
