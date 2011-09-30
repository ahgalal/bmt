/***************************************************************************
 *  Copyright 2010,2011 by Ahmed Galal, Ahmed Mohammed Aly,
 *  Sarah Hamid and Mohammed Ahmed Ramadan
 *  contact: ceng.ahmedgalal@gmail.com
 *
 *  This file is part of Behavioral Monitoring Tool.
 *
 *  Behavioral Monitoring Tool is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation, version 3 of the
 *  License.
 *
 *  Behavioral Monitoring Tool is distributed in the hope that it
 *  will be useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Behavioral Monitoring Tool.
 *  If not, see <http://www.gnu.org/licenses/>.
 *   
 **************************************************************************/

package gfx_panel;

import gfx_panel.Snapper.SnapResults;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * A drawing panel that supports different shapes and many features like
 * snapping.
 * 
 * @author Creative
 */
public class GfxPanel
{
	private final ArrayList<Shape> shp_arr;
	private final Frame awt_frame;
	private final int width, height;
	private int ini_x, ini_y;
	private boolean drawing_now;
	private final Graphics gfx;
	private Shape shp_being_drawn, shp_selected, shp_to_draw;
	private final ArrayList<GfxPanelNotifiee> arr_notifiee;
	protected boolean moving/* ,snapped_x,snapped_y */;
	protected boolean resizing_x_right;
	protected boolean resizing_y_down;
	protected boolean resizing_x_left;
	protected boolean resizing_y_up;
	private Composite composite;
	private final Composite awt_composite;
	private int cursor_pos_in_shp_x;
	private int cursor_pos_in_shp_y;
	private final int[] bg;
	private final BufferedImage bg_buff_img;
	private final int img_w, img_h;
	private final Button btn_add_rect_shape;
	private final Button btn_add_circle_shape;
	private final Button chk_enable_snap;
	private final Point current_click_pos;
	private final Snapper snapper;

	/**
	 * Creates the main composite.
	 * 
	 * @param parent_shell
	 *            parent shell
	 * @param parent
	 *            parent composite
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	private void createMainComposite(
			final Shell parent_shell,
			final Composite parent,
			final int w,
			final int h)
	{
		composite = new Composite(parent, 0);
		composite.setLayout(null);
		composite.setBounds(new Rectangle(0, 0, w, h));
	}

	/**
	 * Generates a number to be used for the newly created shape as a shape
	 * number.
	 * 
	 * @return integer representing the new shape's number
	 */
	private int generateNewShapeNumber()
	{
		if (shp_arr.size() == 0)
			return 0;

		final int[] taken_numbers = new int[shp_arr.size()];
		for (int y = 0; y < shp_arr.size(); y++)
			taken_numbers[y] = shp_arr.get(y).getShapeNumber();

		iterate_i: for (int i = 0; i < 1000; i++) // we can handle up to 1000 of
		// shapes
		{
			for (final int a : taken_numbers)
			{
				if (a == i) // this i is rejected .. go to next i
					continue iterate_i;
			}
			return i;
		}
		return -1;
	}

	/**
	 * Initializes the background of the panel.
	 * 
	 * @param rgb_value
	 *            Color of the background [0x00 B G R]
	 */
	private void initializeBackground(final int rgb_value)
	{
		for (int i = 0; i < bg.length; i++)
		{
			bg[i] = rgb_value;
		}
	}

	/**
	 * Enables drawing on the panel.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void enableDraw(final boolean enable)
	{
		btn_add_rect_shape.setEnabled(enable);
		btn_add_circle_shape.setEnabled(enable);
	}

	/**
	 * Starts drawing a new shape.
	 * 
	 * @param shp
	 *            shape to start drawing
	 */
	public void startDrawingShape(final Shape shp)
	{
		shp_to_draw = shp;
	}

	/**
	 * Initializes the panel.
	 * 
	 * @param parent_shell
	 *            parent shell of the panel
	 * @param parent
	 *            parent composite of the panel
	 * @param width
	 *            panel's width
	 * @param height
	 *            panel's height (must add 35 to the image's size)
	 */
	public GfxPanel(
			final Shell parent_shell,
			final Composite parent,
			final int width,
			final int height)
	{
		this.width = width;
		this.height = height;
		current_click_pos = new Point();
		current_click_pos.x = -1;
		img_w = width;
		img_h = height - 35;

		bg_buff_img = new BufferedImage(img_w, img_h, BufferedImage.TYPE_INT_RGB);
		bg = ((DataBufferInt) bg_buff_img.getRaster().getDataBuffer()).getData();
		initializeBackground(0xFFFFFF);
		createMainComposite(parent_shell, parent, width, height);
		parent_shell.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent e)
			{
				if (e.character == java.awt.event.KeyEvent.VK_DELETE)
					deleteSelectedShape();
			}

			@Override
			public void keyPressed(final KeyEvent arg0)
			{

			}
		});

		final Button btn_setclr = new Button(composite, 0);
		btn_setclr.setBounds(10, composite.getBounds().height - 30, 80, 25);
		btn_setclr.setText("Color..");
		btn_setclr.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				if (shp_selected != null)
				{
					final ColorDialog cd = new ColorDialog(parent_shell);
					cd.open();
					shp_selected.setColor(cd.getRGB());
					refreshDrawingArea();
					notifyShapeModified(shp_selected.getShapeNumber());
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{

			}
		});

		btn_add_rect_shape = new Button(composite, 0);
		btn_add_rect_shape.setBounds(100, composite.getBounds().height - 30, 80, 25);
		btn_add_rect_shape.setText("Add Rectangle");
		btn_add_rect_shape.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				startDrawingShape(new RectangleShape());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{

			}
		});

		chk_enable_snap = new Button(composite, SWT.CHECK);
		chk_enable_snap.setBounds(280, composite.getBounds().height - 30, 110, 25);
		chk_enable_snap.setText("Enable Snapping");
		chk_enable_snap.setSelection(true);
		chk_enable_snap.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				snapper.enableSnap(chk_enable_snap.getSelection());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{
			}
		});

		btn_add_circle_shape = new Button(composite, 0);
		btn_add_circle_shape.setBounds(190, composite.getBounds().height - 30, 80, 25);
		btn_add_circle_shape.setText("Add Circle");
		btn_add_circle_shape.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e)
			{
				startDrawingShape(new OvalShape());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e)
			{

			}
		});

		awt_composite = new Composite(composite, SWT.BORDER | SWT.EMBEDDED);
		awt_composite.setBounds(
				0,
				0,
				composite.getBounds().width,
				composite.getBounds().height - 35);

		awt_frame = SWT_AWT.new_Frame(awt_composite);
		gfx = awt_frame.getGraphics();
		shp_arr = new ArrayList<Shape>();
		snapper = new Snapper(shp_arr);
		arr_notifiee = new ArrayList<GfxPanelNotifiee>();

		awt_frame.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(final MouseEvent e)
			{
				final int x = e.getX(), y = e.getY();
				if (drawing_now)
				{
					drawing_now = false;
					final int new_shape_number = addShape(shp_being_drawn);
					notifyShapeAdded(new_shape_number);
					shp_being_drawn = null;
				}
				/**
				 * the next code is for resizing or moving
				 */
				else if (resizing_x_right
						| resizing_y_down
						| resizing_x_left
						| resizing_y_up
						| moving)
				{
					resizing_x_right = resizing_y_down = resizing_x_left = resizing_y_up = moving = false;
					notifyShapeModified(shp_selected.getShapeNumber());

					final Shape dragged_on_shape = getDraggedOnShape(x, y);
					notifyTargettedDragOperation(shp_selected, dragged_on_shape);
				}
				else
				{
					current_click_pos.x = e.getX();
					current_click_pos.y = e.getY();

					notifyMouseClick(current_click_pos);
				}
			}

			@Override
			public void mousePressed(final MouseEvent e)
			{
				ini_x = e.getX();
				ini_y = e.getY();

				if (shp_to_draw != null)
				{
					shp_being_drawn = shp_to_draw;
					shp_being_drawn.setX(ini_x);
					shp_being_drawn.setY(ini_y);
					shp_being_drawn.setColor(new RGB(0, 0, 0));
					drawing_now = true;
					shp_to_draw = null;
					shp_selected = null;
				}
				else
				{
					shp_selected = getShapeByPosition(ini_x, ini_y);
					if (shp_selected != null)
					{
						notifyShapeSelected(shp_selected.getShapeNumber());
						cursor_pos_in_shp_x = e.getX() - shp_selected.getX();
						cursor_pos_in_shp_y = e.getY() - shp_selected.getY();
					}
					refreshDrawingArea();
				}
			}

			@Override
			public void mouseExited(final MouseEvent arg0)
			{

			}

			@Override
			public void mouseEntered(final MouseEvent arg0)
			{

			}

			@Override
			public void mouseClicked(final MouseEvent arg0)
			{

			}
		});

		awt_frame.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(final MouseEvent e)
			{

			}

			@Override
			public void mouseDragged(final MouseEvent e)
			{
				// Drawing:
				if (drawing_now)
				{
					final int x = e.getX(), y = e.getY();
					updateNewShape(shp_being_drawn, x, y);
					refreshDrawingArea();
					shp_being_drawn.draw(gfx);
				}
				// Modifying:
				else if (shp_selected != null)
				{
					// Resizing:
					if (!moving && isResizing(shp_selected, e.getX(), e.getY()))
						resizeShape(shp_selected, e.getX(), e.getY());
					// Moving:
					else
						moveShape(shp_selected, e.getX(), e.getY());
				}
			}
		});

	}

	/**
	 * Moves the selected shape according to the cursor movement (taking
	 * snapping into account if enabled).
	 * 
	 * @param shp
	 *            shape to move
	 * @param x
	 *            cursor's position on the x axis
	 * @param y
	 *            cursor's position on the y axis
	 */
	private void moveShape(final Shape shp, final int x, final int y)
	{
		moving = true;
		SnapResults snp_res;
		snp_res = snapper.prepareSnapPosition(
				shp_selected,
				x,
				y,
				cursor_pos_in_shp_x,
				cursor_pos_in_shp_y);
		if (!snp_res.snapped_x)
			shp.setX(x - cursor_pos_in_shp_x);
		if (!snp_res.snapped_y)
			shp.setY(y - cursor_pos_in_shp_y);
		refreshDrawingArea();
	}

	/**
	 * Gets the shape being dragged on.
	 * 
	 * @param x
	 *            cursor's position on the x axis
	 * @param y
	 *            cursor's position on the y axis
	 * @return shape being dragged on
	 */
	private Shape getDraggedOnShape(final int x, final int y)
	{
		final Shape[] possible_shapes = getShapesByPosition(x, y);
		for (int i = 0; i < possible_shapes.length; i++)
			if (possible_shapes[i] == shp_selected)
				possible_shapes[i] = null;

		return getShapeWithleastArea(possible_shapes);
	}

	/**
	 * Notifies the Notifiees about the dragging operation.
	 * 
	 * @param shp_dragged
	 *            shape dragged
	 * @param shp_dragged_on
	 *            shape being dragged on
	 */
	private void notifyTargettedDragOperation(
			final Shape shp_dragged,
			final Shape shp_dragged_on)
	{
		int dragtarget = -1;
		if (shp_dragged_on != null)
			dragtarget = shp_dragged_on.getShapeNumber();
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.dragOccured(shp_dragged.getShapeNumber(), dragtarget);
		}
	}

	/**
	 * Determines if the active shape is being resized.
	 * 
	 * @param shp
	 *            active shape
	 * @param x
	 *            cursor's position on the x axis
	 * @param y
	 *            cursor's position on the y axis
	 * @return true: resizing, false: not resizing
	 */
	private boolean isResizing(final Shape shp, final int x, final int y)
	{
		final int sel_x = shp.getX(), sel_y = shp.getY();
		int width = 0, height = 0;
		width = shp.getWidth();
		height = shp.getHeight();
		resizing_x_left = resizing_x_left
				| ((Math.abs(x - (sel_x)) < 5) & (y < (sel_y + height)) & (y > sel_y));
		resizing_y_up = resizing_y_up
				| ((Math.abs(y - (sel_y)) < 5) & (x < (sel_x + width)) & (x > sel_x));
		resizing_x_right = resizing_x_right
				| ((Math.abs(x - (sel_x + width)) < 5) & (y < (sel_y + height)) & (y > sel_y));
		resizing_y_down = resizing_y_down
				| ((Math.abs(y - (sel_y + height)) < 5) & (x < (sel_x + width)) & (x > sel_x));

		return (resizing_x_right | resizing_y_down | resizing_x_left | resizing_y_up);
	}

	/**
	 * Resizes the shape.
	 * 
	 * @param shp
	 *            shape to resize
	 * @param x
	 *            cursor's position on the x axis
	 * @param y
	 *            cursor's position on the y axis
	 */
	private void resizeShape(final Shape shp, final int x, final int y)
	{
		final int sel_x = shp.getX(), sel_y = shp.getY();

		final int w = shp.getWidth(), h = shp.getHeight();
		SnapResults snp_res;
		snp_res = snapper.prepareSnapSize(shp_selected, x, y);
		if (!snp_res.snapped_x)
		{
			if (resizing_x_right)
			{
				shp.setWidth(x - sel_x);
			}
			if (resizing_x_left)
			{
				shp.setX(x);
				shp.setWidth(w + sel_x - x);
			}
		}
		if (!snp_res.snapped_y)
		{
			if (resizing_y_down)
			{
				shp.setHeight(y - sel_y);
			}

			if (resizing_y_up)
			{
				shp.setY(y);
				shp.setHeight(h + sel_y - y);
			}
		}
		refreshDrawingArea();
	}

	/**
	 * Refreshed the new shape that is being drawn. (it is not added yet to the
	 * shapes collection, so we must refresh it explicitly here).
	 * 
	 * @param shp
	 *            active shape
	 * @param x
	 *            cursor's position on the x axis
	 * @param y
	 *            cursor's position on the y axis
	 */
	private void updateNewShape(final Shape shp, final int x, final int y)
	{
		if (x > ini_x)
			shp.setWidth(x - ini_x);
		if (x < ini_x)
		{
			shp.setX(x);
			shp.setWidth(ini_x - x);
		}

		if (y > ini_y)
			shp.setHeight(y - ini_y);
		if (y < ini_y)
		{
			shp.setY(y);
			shp.setHeight(ini_y - y);
		}
	}

	/**
	 * Clears the panel.
	 */
	public void clearDrawingArea()
	{
		gfx.clearRect(0, 0, width, height);
	}

	/**
	 * Redraws all the shapes.
	 */
	public void redrawAllShapes()
	{
		final int cleaning_margin = 30;
		int x_left = 0, x_right = img_w, y_up = 0, y_down = img_h;
		if (resizing_x_left | resizing_x_right | resizing_y_down | resizing_y_up | moving)
		{
			x_left = shp_selected.getX() - cleaning_margin;
			x_right = shp_selected.getX() + shp_selected.getWidth() + cleaning_margin;
			y_up = shp_selected.getY() - cleaning_margin;
			y_down = shp_selected.getY() + shp_selected.getHeight() + cleaning_margin;
			if (x_left < 0)
				x_left = 0;
			if (x_right > bg_buff_img.getWidth())
				x_right = bg_buff_img.getWidth();
			if (y_up < 0)
				y_up = 0;
			if (y_down > bg_buff_img.getHeight())
				y_down = bg_buff_img.getHeight();
		}
		gfx.drawImage(
				bg_buff_img,
				x_left,
				y_up,
				x_right + x_left,
				y_down + y_up,
				x_left,
				y_up,
				x_right + x_left,
				y_down + y_up,
				null);

		for (final Shape sh : shp_arr)
			sh.draw(gfx);
	}

	/**
	 * Adds a new shape to the collection.
	 * 
	 * @param shp
	 *            new shape
	 * @return integer representing the new shape's number
	 */
	private int addShape(final Shape shp)
	{
		final int new_shp_number = generateNewShapeNumber();
		shp.setShapeNumber(new_shp_number);
		shp_arr.add(shp);
		return new_shp_number;
	}

	/**
	 * Registers a new Notifiee to receive notifications from us.
	 * 
	 * @param notifiee
	 *            object will be notified with events occurring to the panel
	 */
	public void registerForNotifications(final GfxPanelNotifiee notifiee)
	{
		arr_notifiee.add(notifiee);
	}

	/**
	 * Notifies all Notifiees of the newly added shape.
	 * 
	 * @param shape_number
	 *            number of the new shape
	 */
	private void notifyShapeAdded(final int shape_number)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.shapeAdded(shape_number);
		}
	}

	/**
	 * Notifies all the Notifiess of a mouse click.
	 * 
	 * @param pos
	 *            position of the mouse click with respect to the panel's top
	 *            left
	 */
	private void notifyMouseClick(final Point pos)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.mouseClicked(pos);
		}
	}

	/**
	 * Notifies all the Notifiees of the modified shape.
	 * 
	 * @param shape_number
	 *            number of the modified shape
	 */
	private void notifyShapeModified(final int shape_number)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.shapeModified(shape_number);
		}
	}

	/**
	 * Notifies all the Notifiees of the deleted shape.
	 * 
	 * @param shape_number
	 *            number of the deleted shape
	 */
	private void notifyShapeDeleted(final int shape_number)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.shapeDeleted(shape_number);
		}
	}

	/**
	 * Notifies all the Notifiees of the selected shape.
	 * 
	 * @param shape_number
	 *            number of the selected shape
	 */
	private void notifyShapeSelected(final int shape_number)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.shapeSelected(shape_number);
		}
	}

	/**
	 * Gets the shapes existing on the specified pixel position.
	 * 
	 * @param x
	 *            x axis position to get the shapes existing there
	 * @param y
	 *            y axis position to get the shapes existing there
	 * @return array of shapes existing on the specified pixel
	 */
	private Shape[] getShapesByPosition(final int x, final int y)
	{
		final ArrayList<Shape> possible_shps = new ArrayList<Shape>();

		for (final Shape shp : shp_arr)
		{
			if (x > shp.getX()
					&& x < (shp.getX() + shp.getWidth())
					&& y > shp.getY()
					&& y < (shp.getY() + shp.getHeight()))
			{
				// return tmp_rect; //return the "FIRST" rectangle that meets
				// the condition!!
				possible_shps.add(shp);
			}
		}
		final Shape[] returned_array = new Shape[possible_shps.size()];
		possible_shps.toArray(returned_array);
		return returned_array; // returns all the shapes existing in this
		// position
	}

	/**
	 * Gets the shape existing on the specified pixel position.
	 * 
	 * @param x
	 *            x axis position to get the shape existing there
	 * @param y
	 *            y axis position to get the shape existing there
	 * @return shape existing on the specified pixel
	 */
	private Shape getShapeByPosition(final int x, final int y)
	{
		final Shape[] possible_shps = getShapesByPosition(x, y);
		return getShapeWithleastArea(possible_shps);
	}

	/**
	 * Gets the shape having the least area compared to the others shapes in the
	 * array.
	 * 
	 * @param possible_shps
	 *            array of shapes to get the least area shape from
	 * @return shape having the least area
	 */
	private Shape getShapeWithleastArea(final Shape[] possible_shps)
	{
		Shape tmp_shp = null;
		int leastarea = 1000000;
		for (final Shape shp : possible_shps)
		{
			if (shp != null)
				if (shp.getArea() < leastarea)
				{
					leastarea = shp.getArea();
					tmp_shp = shp;
				}
		}
		return tmp_shp; // returns the shape having the smallest area
	}

	/**
	 * Refreshes the drawing area of the panel.
	 */
	public void refreshDrawingArea()
	{
		redrawAllShapes();
		if (shp_selected != null)
			draw4Corners(shp_selected, 5);
	}

	/**
	 * Deleted a shape.
	 * 
	 * @param shp
	 *            shape to be deleted
	 */
	public void deleteShape(final Shape shp)
	{
		shp_arr.remove(shp);
	}

	/**
	 * Attaches the "attachedshape" to the "mainshape".
	 * 
	 * @param attachedshape
	 *            shape to be attached to the "mainshape", it will be moved with
	 *            the main shape
	 * @param mainshape
	 *            shape to attach to
	 */
	public void attachShapes(final int attachedshape, final int mainshape)
	{
		getShapeByNumber(mainshape).attachToMe(getShapeByNumber(attachedshape));
	}

	/**
	 * Detaches two shapes.
	 * 
	 * @param mainshape
	 *            main shape
	 * @param attachedshape
	 *            attached shape to the main shape
	 */
	public void deattachShapes(final int mainshape, final int attachedshape)
	{
		getShapeByNumber(mainshape).deattachFromMe(getShapeByNumber(attachedshape));
	}

	/**
	 * Deleted the selected shape.
	 * 
	 * @return true: success, false: failure
	 */
	public boolean deleteSelectedShape()
	{
		if (shp_selected != null)
		{
			shp_arr.remove(shp_selected);
			notifyShapeDeleted(shp_selected.getShapeNumber());
			shp_selected = null;
			refreshDrawingArea();
			return true;
		}
		return false;
	}

	/**
	 * Draws the selection corners for the specified shape.
	 * 
	 * @param shp
	 *            shape to draw selection corners to
	 * @param length
	 *            length of the side of each selection corner box
	 */
	private void draw4Corners(final Shape shp, final int length)
	{
		shp.select(gfx, length);
	}

	/**
	 * Gets the collection of shapes.
	 * 
	 * @return arraylist containing all the shapes on the panel
	 */
	public ArrayList<Shape> getShapeArray()
	{
		return shp_arr;
	}

	/**
	 * Gets a shape using its number.
	 * 
	 * @param shapenumber
	 *            number of the shape to get
	 * @return shape having the specified number
	 */
	public Shape getShapeByNumber(final int shapenumber)
	{
		for (int i = 0; i < shp_arr.size(); i++)
			if (shp_arr.get(i).getShapeNumber() == shapenumber)
				return shp_arr.get(i);
		return null;
	}

	/**
	 * Gets the number of the selected shape.
	 * 
	 * @return integer representing the number of the selected shape
	 */
	public int getSelectedShapeNumber()
	{
		if (shp_selected != null)
		{
			return shp_selected.getShapeNumber();
		}
		return -100;
	}

	/**
	 * Selects a shape.
	 * 
	 * @param shape_number
	 *            number of the shape to select
	 */
	public void selectShape(final int shape_number)
	{
		shp_selected = getShapeByNumber(shape_number);
		refreshDrawingArea();
	}

	/**
	 * Sets the background of the panel to the given image.
	 * 
	 * @param bg_data
	 *            new background image
	 */
	public void setBackground(final int[] bg_data)
	{
		System.arraycopy(bg_data, 0, bg, 0, bg_data.length);
	}

	/**
	 * Enables/Disables snapping.
	 * 
	 * @param enable_snap
	 *            true/false
	 */
	public void setEnableSnap(final boolean enable_snap)
	{
		chk_enable_snap.setSelection(enable_snap);
		snapper.enableSnap(enable_snap);
	}

	/**
	 * Direction, used in snapping and maybe othe stuff in the future.
	 * 
	 * @author Creative
	 */
	public enum Direction
	{
		/**
		 * the four directions.
		 */
		LEFT, RIGHT, UP, DOWN;
	}

}
