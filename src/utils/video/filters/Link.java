package utils.video.filters;

import java.awt.Point;


/**
 * Link between filters, to transfer image data.
 * @author Creative
 *
 */
public class Link
{
	private int[] img_data;
	/**
	 * Initializes image data.
	 * @param dims
	 */
	public Link(Point dims)//int width,int height)
	{
		img_data = new int[dims.x*dims.y];
	}
	
	/**
	 * Gets image's data on the link.
	 * @return array of integers representing the image's data
	 */
	public int[] getData()
	{
		return img_data;
	}
	
	/**
	 * Sets the image data on the link.
	 * @param newdata
	 */
	public void setData(int[] newdata)
	{
		img_data=newdata;
	}
}
