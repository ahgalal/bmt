package control;

import gfx_panel.GfxPanel;
import gfx_panel.GfxPanelNotifiee;
import gfx_panel.Shape;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import ui.FormZoneType;
import utils.PManager;

/**
 * @author Creative Handles all shapes to be displayed on the GfxPanel
 */
public class ShapeController implements GfxPanelNotifiee
{
	static ShapeController default_controller;

	public void setSettingScale(final boolean settingScale)
	{
		setting_scale = settingScale;
	}

	private GfxPanel gfx_panel;
	private ArrayList<Shape> shp_arr;
	private ZonesController zone_conteroller;
	private final PManager pm;
	private boolean setting_scale;

	public void clearAllShapes()
	{
		shp_arr.clear();
	}

	public int getNumberOfShapes()
	{
		return shp_arr.size();
	}

	/**
	 * just initialize the PManager instance.
	 */
	public ShapeController()
	{
		pm = PManager.getDefault();
	}

	/**
	 * @param shapenumber
	 *            the number of the shape to return its instance
	 * @return instance of the shape having the given shapenumber
	 */
	public Shape getShapeByNumber(final int shapenumber)
	{
		return gfx_panel.getShapeByNumber(shapenumber);
	}

	/**
	 * @param index
	 *            the index of the shape in the arraylist
	 * @return the shape instance corresponding to the given index
	 */
	public Shape getShapeByIndex(final int index)
	{
		return shp_arr.get(index);
	}

	public static ShapeController getDefault()
	{
		if (default_controller == null)
			default_controller = new ShapeController();
		return default_controller;
	}

	public void drawAllShapes()
	{
		gfx_panel.redrawAllShapes();
	}

	/**
	 * adds the given shape to the arraylist of gfx_panel. notice that the
	 * arraylist used is the same instance used by gfx_panel, and that's how
	 * gfx_panel allows shapes to be added by code.
	 * 
	 * @param tmpRect
	 *            the shape to be added
	 */
	public void addShape(final Shape tmpRect)
	{
		shp_arr.add(tmpRect);
		gfx_panel.refreshDrawingArea();
	}

	/*
	 * (non-Javadoc)
	 * @see ui.GfxPanel_Notifiee#shapeAdded(int) displays a "select zone type"
	 * dialog box
	 */
	@Override
	public void shapeAdded(final int shapeNumber)
	{
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run()
			{
				final FormZoneType frm_zn_type = new FormZoneType();
				frm_zn_type.open(shapeNumber);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ui.GfxPanel_Notifiee#shapeModified(int) calls zone_controller to
	 * update the modified shape's data in GUI data updated: color & zone type
	 */
	@Override
	public void shapeModified(final int shapeNumber)
	{
		zone_conteroller.updateZoneDataInGUI(shapeNumber);
	}

	@Override
	public void shapeDeleted(final int shapeNumber)
	{
		zone_conteroller.deleteZone(shapeNumber);

	}

	/*
	 * (non-Javadoc)
	 * @see ui.GfxPanel_Notifiee#shapeSelected(int) calls zone_controller to
	 * select the zone corresponding to the selected shape, in the GUI table
	 */
	@Override
	public void shapeSelected(final int shapeNumber)
	{
		zone_conteroller.selectZoneInGUI(shapeNumber);
	}

	/**
	 * in the ShapeController , we need to have a reference to the GfxPanel
	 * instance (from PManager) PManager calls this method to give the
	 * ShapeController the instance of GfxPanel.
	 * 
	 * @param gfx_panel
	 *            instance of GfxPanel class , instantiated in the
	 *            Ctrl_DrawZones class
	 */
	public void linkWithGFXPanel(final GfxPanel gfx_panel)
	{
		this.gfx_panel = PManager.getDefault().getGfxPanel();
		shp_arr = gfx_panel.getShapeArray();
		gfx_panel.registerForNotifications(this);
	}

	/**
	 * just get the instance of the ZoneController (Singleton).
	 */
	public void init()
	{
		zone_conteroller = ZonesController.getDefault();
	}

	public void drawaAllShapes(final Graphics gfx)
	{
		int i = 0;
		for (i = 0; i < this.getNumberOfShapes(); i++)
		{
			this.shp_arr.get(i).draw(gfx);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ui.GfxPanel_Notifiee#mouseClicked(java.awt.Point) used to record
	 * measure points when setting the scale in DrawZones GUI
	 */
	@Override
	public void mouseClicked(final Point pos)
	{
		if (setting_scale)
			pm.drw_zns.addMeasurePoint(pos);
	}

	/*
	 * (non-Javadoc)
	 * @see ui.GfxPanel_Notifiee#dragOccured(int, int) not implemented
	 */
	@Override
	public void dragOccured(final int draggedShape, final int draggedOnShape)
	{
		// pm.log.print("Shape: " +draggedShape+ " is Dragged on the Shape: "
		// +draggedOnShape ,this);
	}

}
