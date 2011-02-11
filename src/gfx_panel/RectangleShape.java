package gfx_panel;


import java.awt.Color;
import java.awt.Graphics;

import org.eclipse.swt.graphics.RGB;


/**
 * Rectange Shape Class, represents a rectangle drawn on the GfxPanel
 * @author Creative
 *
 */
public class RectangleShape extends Shape{

	public RectangleShape(int x, int y, int width, int height,RGB c) {
		super(x, y, width, height, c);
	}

	public RectangleShape() {
		this(0, 0, 0, 0,null);
	}

	public int getArea()
	{
		return width*height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public void Draw(Graphics g) {
		g.setColor(new Color(rgb_color.red,rgb_color.green,rgb_color.blue));
		g.drawRect(x, y, width, height);
	}


}

