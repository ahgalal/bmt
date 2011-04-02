package modules;

import utils.video.filters.Data;

/**
 * Parent of all modules classes.
 * @author Creative
 *
 */
public abstract class Module
{
	protected Cargo gui_cargo;
	protected Cargo file_cargo;
	protected ModuleConfigs configs;
	protected String name;

	protected Data[] data;

	/**
	 * Gets the data cargo to be sent to GUI.
	 * @return Cargo containing data for GUI
	 */
	public Cargo getGUICargo()
	{
		return gui_cargo;
	}

	/**
	 * Gets the data cargo to be sent to file writer.
	 * @return Cargo containing data for file writer
	 */
	public Cargo getFileCargo()
	{
		return file_cargo;
	}

	/**
	 * Initializes the module.
	 */
	public abstract void initialize();

	/**
	 * Gets the module's name.
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
	 * @param config configurations object containing new values
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
	 * @param data data object of a video filer
	 */
	public abstract void registerDataObject(Data data);
	
	/**
	 * Unregisters a data object of a video filter from the module.
	 * @param data data object of a video filer
	 */
	public abstract void deRegisterDataObject(Data data);

	/**
	 * Initializes the module.
	 * @param name module's name
	 * @param config initial configurations of the module
	 */
	public Module(final String name, final ModuleConfigs config)
	{
		this.name = name;
		configs = config;
	}
}