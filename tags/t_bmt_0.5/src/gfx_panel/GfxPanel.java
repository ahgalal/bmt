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
public class GfxPanel {
	/**
	 * Direction, used in snapping and maybe othe stuff in the future.
	 * 
	 * @author Creative
	 */
	public enum Direction {
		DOWN, /**
		 * the four directions.
		 */
		LEFT, RIGHT, UP;
	}

	private final ArrayList<GfxPanelNotifiee>	arrNotifiee;
	private final Composite						awtComposite;
	private final Frame							awtFrame;
	private final int[]							bg;
	private final BufferedImage					bgBuffImg;
	private final Button						btnAddCircleShape;
	private final Button						btnAddRectShape;
	private final Button						chkEnableSnap;
	private Composite							composite;
	private final Point							currentClickPos;
	private int									cursorPosInShpX;
	private int									cursorPosOnShpY;
	private boolean								drawingNow;
	private final Graphics						gfx;
	private final int							imgW, imgH;
	private int									iniX, iniY;
	protected boolean							moving;
	protected boolean							resizingLeftX;
	protected boolean							resizingRightX;
	protected boolean							resizingDownY;
	protected boolean							resizingUpY;
	private final ArrayList<Shape>				shapes;
	private Shape								shpBeingDrawn, shpSelected,
			shpToDraw;
	private final Snapper						snapper;

	private final int							width, height;

	/**
	 * Initializes the panel.
	 * 
	 * @param parentShell
	 *            parent shell of the panel
	 * @param parent
	 *            parent composite of the panel
	 * @param width
	 *            panel's width
	 * @param height
	 *            panel's height (must add 35 to the image's size)
	 */
	public GfxPanel(final Shell parentShell, final Composite parent,
			final int width, final int height) {
		this.width = width;
		this.height = height;
		currentClickPos = new Point();
		currentClickPos.x = -1;
		imgW = width;
		imgH = height - 35;

		bgBuffImg = new BufferedImage(imgW, imgH,
				BufferedImage.TYPE_INT_RGB);
		bg = ((DataBufferInt) bgBuffImg.getRaster().getDataBuffer())
				.getData();
		initializeBackground(0xFFFFFF);
		createMainComposite(parentShell, parent, width, height);
		parentShell.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(final KeyEvent arg0) {

			}

			@Override
			public void keyReleased(final KeyEvent e) {
				if (e.character == java.awt.event.KeyEvent.VK_DELETE)
					deleteSelectedShape();
			}
		});

		final Button btnSetColor = new Button(composite, 0);
		btnSetColor.setBounds(10, composite.getBounds().height - 30, 80, 25);
		btnSetColor.setText("Color..");
		btnSetColor.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (shpSelected != null) {
					final ColorDialog cd = new ColorDialog(parentShell);
					cd.open();
					shpSelected.setColor(cd.getRGB());
					refreshDrawingArea();
					notifyShapeModified(shpSelected.getShapeNumber());
				}
			}
		});

		btnAddRectShape = new Button(composite, 0);
		btnAddRectShape.setBounds(100, composite.getBounds().height - 30,
				80, 25);
		btnAddRectShape.setText("Add Rectangle");
		btnAddRectShape.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				startDrawingShape(new RectangleShape());
			}
		});

		chkEnableSnap = new Button(composite, SWT.CHECK);
		chkEnableSnap.setBounds(280, composite.getBounds().height - 30, 110,
				25);
		chkEnableSnap.setText("Enable Snapping");
		chkEnableSnap.setSelection(true);
		chkEnableSnap.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				snapper.enableSnap(chkEnableSnap.getSelection());
			}
		});

		btnAddCircleShape = new Button(composite, 0);
		btnAddCircleShape.setBounds(190, composite.getBounds().height - 30,
				80, 25);
		btnAddCircleShape.setText("Add Circle");
		btnAddCircleShape.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}

			@Override
			public void widgetSelected(final SelectionEvent e) {
				startDrawingShape(new OvalShape());
			}
		});

		awtComposite = new Composite(composite, SWT.BORDER | SWT.EMBEDDED);
		awtComposite.setBounds(0, 0, composite.getBounds().width,
				composite.getBounds().height - 35);

		awtFrame = SWT_AWT.new_Frame(awtComposite);
		gfx = awtFrame.getGraphics();
		shapes = new ArrayList<Shape>();
		snapper = new Snapper(shapes);
		arrNotifiee = new ArrayList<GfxPanelNotifiee>();

		awtFrame.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(final MouseEvent arg0) {

			}

			@Override
			public void mouseExited(final MouseEvent arg0) {

			}

			@Override
			public void mousePressed(final MouseEvent e) {
				iniX = e.getX();
				iniY = e.getY();

				if (shpToDraw != null) {
					shpBeingDrawn = shpToDraw;
					shpBeingDrawn.setX(iniX);
					shpBeingDrawn.setY(iniY);
					shpBeingDrawn.setColor(new RGB(0, 0, 0));
					drawingNow = true;
					shpToDraw = null;
					shpSelected = null;
				} else {
					shpSelected = getShapeByPosition(iniX, iniY);
					if (shpSelected != null) {
						notifyShapeSelected(shpSelected.getShapeNumber());
						cursorPosInShpX = e.getX() - shpSelected.getX();
						cursorPosOnShpY = e.getY() - shpSelected.getY();
					}
					refreshDrawingArea();
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				final int x = e.getX(), y = e.getY();
				if (drawingNow) {
					drawingNow = false;
					final int newShapeNumber = addShape(shpBeingDrawn);
					notifyShapeAdded(newShapeNumber);
					shpBeingDrawn = null;
				}
				/**
				 * the next code is for resizing or moving
				 */
				else if (resizingRightX | resizingDownY | resizingLeftX
						| resizingUpY | moving) {
					resizingRightX = resizingDownY = resizingLeftX = resizingUpY = moving = false;
					notifyShapeModified(shpSelected.getShapeNumber());

					final Shape draggedOnShape = getDraggedOnShape(x, y);
					notifyTargettedDragOperation(shpSelected, draggedOnShape);
				}
				currentClickPos.x = e.getX();
				currentClickPos.y = e.getY();
				notifyMouseClick(currentClickPos);
			}
		});

		awtFrame.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(final MouseEvent e) {
				// Drawing:
				if (drawingNow) {
					final int x = e.getX(), y = e.getY();
					updateNewShape(shpBeingDrawn, x, y);
					refreshDrawingArea();
					shpBeingDrawn.draw(gfx);
				}
				// Modifying:
				else if (shpSelected != null)
					// Resizing:
					if (!moving && isResizing(shpSelected, e.getX(), e.getY()))
						resizeShape(shpSelected, e.getX(), e.getY());
					// Moving:
					else
						moveShape(shpSelected, e.getX(), e.getY());
			}

			@Override
			public void mouseMoved(final MouseEvent e) {

			}
		});

	}

	/**
	 * Adds a new shape to the collection.
	 * 
	 * @param shp
	 *            new shape
	 * @return integer representing the new shape's number
	 */
	private int addShape(final Shape shp) {
		final int newShpNumber = generateNewShapeNumber();
		shp.setShapeNumber(newShpNumber);
		shapes.add(shp);
		return newShpNumber;
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
	public void attachShapes(final int attachedshape, final int mainshape) {
		getShapeByNumber(mainshape).attachToMe(getShapeByNumber(attachedshape));
	}

	/**
	 * Clears the panel.
	 */
	public void clearDrawingArea() {
		gfx.clearRect(0, 0, width, height);
	}

	/**
	 * Creates the main composite.
	 * 
	 * @param parentShell
	 *            parent shell
	 * @param parent
	 *            parent composite
	 * @param w
	 *            width
	 * @param h
	 *            height
	 */
	private void createMainComposite(final Shell parentShell,
			final Composite parent, final int w, final int h) {
		composite = new Composite(parent, 0);
		composite.setLayout(null);
		composite.setBounds(new Rectangle(0, 0, w, h));
	}

	/**
	 * Detaches two shapes.
	 * 
	 * @param mainshape
	 *            main shape
	 * @param attachedshape
	 *            attached shape to the main shape
	 */
	public void deattachShapes(final int mainshape, final int attachedshape) {
		getShapeByNumber(mainshape).deattachFromMe(
				getShapeByNumber(attachedshape));
	}

	/**
	 * Deleted the selected shape.
	 * 
	 * @return true: success, false: failure
	 */
	public boolean deleteSelectedShape() {
		if (shpSelected != null) {
			shapes.remove(shpSelected);
			notifyShapeDeleted(shpSelected.getShapeNumber());
			shpSelected = null;
			refreshDrawingArea();
			return true;
		}
		return false;
	}

	/**
	 * Deleted a shape.
	 * 
	 * @param shp
	 *            shape to be deleted
	 */
	public void deleteShape(final Shape shp) {
		shapes.remove(shp);
	}

	/**
	 * Draws the selection corners for the specified shape.
	 * 
	 * @param shp
	 *            shape to draw selection corners to
	 * @param length
	 *            length of the side of each selection corner box
	 */
	private void draw4Corners(final Shape shp, final int length) {
		shp.select(gfx, length);
	}

	/**
	 * Enables drawing on the panel.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void enableDraw(final boolean enable) {
		btnAddRectShape.setEnabled(enable);
		btnAddCircleShape.setEnabled(enable);
	}

	/**
	 * Generates a number to be used for the newly created shape as a shape
	 * number.
	 * 
	 * @return integer representing the new shape's number
	 */
	private int generateNewShapeNumber() {
		if (shapes.size() == 0)
			return 0;

		final int[] takenNumbers = new int[shapes.size()];
		for (int y = 0; y < shapes.size(); y++)
			takenNumbers[y] = shapes.get(y).getShapeNumber();

		iterateI: for (int i = 0; i < 1000; i++) // we can handle up to 1000 of
		// shapes
		{
			for (final int a : takenNumbers)
				if (a == i) // this i is rejected .. go to next i
					continue iterateI;
			return i;
		}
		return -1;
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
	private Shape getDraggedOnShape(final int x, final int y) {
		final Shape[] possibleShapes = getShapesByPosition(x, y);
		for (int i = 0; i < possibleShapes.length; i++)
			if (possibleShapes[i] == shpSelected)
				possibleShapes[i] = null;

		return getShapeWithleastArea(possibleShapes);
	}

	/**
	 * Gets the number of the selected shape.
	 * 
	 * @return integer representing the number of the selected shape
	 */
	public int getSelectedShapeNumber() {
		if (shpSelected != null)
			return shpSelected.getShapeNumber();
		return -100;
	}

	/**
	 * Gets the collection of shapes.
	 * 
	 * @return arraylist containing all the shapes on the panel
	 */
	public ArrayList<Shape> getShapeArray() {
		return shapes;
	}

	/**
	 * Gets a shape using its number.
	 * 
	 * @param shapenumber
	 *            number of the shape to get
	 * @return shape having the specified number
	 */
	public Shape getShapeByNumber(final int shapenumber) {
		for (int i = 0; i < shapes.size(); i++)
			if (shapes.get(i).getShapeNumber() == shapenumber)
				return shapes.get(i);
		return null;
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
	private Shape getShapeByPosition(final int x, final int y) {
		final Shape[] possibleShapes = getShapesByPosition(x, y);
		return getShapeWithleastArea(possibleShapes);
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
	private Shape[] getShapesByPosition(final int x, final int y) {
		final ArrayList<Shape> possibleShapes = new ArrayList<Shape>();

		for (final Shape shp : shapes)
			if ((x > shp.getX()) && (x < (shp.getX() + shp.getWidth()))
					&& (y > shp.getY()) && (y < (shp.getY() + shp.getHeight())))
				possibleShapes.add(shp);
		final Shape[] returnedArray = new Shape[possibleShapes.size()];
		possibleShapes.toArray(returnedArray);
		return returnedArray; // returns all the shapes existing in this
		// position
	}

	/**
	 * Gets the shape having the least area compared to the others shapes in the
	 * array.
	 * 
	 * @param possibleShapes
	 *            array of shapes to get the least area shape from
	 * @return shape having the least area
	 */
	private Shape getShapeWithleastArea(final Shape[] possibleShapes) {
		Shape tmpShp = null;
		int leastarea = 1000000;
		for (final Shape shp : possibleShapes)
			if (shp != null)
				if (shp.getArea() < leastarea) {
					leastarea = shp.getArea();
					tmpShp = shp;
				}
		return tmpShp; // returns the shape having the smallest area
	}

	/**
	 * Initializes the background of the panel.
	 * 
	 * @param rgbValue
	 *            Color of the background [0x00 B G R]
	 */
	private void initializeBackground(final int rgbValue) {
		for (int i = 0; i < bg.length; i++)
			bg[i] = rgbValue;
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
	private boolean isResizing(final Shape shp, final int x, final int y) {
		final int selX = shp.getX(), selY = shp.getY();
		int width = 0, height = 0;
		width = shp.getWidth();
		height = shp.getHeight();
		resizingLeftX = resizingLeftX
				| ((Math.abs(x - (selX)) < 5) & (y < (selY + height)) & (y > selY));
		resizingUpY = resizingUpY
				| ((Math.abs(y - (selY)) < 5) & (x < (selX + width)) & (x > selX));
		resizingRightX = resizingRightX
				| ((Math.abs(x - (selX + width)) < 5) & (y < (selY + height)) & (y > selY));
		resizingDownY = resizingDownY
				| ((Math.abs(y - (selY + height)) < 5) & (x < (selX + width)) & (x > selX));

		return (resizingRightX | resizingDownY | resizingLeftX | resizingUpY);
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
	private void moveShape(final Shape shp, final int x, final int y) {
		moving = true;
		SnapResults snpRes;
		snpRes = snapper.prepareSnapPosition(shpSelected, x, y,
				cursorPosInShpX, cursorPosOnShpY);
		if (snpRes==null || !snpRes.isSnappedX())
			shp.setX(x - cursorPosInShpX);
		if (snpRes==null || !snpRes.isSnappedY())
			shp.setY(y - cursorPosOnShpY);
		refreshDrawingArea();
	}

	/**
	 * Notifies all the Notifiess of a mouse click.
	 * 
	 * @param pos
	 *            position of the mouse click with respect to the panel's top
	 *            left
	 */
	private void notifyMouseClick(final Point pos) {
		for (final GfxPanelNotifiee notifiee : arrNotifiee)
			notifiee.mouseClicked(pos);
	}

	/**
	 * Notifies all Notifiees of the newly added shape.
	 * 
	 * @param shapeNumber
	 *            number of the new shape
	 */
	private void notifyShapeAdded(final int shapeNumber) {
		for (final GfxPanelNotifiee notifiee : arrNotifiee)
			notifiee.shapeAdded(shapeNumber);
	}

	/**
	 * Notifies all the Notifiees of the deleted shape.
	 * 
	 * @param shapeNumber
	 *            number of the deleted shape
	 */
	private void notifyShapeDeleted(final int shapeNumber) {
		for (final GfxPanelNotifiee notifiee : arrNotifiee)
			notifiee.shapeDeleted(shapeNumber);
	}

	/**
	 * Notifies all the Notifiees of the modified shape.
	 * 
	 * @param shapeNumber
	 *            number of the modified shape
	 */
	private void notifyShapeModified(final int shapeNumber) {
		for (final GfxPanelNotifiee notifiee : arrNotifiee)
			notifiee.shapeModified(shapeNumber);
	}

	/**
	 * Notifies all the Notifiees of the selected shape.
	 * 
	 * @param shapeNumber
	 *            number of the selected shape
	 */
	private void notifyShapeSelected(final int shapeNumber) {
		for (final GfxPanelNotifiee notifiee : arrNotifiee)
			notifiee.shapeSelected(shapeNumber);
	}

	/**
	 * Notifies the Notifiees about the dragging operation.
	 * 
	 * @param shpDragged
	 *            shape dragged
	 * @param shpDraggedOn
	 *            shape being dragged on
	 */
	private void notifyTargettedDragOperation(final Shape shpDragged,
			final Shape shpDraggedOn) {
		int dragtarget = -1;
		if (shpDraggedOn != null)
			dragtarget = shpDraggedOn.getShapeNumber();
		for (final GfxPanelNotifiee notifiee : arrNotifiee)
			notifiee.dragOccured(shpDragged.getShapeNumber(), dragtarget);
	}

	/**
	 * Redraws all the shapes.
	 */
	public void redrawAllShapes() {
		final int cleaningMargin = 30;
		int xLeft = 0, xRight = imgW, yUp = 0, yDown = imgH;
		if (resizingLeftX | resizingRightX | resizingDownY
				| resizingUpY | moving) {
			xLeft = shpSelected.getX() - cleaningMargin;
			xRight = shpSelected.getX() + shpSelected.getWidth()
					+ cleaningMargin;
			yUp = shpSelected.getY() - cleaningMargin;
			yDown = shpSelected.getY() + shpSelected.getHeight()
					+ cleaningMargin;
			if (xLeft < 0)
				xLeft = 0;
			if (xRight > bgBuffImg.getWidth())
				xRight = bgBuffImg.getWidth();
			if (yUp < 0)
				yUp = 0;
			if (yDown > bgBuffImg.getHeight())
				yDown = bgBuffImg.getHeight();
		}
		gfx.drawImage(bgBuffImg, xLeft, yUp, xRight + xLeft, yDown
				+ yUp, xLeft, yUp, xRight + xLeft, yDown + yUp, null);

		for (final Shape sh : shapes)
			sh.draw(gfx);
	}

	/**
	 * Refreshes the drawing area of the panel.
	 */
	public void refreshDrawingArea() {
		redrawAllShapes();
		if (shpSelected != null)
			draw4Corners(shpSelected, 5);
	}

	/**
	 * Registers a new Notifiee to receive notifications from us.
	 * 
	 * @param notifiee
	 *            object will be notified with events occurring to the panel
	 */
	public void registerForNotifications(final GfxPanelNotifiee notifiee) {
		arrNotifiee.add(notifiee);
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
	private void resizeShape(final Shape shp, final int x, final int y) {
		final int selX = shp.getX(), selY = shp.getY();

		final int w = shp.getWidth(), h = shp.getHeight();
		SnapResults snpRes;
		snpRes = snapper.prepareSnapSize(shpSelected, x, y);
		if (snpRes==null || !snpRes.isSnappedX()) {
			if (resizingRightX)
				shp.setWidth(x - selX);
			if (resizingLeftX) {
				shp.setX(x);
				shp.setWidth(w + selX - x);
			}
		}
		if (snpRes==null || !snpRes.isSnappedY()) {
			if (resizingDownY)
				shp.setHeight(y - selY);

			if (resizingUpY) {
				shp.setY(y);
				shp.setHeight(h + selY - y);
			}
		}
		refreshDrawingArea();
	}

	/**
	 * Selects a shape.
	 * 
	 * @param shapeNumber
	 *            number of the shape to select
	 */
	public void selectShape(final int shapeNumber) {
		shpSelected = getShapeByNumber(shapeNumber);
		refreshDrawingArea();
	}

	/**
	 * Sets the background of the panel to the given image.
	 * 
	 * @param bgData
	 *            new background image
	 */
	public void setBackground(final int[] bgData) {
		System.arraycopy(bgData, 0, bg, 0, bgData.length);
	}

	/**
	 * Enables/Disables snapping.
	 * 
	 * @param enableSnap
	 *            true/false
	 */
	public void setEnableSnap(final boolean enableSnap) {
		chkEnableSnap.setSelection(enableSnap);
		snapper.enableSnap(enableSnap);
	}

	/**
	 * Starts drawing a new shape.
	 * 
	 * @param shp
	 *            shape to start drawing
	 */
	public void startDrawingShape(final Shape shp) {
		shpToDraw = shp;
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
	private void updateNewShape(final Shape shp, final int x, final int y) {
		if (x > iniX)
			shp.setWidth(x - iniX);
		if (x < iniX) {
			shp.setX(x);
			shp.setWidth(iniX - x);
		}

		if (y > iniY)
			shp.setHeight(y - iniY);
		if (y < iniY) {
			shp.setY(y);
			shp.setHeight(iniY - y);
		}
	}

}
