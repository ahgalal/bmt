package control.ui;

import java.awt.Point;

import modules.ModulesManager;
import modules.zones.ZonesModule;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import ui.DrawZones;
import utils.PManager;
import control.ShapeController;

/**
 * Controller of the DrawZones GUI window.
 * 
 * @author Creative
 */
public class CtrlDrawZones extends ControllerUI
{

	private final DrawZones ui;

	/**
	 * Initializes class attributes (DrawZones , PManager and ZoneController)
	 * then gives the instance of GfxPanel to PManager to share it.
	 */
	public CtrlDrawZones()
	{
		pm = PManager.getDefault();
		ui = new DrawZones();
		ui.setController(this);
		pm.linkGFXPanelWithShapeCtrlr(ui.getGFXPanel());
	}

	/*
	 * (non-Javadoc)
	 * @see control.ui.ControllerUI#setVars(java.lang.String[])
	 */
	@Override
	public boolean setVars(final String[] objs)
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see control.ui.ControllerUI#show(boolean)
	 */
	@Override
	public void show(final boolean visibility)
	{
		ui.show(visibility);
	}

	/**
	 * Sends measure points selected by the user along with the real distance
	 * between the two points in reality to the StatsController.
	 * 
	 * @param measure_pnt1
	 *            First point that the user selected
	 * @param measure_pnt2
	 *            Second point that the user selected
	 * @param str_real_distance
	 *            Real distance between the two points
	 */
	public void sendScaletoStatsCtrlr(
			final Point measure_pnt1,
			final Point measure_pnt2,
			final String str_real_distance)
	{
		((ZonesModule) ModulesManager.getDefault().getModuleByName("Zones Module")).setScale(
				measure_pnt1,
				measure_pnt2,
				Integer.parseInt(str_real_distance));
	}

	/**
	 * Handles the "Load Zones" button click action.
	 */
	public void btn_load_zonesAction(final Shell shell)
	{
		final FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		final String file_name = fileDialog.open();
		if (file_name != null)
			((ZonesModule) ModulesManager.getDefault().getModuleByName("Zones Module")).loadZonesFromFile(file_name);
	}

	/**
	 * Sets the background of the GfxPanel.
	 * 
	 * @param img
	 *            new Background image
	 */
	public void setBackground(final int[] img)
	{
		ui.getGFXPanel().setBackground(img);
	}

	/**
	 * Handles the "Hide" button click action.
	 */
	public void btn_hide_Action()
	{
		((ZonesModule) ModulesManager.getDefault().getModuleByName("Zones Module")).updateZoneMap();
		show(false);
	}

	/**
	 * Handles the "Save Zones" button click action.
	 */
	public void btn_save_zones_Action(final Shell shell)
	{
		final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
		final String file_name = fileDialog.open();
		if (file_name != null)
			((ZonesModule) ModulesManager.getDefault().getModuleByName("Zones Module")).saveZonesToFile(file_name);
	}

	/**
	 * Gives the measure point to the GUI. The method call is originated in
	 * GfxPanel when the user clicks a new measure point.
	 * 
	 * @param pos
	 *            The new Point (x,y)
	 */
	public void addMeasurePoint(final Point pos)
	{
		ui.addMeasurePoint(pos);
	}

	/**
	 * Clears GUI table.
	 */
	public void clearTable()
	{
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
	public void editZoneDataInTable(
			final int zonenumber,
			final String color,
			final String zonetype)
	{
		ui.editZoneDataInTable(zonenumber, color, zonetype);
	}

	/**
	 * Adds the given zone to the GUI table.
	 * 
	 * @param zone_no
	 *            zone's number
	 * @param zone_col
	 *            zone's color
	 * @param zone_type
	 *            zone's type
	 */
	public void addZoneToTable(
			final String zone_no,
			final String zone_col,
			final String zone_type)
	{
		ui.addZoneToTable(zone_no, zone_col, zone_type);
	}

	/**
	 * Selects the zone having "zone_number" in the GUI table.
	 * 
	 * @param zone_number
	 *            number of the zone to select
	 */
	public void selectZoneInTable(final int zone_number)
	{
		ui.selectZoneInTable(zone_number);
	}

	/**
	 * Notifies the ShapeController that a "setting scale" operation is taking
	 * place, in order to start capturing measure points.
	 * 
	 * @param enable
	 */
	public void settingScale(final boolean enable)
	{
		ShapeController.getDefault().setSettingScale(enable);
	}

}
