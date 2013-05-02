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
import ui.FormZoneType;
import utils.PManager;

/**
 * Handles all shapes to be displayed on the GfxPanel.
 * 
 * @author Creative
 */
public class ShapeController implements GfxPanelNotifiee, ShapeCollection {
	private static ShapeController	defaultController;

	/**
	 * Gets the singleton instance.
	 * 
	 * @return singleton instance
	 */
	public static ShapeController getDefault() {
		if (defaultController == null)
			defaultController = new ShapeController();
		return defaultController;
	}

	private GfxPanel			gfxPanel;
	private final PManager		pm;
	private boolean				settingScale;
	private ArrayList<Shape>	shapes;

	/**
	 * just initialize the PManager instance.
	 */
	public ShapeController() {
		pm = PManager.getDefault();
	}

	/**
	 * adds the given shape to the arraylist of gfx_panel. notice that the
	 * arraylist used is the same instance used by gfx_panel, and that's how
	 * gfx_panel allows shapes to be added by code.
	 * 
	 * @param tmpRect
	 *            the shape to be added
	 */
	public void addShape(final Shape tmpRect) {
		shapes.add(tmpRect);
		gfxPanel.refreshDrawingArea();
	}

	/**
	 * Clears all shapes stored.
	 */
	public void clearAllShapes() {
		shapes.clear();
	}

	@Override
	public void dragOccured(final int draggedShape, final int draggedOnShape) {
	}

	/*
	 * (non-Javadoc)
	 * @see control.ShapeCollection#drawaAllShapes(java.awt.Graphics)
	 */
	@Override
	public void drawAllShapes(final Graphics gfx,double xScale,double yScale) {
		int i = 0;
		for (i = 0; i < this.getNumberOfShapes(); i++)
			this.shapes.get(i).draw(gfx,xScale,yScale);
	}

	/**
	 * Gets the number of shapes stored.
	 * 
	 * @return integer representing the number of shapes
	 */
	public int getNumberOfShapes() {
		return shapes.size();
	}

	/**
	 * @param index
	 *            the index of the shape in the arraylist
	 * @return the shape instance corresponding to the given index
	 */
	public Shape getShapeByIndex(final int index) {
		return shapes.get(index);
	}

	/**
	 * @param shapenumber
	 *            the number of the shape to return its instance
	 * @return instance of the shape having the given shapenumber
	 */
	public Shape getShapeByNumber(final int shapenumber) {
		return gfxPanel.getShapeByNumber(shapenumber);
	}

	/**
	 * in the ShapeController , we need to have a reference to the GfxPanel
	 * instance (from PManager) PManager calls this method to give the
	 * ShapeController the instance of GfxPanel.
	 * 
	 * @param gfxPanel
	 *            instance of GfxPanel class , instantiated in the
	 *            Ctrl_DrawZones class
	 */
	public void linkWithGFXPanel(final GfxPanel gfxPanel) {
		this.gfxPanel = gfxPanel;
		shapes = gfxPanel.getShapeArray();
		gfxPanel.registerForNotifications(this);
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.GfxPanelNotifiee#mouseClicked(java.awt.Point)
	 */
	@Override
	public void mouseClicked(final Point pos) {
		if (settingScale)
			pm.getDrawZns().addMeasurePoint(pos);
	}

	/**
	 * Notifies this object that a "setting scale" operation is taking place.
	 * 
	 * @param settingScale
	 *            true: yes, false: no
	 */
	public void setSettingScale(final boolean settingScale) {
		this.settingScale = settingScale;
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.GfxPanelNotifiee#shapeAdded(int)
	 */
	@Override
	public void shapeAdded(final int shapeNumber) {
		PManager.getDefault().displaySyncExec(new Runnable() {

			@Override
			public void run() {
				final FormZoneType frmZpneType = new FormZoneType();
				frmZpneType.open(shapeNumber);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.GfxPanelNotifiee#shapeDeleted(int)
	 */
	@Override
	public void shapeDeleted(final int shapeNumber) {
		((ZonesModule) ModulesManager.getDefault().getModuleByID(
				ZonesModule.moduleID)).deleteZone(shapeNumber);

	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.GfxPanelNotifiee#shapeModified(int)
	 */
	@Override
	public void shapeModified(final int shapeNumber) {
		((ZonesModule) ModulesManager.getDefault().getModuleByID(
				ZonesModule.moduleID)).updateZoneDataInGUI(shapeNumber);
	}

	/*
	 * (non-Javadoc)
	 * @see gfx_panel.GfxPanelNotifiee#shapeSelected(int)
	 */
	@Override
	public void shapeSelected(final int shapeNumber) {
		((ZonesModule) ModulesManager.getDefault().getModuleByID(
				ZonesModule.moduleID)).selectZoneInGUI(shapeNumber);
	}

	@Override
	public void drawAllShapes(Graphics gfx) {
		drawAllShapes(gfx, 1, 1);
	}

}
