package utils.video.filters;

import java.awt.Point;

/**
 * Link between filters, to transfer image data.
 * 
 * @author Creative
 */
public class Link
{
	private int[] img_data;

	/**
	 * Initializes image data.
	 * 
	 * @param dims
	 *            dimensions of the image the link is to deal with
	 */
	public Link(final Point dims)// int width,int height)
	{
		img_data = new int[dims.x * dims.y];
	}

	/**
	 * Gets image's data on the link.
	 * 
	 * @return array of integers representing the image's data
	 */
	public int[] getData()
	{
		return img_data;
	}

	/**
	 * Sets the image data on the link.
	 * 
	 * @param newdata
	 *            new data to put on the link
	 */
	public void setData(final int[] newdata)
	{
		img_data = newdata;
	}
}
