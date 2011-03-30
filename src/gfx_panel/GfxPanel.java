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

	private void initializeBackground(final int rgb_value)
	{
		for (int i = 0; i < bg.length; i++)
		{
			bg[i] = rgb_value;
		}
	}

	public void enableDraw(final boolean en)
	{
		btn_add_rect_shape.setEnabled(en);
		btn_add_circle_shape.setEnabled(en);
	}

	public boolean startDrawingShape(final Shape shp)
	{
		shp_to_draw = shp;
		return true;
	}

	public GfxPanel(
			final Shell parent_shell,
			final Composite parent,
			final int width_,
			final int height_)
	{
		this.width = width_;
		this.height = height_;
		current_click_pos = new Point();
		current_click_pos.x = -1;
		img_w = width_;
		img_h = height_ - 35;

		bg_buff_img = new BufferedImage(img_w, img_h, BufferedImage.TYPE_INT_RGB);
		bg = ((DataBufferInt) bg_buff_img.getRaster().getDataBuffer()).getData();
		initializeBackground(0xFFFFFF);
		createMainComposite(parent_shell, parent, width_, height_);
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
				} else
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
				} else
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
						resizingShape(shp_selected, e.getX(), e.getY());
					// Moving:
					else
						movingShape(shp_selected, e.getX(), e.getY());
				}
			}
		});

	}

	private void movingShape(final Shape tmp_shp, final int x, final int y)
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
			tmp_shp.setX(x - cursor_pos_in_shp_x);
		if (!snp_res.snapped_y)
			tmp_shp.setY(y - cursor_pos_in_shp_y);
		refreshDrawingArea();
	}

	private Shape getDraggedOnShape(final int x, final int y)
	{
		final Shape[] possible_shapes = getShapesByPosition(x, y);
		for (int i = 0; i < possible_shapes.length; i++)
			if (possible_shapes[i] == shp_selected)
				possible_shapes[i] = null;

		return getShapeWithleastArea(possible_shapes);
	}

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

	private boolean isResizing(final Shape tmp_sh, final int x, final int y)
	{
		final int sel_x = tmp_sh.getX(), sel_y = tmp_sh.getY();
		int width = 0, height = 0;
		width = tmp_sh.getWidth();
		height = tmp_sh.getHeight();
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

	private void resizingShape(final Shape tmp_sh, final int x, final int y)
	{
		final int sel_x = tmp_sh.getX(), sel_y = tmp_sh.getY();

		final int w = tmp_sh.getWidth(), h = tmp_sh.getHeight();
		SnapResults snp_res;
		snp_res = snapper.prepareSnapSize(shp_selected, x, y);
		if (!snp_res.snapped_x)
		{
			if (resizing_x_right)
			{
				tmp_sh.setWidth(x - sel_x);
			}
			if (resizing_x_left)
			{
				tmp_sh.setX(x);
				tmp_sh.setWidth(w + sel_x - x);
			}
		}
		if (!snp_res.snapped_y)
		{
			if (resizing_y_down)
			{
				tmp_sh.setHeight(y - sel_y);
			}

			if (resizing_y_up)
			{
				tmp_sh.setY(y);
				tmp_sh.setHeight(h + sel_y - y);
			}
		}
		refreshDrawingArea();
	}

	private void updateNewShape(final Shape tmp_shp, final int x, final int y)
	{
		if (x > ini_x)
			tmp_shp.setWidth(x - ini_x);
		if (x < ini_x)
		{
			tmp_shp.setX(x);
			tmp_shp.setWidth(ini_x - x);
		}

		if (y > ini_y)
			tmp_shp.setHeight(y - ini_y);
		if (y < ini_y)
		{
			tmp_shp.setY(y);
			tmp_shp.setHeight(ini_y - y);
		}
	}

	public void clearDrawingArea()
	{
		gfx.clearRect(0, 0, width, height);
	}

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

	private int addShape(final Shape shp)
	{
		final int new_shp_number = generateNewShapeNumber();
		shp.setShapeNumber(new_shp_number);
		shp_arr.add(shp);
		return new_shp_number;
	}

	public void registerForNotifications(final GfxPanelNotifiee notifiee)
	{
		arr_notifiee.add(notifiee);
	}

	private void notifyShapeAdded(final int shape_number)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.shapeAdded(shape_number);
		}
	}

	private void notifyMouseClick(final Point pos)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.mouseClicked(pos);
		}
	}

	private void notifyShapeModified(final int shape_number)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.shapeModified(shape_number);
		}
	}

	private void notifyShapeDeleted(final int shape_number)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.shapeDeleted(shape_number);
		}
	}

	private void notifyShapeSelected(final int shape_number)
	{
		for (final GfxPanelNotifiee notifiee : arr_notifiee)
		{
			notifiee.shapeSelected(shape_number);
		}
	}

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

	private Shape getShapeByPosition(final int x, final int y)
	{
		final Shape[] possible_shps = getShapesByPosition(x, y);
		return getShapeWithleastArea(possible_shps);
	}

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

	public void refreshDrawingArea()
	{
		// clearDrawingArea();
		redrawAllShapes();
		if (shp_selected != null)
			draw4Corners(shp_selected, 5);
	}

	public void deleteShape(final Shape shp)
	{
		shp_arr.remove(shp);
	}

	public void attachShapes(final int attachedshape, final int mainshape)
	{
		getShapeByNumber(mainshape).attachToMe(getShapeByNumber(attachedshape));
	}

	public boolean deattachShapes(final int mainshape, final int attachedshape)
	{
		getShapeByNumber(mainshape).deattachFromMe(getShapeByNumber(attachedshape));
		return false;
	}

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

	private void draw4Corners(final Shape shp, final int l)
	{
		shp.select(gfx, l);
	}

	public ArrayList<Shape> getShapeArray()
	{
		return shp_arr;
	}

	public Shape getShapeByNumber(final int shapenumber)
	{
		for (int i = 0; i < shp_arr.size(); i++)
			if (shp_arr.get(i).getShapeNumber() == shapenumber)
				return shp_arr.get(i);
		return null;
	}

	public int getSelectedShapeNumber()
	{
		if (shp_selected != null)
		{
			return shp_selected.getShapeNumber();
		}
		return -100;
	}

	public void selectShape(final int shape_number)
	{
		shp_selected = getShapeByNumber(shape_number);
		refreshDrawingArea();
	}

	public void setBackground(final int[] bg_data)
	{
		System.arraycopy(bg_data, 0, bg, 0, bg_data.length);
	}

	public void setEnable_snap(final boolean enable_snap)
	{
		chk_enable_snap.setSelection(enable_snap);
		snapper.enableSnap(enable_snap);
	}

	public enum Direction
	{
		LEFT, RIGHT, UP, DOWN;
	}

}
