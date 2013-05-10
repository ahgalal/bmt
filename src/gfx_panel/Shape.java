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
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;

import org.eclipse.swt.graphics.RGB;

/**
 * Parent of all Shapes. Rectangles,Ovals and others inherite from this class. a
 * Shape can be drawn on GfxPanel.
 * 
 * @author Creative
 */
public abstract class Shape {

	/**
	 * Maps color values to Strings.
	 * 
	 * @param color
	 *            Color value
	 * @return String value of the color
	 */
	public static String color2String(final Color color) {
		String res = null;
		if (color == Color.BLACK)
			res = "Black";
		else if (color == Color.BLUE)
			res = "Blue";
		else if (color == Color.GREEN)
			res = "Green";
		else if (color == Color.CYAN)
			res = "Cyan";
		else if (color == Color.GRAY)
			res = "Gray";
		else if (color == Color.MAGENTA)
			res = "Magenta";
		else if (color == Color.ORANGE)
			res = "Orange";
		else if (color == Color.PINK)
			res = "Pink";
		else if (color == Color.YELLOW)
			res = "Yellow";
		else if (color == Color.RED)
			res = "Red";
		return res;
	}

	/**
	 * Converts RGB color objects to a string value of: R G B.
	 * 
	 * @param color
	 *            color to be converted
	 * @return String describing the RGB color [0x00 B G R]
	 */
	public static String color2String(final RGB color) {
		return Integer.toString(color.red)
				+ System.getProperty("line.separator")
				+ Integer.toString(color.green)
				+ System.getProperty("line.separator")
				+ Integer.toString(color.blue);
	}

	private AttributedString attributedString;
	private int attributedStringSize;
	private String attributedStringText;
	protected AttributedString getAttributedString(int size, String string) {
		if(attributedString!=null){
			if(attributedStringSize==size && attributedStringText.equals(string))
				return attributedString;
			else{
				return createNewAttributedString(size,string);
			}
		}
		return createNewAttributedString(size,string);
	}

	private AttributedString createNewAttributedString(int size, String string) {
		attributedString = new AttributedString(string);
		attributedString.addAttribute(TextAttribute.SIZE, (int)(size));
		attributedStringSize=size;
		attributedStringText = string;
		return attributedString;
	}

	
	/**
	 * Mapes Color names to color objects.
	 * 
	 * @param strcol
	 *            String containing the color's name
	 * @return Color object corresponding to the color specified in the string
	 */
	public static Color string2Color(final String strcol) {
		Color rescolor = null;
		if (strcol.equals("Black"))
			rescolor = Color.BLACK;
		else if (strcol.equals("Blue"))
			rescolor = Color.BLUE;
		else if (strcol.equals("Green"))
			rescolor = Color.GREEN;
		else if (strcol.equals("Cyan"))
			rescolor = Color.CYAN;
		else if (strcol.equals("Gray"))
			rescolor = Color.GRAY;
		else if (strcol.equals("Magenta"))
			rescolor = Color.MAGENTA;
		else if (strcol.equals("Orange"))
			rescolor = Color.ORANGE;
		else if (strcol.equals("Pink"))
			rescolor = Color.PINK;
		else if (strcol.equals("Yellow"))
			rescolor = Color.YELLOW;
		else if (strcol.equals("Red"))
			rescolor = Color.RED;

		return rescolor;
	}

	/**
	 * array of attached shapes to this shape, they move as this shape moves.
	 */
	protected ArrayList<Shape>	arrAttachee;
	/**
	 * Color.
	 */
	protected RGB				rgbColor;

	/**
	 * side length of the selection box drawn at corners.
	 */
	protected int				selectionboxLength;

	/**
	 * Shape identifier.
	 */
	protected int				shapeNumber;

	/**
	 * Dimensions.
	 */
	protected int				x, y, width, height;

	/**
	 * Shape initialization.
	 * 
	 * @param x
	 *            top left corner of the shape in the x dimension
	 * @param y
	 *            top left corner of the shape in the y dimension
	 * @param width
	 *            shape's width (bounding box)
	 * @param height
	 *            shape's height (bounding box)
	 * @param c
	 *            shape's color
	 */
	public Shape(final int x, final int y, final int width, final int height,
			final RGB c) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rgbColor = c;
		arrAttachee = new ArrayList<Shape>();
	}

	/**
	 * Attaches another shape to this shape, when this shape moves, all its
	 * attachees are moved.
	 * 
	 * @param attachee
	 *            Shape to attach
	 */
	public void attachToMe(final Shape attachee) {
		arrAttachee.add(attachee);
	}

	/**
	 * deattach a shape from this shape.
	 * 
	 * @param attachee
	 *            Shape to deattach
	 */
	public void deattachFromMe(final Shape attachee) {
		arrAttachee.remove(attachee);
	}

	/**
	 * Draw the Shape on the graphics object.
	 * 
	 * @param g
	 *            Graphics object to draw the shape on
	 */
	public abstract void draw(Graphics g);

	public void draw(final Graphics gfx, final double xScale, final double yScale) {
	}

	/**
	 * Gets the surface area of the shape.
	 * 
	 * @return integer representing the surface area (unit is pixels)
	 */
	public abstract int getArea();

	/**
	 * Gets the color of the shape.
	 * 
	 * @return RGB color representing the color of the shape
	 */
	public RGB getColor() {
		return rgbColor;
	}

	/**
	 * Gets shape's height.
	 * 
	 * @return integer representing the height of the shape
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the shape number.
	 * 
	 * @return integer representing the shape's number
	 */
	public int getShapeNumber() {
		return shapeNumber;
	}

	/**
	 * Gets shape's width.
	 * 
	 * @return integer representing the width of the shape
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the X co-ordinate of the shape.
	 * 
	 * @return integer value representing the X co-ordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the YX co-ordinate of the shape.
	 * 
	 * @return integer value representing the Y co-ordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Moves this shape by a value deltaX. if deltaX is positive, shape moves
	 * right, else , to the left.
	 * 
	 * @param deltaX
	 *            distance to move the shape along the x-axis
	 */
	public void moveX(final int deltaX) {
		setX(x + deltaX);
	}

	/**
	 * Moves this shape by a value deltaY. if deltaY is positive, shape moves
	 * down, else , up.
	 * 
	 * @param deltaY
	 *            distance to move the shape along the y-axis
	 */
	public void moveY(final int deltaY) {
		setY(y + deltaY);
	}

	/**
	 * Draws the four selection boxes on the shape's corners.
	 * 
	 * @param gfx
	 *            Graphics object to draw on
	 * @param length
	 *            side length of the selection box
	 */
	public void select(final Graphics gfx, final int length) {
		selectionboxLength = length;
		gfx.setColor(Color.black);
		gfx.fillRect(x, y, length, length);
		gfx.fillRect(x + width - length, y, length, length);
		gfx.fillRect(x, y + height - length, length, length);
		gfx.fillRect(x + width - length, y + height - length, length, length);
	}

	/**
	 * Sets the color of the shape.
	 * 
	 * @param color
	 *            new shape's color
	 */
	public void setColor(final RGB color) {
		this.rgbColor = color;
	}

	/**
	 * ets shape's height.
	 * 
	 * @param height
	 *            integer representing the new height of the shape
	 */
	public void setHeight(final int height) {
		this.height = height;
	}

	/**
	 * Sets the shape number.
	 * 
	 * @param shapeNumber
	 *            the new shape number
	 */
	public void setShapeNumber(final int shapeNumber) {
		this.shapeNumber = shapeNumber;
	}

	/**
	 * Sets shape's width.
	 * 
	 * @param width
	 *            integer representing the new width of the shape
	 */
	public void setWidth(final int width) {
		this.width = width;
	}

	/**
	 * Move this shape to certain X position, move the attachees also.
	 * 
	 * @param x
	 *            new X value to move to
	 */
	public void setX(final int x) {
		for (final Shape att : arrAttachee)
			att.moveX(x - this.x);
		this.x = x;
	}

	/**
	 * Move this shape to certain Y position, move the attachees also.
	 * 
	 * @param y
	 *            new Y value to move to
	 */
	public void setY(final int y) {
		for (final Shape att : arrAttachee)
			att.moveY(y - this.y);
		this.y = y;
	}

}
