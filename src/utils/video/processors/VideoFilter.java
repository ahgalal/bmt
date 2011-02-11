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
	
	public abstract int[] process(int[] image_data);
	public boolean enable(boolean enable)
	{
		enabled=enable;
		return enabled;
	}
}