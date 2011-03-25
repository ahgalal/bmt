package gfx_panel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.eclipse.swt.graphics.RGB;

/**
 * Parent of all Shapes. Rectangles,Ovals and others inherite from this class. a
 * Shape can be drawn on GfxPanel.
 * 
 * @author Creative
 */
public abstract class Shape
{

	protected int shape_number; // Shape identifier
	protected int x, y, width, height; // Dimensions
	protected RGB rgb_color; // Color
	protected int selectionbox_length; // side length of the selection box drawn
	// at corners
	protected ArrayList<Shape> arr_attachee; // array of attached shapes to this

	// shape, they move as this
	// shape moves

	/**
	 * Draw the Shape on the graphics object.
	 * 
	 * @param g
	 *            Graphics object to draw the shape on
	 */
	public abstract void draw(Graphics g);

	/**
	 * Shape initialization.
	 * 
	 * @param x
	 *            top left corner of the shape in the x dimension
	 * @param y
	 *            top left corner of the shape in the y dimension
	 * @param width
	 * @param height
	 * @param c
	 *            shape's color
	 */
	public Shape(final int x, final int y, final int width, final int height, final RGB c)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rgb_color = c;
		arr_attachee = new ArrayList<Shape>();
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(final int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(final int height)
	{
		this.height = height;
	}

	public void setShapeNumber(final int shape_number)
	{
		this.shape_number = shape_number;
	}

	public int getShapeNumber()
	{
		return shape_number;
	}

	public void setColor(final RGB color)
	{
		this.rgb_color = color;
	}

	public RGB getColor()
	{
		return rgb_color;
	}

	public abstract int getArea();

	/**
	 * Draws the four selection boxes on the shape's corners.
	 * 
	 * @param gfx
	 *            Graphics object to draw on
	 * @param length
	 *            side length of the selection box
	 */
	public void select(final Graphics gfx, final int length)
	{
		selectionbox_length = length;
		gfx.setColor(Color.black);
		gfx.fillRect(x, y, length, length);
		gfx.fillRect(x + width - length, y, length, length);
		gfx.fillRect(x, y + height - length, length, length);
		gfx.fillRect(x + width - length, y + height - length, length, length);
	}

	public void deselect(final Graphics gfx)
	{

	}

	/**
	 * Maps color values to Strings.
	 * 
	 * @param color
	 *            Color value
	 * @return String value of the color
	 */
	public static String color2String(final Color color)
	{
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
	 * Mapes Color names to color objects.
	 * 
	 * @param strcol
	 *            String containing the color's name
	 * @return Color object corresponding to the color specified in the string
	 */
	public static Color string2Color(final String strcol)
	{
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
	 * Attaches another shape to this shape, when this shape moves, all its
	 * attachees are moved.
	 * 
	 * @param attachee
	 *            Shape to attach
	 */
	public void attachToMe(final Shape attachee)
	{
		arr_attachee.add(attachee);
	}

	/**
	 * deattach a shape from this shape.
	 * 
	 * @param attachee
	 *            Shape to deattach
	 */
	public void deattachFromMe(final Shape attachee)
	{
		arr_attachee.remove(attachee);
	}

	/**
	 * Converts RGB color objects to a string value of: R G B.
	 * 
	 * @param color
	 * @return
	 */
	public static String color2String(final RGB color)
	{
		return Integer.toString(color.red)
				+ System.getProperty("line.separator")
				+ Integer.toString(color.green)
				+ System.getProperty("line.separator")
				+ Integer.toString(color.blue);
	}

	/**
	 * Move this shape to certain X position, move the attachees also.
	 * 
	 * @param x
	 *            new X value to move to
	 */
	public void setX(final int x)
	{
		for (final Shape att : arr_attachee)
			att.moveX(x - this.x);
		this.x = x;
	}

	/**
	 * Moves this shape by a value delta_x. if delta_x is positive, shape moves
	 * right, else , to the left.
	 * 
	 * @param delta_x
	 *            distance to move the shape along the x-axis
	 */
	public void moveX(final int delta_x)
	{
		setX(x + delta_x);
	}

	/**
	 * Moves this shape by a value delta_y. if delta_y is positive, shape moves
	 * down, else , up.
	 * 
	 * @param delta_y
	 *            distance to move the shape along the y-axis
	 */
	public void moveY(final int delta_y)
	{
		setY(y + delta_y);
	}

	public int getX()
	{
		return x;
	}

	/**
	 * Move this shape to certain Y position, move the attachees also.
	 * 
	 * @param y
	 *            new Y value to move to
	 */
	public void setY(final int y)
	{
		for (final Shape att : arr_attachee)
			att.moveY(y - this.y);
		this.y = y;
	}

	public int getY()
	{
		return y;
	}

}
