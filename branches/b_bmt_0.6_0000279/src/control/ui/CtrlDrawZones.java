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

package control.ui;

import java.awt.Point;

import modules.zones.ZonesModule;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ui.DrawZones;
import utils.PManager;
import utils.StatusManager.StatusSeverity;

/**
 * Controller of the DrawZones GUI window.
 * 
 * @author Creative
 */
public class CtrlDrawZones extends ControllerUI<DrawZones> {

	private final ZonesModule	zonesModule;

	/**
	 * Initializes class attributes (DrawZones , PManager and ZoneController)
	 * then gives the instance of GfxPanel to PManager to share it.
	 */
	public CtrlDrawZones(final ZonesModule zonesModule) {
		ui = new DrawZones();
		ui.setController(this);
		this.zonesModule = zonesModule;
		zonesModule.getShapeController().linkWithGFXPanel(ui.getGFXPanel());
	}

	/**
	 * Gives the measure point to the GUI. The method call is originated in
	 * GfxPanel when the user clicks a new measure point.
	 * 
	 * @param pos
	 *            The new Point (x,y)
	 */
	public void addMeasurePoint(final Point pos) {
		ui.addMeasurePoint(pos);
	}

	/**
	 * Adds the given zone to the GUI table.
	 * 
	 * @param zoneNumber
	 *            zone's number
	 * @param zoneColor
	 *            zone's color
	 * @param zoneType
	 *            zone's type
	 */
	public void addZoneToTable(final String zoneNumber, final String zoneColor,
			final String zoneType) {
		ui.addZoneToTable(zoneNumber, zoneColor, zoneType);
	}

	/**
	 * Handles the "Hide" button click action.
	 */
	public void btnHideAction() {
		zonesModule.updateZoneMap();
		show(false);
	}

	/**
	 * Handles the "Load Zones" button click action.
	 * 
	 * @param shell
	 *            parent shell for the open dialog box
	 */
	public void btnLoadZonesAction(final Shell shell) {
		final FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		final String fileName = fileDialog.open();
		if (fileName != null)
			zonesModule.loadZonesFromFile(fileName);
	}

	/**
	 * Handles the "Save Zones" button click action.
	 * 
	 * @param shell
	 *            parent shell for the open dialog box
	 */
	public void btnSaveZonesAction(final Shell shell) {
		final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		final String fileName = fileDialog.open();
		if (fileName != null)
			zonesModule.saveZonesToFile(fileName);
	}

	/**
	 * Clears GUI table.
	 */
	public void clearTable() {
		ui.clearTable();
	}

	/**
	 * Updates zone data in the GUI table.
	 * 
	 * @param zonenumber
	 *            Number of the zone to update its information
	 * @param color
	 *            the new color of the zone
	 * @param zonetype
	 *            the new zone type
	 */
	public void editZoneDataInTable(final int zonenumber, final String color,
			final String zonetype) {
		ui.editZoneDataInTable(zonenumber, color, zonetype);
	}

	/**
	 * Selects the zone having "zoneNumber" in the GUI table.
	 * 
	 * @param zoneNumber
	 *            number of the zone to select
	 */
	public void selectZoneInTable(final int zoneNumber) {
		ui.selectZoneInTable(zoneNumber);
	}

	/**
	 * Sends measure points selected by the user along with the real distance
	 * between the two points in reality to the ZonesModule.
	 * 
	 * @param measurePnt1
	 *            First point that the user selected
	 * @param measurePnt2
	 *            Second point that the user selected
	 * @param strRealDistance
	 *            Real distance between the two points
	 */
	public void sendScaletoZonesModule(final Point measurePnt1,
			final Point measurePnt2, final String strRealDistance) {
		try {
			final int realDist = Integer.parseInt(strRealDistance);
			zonesModule.setScale(measurePnt1, measurePnt2, realDist);
		} catch (final NumberFormatException e) {
			PManager.getDefault()
					.getStatusMgr()
					.setStatus("Invalid distance, please try again.",
							StatusSeverity.ERROR);
		}
	}

	/**
	 * Sets the background of the GfxPanel.
	 * 
	 * @param img
	 *            new Background image
	 */
	public void setBackground(final int[] img, final int width, final int height) {
		ui.getGFXPanel().setBackground(img, width, height);
	}

	/**
	 * Notifies the ShapeController that a "setting scale" operation is taking
	 * place, in order to start capturing measure points.
	 * 
	 * @param enable
	 *            true/false
	 */
	public void settingScale(final boolean enable) {
		zonesModule.getShapeController().setSettingScale(enable);
	}

	/*
	 * (non-Javadoc)
	 * @see control.ui.ControllerUI#setVars(java.lang.String[])
	 */
	@Override
	public boolean setVars(final String[] objs) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see control.ui.ControllerUI#show(boolean)
	 */
	@Override
	public void show(final boolean visibility) {
		ui.show(visibility);
	}
}
