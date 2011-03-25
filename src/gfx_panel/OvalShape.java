/*
 * this class is related to shape of zone Circular Zone
 */
package gfx_panel;

import java.awt.Color;
import java.awt.Graphics;

import org.eclipse.swt.graphics.RGB;

// this class inherited form Shape class
public class OvalShape extends Shape
{

	private final double PI = 3.14159; // mathmatics constant
	private int diameter_x; // position in X-axis
	private int diameter_y; // postion in Y-axis

	/*
	 * the definition of this class need 4 parameters its position in x-axis its
	 * position in y-axis and its color
	 */
	public OvalShape(final int rx, final int ry, final int x, final int y, final RGB c)
	{
		super(x, y, rx, ry, c);
		this.diameter_x = rx;
		this.diameter_y = ry;
	}

	/*
	 * this is by definition calling
	 */
	public OvalShape()
	{
		this(0, 0, 0, 0, null);
	}

	@Override
	/*
	 * this function to set the height in Y-axis
	 */
	public void setHeight(final int height)
	{
		super.setHeight(height);
		diameter_y = height;
	}

	@Override
	/*
	 * this funtion is to set the width of oval
	 */
	public void setWidth(final int width)
	{
		super.setWidth(width);
		diameter_x = width;
	}

	/*
	 * this funtion to get the area of the circle
	 */
	@Override
	public int getArea()
	{
		return (int) (PI * (diameter_x / 2) * (diameter_y / 2));
	}

	/*
	 * this function is responsible for drawing the shape in the screen
	 */
	@Override
	public void draw(final Graphics g)
	{
		g.setColor(new Color(rgb_color.red, rgb_color.green, rgb_color.blue));
		g.drawOval(x, y, diameter_x, diameter_y);
	}
}
