package gfx_panel;

import java.awt.Color;
import java.awt.Graphics;

import org.eclipse.swt.graphics.RGB;

/**
 * Rectange Shape Class, represents a rectangle drawn on the GfxPanel.
 * 
 * @author Creative
 */
public class RectangleShape extends Shape
{

	/**
	 * Initialized the Rectangle shape.
	 * @param x top left corner of the shape in the x dimension
	 * @param y top left corner of the shape in the y dimension
	 * @param width rectangle's width
	 * @param height rectangle's height
	 * @param c rectangle's color
	 */
	public RectangleShape(
			final int x,
			final int y,
			final int width,
			final int height,
			final RGB c)
	{
		super(x, y, width, height, c);
	}

	/**
	 * Initialized the Rectangle shape.
	 */
	public RectangleShape()
	{
		this(0, 0, 0, 0, null);
	}

	/* (non-Javadoc)
	 * @see gfx_panel.Shape#getArea()
	 */
	@Override
	public int getArea()
	{
		return width * height;
	}

	/* (non-Javadoc)
	 * @see gfx_panel.Shape#getWidth()
	 */
	@Override
	public int getWidth()
	{
		return width;
	}

	/* (non-Javadoc)
	 * @see gfx_panel.Shape#setWidth(int)
	 */
	@Override
	public void setWidth(final int width)
	{
		this.width = width;
	}

	/* (non-Javadoc)
	 * @see gfx_panel.Shape#getHeight()
	 */
	@Override
	public int getHeight()
	{
		return height;
	}

	/* (non-Javadoc)
	 * @see gfx_panel.Shape#setHeight(int)
	 */
	@Override
	public void setHeight(final int height)
	{
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see gfx_panel.Shape#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(final Graphics g)
	{
		g.setColor(new Color(rgb_color.red, rgb_color.green, rgb_color.blue));
		g.drawRect(x, y, width, height);
	}

}
