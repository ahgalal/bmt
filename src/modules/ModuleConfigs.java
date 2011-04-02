package modules;

/**
 * Parent of all Configurations Classes of modules.
 * 
 * @author Creative
 */
public abstract class ModuleConfigs
{

	protected boolean enable;
	protected String module_name;

	/**
	 * Get the module name this configuration object is to be applied to.
	 * 
	 * @return String containing the name of the module this configuration
	 *         object is to be applied to
	 */
	public String getModuleName()
	{
		return module_name;
	}

	/**
	 * Initializes the configurations.
	 * 
	 * @param module_name
	 */
	public ModuleConfigs(final String module_name)
	{
		this.module_name = module_name;
	}

	/**
	 * Merges the current configurations with the incoming configurations
	 * object, as most of the time, the incoming configurations object has some
	 * null fields (due to the caller not knowing the values), so the non-null
	 * fields ONLY should be merged with the current configurations.
	 * 
	 * @param config
	 *            incoming configurations object
	 */
	protected abstract void mergeConfigs(ModuleConfigs config);
}
