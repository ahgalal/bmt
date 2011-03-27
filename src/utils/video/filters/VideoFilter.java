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
	protected String name;
	protected FilterData filter_data;
	protected FilterConfigs configs;
	protected Link link_in,link_out;

	public VideoFilter(
			final String name,
			final FilterConfigs configs,
			final Link link_in,
			final Link link_out)
	{
		this.name = name;
		this.configs = configs;
		this.link_in=link_in;
		this.link_out=link_out;
	}

	public FilterConfigs getConfigs()
	{
		return configs;
	}

	public void updateConfigs(final FilterConfigs configs)
	{
		this.configs.mergeConfigs(configs);
	}

	public abstract void process();

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