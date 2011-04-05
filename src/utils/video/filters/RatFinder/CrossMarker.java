package utils.video.filters.RatFinder;

import java.awt.Color;

public class CrossMarker extends Marker
{

	private final int width, height, thikness;
	private final Color color;
	private int actual_width, actual_height;
	private int x1, x2, y1, y2;

	public CrossMarker(
			final int width,
			final int height,
			final int thickness,
			final Color color,
			final int img_width,
			final int img_height)
	{
		super(img_width, img_height);
		this.width = width;
		this.height = height;
		this.thikness = thickness;
		this.color = color;
	}

	@Override
	public void draw(final int[] img, final int x, final int y)
	{
		actual_width = (x < width / 2) ? x + width / 2 : width;
		actual_height = (y < height / 2) ? y + height / 2 : height;

		x1 = (x < width / 2) ? 0 : x - width / 2;
		x2 = (x < thikness / 2) ? 0 : x - thikness / 2;

		y1 = (y < thikness / 2) ? 0 : y - thikness / 2;
		y2 = (y < height / 2) ? 0 : y - height / 2;

		// Horizontal line
		fillRect(img, x1, y1, actual_width, thikness);

		// Vertical line
		fillRect(img, x2, y2, thikness, actual_height);

	}

	/**
	 * Fills the specified rectangular area of the image with the specified color.
	 * @param img image to fill the rectangle on.
	 * @param x x co-ordinate of the rectangle
	 * @param y y co-ordinate of the rectangle
	 * @param width rectangle's width
	 * @param height rectangle's height
	 */
	private void fillRect(
			final int img[],
			int x,
			int y,
			final int width,
			final int height)
	{
		if(x<0) x=0;
		if(x+width>img_width) x=img_width-width-1;
		if(y<0) y=0;
		if(y+height>img_height) y=img_height-height-1;
		
		for (int i = x; i < x + width; i++)
		{
			for (int j = y; j < y + height; j++)
			{
				img[i + j * img_width] = color.getRGB();
			}
		}
	}

}
