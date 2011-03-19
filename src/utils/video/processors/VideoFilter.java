/**
 * 
 */
package utils.video.processors;

/**
 * Parent of all Video utilities & filters.
 * @author Creative
 */
public abstract class VideoFilter {
	
	//protected boolean enabled;
	protected String name;
	protected FilterData filter_data;
	protected FilterConfigs configs;
	//protected Module data_analyzer;
	

	public VideoFilter(String name,FilterConfigs configs)
	{
		this.name=name;
		this.configs=configs;
	}
	
	public FilterConfigs getConfigs() {
		return configs;
	}
	public void updateConfigs(FilterConfigs configs)
	{
		this.configs.mergeConfigs(configs);
	}
	
	public abstract int[] process(int[] image_data);
	public boolean enable(boolean enable)
	{
		configs.enabled=enable;
		return configs.enabled;
	}
	public String getName() {
		return name;
	}
	
	public Data getFilterData()
	{
		return filter_data;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public abstract boolean initialize();
	
	
}