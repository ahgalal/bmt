/**
 * 
 */
package utils.video.filters;

/**
 * Parent of all Video utilities & filters.
 * 
 * @author Creative
 */
public abstract class VideoFilter
{

	// protected boolean enabled;
	protected String name;
	protected FilterData filter_data;
	protected FilterConfigs configs;

	// protected Module data_analyzer;

	public VideoFilter(final String name, final FilterConfigs configs)
	{
		this.name = name;
		this.configs = configs;
	}

	public FilterConfigs getConfigs()
	{
		return configs;
	}

	public void updateConfigs(final FilterConfigs configs)
	{
		this.configs.mergeConfigs(configs);
	}

	public abstract int[] process(int[] image_data);

	public boolean enable(final boolean enable)
	{
		configs.enabled = enable;
		return configs.enabled;
	}

	public String getName()
	{
		return name;
	}

	public Data getFilterData()
	{
		return filter_data;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public abstract boolean initialize();

}