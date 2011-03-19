package utils.video.processors;

public abstract class FilterConfigs
{
	public CommonFilterConfigs common_configs;
	public boolean enabled;
	private String name;
	
	public FilterConfigs(String name,CommonFilterConfigs common_configs)
	{
		this.common_configs=common_configs;
		this.name=name;
	}
	
	public String getConfigurable_name() {
		return name;
	}
	
	public abstract void mergeConfigs(FilterConfigs configs);
}