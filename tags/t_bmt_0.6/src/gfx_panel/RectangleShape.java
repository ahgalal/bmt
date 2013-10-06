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

package gfx_panel;

import java.awt.Color;
import java.awt.Graphics;
import java.text.AttributedString;

import org.eclipse.swt.graphics.RGB;

/**
 * Rectangle Shape Class, represents a rectangle drawn on the GfxPanel.
 * 
 * @author Creative
 */
public class RectangleShape extends Shape {

	/**
	 * Initialized the Rectangle shape.
	 */
	public RectangleShape() {
		this(0, 0, 0, 0, null);
	}

	/**
	 * Initialized the Rectangle shape.
	 * 
	 * @param x
	 *            top left corner of the shape in the x dimension
	 * @param y
	 *            top left corner of the shape in the y dimension
	 * @param width
	 *            rectangle's width
	 * @param height
	 *            rectangle's height
	 * @param c
	 *            rectangle's color
	 */
	public RectangleShape(final int x, final int y, final int width,
			final int height, final RGB c) {
		super(x, y, width, height, c);
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(final Graphics g) {
		draw(g, 1, 1);
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#getArea()
	 */
	@Override
	public int getArea() {
		return width * height;
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#getHeight()
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#getWidth()
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#setHeight(int)
	 */
	@Override
	public void setHeight(final int height) {
		this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.Shape#setWidth(int)
	 */
	@Override
	public void setWidth(final int width) {
		this.width = width;
	}

	@Override
	public void draw(Graphics gfx, double xScale, double yScale) {
		gfx.setColor(new Color(rgbColor.red, rgbColor.green, rgbColor.blue));
		int xFinal = (int)(x*xScale);
		int yFinal = (int)(y*yScale);
		int widthFinal = (int)(width*xScale);
		int heightFinal = (int) (height*yScale);
		if(xScale!=1 || yScale!=1)
			gfx.drawRect(xFinal-1, yFinal-1, widthFinal+2, heightFinal+2);
		gfx.drawRect(xFinal, yFinal, widthFinal, heightFinal);
		
		AttributedString attributedString = getAttributedString((int) (xScale*12),Integer.toString(getShapeNumber()));
		gfx.drawString(attributedString.getIterator(), (int)((x+2)*xScale), (int) ((y+12)*yScale));
	}
}
