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

	public RectangleShape(
			final int x,
			final int y,
			final int width,
			final int height,
			final RGB c)
	{
		super(x, y, width, height, c);
	}

	public RectangleShape()
	{
		this(0, 0, 0, 0, null);
	}

	@Override
	public int getArea()
	{
		return width * height;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public void setWidth(final int width)
	{
		this.width = width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public void setHeight(final int height)
	{
		this.height = height;
	}

	@Override
	public void draw(final Graphics g)
	{
		g.setColor(new Color(rgb_color.red, rgb_color.green, rgb_color.blue));
		g.drawRect(x, y, width, height);
	}

}
