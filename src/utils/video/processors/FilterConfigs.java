package utils.video.processors;

public abstract class FilterConfigs
{
	public CommonConfigs common_configs;
	public boolean enabled;
	private String filter_name;
	
	
	public void setFilter_name(String filter_name) {
		this.filter_name = filter_name;
	}

	public String getFilter_name() {
		return filter_name;
	}
	
	public abstract void mergeConfigs(FilterConfigs configs);
}