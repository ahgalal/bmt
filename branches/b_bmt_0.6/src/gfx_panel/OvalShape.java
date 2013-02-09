/***************************************************************************
 * Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly, Sarah Hamid and
 * Mohammed Ahmed Ramadan contact: ceng.ahmedgalal@gmail.com This file is part
 * of Behavioral Monitoring Tool. Behavioral Monitoring Tool is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, version 3 of the
 * License. Behavioral Monitoring Tool is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with Behavioral Monitoring Tool. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

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
public class OvalShape extends Shape {

	private static final double	PI	= 3.14159;	// mathmatics constant
	private int					diameterX;	// position in X-axis
	private int					diameterY;	// postion in Y-axis

	/**
	 * this is by definition calling (Initializes the OvalShape).
	 */
	public OvalShape() {
		this(0, 0, 0, 0, null);
	}

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
	public OvalShape(final int dx, final int dy, final int x, final int y,
			final RGB c) {
		super(x, y, dx, dy, c);
		this.diameterX = dx;
		this.diameterY = dy;
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(final Graphics g) {
		g.setColor(new Color(rgbColor.red, rgbColor.green, rgbColor.blue));
		g.drawOval(x, y, diameterX, diameterY);
		g.drawString(""+getShapeNumber(), x+diameterX/2-5, y+diameterY/2+5);
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#getArea()
	 */
	@Override
	public int getArea() {
		return (int) (PI * (diameterX / 2) * (diameterY / 2));
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#setHeight(int)
	 */
	@Override
	public void setHeight(final int height) {
		super.setHeight(height);
		diameterY = height;
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#setWidth(int)
	 */
	@Override
	public void setWidth(final int width) {
		super.setWidth(width);
		diameterX = width;
	}
}
