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
	protected FilterSpecialData special_data;
	protected FilterConfigs configs;
	protected GUICargo cargo;

	
	public FilterConfigs getConfigs() {
		return configs;
	}
	public void setConfigs(FilterConfigs configs)
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
	
	public Object getSpecialData()
	{
		return special_data;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public abstract boolean initialize();
	
	public abstract void setCargo();
	
}