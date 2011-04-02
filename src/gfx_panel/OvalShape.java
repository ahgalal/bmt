/*
 * this class is related to shape of zone Circular Zone
 */
package gfx_panel;

import java.awt.Color;
import java.awt.Graphics;

import org.eclipse.swt.graphics.RGB;

/**
 * this class inherited form Shape class.
 * 
 * @author A.Ramadan
 */
public class OvalShape extends Shape
{

	private final double PI = 3.14159; // mathmatics constant
	private int diameter_x; // position in X-axis
	private int diameter_y; // postion in Y-axis

	/**
	 * the definition of this class need 5 parameters its position/length in
	 * x-axis its position/length in y-axis and its color.
	 * 
	 * @param dx
	 *            diameter on the x axis
	 * @param dy
	 *            diameter on the y axis
	 * @param x
	 *            position on the x axis
	 * @param y
	 *            position on the y axis
	 * @param c
	 *            color of the shape
	 */
	public OvalShape(final int dx, final int dy, final int x, final int y, final RGB c)
	{
		super(x, y, dx, dy, c);
		this.diameter_x = dx;
		this.diameter_y = dy;
	}

	/**
	 * this is by definition calling (Initializes the OvalShape).
	 */
	public OvalShape()
	{
		this(0, 0, 0, 0, null);
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#setHeight(int)
	 */
	@Override
	public void setHeight(final int height)
	{
		super.setHeight(height);
		diameter_y = height;
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#setWidth(int)
	 */
	@Override
	public void setWidth(final int width)
	{
		super.setWidth(width);
		diameter_x = width;
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#getArea()
	 */
	@Override
	public int getArea()
	{
		return (int) (PI * (diameter_x / 2) * (diameter_y / 2));
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(final Graphics g)
	{
		g.setColor(new Color(rgb_color.red, rgb_color.green, rgb_color.blue));
		g.drawOval(x, y, diameter_x, diameter_y);
	}
}
