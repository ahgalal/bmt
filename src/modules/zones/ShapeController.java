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

package modules.zones;

import gfx_panel.GfxPanel;
import gfx_panel.GfxPanelNotifiee;
import gfx_panel.Shape;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import modules.ModulesManager;

import org.eclipse.swt.widgets.Display;

import ui.FormZoneType;
import utils.PManager;

/**
 * Handles all shapes to be displayed on the GfxPanel.
 * 
 * @author Creative
 */
public class ShapeController implements GfxPanelNotifiee, ShapeCollection
{
	static ShapeController default_controller;

	/**
	 * Notifies this object that a "setting scale" operation is taking place.
	 * 
	 * @param settingScale
	 *            true: yes, false: no
	 */
	public void setSettingScale(final boolean settingScale)
	{
		setting_scale = settingScale;
	}

	private GfxPanel gfx_panel;
	private ArrayList<Shape> shp_arr;
	private final PManager pm;
	private boolean setting_scale;

	/**
	 * Clears all shapes stored.
	 */
	public void clearAllShapes()
	{
		shp_arr.clear();
	}

	/**
	 * Gets the number of shapes stored.
	 * 
	 * @return integer representing the number of shapes
	 */
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

	/**
	 * Gets the singleton instance.
	 * 
	 * @return singleton instance
	 */
	public static ShapeController getDefault()
	{
		if (default_controller == null)
			default_controller = new ShapeController();
		return default_controller;
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
	 * @see gfx_panel.GfxPanelNotifiee#shapeAdded(int)
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
	 * @see gfx_panel.GfxPanelNotifiee#shapeModified(int)
	 */
	@Override
	public void shapeModified(final int shapeNumber)
	{
		((ZonesModule) ModulesManager.getDefault().getModuleByName("Zones Module")).updateZoneDataInGUI(shapeNumber);
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.GfxPanelNotifiee#shapeDeleted(int)
	 */
	@Override
	public void shapeDeleted(final int shapeNumber)
	{
		((ZonesModule) ModulesManager.getDefault().getModuleByName("Zones Module")).deleteZone(shapeNumber);

	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.GfxPanelNotifiee#shapeSelected(int)
	 */
	@Override
	public void shapeSelected(final int shapeNumber)
	{
		((ZonesModule) ModulesManager.getDefault().getModuleByName("Zones Module")).selectZoneInGUI(shapeNumber);
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
		this.gfx_panel = gfx_panel;
		shp_arr = gfx_panel.getShapeArray();
		gfx_panel.registerForNotifications(this);
	}

	/*
	 * (non-Javadoc)
	 * @see control.ShapeCollection#drawaAllShapes(java.awt.Graphics)
	 */
	@Override
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
	 * @see gfx_panel.GfxPanelNotifiee#mouseClicked(java.awt.Point)
	 */
	@Override
	public void mouseClicked(final Point pos)
	{
		if (setting_scale)
			pm.drw_zns.addMeasurePoint(pos);
	}

	@Override
	public void dragOccured(final int draggedShape, final int draggedOnShape)
	{
	}

}
