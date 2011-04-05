package utils.video.filters.RatFinder;

/**
 * Parent of all markers, a marker is an object drawn on an image.
 * @author Creative
 *
 */
public abstract class Marker
{
	/**
	 * Image's dimensions
	 */
	protected int img_width,img_height;
	/**
	 * Initializes the marker.
	 * @param img_width image's width
	 * @param img_height image's height
	 */
	public Marker(int img_width,int img_height)
	{
		this.img_width=img_width;
		this.img_height=img_height;
	}
	/**
	 * Draws the marker on the specified image.
	 * @param img input image as an integer array
	 * @param x x co-ordinate of the marker on the image
	 * @param y y co-ordinate of the marker on the image
	 */
	public abstract void draw(int[] img,int x,int y);
}
