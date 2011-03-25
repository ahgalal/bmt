package utils.video.processors;

public abstract class FilterConfigs
{
	public CommonFilterConfigs common_configs;
	public boolean enabled;
	private final String name;

	public FilterConfigs(final String name, final CommonFilterConfigs common_configs)
	{
		this.common_configs = common_configs;
		this.name = name;
	}

	public String getConfigurablename()
	{
		return name;
	}

	public abstract void mergeConfigs(FilterConfigs configs);
}