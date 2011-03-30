package utils.video.filters;

/**
 * Configurations of a VideoFilter.
 * 
 * @author Creative
 */
public abstract class FilterConfigs
{
	/**
	 * Needed by all filters, contains properties like: image width,height ..
	 * etc.
	 */
	public CommonFilterConfigs common_configs;
	/**
	 * is the filter enable
	 */
	public boolean enabled;
	private final String name;

	/**
	 * Initializes configurations.
	 * 
	 * @param name
	 *            name of the filter these configurations are intended for.
	 * @param common_configs
	 *            CommonFilterConfigs object, needed by almost all filters
	 */
	public FilterConfigs(final String name, final CommonFilterConfigs common_configs)
	{
		this.common_configs = common_configs;
		this.name = name;
	}

	/**
	 * Gets the filter name for this configuration object.
	 * 
	 * @return String containing the filter's name
	 */
	public String getConfigurablename()
	{
		return name;
	}

	/**
	 * Merges the incoming configurations object with "this", typically the
	 * incoming configurations object will have some null fields (due to lack of
	 * info on the caller side), those null fileds should be filtered out, and
	 * only valid fields are copied to "this" object.
	 * 
	 * @param configs incoming configurations object
	 */
	public abstract void mergeConfigs(FilterConfigs configs);
	
	/**
	 * Checks that All configurations are set. (for testing purposes only)
	 * @return true: success
	 */
	public abstract boolean validate();
}