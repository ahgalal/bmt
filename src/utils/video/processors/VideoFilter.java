/**
 * 
 */
package utils.video.processors;

/**
 * Parent of all Video utilities & filters.
 * @author Creative
 */
public abstract class VideoFilter {
	
	protected boolean enabled;
	protected String name;
	
	public abstract int[] process(int[] image_data);
	public boolean enable(boolean enable)
	{
		enabled=enable;
		return enabled;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}